package com.zterc.uos.fastflow.dto.specification;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public class PageConstraintDto {
	private Long consId;
	private String consType;
	private String templateCode;
	private String pageCode;
	private String consCondition;
	private String resultValue;
	private String resultClass;
	private String resultMethod;
	private Integer routeId;
	private String tenantId;
	
	public Long getConsId() {
		return consId;
	}
	public void setConsId(Long consId) {
		this.consId = consId;
	}
	public String getConsType() {
		return consType;
	}
	public void setConsType(String consType) {
		this.consType = consType;
	}
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public String getPageCode() {
		return pageCode;
	}
	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}
	public String getConsCondition() {
		return consCondition;
	}
	public void setConsCondition(String consCondition) {
		this.consCondition = consCondition;
	}
	public String getResultValue() {
		return resultValue;
	}
	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}
	public String getResultClass() {
		return resultClass;
	}
	public void setResultClass(String resultClass) {
		this.resultClass = resultClass;
	}
	public String getResultMethod() {
		return resultMethod;
	}
	public void setResultMethod(String resultMethod) {
		this.resultMethod = resultMethod;
	}
	public Integer getRouteId() {
		return routeId;
	}
	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("consId", "CONS_ID");
		mapper.put("consType", "CONS_TYPE");
		mapper.put("templateCode", "TEMPLATE_CODE");
		mapper.put("pageCode", "PAGE_CODE");
		mapper.put("consCondition", "CONS_CONDITION");
		mapper.put("resultValue", "RESULT_VALUE");
		mapper.put("resultClass", "RESULT_CLASS");
		mapper.put("resultMethod", "RESULT_METHOD");
		mapper.put("routeId", "ROUTE_ID");
		mapper.put("tenantId", "TENANT_ID");
		return mapper;
	}

}
