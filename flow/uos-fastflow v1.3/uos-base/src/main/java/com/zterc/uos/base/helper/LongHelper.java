package com.zterc.uos.base.helper;

/**
 * Long ���ʹ�������
 * 
 * @author gongyi
 * 
 */
public class LongHelper {

	/**
	 * StringתLong
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
