package com.xsy.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsy.sys.dto.SysLogDTO;
import com.xsy.sys.entity.SysLogEntity;

/**
 * @author Q1sj
 * @date 2023.11.9 10:20
 */
public interface SysLogService extends IService<SysLogEntity> {
	IPage<SysLogEntity> list(SysLogDTO dto);

	void clearLog(int ago);
}
