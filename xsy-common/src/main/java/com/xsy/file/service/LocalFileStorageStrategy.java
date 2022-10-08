package com.xsy.file.service;


import com.xsy.base.util.FileUtils;
import com.xsy.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件本地存储策略
 *
 * @author Q1sj
 * @date 2022.8.15 14:14
 */
public class LocalFileStorageStrategy implements FileStorageStrategy {

    @Value("${file.localStorage.basePath:/home/file}")
    private String basePath;

    @Override
    public byte[] getFileBytes(String path) throws IOException {
        return FileUtils.readFileToByteArray(new File(basePath + path));
    }

    @Override
    public String saveFile(byte[] data, String fileName, String source) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateFormat = sdf.format(new Date());
        String relativePathPrefix = StringUtils.isNotBlank(source) ? "/" + source : "";
        String relativePath = relativePathPrefix + "/" + dateFormat + "/" + fileName;
        String absolutePath = basePath + relativePath;
        File parentFile = new File(absolutePath).getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new IOException(parentFile.getPath() + "目录创建失败");
        }
        try (FileOutputStream fos = new FileOutputStream(absolutePath)) {
            fos.write(data);
        }
        return relativePath;
    }

    @Override
    public void delete(String path) throws IOException {
        path = basePath + path;
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException(path + "文件不存在");
        }
        if (!file.delete()) {
            throw new IOException(path + "删除失败");
        }
    }


}
