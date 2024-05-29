/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.BizAssertUtils;
import com.xsy.base.util.DateUtils;
import com.xsy.base.util.PageData;
import com.xsy.base.util.StringUtils;
import com.xsy.job.dao.ScheduleJobLogDao;
import com.xsy.job.entity.ScheduleJobLogEntity;
import com.xsy.job.service.ScheduleJobLogService;
import com.xsy.job.utils.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {
	@Value("${api.log.save-day:30}")
	private Integer logSaveDay;
	@Resource
	private ScheduleJobLogDao scheduleJobLogDao;

	@Override
	public PageData<ScheduleJobLogEntity> queryPage(Map<String, Object> params) {
		String jobId = (String) params.get("jobId");

		IPage<ScheduleJobLogEntity> page = this.page(
				new Page<>(Integer.parseInt(params.getOrDefault(Constant.PAGE, "1").toString()), Integer.parseInt(params.getOrDefault(Constant.LIMIT, "15").toString())),
				new QueryWrapper<ScheduleJobLogEntity>().like(StringUtils.isNotBlank(jobId), "job_id", jobId)
		);

		return new PageData(page);
	}

	@Override
	public void truncateTable() {
		scheduleJobLogDao.truncateTable();
	}

	@Override
	public void clearLog(int ago) {
		BizAssertUtils.isTrue(ago > 0, "必须大于0");
		this.remove(Wrappers.lambdaQuery(ScheduleJobLogEntity.class)
				.lt(ScheduleJobLogEntity::getCreateTime, DateUtils.addDays(new Date(), -ago)));
	}

	@Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 60 * 1000)
	public void clear() {
		clearLog(logSaveDay);
	}
}
