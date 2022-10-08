package com.xsy.file.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsy.file.entity.FileRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Q1sj
 * @date 2022.8.22 16:12
 */
@Mapper
@Repository
public interface FileRecordDao extends BaseMapper<FileRecordEntity> {
}
