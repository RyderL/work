package com.ztesoft.uosflow.core.dto.client;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class CreateWorkOrderDto extends CommandDto {
	private static final long serialVersionUID = 1L;

	protected String tacheId;//环节ID
	protected String tacheCode;//环节编码
	protected String tacheName;//环节名称
	protected String workItemId;//工作项 ID
	protected String relaWorkItemId;//原工作项ID
	protected String direction;// 方向
	protected String batchId;//批次号
	
	protected Map<String, String> flowPassMap;//流程引擎透传参数
	protected Map<String, String> flowParamMap;//流程参数
	protected String returnToStart;
	
	public CreateWorkOrderDto(){
		this.setCommandCode("createWorkOrder");
	}
	
	public String getTacheId() {
		return tacheId;
	}

	public void setTacheId(String tacheId) {
		this.tacheId = tacheId;
	}

	public String getTacheCode() {
		return tacheCode;
	}

	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}
	
	public String getTacheName() {
		return tacheName;
	}

	public void setTacheName(String tacheName) {
		this.tacheName = tacheName;
	}

	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	public Map<String, String> getFlowPassMap() {
		return flowPassMap;
	}

	public void setFlowPassMap(Map<String, String> flowPassMap) {
		this.flowPassMap = flowPassMap;
	}

	public Map<String, String> getFlowParamMap() {
		return flowParamMap;
	}

	public void setFlowParamMap(Map<String, String> flowParamMap) {
		this.flowParamMap = flowParamMap;
	}
	
	public String getRelaWorkItemId() {
		return relaWorkItemId;
	}

	public void setRelaWorkItemId(String relaWorkItemId) {
		this.relaWorkItemId = relaWorkItemId;
	}

	public String getReturnToStart() {
		return returnToStart;
	}

	public void setReturnToStart(String returnToStart) {
		this.returnToStart = returnToStart;
	}

	@SuppressWarnings("all")
	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.tacheId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TACHEID));
		this.tacheCode = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TACHECODE));
		this.tacheName = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TACHENAME));
		this.workItemId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_WORKITEMID));
		this.relaWorkItemId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_RELA_WORKITEMID));
		this.direction =StringHelper.valueOf(paramsMap.get(InfConstant.INF_DIRECTION));
		this.batchId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_BATCHID));
		this.flowPassMap = (Map<String, String>)paramsMap.get(InfConstant.INF_FLOW_PASS_LIST);
		this.flowParamMap = (Map<String, String>)paramsMap.get(InfConstant.INF_FLOW_PARAM_LIST);
		this.returnToStart = StringHelper.valueOf(paramsMap.get(InfConstant.INF_RETURNTOSTART));
	}
	
	public Map<String,Object> toMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(InfConstant.INF_SERIAL, getSerial());
		map.put(InfConstant.INF_TIME, getTime());
		map.put(InfConstant.INF_FROM, getFrom());
		map.put(InfConstant.INF_TO, getTo());
		map.put(InfConstant.INF_COMMAND_CODE, getCommandCode());
		map.put(InfConstant.INF_AREA_CODE, areaCode);
		map.put(InfConstant.INF_PROCESSINSTANCEID, processInstanceId);
		map.put(InfConstant.INF_TACHEID, tacheId);
		map.put(InfConstant.INF_TACHECODE, tacheCode);
		map.put(InfConstant.INF_TACHENAME, tacheName);
		map.put(InfConstant.INF_WORKITEMID, workItemId);
		map.put(InfConstant.INF_RELA_WORKITEMID, this.relaWorkItemId);
		map.put(InfConstant.INF_DIRECTION, direction);
		map.put(InfConstant.INF_BATCHID, batchId);
		if(this.flowPassMap!=null){
			map.put(InfConstant.INF_FLOW_PASS_LIST, this.flowPassMap);
		}
		if(this.flowParamMap!=null){
			map.put(InfConstant.INF_FLOW_PARAM_LIST, this.flowParamMap);
		}
		map.put(InfConstant.INF_RETURNTOSTART, returnToStart);
		return map;
	}
}
