/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.base.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 总条数
     */
    private int total;
    /**
     * 当前页数据
     */
    private List<T> list;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageData(List<T> list, long total) {
        this.list = list;
        this.total = (int) total;
    }

    public PageData(IPage<T> iPage) {
        this.list = iPage.getRecords();
        this.total = (int) iPage.getTotal();
    }

    public <R> PageData<R> convert(Function<T, R> function) {
        List<R> newList = list.stream().map(function).collect(Collectors.toList());
        return new PageData<>(newList, total);
    }
}
