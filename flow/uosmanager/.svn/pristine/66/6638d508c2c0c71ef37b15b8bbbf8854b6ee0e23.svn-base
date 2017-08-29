package com.ztesoft.uosflow.web.inf.model.server;

import java.io.Serializable;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class OrgInfDto implements InfDto, Serializable{
	private static final long serialVersionUID = 6372615882701482105L;
	private String orgId;
	private String orgName;
	private String pathCode;
	private List<OrgInfDto> children;
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
	public String getPathCode() {
		return pathCode;
	}
	public void setPathCode(String pathCode) {
		this.pathCode = pathCode;
	}
	public List<OrgInfDto> getChildren() {
		return children;
	}
	public void setChildren(List<OrgInfDto> children) {
		this.children = children;
	}
	@Override
	public JsonObject getTreeJsonObject() {
		JsonObject org = new JsonObject();
		org.addProperty("id", this.getOrgId());
		org.addProperty("text", this.getOrgName());
		org.addProperty("type", "ORG");
		org.addProperty("path", this.getPathCode());
		if(this.getChildren()!=null&&this.getChildren().size()>0){
			JsonArray list = new JsonArray();
			for(int i=0,j=this.getChildren().size();i<j;i++){
				OrgInfDto _child = this.getChildren().get(i);
				JsonObject child = _child.getTreeJsonObject();
				list.add(child);
			}
			org.add("children", list);
		}
		return org;
	}
}
