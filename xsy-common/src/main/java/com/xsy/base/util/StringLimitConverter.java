package com.xsy.base.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.poi.ss.SpreadsheetVersion;

/**
 * @author Q1sj
 * @date 2024.3.29 14:03
 */
public class StringLimitConverter implements Converter<String> {
	@Override
	public Class<?> supportJavaTypeKey() {
		return String.class;
	}

	@Override
	public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		if (value == null) {
			return new WriteCellData<>("");
		}
		if (value.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
			value = value.substring(0, SpreadsheetVersion.EXCEL2007.getMaxTextLength());
		}
		return new WriteCellData<>(value);
	}
}
