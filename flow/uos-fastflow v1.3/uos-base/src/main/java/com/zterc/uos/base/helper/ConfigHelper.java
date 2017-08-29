package com.zterc.uos.base.helper;

public class ConfigHelper {
	private static boolean cacheOnly = false;

	public static boolean isCacheOnly() {
		return cacheOnly;
	}

	public static void setCacheOnly(boolean cacheOnly) {
		ConfigHelper.cacheOnly = cacheOnly;
	}
}
