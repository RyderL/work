package com.zterc.uos.fastflow.dto.specification;

import java.util.HashMap;
import java.util.Map;

public class AppCfgDto {
	public Long id;
	private String appName;
	private String pKey;
	private String pValue;

	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getpKey() {
		return pKey;
	}

	public void setpKey(String pKey) {
		this.pKey = pKey;
	}

	public String getpValue() {
		return pValue;
	}

	public void setpValue(String pValue) {
		this.pValue = pValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("appName", "APP_NAME");
		mapper.put("pKey", "P_KEY");
		mapper.put("pValue", "P_VALUE");
		mapper.put("comment", "COMMENT");
		return mapper;
	}
}
