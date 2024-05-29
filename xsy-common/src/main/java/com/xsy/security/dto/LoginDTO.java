/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.dto;

import com.xsy.base.log.IgnoreLog;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录表单
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{sysuser.username.require}")
    private String username;

    @IgnoreLog
    @NotBlank(message = "{sysuser.password.require}")
    private String password;
}
