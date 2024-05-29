package com.xsy.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsy.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Q1sj
 * @date 2023.11.9 10:20
 */
@Mapper
public interface ApiLogDao extends BaseMapper<SysLogEntity> {
}
