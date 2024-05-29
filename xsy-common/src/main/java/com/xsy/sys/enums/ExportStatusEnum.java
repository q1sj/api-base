package com.xsy.sys.enums;

import com.xsy.base.enums.BaseEnum;
import lombok.AllArgsConstructor;

/**
 * 导出状态枚举
 *
 * @author Q1sj
 * @date 2021.4.9 17:05
 */
@AllArgsConstructor
public enum ExportStatusEnum implements BaseEnum<Integer, String> {
	//0待导出、 1导出中、2导出成功、3导出失败
	WAIT(0, "待导出"),
	ING(1, "导出中"),
	SUCCESS(2, "导出成功"),
	FAIL(3, "导出失败"),
	;

	public final int value;
	public final String desc;

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public String getDesc() {
		return desc;
	}
}