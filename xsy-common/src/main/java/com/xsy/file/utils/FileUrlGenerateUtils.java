package com.xsy.file.utils;

import com.xsy.file.controller.FileRecordController;
import com.xsy.file.entity.FileRecordEntity;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Q1sj
 * @date 2022.9.5 15:25
 */
public class FileUrlGenerateUtils {

    /**
     * 生成图片链接
     *
     * @param path {@link FileRecordEntity#getPath()}
     * @return
     */
    public static String imgUrl(String path) {
        try {
            return FileRecordController.REQUEST_MAPPING + (StringUtils.isNotBlank(path)
                    ? FileRecordController.IMG_MAPPING + "?path=" + URLEncoder.encode(path, StandardCharsets.UTF_8.displayName())
                    : FileRecordController.NOT_FOUND);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 生成下载链接
     *
     * @param path {@link FileRecordEntity#getPath()}
     * @return
     */
    public static String downloadUrl(String path) {
        try {
            return FileRecordController.REQUEST_MAPPING + (StringUtils.isNotBlank(path)
                    ? FileRecordController.DOWNLOAD_MAPPING + "?path=" + URLEncoder.encode(path, StandardCharsets.UTF_8.displayName())
                    : FileRecordController.NOT_FOUND);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
