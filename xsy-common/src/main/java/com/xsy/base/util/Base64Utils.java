package com.xsy.base.util;

/**
 * @author Q1sj
 * @date 2022.9.26 14:56
 */
public class Base64Utils extends org.springframework.util.Base64Utils {
	public static byte[] decodeFromString(String src) {
		if (src == null) {
			return new byte[0];
		}
		src = src.replace("\n", "");
		src = src.replace("\r", "");
		return org.springframework.util.Base64Utils.decodeFromString(src);
	}
}
