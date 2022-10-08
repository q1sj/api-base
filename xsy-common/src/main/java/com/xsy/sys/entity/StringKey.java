package com.xsy.sys.entity;

public class StringKey extends BaseKey<String> {

    public StringKey(String key) {
        super(key);
    }

    public StringKey(String key, String defaultValue) {
        super(key, defaultValue);
    }

    @Override
    protected String serializationNotNull(String val) {
        return val;
    }

    @Override
    protected String deserializationNotNull(String val) {
        return val;
    }
}
