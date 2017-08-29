package com.zterc.uos.base.helper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;

/**
 * ���ڴ��� ������
 * 
 * @author gongyi
 * 
 */
public class DateHelper {
	private static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * ���ر�׼��ʽ�ĵ�ǰʱ��Timestamp
	 * 
	 * @return
	 */
	public static Timestamp getTimeStamp() {
		return new Timestamp(getTimeMillis());
	}

	/**
	 * ���ر�׼��ʽ�ĵ�ǰʱ��
	 * 
	 * @return
	 */
	public static String getTime() {
		return new DateTime().toString(DATE_FORMAT_DEFAULT);
	}

	/**
	 * ��������ʱ�����
	 * 
	 * @param date
	 * @return
	 */
	public static String parseTime(Object date) {
		if (date == null)
			return null;
		if (date instanceof Date) {
			return new DateTime((Date) date).toString(DATE_FORMAT_DEFAULT);
		} else if (date instanceof String) {
			return String.valueOf(date);
		}
		return "";
	}

	/**
	 * ��ȡ��ǰʱ��ĺ�����
	 * 
	 * @return
	 */
	public static long getTimeMillis() {
		return new DateTime().getMillis();
	}

	/**
	 * ����String������ΪDate����
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parse(String dateStr) {
		if (StringHelper.isEmpty(dateStr)) {
			return null;
		}
		try {
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException("���ڽ����쳣��" + dateStr);
		}
	}
}
