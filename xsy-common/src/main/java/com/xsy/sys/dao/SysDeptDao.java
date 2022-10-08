/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xsy.sys.dao;


import com.xsy.base.dao.RenBaseDao;
import com.xsy.sys.entity.SysDeptEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysDeptDao extends RenBaseDao<SysDeptEntity> {

    List<SysDeptEntity> getList(Map<String, Object> params);

    SysDeptEntity getById(Long id);

    /**
     * 获取所有部门的id、pid列表
     */
    List<SysDeptEntity> getIdAndPidList();

    /**
     * 根据部门ID，获取所有子部门ID列表
     * @param id   部门ID
     */
    List<Long> getSubDeptIdList(String id);

}
