package com.zterc.uos.fastflow.dto.specification;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public class PageTemplateDetailDto {
	private Long templateDetailId;
	private String templateCode;
	private String elementCode;
	private String pageTitle;
	private String pageCode;
	private Integer locateRow;
	private Integer locateColumn;
	private String align;
	private String isRet;
	private String isMust;
	private String isEnabled;
	private String isDisplay;
	private String isInit;
	private String comments;
	private Integer routeId;
	private String tenantId;
	
	
	public Long getTemplateDetailId() {
		return templateDetailId;
	}
	public void setTemplateDetailId(Long templateDetailId) {
		this.templateDetailId = templateDetailId;
	}
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public String getElementCode() {
		return elementCode;
	}
	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getPageCode() {
		return pageCode;
	}
	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}
	public Integer getLocateRow() {
		return locateRow;
	}
	public void setLocateRow(Integer locateRow) {
		this.locateRow = locateRow;
	}
	public Integer getLocateColumn() {
		return locateColumn;
	}
	public void setLocateColumn(Integer locateColumn) {
		this.locateColumn = locateColumn;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getIsRet() {
		return isRet;
	}
	public void setIsRet(String isRet) {
		this.isRet = isRet;
	}
	public String getIsMust() {
		return isMust;
	}
	public void setIsMust(String isMust) {
		this.isMust = isMust;
	}
	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}
	public String getIsInit() {
		return isInit;
	}
	public void setIsInit(String isInit) {
		this.isInit = isInit;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
		mapper.put("templateDetailId", "TEMPLATE_DETAIL_ID");
		mapper.put("templateCode", "TEMPLATE_CODE");
		mapper.put("elementCode", "ELEMENT_CODE");
		mapper.put("pageTitle", "PAGE_TITLE");
		mapper.put("pageCode", "PAGE_CODE");
		mapper.put("locateRow", "LOCATE_ROW");
		mapper.put("locateColumn", "LOCATE_COLUMN");
		mapper.put("align", "ALIGN");
		mapper.put("isRet", "IS_RET");
		mapper.put("isMust", "IS_MUST");
		mapper.put("isEnabled", "IS_ENABLED");
		mapper.put("isDisplay", "IS_DISPLAY");
		mapper.put("isInit", "IS_INIT");
		mapper.put("comments", "COMMENTS");
		mapper.put("routeId", "ROUTE_ID");
		mapper.put("tenantId", "TENANT_ID");
		return mapper;
	}
}
