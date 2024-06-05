package com.xsy.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Q1sj
 * @date 2024/6/5 下午3:27
 */
@Slf4j
public class SocketClient implements AutoCloseable {
	private static final int DEFAULT_SO_TIMEOUT = 5000;
	private static final int DEFAULT_CONNECT_TIMEOUT = 5000;

	private final String host;
	private final int port;
	private final Socket socket;


	private SocketClient(Socket socket) {
		this.socket = socket;
		this.host = socket.getInetAddress().getHostAddress();
		this.port = socket.getPort();
	}

	public static SocketClient connect(String host, int port) throws IOException {
		return connect(host, port, DEFAULT_SO_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
	}

	public static SocketClient connect(String host, int port, int connectTimeout, int readTimeout) throws IOException {
		Socket socket = new Socket();
		socket.setSoTimeout(readTimeout);
		socket.connect(new InetSocketAddress(host, port), connectTimeout);
		return new SocketClient(socket);
	}

	public void send(String msg) throws IOException {
		socket.getOutputStream().write(msg.getBytes());
		log.info("向{}:{} 发送string: {}", host, port, msg);
	}

	public void send(byte[] bytes) throws IOException {
		socket.getOutputStream().write(bytes);
		log.info("向{}:{} 发送hex: 0x{}", host, port, Hex.encodeHexString(bytes));
	}

	public byte[] read(int length) throws IOException {
		byte[] bytes = new byte[length];
		IOUtils.read(socket.getInputStream(), bytes);
		return bytes;
	}

	public String readJson() throws IOException {
		return IOUtils.readJson(socket.getInputStream());
	}

	@Override
	public void close() throws Exception {
		socket.close();
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
}
