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
		Runnable r = () -> {
			try {
				HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
			} catch (GlobalException e) {
				e.printStackTrace();
			}
		};
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start();
		TimeUnit.SECONDS.sleep(5);
		r.run();
		TimeUnit.SECONDS.sleep(5);
		r.run();
	}

	@Test
	public void test() {
		String url = "http://www.qq.com";
		HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
		HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
		HttpUtils.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
	}

}