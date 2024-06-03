package com.xsy.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * socket客户端短链接工具
 *
 * @author Q1sj
 * @date 2024.3.20 15:32
 */
@Slf4j
public class SocketClientUtils {
	/**
	 * 读取超时时间
	 */
	public static final int SO_TIMEOUT = 3000;
	/**
	 * 连接超时时间
	 */
	public static final int CONNECT_TIMEOUT = 1000;

	public static byte[] send(String hostname, int port, byte[] req) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		send(hostname, port, req, os);
		return os.toByteArray();
	}

	public static void send(String hostname, int port, byte[] req, OutputStream os) throws IOException {
		try (Socket socket = new Socket()) {
			socket.setSoTimeout(SO_TIMEOUT);
			socket.connect(new InetSocketAddress(hostname, port), CONNECT_TIMEOUT);
			log.info("向{}:{} 发送:0x{}", hostname, port, Hex.encodeHexString(req));
			try (OutputStream socketOs = socket.getOutputStream();
			     InputStream is = socket.getInputStream()) {
				socketOs.write(req);
				IOUtils.copy(is, os);
			}
		}
	}
}
