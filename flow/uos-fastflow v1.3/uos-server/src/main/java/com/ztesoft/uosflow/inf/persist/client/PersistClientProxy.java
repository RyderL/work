package com.ztesoft.uosflow.inf.persist.client;

import org.springframework.stereotype.Component;

import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.inf.persist.server.PersistSeviceInf;

@Component("persistClientProxy")
public class PersistClientProxy {

	private PersistClientProxy() {
	}

	public static PersistClientProxy getInstance() {
		return (PersistClientProxy) ApplicationContextProxy.getBean("persistClientProxy");
	}

	public CommandResultDto sendMessage(CommandDto commandDto){
		CommandResultDto commandResultDto = null;
		PersistSeviceInf service = (PersistSeviceInf) ApplicationContextProxy.getBean("persistSevice");
		commandResultDto = service.sendMessage(commandDto);
		return commandResultDto;
	}

}
