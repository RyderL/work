package com.ztesoft.uosflow.dubbo.inf.manager;

public interface FlowManagerInf {
	/**
	 * 清理所有的流程模版缓存
	 * 
	 * @param processDefineId
	 * @param processDefineCode
	 */
	public void refreshProcessDefineCache();
	
	/**
	 * 清理所有的环节缓存
	 * 
	 * @param tacheDefineId
	 */
	public void refreshTacheDefCache();
	
	/**
	 * 清理所有的流程退单异常配置缓存
	 * 
	 * @param processDefineId
	 */
	public void refreshReturnReasonConfigCache();
	
	/**
	 * 清理所有的溜冰变量配置缓存
	 * 
	 * @param processDefineId
	 */
	public void refreshProcessParamDefCache();
	
	public void reCreateWorkOrder(String paramJson);
	
	public void refreshProcessPackageCache();
	
	public void reExcuteCommand(String paramJson);
}
