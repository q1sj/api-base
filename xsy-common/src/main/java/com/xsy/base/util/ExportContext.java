package com.xsy.base.util;

import com.xsy.base.exception.GlobalException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Q1sj
 * @date 2020.12.16 16:33
 */
@Data
@Component
public class ExportContext {
	private static Map<String, Export> exportMap = new HashMap<>(8);

	/**
	 * 导出数据list
	 *
	 * @param code
	 * @param conditions
	 * @return
	 */
	public List<?> getList(String code, String conditions) {
		return getExport(code).getExportList(conditions);
	}

	private Export getExport(String code) {
		Export export = exportMap.get(code);
		if (export == null) {
			throw new GlobalException("当前code：" + code + "不能导出");
		}
		return export;
	}

	@Autowired
	public ExportContext(List<Export> exportList) {
		exportList.forEach(export -> exportMap.put(export.getExportCode(), export));
	}
}
