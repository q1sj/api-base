package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.PageData;
import com.xsy.sys.dao.SysConfigDao;
import com.xsy.sys.entity.BaseKey;
import com.xsy.sys.entity.SysConfigEntity;
import com.xsy.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * @author Q1sj
 * @date 2022.9.20 14:12
 */
@Slf4j
@Service
@CacheConfig(cacheNames = SysConfigServiceImpl.CACHE_NAME)
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfigEntity> implements SysConfigService {

    static final String CACHE_NAME = "sys_config";

    @Override
    public PageData<SysConfigEntity> list(String configKey, int page, int pageSize) {
        LambdaQueryWrapper<SysConfigEntity> wrapper = Wrappers.lambdaQuery(SysConfigEntity.class)
                .like(StringUtils.isNotBlank(configKey), SysConfigEntity::getConfigKey, configKey);
        IPage<SysConfigEntity> iPage = new Page<>(page, pageSize);
        this.page(iPage, wrapper);
        return new PageData<>(iPage);
    }

    @Override
    @CacheEvict(key = "#key.getKey()")
    public <T> void put(BaseKey<T> key, T val) {
        saveOrUpdate(new SysConfigEntity(key.getKey(), key.serialization(val)));
    }

    @Override
    @CacheEvict(key = "#sysConfigEntity.getConfigKey()")
    public void put(SysConfigEntity sysConfigEntity) {
        saveOrUpdate(sysConfigEntity);
    }

    @Override
    @Cacheable(key = "#key.getKey()")
    public <T> T get(BaseKey<T> key) {
        log.debug("select {}", key);
        SysConfigEntity entity = getById(key.getKey());
        return entity != null ? key.deserialization(entity.getConfigValue()) : key.getDefaultValue();
    }

    @Override
    @Cacheable(key = "#key.getKey()")
    public <T> T get(BaseKey<T> key, Supplier<T> valueLoad) {
        T val = get(key);
        if (val != null) {
            return val;
        }
        synchronized (key) {
            val = get(key);
            if (val != null) {
                return val;
            }
            val = valueLoad.get();
            put(key, val);
            return val;
        }
    }

    @Override
    @CacheEvict(key = "#key.getKey()")
    public void del(BaseKey<?> key) {
        removeById(key.getKey());
    }
}
