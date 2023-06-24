package top.ysqorz.forum.config.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.annotation.ApiAccessLimit;
import top.ysqorz.forum.config.cache.PlusCaffeineCacheManager;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.CommonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

/**
 * 通过拦截器、本地缓存、redis实现简单的API防刷
 *
 * @author passerbyYSQ
 * @create 2021-07-09 22:01
 */
@Component
@Slf4j
public class ApiAccessLimitInterceptor implements HandlerInterceptor {
    @Resource
    private RedisService redisService;
    private PlusCaffeineCacheManager blackCacheManager = new PlusCaffeineCacheManager();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取频控参数
        int maxCount = 60;
        Duration duration = Duration.ofMinutes(1);
        Duration blackDuration = Duration.ofDays(1);
        ApiAccessLimit apiAccessLimit = findApiAccessLimit(handler);
        if (!ObjectUtils.isEmpty(apiAccessLimit)) {
            maxCount = apiAccessLimit.maxCount();
            duration = Duration.of(apiAccessLimit.duration(), apiAccessLimit.unit());
            blackDuration = Duration.of(apiAccessLimit.blackDuration(), apiAccessLimit.blackUnit());
        }

        // 加一层黑名单本地缓存，如果被封禁的IP刷接口，会将压力阻挡到集群节点，降低Redis缓存的压力
        String clientIP = ServletUtil.getClientIP(request);
        String servletPath = request.getServletPath();
        // 获取这个servletPath的黑名单，如果没有则自动创建，内部已经确保了创建动作的并发安全
        Cache blackCache = blackCacheManager.getCache(servletPath,
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(blackDuration)
        );
        Integer userID = blackCache.get(clientIP, Integer.class); // 动态创建，cache不可能为null
        if (!ObjectUtils.isEmpty(userID)) {
            refuse(clientIP, servletPath, response);
            return false;
        }

        // 维护Redis缓存计数器
        String key = "API:" + servletPath + ":" + clientIP;
        // 同一个IP对于同一个接口，1分钟允许最多访问60次。超过后，1小时内禁止该IP访问该接口，但该IP访问其他接口正常
        Boolean isPassed = redisService.recordIpAccessAPI(key, maxCount, duration, blackDuration);
        if (Boolean.FALSE.equals(isPassed)) {
            // 不存在才放入。尝试获取UserID，如果未登录获取不到
            blackCache.putIfAbsent(key, ShiroUtils.getUserId());
            refuse(clientIP, servletPath, response);
            return false;
        }
        return true;
    }

    public ApiAccessLimit findApiAccessLimit(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return null;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ApiAccessLimit apiAccessLimit = handlerMethod.getMethodAnnotation(ApiAccessLimit.class);
        if (!ObjectUtils.isEmpty(apiAccessLimit)) {
            return apiAccessLimit;
        }
        return AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), ApiAccessLimit.class);
    }

    private void refuse(String ip, String path, HttpServletResponse response) {
        String msg = String.format("当前ip(%s)访问接口(%s)过于频繁，已被暂时封禁！当前登录用户ID：%s", ip, path, ShiroUtils.getUserId());
        log.error(msg);
        // 此处无法被GlobalExceptionHandler所捕获，但能被SpringBoot的默认异常处理所捕获
        // throw new AuthorizationException(msg);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        CommonUtils.writeJson(response, ResultModel.failed(HttpStatus.FORBIDDEN.value(), msg));
    }
}
