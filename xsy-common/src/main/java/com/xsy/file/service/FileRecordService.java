package com.xsy.file.service;

import com.xsy.file.controller.FileRecordController;
import com.xsy.file.entity.FileRecordDTO;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.entity.UploadFileDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Q1sj
 * @date 2022.8.15 14:02
 */
public interface FileRecordService {
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
     * @param expireMs 过期毫秒值
     * @return
     * @throws IOException
     */
    FileRecordEntity save(File file, String source, long expireMs) throws IOException;

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
     * 获取文件内容
     *
     * @param path {@link FileRecordEntity#getPath()}
     * @return
     * @throws IOException
     */
    InputStream getInputStream(String path) throws IOException;

    /**
     * 获取文件内容
     *
     * @param path
     * @return
     * @throws IOException
     */
    FileRecordDTO getFileRecord(String path) throws IOException;

    /**
     * 删除文件
     *
     * @param path {@link FileRecordEntity#getPath()}
     * @return
     */
    boolean delete(String path);
}
