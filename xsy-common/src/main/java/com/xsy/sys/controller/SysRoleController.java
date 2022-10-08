/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xsy.sys.controller;

import com.xsy.base.enums.AddGroup;
import com.xsy.base.enums.UpdateGroup;
import com.xsy.base.util.*;
import com.xsy.sys.dto.SysRoleDTO;
import com.xsy.sys.service.SysRoleMenuService;
import com.xsy.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@GetMapping("page")
	@RequiresPermissions("sys:role:page")
	public Result<PageData<SysRoleDTO>> page(@RequestParam Map<String, Object> params){
		PageData<SysRoleDTO> page = sysRoleService.page(params);

		return Result.ok(page);
	}

	@GetMapping("list")
	@RequiresPermissions("sys:role:list")
	public Result<List<SysRoleDTO>> list(){
		List<SysRoleDTO> data = sysRoleService.list(new HashMap<>(1));

		return Result.ok(data);
	}

	@GetMapping("{id}")
	@RequiresPermissions("sys:role:info")
	public Result<SysRoleDTO> get(@PathVariable("id") Long id){
		SysRoleDTO data = sysRoleService.get(id);

		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.getMenuIdList(id);
		data.setMenuIdList(menuIdList);

		return Result.ok(data);
	}

	@PostMapping
	@RequiresPermissions("sys:role:save")
	public Result save(@RequestBody SysRoleDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, AddGroup.class, Default.class);

		sysRoleService.save(dto);

		return Result.ok();
	}

	@PutMapping
	@RequiresPermissions("sys:role:update")
	public Result update(@RequestBody SysRoleDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, UpdateGroup.class, Default.class);

		sysRoleService.update(dto);

		return Result.ok();
	}

	@DeleteMapping
	@RequiresPermissions("sys:role:delete")
	public Result delete(@RequestBody Long[] ids){
		//效验数据
		BizAssertUtils.isNotEmpty(ids,"id不能为空");

		sysRoleService.delete(ids);

		return Result.ok();
	}
}
