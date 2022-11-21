package com.xsy.base.util;

/**
 * @author Q1sj
 * @date 2022.11.21 14:33
 */
public class DigestUtils {
    public static String md5Hex(byte[] data) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
    }
}
