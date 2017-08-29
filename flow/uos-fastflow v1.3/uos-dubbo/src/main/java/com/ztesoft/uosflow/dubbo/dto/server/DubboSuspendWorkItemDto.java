package com.ztesoft.uosflow.dubbo.dto.server;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboSuspendWorkItemDto extends DubboCommandDto{

	private static final long serialVersionUID = 1L;
	private Map<String, String> flowPassList;// ����͸�������б�
	private String workItemId;
	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}

	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}
	
	public DubboSuspendWorkItemDto(){
		this.setCommandCode("suspendWorkItem");
	}

}
