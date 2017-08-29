package com.ztesoft.uosflow.inf.client.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.helper.DateHelper;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.client.CreateWorkOrderDto;
import com.ztesoft.uosflow.core.dto.client.ReportProcessStateDto;
import com.ztesoft.uosflow.core.dto.server.CompleteWorkItemDto;
import com.ztesoft.uosflow.core.util.CommandResultDtoUtil;
import com.ztesoft.uosflow.inf.client.inf.IClient;
import com.ztesoft.uosflow.inf.util.ServerJsonUtil;

public class AutoCompleteClient implements IClient {
	private static final Logger logger = LoggerFactory.getLogger(AutoCompleteClient.class);

	@Override
	public CommandResultDto sendMessage(CommandDto commandDto) {
		logger.info("调用业务接口："
				+ ServerJsonUtil.getJsonFromCommandDto(commandDto));
		CommandResultDto resultDto = null;
		if (commandDto instanceof CreateWorkOrderDto) {
			CreateWorkOrderDto createWorkOrderDto = (CreateWorkOrderDto) commandDto;
			CompleteWorkItemDto dto = new CompleteWorkItemDto();
			dto.setSerial("1");
			dto.setFrom("IOM");
			dto.setTo("UOSFLOW");
			dto.setAreaCode("1");
			dto.setTime(DateHelper.getTime());

			dto.setProcessInstanceId(createWorkOrderDto.getProcessInstanceId());
			dto.setWorkitemId("" + createWorkOrderDto.getWorkItemId());

			resultDto = CommandProxy.getInstance().dealCommand(dto);
		} else if (commandDto instanceof ReportProcessStateDto) {
			resultDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "流程状态通知成功", commandDto.getProcessInstanceId());
		}
		return resultDto;
	}

}
