package com.xsy.base.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SocketClientTest {
	@Test
	public void test() throws Exception {
		try (SocketClient socketClient = SocketClient.connect("192.168.2.111", 8092)) {
			socketClient.write("hello world");
			byte[] bytes = socketClient.read(2);
			log.info(new String(bytes));
			String json = socketClient.readJson();
			log.info(json);
			socketClient.write("bye".getBytes());
		}
	}
}