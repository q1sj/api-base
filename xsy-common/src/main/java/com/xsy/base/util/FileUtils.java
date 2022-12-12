package com.xsy.base.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Q1sj
 * @date 2022.9.26 14:54
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    /**
     * 读取resource目录下文件
     * 不要以/开头
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] readClassPathResourceFile(String path) throws IOException {
        BizAssertUtils.isNotBlank(path, "path not blank");
        ClassPathResource classPathResource = new ClassPathResource(path);
        try (InputStream inputStream = classPathResource.getInputStream()) {
            return IOUtils.readFully(inputStream, inputStream.available());
        }
    }
}
