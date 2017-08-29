package com.zterc.uos.fastflow.dto.specification;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public class PageConstraintTypeDto {
	
	private Long id;
	private String consType;
	private String consName;
	private Integer routeId;
	private String tenantId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getConsType() {
		return consType;
	}
	public void setConsType(String consType) {
		this.consType = consType;
	}
	public String getConsName() {
		return consName;
	}
	public void setConsName(String consName) {
		this.consName = consName;
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
		mapper.put("consType", "CONS_TYPE");
		mapper.put("consName", "CONS_NAME");
		mapper.put("routeId", "ROUTE_ID");
		mapper.put("tenantId", "TENANT_ID");
		return mapper;
	}
}
