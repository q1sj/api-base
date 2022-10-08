package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * 参数管理
 *
 * @author Q1sj
 * @date 2022.9.22 8:58
 */
@Data
@TableName("sys_config")
@NoArgsConstructor
public class SysConfigEntity {
    /**
     * 键
     */
    @TableId(type = IdType.INPUT)
    @NotNull
    private String configKey;
    /**
     * 值
     */
    private String configValue;
    /**
     * 备注
     */
    private String remark;
    /**
     * 更新时间
     * @ignore
     */
    @Null
    private Date updateTime;

    public SysConfigEntity(String key, String value) {
        this.configKey = key;
        this.configValue = value;
        this.updateTime = new Date();
    }
}
