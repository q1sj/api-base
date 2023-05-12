package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.sys.dao.SysDictionaryDao;
import com.xsy.sys.entity.SysDictionaryEntity;
import com.xsy.sys.service.SysDictionaryService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Q1sj
 * @date 2023.4.23 16:20
 */
@Service
public class SysDictionaryServiceImpl extends ServiceImpl<SysDictionaryDao, SysDictionaryEntity> implements SysDictionaryService {
    public List<SysDictionaryEntity> values(String type) {
        LambdaQueryWrapper<SysDictionaryEntity> wrapper = Wrappers.lambdaQuery(SysDictionaryEntity.class)
                .eq(SysDictionaryEntity::getType, type)
                .orderByAsc(SysDictionaryEntity::getSort);
        return this.list(wrapper);
    }

    @Nullable
    public SysDictionaryEntity getByCode(String type, String code) {
        LambdaQueryWrapper<SysDictionaryEntity> wrapper = Wrappers.lambdaQuery(SysDictionaryEntity.class)
                .eq(SysDictionaryEntity::getType, type)
                .eq(SysDictionaryEntity::getCode, code);
        return this.getOne(wrapper);
    }

    @Override
    public SysDictionaryEntity getByValue(String type, String value) {
        LambdaQueryWrapper<SysDictionaryEntity> wrapper = Wrappers.lambdaQuery(SysDictionaryEntity.class)
                .eq(SysDictionaryEntity::getType, type)
                .eq(SysDictionaryEntity::getValue, value);
        return this.getOne(wrapper);
    }
}
