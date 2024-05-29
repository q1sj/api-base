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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.PageData;
import com.xsy.base.util.StringUtils;
import com.xsy.job.dao.ScheduleJobDao;
import com.xsy.job.entity.ScheduleJobEntity;
import com.xsy.job.service.ScheduleJobService;
import com.xsy.job.utils.Constant;
import com.xsy.job.utils.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJobEntity> implements ScheduleJobService {
	@Autowired
	private Scheduler scheduler;
	@Value("${quartz.enable:true}")
	private Boolean quartzEnable;

	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init() {
		if (!quartzEnable) {
			log.info("quartzEnable=false");
			return;
		}
		List<ScheduleJobEntity> scheduleJobList = this.list();
		for (ScheduleJobEntity scheduleJob : scheduleJobList) {
			CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
			//如果不存在，则创建
			if (cronTrigger == null) {
				ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
			} else {
				ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
			}
		}
	}

	@Override
	public PageData<ScheduleJobEntity> queryPage(Map<String, Object> params) {
		String beanName = (String) params.get("beanName");

		IPage<ScheduleJobEntity> page = this.page(
				new Page<>(Integer.parseInt(params.getOrDefault(Constant.PAGE, "1").toString()), Integer.parseInt(params.getOrDefault(Constant.LIMIT, "15").toString())),
				new QueryWrapper<ScheduleJobEntity>().like(StringUtils.isNotBlank(beanName), "bean_name", beanName)
		);
		return new PageData<>(page);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveJob(ScheduleJobEntity scheduleJob) {
		scheduleJob.setCreateTime(new Date());
		scheduleJob.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
		this.save(scheduleJob);

		ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ScheduleJobEntity scheduleJob) {
		ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);

		this.updateById(scheduleJob);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.deleteScheduleJob(scheduler, jobId);
		}

		//删除数据
		this.removeByIds(Arrays.asList(jobIds));
	}

	@Override
	public int updateBatch(Long[] jobIds, int status) {
		Map<String, Object> map = new HashMap<>(2);
		List<Long> list = Arrays.asList(jobIds);
		map.put("list", list);
		map.put("status", status);
		return baseMapper.updateBatch(map);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void run(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.run(scheduler, this.getById(jobId));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void pause(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.pauseJob(scheduler, jobId);
		}

		updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void resume(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.resumeJob(scheduler, jobId);
		}

		updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());
	}
}
