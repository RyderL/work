package com.ztesoft.uosflow.core.dto.server;

import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class SaveExceptionDto extends CommandDto {
	private static final long serialVersionUID = 1L;
	private ExceptionDto exceptionDto;
	private Integer operType;//insert/update
	
	public ExceptionDto getExceptionDto() {
		return exceptionDto;
	}
	public void setExceptionDto(ExceptionDto exceptionDto) {
		this.exceptionDto = exceptionDto;
	}
	public Integer getOperType() {
		return operType;
	}
	public void setOperType(Integer operType) {
		this.operType = operType;
	}
	public SaveExceptionDto(){
		this.setCommandCode("saveException");
	}
}
