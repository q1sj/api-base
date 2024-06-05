package com.xsy.base.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SocketClientTest {
	@Test
	public void test() throws Exception {
		try (SocketClient socketClient = SocketClient.connect("192.168.2.111", 8092)) {
			socketClient.send("hello world");
			String json = socketClient.readJson();
			log.info(json);
			byte[] bytes = socketClient.read(1);
			log.info(new String(bytes));
			socketClient.send("bye".getBytes());
		}
	}
}