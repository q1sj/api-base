package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.PageData;
import com.xsy.sys.dao.SysConfigDao;
import com.xsy.sys.entity.RefreshConfigEvent;
import com.xsy.sys.entity.SysConfigEntity;
import com.xsy.sys.enums.SysConfigValueTypeEnum;
import com.xsy.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Objects;

/**
 * @author Q1sj
 * @date 2022.9.20 14:12
 */
@Slf4j
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfigEntity> implements SysConfigService {

    private final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}", ":", false);

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(SysConfigEntity entity) {
        if (entity.getConfigValueType() == null) {
            entity.setConfigValueType(SysConfigValueTypeEnum.STRING.name());
        }
        boolean saveOrUpdate = super.saveOrUpdate(entity);
        applicationContext.publishEvent(new RefreshConfigEvent(entity.getConfigKey()));
        return saveOrUpdate;
    }

    @Nullable
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
    public void delete(String key) {
        log.warn("参数管理删除 key:{}", key);
        removeById(key);
    }

    @Override
    public PageData<SysConfigEntity> list(@Nullable String configKey, int page, int pageSize) {
        LambdaQueryWrapper<SysConfigEntity> wrapper = Wrappers.lambdaQuery(SysConfigEntity.class)
                .like(StringUtils.isNotBlank(configKey), SysConfigEntity::getConfigKey, configKey);
        IPage<SysConfigEntity> iPage = new Page<>(page, pageSize);
        this.page(iPage, wrapper);
        return new PageData<>(iPage);
    }
}
