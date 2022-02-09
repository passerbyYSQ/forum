package top.ysqorz.forum.config;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ysqorz.forum.utils.DateTimeUtils;
import top.ysqorz.forum.utils.IpUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

/**
 * 通过拦截器和redis实现简单的API防刷
 *
 * 同一个ip在1分钟内调用同个一个API接口超过60次，认为非正常请求，
 * 该请求在当天内无法再次调用任何API
 *
 * @author passerbyYSQ
 * @create 2021-07-09 22:01
 */
@Component
public class ApiAccessLimitInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 判断IP是否在黑名单中
        String ip = IpUtils.getIpFromRequest(request);
        String blackListKey = "blackList:" + DateTimeUtils.getFormattedDate();
        if (stringRedisTemplate.opsForSet().isMember(blackListKey, ip)) { // 黑名单(set)不存在也会返回false
            // 如果在黑名单中，直接拦截
            throw new AuthorizationException("当前IP已被列入黑名单，5分钟后请重试");
        }

        String accessKey = "API:" + request.getServletPath() + ":" + ip;
        // 60为剩余次数，过期时间为1分钟。如果已经设置，不会重复set
        stringRedisTemplate.opsForValue()
                .setIfAbsent(accessKey, "60", Duration.ofMinutes(1));
        // 次数-1
        Long remained = stringRedisTemplate.opsForValue().decrement(accessKey, 1);
        if (remained < 0) {
            // 加入黑名单
            stringRedisTemplate.opsForSet().add(blackListKey, ip);
            if (stringRedisTemplate.getExpire(blackListKey) == -1) { // 黑名单还没设置过期时间
                stringRedisTemplate.expire(blackListKey, Duration.ofMinutes(5));
            }
        }
        return true;
    }


}
