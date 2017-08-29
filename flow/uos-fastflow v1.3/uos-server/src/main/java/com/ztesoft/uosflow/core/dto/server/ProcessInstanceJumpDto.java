package com.ztesoft.uosflow.core.dto.server;

import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class ProcessInstanceJumpDto extends CommandDto{
	private static final long serialVersionUID = 1L;
	
	private String fromActivityInstanceId;
	private String toActivityId;
	private Map<String, String> flowPassList;// 流程透传参数列表
	
	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}

	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}

	public ProcessInstanceJumpDto(){
		this.setCommandCode("processInstanceJump");
	}
	
	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.fromActivityInstanceId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_FROM_ACTIVITYINSTANCE_ID));
		this.toActivityId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TO_ACTIVITY_ID));
	}
	
	public String getFromActivityInstanceId() {
		return fromActivityInstanceId;
	}

	public void setFromActivityInstanceId(String fromActivityInstanceId) {
		this.fromActivityInstanceId = fromActivityInstanceId;
	}

	public String getToActivityId() {
		return toActivityId;
	}

	public void setToActivityId(String toActivityId) {
		this.toActivityId = toActivityId;
	}
	
	@Override
	public Map<String,Object> toMap(){
		Map<String,Object> map = super.toMap();
		map.put(InfConstant.INF_FLOW_PASS_LIST, flowPassList);
		map.put(InfConstant.INF_FROM_ACTIVITYINSTANCE_ID, fromActivityInstanceId);
		map.put(InfConstant.INF_TO_ACTIVITY_ID, toActivityId);
		return map;
	}
}
