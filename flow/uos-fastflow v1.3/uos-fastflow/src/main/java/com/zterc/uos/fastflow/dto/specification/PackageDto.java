package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class PackageDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long packageId;
	private Long catalogId;
	private String name;
	private String state;
	private Timestamp stateDate;
	private String description;
	private String ownerAreaId;
	private String packageType;
	private Long sendServiceId;//???
	private Long routeId;
	private Timestamp effDate;
	private Timestamp expDate;
	
	public Long getPackageId() {
		return packageId;
	}
	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
	public Long getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwnerAreaId() {
		return ownerAreaId;
	}
	public void setOwnerAreaId(String ownerAreaId) {
		this.ownerAreaId = ownerAreaId;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public Long getSendServiceId() {
		return sendServiceId;
	}
	public void setSendServiceId(Long sendServiceId) {
		this.sendServiceId = sendServiceId;
	}
	public Long getRouteId() {
		return routeId;
	}
	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}
	public JsonObject getTreeJsonObject()
	{
		JsonObject pack = new JsonObject();
		pack.addProperty("id", this.getPackageId());
		pack.addProperty("parentId", this.getCatalogId());
		pack.addProperty("text",  this.getName());
		pack.addProperty("type",  2);
		pack.addProperty("packageType",  this.getPackageType());
		pack.addProperty("effDate", this.getEffDate()==null?"": this.getEffDate().toString());
		pack.addProperty("expDate", this.getExpDate()==null?"": this.getExpDate().toString());
		return pack;
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
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("packageId", "PACKAGEID");
		mapper.put("catalogId", "CATALOGID");
		mapper.put("name", "NAME");
		mapper.put("state", "STATE");
		mapper.put("stateDate", "STATEDATE");
		mapper.put("description", "DESCRIPTION");
		mapper.put("ownerAreaId", "OWNERAREAID");
		mapper.put("packageType", "PACKAGE_TYPE");
		mapper.put("routeId", "ROUTE_ID");
		mapper.put("effDate", "EFF_DATE");
		mapper.put("expDate", "EXP_DATE");
		return mapper;
	}
}
