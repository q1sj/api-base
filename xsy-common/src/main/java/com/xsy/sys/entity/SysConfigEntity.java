package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Id
    @TableId(type = IdType.INPUT)
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "key只允许英文数字-_")
    private String configKey;
    /**
     * 参数值的数据类型
     * {@link com.xsy.sys.enums.SysConfigValueTypeEnum}
     */
    private String configValueType;
    /**
     * 值
     */
    @NotNull
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
