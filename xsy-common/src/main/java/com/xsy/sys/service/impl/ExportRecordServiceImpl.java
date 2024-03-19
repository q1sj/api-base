package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.PageData;
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
				.between(query.getStartTime() != null && query.getEndTime() != null, ExportRecordEntity::getExportTime, query.getStartTime(), query.getEndTime()));
		return new PageData<>(page);
	}

	@Override
	public List<ExportRecordEntity> findByStatus(ExportStatusEnum exportStatus) {
		return list(Wrappers.lambdaQuery(ExportRecordEntity.class)
				.eq(ExportRecordEntity::getStatus, exportStatus));
	}

	@Override
	public boolean save(ExportRecordEntity entity) {
		boolean save = super.save(entity);
		exportTask.asyncExport(entity);
		return save;
	}
}
