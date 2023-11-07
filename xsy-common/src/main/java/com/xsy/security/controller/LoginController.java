/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.controller;

import com.xsy.base.util.Result;
import com.xsy.base.util.ValidatorUtils;
import com.xsy.security.annotation.NoAuth;
import com.xsy.security.dto.LoginDTO;
import com.xsy.security.dto.TokenDTO;
import com.xsy.security.service.SysUserTokenService;
import com.xsy.security.user.SecurityUser;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.dto.SysUserDTO;
import com.xsy.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
public class LoginController {
    public static final String LOGIN_MAPPING = "/login";
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    /**
     * 登录
     *
     * @param login
     * @return
     */
    @NoAuth
    @PostMapping(LoginController.LOGIN_MAPPING)
    public Result<TokenDTO> login(@RequestBody LoginDTO login) {
        //效验数据
        ValidatorUtils.validateEntity(login);
        SysUserDTO user = sysUserService.validLogin(login);
        //登录成功
        return Result.ok(sysUserTokenService.createToken(user));
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping("logout")
    public Result<Void> logout() {
        UserDetail user = SecurityUser.getUser();
        //退出
        sysUserTokenService.logout(user.getId());
        //用户信息
        return Result.ok();
    }

}
