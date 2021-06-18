package top.ysqorz.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.utils.RandomUtils;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * redis实现分布式锁的方案
 *
 * @author passerbyYSQ
 * @create 2021-06-15 22:48
 */
@Slf4j
@Controller
@ResponseBody
@RequestMapping("/test")
public class RedisDistributedLock {

    @Autowired
    private RedisTemplate<String, String> redisTemplate; // !!!

    private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    @RequestMapping("/lua")
    public String lua() throws InterruptedException {
        redisTemplate.opsForValue().set("test", "123");
        String script = " local lockOwner = redis.call('get', KEYS[1]) " +
                        " if lockOwner ~= nil and lockOwner == ARGV[1] then " + // 序列化的问题
                            " redis.call('expire', KEYS[1], 30) " +
                            " return '1' " +
                        " else " +
                            " return '0' " +
                        " end ";
        redisTemplate.execute(new DefaultRedisScript<>(script),
                Collections.singletonList("test"), "123");
        return "设置成功";
    }

    @RequestMapping("/lock")
    public String secondKill() {
        // 商品product_001的锁
        String lockKey = "lock_product_001";
        String clientId = RandomUtils.generateUUID();
        try {
            Boolean res = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, clientId, 30, TimeUnit.SECONDS);
            if (!res) { // 获取锁失败
                return "抢购失败，请重试";
            }
            // 获取锁失败
            watchDog(lockKey, clientId);
            // --------------------------------

            // 抢购业务，模拟减库存
            int stock = Integer.parseInt(redisTemplate.opsForValue().get("stock_product_001"));
            if (stock <= 0) {
                return "已经没有库存了";
            }
            stock--; // 减库存
            redisTemplate.opsForValue().set("stock_product_001", stock + "");

            // --------------------------------

            log.info("库存成功扣减1成功，当前剩余库存：{}", stock);
            return "库存成功扣减1成功";
        } finally {
            // 谁加的锁谁释放。但是仍没有解决 业务操作的耗时可能比锁过期时间长，造成锁提前释放的问题
            // 改成lua脚本
            String script = " local lockOwner = redis.call('get', KEYS[1]) " +
                            " if lockOwner ~= nil and lockOwner == ARGV[1] then " + // 序列化的问题
                                " redis.call('del', KEYS[1]) " +
                                " return '1' " +
                            " else " +
                                " return '0' " +
                            " end ";
            redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                    Collections.singletonList(lockKey), clientId);
//            if (clientId.equals(redisTemplate.opsForValue().get(lockKey))) {
//                redisTemplate.delete(lockKey);
//            }
        }

    }

    public void watchDog(String lockKey, String clientId) {
        String script = " local lockOwner = redis.call('get', KEYS[1]) " +
                        " if lockOwner ~= nil and lockOwner == ARGV[1] then " + // 序列化的问题
                            " redis.call('expire', KEYS[1], 30) " +
                            " return '1' " +
                        " else " +
                            " return '0' " +
                        " end ";
        scheduledExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                // 10s后查看锁是否存在。
                String res = redisTemplate.execute(new DefaultRedisScript<>(script, String.class),
                        Collections.singletonList(lockKey), clientId);
                if ("1".equals(res)) {
                    scheduledExecutor.schedule(this, 10, TimeUnit.SECONDS);
                }

//                String lockOwner = (String) redisTemplate.opsForValue().get(lockKey);
//                if (lockOwner != null && lockOwner.equals(clientId)) {
//                    redisTemplate.expire(lockKey, 30, TimeUnit.SECONDS); // 续命
//                    // 开启下一次延时
//                    scheduledExecutor.schedule(this, 10, TimeUnit.SECONDS);
//                }
                // 如果不存在，说明业务已完成，已被正常释放。终止看门狗。不再续命
                // 不可能是锁过期的情况。因为锁检查续命的间隔是10s，而锁过期是30s
            }
        }, 10, TimeUnit.SECONDS);
    }


}
