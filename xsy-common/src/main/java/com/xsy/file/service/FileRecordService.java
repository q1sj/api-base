package com.xsy.file.service;

import com.xsy.file.controller.FileRecordController;
import com.xsy.file.entity.FileRecordDTO;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.entity.UploadFileDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Q1sj
 * @date 2022.8.15 14:02
 */
public interface FileRecordService {
	/**
	 * 永不过期
	 */
	int NO_EXPIRE = -1;

	/**
	 * 上传文件
	 * controller demo {@link FileRecordController#upload}
	 *
	 * @param uploadFileDTO
	 * @return
	 * @throws IOException
	 */
	FileRecordEntity upload(UploadFileDTO uploadFileDTO) throws IOException;

	/**
	 * 保存文件
	 *
	 * @param file     文件
	 * @param source   数据来源 避免使用文件名中不允许的字符
	 * @param expireMs 过期毫秒值  <= 0 不过期
	 * @return
	 * @throws IOException
	 */
	FileRecordEntity save(File file, String source, long expireMs) throws IOException;

	/**
	 * 保存文件 自定义fileId 用于预生成下载链接
	 *
	 * @param id
	 * @param file
	 * @param source
	 * @param expireMs
	 * @return
	 * @throws IOException
	 */
	FileRecordEntity save(long id, File file, String source, long expireMs) throws IOException;

	/**
	 * 保存文件
	 *
	 * @param data             文件内容
	 * @param fileSize         文件大小
	 * @param originalFilename 原始文件名
	 * @param source           数据来源 避免使用文件名中不允许的字符
	 * @param expireMs         过期毫秒值
	 * @return
	 */
	FileRecordEntity save(InputStream data, long fileSize, String originalFilename, String source, long expireMs) throws IOException;

	/**
	 * 保存文件 自定义fileId 用于预生成下载链接
	 *
	 * @param id
	 * @param data
	 * @param fileSize
	 * @param originalFilename
	 * @param source
	 * @param expireMs
	 * @return
	 * @throws IOException
	 */
	FileRecordEntity save(long id, InputStream data, long fileSize, String originalFilename, String source, long expireMs) throws IOException;

	/**
	 * 从URL中下载文件保存
	 *
	 * @param url
	 * @param originalFilename
	 * @param source
	 * @param expireMs
	 * @return
	 * @throws IOException
	 */
	FileRecordEntity save(URL url, String originalFilename, String source, long expireMs) throws IOException;

	FileRecordEntity save(long id, URL url, String originalFilename, String source, long expireMs) throws IOException;
	/**
	 * 获取文件内容
	 *
	 * @param path {@link FileRecordEntity#getPath()}
	 * @return
	 * @throws IOException
	 */
	InputStream getInputStream(String path) throws IOException;

	InputStream getInputStream(Long fileId) throws IOException;

	/**
	 * 获取文件内容
	 *
	 * @param path
	 * @return
	 * @throws IOException
	 */
	FileRecordDTO getFileRecord(String path) throws IOException;

	FileRecordDTO getFileRecord(Long fileId) throws IOException;

	/**
	 * 删除文件
	 *
	 * @param path {@link FileRecordEntity#getPath()}
	 * @return
	 */
	boolean delete(String path);

	boolean delete(Long fileId);

	/**
	 * 更新文件过期时间
	 *
	 * @param fileId   文件id
	 * @param expireMs 过期时间=当前时间+expireMs expireMs<=0 永不过期
	 */
	void updateExpireTime(long fileId, long expireMs);
}
