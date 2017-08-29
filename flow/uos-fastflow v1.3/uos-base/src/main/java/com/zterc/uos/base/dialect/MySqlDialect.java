package com.zterc.uos.base.dialect;

import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.Page;

/**
 * Mysql���ݿⷽ��ʵ��
 * 
 * @author gongyi
 */
public class MySqlDialect implements Dialect {
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
	
	/**
	 * mysql���ڱȽ�
	 */
	public String getFormatDate(){
		return "STR_TO_DATE( ?,'%Y-%m-%d %H:%i:%s')";
	}

	@Override
	public String getToNumber(String str) {
		return "CONVERT("+str+",SIGNED)";//���� : SIGNED
	}

	@Override
	public String getSubstr(String str) {
		return "SUBSTRING("+str+")";
	}

	@Override//CONCAT(UTC.PATH_CODE,'.',?)
	public String concat(String str, Object... params) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("CONCAT(").append(str);
		for (int i = 0; i < params.length; i++) {
            sbf.append(",").append(StringHelper.valueOf(params[i]));
		}
		sbf.append(")");
		return sbf.toString();
	}
}
