package com.ztesoft.uosflow.dubbo.dto.client;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboReportTimeLimitDto extends DubboCommandDto {

	private static final long serialVersionUID = 1L;
	private String alertDate;
	private String limitDate;
	private String workItemId;
	private String tacheCode;

	public DubboReportTimeLimitDto(){
		this.setCommandCode("reportTimeLimit");
	}
	
	public String getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(String alertDate) {
		this.alertDate = alertDate;
	}
	public String getLimitDate() {
		return limitDate;
	}
	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}
	public String getWorkItemId() {
		return workItemId;
	}
	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
	public String getTacheCode() {
		return tacheCode;
	}
	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}
	
}
