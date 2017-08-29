package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TacheLimitRuleDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id; 
	private Long packageId;//流程定义id
	private Long tacheLimitId;
	private String flowName;//流程名称
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTacheLimitId() {
		return tacheLimitId;
	}
	public void setTacheLimitId(Long tacheLimitId) {
		this.tacheLimitId = tacheLimitId;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public Long getPackageId() {
		return packageId;
	}
	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("packageId", "PACKAGE_ID");
		mapper.put("tacheLimitId", "TACHE_LIMIT_ID");
		mapper.put("flowName", "FLOW_NAME");
		return mapper;
	}
}
