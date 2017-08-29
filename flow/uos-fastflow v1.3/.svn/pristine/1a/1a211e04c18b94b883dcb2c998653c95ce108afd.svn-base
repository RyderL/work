package com.ztesoft.uosflow.core.dto;

import java.io.Serializable;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;

public class CommandResultDto implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String serial = "1";// 接口模式要用到，嵌入模式队列逻辑里面需要用到
	protected boolean dealFlag;
	protected String dealMsg;

	protected String processInstanceId;
	protected CommandDto commandDto;

	protected Map<String, String> flowPassList;// 流程透传参数列表
	protected Map<String, String> flowParamList;// 流程参数列表
	
	public Map<String, String> getFlowParamList() {
		return flowParamList;
	}
	public void setFlowParamList(Map<String, String> flowParamList) {
		this.flowParamList = flowParamList;
	}
	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}
	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}

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

	public CommandDto getCommandDto() {
		return commandDto;
	}

	public void setCommandDto(CommandDto commandDto) {
		this.commandDto = commandDto;
	}

	public void init(Map<String, Object> paramsMap) {
		serial = StringHelper.valueOf(paramsMap.get(InfConstant.INF_SERIAL));
		if ("0".equals(paramsMap.get(InfConstant.INF_DEAL_FLAG))) {
			dealFlag = true;
		} else {
			dealFlag = false;
		}
		dealMsg = StringHelper.valueOf(paramsMap.get(InfConstant.INF_DEAL_MSG));
	}

}
