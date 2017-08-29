package com.zterc.uos.fastflow.dto.specification;

import java.util.HashMap;
import java.util.Map;

public class ProcessParamDefRelDto {
	private Long packageDefineId;
	private String code;
	private String value;
	private String type;
	private String tacheCode;
	private int isVariable;
	public Long getPackageDefineId() {
		return packageDefineId;
	}
	public void setPackageDefineId(Long packageDefineId) {
		this.packageDefineId = packageDefineId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTacheCode() {
		return tacheCode;
	}
	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}
	public int getIsVariable() {
		return isVariable;
	}
	public void setIsVariable(int isVariable) {
		this.isVariable = isVariable;
	}
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("packageDefineId", "PACKAGEDEFINEID");
		mapper.put("code", "CODE");
		mapper.put("value", "VALUE");
		mapper.put("type", "TYPE");
		mapper.put("tacheCode", "TACHE_CODE");
		mapper.put("isVariable", "IS_VARIABLE");
		return mapper;
	}
}
