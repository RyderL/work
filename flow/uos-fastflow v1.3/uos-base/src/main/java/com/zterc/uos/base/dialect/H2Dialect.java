package com.zterc.uos.base.dialect;

import com.zterc.uos.base.jdbc.Page;

/**
 * H2���ݿⷽ��ʵ��
 * 
 * @author gongyi
 */
public class H2Dialect implements Dialect {
	/**
	 * mysql��ҳͨ��limitʵ��
	 */
	public String getPageSql(String sql, Page<?> page) {
		StringBuffer pageSql = new StringBuffer(sql.length() + 100);
		pageSql.append(sql);
		long start = (page.getPageNo() - 1) * page.getPageSize();
		pageSql.append(" limit ").append(start).append(",")
				.append(page.getPageSize());
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