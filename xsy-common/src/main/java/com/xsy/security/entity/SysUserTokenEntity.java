/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户Token
 */
@Data
@TableName("sys_user_token")
@Entity(name = "sys_user_token")
public class SysUserTokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    @Column(nullable = false, unique = true,length = 20)
    private Long userId;
    /**
     * 用户token
     */
	@Column(nullable = false, unique = true,length = 100)
	private String token;
    /**
     * 过期时间
     */
    private Date expireDate;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

}
