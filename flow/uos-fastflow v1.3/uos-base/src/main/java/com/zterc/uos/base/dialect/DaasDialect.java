package com.zterc.uos.base.dialect;


/**
 * daas数据库方言实现
 * 
 * @author gongyi
 */
public class DaasDialect extends MySqlDialect implements Dialect{

	@Override
	public String getToNumber(String str) {
		return "CAST("+str+" AS SIGNED)";//整数 : SIGNED
	}

}
