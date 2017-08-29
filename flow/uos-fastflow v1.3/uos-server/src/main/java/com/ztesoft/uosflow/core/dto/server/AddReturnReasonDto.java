package com.ztesoft.uosflow.core.dto.server;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class AddReturnReasonDto extends CommandDto {

	private static final long serialVersionUID = 1L;

	public AddReturnReasonDto() {
		this.setCommandCode("addReturnReason");
	}

	private String reasonCode;//异常编码
	private String reasonName;//异常名称
	private String reasonCatalogId;//异常目录id
	private String reasonType;//异常类型
	private String comment;//备注
	private String recommandMeans;//处理建议
	
	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
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

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRecommandMeans() {
		return recommandMeans;
	}

	public void setRecommandMeans(String recommandMeans) {
		this.recommandMeans = recommandMeans;
	}

	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.reasonCatalogId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_CATALOG_ID));
		this.reasonCode = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_CODE));
		this.reasonName = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_NAME));
		this.reasonType = StringHelper.valueOf(paramsMap.get(InfConstant.INF_REASON_TYPE));
		this.comment = StringHelper.valueOf(paramsMap.get(InfConstant.INF_COMMENT));
		this.recommandMeans = StringHelper.valueOf(paramsMap.get(InfConstant.INF_RECOMMEND_MEANS));
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
		map.put(InfConstant.INF_REASON_CODE, reasonCode);
		map.put(InfConstant.INF_REASON_NAME, reasonName);
		map.put(InfConstant.INF_REASON_TYPE, reasonType);
		map.put(InfConstant.INF_COMMENT, comment);
		map.put(InfConstant.INF_REASON_CATALOG_ID, reasonCatalogId);
		map.put(InfConstant.INF_RECOMMEND_MEANS, recommandMeans);
		return map;
	}
}
