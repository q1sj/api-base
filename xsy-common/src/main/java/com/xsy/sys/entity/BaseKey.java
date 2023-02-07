package com.xsy.sys.entity;

import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Objects;

@Getter
public abstract class BaseKey<T> {
    private final String key;
    @Nullable
    private T defaultValue;

    public BaseKey(String key) {
        this.key = key;
    }

    public BaseKey(String key, @Nullable T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     * val对象转换为string
     *
     * @param val
     * @return
     */
    @Nullable
    public String serialization(@Nullable T val) {
        return val != null ? serializationNotNull(val) : null;
    }

    /**
     * string转换为对象
     *
     * @param val
     * @return
     */
    @Nullable
    public T deserialization(@Nullable String val) {
        return val != null ? deserializationNotNull(val) : null;
    }

    /**
     * val对象转换为string val不为空
     *
     * @param val
     * @return
     */
    protected abstract String serializationNotNull(T val);

    /**
     * string转换为对象 val不为空
     *
     * @param val
     * @return
     */
    protected abstract T deserializationNotNull(String val);

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

    @Override
    public String toString() {
        return "BaseKey{" +
                "key='" + key + '\'' +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
