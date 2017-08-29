package com.zterc.uos.base.helper;

/**
 * Long 类型处理辅助类
 * 
 * @author gongyi
 * 
 */
public class LongHelper {

	/**
	 * String转Long
	 * 
	 * @param str
	 * @return
	 */
	public static Long valueOf(Object obj) {
		if (obj != null) {
			return Long.valueOf(obj.toString());
		}
		return null;
	}

}
