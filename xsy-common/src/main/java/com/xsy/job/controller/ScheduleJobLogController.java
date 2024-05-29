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
import com.xsy.job.entity.ScheduleJobLogEntity;
import com.xsy.job.service.ScheduleJobLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {
	@Autowired
	private ScheduleJobLogService scheduleJobLogService;

	/**
	 * 定时任务日志列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("schedule:log")
	public Result<PageData<ScheduleJobLogEntity>> list(@RequestParam Map<String, Object> params) {
		PageData<ScheduleJobLogEntity> page = scheduleJobLogService.queryPage(params);

		return Result.ok(page);
	}

	/**
	 * 定时任务日志信息
	 */
	@RequestMapping("/info/{logId}")
	public Result<ScheduleJobLogEntity> info(@PathVariable("logId") Long logId) {
		ScheduleJobLogEntity log = scheduleJobLogService.getById(logId);

		return Result.ok(log);
	}
}
