package com.zterc.uos.base.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * �ַ��������
 * 
 * @author gongyi
 * 
 */
public class StringHelper {

	/**
	 * �ж��ַ��Ƿ�Ϊ��
	 * 
	 * @param str
	 *            �ַ�
	 * @return �Ƿ�Ϊ�ձ�ʶ
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0||"null".equalsIgnoreCase(str);
	}

	public static String valueOf(Object obj) {
		if (obj == null) {
			return null;
		}
		return String.valueOf(obj);
	}

	/**
	 * ������������
	 * 
	 * @param order
	 *            ������
	 * @param orderby
	 *            �����ֶ�
	 * @return ��װ�õ�����sql
	 */
	public static String buildPageOrder(String order, String orderby) {
		if (isEmpty(orderby) || isEmpty(order))
			return "";
		String[] orderByArray = StringUtils.split(orderby, ',');
		String[] orderArray = StringUtils.split(order, ',');
		if (orderArray.length != orderByArray.length)
			throw new RuntimeException("��ҳ�������������,�����ֶ���������ĸ������");
		StringBuilder orderStr = new StringBuilder(30);
		orderStr.append(" order by ");

		for (int i = 0; i < orderByArray.length; i++) {
			orderStr.append(orderByArray[i]).append(" ").append(orderArray[i])
					.append(" ,");
		}
		orderStr.deleteCharAt(orderStr.length() - 1);
		return orderStr.toString();
	}

	public static String[] split(String string1, String string2) {
		List<String> list = new ArrayList<String>();
		if (string1 != null) {
			int i = 0;
			int j = 0;
			int k = string2.length();
			while ((j = string1.indexOf(string2, i)) >= 0) {
				list.add(string1.substring(i, j));
				i = j + k;
			}
			if (i < string1.length()) {
				list.add(string1.substring(i, string1.length()));
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
}
