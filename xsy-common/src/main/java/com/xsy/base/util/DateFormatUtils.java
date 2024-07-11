package com.xsy.base.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2024.4.24 10:27
 */
public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static String format(Date date) {
		return org.apache.commons.lang3.time.DateFormatUtils.format(date, DEFAULT_PATTERN);
	}

	public static String format(long millis) {
		return org.apache.commons.lang3.time.DateFormatUtils.format(millis, DEFAULT_PATTERN);
	}

	public static Date parse(String source) throws ParseException {
		return parse(source, DEFAULT_PATTERN);
	}

	public static Date parse(String source, String pattern) throws ParseException {
		FastDateFormat fastDateFormat = FastDateFormat.getInstance(pattern);
		return fastDateFormat.parse(source);
	}
}
