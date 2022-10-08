package com.xsy.file.service;

import com.xsy.file.controller.FileRecordController;
import com.xsy.file.entity.FileRecordEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author Q1sj
 * @date 2022.8.15 14:02
 */
public interface FileRecordService {
    /**
     * 上传文件
     * controller demo {@link FileRecordController#upload}
     * @param file
     * @param source        数据来源
     * @param expireMs      文件过期时间
     * @param maxSize       文件最大阈值 单位:byte
     * @param fileExtension 合法文件后缀名
     * @return
     * @throws IOException
     */
    FileRecordEntity upload(MultipartFile file, String source, long expireMs,
                            long maxSize, List<String> fileExtension) throws IOException;

    /**
     * 保存文件
     *
     * @param data             文件内容
     * @param originalFilename 原始文件名
     * @param source           数据来源 避免使用文件名中不允许的字符
     * @param userId           上传用户id
     * @param ip               上传用户ip
     * @param expireMs         过期毫秒值
     * @return
     */
    FileRecordEntity save(byte[] data, String originalFilename, String source, String userId, String ip, long expireMs) throws IOException;

    /**
     * 获取文件内容
     *
     * @param path {@link FileRecordEntity#getPath()}
     * @return
     * @throws IOException
     */
    byte[] getFileBytes(String path) throws IOException;

    /**
     * 删除文件
     *
     * @param path {@link FileRecordEntity#getPath()}
     * @return
     */
    boolean delete(String path);
}
