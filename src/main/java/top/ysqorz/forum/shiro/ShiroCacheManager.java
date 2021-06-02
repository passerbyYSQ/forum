package top.ysqorz.forum.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * @author passerbyYSQ
 * @create 2021-05-29 16:09
 */
public class ShiroCacheManager implements CacheManager { // CacheManager注意导包

    private org.springframework.cache.CacheManager springCacheManager;

    public ShiroCacheManager(org.springframework.cache.CacheManager springCacheManager) {
        this.springCacheManager = springCacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        return new ShiroCache<>(springCacheManager, cacheName);
    }
}
