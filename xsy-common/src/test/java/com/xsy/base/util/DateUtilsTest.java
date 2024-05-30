package com.xsy.base.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtilsTest {

	@Test
	public void getNetDate() throws Exception {
		Date netDate = DateUtils.getNetDate();
	}

	@Test
	public void duration() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals(0, DateUtils.duration(sdf.parse("2024-05-30 11:01:00"), sdf.parse("2024-05-30 11:01:59"), TimeUnit.MINUTES));
		Assert.assertEquals(59, DateUtils.duration(sdf.parse("2024-05-30 11:01:00"), sdf.parse("2024-05-30 11:01:59"), TimeUnit.SECONDS));

	}
}