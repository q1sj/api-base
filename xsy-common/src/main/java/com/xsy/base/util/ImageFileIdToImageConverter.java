package com.xsy.base.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.xsy.file.entity.FileRecordDTO;
import com.xsy.file.service.FileRecordService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Q1sj
 * @date 2024/5/29 下午3:08
 */
@Slf4j
public class ImageFileIdToImageConverter implements Converter<Long> {
	@Override
	public Class<?> supportJavaTypeKey() {
		return Long.class;
	}

	@Override
	public WriteCellData<?> convertToExcelData(Long value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		FileRecordService fileRecordService = SpringContextUtils.getBean(FileRecordService.class);
		FileRecordDTO fileRecord = null;
		try {
			fileRecord = fileRecordService.getFileRecord(value);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return new WriteCellData<>(Objects.toString(value));
		}
		if (fileRecord == null) {
			return new WriteCellData<>(Objects.toString(value));
		}
		try (InputStream content = fileRecord.getContent()) {
			return new WriteCellData<>(IOUtils.readFully(content, fileRecord.getFileSize().intValue()));
		}
	}
}