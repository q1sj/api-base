package com.xsy.base.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Q1sj
 * @date 2022.9.26 14:54
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    /**
     * 读取resource目录下文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream readResourceFile(String path) throws IOException {
        BizAssertUtils.isNotBlank(path, "path not blank");
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream();
    }

    public static String readResourceFileAsString(String path) throws IOException {
        InputStream is = readResourceFile(path);
        return IOUtils.toString(is, Charset.defaultCharset());
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileExtName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        String[] split = fileName.split("\\.");
        if (split.length > 1) {
            return split[split.length - 1].toLowerCase();
        }
        return "";
    }
}
