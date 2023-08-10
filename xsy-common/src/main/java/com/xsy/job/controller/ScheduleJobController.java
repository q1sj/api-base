/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.job.controller;


import com.xsy.base.util.PageData;
import com.xsy.base.util.Result;
import com.xsy.base.util.ValidatorUtils;
import com.xsy.job.entity.ScheduleJobEntity;
import com.xsy.job.service.ScheduleJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 定时任务
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleJobController {
	@Autowired
	private ScheduleJobService scheduleJobService;

	/**
	 * 定时任务列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:schedule:list")
	public Result<PageData<ScheduleJobEntity>> list(@RequestParam Map<String, Object> params) {
		PageData<ScheduleJobEntity> page = scheduleJobService.queryPage(params);

		return Result.ok(page);
	}

	/**
	 * 定时任务信息
	 */
	@RequestMapping("/info/{jobId}")
	@RequiresPermissions("sys:schedule:info")
	public Result<ScheduleJobEntity> info(@PathVariable("jobId") Long jobId) {
		ScheduleJobEntity schedule = scheduleJobService.getById(jobId);

		return Result.ok(schedule);
	}

	/**
	 * 保存定时任务
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:schedule:save")
	public Result<Void> save(@RequestBody ScheduleJobEntity scheduleJob) {
		ValidatorUtils.validateEntity(scheduleJob);

		scheduleJobService.saveJob(scheduleJob);

		return Result.ok();
	}

	/**
	 * 修改定时任务
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:schedule:update")
	public Result<Void> update(@RequestBody ScheduleJobEntity scheduleJob) {
		ValidatorUtils.validateEntity(scheduleJob);

		scheduleJobService.update(scheduleJob);

		return Result.ok();
	}

	/**
	 * 删除定时任务
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:schedule:delete")
	public Result<Void> delete(@RequestBody Long[] jobIds) {
		scheduleJobService.deleteBatch(jobIds);

		return Result.ok();
	}

	/**
	 * 立即执行任务
	 */
	@RequestMapping("/run")
	@RequiresPermissions("sys:schedule:run")
	public Result<Void> run(@RequestBody Long[] jobIds) {
		scheduleJobService.run(jobIds);

		return Result.ok();
	}

	/**
	 * 暂停定时任务
	 */
	@RequestMapping("/pause")
	@RequiresPermissions("sys:schedule:pause")
	public Result<Void> pause(@RequestBody Long[] jobIds) {
		scheduleJobService.pause(jobIds);

		return Result.ok();
	}

	/**
	 * 恢复定时任务
	 */
	@RequestMapping("/resume")
	@RequiresPermissions("sys:schedule:resume")
	public Result<Void> resume(@RequestBody Long[] jobIds) {
		scheduleJobService.resume(jobIds);

		return Result.ok();
	}

}
