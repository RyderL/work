package com.ztesoft.uosflow.dubbo.dto.server;


import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboAddReturnReasonDto extends DubboCommandDto {

	private static final long serialVersionUID = 1L;

	public DubboAddReturnReasonDto(){
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

}
