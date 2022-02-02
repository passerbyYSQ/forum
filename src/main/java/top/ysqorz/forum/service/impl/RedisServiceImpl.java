package top.ysqorz.forum.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.dto.WeekTopPostDTO;
import top.ysqorz.forum.service.PostService;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.DateTimeUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2021-06-02 13:21
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private PostService postService;

    @Override
    public void saveCaptcha(String key, String captcha) {
        redisTemplate.opsForValue()
                .set(Constant.REDIS_KEY_CAPTCHA + key,
                    captcha, Constant.DURATION_CAPTCHA);
    }

    @Override
    public String getCaptcha(String key) {
        key = Constant.REDIS_KEY_CAPTCHA + key;
        String correctCaptcha = (String) redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return correctCaptcha;
    }

    @Override
    public void saveOauthState(String key, String salt) {
        redisTemplate.opsForValue()
                .set(Constant.REDIS_KEY_OAUTH_STATE + key,
                    salt, Constant.DURATION_OAUTH_STATE);
    }

    @Override
    public String getOauthState(String key) {
        key = Constant.REDIS_KEY_OAUTH_STATE + key;
        // 错误的key或者key过期，都会返回null
        String salt = (String) redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return salt;
    }

    @Override
    public boolean tryAddPostVisitIp(String ipAddress, Integer postId) {
        String key = String.format(Constant.REDIS_KEY_POST_VISIT, postId, ipAddress);
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

    @Override
    public void addHotPostWeekRankScore(Integer postId) {
        // 帖子访问量的周榜。key：post:hot_rank:2020-28
        String key = "post:hot_rank:" + DateTimeUtils.getFormattedWeek();
        // 如果没有 zset，会先创建
        redisTemplate.opsForZSet().incrementScore(key, postId, 1);
        if (redisTemplate.getExpire(key) == -1) { // 秒数。-1：不过期
            redisTemplate.expire(key, DateTimeUtils.durationToNextWeek());
        }
    }

    @Override
    public List<WeekTopPostDTO> hostPostWeekRankTop(Integer count) {
        // Redis Zset使用方法：https://blog.csdn.net/weixin_45216439/article/details/91390743
        String key = "post:hot_rank:" + DateTimeUtils.getFormattedWeek();
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet =
                redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, count - 1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator =
                typedTupleSet.iterator();
        List<WeekTopPostDTO> list = new ArrayList<>();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            // 全局配置了二级key的序列化为string。集合内的类型不一样，不能直接强转，只能先抹除泛型
            Object value = typedTuple.getValue();
            Integer score = typedTuple.getScore().intValue();
            WeekTopPostDTO post = new WeekTopPostDTO();
            post.setPostId((Integer) value);
            post.setViews(score);
            post.setTitle(postService.getPostById(post.getPostId()).getTitle());
            list.add(post);
        }
        return list;
    }

    @Override
    public Boolean isHaveWeekHotPost() {
        String key = "post:hot_rank:" + DateTimeUtils.getFormattedWeek();
        Set<Object> set = redisTemplate.opsForZSet().range(key, 0, 1);
        return set != null && set.size() != 0;
    }

    @Override
    public void bindUserIdToWsIp(Integer userId, String wsIp) {
        redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_USER_WS + userId.toString(), wsIp);
    }

    @Override
    public void removeUserIdToWsIp(Integer userId) {
        redisTemplate.delete(Constant.REDIS_KEY_USER_WS + userId.toString());
    }

    @Override
    public String getUserWs(Integer userId) {
        return (String) redisTemplate.opsForValue().get(Constant.REDIS_KEY_USER_WS + userId.toString());
    }


}
