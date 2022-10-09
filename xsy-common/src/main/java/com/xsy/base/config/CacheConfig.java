package com.xsy.base.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
@Slf4j
@Component
@Configuration
public class CacheConfig {
    /**
     * 缓存过期天数
     */
    @Value("${cache.expire:1}")
    private Integer expire;
    @Value(("${cache.expireTimeUnit:HOUR}"))
    private String expireTimeUnit;
    /**
     * 缓存最大大小
     */
    @Value("${cache.maxSize:10000}")
    private Long cacheMaxSize;

    /**
     * 配置缓存管理器
     *
     * @return 缓存管理器
     */
    @Bean("cacheManager")
    public CacheManager cacheManager() {
        TimeUnit timeUnit = null;
        try {
            timeUnit = TimeUnit.valueOf(expireTimeUnit);
        } catch (IllegalArgumentException e) {
            timeUnit = TimeUnit.HOURS;
            expire = 1;
            log.warn("expireTimeUnit:{}无效 使用默认过期时间{} {}", expireTimeUnit, expire, timeUnit.name());
        }
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterAccess(expire, timeUnit)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(cacheMaxSize));
        return cacheManager;
    }
}
