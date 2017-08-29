package com.ztesoft.uosflow.core.dto.server;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class CompleteWorkItemDto extends CommandDto {
	private static final long serialVersionUID = 1L;

	private String workitemId;
	private String memo;
	private Map<String, String> flowParamMap;// 流程参数列表
	private Map<String, String> flowPassMap;// 流程透传参数列表

	public CompleteWorkItemDto() {
		this.setCommandCode("completeWorkItem");
	}

	public String getWorkitemId() {
		return workitemId;
	}

	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Map<String, String> getFlowParamMap() {
		return flowParamMap;
	}

	public void setFlowParamMap(Map<String, String> flowParamMap) {
		this.flowParamMap = flowParamMap;
	}

	public Map<String, String> getFlowPassMap() {
		return flowPassMap;
	}

	public void setFlowPassMap(Map<String, String> flowPassMap) {
		this.flowPassMap = flowPassMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.workitemId = StringHelper.valueOf(paramsMap
				.get(InfConstant.INF_WORKITEMID));
		this.memo = StringHelper.valueOf(paramsMap.get(InfConstant.INF_MEMO));
		this.flowParamMap = (Map<String, String>) paramsMap
				.get(InfConstant.INF_FLOW_PARAM_LIST);
		if (paramsMap.get(InfConstant.INF_FLOW_PASS_LIST) != null) {
			this.flowPassMap = (Map<String, String>) paramsMap
					.get(InfConstant.INF_FLOW_PASS_LIST);
		}
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
		map.put(InfConstant.INF_WORKITEMID, workitemId);
		map.put(InfConstant.INF_MEMO, memo);
		map.put(InfConstant.INF_FLOW_PARAM_LIST, flowParamMap);
		map.put(InfConstant.INF_FLOW_PASS_LIST, flowPassMap);
		return map;
	}
}
