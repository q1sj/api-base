/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.service;

import com.xsy.base.service.RenBaseService;
import com.xsy.security.dto.TokenDTO;
import com.xsy.security.entity.SysUserTokenEntity;

/**
 * 用户Token
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserTokenService extends RenBaseService<SysUserTokenEntity> {

    /**
     * 生成token
     *
     * @param userId 用户ID
     */
    TokenDTO createToken(Long userId);

    /**
     * 根据有效token获取用户信息
     *
     * @param token
     * @return
     */
    SysUserTokenEntity getByToken(String token);

    /**
     * 退出，修改token值
     *
     * @param userId 用户ID
     */
    void logout(Long userId);

    /**
     * 刷新token过期时间
     *
     * @param token
     */
    void refreshExpireDate(String token);
}
