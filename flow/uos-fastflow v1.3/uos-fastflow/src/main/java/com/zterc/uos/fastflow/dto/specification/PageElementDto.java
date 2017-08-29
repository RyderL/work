package com.zterc.uos.fastflow.dto.specification;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public class PageElementDto {
	private Long id;
	private String elementCode;
	private String elementName;
	private String elementType;
	private String popPageUrl;
	private Integer popPageWidth;
	private Integer popPageHeight;
	private Integer textRow;
	private String buttonFunc;
	private String buttonClass;
	private String buttonMethod;
	private String optionDataClass;
	private String optionDataMethod;
	private String initDataClass;
	private String initDataMethod;
	private String comments;
	private Integer routeId;
	private String tenantId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getElementCode() {
		return elementCode;
	}
	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getPopPageUrl() {
		return popPageUrl;
	}
	public void setPopPageUrl(String popPageUrl) {
		this.popPageUrl = popPageUrl;
	}
	public Integer getPopPageWidth() {
		return popPageWidth;
	}
	public void setPopPageWidth(Integer popPageWidth) {
		this.popPageWidth = popPageWidth;
	}
	public Integer getPopPageHeight() {
		return popPageHeight;
	}
	public void setPopPageHeight(Integer popPageHeight) {
		this.popPageHeight = popPageHeight;
	}
	public Integer getTextRow() {
		return textRow;
	}
	public void setTextRow(Integer textRow) {
		this.textRow = textRow;
	}
	public String getButtonFunc() {
		return buttonFunc;
	}
	public void setButtonFunc(String buttonFunc) {
		this.buttonFunc = buttonFunc;
	}
	public String getButtonClass() {
		return buttonClass;
	}
	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}
	public String getButtonMethod() {
		return buttonMethod;
	}
	public void setButtonMethod(String buttonMethod) {
		this.buttonMethod = buttonMethod;
	}
	public String getOptionDataClass() {
		return optionDataClass;
	}
	public void setOptionDataClass(String optionDataClass) {
		this.optionDataClass = optionDataClass;
	}
	public String getOptionDataMethod() {
		return optionDataMethod;
	}
	public void setOptionDataMethod(String optionDataMethod) {
		this.optionDataMethod = optionDataMethod;
	}
	public String getInitDataClass() {
		return initDataClass;
	}
	public void setInitDataClass(String initDataClass) {
		this.initDataClass = initDataClass;
	}
	public String getInitDataMethod() {
		return initDataMethod;
	}
	public void setInitDataMethod(String initDataMethod) {
		this.initDataMethod = initDataMethod;
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
		mapper.put("id", "ID");
		mapper.put("elementCode", "ELEMENT_CODE");
		mapper.put("elementName", "ELEMENT_NAME");
		mapper.put("elementType", "ELEMENT_TYPE");
		mapper.put("popPageUrl", "POP_PAGE_URL");
		mapper.put("popPageWidth", "POP_PAGE_WIDTH");
		mapper.put("popPageHeight", "POP_PAGE_HEIGHT");
		mapper.put("textRow", "TEXT_ROW");
		mapper.put("buttonFunc", "BUTTON_FUNC");
		mapper.put("buttonClass", "BUTTON_CLASS");
		mapper.put("buttonMethod", "BUTTON_METHOD");
		mapper.put("optionDataClass", "OPTION_DATA_CLASS");
		mapper.put("optionDataMethod", "OPTION_DATA_METHOD");
		mapper.put("initDataClass", "INIT_DATA_CLASS");
		mapper.put("initDataMethod", "INIT_DATA_METHOD");
		mapper.put("comments", "COMMENTS");
		mapper.put("routeId", "ROUTE_ID");
		mapper.put("tenantId", "TENANT_ID");
		return mapper;
	}
}		

