package top.ysqorz.forum.config.cache;

import cn.hutool.core.util.ReflectUtil;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author passerbyYSQ
 * @create 2023-06-24 21:31
 */
public class PlusCaffeineCacheManager extends CaffeineCacheManager {
    protected boolean dynamic1; // 缓存父类标记
    protected final Map<String, Cache> cacheMap1; // 缓存父类引用，内存地址都指向同一块

    @SuppressWarnings("unchecked")
    public PlusCaffeineCacheManager(String... cacheNames) {
        super(cacheNames);
        // 缓存父类的属性
        this.dynamic1 = (boolean) ReflectUtil.getFieldValue(this, "dynamic");
        this.cacheMap1 = (Map<String, Cache>) ReflectUtil.getFieldValue(this, "cacheMap");
    }

    @Override
    public void setCacheNames(@Nullable Collection<String> cacheNames) {
        if (ObjectUtils.isEmpty(cacheNames)) {
            setDynamic(true);
        } else {
            for (String name : cacheNames) {
                this.cacheMap1.put(name, createCaffeineCache(name));
            }
            setDynamic(false);
        }
    }

    /**
     * 暴露dynamic的修改
     */
    public void setDynamic(boolean dynamic) {
        this.dynamic1 = dynamic;
        // 同步父类
        ReflectUtil.setFieldValue(this, "dynamic", dynamic);
    }

    public Cache getCache(String name, Caffeine<Object, Object> caffeine) {
        Cache cache = this.cacheMap1.get(name);
        if (Objects.isNull(cache) && this.dynamic1) {
            synchronized (this.cacheMap1) {
                cache = this.cacheMap1.get(name);
                if (Objects.isNull(cache)) {
                    cache = createCaffeineCache(name, caffeine);
                    this.cacheMap1.put(name, cache);
                }
            }
        }
        return cache;
    }

    @SuppressWarnings("unchecked")
    protected Cache createCaffeineCache(String name, Caffeine<Object, Object> caffeine) {
        Object cacheLoader = ReflectUtil.getFieldValue(this, "cacheLoader");
        return new CaffeineCache(name,
                Objects.isNull(cacheLoader) ?
                        caffeine.build() :
                        caffeine.build((CacheLoader<Object, Object>) cacheLoader),
                isAllowNullValues());
    }
}
