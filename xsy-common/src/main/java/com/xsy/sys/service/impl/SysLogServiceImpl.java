package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.log.IgnoreLog;
import com.xsy.base.util.*;
import com.xsy.sys.dao.ApiLogDao;
import com.xsy.sys.dto.SysLogDTO;
import com.xsy.sys.entity.ExportRecordEntity;
import com.xsy.sys.entity.SysLogEntity;
import com.xsy.sys.service.ExportRecordService;
import com.xsy.sys.service.SysLogService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Q1sj
 * @date 2023.11.9 10:20
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<ApiLogDao, SysLogEntity> implements SysLogService, Export, DisposableBean {
	@Value("${api.log.save-day:30}")
	private Integer logSaveDay;

	@Autowired
	private ExportRecordService exportRecordService;

	private final List<SysLogEntity> sysLogCache = new CopyOnWriteArrayList<>();

	@Override
	public boolean save(SysLogEntity entity) {
		sysLogCache.add(entity);
		return true;
	}


	@IgnoreLog
	@Scheduled(fixedDelay = 5000)
	public void save() {
		if (CollectionUtils.isEmpty(sysLogCache)) {
			return;
		}
		ArrayList<SysLogEntity> list = new ArrayList<>(sysLogCache);
		sysLogCache.removeAll(list);
		// 先删缓存再存数据库 否则一条数据异常 导致后续日志全部无法落库
		saveBatch(list);
	}

	@Override
	public IPage<SysLogEntity> list(SysLogDTO dto) {
		LambdaQueryWrapper<SysLogEntity> wrapper = getWrapper(dto);
		return page(new Page<>(dto.getPage(), dto.getPageSize()), wrapper);
	}

	public void clearLog(int ago) {
		BizAssertUtils.isTrue(ago > 0, "必须大于0");
		this.remove(Wrappers.lambdaQuery(SysLogEntity.class)
				.lt(SysLogEntity::getRecordTime, DateUtils.addDays(new Date(), -ago)));
	}

	@Override
	public void export(SysLogDTO dto) {
		exportRecordService.save(ExportRecordEntity.init(getExportCode(), "操作日志", JsonUtils.toJsonString(dto)));
	}

	@Override
	public String getExportCode() {
		return "SYS_LOG";
	}

	@Override
	public ExportData getExportData(String conditions) {
		SysLogDTO dto = JsonUtils.parseObject(conditions, SysLogDTO.class);
		List<SysLogEntity> list = list(getWrapper(dto));
		return new ExportData(list, null);
	}

	private static LambdaQueryWrapper<SysLogEntity> getWrapper(SysLogDTO dto) {
		return Wrappers.lambdaQuery(SysLogEntity.class)
				.between(dto.getStartTime() != null && dto.getEndTime() != null, SysLogEntity::getRecordTime, dto.getStartTime(), dto.getEndTime())
				.orderByDesc(SysLogEntity::getRecordTime);
	}

	@Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 60 * 1000)
	public void clear() {
		clearLog(logSaveDay);
	}

	@Override
	public void destroy() throws Exception {
		save();
	}
}
