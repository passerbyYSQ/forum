package top.ysqorz.forum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.dto.resp.WeekTopPostDTO;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.service.PostService;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.DateTimeUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author passerbyYSQ
 * @create 2021-06-02 13:21
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
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
        return redisTemplate.opsForValue().setIfAbsent(key, "", Constant.DURATION_POST_VISIT);
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

    /**
     * 当服务宕机时导致在线长连接关闭，Redis中的对应缓存没有扣减，导致数量偏大，这个可以允许，只能通过重建缓存来纠正
     */
    @Override
    public void bindWsServer(ChannelType channelType, String groupId) {
        int randMinutes = RandomUtils.generateInt(30);
        // 次日凌晨两点多
        LocalDateTime expireTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(2, randMinutes));
        long millis = Duration.between(LocalDateTime.now(), expireTime).toMillis();
        String lua = "redis.call('hincrby', KEYS[1], KEYS[2], 1) " +
                "if (redis.call('ttl', KEYS[1]) == -1) then " +
                "redis.call('pexpire', KEYS[1], ARGV[1]) end";
        DefaultRedisScript<Void> script = new DefaultRedisScript<>(lua, Void.class);
        String key = String.format(Constant.REDIS_KEY_IM_WS, channelType.name(), groupId);
        String hashKey = IMUtils.getWebServer();
        // 使用RedisTemplate进行incrby操作会报错：ERR value is not an integer or out of range
        // https://blog.csdn.net/weixin_42829048/article/details/83989784
        stringRedisTemplate.execute(script, Arrays.asList(key, hashKey), String.valueOf(millis));
    }

    /**
     * 当缓存过期后，在线长连接才关闭，会导致缓存出现负数。因此为0时不能扣减，需要补偿纠正
     */
    @Override
    public void removeWsServer(ChannelType channelType, String groupId) {
        // 使用字符串拼接lua脚本时，注意每一行后加空格隔开
        String lua = "local val = redis.call('hget', KEYS[1], KEYS[2]) " +
                "if (val and tonumber(val) > 0) then " +
                "redis.call('hset', KEYS[1], KEYS[2], tonumber(val) - 1) " +
                "return true " +
                "else " +
                "return false " +
                "end";
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>(lua, Boolean.class);
        String key = String.format(Constant.REDIS_KEY_IM_WS, channelType.name(), groupId);
        String hashKey = IMUtils.getWebServer();
        Boolean res = redisTemplate.execute(script, Arrays.asList(key, hashKey));
        if (Boolean.FALSE.equals(res)) {
            log.info("二级Map {} --> {} 为0时出现扣减", key, hashKey);
        }
    }

    @Override
    public Set<String> getWsServers(ChannelType channelType, String groupId) {
        String key = String.format(Constant.REDIS_KEY_IM_WS, channelType.name(), groupId);
        Map<Object, Object> hash = stringRedisTemplate.opsForHash().entries(key); // 使用 redisTemplate 反序列化报错
        return hash.entrySet().stream().filter(entry -> Integer.parseInt((String) entry.getValue()) > 0)
                .map(entry -> (String) entry.getKey())
                .collect(Collectors.toSet());
    }

    @Override
    public Boolean recordIpAccessAPI(String key, Integer maxCount, Duration duration, Duration blackDuration) {
        String lua = "redis.call('set', KEYS[1], ARGV[1], 'NX', 'PX', ARGV[2]) " +
                "local remained = tonumber(redis.call('decr', KEYS[1])) " +
                "if (remained < 0) then " +
                "return false " +
                "else " +
                "if (remained == 0) then " +
                "redis.call('pexpire', KEYS[1], ARGV[3]) " +
                "end " +
                "return true " +
                "end";
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>(lua, Boolean.class);
        return stringRedisTemplate.execute(script, Collections.singletonList(key), String.valueOf(maxCount),
                String.valueOf(duration.toMillis()), String.valueOf(blackDuration.toMillis()));
    }

    @Override
    public boolean isUserOnline(Integer userId) {
        String key = String.format(Constant.REDIS_KEY_IM_WS, ChannelType.CHAT.name(), userId);
        Map<Object, Object> hash = stringRedisTemplate.opsForHash().entries(key); // 使用 redisTemplate 反序列化报错
        boolean isOnline = false;
        if (!ObjectUtils.isEmpty(hash)) {
            for (Map.Entry<Object, Object> entry : hash.entrySet()) {
                int wsCount = Integer.parseInt((String) entry.getValue());
                if (wsCount > 0) {
                    isOnline = true;
                    break;
                }
            }
        }
        return isOnline;
    }

}
