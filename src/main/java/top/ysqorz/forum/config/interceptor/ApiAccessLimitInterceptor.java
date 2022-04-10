package top.ysqorz.forum.config.interceptor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.CommonUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * TODO 通过拦截器和redis实现简单的API防刷，待优化
 *
 * 同一个ip在1分钟内调用同个一个API接口超过60次，认为非正常请求，
 * 该请求在当天内无法再次调用该API
 *
 * @author passerbyYSQ
 * @create 2021-07-09 22:01
 */
@Component
public class ApiAccessLimitInterceptor implements HandlerInterceptor {
    @Resource
    private RedisService redisService;
    private Cache<String, Object> blackCache;

    @PostConstruct
    public void postConstruct() {
        blackCache = CacheBuilder.newBuilder()
                .maximumSize(2000L)
                .expireAfterWrite(1L, TimeUnit.HOURS) // 注意与Redis缓存中的黑名单时间一致
                .build();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = CommonUtils.getIpFromRequest(request);
        String path = request.getServletPath();
        String key = "API:" + path + ":" + ip;
        // 加一层本地缓存，如果被封禁的IP刷接口，会将压力阻挡到集群节点，降低Redis缓存的压力
        if (blackCache.getIfPresent(key) != null) {
            refuse(ip, path, response);
            return false;
        }
        // 同一个IP对于同一个接口，1分钟允许最多访问60次。超过后，1小时内禁止该IP访问该接口，但该IP访问其他接口正常
        Boolean isPassed = redisService.recordIpAccessAPI(key, 60, Duration.ofMinutes(1), Duration.ofHours(1));
        if (Boolean.FALSE.equals(isPassed)) {
            blackCache.put(key, "");
            refuse(ip, path, response);
            return false;
        }
        return true;
    }

    private void refuse(String ip, String path, HttpServletResponse response) {
        String msg = String.format("当前ip（%s）访问接口（%s）过于频繁，已被暂时封禁", ip, path);
        // 此处无法被GlobalExceptionHandler所捕获，但能被SpringBoot的默认异常处理所捕获
        // throw new AuthorizationException(msg);
        CommonUtils.writeJson(response, ResultModel.failed(403, msg));
    }
}
