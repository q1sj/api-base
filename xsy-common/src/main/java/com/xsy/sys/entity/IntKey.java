package com.xsy.sys.entity;

public class IntKey extends BaseKey<Integer> {

    public IntKey(String key) {
        super(key);
    }

    public IntKey(String key, Integer defaultValue) {
        super(key, defaultValue);
    }

    @Override
    protected String serializationNotNull(Integer val) {
        return val.toString();
    }

    @Override
    protected Integer deserializationNotNull(String val) {
        return Integer.valueOf(val);
    }
}
