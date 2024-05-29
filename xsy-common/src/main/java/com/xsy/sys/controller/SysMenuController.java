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

    /**
     * nav
     *
     * @return
     */
    @GetMapping("nav")
    public Result<List<SysMenuDTO>> nav() {
        UserDetail user = SecurityUser.getUser();
        List<SysMenuDTO> list = sysMenuService.getUserMenuList(user, MenuTypeEnum.MENU.value());

        return Result.ok(list);
    }

    /**
     * 当前用户拥有权限
     *
     * @return
     */
    @GetMapping("permissions")
    public Result<Set<String>> permissions() {
        UserDetail user = SecurityUser.getUser();
        Set<String> set = authService.getUserPermissions(user);

        return Result.ok(set);
    }

    /**
     * 后端所有可配置权限
     *
     * @return
     */
    @GetMapping("/allPermissions")
    public Result<List<String>> allPermissions() {
        List<String> list = sysMenuService.getAllPermissions();
        return Result.ok(list);
    }

    /**
     * 列表
     *
     * @param type
     * @return
     */
    @GetMapping("list")
    public Result<List<SysMenuDTO>> list(Integer type) {
        List<SysMenuDTO> list = sysMenuService.getAllMenuList(type);

        return Result.ok(list);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Result<SysMenuDTO> get(@PathVariable("id") Long id) {
        SysMenuDTO data = sysMenuService.get(id);

        return Result.ok(data);
    }

    /**
     * 保存
     *
     * @param dto
     * @return
     */
    @PostMapping
    @RequiresPermissions("menu:save")
    public Result save(@RequestBody SysMenuDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, Default.class);

        sysMenuService.save(dto);

        return Result.ok();
    }

    /**
     * 更新
     *
     * @param dto
     * @return
     */
    @PutMapping
    @RequiresPermissions("menu:update")
    public Result update(@RequestBody SysMenuDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, Default.class);

        sysMenuService.update(dto);

        return Result.ok();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @RequiresPermissions("menu:delete")
    public Result delete(@PathVariable("id") Long id) {
        //判断是否有子菜单或按钮
        List<SysMenuDTO> list = sysMenuService.getListPid(id);
        if (list.size() > 0) {
            return Result.error("当前菜单存在子菜单");
        }

        sysMenuService.delete(id);

        return Result.ok();
    }

    /**
     * 当前用户拥有菜单
     *
     * @return
     */
    @GetMapping("select")
    public Result<List<SysMenuDTO>> select() {
        UserDetail user = SecurityUser.getUser();
        //非bug 只查菜单使用nav接口
        List<SysMenuDTO> list = sysMenuService.getUserMenuList(user, null);

        return Result.ok(list);
    }
}
