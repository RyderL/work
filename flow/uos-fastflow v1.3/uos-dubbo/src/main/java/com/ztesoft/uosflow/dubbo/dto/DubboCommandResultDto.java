package com.ztesoft.uosflow.dubbo.dto;

import java.io.Serializable;

public class DubboCommandResultDto implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String serial = "1";// 接口模式要用到，嵌入模式队列逻辑里面需要用到
	protected boolean dealFlag;
	protected String dealMsg;

	protected String processInstanceId;

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public boolean isDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(boolean dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getDealMsg() {
		return dealMsg;
	}

	public void setDealMsg(String dealMsg) {
		this.dealMsg = dealMsg;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
}