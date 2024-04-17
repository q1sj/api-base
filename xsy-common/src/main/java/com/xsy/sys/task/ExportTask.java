package com.xsy.sys.task;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.xsy.base.util.CollectionUtils;
import com.xsy.base.util.Export;
import com.xsy.base.util.ExportContext;
import com.xsy.base.util.FileUtils;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.service.FileRecordService;
import com.xsy.sys.annotation.SysConfig;
import com.xsy.sys.entity.ExportRecordEntity;
import com.xsy.sys.enums.ExportStatusEnum;
import com.xsy.sys.service.ExportRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Q1sj
 * @date 2024.3.18 15:51
 */
@Slf4j
@Component
public class ExportTask {
	@Autowired
	private ExportContext exportContext;
	@Autowired
	private ExportRecordService exportRecordService;
	@Autowired
	private ExecutorService exportThreadPool;
	@Autowired
	private FileRecordService fileRecordService;
	@SysConfig("EXCEL_MAX_ROWS")
	private Integer excelMaxRows = 1000;

	@Scheduled(fixedDelay = 60 * 1000, initialDelay = 60 * 1000)
	public void run() {
		// 查询 待导出记录
		List<ExportRecordEntity> exportRecordList = this.exportRecordService.findByStatus(ExportStatusEnum.WAIT);
		// 修改状态 导出中
		exportRecordList.forEach(entity -> {
			entity.setStatus(ExportStatusEnum.ING.value);
			entity.setStatusName(ExportStatusEnum.ING.desc);
		});
		this.exportRecordService.updateBatchById(exportRecordList);
		// 导出
		for (ExportRecordEntity entity : exportRecordList) {
			export(entity);
		}
	}

	public void asyncExport(ExportRecordEntity entity) {
		exportThreadPool.submit(() -> export(entity));
	}

	private void export(ExportRecordEntity entity) {
		File zipFile = null;
		String excelSavePath = null;
		List<File> files = new ArrayList<>();
		try {
			Long id = entity.getId();
			// 防止重复执行
			entity = exportRecordService.getById(id);
			if (entity == null) {
				log.error("导出记录不存在 id:{}", id);
				return;
			}
			if (ExportStatusEnum.WAIT.value == entity.getStatus()) {
				entity.setStatus(ExportStatusEnum.ING.value);
				entity.setStatusName(ExportStatusEnum.ING.desc);
				exportRecordService.updateById(entity);
			} else {
				log.warn("id:{}导出中 不重复导出", id);
				return;
			}

			// 获取要导出数据
			String conditions = entity.getConditions();
			// 获取导出的excel
			Export.ExportData exportData = exportContext.getList(entity.getCode(), conditions);
			excelSavePath = exportExcel(exportData, entity);
			if (excelSavePath != null) {
				files.addAll(Arrays.asList(new File(excelSavePath).listFiles()));
			}
			// 获取导出的其他文件
			List<File> otherFile = exportData.getOtherFile();
			if (CollectionUtils.isNotEmpty(otherFile)) {
				files.addAll(otherFile);
			}
			log.info("开始压缩");
			// 压缩
			zipFile = File.createTempFile("export-" + System.currentTimeMillis(), ".zip");
			FileUtils.zipFiles(files, zipFile);
			// 记录fileRecord 保留原始文件名
			FileRecordEntity fileRecordEntity = fileRecordService.save(Files.newInputStream(zipFile.toPath()), zipFile.length(), entity.getFileName() + ".zip", "export", TimeUnit.DAYS.toMillis(30));
			// 打包完成 更新数据库导出状态
			entity.setStatus(ExportStatusEnum.SUCCESS.value);
			entity.setStatusName(ExportStatusEnum.SUCCESS.desc);
			entity.setFileId(fileRecordEntity.getId());
			entity.setProgress(100);
			entity.setExportTime((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - entity.getCreateTime().getTime()));
			this.exportRecordService.updateById(entity);
		} catch (Throwable e) {
			log.error("导出失败:{}", e.getMessage(), e);
			if (entity != null) {
				// 错误原因插入到数据库
				entity.setStatus(ExportStatusEnum.FAIL.value);
				entity.setStatusName(ExportStatusEnum.FAIL.desc);
				entity.setFailReason(e.getMessage());
				this.exportRecordService.updateById(entity);
			}
		} finally {
			if (excelSavePath != null) {
				FileUtils.deleteQuietly(new File(excelSavePath));
			}
			if (zipFile != null) {
				FileUtils.deleteQuietly(zipFile);
			}
		}
	}

	@Nullable
	private String exportExcel(Export.ExportData exportData, ExportRecordEntity entity) {
		List<?> excelData = exportData.getExcelData();
		if (CollectionUtils.isEmpty(excelData)) {
			return null;
		}
		// 导出
		String excelPath = System.getProperty("java.io.tmpdir");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// 生成导出目录
		String filePath = excelPath + File.separator + sdf.format(new Date()) + File.separator + entity.getFileName();
		// 目录不存在 自动创建
		new File(filePath).mkdirs();
		// 获取导出实体类
		Class<?> exportEntityClass = excelData.get(0).getClass();
		// 大list分割 多excel导出
		// 获取excel_max_rows
		log.debug("单表最大行数：" + excelMaxRows);
		// 分割
		List<? extends List<?>> lists = subList(excelData, excelMaxRows);
		for (int i = 0; i < lists.size(); i++) {
			log.info("{}/{}.xlsx导出中", filePath, i);
			ExcelWriterSheetBuilder builder = EasyExcel.write(filePath + File.separator + entity.getFileName() + "_" + i + ".xlsx", exportEntityClass).sheet("sheet1");
			if (CollectionUtils.isNotEmpty(exportData.getCellWriteHandlerList())) {
				for (CellWriteHandler cellWriteHandler : exportData.getCellWriteHandlerList()) {
					builder.registerWriteHandler(cellWriteHandler);
				}
			}
			builder.doWrite(lists.get(i));
			// 更新导出进度
			int progress = calculateProgress(i + 1, lists.size());
			entity.setProgress(progress);
			log.info("导出进度:{}%", progress);
			this.exportRecordService.updateById(entity);
		}
		return filePath;
	}

	/**
	 * 计算进度百分比,全部完成返回99%,等待压缩完成修改为100%
	 *
	 * @param complete
	 * @param total
	 * @return
	 */
	private int calculateProgress(int complete, int total) {
		if (complete == total) {
			return 99;
		}
		double progress = 100D * complete / total;
		return (int) progress;
	}

	/**
	 * 拆分list
	 *
	 * @param list
	 * @param maxSize
	 * @param <T>
	 * @return
	 */
	public static <T> List<List<T>> subList(List<T> list, int maxSize) {
		// size小于最大值直接返回
		if (list.size() <= maxSize) {
			return Collections.singletonList(list);
		}

		List<List<T>> result = new ArrayList<>();
		for (int i = 0; i < list.size(); i += maxSize) {
			// 检查是否下标越界
			if (i + maxSize <= list.size()) {
				result.add(list.subList(i, i + maxSize));
			} else {
				result.add(list.subList(i, list.size()));
			}
		}
		return result;
	}
}
