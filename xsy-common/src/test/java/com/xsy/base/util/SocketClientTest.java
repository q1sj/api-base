package com.xsy.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

@Slf4j
public class SocketClientTest {
	@Test
	public void test() throws Exception {
		try (SocketClient socketClient = SocketClient.connect("192.168.2.111", 8092)) {
			socketClient.write("hello world");
			int i = socketClient.read(IOUtils::readInt); // int i = socketClient.readInt();
			log.info("{}", i);
			byte[] read = socketClient.read(is -> IOUtils.read(is, (byte) 0xFF));
			log.info("{}", Hex.encodeHexString(read));
			byte[] bytes = socketClient.read(2);
			log.info(new String(bytes));
			String json = socketClient.readJson();
			log.info(json);
			socketClient.write("bye".getBytes());
		}
	}
}