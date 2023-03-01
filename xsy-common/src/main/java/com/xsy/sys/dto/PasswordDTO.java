/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 修改密码
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
public class PasswordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{sysuser.password.require}")
    private String password;

    @NotBlank(message = "{sysuser.password.require}")
    private String newPassword;

    @Override
    public String toString() {
        return "PasswordDTO{" +
                "password='" + (password == null ? "null" : "****") + '\'' +
                ", newPassword='" + (newPassword == null ? "null" : "****") + '\'' +
                '}';
    }
}
