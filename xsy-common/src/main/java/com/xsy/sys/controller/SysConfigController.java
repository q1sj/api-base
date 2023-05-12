package com.xsy.sys.controller;

import com.xsy.base.util.PageData;
import com.xsy.base.util.Result;
import com.xsy.sys.entity.SysConfigEntity;
import com.xsy.sys.enums.SysConfigValueTypeEnum;
import com.xsy.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数管理
 *
 * @author Q1sj
 * @date 2022.9.22 9:25
 */
@Slf4j
@RequestMapping("/sys/config")
@RestController
public class SysConfigController {
    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 列表
     *
     * @param configKey
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:config:list")
    public Result<PageData<SysConfigEntity>> list(String configKey, @RequestParam int page, @RequestParam int pageSize) {
        PageData<SysConfigEntity> pageData = sysConfigService.list(configKey, page, pageSize);
        return Result.ok(pageData);
    }

    /**
     * 新增或修改
     *
     * @param sysConfigEntity
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @RequiresPermissions("sys:config:saveOrUpdate")
    public Result<Void> saveOrUpdate(@RequestBody @Validated SysConfigEntity sysConfigEntity) {
        sysConfigService.saveOrUpdate(sysConfigEntity);
        return Result.ok();
    }

    @GetMapping("/configValueType")
    public Result<List<String>> configValueType() {
        return Result.ok(Arrays.stream(SysConfigValueTypeEnum.values()).map(SysConfigValueTypeEnum::name).collect(Collectors.toList()));
    }
}
