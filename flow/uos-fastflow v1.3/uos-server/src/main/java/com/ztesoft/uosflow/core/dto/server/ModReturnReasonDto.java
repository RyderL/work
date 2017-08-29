package com.ztesoft.uosflow.core.dto.server;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class ModReturnReasonDto extends CommandDto {

	private static final long serialVersionUID = 1L;

	public ModReturnReasonDto() {
		this.setCommandCode("modReturnReason");
	}

	private String reasonId;//异常原因id
	private String reasonName;//异常原因名称
	private String reasonCatalogId;//异常原因目录id
	
	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	public String getReasonName() {
		return reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	public String getReasonCatalogId() {
		return reasonCatalogId;
	}

	public void setReasonCatalogId(String reasonCatalogId) {
		this.reasonCatalogId = reasonCatalogId;
	}

	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.reasonId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_ID));
		this.reasonCatalogId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_CATALOG_ID));
		this.reasonName = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_NAME));
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
		map.put(InfConstant.INF_REASON_NAME, reasonName);
		map.put(InfConstant.INF_REASON_CATALOG_ID, reasonCatalogId);
		return map;
	}
}
