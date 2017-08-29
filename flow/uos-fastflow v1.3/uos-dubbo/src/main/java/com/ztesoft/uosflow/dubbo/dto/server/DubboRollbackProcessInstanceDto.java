package com.ztesoft.uosflow.dubbo.dto.server;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboRollbackProcessInstanceDto extends DubboCommandDto {
	private static final long serialVersionUID = 1L;

	private String reasonCode;
	private Map<String, String> flowPassList;// 流程透传参数列表
	private String startMode;

	public DubboRollbackProcessInstanceDto() {
		this.setCommandCode("rollbackProcessInstance");
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}

	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}

	public String getStartMode() {
		return startMode;
	}

	public void setStartMode(String startMode) {
		this.startMode = startMode;
	}

}
