package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.DateUtils;
import com.xsy.sys.dao.SysTaskLogDao;
import com.xsy.sys.entity.SysTaskLogEntity;
import com.xsy.sys.service.SysTaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Q1sj
 * @date 2023.6.1 15:01
 */
@Slf4j
@Service
public class SysTaskLogServiceImpl extends ServiceImpl<SysTaskLogDao, SysTaskLogEntity> implements SysTaskLogService {
	@Scheduled(fixedDelay = 24 * 60 * 60 * 1000, initialDelay = 60 * 1000)
	public void cleanLog() {
		log.info("清理定时任务日志开始");
		LambdaQueryWrapper<SysTaskLogEntity> wrapper = Wrappers.lambdaQuery(SysTaskLogEntity.class)
				.lt(SysTaskLogEntity::getCreateTime, DateUtils.addDays(new Date(), -30));
		this.remove(wrapper);
		log.info("清理定时任务日志结束");
	}
}
