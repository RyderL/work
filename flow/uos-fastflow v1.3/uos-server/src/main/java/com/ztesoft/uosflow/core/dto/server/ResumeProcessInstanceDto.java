package com.ztesoft.uosflow.core.dto.server;

import java.util.HashMap;
import java.util.Map;

import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class ResumeProcessInstanceDto extends CommandDto {

	private static final long serialVersionUID = 1L;
	private Map<String, String> flowPassList;// 流程透传参数列表
	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}

	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}
	

	public ResumeProcessInstanceDto(){
		this.setCommandCode("resumeProcessInstance");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		if (paramsMap.get(InfConstant.INF_FLOW_PASS_LIST) != null) {
			this.flowPassList = (Map<String, String>) paramsMap
					.get(InfConstant.INF_FLOW_PASS_LIST);
		}
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
		map.put(InfConstant.INF_FLOW_PASS_LIST, flowPassList);
		return map;
	}
}
