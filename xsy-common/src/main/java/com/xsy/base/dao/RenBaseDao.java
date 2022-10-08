/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xsy.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基础Dao
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Deprecated
public interface RenBaseDao<T> extends BaseMapper<T> {
    @Override
    @Deprecated
    List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);
}
