package com.xsy.base.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置
 *
 * @author Q1sj
 */
@Slf4j
@Component
@Configuration
public class CacheConfig {
    /**
     * 缓存过期时间
     */
    @Value("${cache.expire:1}")
    private Integer expire;
    /**
     * 缓存过期时间单位
     */
    @Value("${cache.expireTimeUnit:HOURS}")
    private String expireTimeUnit;
    /**
     * 缓存最大大小
     */
    @Value("${cache.maxSize:10000}")
    private Long cacheMaxSize;

    /**
     * Caffeine
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager caffeineCacheManager() {
        log.info("init caffeineCacheManager...");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterAccess(expireMs(), TimeUnit.MILLISECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(cacheMaxSize));
        return cacheManager;
    }

    /**
     * redis
     */
    @Configuration
    @ConditionalOnClass(RedisCacheManager.class)
    class RedisConfig {
        @Bean
        public CacheManager redisCacheManager(ObjectProvider<RedisConnectionFactory> factory) {
            log.info("init redisCacheManager...");
            RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                    // 设置缓存过期时间
                    .entryTtl(Duration.ofMillis(expireMs()))
                    // 设置CacheManager的值序列化方式为json序列化，可使用加入@Class属性
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                            new GenericJackson2JsonRedisSerializer()
                    ));
            // 使用RedisCacheConfiguration创建RedisCacheManager
            return RedisCacheManager.builder(Objects.requireNonNull(factory.getIfAvailable()))
                    .cacheDefaults(cacheConfiguration)
                    .build();
        }
    }

    /**
     * 缓存过期时间
     */
    private long expireMs() {
        TimeUnit timeUnit = null;
        try {
            timeUnit = TimeUnit.valueOf(expireTimeUnit);
        } catch (IllegalArgumentException e) {
            timeUnit = TimeUnit.HOURS;
            expire = 1;
            log.warn("expireTimeUnit:{}无效 使用默认过期时间{} {}", expireTimeUnit, expire, timeUnit.name());
        }
        return timeUnit.toMillis(expire);
    }
}
