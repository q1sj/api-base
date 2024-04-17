package com.xsy.sys.controller;

import com.xsy.base.pojo.ChartDTO;
import com.xsy.base.util.EnumUtils;
import com.xsy.base.util.PageData;
import com.xsy.base.util.Result;
import com.xsy.sys.dto.ExportRecordQuery;
import com.xsy.sys.entity.ExportRecordEntity;
import com.xsy.sys.enums.ExportStatusEnum;
import com.xsy.sys.service.ExportRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * 导出记录
 *
 * @author Q1sj
 * @date 2024.3.18 16:05
 */
//@Validated
//@RestController
//@RequestMapping("/export/record")
public class BaseExportRecordController {
	@Autowired
	private ExportRecordService exportRecordService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public Result<PageData<ExportRecordEntity>> list(@Valid ExportRecordQuery query) {
		PageData<ExportRecordEntity> pageData = this.exportRecordService.list(query);
		return Result.ok(pageData);
	}

	/**
	 * 导出状态枚举
	 *
	 * @return
	 */
	@GetMapping("/status/enum")
	public Result<List<ChartDTO<String, Integer>>> status() {
		return Result.ok(EnumUtils.enumToCharts(ExportStatusEnum.class));
	}
}
