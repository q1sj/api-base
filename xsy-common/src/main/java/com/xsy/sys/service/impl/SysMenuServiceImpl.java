/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.service.impl;

import com.xsy.base.enums.RenConstant;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.service.impl.RenBaseServiceImpl;
import com.xsy.base.util.ConvertUtils;
import com.xsy.base.util.TreeUtils;
import com.xsy.security.config.RequiresPermissionsScan;
import com.xsy.security.enums.SecurityConstant;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.dao.SysMenuDao;
import com.xsy.sys.dto.SysMenuDTO;
import com.xsy.sys.entity.SysMenuEntity;
import com.xsy.sys.enums.SuperAdminEnum;
import com.xsy.sys.service.SysMenuService;
import com.xsy.sys.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysMenuServiceImpl extends RenBaseServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private RequiresPermissionsScan requiresPermissionsScan;

    @Override
    public SysMenuDTO get(Long id) {
        SysMenuEntity entity = baseDao.getById(id);

        SysMenuDTO dto = ConvertUtils.sourceToTarget(entity, SysMenuDTO.class);

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);

        //保存菜单
        insert(entity);
    }

    @Override
    @CacheEvict(cacheNames = SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);
        //上级菜单不能为自身
        if (entity.getId().equals(entity.getPid())) {
            throw new GlobalException("上级菜单不能为自身");
        }

        //更新菜单
        updateById(entity);
    }

    @Override
    @CacheEvict(cacheNames = SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //删除菜单
        deleteById(id);

        //删除角色菜单关系
        sysRoleMenuService.deleteByMenuId(id);
    }

    @Override
    public List<SysMenuDTO> getAllMenuList(Integer type) {
        List<SysMenuEntity> menuList = baseDao.getMenuList(type);

        List<SysMenuDTO> dtoList = ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);

        return TreeUtils.build(dtoList, RenConstant.MENU_ROOT);
    }

    @Override
    public List<SysMenuDTO> getUserMenuList(UserDetail user, Integer type) {
        List<SysMenuEntity> menuList;

        //系统管理员，拥有最高权限
        if (user.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            menuList = baseDao.getMenuList(type);
        } else {
            menuList = baseDao.getUserMenuList(user.getId(), type);
        }

        List<SysMenuDTO> dtoList = ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);

        return TreeUtils.build(dtoList);
    }

    @Override
    public List<String> getUserPermissionsList(Long userId) {
        return baseDao.getUserPermissionsList(userId);
    }

    @Override
    public List<SysMenuDTO> getListPid(Long pid) {
        List<SysMenuEntity> menuList = baseDao.getListPid(pid);

        return ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);
    }

    @Override
    public List<String> getAllPermissions() {
        return requiresPermissionsScan.getPermissionList();
    }
}
