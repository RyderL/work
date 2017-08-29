package com.ztesoft.uosflow.dubbo.dto.server;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboStartProcessInstacneDto extends DubboCommandDto {
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> flowPassList;// 流程透传参数列表
	protected Map<String, String> flowParamList;// 流程参数

	public DubboStartProcessInstacneDto() {
		this.setCommandCode("startProcessInstance");
	}

	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}

	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}

	public Map<String, String> getFlowParamList() {
		return flowParamList;
	}

	public void setFlowParamList(Map<String, String> flowParamList) {
		this.flowParamList = flowParamList;
	}
	
}
