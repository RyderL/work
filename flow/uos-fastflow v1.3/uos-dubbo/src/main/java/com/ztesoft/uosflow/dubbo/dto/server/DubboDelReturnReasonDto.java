package com.ztesoft.uosflow.dubbo.dto.server;


import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboDelReturnReasonDto extends DubboCommandDto {

	private static final long serialVersionUID = 1L;

	public DubboDelReturnReasonDto(){
		this.setCommandCode("delReturnReason");
	}
	private String reasonId;//异常原因id

	public String getReasonId() {
		return reasonId;
	}
	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

}
