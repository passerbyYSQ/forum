package top.ysqorz.forum.shiro.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author passerbyYSQ
 * @create 2021-05-29 16:09
 */
@Slf4j
public class ShiroCache<K, V> implements Cache<K, V> {

    // CacheManager注意导包
    private CacheManager springCacheManager;
    private String cacheName;

    private org.springframework.cache.Cache springCache;

    public ShiroCache(CacheManager springCacheManager, String cacheName) {
        this.springCacheManager = springCacheManager;
        this.cacheName = cacheName;
        this.springCache = springCacheManager.getCache(cacheName);
    }

    @Override
    public V get(K key) throws CacheException {
        log.info("获取缓存，key：{}", key);
        org.springframework.cache.Cache.ValueWrapper valueWrapper = springCache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return (V) valueWrapper.get();
    }

    @Override
    public V put(K key, V value) throws CacheException {
        log.info("放入缓存，key：{}，value：{}", key, value);
        springCache.put(key, value);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        log.info("移除缓存，key：{}", key);
        V oldValue = this.get(key);
        springCache.evict(key);
        return oldValue;
    }

    @Override
    public void clear() throws CacheException {
        log.info("清空缓存，cacheName：{}", cacheName);
        springCache.clear();
    }

    @Override
    public int size() {
        log.info("缓存数量：{}", this.keys().size());
        return this.keys().size();
    }

    @Override
    public Set<K> keys() {
        Collection<String> cacheNameSet = springCacheManager.getCacheNames();
        log.info("缓存key(CacheName)的集合：{}", String.join(",", cacheNameSet));
        return (Set<K>) cacheNameSet;
    }

    @Override
    public Collection<V> values() {
        List<V> values = this.keys().stream().map(this::get).collect(Collectors.toList());
        log.info("缓存value的集合：{}", String.join(",", values.toString()));
        return values;
    }
}
