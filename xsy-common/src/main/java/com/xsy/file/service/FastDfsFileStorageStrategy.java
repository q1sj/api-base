package com.xsy.file.service;

import java.io.IOException;

/**
 * TODO FastDfs存储策略
 * @author Q1sj
 * @date 2022.11.21 16:01
 */
public class FastDfsFileStorageStrategy implements FileStorageStrategy{
    @Override
    public byte[] getFileBytes(String path) throws IOException {
        return new byte[0];
    }

    @Override
    public String saveFile(byte[] data, String fileName, String source) throws IOException {
        return null;
    }

    @Override
    public void delete(String path) throws IOException {

    }
}
