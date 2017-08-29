package com.ztesoft.uosflow.dubbo.dto.server;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboCreateProcessInstacneDto extends DubboCommandDto {
	private static final long serialVersionUID = 1L;

	private String flowPackageCode;
	private String priority;
	private Map<String, String> flowParamList;// ���̲����б�
	private Map<String, String> flowPassList;// ����͸�������б�

	public DubboCreateProcessInstacneDto() {
		this.setCommandCode("createProcessInstance");
	}

	public String getFlowPackageCode() {
		return flowPackageCode;
	}

	public void setFlowPackageCode(String flowPackageCode) {
		this.flowPackageCode = flowPackageCode;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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
