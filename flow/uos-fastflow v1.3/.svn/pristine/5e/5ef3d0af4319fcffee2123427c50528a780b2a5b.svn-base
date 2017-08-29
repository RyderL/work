package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MultiClientDto implements Serializable {
	private static final long serialVersionUID = -6413250145785070818L;

	private String system;
	private String systemBean;
	private String wsdlUrl;
	private String infType;
	private String routeId;
	
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getSystemBean() {
		return systemBean;
	}
	public void setSystemBean(String systemBean) {
		this.systemBean = systemBean;
	}
	public String getWsdlUrl() {
		return wsdlUrl;
	}
	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}
	public String getInfType() {
		return infType;
	}
	public void setInfType(String infType) {
		this.infType = infType;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("system", "SYSTEM");
		mapper.put("systemBean", "SYSTEM_BEAN");
		mapper.put("wsdlUrl", "WSDL_URL");
		mapper.put("infType", "INF_TYPE");
		mapper.put("routeId", "ROUTE_ID");
		return mapper;
	}
	
}
