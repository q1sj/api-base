/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.base.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类，所有实体都需要继承
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@FieldNameConstants
public abstract class BaseEntity implements Serializable {
    /**
     * id
     */
    @Id
    @TableId
    private Long id;
	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long creator;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}
