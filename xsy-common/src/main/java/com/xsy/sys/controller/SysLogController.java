package com.xsy.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xsy.base.util.Result;
import com.xsy.sys.dto.SysLogDTO;
import com.xsy.sys.entity.SysLogEntity;
import com.xsy.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 系统日志记录
 *
 * @author Q1sj
 * @date 2023.11.9 10:19
 */
@RequestMapping("/sys/log")
@RestController
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 列表
	 *
	 * @param dto
	 * @return
	 */
	@GetMapping("/list")
	public Result<IPage<SysLogEntity>> list(@Validated SysLogDTO dto) {
		IPage<SysLogEntity> page = sysLogService.list(dto);
		return Result.ok(page);
	}
}
