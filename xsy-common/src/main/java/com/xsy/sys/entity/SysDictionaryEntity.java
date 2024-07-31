package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xsy.base.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;


/**
 * @author Q1sj
 * @date 2023.4.23 16:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@TableName("sys_dictionary")

public class SysDictionaryEntity extends BaseEntity {
    private String dictType;
    private String dictCode;
    private String dictValue;
    private Integer dictSort;
}
