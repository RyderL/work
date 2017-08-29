package com.ztesoft.uosflow.dubbo.dto.server;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboProcessInstanceJumpDto extends DubboCommandDto {
	private static final long serialVersionUID = 1L;

	private String fromActivityInstanceId;
	private String toActivityId;
	private String toAtomActivityId;
	private Map<String, String> flowPassList;// 流程透传参数列表

	public DubboProcessInstanceJumpDto() {
		this.setCommandCode("processInstanceJump");
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

	public String getToAtomActivityId() {
		return toAtomActivityId;
	}

	public void setToAtomActivityId(String toAtomActivityId) {
		this.toAtomActivityId = toAtomActivityId;
	}

	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}

	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}

}
