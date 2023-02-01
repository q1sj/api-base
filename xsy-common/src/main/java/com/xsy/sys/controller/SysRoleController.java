/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xsy.base.enums.AddGroup;
import com.xsy.base.enums.UpdateGroup;
import com.xsy.base.pojo.PageQuery;
import com.xsy.base.util.BizAssertUtils;
import com.xsy.base.util.PageData;
import com.xsy.base.util.Result;
import com.xsy.base.util.ValidatorUtils;
import com.xsy.sys.dto.RoleListQuery;
import com.xsy.sys.dto.SysRoleDTO;
import com.xsy.sys.service.SysRoleMenuService;
import com.xsy.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

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

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    @GetMapping("page")
    @RequiresPermissions("sys:role:page")
    public Result<PageData<SysRoleDTO>> page(RoleListQuery query) {
        PageData<SysRoleDTO> page = sysRoleService.page(query);
        return Result.ok(page);
    }

    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    @RequiresPermissions("sys:role:list")
    public Result<List<SysRoleDTO>> list(RoleListQuery query) {
        List<SysRoleDTO> data = sysRoleService.list(query);
        return Result.ok(data);
    }

    /**
     * 根据id查询
     *
     * @param id 角色id
     * @return
     */
    @GetMapping("{id}")
    @RequiresPermissions("sys:role:info")
    public Result<SysRoleDTO> get(@PathVariable("id") Long id) {
        SysRoleDTO data = sysRoleService.get(id);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.getMenuIdList(id);
        data.setMenuIdList(menuIdList);

        return Result.ok(data);
    }

    /**
     * 新增
     *
     * @param dto
     * @return
     */
    @PostMapping
    @RequiresPermissions("sys:role:save")
    public Result save(@RequestBody SysRoleDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, Default.class);

        sysRoleService.save(dto);

        return Result.ok();
    }

    /**
     * 更新
     *
     * @param dto
     * @return
     */
    @PutMapping
    @RequiresPermissions("sys:role:update")
    public Result update(@RequestBody SysRoleDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, Default.class);

        sysRoleService.update(dto);

        return Result.ok();
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @RequiresPermissions("sys:role:delete")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        BizAssertUtils.isNotEmpty(ids, "id不能为空");

        sysRoleService.delete(ids);

        return Result.ok();
    }
}
