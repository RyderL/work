package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class ReturnReasonCatalogDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long parentReasonCatalog;
	private String reasonCatalogName;
	private String pathCode;
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
	public Long getParentReasonCatalog() {
		return parentReasonCatalog;
	}
	public void setParentReasonCatalog(Long parentReasonCatalog) {
		this.parentReasonCatalog = parentReasonCatalog;
	}
	public String getReasonCatalogName() {
		return reasonCatalogName;
	}
	public void setReasonCatalogName(String reasonCatalogName) {
		this.reasonCatalogName = reasonCatalogName;
	}
	public String getPathCode() {
		return pathCode;
	}
	public void setPathCode(String pathCode) {
		this.pathCode = pathCode;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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
		JsonObject returnReasonCatalog = new JsonObject();
		returnReasonCatalog.addProperty("id", this.getId());
		returnReasonCatalog.addProperty("text",  this.getReasonCatalogName());
		returnReasonCatalog.addProperty("type",  1);
		returnReasonCatalog.addProperty("systemCode",  this.getSystemCode());
		return returnReasonCatalog;
	}
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("parentReasonCatalog", "PARENT_REASON_CATALOG");
		mapper.put("reasonCatalogName", "REASON_CATALOG_NAME");
		mapper.put("pathCode", "PATH_CODE");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("state", "STATE");
		mapper.put("stateDate", "STATE_DATE");
		mapper.put("comments", "COMMENTS");
		mapper.put("systemCode", "SYSTEM_CODE");
		return mapper;
	}
}
