/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsy.base.util.PageData;
import com.xsy.job.entity.ScheduleJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageData<ScheduleJobLogEntity> queryPage(Map<String, Object> params);

	void clearLog(int ago);
}
