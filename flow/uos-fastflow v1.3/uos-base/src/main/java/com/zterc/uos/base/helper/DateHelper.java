package com.zterc.uos.base.helper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;

/**
 * 日期处理 辅助类
 * 
 * @author gongyi
 * 
 */
public class DateHelper {
	private static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 返回标准格式的当前时间Timestamp
	 * 
	 * @return
	 */
	public static Timestamp getTimeStamp() {
		return new Timestamp(getTimeMillis());
	}

	/**
	 * 返回标准格式的当前时间
	 * 
	 * @return
	 */
	public static String getTime() {
		return new DateTime().toString(DATE_FORMAT_DEFAULT);
	}

	/**
	 * 解析日期时间对象
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
	 * 获取当前时间的毫秒数
	 * 
	 * @return
	 */
	public static long getTimeMillis() {
		return new DateTime().getMillis();
	}

	/**
	 * 解析String的日期为Date类型
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
			throw new RuntimeException("日期解析异常：" + dateStr);
		}
	}
}
