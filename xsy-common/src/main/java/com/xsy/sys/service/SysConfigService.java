package com.xsy.sys.service;

import com.xsy.base.util.PageData;
import com.xsy.sys.entity.BaseKey;
import com.xsy.sys.entity.SysConfigEntity;

/**
 * @author Q1sj
 * @date 2022.9.22 8:47
 */
public interface SysConfigService {

    PageData<SysConfigEntity> list(String configKey,int page,int pageSize);

    /**
     * 新增或修改
     *
     * @param key
     * @param val
     * @param <T>
     */
    <T> void put(BaseKey<T> key, T val);
    void put(SysConfigEntity sysConfigEntity);

    /**
     * 获取
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> T get(BaseKey<T> key);

    /**
     * 删除
     *
     * @param key
     */
    void del(BaseKey<?> key);
}
