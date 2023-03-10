package com.xsy.sys.controller;

import com.xsy.base.util.PageData;
import com.xsy.base.util.Result;
import com.xsy.sys.entity.SysConfigEntity;
import com.xsy.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result<Void> saveOrUpdate(@RequestBody @Validated SysConfigEntity sysConfigEntity) {
        sysConfigService.put(sysConfigEntity);
        return Result.ok();
    }
}
