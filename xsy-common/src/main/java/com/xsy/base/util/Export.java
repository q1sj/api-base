package com.xsy.base.util;

import com.alibaba.excel.write.handler.CellWriteHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.io.File;
import java.util.List;

/**
 * @author Q1sj
 * @date 2020.12.16 16:33
 */
public interface Export {
	/**
	 * 获取导出事件类型
	 *
	 * @return
	 */
	String getExportCode();

	/**
	 * 获取需要导出的list
	 *
	 * @param conditions
	 * @return
	 */
	ExportData getExportData(String conditions);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class ExportData {
		@Nullable
		private List<?> excelData;
		@Nullable
		private List<File> otherFile;

		@Nullable
		private List<CellWriteHandler> cellWriteHandlerList;

		public ExportData(@Nullable List<?> excelData, @Nullable List<File> otherFile) {
			this.excelData = excelData;
			this.otherFile = otherFile;
		}
	}
}