package com.zterc.uos.base.dialect;

import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.Page;

/**
 * Oracle数据库方言实现
 * 
 * @author gongyi
 */
public class OracleDialect implements Dialect {
	/**
	 * oracle分页通过rownum实现
	 */
	public String getPageSql(String sql, Page<?> page) {
		StringBuffer pageSql = new StringBuffer(sql.length() + 100);
		pageSql.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pageSql.append(sql);
		long start = (page.getPageNo() - 1) * page.getPageSize() + 1;
		pageSql.append(" ) row_ where rownum < ");
		pageSql.append(start + page.getPageSize());
		pageSql.append(" ) where rownum_ >= ");
		pageSql.append(start);
		return pageSql.toString();
	}

	@Override
	public String getFormatDate() {
		return "TO_DATE( ?,'yyyy-mm-dd hh24:mi:ss')";
	}

	@Override
	public String getToNumber(String str) {
		return "TO_NUMBER("+str+")";
	}

	@Override
	public String getSubstr(String str) {
		return "SUBSTR("+str+")";
	}

	@Override//UTC.PATH_CODE||'.'||?
	public String concat(String str, Object... params) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(str);
		for (int i = 0; i < params.length; i++) {
            sbf.append("||").append(StringHelper.valueOf(params[i]));
		}
		return sbf.toString();
	}
}
