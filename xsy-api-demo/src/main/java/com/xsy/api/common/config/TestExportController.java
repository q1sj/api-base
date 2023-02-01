package com.xsy.api.common.config;

import com.xsy.base.export.Export;
import com.xsy.base.util.PageData;
import com.xsy.base.util.Result;
import com.xsy.security.annotation.NoAuth;
import com.xsy.sys.dto.SysUserDTO;
import com.xsy.sys.dto.UserListQuery;
import com.xsy.sys.entity.SysConfigEntity;
import com.xsy.sys.entity.SysRoleEntity;
import com.xsy.sys.entity.SysUserEntity;
import com.xsy.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Q1sj
 * @date 2022.12.1 16:46
 */
@RequestMapping("/a")
@RestController
public class TestExportController {
    @NoAuth
    @Export // TODO  导出接口 @NoAuth失效
    @GetMapping("/test")
    public Result<List<SysUserEntity>> test() {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test");
        return Result.ok(Collections.singletonList(userEntity));
    }

    @Export(filename = "test2")
    @GetMapping("/test2")
    public Result<List<SysRoleEntity>> test2() {
        SysRoleEntity role = new SysRoleEntity();
        role.setId(1L);
        role.setName("test role");
        return Result.ok(Collections.singletonList(role));
    }

    @Export
    @GetMapping("/test3")
    public Result<SysRoleEntity> test3() {
        SysRoleEntity role = new SysRoleEntity();
        role.setId(1L);
        role.setName("test role");
        return Result.ok(role);
    }

    @Export
    @GetMapping("/test4")
    public Object test4() {
        SysConfigEntity config = new SysConfigEntity();
        config.setConfigKey("key");
        config.setConfigValue("val");
        return config;
    }

    @Export
    @GetMapping("/test5")
    public List<Object> test5() {
        SysConfigEntity config = new SysConfigEntity();
        config.setConfigKey("key");
        config.setConfigValue("val");
        return Arrays.asList(config, config);
    }

    @Autowired
    private SysUserService sysUserService;

    @Export
    @GetMapping("/test6")
    public PageData<SysUserDTO> tset6(UserListQuery query) {
        return this.sysUserService.page(query);
    }

    @Export(resultExpression = "#result.get().get()")
    @GetMapping("/test7")
    public Optional<Optional<SysUserEntity>> test7() {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test");
        return  Optional.of(Optional.of(userEntity));
    }
}
