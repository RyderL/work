package com.zterc.uos.base.dialect;

import com.zterc.uos.base.jdbc.Page;

/**
 * db2·½ÑÔ
 * 
 * @author gongyi
 */
public class Db2Dialect implements Dialect {
	public String getPageSql(String sql, Page<?> page) {
		StringBuffer pageSql = new StringBuffer(sql.length() + 100);
		pageSql.append("SELECT * FROM  ( SELECT B.*, ROWNUMBER() OVER() AS RN FROM ( ");
		pageSql.append(sql);
		long start = (page.getPageNo() - 1) * page.getPageSize() + 1;
		pageSql.append(" ) AS B )AS A WHERE A.RN BETWEEN ");
		pageSql.append(start);
		pageSql.append(" AND ");
		pageSql.append(start + page.getPageSize());
		return pageSql.toString();
	}

	@Override
	public String getFormatDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToNumber(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubstr(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String concat(String str, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}
}