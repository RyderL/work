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
	private String packageDefineId;//流程模板ID
	private Long tacheId;//环节ID
	private String tacheCode;//环节编码
	private Long areaId;//区域ID
	private String areaName;//区域名称
	private String type;//派发规则类型
//	private String typeName;//派发规则类型名称
	private String rollbackType;//回退方式
	private String applyAll;//是否适用于所有流程
	private String partyType;//参与人类型
	private String partyId;//参与人ID
	private String partyName;//参与人名称
	private String manualPartyType;//人工执行类型
	private String manualPartyId;//人工执行人ID
	private String manualPartyName;//人工执行人名称
	private String callType;//调用方式
	private Long bizId;//调用组件ID
	private String bizName;//调用组件名称
	private String isAutomaticReturn;//自动组件是否自动回单
	private String isAutoManual;//失败是否自动转人工
	private String isReverseAutomaticReturn;//回滚是否自动回单
	private String isReverseAutomaticManual;//反向失败是否自动转人工
	private String state;//状态
	private Timestamp createDate;//创建时间
	private Timestamp stateDate;//状态时间
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
