package com.zterc.uos.base.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializerHelper {
	// private static Logger logger = LoggerFactory
	// .getLogger(SerializerHelper.class);

	public static byte[] serialize(Object object) {
		if (object == null) {
			return null;
		}
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.flush();
			bytes = bos.toByteArray();
		} catch (IOException ex) {
			throw new RuntimeException("object to byte array IOException.", ex);
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("object to byte array IOException.",
						e);
			}
		}
		return bytes;
	}

	public static Object deSerialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (IOException ex) {
			throw new RuntimeException("object to byte array IOException.", ex);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("class not found exception.", e);
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("object to byte array IOException.",
						e);
			}
		}
		return obj;
	}
}
