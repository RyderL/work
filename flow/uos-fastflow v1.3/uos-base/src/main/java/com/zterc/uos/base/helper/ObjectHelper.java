package com.zterc.uos.base.helper;

public class ObjectHelper {

	public static boolean isNotEmpty(Object obj) {
		if (null == obj) {
			return false;
		}
		if (obj instanceof String && ((String) obj).trim().length() <= 0) {
			return false;
		}
		return true;
	}
}
