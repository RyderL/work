package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TacheDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long tacheCatalogId;
	private String tacheName;
	private String tacheCode;
	private Timestamp createDate;
	private Timestamp stateDate;
	private String state;
	private int isAuto;
	private String operType;
	private String tacheType;
	private String packageDefineCodes;
	private Timestamp effDate;
	private Timestamp expDate;
	private String tacheIconName;
	private String shadowName;//Ó³Éä»·½ÚÃû³Æ

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTacheCatalogId() {
		return tacheCatalogId;
	}

	public void setTacheCatalogId(Long tacheCatalogId) {
		this.tacheCatalogId = tacheCatalogId;
	}

	public String getTacheName() {
		return tacheName;
	}

	public void setTacheName(String tacheName) {
		this.tacheName = tacheName;
	}

	public String getTacheCode() {
		return tacheCode;
	}

	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getTacheType() {
		return tacheType;
	}

	public void setTacheType(String tacheType) {
		this.tacheType = tacheType;
	}

	public String getPackageDefineCodes() {
		return packageDefineCodes;
	}

	public void setPackageDefineCodes(String packageDefineCodes) {
		this.packageDefineCodes = packageDefineCodes;
	}

	
	public Timestamp getEffDate() {
		return effDate;
	}

	public void setEffDate(Timestamp effDate) {
		this.effDate = effDate;
	}

	public Timestamp getExpDate() {
		return expDate;
	}

	public void setExpDate(Timestamp expDate) {
		this.expDate = expDate;
	}

	public String getTacheIconName() {
		return tacheIconName;
	}

	public void setTacheIconName(String tacheIconName) {
		this.tacheIconName = tacheIconName;
	}

	public String getShadowName() {
		return shadowName;
	}

	public void setShadowName(String shadowName) {
		this.shadowName = shadowName;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("tacheCatalogId", "TACHE_CATALOG_ID");
		mapper.put("tacheName", "TACHE_NAME");
		mapper.put("tacheCode", "TACHE_CODE");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("stateDate", "STATE_DATE");
		mapper.put("state", "STATE");
		mapper.put("isAuto", "IS_AUTO");
		mapper.put("tacheType", "TACHE_TYPE");
		mapper.put("packageDefineCodes", "PACKAGEDEFINECODES");
		mapper.put("effDate", "EFF_DATE");
		mapper.put("expDate", "EXP_DATE");
		mapper.put("tacheIconName", "TACHE_ICON_NAME");
		mapper.put("shadowName", "SHADOW_NAME");
		return mapper;
	}
}
