package com.zterc.uos.fastflow.dto.specification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public class PageTemplateRuleDto {
	
	private Long ruleId;
	private String packageCode;
	private String tacheCode;
	private String funcCode;
	private String framePageUrl;
	private String templateCode;
	private Date createDate;
	private Date modifyDate;
	private Integer routeId;
	private String tenantId;
	
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getTacheCode() {
		return tacheCode;
	}
	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public String getFramePageUrl() {
		return framePageUrl;
	}
	public void setFramePageUrl(String framePageUrl) {
		this.framePageUrl = framePageUrl;
	}
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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
		mapper.put("ruleId", "RULE_ID");
		mapper.put("packageCode", "PACKAGE_CODE");
		mapper.put("tacheCode", "TACHE_CODE");
		mapper.put("funcCode", "FUNC_CODE");
		mapper.put("framePageUrl", "FRAME_PAGE_URL");		
		mapper.put("templateCode", "TEMPLATE_CODE");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("modifyDate", "MODIFY_DATE");
		mapper.put("routeId", "ROUTE_ID");
		mapper.put("tenantId", "TENANT_ID");
		return mapper;
	}	
}
