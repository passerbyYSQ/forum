package top.ysqorz.forum.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.service.RedisService;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author passerbyYSQ
 * @create 2021-06-02 13:21
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveCaptcha(String key, String captcha) {
        redisTemplate.opsForValue().set(Constant.REDIS_PREFIX_CAPTCHA + key,
                captcha, Duration.ofMinutes(5));
    }

    @Override
    public String getCaptcha(String key) {
        key = Constant.REDIS_PREFIX_CAPTCHA + key;
        String correctCaptcha = (String) redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return correctCaptcha;
    }
}
