package com.xsy.file.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.ErrorCodeConstants;
import com.github.tobato.fastdfs.exception.FdfsException;
import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xsy.base.util.DigestUtils;
import com.xsy.base.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

/**
 * FastDfs存储策略
 *
 * @author Q1sj
 * @date 2022.11.21 16:01
 */
@Slf4j
public class FastDfsFileStorageStrategy implements FileStorageStrategy {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public String digest(String path) throws IOException {
        try (InputStream is = getInputStream(path)) {
            return DigestUtils.md5Hex(is);
        }
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        StorePath storePath = StorePath.parseFromUrl(path);
        try {
            return fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), is -> is);
        } catch (FdfsException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String saveFile(InputStream data, long length, String fileName, String source) throws IOException {
        try {
            StorePath storePath = fastFileStorageClient.uploadFile(data, length, FileUtils.getExtension(fileName), null);
            return storePath.getFullPath();
        } catch (FdfsException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void delete(String path) throws IOException {
        try {
            fastFileStorageClient.deleteFile(path);
        } catch (FdfsServerException e) {
            if (ErrorCodeConstants.ERR_NO_ENOENT == e.getErrorCode()) {
                log.warn(e.getMessage());
                return;
            }
            throw new IOException(e);
        } catch (FdfsException e) {
            throw new IOException(e);
        }
    }
}
