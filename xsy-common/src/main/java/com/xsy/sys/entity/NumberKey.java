package com.xsy.sys.entity;

import java.math.BigDecimal;

/**
 * 使用redis缓存
 * + {@link org.springframework.cache.annotation.Cacheable}
 * + {@link org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer}
 * <p>
 * 方法返回值为Long 反序列化小于{@link Integer#MAX_VALUE}的数会变为Integer类型导致类型转换异常 java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Long
 * 考虑使用Number类型解决
 */
public class NumberKey extends BaseKey<Number> {

    public NumberKey(String key) {
        super(key);
    }

    public NumberKey(String key, Number defaultValue) {
        super(key, defaultValue);
    }

    @Override
    protected String serializationNotNull(Number val) {
        return val.toString();
    }

    @Override
    protected Number deserializationNotNull(String val) {
        return new BigDecimal(val);
    }
}