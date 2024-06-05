package com.xsy.base.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Q1sj
 * @date 2022.12.19 9:56
 */
public class IOUtils extends org.apache.commons.io.IOUtils {

	public static String readJson(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bracesCount = 0;
		boolean inString = false;
		boolean escaped = false;

		int b;
		while ((b = is.read()) != -1) {
			baos.write(b);
			char c = (char) b;

			if (inString) {
				if (c == '\\' && !escaped) {
					escaped = true;
				} else if (c == '"' && !escaped) {
					inString = false;
				} else {
					escaped = false;
				}
			} else {
				if (c == '"') {
					inString = true;
				} else if (c == '{') {
					bracesCount++;
				} else if (c == '}') {
					bracesCount--;
					if (bracesCount == 0) {
						break;
					}
				}
			}
		}

		if (bracesCount != 0) {
			throw new IOException("Incomplete JSON object");
		}
		return baos.toString("UTF-8");
	}
}
