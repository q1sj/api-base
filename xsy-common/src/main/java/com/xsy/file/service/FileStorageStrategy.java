package com.xsy.file.service;


import java.io.IOException;

/**
 * 文件存储策略
 *
 * @author Q1sj
 * @date 2022.8.22 16:20
 */
public interface FileStorageStrategy {
    /**
     * 获取文件内容
     *
     * @param path 相对路径
     * @return
     * @throws IOException
     */
    byte[] getFileBytes(String path) throws IOException;

    /**
     * 保存文件
     *
     * @param data
     * @param fileName 文件名
     * @param source   数据来源 会创建目录 避免出现不允许的特殊字符
     * @return 相对路径 通过nginx配置反向代理访问
     * @throws IOException
     */
    String saveFile(byte[] data, String fileName, String source) throws IOException;

    /**
     * 删除文件
     *
     * @param path
     * @throws IOException
     */
    void delete(String path) throws IOException;
}
