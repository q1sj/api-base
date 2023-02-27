package com.xsy.base.cache;

import com.xsy.sys.entity.BaseKey;
import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * {@link org.springframework.cache.Cache}包装类
 * 使用{@link BaseKey}作为key泛型限制value类型
 *
 * @author Q1sj
 */
public class CacheWrapper implements Cache {
    private final Cache cache;

    public CacheWrapper(Cache cache) {
        this.cache = cache;
    }

    public <T> void put(BaseKey<T> key, @Nullable T val) {
        cache.put(key.getKey(), val);
    }

    /**
     * 获取
     *
     * @param key
     * @param <T>
     * @return
     */
    @Nullable
    public <T> T get(BaseKey<T> key) {
        Cache.ValueWrapper valueWrapper = cache.get(key.getKey());
        if (valueWrapper == null || valueWrapper.get() == null) {
            return null;
        }
        // 修复redis Jackson2JsonRedisSerializer 序列化long 反序列化变为int
        if (valueWrapper.get() instanceof Number) {
            return key.deserialization(valueWrapper.get().toString());
        }
        return (T) valueWrapper.get();
    }

    public <T> Optional<T> getOptional(BaseKey<T> key) {
        return Optional.ofNullable(get(key));
    }

    @Nullable
    public <T> T get(BaseKey<T> key, Callable<T> valueLoader) {
        return cache.get(key.getKey(), valueLoader);
    }

    /**
     * 删除
     *
     * @param key
     */
    public void evict(BaseKey<?> key) {
        cache.evict(key.getKey());
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public Object getNativeCache() {
        return cache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return cache.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return cache.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return cache.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        cache.evict(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
