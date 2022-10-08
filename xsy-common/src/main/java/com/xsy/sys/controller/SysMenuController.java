/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.controller;

import com.xsy.base.util.Result;
import com.xsy.base.util.ValidatorUtils;
import com.xsy.security.service.AuthService;
import com.xsy.security.user.SecurityUser;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.dto.SysMenuDTO;
import com.xsy.sys.enums.MenuTypeEnum;
import com.xsy.sys.service.SysMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private AuthService authService;

    @GetMapping("nav")
    public Result<List<SysMenuDTO>> nav() {
        UserDetail user = SecurityUser.getUser();
        List<SysMenuDTO> list = sysMenuService.getUserMenuList(user, MenuTypeEnum.MENU.value());

        return Result.ok(list);
    }

    @GetMapping("permissions")
    public Result<Set<String>> permissions() {
        UserDetail user = SecurityUser.getUser();
        Set<String> set = authService.getUserPermissions(user);

        return Result.ok(set);
    }

    @GetMapping("list")
    @RequiresPermissions("sys:menu:list")
    public Result<List<SysMenuDTO>> list(Integer type) {
        List<SysMenuDTO> list = sysMenuService.getAllMenuList(type);

        return Result.ok(list);
    }

    @GetMapping("{id}")
    @RequiresPermissions("sys:menu:info")
    public Result<SysMenuDTO> get(@PathVariable("id") Long id) {
        SysMenuDTO data = sysMenuService.get(id);

        return Result.ok(data);
    }

    @PostMapping
    @RequiresPermissions("sys:menu:save")
    public Result save(@RequestBody SysMenuDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, Default.class);

        sysMenuService.save(dto);

        return Result.ok();
    }

    @PutMapping
    @RequiresPermissions("sys:menu:update")
    public Result update(@RequestBody SysMenuDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, Default.class);

        sysMenuService.update(dto);

        return Result.ok();
    }

    @DeleteMapping("{id}")
    @RequiresPermissions("sys:menu:delete")
    public Result delete(@PathVariable("id") Long id) {
        //判断是否有子菜单或按钮
        List<SysMenuDTO> list = sysMenuService.getListPid(id);
        if (list.size() > 0) {
            return Result.error("当前菜单存在子菜单");
        }

        sysMenuService.delete(id);

        return Result.ok();
    }

    @GetMapping("select")
    @RequiresPermissions("sys:menu:select")
    public Result<List<SysMenuDTO>> select() {
        UserDetail user = SecurityUser.getUser();
        List<SysMenuDTO> list = sysMenuService.getUserMenuList(user, null);

        return Result.ok(list);
    }
}
