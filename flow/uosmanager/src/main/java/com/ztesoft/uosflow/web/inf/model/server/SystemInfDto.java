package com.ztesoft.uosflow.web.inf.model.server;

import java.io.Serializable;
import com.google.gson.JsonObject;

public class SystemInfDto implements InfDto, Serializable{
	private static final long serialVersionUID = -5964762612833331170L;
	private String code;
	private String name;
	private InfDto actor;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InfDto getActor() {
		return actor;
	}
	public void setActor(InfDto actor) {
		this.actor = actor;
	}
	@Override
	public JsonObject getTreeJsonObject(){
		JsonObject sys = new JsonObject();
		sys.addProperty("id", this.getCode());
		sys.addProperty("text", this.getName());
		if(this.getActor()!=null){
			sys.addProperty("actor", this.getActor().getTreeJsonObject().toString());
		}
		sys.addProperty("type", "SYS");
		return sys;
	}
}
