package com.xsy.sys.entity;

import com.xsy.base.util.JsonUtils;

import java.util.List;
import java.util.Objects;

public class ListKey<T> extends BaseKey<List<T>> {

    private final Class<T> clazz;

    public ListKey(String key, Class<T> clazz) {
        super(key);
        this.clazz = clazz;
    }

    public ListKey(String key, List<T> defaultValue, Class<T> clazz) {
        super(key, defaultValue);
        this.clazz = clazz;
    }

    @Override
    protected String serializationNotNull(List<T> val) {
        return JsonUtils.toJsonString(val);
    }

    @Override
    protected List<T> deserializationNotNull(String val) {
        return JsonUtils.parseArray(val, clazz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ListKey<?> that = (ListKey<?>) o;
        return Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clazz);
    }
}
