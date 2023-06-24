package top.ysqorz.forum.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.cache.CacheManager;

import java.util.Collection;
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
        org.springframework.cache.Cache.ValueWrapper valueWrapper = springCache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return (V) valueWrapper.get();
    }

    @Override
    public V put(K key, V value) throws CacheException {
        springCache.put(key, value);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        V oldValue = this.get(key);
        springCache.evict(key);
        return oldValue;
    }

    @Override
    public void clear() throws CacheException {
        springCache.clear();
    }

    @Override
    public int size() {
        return this.keys().size();
    }

    @Override
    public Set<K> keys() {
        Collection<String> cacheNameSet = springCacheManager.getCacheNames();
        return (Set<K>) cacheNameSet;
    }

    @Override
    public Collection<V> values() {
        return this.keys().stream().map(this::get).collect(Collectors.toList());
    }
}
