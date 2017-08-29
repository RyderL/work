package com.ztesoft.uosflow.core.util;

import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;

public class CommandResultDtoUtil {
	
	public static final CommandResultDto createCommandResultDto(CommandDto commandDto,boolean dealFlag,String dealMsg,String processInstanceId)
	{
		CommandResultDto retDto = new CommandResultDto();
		retDto.setCommandDto(commandDto);
		
		retDto.setDealFlag(dealFlag);
		retDto.setDealMsg(dealMsg);
		retDto.setProcessInstanceId(processInstanceId);
		
		return retDto;
	}
}
