package com.xsy.sys.service;

import com.xsy.base.service.BaseService;
import com.xsy.base.util.PageData;
import com.xsy.sys.dto.ExportRecordQuery;
import com.xsy.sys.entity.ExportRecordEntity;
import com.xsy.sys.enums.ExportStatusEnum;

import java.util.List;

public interface ExportRecordService extends BaseService<ExportRecordEntity> {


	PageData<ExportRecordEntity> list(ExportRecordQuery query);

	List<ExportRecordEntity> findByStatus(ExportStatusEnum exportStatus);

}