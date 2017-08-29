package com.ztesoft.uosflow.core.dto.client;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class ReportTimeLimitDto extends CommandDto {

	private static final long serialVersionUID = 1L;
	private String alertDate;
	private String limitDate;
	private String workItemId;
	private String tacheCode;

	public ReportTimeLimitDto(){
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
	
	@SuppressWarnings("all")
	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.tacheCode = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TACHECODE));
		this.workItemId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_WORKITEMID));
		this.limitDate =StringHelper.valueOf(paramsMap.get(InfConstant.INF_LIMIT_DATE));
		this.alertDate = StringHelper.valueOf(paramsMap.get(InfConstant.INF_ALERT_DATE));
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(InfConstant.INF_SERIAL, getSerial());
		map.put(InfConstant.INF_TIME, getTime());
		map.put(InfConstant.INF_FROM, getFrom());
		map.put(InfConstant.INF_TO, getTo());
		map.put(InfConstant.INF_COMMAND_CODE, getCommandCode());
		map.put(InfConstant.INF_AREA_CODE, areaCode);
		map.put(InfConstant.INF_PROCESSINSTANCEID, processInstanceId);
		map.put(InfConstant.INF_WORKITEMID, workItemId);
		map.put(InfConstant.INF_TACHE_CODE, tacheCode);
		map.put(InfConstant.INF_LIMIT_DATE, limitDate);
		map.put(InfConstant.INF_ALERT_DATE, alertDate);
		return map;
	}
}
