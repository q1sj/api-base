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
import com.xsy.base.util.Result;
import com.xsy.base.util.ValidatorUtils;
import com.xsy.sys.dto.SysDeptDTO;
import com.xsy.sys.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.List;

/**
 * 部门管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {
	@Autowired
	private SysDeptService sysDeptService;

	@GetMapping("list")
	public Result<List<SysDeptDTO>> list(){
		List<SysDeptDTO> list = sysDeptService.list(new HashMap<>(1));

		return Result.ok(list);
	}

	@GetMapping("{id}")
	public Result<SysDeptDTO> get(@PathVariable("id") Long id){
		SysDeptDTO data = sysDeptService.get(id);

		return Result.ok(data);
	}

	@PostMapping
	public Result save(@RequestBody SysDeptDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, AddGroup.class, Default.class);

		sysDeptService.save(dto);

		return Result.ok();
	}

	@PutMapping
	public Result update(@RequestBody SysDeptDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, UpdateGroup.class, Default.class);

		sysDeptService.update(dto);

		return Result.ok();
	}

	@DeleteMapping("{id}")
	@RequiresPermissions("sys:dept:delete")
	public Result delete(@PathVariable("id") Long id){
		sysDeptService.delete(id);
		return Result.ok();
	}

}
