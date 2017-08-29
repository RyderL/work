package com.ztesoft.uosflow.web.service.flow;

import java.util.Map;

public interface FlowOperServ {
	
	/**
	 * 启动流程
	 * @param map
	 * @return
	 */
	public String startFlow(Map<String,Object>  map);
	/**
	 * 挂起流程
	 * @param map
	 * @return
	 */
	public String suspendProcessInstance(Map<String,Object>  map);
	/**
	 * 恢复流程
	 * @param map
	 * @return
	 */
	public String resumeProcessInstance(Map<String,Object>  map);
	/**
	 * 提交工作项
	 * @param map
	 * @return
	 */
	public String completeWorkItem(Map<String,Object>  map);
	/**
	 * 作废工作项
	 * @param map
	 * @return
	 */
	public String disableWorkItem(Map<String,Object>  map);
	
	/**
	 * 撤单
	 * @param map
	 * @return
	 */
	public String cancelProcessInstance(Map<String,Object> map);
	
	/**
	 * 终止流程
	 * @param map
	 * @return
	 */
	public String terminateProcessInstance(Map<String,Object> map);
	
	/**
	 * 流程跳转
	 * @param map
	 * @return
	 */
	public String processInstanceJump(Map<String,Object> map);
	
	/**
	 * 生成工单消息重投
	 * @param map
	 * @return
	 */
	public String reCreateWorkOrder(Map<String,Object> map);
	
	/**
	 * 接口消息重投
	 * @param map
	 * @return
	 */
	public String reExcuteMsg(Map<String,Object> map);
	
	public String processInstanceJumpForServer(Map<String,Object> map);
}
