package top.ysqorz.forum.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.ysqorz.forum.common.Constant;

/**
 * redis配置类
 */
@Configuration
@EnableCaching // 开启SpringCache注解
public class CacheConfig extends CachingConfigurerSupport {

    /**
     * 配置自己的RedisTemplate。
     * 1、不配置beanName，默认以方法名作为beanName
     * 2、查看RedisAutoConfiguration自动配置类，发现：只要我们添加了名为"redisTemplate"的bean，那么
     * 自动配置类的bean就不会被装配
     * 3、因为springboot2.0中默认是使用Lettuce来集成Redis服务，spring-boot-starter-data-redis默认
     * 只引入了Lettuce连接池的实现，并没有引入jedis连接池的实现
     *
     * @param factory 注入Redis连接池。有两个实现类，注入的是：LettuceConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        StringRedisSerializer stringSerializer = stringSerializer();

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(stringSerializer);
        // template.setValueSerializer(genericToStringSerializer);
        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(stringSerializer);
        // template.setHashValueSerializer(genericToStringSerializer);

        // 其他采用默认序列化方式（jdk）
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public CacheManager springCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Constant.DURATION_JWT) // 7 天
                .disableCachingNullValues();
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

    // string序列化
    public StringRedisSerializer stringSerializer() {
        return new StringRedisSerializer();
    }

    public GenericToStringSerializer<Object> genericToStringSerializer() {
        return new GenericToStringSerializer<>(Object.class);
    }
}
