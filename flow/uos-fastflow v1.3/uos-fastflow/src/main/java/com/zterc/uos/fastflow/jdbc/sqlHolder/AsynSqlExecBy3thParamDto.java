package com.zterc.uos.fastflow.jdbc.sqlHolder;
import java.io.Serializable;

/**
 * ����������ִ�е�sql����
 * @author:bobping
 * 
 */
public class AsynSqlExecBy3thParamDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2469161145127064248L;
	
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	
	private String sqlStr;
	private String key;
	private Object[] param;
	private int sqlType;
	private String tableName;
	private int sqlSeq;
	
	
	
	
	public AsynSqlExecBy3thParamDto(String sqlStr, String key, Object[] param, int sqlType, String tableName,
			int sqlSeq) {
		super();
		this.sqlStr = sqlStr;
		this.key = key;
		this.param = param;
		this.sqlType = sqlType;
		this.tableName = tableName;
		this.sqlSeq = sqlSeq;
	}
	
	
	
	public AsynSqlExecBy3thParamDto(String sqlStr, String key, Object[] param, int sqlType, String tableName) {
		super();
		this.sqlStr = sqlStr;
		this.key = key;
		this.param = param;
		this.sqlType = sqlType;
		this.tableName = tableName;
	}

	

	public AsynSqlExecBy3thParamDto(String sqlStr, String key, Object[] param) {
		super();
		this.sqlStr = sqlStr;
		this.key = key;
		this.param = param;
	}



	public String getSqlStr() {
		return sqlStr;
	}
	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object[] getParam() {
		return param;
	}
	public void setParam(Object[] param) {
		this.param = param;
	}
	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getSqlSeq() {
		return sqlSeq;
	}
	public void setSqlSeq(int sqlSeq) {
		this.sqlSeq = sqlSeq;
	}
	

}






