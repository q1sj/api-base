package com.xsy.base.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author Q1sj
 * @date 2024/6/5 下午3:27
 */
@Slf4j
public class SocketClient implements Closeable {
	/**
	 * 默认读取超时时间
	 */
	private static final int DEFAULT_SO_TIMEOUT = 5000;
	/**
	 * 默认连接超时时间
	 */
	private static final int DEFAULT_CONNECT_TIMEOUT = 5000;

	@Getter
	private final String hostname;
	@Getter
	private final int port;

	private final Socket socket;


	private SocketClient(Socket socket) {
		this.socket = socket;
		this.hostname = socket.getInetAddress().getHostAddress();
		this.port = socket.getPort();
	}

	public static SocketClient connect(String hostname, int port) throws IOException {
		return connect(hostname, port, DEFAULT_SO_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
	}

	public static SocketClient connect(String host, int port, int connectTimeout, int readTimeout) throws IOException {
		Socket socket = new Socket();
		socket.setSoTimeout(readTimeout);
		socket.connect(new InetSocketAddress(host, port), connectTimeout);
		return new SocketClient(socket);
	}

	public void write(String msg) throws IOException {
		write(msg, Charset.defaultCharset());
	}

	public void write(String msg, Charset charset) throws IOException {
		socket.getOutputStream().write(msg.getBytes(charset));
		log.info("向{}:{} 发送string: {}", hostname, port, msg);
	}

	public void write(byte[] bytes) throws IOException {
		socket.getOutputStream().write(bytes);
		log.info("向{}:{} 发送hex: 0x{}", hostname, port, Hex.encodeHexString(bytes));
	}

	public <T> T read(ReadFunction<InputStream, T> readFunction) throws IOException {
		return readFunction.apply(socket.getInputStream());
	}

	/**
	 * 读取固定长度的数据
	 *
	 * @param length 数据长度
	 * @return
	 * @throws IOException
	 */
	public byte[] read(int length) throws IOException {
		byte[] bytes = new byte[length];
		int read = IOUtils.read(socket.getInputStream(), bytes);
		if (read != length) {
			throw new IOException("不完整的数据,预期:" + length+"实际:"+read);
		}
		return bytes;
	}

	/**
	 * 读取到终止符返回数据
	 *
	 * @param terminator 终止符
	 * @return
	 * @throws IOException
	 */
	public byte[] read(byte terminator) throws IOException {
		return IOUtils.read(socket.getInputStream(), terminator);
	}

	/**
	 * 读取一个完整的json
	 *
	 * @return
	 * @throws IOException
	 */
	public String readJson() throws IOException {
		return IOUtils.readJson(socket.getInputStream());
	}

	/**
	 * 读取一个完整的json
	 *
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public String readJson(Charset charset) throws IOException {
		return IOUtils.readJson(socket.getInputStream(), charset);
	}

	/**
	 * 读取4byte的数据转换为int
	 *
	 * @return
	 * @throws IOException
	 */
	public int readInt() throws IOException {
		return IOUtils.readInt(socket.getInputStream());
	}

	/**
	 * 读取8byte的数据转换为long
	 *
	 * @return
	 * @throws IOException
	 */
	public long readLong() throws IOException {
		return IOUtils.readLong(socket.getInputStream());
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@FunctionalInterface
	public interface ReadFunction<T, R> {
		R apply(T t) throws IOException;
	}
}
