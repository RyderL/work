package com.ztesoft.uosflow.dubbo.dto.server;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

/**
 * ���̻��ˣ��˵�����ζ���
 * 
 * @author Administrator
 * 
 */
public class DubboDisableWorkItemDto extends DubboCommandDto {
	private static final long serialVersionUID = 1L;

	private String workitemId;// ������id
	private String memo;// ��ע
	private String reasonType;// �쳣����
	private String reasonCode;// �쳣����
	private String reasonCfgId;// ԭ�����ü�¼ID��Ĭ��NULL
	private Map<String, String> flowPassList;// ����͸�������б�
	private Map<String,String> flowParamList;//���̲����б�
	private String targetWorkItemId;//Ŀ�껷�ڹ�����id

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
