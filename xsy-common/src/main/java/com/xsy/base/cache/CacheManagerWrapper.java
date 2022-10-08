package com.xsy.base.cache;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * {@link org.springframework.cache.CacheManager}包装类
 * {@link #getCache(String)}返回包装类
 *
 * @author Q1sj
 */
@Component
public class CacheManagerWrapper {

    private final CacheManager cacheManager;

    public CacheManagerWrapper(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheWrapper getCache(String name) {
        return new CacheWrapper(cacheManager.getCache(name));
    }

    public Collection<String> getCacheNames(){
        return cacheManager.getCacheNames();
    }
}
