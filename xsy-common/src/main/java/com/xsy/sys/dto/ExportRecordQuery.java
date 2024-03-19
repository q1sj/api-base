package com.xsy.sys.dto;

import com.xsy.base.pojo.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExportRecordQuery extends PageQuery {
	/**
	 * 导出状态：1等待导出、2导出中、3导出成功、4导出失败
	 */
	private Integer status;
	private Date startTime;
	private Date endTime;
}