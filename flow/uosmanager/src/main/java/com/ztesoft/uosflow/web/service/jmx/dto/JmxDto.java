package com.ztesoft.uosflow.web.service.jmx.dto;

import java.util.Date;

public class JmxDto {
	private long id; // 标识
	private String appName; // 应用名称
	private String hostName; // 主机名
	private String ip; // ip
	private int port; // 端口
	private String ObjName; // 对象名
	private String actName; // 操作名
	private String actDesc; // 操作描述
	private String params; // 入参列表
	private String state; // 状态
	private Date stateDate; // 最后修改时间
	private String remark; // 备注

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("id").append(id).append(",");
		sb.append("appName").append(appName).append(",");
		sb.append("hostName").append(hostName).append(",");
		sb.append("ip").append(ip).append(",");
		sb.append("port").append(port).append(",");
		sb.append("ObjName").append(ObjName).append(",");
		sb.append("actName").append(actName);
		sb.append("actDesc").append(actDesc);
		sb.append("params").append(params);
		sb.append("state").append(state);
		sb.append("stateDate").append(stateDate);
		sb.append("remark").append(remark);
		sb.append("}");
		return sb.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getObjName() {
		return ObjName;
	}

	public void setObjName(String objName) {
		ObjName = objName;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getActDesc() {
		return actDesc;
	}

	public void setActDesc(String actDesc) {
		this.actDesc = actDesc;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getStateDate() {
		return stateDate;
	}

	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
