package com.ztesoft.uosflow.core.dto.client;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class ReportProcessStateDto extends CommandDto {
	private static final long serialVersionUID = 1L;

	protected String comment;
	protected int state;
	protected Map<String, String> flowPassMap;// 流程引擎透传参数
	protected String errMsg;//异常信息

	public ReportProcessStateDto(){
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

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(InfConstant.INF_SERIAL, getSerial());
		map.put(InfConstant.INF_TIME, getTime());
		map.put(InfConstant.INF_FROM, getFrom());
		map.put(InfConstant.INF_TO, getTo());
		map.put(InfConstant.INF_COMMAND_CODE, getCommandCode());
		map.put(InfConstant.INF_AREA_CODE, areaCode);
		map.put(InfConstant.INF_PROCESSINSTANCEID, processInstanceId);
		map.put(InfConstant.INF_PROCESSINSTANCESTATE, state);
		map.put(InfConstant.INF_COMMENT, comment);
		map.put(InfConstant.INF_FLOW_PASS_LIST, flowPassMap);
		map.put(InfConstant.INF_ERR_MSG, errMsg);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.state = IntegerHelper.valueOf(paramsMap.get(InfConstant.INF_PROCESSINSTANCESTATE));
		this.comment = StringHelper.valueOf(paramsMap.get(InfConstant.INF_COMMENT));
		this.errMsg = StringHelper.valueOf(paramsMap.get(InfConstant.INF_ERR_MSG));
		this.flowPassMap = (Map<String, String>)paramsMap.get(InfConstant.INF_FLOW_PASS_LIST);
	}
}
