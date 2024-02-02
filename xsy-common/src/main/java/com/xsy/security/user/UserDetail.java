/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xsy.security.user;

import com.xsy.job.utils.Constant;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 登录用户信息
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
public class UserDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;
	private String username;
	private String realName;
	private String headUrl;
	private Integer gender;
	private String email;
	private String mobile;
	private String password;
	private Integer status;
	/**
	 * 上次登录时间
	 */
	private Date lastLoginTime;
	private Integer superAdmin;

	/**
	 * 是否是超级管理员
	 *
	 * @return
	 */
	public boolean isSuperAdmin() {
		return Objects.equals(Constant.SUPER_ADMIN, this.superAdmin);
	}
}
