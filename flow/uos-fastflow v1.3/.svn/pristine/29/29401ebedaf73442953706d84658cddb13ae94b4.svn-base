package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class PackageCatalogDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long catalogId;
	private Long areaId;
	private String catalogName;
	private Long parentId;
	private String pathCode;
	private String pathName;
	private String packageCatalogType;
	private String state;
	private Timestamp stateDate;
	private String systemCode;
	
	public Long getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getPathCode() {
		return pathCode;
	}
	public void setPathCode(String pathCode) {
		this.pathCode = pathCode;
	}
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	public String getPackageCatalogType() {
		return packageCatalogType;
	}
	public void setPackageCatalogType(String packageCatalogType) {
		this.packageCatalogType = packageCatalogType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Timestamp getStateDate() {
		return stateDate;
	}
	public void setStateDate(Timestamp stateDate) {
		this.stateDate = stateDate;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	
	public JsonObject getTreeJsonObject()
	{
		JsonObject catalog = new JsonObject();
		catalog.addProperty("id", this.getCatalogId());
		catalog.addProperty("parentId", this.getParentId());
		catalog.addProperty("areaId", this.getAreaId());
		catalog.addProperty("systemCode", this.getSystemCode());
		catalog.addProperty("pathCode",  this.getPathCode());
		catalog.addProperty("text",  this.getCatalogName());
		catalog.addProperty("type",  1);
		return catalog;
	}
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("catalogId", "CATALOGID");
		mapper.put("areaId", "AREAID");
		mapper.put("catalogName", "CATALOGNAME");
		mapper.put("parentId", "PARENTID");
		mapper.put("pathCode", "PATHCODE");
		mapper.put("pathName", "PATH_NAME");
		mapper.put("packageCatalogType", "PACKAGECATALOGTYPE");
		mapper.put("state", "STATE");
		mapper.put("stateDate", "STATE_DATE");
		mapper.put("systemCode", "SYSTME_CODE");
		return mapper;
	}
}
