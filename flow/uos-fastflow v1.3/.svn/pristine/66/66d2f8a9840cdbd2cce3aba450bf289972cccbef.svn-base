package com.ztesoft.uosflow.dubbo.dto.server;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

/**
 * 流程回退（退单）入参对象
 * 
 * @author Administrator
 * 
 */
public class DubboDisableWorkItemDto extends DubboCommandDto {
	private static final long serialVersionUID = 1L;

	private String workitemId;// 工作项id
	private String memo;// 备注
	private String reasonType;// 异常类型
	private String reasonCode;// 异常编码
	private String reasonCfgId;// 原因配置记录ID，默认NULL
	private Map<String, String> flowPassList;// 流程透传参数列表
	private Map<String,String> flowParamList;//流程参数列表
	private String targetWorkItemId;//目标环节工作项id

	public DubboDisableWorkItemDto() {
		this.setCommandCode("disableWorkItem");
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

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonCfgId() {
		return reasonCfgId;
	}

	public void setReasonCfgId(String reasonCfgId) {
		this.reasonCfgId = reasonCfgId;
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

	public String getTargetWorkItemId() {
		return targetWorkItemId;
	}

	public void setTargetWorkItemId(String targetWorkItemId) {
		this.targetWorkItemId = targetWorkItemId;
	}

}
