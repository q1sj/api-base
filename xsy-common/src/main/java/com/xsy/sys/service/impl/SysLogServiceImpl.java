package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.sys.dao.ApiLogDao;
import com.xsy.sys.dto.SysLogDTO;
import com.xsy.sys.entity.SysLogEntity;
import com.xsy.sys.service.SysLogService;
import org.springframework.stereotype.Service;

/**
 * @author Q1sj
 * @date 2023.11.9 10:20
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<ApiLogDao, SysLogEntity> implements SysLogService {
	@Override
	public IPage<SysLogEntity> list(SysLogDTO dto) {
		LambdaQueryWrapper<SysLogEntity> wrapper = Wrappers.lambdaQuery(SysLogEntity.class)
				.between(SysLogEntity::getRecordTime, dto.getStartTime(), dto.getEndTime())
				.orderByDesc(SysLogEntity::getRecordTime);
		return page(new Page<>(dto.getPage(), dto.getPageSize()), wrapper);
	}
}
