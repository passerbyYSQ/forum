package top.ysqorz.forum.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.DateTimeUtils;

import javax.annotation.Resource;
import java.util.Set;

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

    @Override
    public void addHotPostDayRankScore(Integer postId) {
        // 帖子访问量的日榜。key：post:hot_rank:2020-07-02
        String key = "post:hot_rank:" + DateTimeUtils.getFormattedDate();
        // 如果没有 zset，会先创建
        redisTemplate.opsForZSet().incrementScore(key, postId, 1);
        if (redisTemplate.getExpire(key) == -1) { // 秒数。-1：不过期
            redisTemplate.expire(key, DateTimeUtils.durationToNextDay());
        }
    }

    @Override
    public Set<Integer> hostPostDayRankTop(Integer count) {
        String key = "post:hot_rank:" + DateTimeUtils.getFormattedDate();
        Set<Object> topPostIds = redisTemplate.opsForZSet()
                .reverseRange(key, 0, count - 1);
        // 全局配置了二级key的序列化为string。集合内的类型不一样，不能直接强转，只能先抹除泛型
        return (Set) topPostIds;
    }

}
