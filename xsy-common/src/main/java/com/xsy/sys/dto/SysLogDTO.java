package com.xsy.sys.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2023.11.9 10:38
 */
@Data
public class SysLogDTO {
	/**
	 * 开始时间
	 */
	@NotNull
	private Date startTime;
	/**
	 * 结束时间
	 */
	@NotNull
	private Date endTime;

	@NotNull
	private Integer page;
	@NotNull
	private Integer pageSize;
}
