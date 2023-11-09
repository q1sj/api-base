package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2023.11.9 9:35
 */
@Data
@Entity(name = "sys_log")
@TableName("sys_log")
public class SysLogEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 操作
	 */
	private String method;

	private String ip;
	private String username;
	private String url;
	/**
	 * 参数
	 */
	private String args;
	/**
	 * 响应
	 */
	private String resp;
	/**
	 * 异常
	 */
	private String throwable;
	/**
	 * 耗时(ms)
	 */
	private Long cost;
	/**
	 * 记录时间
	 */
	private Date recordTime;
	private Date createTime;
}