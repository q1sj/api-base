/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.service.impl.RenBaseServiceImpl;
import com.xsy.base.util.ConvertUtils;
import com.xsy.base.util.PageData;
import com.xsy.base.util.StringUtils;
import com.xsy.security.enums.SecurityConstant;
import com.xsy.security.user.SecurityUser;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.dao.SysRoleDao;
import com.xsy.sys.dto.RoleListQuery;
import com.xsy.sys.dto.SysRoleDTO;
import com.xsy.sys.entity.SysRoleEntity;
import com.xsy.sys.service.SysRoleMenuService;
import com.xsy.sys.service.SysRoleService;
import com.xsy.sys.service.SysRoleUserService;
import com.xsy.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysRoleServiceImpl extends RenBaseServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysUserService sysUserService;
    @Override
    public PageData<SysRoleDTO> page(RoleListQuery query) {
        IPage<SysRoleEntity> page = baseDao.selectPage(
                query.initPage(),
                getWrapper(query)
        );
        return getPageData(page, SysRoleDTO.class);
    }

    @Override
    public List<SysRoleDTO> list(RoleListQuery query) {
        List<SysRoleEntity> entityList = baseDao.selectList(getWrapper(query));
        return ConvertUtils.sourceToTarget(entityList, SysRoleDTO.class);
    }

    @Override
    public SysRoleDTO get(Long id) {
        SysRoleEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysRoleDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysRoleDTO dto) {
        // 校验菜单越权
        validMenuOverride(dto.getMenuIdList());
        SysRoleEntity entity = ConvertUtils.sourceToTarget(dto, SysRoleEntity.class);
        //保存角色
        insert(entity);
        //保存角色菜单关系
        sysRoleMenuService.saveOrUpdate(entity.getId(), dto.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME, allEntries = true)
    public void update(SysRoleDTO dto) {
        // 校验菜单越权
        validMenuOverride(dto.getMenuIdList());
        SysRoleEntity entity = ConvertUtils.sourceToTarget(dto, SysRoleEntity.class);
        //更新角色
        updateById(entity);

        //更新角色菜单关系
        sysRoleMenuService.saveOrUpdate(entity.getId(), dto.getMenuIdList());
    }

    @Override
    @CacheEvict(cacheNames = SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        //删除角色
        baseDao.deleteBatchIds(Arrays.asList(ids));

        //删除角色用户关系
        sysRoleUserService.deleteByRoleIds(ids);

        //删除角色菜单关系
        sysRoleMenuService.deleteByRoleIds(ids);
    }

    private LambdaQueryWrapper<SysRoleEntity> getWrapper(RoleListQuery query) {
        UserDetail user = SecurityUser.getUser();
        return Wrappers.lambdaQuery(SysRoleEntity.class)
                .eq(!user.isAdmin(), SysRoleEntity::getCreator, user.getId())
                .like(StringUtils.isNotBlank(query.getName()), SysRoleEntity::getName, query.getName());
    }

    private void validMenuOverride(List<Long> menuIdList) {
        UserDetail user = SecurityUser.getUser();
        if (user.isAdmin()) {
            return;
        }
        List<Long> allMenuIdList = sysUserService.allMenuId(user.getId());
        //判断是否越权
        if(!new HashSet<>(allMenuIdList).containsAll(menuIdList)){
            throw new GlobalException("新增角色的权限，已超出你的权限范围");
        }
    }
}
