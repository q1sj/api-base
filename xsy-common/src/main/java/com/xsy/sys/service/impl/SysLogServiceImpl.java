package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.BizAssertUtils;
import com.xsy.base.util.DateUtils;
import com.xsy.sys.dao.ApiLogDao;
import com.xsy.sys.dto.SysLogDTO;
import com.xsy.sys.entity.SysLogEntity;
import com.xsy.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Q1sj
 * @date 2023.11.9 10:20
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<ApiLogDao, SysLogEntity> implements SysLogService {
	@Value("${api.log.save-day:30}")
	private Integer logSaveDay;

	@Override
	public IPage<SysLogEntity> list(SysLogDTO dto) {
		LambdaQueryWrapper<SysLogEntity> wrapper = Wrappers.lambdaQuery(SysLogEntity.class)
				.between(dto.getStartTime() != null && dto.getEndTime() != null, SysLogEntity::getRecordTime, dto.getStartTime(), dto.getEndTime())
				.orderByDesc(SysLogEntity::getRecordTime);
		return page(new Page<>(dto.getPage(), dto.getPageSize()), wrapper);
	}

	public void clearLog(int ago) {
		BizAssertUtils.isTrue(ago > 0, "必须大于0");
		this.remove(Wrappers.lambdaQuery(SysLogEntity.class)
				.lt(SysLogEntity::getRecordTime, DateUtils.addDays(new Date(), -ago)));
	}

	@Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 60 * 1000)
	public void clear() {
		clearLog(logSaveDay);
	}
}
