package com.xsy.base.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class IOUtilsTest {

	@Test
	public void readJson() throws IOException {
		// {"key":"value","key1":1}
		String json = "{\"key\":\"value\",\"key1\":1}";
		Assert.assertEquals(json, IOUtils.readJson(new ByteArrayInputStream(json.getBytes())));

		String jsonArray = "[{\"key\":\"value\",\"key1\":1}]";
		Assert.assertEquals(jsonArray, IOUtils.readJson(new ByteArrayInputStream(jsonArray.getBytes())));

		int i = 999;
		ByteBuffer buffer = ByteBuffer.allocate(4).putInt(i);
		Assert.assertEquals(i, IOUtils.readInt(new ByteArrayInputStream(buffer.array())));
	}
}