package com.zterc.uos.fastflow.jdbc.sqlHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存sql的线程变量
 * 主要目的是为了第三方异步持久化传输sql
 * @author bobping
 *
 */
public class SqlLocalHolder {
	private static ThreadLocal<List<AsynSqlExecBy3thParamDto>> sqlLocal = new ThreadLocal<List<AsynSqlExecBy3thParamDto>>();
	/**
	 * 是否hold住sql
	 */
	private static ThreadLocal<Boolean> holdSqlOn = new ThreadLocal<Boolean>();
	
	public static List<AsynSqlExecBy3thParamDto> get() {
		return sqlLocal.get();
	}

	public static void set(List<AsynSqlExecBy3thParamDto> sqlParams) {
		sqlLocal.set(sqlParams);
	}

	public static void clear() {
		sqlLocal.remove();
		holdSqlOn.remove();
	}
	
	public static void addSqlParam(AsynSqlExecBy3thParamDto sqlParam){
		List<AsynSqlExecBy3thParamDto> sqlParams = sqlLocal.get();
		if(null == sqlParams){
			sqlParams = new ArrayList<>();
		}
		sqlParams.add(sqlParam);
		sqlLocal.set(sqlParams);
	}

	public static boolean isHoldSqlOn() {
		Boolean flag = holdSqlOn.get();
		if(flag == null || flag == false){
			return false;
		}
		return true;
	}

	public static void setHoldSqlOn(boolean isHoldSqlOn) {
		holdSqlOn.set(isHoldSqlOn);
	}
	
	
	
	
	
	
	
}
