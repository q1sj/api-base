/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.controller;


import com.xsy.base.enums.AddGroup;
import com.xsy.base.enums.UpdateGroup;
import com.xsy.base.util.*;
import com.xsy.security.password.PasswordUtils;
import com.xsy.security.user.SecurityUser;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.dto.PasswordDTO;
import com.xsy.sys.dto.SysUserDTO;
import com.xsy.sys.dto.UserListQuery;
import com.xsy.sys.service.SysRoleUserService;
import com.xsy.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 用户管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    @GetMapping("page")
    public Result<PageData<SysUserDTO>> page(UserListQuery query) {
        PageData<SysUserDTO> page = sysUserService.page(query);
        return Result.ok(page);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Result<SysUserDTO> get(@PathVariable("id") Long id) {
        SysUserDTO data = sysUserService.get(id);
        if (data == null) {
            return Result.error("用户不存在");
        }
        //用户角色列表
        List<Long> roleIdList = sysRoleUserService.getRoleIdList(id);
        data.setRoleIdList(roleIdList);

        return Result.ok(data);
    }

    /**
     * 当前用户信息
     *
     * @return
     */
    @GetMapping("info")
    public Result<SysUserDTO> info() {
        SysUserDTO data = ConvertUtils.sourceToTarget(SecurityUser.getUser(), SysUserDTO.class);
        return Result.ok(data);
    }

    /**
     * 修改密码
     *
     * @param dto
     * @return
     */
    @PutMapping("password")
    public Result password(@RequestBody PasswordDTO dto) {
        // TODO RSA
	    //效验数据
        ValidatorUtils.validateEntity(dto);

        UserDetail user = SecurityUser.getUser();

        //原密码不正确
        if (!PasswordUtils.matches(dto.getPassword(), user.getPassword())) {
            return Result.error("原密码不正确");
        }

        sysUserService.updatePassword(user.getId(), dto.getNewPassword());

        return Result.ok();
    }

    /**
     * 重置密码
     *
     * @return
     * @apiNote 将密码重置为xsy@admin
     */
    @PostMapping("/resetPassword/{userId}")
    public Result<Void> resetPassword(@PathVariable Long userId) {
        sysUserService.updatePassword(userId, "xsy@admin");
        return Result.ok();
    }

    /**
     * 新增
     *
     * @param dto
     * @return
     */
    @PostMapping
    @RequiresPermissions("user:save")
    public Result save(@RequestBody SysUserDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, Default.class);

        sysUserService.save(dto);

        return Result.ok();
    }

    /**
     * 更新
     *
     * @param dto
     * @return
     */
    @PutMapping
    @RequiresPermissions("user:update")
    public Result update(@RequestBody SysUserDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, Default.class);

        sysUserService.update(dto);

        return Result.ok();
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @RequiresPermissions("user:delete")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        BizAssertUtils.isNotEmpty(ids, "id不能为空");

        sysUserService.delete(ids);

        return Result.ok();
    }
}
