package com.ztesoft.uosflow.dubbo.dto.server;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandDto;

public class DubboTerminateProcessInstanceDto extends DubboCommandDto {

	private static final long serialVersionUID = 1L;

	public DubboTerminateProcessInstanceDto() {
		this.setCommandCode("terminateProcessInstance");
	}
}
