package com.xsy.sys.service;

import com.xsy.base.util.PageData;
import com.xsy.sys.entity.BaseKey;
import com.xsy.sys.entity.SysConfigEntity;
import com.xsy.sys.enums.SysConfigValueTypeEnum;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author Q1sj
 * @date 2022.9.22 8:47
 */
public interface SysConfigService {
    boolean saveOrUpdate(SysConfigEntity entity);

    boolean saveOrUpdate(String key, String value);

    boolean saveOrUpdate(String key, Number value);

    boolean saveOrUpdate(String key, Boolean value);

    boolean saveOrUpdate(String key, SysConfigValueTypeEnum valueType, String value);

    @Nullable
    String get(String key);

    /**
     * 获取原始值 未进行${}替换的值
     *
     * @param key
     * @return
     */
    @Nullable
    String getOriginal(String key);

    <T> T get(BaseKey<T> key);

    /**
     * 只删除数据库中数据,代码中字段仍保持不变
     * 只删除数据库中数据,代码中字段仍保持不变
     * 只删除数据库中数据,代码中字段仍保持不变
     *
     * @param key
     */
    void delete(String key);

    List<SysConfigEntity> list();

    PageData<SysConfigEntity> list(@Nullable String configKey, int page, int pageSize);

    /**
     * 根据key前缀查询
     *
     * @param keyPrefix
     * @return
     */
    List<SysConfigEntity> startWith(String keyPrefix);
}
