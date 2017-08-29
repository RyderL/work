package com.ztesoft.uosflow.web.inf.model.server;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class StaInfDto implements InfDto, Serializable{
	private static final long serialVersionUID = -6331183366733155390L;
	private String staffId;
	private String staffName;
	private String userName;
	private String orgId;
	private String orgName;
	private String jobName;
	private String officeTel;
	private String mobileTel;
	private String isBasic;
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getOfficeTel() {
		return officeTel;
	}
	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}
	public String getMobileTel() {
		return mobileTel;
	}
	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}
	public String getIsBasic() {
		return isBasic;
	}
	public void setIsBasic(String isBasic) {
		this.isBasic = isBasic;
	}
	@Override
	public JsonObject getTreeJsonObject() {
		JsonObject sta = new JsonObject();
		sta.addProperty("id", this.getStaffId());
		sta.addProperty("text", this.getStaffName());
		sta.addProperty("type", "STA");
		sta.addProperty("userName", this.getUserName());
		sta.addProperty("orgId", this.getOrgId());
		sta.addProperty("orgName", this.getOrgName());
		sta.addProperty("jobName", this.getJobName());
		sta.addProperty("officeTel", this.getOfficeTel());
		sta.addProperty("mobileTel", this.getMobileTel());
		sta.addProperty("isBasic", this.getIsBasic());
		return sta;
	}

}
