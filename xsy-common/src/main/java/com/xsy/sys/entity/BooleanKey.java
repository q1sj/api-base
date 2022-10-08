package com.xsy.sys.entity;

public class BooleanKey extends BaseKey<Boolean> {

    public BooleanKey(String key) {
        super(key);
    }

    public BooleanKey(String key, Boolean defaultValue) {
        super(key, defaultValue);
    }

    @Override
    protected String serializationNotNull(Boolean val) {
        return val != null ? val.toString() : null;
    }

    @Override
    protected Boolean deserializationNotNull(String val) {
        return Boolean.valueOf(val);
    }
}
