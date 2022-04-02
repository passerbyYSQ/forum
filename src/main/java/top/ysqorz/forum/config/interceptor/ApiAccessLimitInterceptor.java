package top.ysqorz.forum.config.interceptor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.utils.CommonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

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
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = CommonUtils.getIpFromRequest(request);
        String accessKey = "API:" + request.getServletPath() + ":" + ip;
        String remained = stringRedisTemplate.opsForValue().get(accessKey);
        if (remained != null && Integer.parseInt(remained) <= 0) {
            String msg = "访问" + request.getServletPath() + "过于频繁，已被拦截";
            // 此处无法被GlobalExceptionHandler所捕获，但能被SpringBoot的默认异常处理所捕获
            // throw new AuthorizationException(msg);
            CommonUtils.writeJson(response, ResultModel.failed(403, msg));
            return false;
        }
        // 60为剩余次数，过期时间为1分钟。如果已经设置，不会重复set
        stringRedisTemplate.opsForValue()
                .setIfAbsent(accessKey, "60", Duration.ofMinutes(1));
        // 次数-1
        stringRedisTemplate.opsForValue().decrement(accessKey, 1);
        return true;
    }
}
