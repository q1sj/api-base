package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 参数管理
 *
 * @author Q1sj
 * @date 2022.9.22 8:58
 */
@Data
@TableName("sys_config")
@Entity(name = "sys_config")
@NoArgsConstructor
public class SysConfigEntity {
    /**
     * 键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @TableId(type = IdType.INPUT)
    @NotBlank
    private String configKey;
    /**
     * 参数值的数据类型
     * {@link com.xsy.sys.enums.SysConfigValueTypeEnum}
     */
    @Column(nullable = false)
    private String configValueType;
    /**
     * 值
     */
    @NotNull
    @Column(nullable = false, columnDefinition = "text")
    private String configValue;
    /**
     * 备注
     */
    private String remark;
    /**
     * 更新时间
     *
     * @ignore
     */
    private Date updateTime;

    public SysConfigEntity(String key, String value) {
        this.configKey = key;
        this.configValue = value;
        this.updateTime = new Date();
    }
}
