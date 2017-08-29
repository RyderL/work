package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FlowLimitDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
    private Long packageId;
    private Long limitValue;
    private Long alertValue;
    private String timeUnit;
    private String isWorkTime;
    private Long areaId;
    private Long[] areaIds;
    private String processName;//流程模板名称
    private String areaName;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getLimitValue() {
		return limitValue;
	}
	public void setLimitValue(Long limitValue) {
		this.limitValue = limitValue;
	}
	public Long getAlertValue() {
		return alertValue;
	}
	public void setAlertValue(Long alertValue) {
		this.alertValue = alertValue;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public String getIsWorkTime() {
		return isWorkTime;
	}
	public void setIsWorkTime(String isWorkTime) {
		this.isWorkTime = isWorkTime;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public Long[] getAreaIds() {
		return areaIds;
	}
	public void setAreaIds(Long[] areaIds) {
		this.areaIds = areaIds;
	}
	public Long getPackageId() {
		return packageId;
	}
	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("packageId", "PACKAGE_ID");
		mapper.put("limitValue", "LIMIT_VALUE");
		mapper.put("alertValue", "ALERT_VALUE");
		mapper.put("timeUnit", "TIME_UNIT");
		mapper.put("isWorkTime", "IS_WORK_TIME");
		mapper.put("areaId", "AREA_ID");
		mapper.put("areaName", "AREA_NAME");
		mapper.put("processName", "PROCESS_NAME");
		return mapper;
	}
}
