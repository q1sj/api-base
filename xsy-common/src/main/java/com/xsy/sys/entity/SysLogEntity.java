package com.xsy.sys.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2023.11.9 9:35
 */
@Data
@Entity(name = "sys_log")
@TableName("sys_log")
@ExcelIgnoreUnannotated
public class SysLogEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@TableId(type = IdType.ASSIGN_ID)
	@ExcelProperty(value = "id", converter = LongStringConverter.class)
	private Long id;
	/**
	 * 操作
	 */
	@ExcelProperty("用户操作")
	private String method;
	@ExcelProperty("ip")
	private String ip;
	@ExcelProperty("用户名")
	private String username;
	@ExcelProperty("请求方法")
	private String url;
	/**
	 * 参数
	 */
	@ExcelProperty("请求参数")
	@Column(columnDefinition = "text")
	private String args;
	/**
	 * 响应
	 */
	@ExcelProperty("响应")
	@Column(columnDefinition = "text")
	private String resp;
	/**
	 * 异常
	 */
	@ExcelProperty("异常")
	@Column(columnDefinition = "text")
	private String throwable;
	/**
	 * 耗时(ms)
	 */
	@ExcelProperty("执行耗时")
	private Long cost;
	/**
	 * 记录时间
	 */
	@ExcelProperty("创建时间")
	private Date recordTime;
	private Date createTime;
}