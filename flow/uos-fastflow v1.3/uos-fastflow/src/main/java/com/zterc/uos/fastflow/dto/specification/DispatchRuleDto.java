package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DispatchRuleDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String packageDefineId;//����ģ��ID
	private Long tacheId;//����ID
	private String tacheCode;//���ڱ���
	private Long areaId;//����ID
	private String areaName;//��������
	private String type;//�ɷ���������
//	private String typeName;//�ɷ�������������
	private String rollbackType;//���˷�ʽ
	private String applyAll;//�Ƿ���������������
	private String partyType;//����������
	private String partyId;//������ID
	private String partyName;//����������
	private String manualPartyType;//�˹�ִ������
	private String manualPartyId;//�˹�ִ����ID
	private String manualPartyName;//�˹�ִ��������
	private String callType;//���÷�ʽ
	private Long bizId;//�������ID
	private String bizName;//�����������
	private String isAutomaticReturn;//�Զ�����Ƿ��Զ��ص�
	private String isAutoManual;//ʧ���Ƿ��Զ�ת�˹�
	private String isReverseAutomaticReturn;//�ع��Ƿ��Զ��ص�
	private String isReverseAutomaticManual;//����ʧ���Ƿ��Զ�ת�˹�
	private String state;//״̬
	private Timestamp createDate;//����ʱ��
	private Timestamp stateDate;//״̬ʱ��
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPackageDefineId() {
		return packageDefineId;
	}
	public void setPackageDefineId(String packageDefineId) {
		this.packageDefineId = packageDefineId;
	}
	public Long getTacheId() {
		return tacheId;
	}
	public void setTacheId(Long tacheId) {
		this.tacheId = tacheId;
	}
	public String getTacheCode() {
		return tacheCode;
	}
	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
//	public String getTypeName() {
//		return typeName;
//	}
//	public void setTypeName(String typeName) {
//		this.typeName = typeName;
//	}
	public String getRollbackType() {
		return rollbackType;
	}
	public void setRollbackType(String rollbackType) {
		this.rollbackType = rollbackType;
	}
	public String getApplyAll() {
		return applyAll;
	}
	public void setApplyAll(String applyAll) {
		this.applyAll = applyAll;
	}
	public String getPartyType() {
		return partyType;
	}
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getManualPartyType() {
		return manualPartyType;
	}
	public void setManualPartyType(String manualPartyType) {
		this.manualPartyType = manualPartyType;
	}
	public String getManualPartyId() {
		return manualPartyId;
	}
	public void setManualPartyId(String manualPartyId) {
		this.manualPartyId = manualPartyId;
	}
	public String getManualPartyName() {
		return manualPartyName;
	}
	public void setManualPartyName(String manualPartyName) {
		this.manualPartyName = manualPartyName;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public String getBizName() {
		return bizName;
	}
	public void setBizName(String bizName) {
		this.bizName = bizName;
	}
	public String getIsAutomaticReturn() {
		return isAutomaticReturn;
	}
	public void setIsAutomaticReturn(String isAutomaticReturn) {
		this.isAutomaticReturn = isAutomaticReturn;
	}
	public String getIsAutoManual() {
		return isAutoManual;
	}
	public void setIsAutoManual(String isAutoManual) {
		this.isAutoManual = isAutoManual;
	}
	public String getIsReverseAutomaticReturn() {
		return isReverseAutomaticReturn;
	}
	public void setIsReverseAutomaticReturn(String isReverseAutomaticReturn) {
		this.isReverseAutomaticReturn = isReverseAutomaticReturn;
	}
	public String getIsReverseAutomaticManual() {
		return isReverseAutomaticManual;
	}
	public void setIsReverseAutomaticManual(String isReverseAutomaticManual) {
		this.isReverseAutomaticManual = isReverseAutomaticManual;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Timestamp getStateDate() {
		return stateDate;
	}
	public void setStateDate(Timestamp stateDate) {
		this.stateDate = stateDate;
	}
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("packageDefineId", "PACKAGEDEFINEID");
		mapper.put("tacheId", "TACHE_ID");
		mapper.put("tacheCode", "TACHE_CODE");
		mapper.put("areaId", "AREA_ID");
		mapper.put("areaName", "AREA_NAME");
		mapper.put("type", "TYPE");
		mapper.put("rollbackType", "ROLLBACK_TYPE");
		mapper.put("applyAll", "APPLY_ALL");
		mapper.put("partyType", "PARTY_TYPE");
		mapper.put("partyId", "PARTY_ID");
		mapper.put("partyName", "PARTY_NAME");
		mapper.put("manualPartyType", "MANUAL_PARTY_TYPE");
		mapper.put("manualPartyId", "MANUAL_PARTY_ID");
		mapper.put("manualPartyName", "MANUAL_PARTY_NAME");
		mapper.put("callType", "CALL_TYPE");
		mapper.put("bizId", "BIZ_ID");
		mapper.put("bizName", "BIZ_NAME");
		mapper.put("isAutomaticReturn", "IS_AUTOMATIC_RETURN");
		mapper.put("isAutoManual", "IS_AUTO_MANUAL");
		mapper.put("isReverseAutomaticReturn", "IS_REVERSE_AUTOMATIC_RETURN");
		mapper.put("isReverseAutomaticManual", "IS_REVERSE_AUTOMATIC_MANUAL");
		mapper.put("state", "STATE");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("stateDate", "STATE_DATE");
		return mapper;
	}
}
