package com.xsy.base.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ImageCompressor {

	public static byte[] compressImage(String formatName, InputStream inputStream, long targetFileSize) throws IOException {
		if ("jpg".equalsIgnoreCase(formatName) || "jpeg".equalsIgnoreCase(formatName)) {
			return compressJpgImage(inputStream, targetFileSize);
		} else if ("png".equalsIgnoreCase(formatName)) {
			return compressPngImage(inputStream, targetFileSize);
		} else {
			throw new IOException("Unsupported image format: " + formatName);
		}
	}

	public static byte[] compressPngImage(InputStream inputStream, long targetFileSize) throws IOException {
		// Convert PNG to JPEG before compression
		BufferedImage image = convertPngToJpg(inputStream);
		return compressJpgImage(image, targetFileSize);
	}

	public static byte[] compressJpgImage(InputStream inputStream, long targetFileSize) throws IOException {
		BufferedImage image = ImageIO.read(inputStream);
		return compressJpgImage(image, targetFileSize);
	}

	private static byte[] compressJpgImage(BufferedImage image, long targetFileSize) throws IOException {
		if (image == null) {
			throw new IOException("image == null!");
		}
		float quality = 0.95f;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (true) {
			baos.reset();
			ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
			if (!writers.hasNext()) throw new IllegalStateException("No writers found");
			ImageWriter writer = writers.next();
			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(quality);
			writer.setOutput(ios);
			writer.write(null, new IIOImage(image, null, null), param);
			writer.dispose();
			ios.close();

			if (baos.size() <= targetFileSize || quality <= 0.01f) {
				break;
			}
			quality -= 0.05f;
		}
		return baos.toByteArray();
	}

	public static BufferedImage convertPngToJpg(InputStream inputStream) throws IOException {
		BufferedImage pngImage = ImageIO.read(inputStream);
		BufferedImage jpgImage = new BufferedImage(
				pngImage.getWidth(),
				pngImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(pngImage, 0, 0, null);
		return jpgImage;
	}
}