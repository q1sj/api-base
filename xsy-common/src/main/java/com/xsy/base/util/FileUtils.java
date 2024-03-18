package com.xsy.base.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Q1sj
 * @date 2022.9.26 14:54
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    /**
     * 读取resource目录下文件
     *
     * @param path 目录名/文件名 开头不要/ 例如 'json/xx.json'
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
    public static String getExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        return extension == null ? "" : extension;
    }

    public static void zipFiles(File[] filesToZip, File zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (File file : filesToZip) {
                // 将文件添加到ZIP文件中
                addToZip(file, zos, "");
            }
        }
    }

    private static void addToZip(File file, ZipOutputStream zos, String parentDirectory) throws IOException {
        // 构建ZIP条目的路径
        String entryPath = parentDirectory + file.getName();

        if (file.isDirectory()) {
            // 如果是目录，创建一个ZIP目录条目
            ZipEntry zipEntry = new ZipEntry(entryPath + "/");
            zos.putNextEntry(zipEntry);

            // 递归处理目录下的文件
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File nestedFile : files) {
                addToZip(nestedFile, zos, entryPath + "/");
            }
        } else {
            // 如果是文件，创建一个ZIP文件条目
            ZipEntry zipEntry = new ZipEntry(entryPath);
            zos.putNextEntry(zipEntry);

            // 将文件内容写入ZIP输出流
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
            }
        }
    }
}
