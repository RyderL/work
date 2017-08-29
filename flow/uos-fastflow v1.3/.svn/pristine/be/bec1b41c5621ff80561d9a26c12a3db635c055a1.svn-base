package com.zterc.uos.base.dialect;

import com.zterc.uos.base.jdbc.Page;

/**
 * 数据库差异的方言接口
 * 
 * @author gongyi
 */
public interface Dialect {
	/**
	 * 根据分页对象获取分页sql语句
	 * 
	 * @param sql
	 *            未分页sql语句
	 * @param page
	 *            分页对象
	 * @return
	 */
	String getPageSql(String sql, Page<?> page);
	
	String getFormatDate();
	
	String getToNumber(String str);
	
	String getSubstr(String str);
	
	String concat(String str,Object... params);
}
