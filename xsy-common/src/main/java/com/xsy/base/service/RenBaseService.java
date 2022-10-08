/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xsy.base.service;

import java.io.Serializable;
import java.util.Collection;

/**
 * 基础服务接口，所有Service接口都要继承
 *
 * @author Mark sunlightcs@gmail.com
 */
@Deprecated
public interface RenBaseService<T> {
    /**
     * <p>
     * 插入一条记录（选择字段，策略插入）
     * </p>
     *
     * @param entity 实体对象
     * @return
     */
    boolean insert(T entity);

    /**
     * <p>
     * 插入（批量），该方法不支持 Oracle、SQL Server
     * </p>
     *
     * @param entityList 实体对象集合
     * @return
     */
    boolean insertBatch(Collection<T> entityList);

    /**
     * <p>
     * 根据 ID 选择修改
     * </p>
     *
     * @param entity 实体对象
     * @return
     */
    boolean updateById(T entity);

    /**
     * <p>
     * 根据ID 批量更新
     * </p>
     *
     * @param entityList 实体对象集合
     * @return
     */
    boolean updateBatchById(Collection<T> entityList);

    /**
     * <p>
     * 根据 ID 查询
     * </p>
     *
     * @param id 主键ID
     * @return
     */
    T selectById(Serializable id);

    /**
     * <p>
     * 根据 ID 删除
     * </p>
     *
     * @param id 主键ID
     * @return
     */
    boolean deleteById(Serializable id);

    /**
     * <p>
     * 删除（根据ID 批量删除）
     * </p>
     *
     * @param idList 主键ID列表
     * @return
     */
    boolean deleteBatchIds(Collection<? extends Serializable> idList);
}
