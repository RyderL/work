package com.ztesoft.uosflow.core.dto.server;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class CreateProcessInstacneDto extends CommandDto {
	private static final long serialVersionUID = 1L;

	private String flowPackageCode;
	private Map<String, String> flowParamList;// 流程参数列表
	private Map<String, String> flowPassList;// 流程透传参数列表

	public CreateProcessInstacneDto() {
		this.setCommandCode("createProcessInstance");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.flowPackageCode = StringHelper.valueOf(paramsMap
				.get(InfConstant.INF_FLOWPACKAGECODE));
		this.priority = StringHelper.valueOf(paramsMap
				.get(InfConstant.INF_PRIORITY));
		this.flowParamList = (Map<String, String>) paramsMap
				.get(InfConstant.INF_FLOW_PARAM_LIST);
		this.flowPassList = (Map<String, String>) paramsMap
				.get(InfConstant.INF_FLOW_PASS_LIST);
	}

	public String getFlowPackageCode() {
		return flowPackageCode;
	}

	public void setFlowPackageCode(String flowPackageCode) {
		this.flowPackageCode = flowPackageCode;
	}

	public Map<String, String> getFlowParamList() {
		return flowParamList;
	}

	public void setFlowParamList(Map<String, String> flowParamList) {
		this.flowParamList = flowParamList;
	}

	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}

	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(InfConstant.INF_SERIAL, getSerial());
		map.put(InfConstant.INF_TIME, getTime());
		map.put(InfConstant.INF_FROM, getFrom());
		map.put(InfConstant.INF_TO, getTo());
		map.put(InfConstant.INF_COMMAND_CODE, getCommandCode());
		map.put(InfConstant.INF_AREA_CODE, areaCode);
		map.put(InfConstant.INF_PROCESSINSTANCEID, processInstanceId);
		map.put(InfConstant.INF_FLOWPACKAGECODE, flowPackageCode);
		map.put(InfConstant.INF_PRIORITY, priority);
		map.put(InfConstant.INF_FLOW_PARAM_LIST, flowParamList);
		map.put(InfConstant.INF_FLOW_PASS_LIST, flowPassList);
		return map;
	}
}
