package com.ztesoft.uosflow.core.dto.server;

import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class SaveCommandQueueDto extends CommandDto {
	private static final long serialVersionUID = 1L;
	private CommandQueueDto commandQueueDto;
	
	public CommandQueueDto getCommandQueueDto() {
		return commandQueueDto;
	}
	public void setCommandQueueDto(CommandQueueDto commandQueueDto) {
		this.commandQueueDto = commandQueueDto;
	}
	public SaveCommandQueueDto(){
		this.setCommandCode("saveCommandQueue");
	}
}
