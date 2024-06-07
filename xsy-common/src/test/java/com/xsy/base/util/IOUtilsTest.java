package com.xsy.base.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class IOUtilsTest {
	// {"key":"value","key1":1,"key3":false,"key4":[1,2,3]}
	String json = "{\"key\":\"value\",\"key1\":1,\"key3\":false,\"key4\":[1,2,3]}";

	String jsonArray = "[" + json + "," + json + "]";

	@Test
	public void readJson() throws IOException {

		Assert.assertEquals(json, IOUtils.readJson(new ByteArrayInputStream(json.getBytes())));

		Assert.assertEquals(jsonArray, IOUtils.readJson(new ByteArrayInputStream(jsonArray.getBytes())));

		int i = 999;
		ByteBuffer buffer = ByteBuffer.allocate(4).putInt(i);
		Assert.assertEquals(i, IOUtils.readInt(new ByteArrayInputStream(buffer.array())));
	}

	@Test
	public void tcpStickyPacket() throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream((json + jsonArray).getBytes());

		Assert.assertEquals(json, IOUtils.readJson(byteArrayInputStream));
		Assert.assertEquals(jsonArray, IOUtils.readJson(byteArrayInputStream));
	}

	@Test
	public void testJsonEscape() throws IOException {
		String json = "{\"key\":\"{\"}";
		Assert.assertEquals(json, IOUtils.readJson(new ByteArrayInputStream(json.getBytes())));

		json = "{\"key\":\"}\"}";
		Assert.assertEquals(json, IOUtils.readJson(new ByteArrayInputStream(json.getBytes())));
		// {"key":"{\"a\":\"{\"}"}
		json = "{\"key\":\"{\\\"a\\\":123}\"}";
		Assert.assertEquals(json, IOUtils.readJson(new ByteArrayInputStream(json.getBytes())));
	}
}