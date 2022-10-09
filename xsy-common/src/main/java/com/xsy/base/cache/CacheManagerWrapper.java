package com.xsy.base.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link org.springframework.cache.CacheManager}包装类
 * {@link #getCache(String)}返回包装类
 *
 * @author Q1sj
 */
@Component
public class CacheManagerWrapper {

    private final CacheManager cacheManager;

    private final Map<String, CacheWrapper> cacheMap = new ConcurrentHashMap<>(16);

    public CacheManagerWrapper(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheWrapper getCache(String name) {
        return cacheMap.computeIfAbsent(name, k -> {
            Cache cache = cacheManager.getCache(name);
            if (cache == null) {
                return null;
            }
            return new CacheWrapper(cache);
        });
    }

    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }
}
