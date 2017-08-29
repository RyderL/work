package com.ztesoft.uosflow.inf.persist.server;

import org.apache.log4j.Logger;

import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;

public class PersistServiceImpl implements PersistSeviceInf{
	private Logger logger = Logger.getLogger(PersistServiceImpl.class);

	@Override
	public CommandResultDto sendMessage(CommandDto commandDto) {
		logger.info("-----进入远程数据转存方法----"+ commandDto.getProcessInstanceId());
		CommandResultDto commandResultDto = CommandProxy.getInstance().dealCommand(
				commandDto);
		return commandResultDto;
	}

}
