package com.zterc.uos.base.dialect;

import com.zterc.uos.base.jdbc.Page;

/**
 * Postgresql数据库方言实现
 * 
 * @author gongyi
 */
public class PostgresqlDialect implements Dialect {
	/**
	 * Postgresql分页通过limit实现
	 */
	public String getPageSql(String sql, Page<?> page) {
		StringBuffer pageSql = new StringBuffer(sql.length() + 100);
		pageSql.append(getPageBefore(sql, page));
		pageSql.append(sql);
		pageSql.append(getPageAfter(sql, page));
		return pageSql.toString();
	}

	public String getPageBefore(String sql, Page<?> page) {
		return "";
	}

	public String getPageAfter(String sql, Page<?> page) {
		long start = (page.getPageNo() - 1) * page.getPageSize();
		StringBuffer sb = new StringBuffer();
		sb.append(" limit ").append(page.getPageSize()).append(" offset ")
				.append(start);
		return sb.toString();
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