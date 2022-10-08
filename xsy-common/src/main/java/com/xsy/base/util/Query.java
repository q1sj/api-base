package com.xsy.base.util;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Q1sj
 * @date 2022.8.30 16:43
 */
public class Query<T> {
    /**
     * 查询条件集合
     */
    private List<QueryCondition<T>> conditions = new ArrayList<>();

    public Query<T> eq(SFunction<T, Object> columnName, Object val) {
        return eq(true, columnName, val);
    }

    public Query<T> eq(boolean condition, SFunction<T, Object> columnName, Object val) {
        return add(QueryType.eq, condition, columnName, val);
    }


    public Query<T> not(SFunction<T, Object> columnName, Object val) {
        return not(true, columnName, val);
    }

    public Query<T> not(boolean condition, SFunction<T, Object> columnName, Object val) {
        return add(QueryType.not, condition, columnName, val);
    }

    public Query<T> gt(SFunction<T, Object> columnName, Object val) {
        return gt(true, columnName, val);
    }

    public Query<T> gt(boolean condition, SFunction<T, Object> columnName, Object val) {
        return add(QueryType.gt, condition, columnName, val);
    }

    public Query<T> gte(SFunction<T, Object> columnName, Object val) {
        return gte(true, columnName, val);
    }

    public Query<T> gte(boolean condition, SFunction<T, Object> columnName, Object val) {
        return add(QueryType.gte, condition, columnName, val);
    }

    public Query<T> lt(SFunction<T, Object> columnName, Object val) {
        return lt(true, columnName, val);
    }

    public Query<T> lt(boolean condition, SFunction<T, Object> columnName, Object val) {
        return add(QueryType.lt, condition, columnName, val);
    }

    public Query<T> lte(SFunction<T, Object> columnName, Object val) {
        return lte(true, columnName, val);
    }

    public Query<T> lte(boolean condition, SFunction<T, Object> columnName, Object val) {
        return add(QueryType.lte, condition, columnName, val);
    }

    public Query<T> in(SFunction<T, Object> columnName, Collection<?> val) {
        return in(true, columnName, val);
    }

    public Query<T> in(boolean condition, SFunction<T, Object> columnName, Collection<?> val) {
        return add(QueryType.in, condition, columnName, val);
    }

    public Query<T> orderByDesc(SFunction<T, Object> columnName) {
        return add(QueryType.orderByDesc, true, columnName, null);
    }

    public Query<T> like(SFunction<T, Object> columnName, Object val) {
        return like(true, columnName, val);
    }

    public Query<T> like(boolean condition, SFunction<T, Object> columnName, Object val) {
        return add(QueryType.like, condition, columnName, val);
    }

    private Query<T> add(QueryType queryType, boolean condition, SFunction<T, Object> columnName, Object val) {
        if (condition) {
            conditions.add(new QueryCondition<>(queryType, columnName, val));
        }
        return this;
    }

    public List<Query.QueryCondition<T>> getConditions() {
        // copy
        return new ArrayList<>(conditions);
    }


    /**
     * 查询条件
     *
     * @param <T>
     */
    @Getter
    public static class QueryCondition<T> {
        private final QueryType queryType;
        private final SFunction<T, Object> columnName;
        private final Object val;

        private QueryCondition(QueryType queryType, SFunction<T, Object> columnName, Object val) {
            this.queryType = queryType;
            this.columnName = columnName;
            this.val = val;
        }
    }

    /**
     * 查询类型
     */
    public enum QueryType {
        // TODO
        eq, not, gt, gte, lt, lte, in, orderByAsc, orderByDesc, like, likeRight
    }
}
