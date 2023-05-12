package com.xsy.sys.service;

import com.xsy.base.util.PageData;
import com.xsy.sys.entity.SysConfigEntity;
import org.springframework.lang.Nullable;

/**
 * @author Q1sj
 * @date 2022.9.22 8:47
 */
public interface SysConfigService {
    boolean saveOrUpdate(SysConfigEntity entity);

    @Nullable
    String get(String key);

    /**
     * 只删除数据库中数据,代码中字段仍保持不变
     * 只删除数据库中数据,代码中字段仍保持不变
     * 只删除数据库中数据,代码中字段仍保持不变
     *
     * @param key
     */
    void delete(String key);

    PageData<SysConfigEntity> list(@Nullable String configKey, int page, int pageSize);
}
