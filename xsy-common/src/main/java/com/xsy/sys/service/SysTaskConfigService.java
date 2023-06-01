package com.xsy.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsy.base.util.PageData;
import com.xsy.sys.dto.SysTaskConfigQuery;
import com.xsy.sys.entity.SysTaskConfigEntity;

/**
 * @author Q1sj
 * @date 2023.3.7 15:23
 */
public interface SysTaskConfigService extends IService<SysTaskConfigEntity> {
	PageData<SysTaskConfigEntity> page(SysTaskConfigQuery query);

	void run(Integer id);
}

