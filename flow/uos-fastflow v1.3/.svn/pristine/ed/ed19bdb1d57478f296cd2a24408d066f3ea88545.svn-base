package com.zterc.uos.fastflow.dto.process;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class ProInstAttrDto {
	private String pid;
	private String attr;
	private String val;
	private String activityId;
	private Long id;
	private Timestamp stateDate;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getStateDate() {
		return stateDate;
	}

	public void setStateDate(Timestamp stateDate) {
		this.stateDate = stateDate;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("pid", "PID");
		mapper.put("attr", "ATTR");
		mapper.put("val", "VAL");
		mapper.put("activityId", "ACTIVITYID");
		return mapper;
	}

}
