/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.service.impl;

import com.xsy.security.entity.SysUserTokenEntity;
import com.xsy.security.enums.SecurityConstant;
import com.xsy.security.service.AuthService;
import com.xsy.security.service.SysUserTokenService;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.dao.SysMenuDao;
import com.xsy.sys.entity.SysUserEntity;
import com.xsy.sys.enums.SuperAdminEnum;
import com.xsy.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Override
    @Cacheable(cacheNames = SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME,key = "T(com.xsy.security.enums.SecurityConstant).getSysUserPermissionsCacheKey(#user.getId())")
    public Set<String> getUserPermissions(UserDetail user) {
        //系统管理员，拥有最高权限
        List<String> permissionsList;
        if (user.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            permissionsList = sysMenuDao.getPermissionsList();
        } else {
            permissionsList = sysMenuDao.getUserPermissionsList(user.getId());
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String permissions : permissionsList) {
            if (StringUtils.isBlank(permissions)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(permissions.trim().split(",")));
        }

        return permsSet;
    }

    @Override
    public SysUserTokenEntity getByToken(String token) {
        return sysUserTokenService.getByToken(token);
    }

    @Override
    public SysUserEntity getUser(Long userId) {
        return sysUserService.selectById(userId);
    }
}
