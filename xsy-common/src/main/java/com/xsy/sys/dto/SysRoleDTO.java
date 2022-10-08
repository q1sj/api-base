/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xsy.sys.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xsy.base.enums.AddGroup;
import com.xsy.base.enums.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
public class SysRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@Null(message="{id.null}", groups = AddGroup.class)
	@NotNull(message="{id.require}", groups = UpdateGroup.class)
	private Long id;


	@NotBlank(message="{sysrole.name.require}", groups = Default.class)
	private String name;

	private String remark;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createDate;

	private List<Long> menuIdList;

}
