/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xsy.sys.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@Null(message="{id.null}", groups = AddGroup.class)
	@NotNull(message="{id.require}", groups = UpdateGroup.class)
	private Long id;

	@NotBlank(message="{sysuser.username.require}", groups = Default.class)
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="{sysuser.password.require}", groups = AddGroup.class)
	private String password;

	@NotBlank(message="{sysuser.realname.require}", groups = Default.class)
	private String realName;

	private String headUrl;

	@Range(min=0, max=2, message = "{sysuser.gender.range}", groups = Default.class)
	private Integer gender;

	@Email(message="{sysuser.email.error}", groups = Default.class)
	private String email;

	private String mobile;

//	@NotNull(message="{sysuser.deptId.require}", groups = Default.class)
	private Long deptId;

	@Range(min=0, max=1, message = "{sysuser.status.range}", groups = Default.class)
	private Integer status;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer superAdmin;

	private List<Long> roleIdList;

	interface AddGroup{}
	interface UpdateGroup{}
}
