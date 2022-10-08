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
import com.xsy.base.util.TreeNode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Date;

/**
 * 部门管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public class SysDeptDTO extends TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;

	@Null(message="{id.null}", groups = AddGroup.class)
	@NotNull(message="{id.require}", groups = UpdateGroup.class)
	private Long id;

	@NotNull(message="{sysdept.pid.require}", groups = Default.class)
	private Long pid;

	@NotBlank(message="{sysdept.name.require}", groups = Default.class)
	private String name;

	@Min(value = 0, message = "{sort.number}", groups = Default.class)
	private Integer sort;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createDate;

	private String parentName;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getPid() {
		return pid;
	}

	@Override
	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}
