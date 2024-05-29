package com.xsy.sys.entity;

public class LongKey extends BaseKey<Long> {

    public LongKey(String key) {
        super(key);
    }

    public LongKey(String key, Long defaultValue) {
        super(key, defaultValue);
    }

    @Override
    protected String serializationNotNull(Long val) {
        return val.toString();
    }

    @Override
    protected Long deserializationNotNull(String val) {
        return Long.valueOf(val);
    }
}
