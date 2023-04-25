/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xsy.security.service;


import com.xsy.security.entity.SysUserTokenEntity;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.entity.SysUserEntity;

import java.util.Set;

/**
 * shiro相关接口
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface AuthService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(UserDetail user);

    SysUserTokenEntity getByToken(String token);

    /**
     * 根据用户ID，查询用户
     *
     * @param userId
     */
    SysUserEntity getUser(Long userId);

    /**
     * 刷新token过期时间
     *
     * @param tokenEntity
     */
    void refreshExpireDate(SysUserTokenEntity tokenEntity);

    /**
     * 用户是否可用
     *
     * @param user
     * @return
     */
    boolean userIsDisable(SysUserEntity user);
}
