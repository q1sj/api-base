package com.xsy.base.util;

import org.junit.Test;

import java.util.Date;

public class DateFormatUtilsTest {

	@Test
	public void formatDate() {
		String format = DateFormatUtils.format(new Date());
		System.out.println(format);
	}

	@Test
	public void formatMillis() {
		String format = DateFormatUtils.format(System.currentTimeMillis());
		System.out.println(format);
	}
}