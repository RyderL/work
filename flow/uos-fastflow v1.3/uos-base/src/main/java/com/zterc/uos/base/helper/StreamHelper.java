package com.zterc.uos.base.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 流操作 处理辅助类
 * 
 * @author gongyi
 * 
 */
public class StreamHelper {
	public static final int DEFAULT_CHUNK_SIZE = 1024;
	public static final int BUFFERSIZE = 4096;

	public static final String STREAM_ENCODING = "UTF-8";

	/**
	 * 从流里面读取字节数组
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		transfer(in, out);
		return out.toByteArray();
	}

	public static long transfer(InputStream in, OutputStream out)
			throws IOException {
		long total = 0;
		byte[] buffer = new byte[BUFFERSIZE];
		for (int count; (count = in.read(buffer)) != -1;) {
			out.write(buffer, 0, count);
			total += count;
		}
		return total;
	}

	/**
	 * 获取string的字节数组
	 * 
	 * @param xml
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getBytes(String xml)
			throws UnsupportedEncodingException {
		return xml.getBytes(STREAM_ENCODING);
	}

	/**
	 * 获取string的字节输入流
	 * 
	 * @param xml
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static InputStream getInputeStreamFromString(String xml)
			throws UnsupportedEncodingException {
		return new ByteArrayInputStream(xml.getBytes(STREAM_ENCODING));
	}

	/**
	 * 获取byte数组的字节输入流
	 * 
	 * @param bytes
	 * @return
	 */
	public static InputStream getInputeStreamFromBytes(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * 从类加载器根目录下加载资源文件返回输入流
	 * 
	 * @param name
	 * @return
	 */
	public static InputStream getInputStreamFromResource(String name) {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream(name);

		if (stream == null) {
			stream = StreamHelper.class.getClassLoader().getResourceAsStream(
					name);
		}
		return stream;
	}
}
