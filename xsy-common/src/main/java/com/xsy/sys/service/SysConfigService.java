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

    void saveOrUpdate(String key, String value);

    @Nullable
    String get(String key);

    void delete(String key);

    PageData<SysConfigEntity> list(@Nullable String configKey, int page, int pageSize);
}
