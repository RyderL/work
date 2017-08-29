package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class TacheCatalogDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String tacheCatalogName;
	private String pathCode;
	private String pathName;
	private Long parentTacheCatalogId;
	private Timestamp createDate;
	private String state;
	private Timestamp stateDate;
	private String comments;
	private String systemCode;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTacheCatalogName() {
		return tacheCatalogName;
	}
	public void setTacheCatalogName(String tacheCatalogName) {
		this.tacheCatalogName = tacheCatalogName;
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
	public Long getParentTacheCatalogId() {
		return parentTacheCatalogId;
	}
	public void setParentTacheCatalogId(Long parentTacheCatalogId) {
		this.parentTacheCatalogId = parentTacheCatalogId;
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
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public JsonObject getTreeJsonObject()
	{
		JsonObject tacheCatalog = new JsonObject();
		tacheCatalog.addProperty("id", this.getId());
		tacheCatalog.addProperty("text",  this.getTacheCatalogName());
		tacheCatalog.addProperty("type",  1);
		tacheCatalog.addProperty("systemCode",  this.getSystemCode());
		return tacheCatalog;
	}
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("tacheCatalogName", "TACHE_CATALOG_NAME");
		mapper.put("pathCode", "PATH_CODE");
		mapper.put("pathName", "PATH_NAME");
		mapper.put("parentTacheCatalogId", "PARENT_TACHE_CATALOG_ID");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("state", "STATE");
		mapper.put("stateDate", "STATE_DATE");
		mapper.put("comments", "COMMENTS");
		mapper.put("systemCode", "SYSTEM_CODE");
		return mapper;
	}
}
