package com.ztesoft.uosflow.core.dbpersist.dto;




public class AsyncSqlDto4Inst  extends AsyncSqlDto implements IPriorityMessage{
	
	public  AsyncSqlDto4Inst(){
		
	}
	
	public  AsyncSqlDto4Inst(int sqlType){
		super(sqlType);
	}
	
	/**
	 * @author  zhong.kaijie  on 2016年10月27日 上午12:17:39
	 * @version 1.0.0
	 */
	private static final long serialVersionUID = -7271689681415398925L;
	
	
	@Override
	public double getPriority() {
		double result=0;
		String key=getKey();
		result=key.hashCode()*100;
		result+=(-1*getExecuteTimes()*10);
		result+=getSqlSeq();
		return result;
	}
}
