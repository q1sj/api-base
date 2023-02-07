package com.xsy.file.service;


import com.xsy.base.util.DigestUtils;
import com.xsy.base.util.FileUtils;
import com.xsy.base.util.IOUtils;
import com.xsy.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件本地存储策略
 *
 * @author Q1sj
 * @date 2022.8.15 14:14
 */
@Slf4j
public class LocalFileStorageStrategy implements FileStorageStrategy {
    private final String separator = "/";
    private String basePath;

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String digest(String path) throws IOException {
        try (InputStream is = getInputStream(path)) {
            return DigestUtils.md5Hex(is);
        }
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        return Files.newInputStream(Paths.get(getAbsolutePath(path)));
    }

    @Override
    public String saveFile(InputStream data, long length, String fileName, String source) throws IOException {
        String relativePath = getRelativePath(fileName, source);
        String absolutePath = getAbsolutePath(relativePath);
        File file = new File(absolutePath);
        FileUtils.forceMkdirParent(file);
        try (OutputStream os = Files.newOutputStream(file.toPath())) {
            IOUtils.copyLarge(data, os, 0, length, new byte[(int) FileUtils.ONE_MB]);
            log.info("写入文件 size:{} path:{}", FileUtils.byteCountToDisplaySize(length), absolutePath);
        }
        return relativePath;
    }

    @Override
    public void delete(String path) throws IOException {
        path = getAbsolutePath(path);
        log.info("删除文件 path:{}", path);
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException(path);
        }
        if (!file.delete()) {
            throw new IOException(path + "删除失败");
        }
    }

    /**
     * 获取相对路径
     *
     * @param fileName
     * @param source
     * @return
     */
    private String getRelativePath(String fileName, String source) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateFormat = sdf.format(new Date());
        String relativePathPrefix = StringUtils.isNotBlank(source) ? separator + source : "";
        // 固定前缀 方便nginx反向代理
        return "/file-storage" + relativePathPrefix + separator + dateFormat + separator + fileName;
    }

    /**
     * 获取绝对路径
     *
     * @param relativePath
     * @return
     */
    private String getAbsolutePath(String relativePath) {
        // 删除多余的分隔符
        if (basePath.endsWith(separator)) {
            return relativePath.startsWith(separator)
                    ? basePath + relativePath.substring(1)
                    : basePath + relativePath;
        }
        return relativePath.startsWith(separator)
                ? basePath + relativePath
                : basePath + separator + relativePath;
    }
}
