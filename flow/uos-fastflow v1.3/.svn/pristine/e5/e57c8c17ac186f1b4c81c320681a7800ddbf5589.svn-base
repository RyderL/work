package com.ztesoft.uosflow.core.dto.client;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class ReportCalCondResultDto extends CommandDto {

	private static final long serialVersionUID = 1L;

	protected String isPassed;
	protected String tacheCode;
	protected Map<String, String> flowPassMap;// 流程引擎透传参数

	public ReportCalCondResultDto(){
		this.setCommandCode("reportCalCondResult");
	}
	
	
	public String getIsPassed() {
		return isPassed;
	}
	public void setIsPassed(String isPassed) {
		this.isPassed = isPassed;
	}
	public String getTacheCode() {
		return tacheCode;
	}
	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}
	public Map<String, String> getFlowPassMap() {
		return flowPassMap;
	}
	public void setFlowPassMap(Map<String, String> flowPassMap) {
		this.flowPassMap = flowPassMap;
	}
	
	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(InfConstant.INF_SERIAL, getSerial());
		map.put(InfConstant.INF_TIME, getTime());
		map.put(InfConstant.INF_FROM, getFrom());
		map.put(InfConstant.INF_TO, getTo());
		map.put(InfConstant.INF_COMMAND_CODE, getCommandCode());
		map.put(InfConstant.INF_AREA_CODE, areaCode);
		map.put(InfConstant.INF_PROCESSINSTANCEID, processInstanceId);
		map.put(InfConstant.INF_IS_PASSED, isPassed);
		map.put(InfConstant.INF_TACHE_CODE, tacheCode);
		map.put(InfConstant.INF_FLOW_PASS_LIST, flowPassMap);
		return map;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.isPassed = StringHelper.valueOf(paramsMap.get(InfConstant.INF_IS_PASSED));
		this.tacheCode = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TACHE_CODE));
		this.flowPassMap = (Map<String, String>)paramsMap.get(InfConstant.INF_FLOW_PASS_LIST);
	}
}
