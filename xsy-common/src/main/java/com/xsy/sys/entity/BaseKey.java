package com.xsy.sys.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class BaseKey<T> {
    private final String key;
    private T defaultValue;

    public String serialization(T val) {
        return val != null ? serializationNotNull(val) : null;
    }

    public T deserialization(String val) {
        return val != null ? deserializationNotNull(val) : null;
    }

    protected abstract String serializationNotNull(T val);

    protected abstract T deserializationNotNull(String val);

    public BaseKey(String key) {
        this.key = key;
    }

    public BaseKey(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseKey<?> that = (BaseKey<?>) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, defaultValue);
    }
}
