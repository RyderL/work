package com.ztesoft.uosflow.web.inf.model.server;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class BizObjInfDto implements InfDto, Serializable{
	private static final long serialVersionUID = -7855132354242317020L;
	private String id;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public JsonObject getTreeJsonObject(){
		JsonObject bizObj = new JsonObject();
		bizObj.addProperty("id", this.getId());
		bizObj.addProperty("text", this.getName());
		return bizObj;
	}
}
