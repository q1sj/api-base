/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.service;


import com.xsy.base.exception.GlobalException;
import com.xsy.base.service.RenBaseService;
import com.xsy.base.util.PageData;
import com.xsy.security.dto.LoginDTO;
import com.xsy.sys.dto.SysUserDTO;
import com.xsy.sys.dto.UserListQuery;
import com.xsy.sys.entity.SysUserEntity;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends RenBaseService<SysUserEntity> {

    PageData<SysUserDTO> page(UserListQuery query);

    SysUserDTO get(Long id);

    SysUserDTO getByUsername(String username);

    void save(SysUserDTO dto);

    void update(SysUserDTO dto);

    void updateLastLoginTime(Long id);

    void delete(Long[] ids);

    /**
     * 修改密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     */
    void updatePassword(Long id, String newPassword);

    /**
     * 校验账号密码是否正确
     *
     * @param login
     * @throws GlobalException
     */
    SysUserDTO validLogin(LoginDTO login) throws GlobalException;

    /**
     * 用户是否可用
     *
     * @param user
     * @return
     */
    boolean userIsDisable(SysUserEntity user);
}
