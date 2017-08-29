package com.ztesoft.uosflow.core.dto.server;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class DelReturnReasonDto extends CommandDto {

	private static final long serialVersionUID = 1L;

	public DelReturnReasonDto() {
		this.setCommandCode("delReturnReason");
	}

	private String reasonId;//“Ï≥£‘≠“Úid
	
	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.reasonId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_ID));
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
		map.put(InfConstant.INF_REASON_ID, reasonId);
		return map;
	}
}
