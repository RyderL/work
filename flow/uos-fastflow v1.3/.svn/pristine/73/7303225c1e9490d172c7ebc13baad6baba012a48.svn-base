package com.zterc.uos.fastflow.dto.process;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProcessParamDefDto implements Serializable,Cloneable{
	public static final int TYPE_SEND = 1;
	public static final int TYPE_DEFAULT = 0;

	private String code;
	private String name;
	private String value;
	private int type;
	private String systemCode;
	private String comments;
	private int routeId;
	private int isVariable;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsVariable() {
		return isVariable;
	}

	public void setIsVariable(int isVariable) {
		this.isVariable = isVariable;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("code", "CODE");
		mapper.put("name", "NAME");
		mapper.put("value", "VALUE");
		mapper.put("systemCode", "SYSTEM_CODE");
		mapper.put("comments", "COMMENTS");
		mapper.put("type", "TYPE");
		mapper.put("isVariable", "IS_VARIABLE");
		return mapper;
	}
}
