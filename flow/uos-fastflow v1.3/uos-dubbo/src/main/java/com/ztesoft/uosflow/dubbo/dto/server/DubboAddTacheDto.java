package com.ztesoft.uosflow.dubbo.dto.server;


import java.util.Date;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboAddTacheDto extends DubboCommandDto {

	private static final long serialVersionUID = 1L;

	public DubboAddTacheDto(){
		this.setCommandCode("addTache");
	}
	private String tacheCode;//环节编码
	private String tacheName;//环节名称
	private String tacheCatalogId;//环节目录id
	private String tacheType;//环节类型
	private String packageDefineCodes;//环节适用流程模板编码集合
	private Date effDate;
	private Date expDate;
	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
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
	public String getTacheCatalogId() {
		return tacheCatalogId;
	}
	public void setTacheCatalogId(String tacheCatalogId) {
		this.tacheCatalogId = tacheCatalogId;
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
	
}
