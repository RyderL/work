package com.ztesoft.uosflow.inf.client.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.config.ConfigContext;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.client.CreateWorkOrderDto;
import com.ztesoft.uosflow.core.dto.server.SaveCommandQueueDto;
import com.ztesoft.uosflow.core.util.CommandInvokeUtil;
import com.ztesoft.uosflow.inf.client.inf.IClient;
import com.ztesoft.uosflow.inf.util.ServerJsonUtil;

@Component("clientProxy")
public class ClientProxy {
	private Logger logger = LoggerFactory.getLogger(ClientProxy.class);

	private ClientProxy() {
	}

	public static ClientProxy getInstance() {
		return (ClientProxy) ApplicationContextProxy.getBean("clientProxy");
	}

	@Autowired
	private IClient infClient;

	/**
	 * 针对json格式的接口交互处理
	 * 
	 * @param jsonParams
	 * @return
	 */
	public CommandResultDto dealCommand(CommandDto commandDto) {
		CommandResultDto commandResultDto = CommandInvokeUtil.invoke(this, commandDto);

		if (ConfigContext.isNeedInfPersist()) {
			// 在接口表中插入
			SaveCommandQueueDto saveCommandQueueDto = new SaveCommandQueueDto();
			CommandQueueDto dto = new CommandQueueDto();
			dto.setCommandCode(commandDto.getCommandCode());
			dto.setCommandMsg(GsonHelper.toJson(commandDto.toMap()));
			dto.setCommandResultMsg(ServerJsonUtil.getJsonFromCommandResultDto(commandResultDto));
			dto.setProcessInstanceId(LongHelper.valueOf(commandDto.getProcessInstanceId()));
			if("createWorkOrder".equals(commandDto.getCommandCode())){
				CreateWorkOrderDto createWorkOrderDto = (CreateWorkOrderDto) commandDto;
				dto.setWorkItemId(LongHelper.valueOf(createWorkOrderDto.getWorkItemId()));
			}
			dto.setState(0L);
			dto.setCreateDate(DateHelper.getTimeStamp());
			dto.setRoute(0L);
			dto.setAreaId(commandDto.getAreaCode());
			saveCommandQueueDto.setCommandQueueDto(dto);
			saveCommandQueueDto.setProcessInstanceId(commandDto.getProcessInstanceId());
			CommandProxy.getInstance().dealCommand(saveCommandQueueDto);
		}
		return commandResultDto;
	}

	public CommandResultDto createWorkOrder(CommandDto commandDto) throws Exception {
		logger.info("client -------createWorkOrder------");
		return infClient.sendMessage(commandDto);
	}

	public CommandResultDto reportProcessState(CommandDto commandDto) throws Exception {
		logger.info("client -------reportProcessState------");
		return  infClient.sendMessage(commandDto);
	}

	public CommandResultDto processReachedTarget(CommandDto commandDto) {
		logger.info("client -------processReachedTarget------");

		return null;
	}

	public CommandResultDto reportCalCondResult(CommandDto commandDto) throws Exception {
		logger.info("client -------reportCalCondResult------");
		return  infClient.sendMessage(commandDto);
	}
	
	public CommandResultDto reportTimeLimit(CommandDto commandDto) throws Exception {
		logger.info("client -------reportTimeLimit------");
		return  infClient.sendMessage(commandDto);
	}
}
