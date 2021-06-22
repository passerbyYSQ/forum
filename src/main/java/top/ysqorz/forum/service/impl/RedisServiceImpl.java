package top.ysqorz.forum.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.service.RedisService;

import javax.annotation.Resource;

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
        redisTemplate.opsForValue()
                .set(Constant.REDIS_PREFIX_CAPTCHA + key,
                    captcha, Constant.DURATION_CAPTCHA);
    }

    @Override
    public String getCaptcha(String key) {
        key = Constant.REDIS_PREFIX_CAPTCHA + key;
        String correctCaptcha = (String) redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return correctCaptcha;
    }

    @Override
    public void saveOauthState(String key, String salt) {
        redisTemplate.opsForValue()
                .set(Constant.REDIS_PREFIX_OAUTH_STATE + key,
                    salt, Constant.DURATION_OAUTH_STATE);
    }

    @Override
    public String getOauthState(String key) {
        key = Constant.REDIS_PREFIX_OAUTH_STATE + key;
        // 错误的key或者key过期，都会返回null
        String salt = (String) redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return salt;
    }

    @Override
    public boolean tryAddPostVisitIp(String ipAddress, Integer postId) {
        String key = String.format(Constant.REDIS_PREFIX_POST_VISIT, postId, ipAddress);
        return redisTemplate.opsForValue()
                .setIfAbsent(key, "", Constant.DURATION_POST_VISIT);
    }
}
