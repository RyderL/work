package com.ztesoft.uosflow.dubbo.dto.client;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboCreateWorkOrderDto extends DubboCommandDto {
	private static final long serialVersionUID = 1L;
	protected String tacheId;// 环节ID
	protected String tacheCode;// 环节编码
	protected String tacheName;// 环节名称
	protected String workItemId;// 工作项 ID
	protected String relaWorkItemId;//原工作项ID
	protected String direction;// 方向
	protected String batchId;// 批次号

	protected Map<String, String> flowPassMap;// 流程引擎透传参数
	protected Map<String, String> flowParamMap;// 流程参数
	protected String returnToStart;

	public DubboCreateWorkOrderDto() {
		this.setCommandCode("createWorkOrder");
	}

	public String getTacheId() {
		return tacheId;
	}

	public void setTacheId(String tacheId) {
		this.tacheId = tacheId;
	}

	public String getTacheCode() {
		return tacheCode;
	}

	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}
	
	public String getTacheName() {
		return tacheName;
	}

	public void setTacheName(String tacheName) {
		this.tacheName = tacheName;
	}

	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public String getRelaWorkItemId() {
		return relaWorkItemId;
	}

	public void setRelaWorkItemId(String relaWorkItemId) {
		this.relaWorkItemId = relaWorkItemId;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Map<String, String> getFlowPassMap() {
		return flowPassMap;
	}

	public void setFlowPassMap(Map<String, String> flowPassMap) {
		this.flowPassMap = flowPassMap;
	}

	public Map<String, String> getFlowParamMap() {
		return flowParamMap;
	}

	public void setFlowParamMap(Map<String, String> flowParamMap) {
		this.flowParamMap = flowParamMap;
	}

	public String getReturnToStart() {
		return returnToStart;
	}

	public void setReturnToStart(String returnToStart) {
		this.returnToStart = returnToStart;
	}
}