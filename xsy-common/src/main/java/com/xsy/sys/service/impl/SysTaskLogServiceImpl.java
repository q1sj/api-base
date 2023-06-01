package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.sys.dao.SysTaskLogDao;
import com.xsy.sys.entity.SysTaskLogEntity;
import com.xsy.sys.service.SysTaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Q1sj
 * @date 2023.6.1 15:01
 */
@Slf4j
@Service
public class SysTaskLogServiceImpl extends ServiceImpl<SysTaskLogDao, SysTaskLogEntity> implements SysTaskLogService {
}
