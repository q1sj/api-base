package com.xsy.sys.controller;

import com.xsy.base.util.PageData;
import com.xsy.base.util.Result;
import com.xsy.sys.dto.ExportRecordQuery;
import com.xsy.sys.entity.ExportRecordEntity;
import com.xsy.sys.service.ExportRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 导出记录
 *
 * @author Q1sj
 * @date 2024.3.18 16:05
 */
@Validated
@RestController
@RequestMapping("/export/record")
public class ExportRecordController {
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
}
