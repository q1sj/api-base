package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xsy.base.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Q1sj
 * @date 2023.4.23 16:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@TableName("sys_dictionary")
@Entity(name = "sys_dictionary")
@Table(indexes = {
        @Index(name = "uk_type_code_val", columnList = SysDictionaryEntity.Fields.type + "," + SysDictionaryEntity.Fields.code + "," + SysDictionaryEntity.Fields.value, unique = true)
})
public class SysDictionaryEntity extends BaseEntity {
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String value;
    @Column(nullable = false)
    private Integer sort;
}
