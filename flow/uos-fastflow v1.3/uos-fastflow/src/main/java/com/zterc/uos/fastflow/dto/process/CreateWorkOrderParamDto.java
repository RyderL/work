package com.zterc.uos.fastflow.dto.process;

import java.io.Serializable;
import java.util.Map;

public class CreateWorkOrderParamDto implements Serializable {
	private static final long serialVersionUID = 8517269826372408318L;
	private Long processInstanceId;// ����ʵ����ʶ
	private Long tacheId;// ���ڱ�ʶ
	private Long operId;// ������ʽ��ʶ
	private String workOrderType;// �������ͱ�ʶ
	private Long workitemId;// ������Id
	private String participantId;// ִ���˱�ʶ���ɿգ�
	private String participantType;// ִ�������ͣ��ɿգ�
	private Long relaWorkitemId;// ����������Id
	private String direction;// ����1,����; 0,����
	private boolean isCollaborate;// �ڲ��л����У���ʾĳһ������Э������
	private String returnToStart;// �Ƿ��˵���ʼ�ڵ�
	private Long workOrderBatchNo;// �������κ�
	private String areaId;
	private String tacheCode;// ���ڱ���
	private String tacheName;//��������
	private Map<String, String> flowPassMap;// ��������͸������
	private Map<String, String> flowParamMap;// ���̲���

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

	public CreateWorkOrderParamDto() {

	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getTacheId() {
		return tacheId;
	}

	public void setTacheId(Long tacheId) {
		this.tacheId = tacheId;
	}

	public Long getOperId() {
		return operId;
	}

	public void setOperId(Long operId) {
		this.operId = operId;
	}

	public String getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(String workOrderType) {
		this.workOrderType = workOrderType;
	}

	public Long getWorkitemId() {
		return workitemId;
	}

	public void setWorkitemId(Long workitemId) {
		this.workitemId = workitemId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getParticipantType() {
		return participantType;
	}

	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}

	public Long getRelaWorkitemId() {
		return relaWorkitemId;
	}

	public void setRelaWorkitemId(Long relaWorkitemId) {
		this.relaWorkitemId = relaWorkitemId;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public boolean isCollaborate() {
		return isCollaborate;
	}

	public void setCollaborate(boolean isCollaborate) {
		this.isCollaborate = isCollaborate;
	}

	public String getReturnToStart() {
		return returnToStart;
	}

	public void setReturnToStart(String returnToStart) {
		this.returnToStart = returnToStart;
	}

	public Long getWorkOrderBatchNo() {
		return workOrderBatchNo;
	}

	public void setWorkOrderBatchNo(Long workOrderBatchNo) {
		this.workOrderBatchNo = workOrderBatchNo;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
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

}
