package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.util.PageData;
import com.xsy.sys.dao.SysConfigDao;
import com.xsy.sys.entity.BaseKey;
import com.xsy.sys.entity.RefreshConfigEvent;
import com.xsy.sys.entity.SysConfigEntity;
import com.xsy.sys.enums.SysConfigValueTypeEnum;
import com.xsy.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.PropertyPlaceholderHelper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Q1sj
 * @date 2022.9.20 14:12
 */
@Slf4j
@Service
@CacheConfig(cacheNames = SysConfigServiceImpl.CACHE_NAME)
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfigEntity> implements SysConfigService {
    public static final String CACHE_NAME = "sys_config";
    private final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}", ":", false);

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SysConfigService _this;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#entity.configKey")
    public boolean saveOrUpdate(SysConfigEntity entity) {
        if (entity.getConfigValueType() == null) {
            entity.setConfigValueType(SysConfigValueTypeEnum.STRING.name());
        }
        SysConfigValueTypeEnum configValueTypeEnum = null;
        try {
            configValueTypeEnum = SysConfigValueTypeEnum.valueOf(entity.getConfigValueType());
        } catch (IllegalArgumentException e) {
            throw new GlobalException(entity.getConfigValueType() + "不存在", e);
        }
        if (!configValueTypeEnum.valid(entity.getConfigValue())) {
            throw new GlobalException(entity.getConfigValueType() + "类型转换失败");
        }
        entity.setUpdateTime(new Date());
        boolean saveOrUpdate = super.saveOrUpdate(entity);
        applicationContext.publishEvent(new RefreshConfigEvent(entity.getConfigKey(), entity.getConfigValue()));
        return saveOrUpdate;
    }

    @Override
    @CacheEvict(key = "#key")
    public boolean saveOrUpdate(String key, String value) {
        SysConfigEntity sysConfig = new SysConfigEntity();
        sysConfig.setConfigKey(key);
        sysConfig.setConfigValue(value);
        return saveOrUpdate(sysConfig);
    }

    @Override
    @CacheEvict(key = "#key")
    public boolean saveOrUpdate(String key, Number value) {
        SysConfigEntity sysConfig = new SysConfigEntity();
        sysConfig.setConfigKey(key);
        if (value instanceof BigDecimal) {
            sysConfig.setConfigValue(((BigDecimal) value).toPlainString());
        } else {
            sysConfig.setConfigValue(Objects.toString(value));
        }
        if (value instanceof Integer) {
            sysConfig.setConfigValueType(SysConfigValueTypeEnum.INT.name());
        } else if (value instanceof Long) {
            sysConfig.setConfigValueType(SysConfigValueTypeEnum.LONG.name());
        } else {
            sysConfig.setConfigValueType(SysConfigValueTypeEnum.STRING.name());
        }
        return saveOrUpdate(sysConfig);
    }

    @Override
    @CacheEvict(key = "#key")
    public boolean saveOrUpdate(String key, Boolean value) {
        SysConfigEntity sysConfig = new SysConfigEntity();
        sysConfig.setConfigKey(key);
        sysConfig.setConfigValueType(SysConfigValueTypeEnum.BOOLEAN.name());
        sysConfig.setConfigValue(Objects.toString(value));
        return saveOrUpdate(sysConfig);
    }

    @Override
    @CacheEvict(key = "#key")
    public boolean saveOrUpdate(String key, SysConfigValueTypeEnum valueType, String value) {
        SysConfigEntity sysConfig = new SysConfigEntity();
        sysConfig.setConfigKey(key);
        sysConfig.setConfigValue(value);
        sysConfig.setConfigValueType(valueType.name());
        return saveOrUpdate(sysConfig);
    }

    @Nullable
    @Cacheable(key = "#key")
    public String get(String key) {
        SysConfigEntity entity = this.getById(key);
        if (entity == null) {
            return null;
        }
        return propertyPlaceholderHelper.replacePlaceholders(entity.getConfigValue(), k -> {
            if (Objects.equals(k, key)) {
                throw new IllegalArgumentException("key:" + key);
            }
            return get(k);
        });
    }

    @Override
    public String getOriginal(String key) {
        SysConfigEntity entity = this.getById(key);
        if (entity == null) {
            return null;
        }
        return entity.getConfigValue();
    }

    @Override
    public <T> T get(BaseKey<T> key) {
        String k = key.getKey();
        String valStr = _this.get(k);
        return key.deserialization(valStr);
    }

    @Override
    @CacheEvict(key = "#key")
    public void delete(String key) {
        log.warn("参数管理删除 key:{}", key);
        removeById(key);
    }

    @Override
    public List<SysConfigEntity> list() {
        return super.list();
    }

    @Override
    public PageData<SysConfigEntity> list(@Nullable String configKey, int page, int pageSize) {
        LambdaQueryWrapper<SysConfigEntity> wrapper = Wrappers.lambdaQuery(SysConfigEntity.class)
                .like(StringUtils.isNotBlank(configKey), SysConfigEntity::getConfigKey, configKey);
        IPage<SysConfigEntity> iPage = new Page<>(page, pageSize);
        this.page(iPage, wrapper);
        return new PageData<>(iPage);
    }

    @Override
    public List<SysConfigEntity> startWith(String keyPrefix) {
        return this.list(Wrappers.lambdaQuery(SysConfigEntity.class)
                .likeRight(SysConfigEntity::getConfigKey, keyPrefix));
    }
}
