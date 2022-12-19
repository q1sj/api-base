package com.xsy.file.service;


import com.xsy.base.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件存储策略
 *
 * @author Q1sj
 * @date 2022.8.22 16:20
 */
public interface FileStorageStrategy {
    String digest(String path) throws IOException;

    /**
     * 获取文件内容
     * 容易导致oom 尽量使用{@link #getInputStream(String)}
     * @param path 相对路径
     * @return
     * @throws IOException
     */
    default byte[] getFileBytes(String path) throws IOException {
        InputStream is = getInputStream(path);
        return IOUtils.readFully(is, is.available());
    }

    /**
     * 获取文件内容
     *
     * @param path 相对路径
     * @return
     * @throws IOException
     */
    InputStream getInputStream(String path) throws IOException;

    /**
     * 保存文件
     * 容易导致oom 尽量使用{@link #saveFile(InputStream, String, String)}
     *
     * @param data
     * @param fileName 文件名
     * @param source   数据来源 会创建目录 避免出现不允许的特殊字符
     * @return 相对路径 通过nginx配置反向代理访问
     * @throws IOException
     */
    default String saveFile(byte[] data, String fileName, String source) throws IOException {
        return saveFile(new ByteArrayInputStream(data), fileName, source);
    }

    String saveFile(InputStream data, String fileName, String source) throws IOException;
    /**
     * 删除文件
     *
     * @param path
     * @throws IOException
     */
    void delete(String path) throws IOException;
}
