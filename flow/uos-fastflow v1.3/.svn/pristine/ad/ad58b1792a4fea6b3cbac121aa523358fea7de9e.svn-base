package com.ztesoft.uosflow.inf.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.util.CommandPropUtil;

public class ServerJsonUtil {
	private static final Logger logger = LoggerFactory.getLogger(ServerJsonUtil.class);

	public static String getJsonFromCommandResultDto(
			CommandResultDto cmdResultDto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(InfConstant.INF_SERIAL, cmdResultDto.getCommandDto()
				.getSerial());
		map.put(InfConstant.INF_DEAL_FLAG, cmdResultDto.isDealFlag() ? "0"
				: "1");
		map.put(InfConstant.INF_DEAL_MSG, cmdResultDto.getDealMsg());

		if (cmdResultDto.getProcessInstanceId() != null) {
			map.put(InfConstant.INF_PROCESSINSTANCEID, cmdResultDto
					.getProcessInstanceId().toString());
		}
		return GsonHelper.toJson(map);
	}
	
	public static CommandDto getCommandDtoFromJson(String jsonParams) {
		Map<String, Object> paramsMap = GsonHelper.toMap(jsonParams);
		String commandCode = StringHelper.valueOf(paramsMap.get(InfConstant.INF_COMMAND_CODE));
		return getCommandDtoFromJson(commandCode,paramsMap);
	}

	public static CommandDto getCommandDtoFromJson(String commandCode,Map<String,Object> paramsMap) {
		String dtoClassName = CommandPropUtil.getInstance().getDtoClassName(commandCode);
		Class<?> clazz = null;
		try {
			clazz = Class.forName(dtoClassName);
			CommandDto cmdDto = (CommandDto) clazz.newInstance();
			cmdDto.init(paramsMap);
			return cmdDto;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("接口命令:" + commandCode + ";接口类：" + dtoClassName
					+ "===生成异常;异常信息：" + e.getMessage(), e);
		}
		return null;
	}

	public static CommandResultDto getCommandResultDtoFromJson(String json) {
		CommandResultDto commandResultDto = new CommandResultDto();
		Map<String, Object> paramsMap = GsonHelper.toMap(json);
		commandResultDto.init(paramsMap);
		return commandResultDto;
	}

	public static String getJsonFromCommandDto(CommandDto commandDto) {
		return GsonHelper.toJson(commandDto.toMap());
	}
}
