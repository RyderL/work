package com.ztesoft.uosflow.dubbo.dto.client;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboReportProcessStateDto extends DubboCommandDto {
	private static final long serialVersionUID = 1L;

	protected String comment;
	protected int state;
	protected Map<String, String> flowPassMap;// 流程引擎透传参数
	protected String errMsg;//异常信息

	public DubboReportProcessStateDto(){
		this.setCommandCode("reportProcessState");
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Map<String, String> getFlowPassMap() {
		return flowPassMap;
	}

	public void setFlowPassMap(Map<String, String> flowPassMap) {
		this.flowPassMap = flowPassMap;
	}
}
