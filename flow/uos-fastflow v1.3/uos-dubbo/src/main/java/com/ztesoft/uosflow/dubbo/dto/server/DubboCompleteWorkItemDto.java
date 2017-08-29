package com.ztesoft.uosflow.dubbo.dto.server;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboCompleteWorkItemDto extends DubboCommandDto {
	private static final long serialVersionUID = 1L;

	private String workitemId;
	private String memo;
	private Map<String, String> flowParamList;// ���̲����б�
	private Map<String, String> flowPassList;// ����͸�������б�

	public DubboCompleteWorkItemDto() {
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

}
