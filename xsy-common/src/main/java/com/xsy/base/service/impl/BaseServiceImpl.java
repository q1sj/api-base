package com.xsy.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.service.BaseService;
import com.xsy.base.util.PageData;

/**
 * @author Q1sj
 * @date 2022.8.29 16:16
 */

public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {
    public PageData<T> page(Wrapper<T> wrapper, int page, int pageSize) {
        return new PageData<>(page(new Page<>(page, pageSize), wrapper));
    }
}
