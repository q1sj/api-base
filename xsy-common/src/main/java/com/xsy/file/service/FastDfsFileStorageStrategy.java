package com.xsy.file.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO FastDfs存储策略
 * @author Q1sj
 * @date 2022.11.21 16:01
 */
public class FastDfsFileStorageStrategy implements FileStorageStrategy{
    @Override
    public String digest(String path) throws IOException {
        return "";
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        return null;
    }

    @Override
    public String saveFile(InputStream data, String fileName, String source) throws IOException {
        return null;
    }

    @Override
    public void delete(String path) throws IOException {

    }
}
