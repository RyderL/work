package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TacheLimitDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private Long tacheId;
    private Long limitValue;
    private Long alertValue;
    private String timeUnit;
    private String isWorkTime;
    private Long areaId;
    private String timeUnitName;
    private String areaName;
    private String pathName;//环节路径名
    private String tacheName;//环节名
    
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTacheId() {
		return tacheId;
	}
	public void setTacheId(Long tacheId) {
		this.tacheId = tacheId;
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
	public String getTimeUnitName() {
		return timeUnitName;
	}
	public void setTimeUnitName(String timeUnitName) {
		this.timeUnitName = timeUnitName;
	}
	public String getTacheName() {
		return tacheName;
	}
	public void setTacheName(String tacheName) {
		this.tacheName = tacheName;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("tacheId", "TACHE_ID");
		mapper.put("alertValue", "ALERT_VALUE");
		mapper.put("timeUnit", "TIME_UNIT");
		mapper.put("limitValue", "LIMIT_VALUE");
		mapper.put("isWorkTime", "IS_WORK_TIME");
		mapper.put("areaId", "AREA_ID");
		mapper.put("tacheName", "TACHE_NAME");
		mapper.put("areaName", "AREA_NAME");
		mapper.put("pathName", "PATH_NAME");
		return mapper;
	}
}
