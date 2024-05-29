/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.service.impl.RenBaseServiceImpl;
import com.xsy.sys.dao.SysRoleMenuDao;
import com.xsy.sys.entity.SysRoleMenuEntity;
import com.xsy.sys.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 角色与菜单对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysRoleMenuServiceImpl extends RenBaseServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色菜单关系
        deleteByRoleIds(new Long[]{roleId});

        //角色没有一个菜单权限的情况
        if (CollectionUtils.isEmpty(menuIdList)) {
            return;
        }

        //保存角色菜单关系
        for (Long menuId : menuIdList) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);

            //保存
            insert(sysRoleMenuEntity);
        }
    }

    @Override
    public List<Long> getMenuIdList(Long roleId) {
        return baseDao.getMenuIdList(roleId);
    }

    @Override
    public List<Long> getMenuIdList(List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Collections.emptyList();
        }
        List<SysRoleMenuEntity> sysRoleMenuList = baseDao.selectList(Wrappers.lambdaQuery(SysRoleMenuEntity.class)
                .in(SysRoleMenuEntity::getRoleId, roleIdList));
        return sysRoleMenuList.stream().map(SysRoleMenuEntity::getMenuId).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIds(Long[] roleIds) {
        baseDao.deleteByRoleIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByMenuId(Long menuId) {
        baseDao.deleteByMenuId(menuId);
    }

}
