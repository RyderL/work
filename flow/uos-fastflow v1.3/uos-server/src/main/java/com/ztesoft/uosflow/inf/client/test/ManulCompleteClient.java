package com.ztesoft.uosflow.inf.client.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.client.CreateWorkOrderDto;
import com.ztesoft.uosflow.core.dto.client.ReportProcessStateDto;
import com.ztesoft.uosflow.core.util.CommandResultDtoUtil;
import com.ztesoft.uosflow.inf.client.inf.IClient;
import com.ztesoft.uosflow.inf.util.ServerJsonUtil;

public class ManulCompleteClient  implements IClient {
	private static final Logger logger = LoggerFactory.getLogger(ManulCompleteClient.class);

	@Override
	public CommandResultDto sendMessage(CommandDto commandDto) {
		logger.info("调用业务接口："
				+ ServerJsonUtil.getJsonFromCommandDto(commandDto));
		CommandResultDto resultDto = null;
		if (commandDto instanceof CreateWorkOrderDto) {
			resultDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "创建工单通知成功", commandDto.getProcessInstanceId());
		} else if (commandDto instanceof ReportProcessStateDto) {
			resultDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "流程状态通知成功", commandDto.getProcessInstanceId());
		}
		return resultDto;
	}
}
