package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.DateUtils;
import com.xsy.base.util.PageData;
import com.xsy.base.util.StringUtils;
import com.xsy.security.user.SecurityUser;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.dao.ExportRecordDao;
import com.xsy.sys.dto.ExportRecordQuery;
import com.xsy.sys.entity.ExportRecordEntity;
import com.xsy.sys.enums.ExportStatusEnum;
import com.xsy.sys.service.ExportRecordService;
import com.xsy.sys.task.ExportTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Q1sj
 * @date 2024.3.18 15:58
 */
@Slf4j
@Service
public class ExportRecordServiceImpl extends ServiceImpl<ExportRecordDao, ExportRecordEntity> implements ExportRecordService {
	@Autowired
	private ExportTask exportTask;

	@Override
	public PageData<ExportRecordEntity> list(ExportRecordQuery query) {
		// 非管理员用户只能查看自己的导出记录
		UserDetail user = SecurityUser.getUser();
		IPage<ExportRecordEntity> page = page(query.initPage(), Wrappers.lambdaQuery(ExportRecordEntity.class)
				.eq(!user.isAdmin(), ExportRecordEntity::getUserId, user.getId())
				.eq(query.getStatus() != null, ExportRecordEntity::getStatus, query.getStatus())
				.eq(StringUtils.isNotBlank(query.getType()), ExportRecordEntity::getCode, query.getType())
				.between(query.getStartTime() != null && query.getEndTime() != null, ExportRecordEntity::getExportTime, query.getStartTime(), query.getEndTime())
				.orderByDesc(ExportRecordEntity::getCreateTime)
		);
		return new PageData<>(page);
	}

	@Override
	public List<ExportRecordEntity> findByStatus(ExportStatusEnum exportStatus) {
		return list(Wrappers.lambdaQuery(ExportRecordEntity.class)
				.eq(ExportRecordEntity::getStatus, exportStatus.getValue()));
	}

	@Override
	public boolean save(ExportRecordEntity entity) {
		boolean save = super.save(entity);
		exportTask.asyncExport(entity);
		return save;
	}

	@Override
	public void clearTimeoutRecord() {
		update(Wrappers.lambdaUpdate(ExportRecordEntity.class)
				.eq(ExportRecordEntity::getStatus, ExportStatusEnum.ING.value)
				.lt(ExportRecordEntity::getCreateTime, DateUtils.addDays(new Date(), -1))
				.set(ExportRecordEntity::getStatus, ExportStatusEnum.FAIL.value)
				.set(ExportRecordEntity::getStatusName, ExportStatusEnum.FAIL.desc)
				.set(ExportRecordEntity::getFailReason, "导出超时")
				.set(ExportRecordEntity::getUpdateTime, new Date())
		);
	}
}
