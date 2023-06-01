package com.xsy.sys.controller;

import com.xsy.base.util.PageData;
import com.xsy.base.util.Result;
import com.xsy.sys.dto.SysTaskConfigQuery;
import com.xsy.sys.entity.SysTaskConfigEntity;
import com.xsy.sys.service.SysTaskConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务配置
 *
 * @author xsy xsy@xsy.com
 * @since 1.0.0 2023-03-07
 */
@Slf4j
@RestController
@RequestMapping("/sys/task/config")
public class SysTaskConfigController {
    public static final String LIST_PERMISSIONS = "sysTaskConfig:list";
    public static final String DETAIL_PERMISSIONS = "sysTaskConfig:detail";
    public static final String SAVE_PERMISSIONS = "sysTaskConfig:save";
    public static final String UPDATE_PERMISSIONS = "sysTaskConfig:update";
    public static final String DELETE_PERMISSIONS = "sysTaskConfig:delete";

    @Autowired
    private SysTaskConfigService sysTaskConfigService;

    /**
     * 立即执行
     *
     * @param name 定时任务名称
     * @return
     */
    @PostMapping("/run/{id}")
    public Result<Void> run(@PathVariable Integer id) {
        sysTaskConfigService.run(id);
        return Result.ok();
    }

    /**
     * 列表
     *
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions(LIST_PERMISSIONS)
    public Result<PageData<SysTaskConfigEntity>> list(@Validated SysTaskConfigQuery query) {
        PageData<SysTaskConfigEntity> pageData = sysTaskConfigService.page(query);
        return Result.ok(pageData);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail")
    @RequiresPermissions(DETAIL_PERMISSIONS)
    public Result<SysTaskConfigEntity> detail(@RequestParam Long id) {
        SysTaskConfigEntity entity = sysTaskConfigService.getById(id);
        if (entity == null) {
            return Result.error("id:" + id + "不存在");
        }
        return Result.ok(entity);
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions(SAVE_PERMISSIONS)
    public Result<Void> save(@RequestBody @Validated SysTaskConfigEntity entity) {
        sysTaskConfigService.save(entity);
        return Result.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PostMapping("/update")
    @RequiresPermissions(UPDATE_PERMISSIONS)
    public Result<Void> update(@RequestBody @Validated SysTaskConfigEntity entity) {
        sysTaskConfigService.updateById(entity);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @RequiresPermissions(DELETE_PERMISSIONS)
    public Result<Void> delete(@PathVariable Integer id) {
        sysTaskConfigService.removeById(id);
        return Result.ok();
    }
}
