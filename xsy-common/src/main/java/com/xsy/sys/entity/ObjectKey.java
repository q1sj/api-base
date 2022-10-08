package com.xsy.sys.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xsy.base.util.JsonUtils;

import java.util.Objects;

public class ObjectKey<T> extends BaseKey<T> {
    private Class<T> clazz;
    private TypeReference<T> typeReference;

    public ObjectKey(String key, Class<T> clazz) {
        super(key);
        this.clazz = clazz;
    }

    public ObjectKey(String key, T defaultValue, Class<T> clazz) {
        super(key, defaultValue);
        this.clazz = clazz;
    }

    public ObjectKey(String key, T defaultValue, TypeReference<T> typeReference) {
        super(key, defaultValue);
        this.typeReference = typeReference;
    }

    @Override
    protected String serializationNotNull(T val) {
        return JsonUtils.toJsonString(val);
    }

    @Override
    protected T deserializationNotNull(String val) {
        if (typeReference != null) {
            return JsonUtils.parseObject(val, typeReference);
        }
        return JsonUtils.parseObject(val, clazz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectKey<?> that = (ObjectKey<?>) o;
        return Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clazz);
    }
}
