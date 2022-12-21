package com.xsy.file.service;


import com.xsy.base.util.DigestUtils;
import com.xsy.base.util.FileUtils;
import com.xsy.base.util.IOUtils;
import com.xsy.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
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

    private String basePath;

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String digest(String path) throws IOException {
        try (InputStream is = getInputStream(path)){
            return DigestUtils.md5Hex(is);
        }
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        return new FileInputStream(basePath + path);
    }

    @Override
    public String saveFile(InputStream data, String fileName, String source) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateFormat = sdf.format(new Date());
        String relativePathPrefix = StringUtils.isNotBlank(source) ? "/" + source : "";
        String relativePath = relativePathPrefix + "/" + dateFormat + "/" + fileName;
        String absolutePath = basePath + relativePath;
        log.info("写入文件 size:{} path:{}", FileUtils.byteCountToDisplaySize(data.available()), absolutePath);
        File file = new File(absolutePath);
        FileUtils.forceMkdirParent(file);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            IOUtils.copy(data, fos);
        }
        return relativePath;
    }

    @Override
    public void delete(String path) throws IOException {
        path = basePath + path;
        log.info("删除文件 path:{}", path);
        File file = new File(path);
        if (!file.exists()) {
            log.warn("{}文件不存在", path);
            return;
        }
        if (!file.delete()) {
            throw new IOException(path + "删除失败");
        }
    }
}
