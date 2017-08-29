package com.zterc.uos.fastflow.dto.specification;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public class ComboboxDto {
	
	private String code;
	private String name;
	
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
	
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("code", "CODE");
		mapper.put("name", "NAME");
		return mapper;
	}

}
