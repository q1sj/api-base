package com.xsy.sys.dto;

import com.xsy.base.pojo.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExportRecordQuery extends PageQuery {
	/**
	 * 导出状态：0等待导出、1导出中、2导出成功、3导出失败
	 */
	private Integer status;
	private String type;
	private Date startTime;
	private Date endTime;
}