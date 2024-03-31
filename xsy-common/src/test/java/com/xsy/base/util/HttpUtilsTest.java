package com.xsy.base.util;

import com.xsy.base.exception.GlobalException;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.concurrent.TimeUnit;

public class HttpUtilsTest {
	@Test
	public void testFuse() throws InterruptedException {
		String url = "http://127.0.0.1:12345";
		try {
			HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
		} catch (GlobalException e) {
			e.printStackTrace();
		}
		try {
			HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
		} catch (GlobalException e) {
			e.printStackTrace();
		}
		TimeUnit.SECONDS.sleep(5);
		HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);

	}

	@Test
	public void test() {
		String url = "http://www.qq.com";
		HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
		HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
		HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
	}

}