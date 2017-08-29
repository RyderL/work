package com.ztesoft.uosflow.core.dto.server;


import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

/**
 * 流程回退（退单）入参对象
 * @author Administrator
 *
 */
public class DisableWorkItemDto extends CommandDto {
	private static final long serialVersionUID = 1L;

	private String workitemId;//工作项id
	private String memo;//备注
	private String reasonType;//异常类型
	private String reasonCode;//异常编码
	private String reasonCfgId;//原因配置记录ID，默认NULL
	private Map<String, String> flowPassList;// 流程透传参数列表
	private Map<String,String> flowParamList;//流程参数列表
	private String targetWorkItemId;//目标环节工作项id

	public DisableWorkItemDto(){
		this.setCommandCode("disableWorkItem");
	}
	
	public String getWorkitemId() {
		return workitemId;
	}

	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonCfgId() {
		return reasonCfgId;
	}

	public void setReasonCfgId(String reasonCfgId) {
		this.reasonCfgId = reasonCfgId;
	}
	
	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}

	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}

	public Map<String, String> getFlowParamList() {
		return flowParamList;
	}

	public void setFlowParamList(Map<String, String> flowParamList) {
		this.flowParamList = flowParamList;
	}

	public String getTargetWorkItemId() {
		return targetWorkItemId;
	}

	public void setTargetWorkItemId(String targetWorkItemId) {
		this.targetWorkItemId = targetWorkItemId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.workitemId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_WORKITEMID));
		this.memo = StringHelper.valueOf(paramsMap.get(InfConstant.INF_MEMO));
		this.reasonType = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_TYPE));
		this.reasonCode = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_CODE));
		this.reasonCfgId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_CFG_ID));
		this.flowPassList = (Map<String, String>) paramsMap.get(InfConstant.INF_FLOW_PASS_LIST);
		this.flowParamList = (Map<String, String>) paramsMap.get(InfConstant.INF_FLOW_PARAM_LIST);
		this.targetWorkItemId = (String) paramsMap.get(InfConstant.INF_TARGET_WORKITEM_ID);
	}
	
	public Map<String,Object> toMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(InfConstant.INF_SERIAL, getSerial());
		map.put(InfConstant.INF_TIME, getTime());
		map.put(InfConstant.INF_FROM, getFrom());
		map.put(InfConstant.INF_TO, getTo());
		map.put(InfConstant.INF_COMMAND_CODE, getCommandCode());
		map.put(InfConstant.INF_AREA_CODE, areaCode);
		map.put(InfConstant.INF_PROCESSINSTANCEID, processInstanceId);
		map.put(InfConstant.INF_WORKITEMID, workitemId);
		map.put(InfConstant.INF_REASON_TYPE, reasonType);
		map.put(InfConstant.INF_REASON_CODE, reasonCode);
		map.put(InfConstant.INF_REASON_CFG_ID, reasonCfgId);
		map.put(InfConstant.INF_MEMO, memo);
		map.put(InfConstant.INF_FLOW_PASS_LIST, flowPassList);
		map.put(InfConstant.INF_FLOW_PARAM_LIST, flowParamList);
		map.put(InfConstant.INF_TARGET_WORKITEM_ID, flowParamList);
		return map;
	}
}
