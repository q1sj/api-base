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
import com.xsy.base.cache.CacheManagerWrapper;
import com.xsy.base.cache.CacheWrapper;
import com.xsy.base.enums.RenConstant;
import com.xsy.base.service.impl.RenBaseServiceImpl;
import com.xsy.base.util.ConvertUtils;
import com.xsy.base.util.PageData;
import com.xsy.security.enums.SecurityConstant;
import com.xsy.security.password.PasswordUtils;
import com.xsy.sys.dao.SysUserDao;
import com.xsy.sys.dto.SysUserDTO;
import com.xsy.sys.dto.UserListQuery;
import com.xsy.sys.entity.SysUserEntity;
import com.xsy.sys.enums.SuperAdminEnum;
import com.xsy.sys.service.SysRoleUserService;
import com.xsy.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysUserServiceImpl extends RenBaseServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private CacheManagerWrapper cacheManagerWrapper;

    @Override
    public PageData<SysUserDTO> page(UserListQuery query) {
        LambdaQueryWrapper<SysUserEntity> wrapper = getWrapper(query);
        IPage<SysUserEntity> iPage = baseDao.selectPage(query.initPage(), wrapper);
        return getPageData(iPage, SysUserDTO.class);
    }

    @Override
    protected <T> PageData<T> getPageData(List<?> list, long total, Class<T> target) {
        List<T> targetList = ConvertUtils.sourceToTarget(list, target);

        return new PageData<>(targetList, total);
    }

    @Override
    protected <T> PageData<T> getPageData(IPage page, Class<T> target) {
        return getPageData(page.getRecords(), page.getTotal(), target);
    }

    @Override
    public SysUserDTO get(Long id) {

        SysUserEntity entity = this.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    @Override
    @Cacheable(cacheNames = SecurityConstant.SYS_USER_CACHE_NAME, key = "T(com.xsy.security.enums.SecurityConstant).getSysUserCacheKey(#id)")
    public SysUserEntity selectById(Serializable id) {
        return baseDao.selectById(id);
    }

    @Override
    public SysUserDTO getByUsername(String username) {
        SysUserEntity entity = baseDao.getByUsername(username);
        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysUserDTO dto) {
        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);

        //密码加密
        String password = PasswordUtils.encode(entity.getPassword());
        entity.setPassword(password);

        //保存用户
        entity.setSuperAdmin(SuperAdminEnum.NO.value());
        insert(entity);

        //保存角色用户关系
        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = SecurityConstant.SYS_USER_CACHE_NAME, key = "T(com.xsy.security.enums.SecurityConstant).getSysUserCacheKey(#dto.getId())")
    public void update(SysUserDTO dto) {
        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);
        // 清除用户权限缓存
        CacheWrapper cache = cacheManagerWrapper.getCache(SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME);
        cache.evict(SecurityConstant.getSysUserPermissionsCacheKey(dto.getId()));
        //密码加密
        if (StringUtils.isBlank(dto.getPassword())) {
            entity.setPassword(null);
        } else {
            String password = PasswordUtils.encode(entity.getPassword());
            entity.setPassword(password);
        }

        //更新用户
        updateById(entity);

        //更新角色用户关系
        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
    }

    @Override
    @CacheEvict(cacheNames = SecurityConstant.SYS_USER_CACHE_NAME, key = "T(com.xsy.security.enums.SecurityConstant).getSysUserCacheKey(#dto.getId())")
    public void delete(Long[] ids) {
        // 清除用户权限缓存
        CacheWrapper cache = cacheManagerWrapper.getCache(SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME);
        for (Long id : ids) {
            cache.evict(SecurityConstant.getSysUserPermissionsCacheKey(id));
        }
        //删除用户
        baseDao.deleteBatchIds(Arrays.asList(ids));

        //删除角色用户关系
        sysRoleUserService.deleteByUserIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long id, String newPassword) {
        newPassword = PasswordUtils.encode(newPassword);

        baseDao.updatePassword(id, newPassword);
    }
    private LambdaQueryWrapper<SysUserEntity> getWrapper(UserListQuery query) {
        return Wrappers.lambdaQuery(SysUserEntity.class)
                .like(StringUtils.isNotBlank(query.getUsername()), SysUserEntity::getUsername, query.getUsername());
    }
}
