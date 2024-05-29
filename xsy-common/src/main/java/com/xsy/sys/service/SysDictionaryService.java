package com.xsy.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsy.sys.entity.SysDictionaryEntity;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author Q1sj
 * @date 2023.4.23 16:11
 */
public interface SysDictionaryService extends IService<SysDictionaryEntity> {
    List<SysDictionaryEntity> values(String type);

    @Nullable
    SysDictionaryEntity getByCode(String type, String code);

    SysDictionaryEntity getByValue(String type, String value);
}
