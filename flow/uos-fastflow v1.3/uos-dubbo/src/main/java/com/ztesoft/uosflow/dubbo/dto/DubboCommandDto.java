package com.ztesoft.uosflow.dubbo.dto;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class DubboCommandDto implements Serializable {

	private static final long serialVersionUID = 1l;

	protected String commandCode;
	protected String serial = "1";// 接口模式要用到，嵌入模式队列逻辑里面需要用到
	protected String from;// 接口模式要用到，嵌入模式队列逻辑里面需要用到
	protected String to = "FLOW";// 接口模式要用到，嵌入模式队列逻辑里面需要用到
	protected String areaCode;
	protected String time;// 交互时间
	protected String processInstanceId;// 无流程实例Id的接口：可以不用传值（暂时只有创建流程实例接口和提交工作项无此值）
	protected String prio;// 优先级别

	public String getPrio() {
		if (prio == null) {
			prio = "0";
		}
		return prio;
	}

	public void setPrio(String prio) {
		this.prio = prio;
	}

	protected Map<String, Object> infParamMap;

	public DubboCommandDto() {
		String filePath = "systemConfig";
		ResourceBundle properties = ResourceBundle.getBundle(filePath, Locale.getDefault());
		if(properties == null) {
			throw new RuntimeException(filePath + ".propertites not found error!");
		}
		from = properties.getString("from");
		to = properties.getString("to");
	}

	public String getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Map<String, Object> getInfParamMap() {
		return infParamMap;
	}

	public void setInfParamMap(Map<String, Object> infParamMap) {
		this.infParamMap = infParamMap;
	}
}
