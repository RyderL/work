package com.ztesoft.uosflow.web.inf.model.server;

import java.io.Serializable;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AreaInfDto implements InfDto, Serializable{
	private static final long serialVersionUID = -2434246222864009590L;
	private String areaId;
	private String areaCode;
	private String areaName;
	private String pathCode;
	private List<AreaInfDto> children;
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getPathCode() {
		return pathCode;
	}
	public void setPathCode(String pathCode) {
		this.pathCode = pathCode;
	}
	public List<AreaInfDto> getChildren() {
		return children;
	}
	public void setChildren(List<AreaInfDto> children) {
		this.children = children;
	}
	@Override
	public JsonObject getTreeJsonObject(){
		JsonObject area = new JsonObject();
		area.addProperty("id", this.getAreaId());
		area.addProperty("text", this.getAreaName());
		area.addProperty("code", this.getAreaCode());
		area.addProperty("path", this.getPathCode());
		if(this.getChildren()!=null&&this.getChildren().size()>0){
			JsonArray list = new JsonArray();
			for(int i=0,j=this.getChildren().size();i<j;i++){
				AreaInfDto _child = this.getChildren().get(i);
				JsonObject child = _child.getTreeJsonObject();
				list.add(child);
			}
			area.add("children", list);
		}
		return area;
	}
}
