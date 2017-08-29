package com.ztesoft.uosflow.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.bean.BeanContextProxy;
import com.zterc.uos.fastflow.dto.specification.CommandCfgDto;
import com.zterc.uos.fastflow.service.CommandCfgService;

public class CommandPropUtil {
	// µ¥ÀýÄ£Ê½
	private static CommandPropUtil INSTANCE;;

	private Map<String, CommandCfgDto> commandCfgMap;

	private Map<String, String> dtoToCommandCodeMap;

	private CommandCfgService commandCfgService;

	public boolean inited = false;
	
	private Map<String, CommandCfgDto> commandQueMap;

	private CommandPropUtil() {

	}

	public CommandCfgService getCommandCfgService() {
		return commandCfgService;
	}

	public void setCommandCfgService(CommandCfgService commandCfgService) {
		this.commandCfgService = commandCfgService;
	}

	public static CommandPropUtil getInstance() {
		if (INSTANCE == null) {
			INSTANCE = BeanContextProxy.getBean(CommandPropUtil.class);
		}
		return INSTANCE;
	}

	public void init() {
		if (!inited) {
			List<CommandCfgDto> commandCfgDtos = commandCfgService
					.qryComandCfgs();
			commandQueMap = new HashMap<String, CommandCfgDto>();
			commandCfgMap = new HashMap<String, CommandCfgDto>();
			dtoToCommandCodeMap = new HashMap<String, String>();
			for (int i = 0; i < commandCfgDtos.size(); i++) {
				CommandCfgDto dto = commandCfgDtos.get(i);
				if(dto.getQueueCode() != null){
					commandQueMap.put(dto.getQueueCode().trim(),
							dto);
				}
				commandCfgMap.put(dto.getCommandCode().trim(),
						dto);
				dtoToCommandCodeMap.put(dto.getBeanName(),
						dto.getCommandCode().trim());
			}
			inited = true;
		}
	}

	public boolean isSyn(String key) {
		return commandCfgMap.get(key).getIsSyn() == CommandCfgDto.SYN_FLAG_TRUE;
	}

	public boolean isTypeOfServer(String key) {
		return commandCfgMap.get(key).getType()
				.equalsIgnoreCase(CommandCfgDto.TYPE_SERVER);
	}

	public int getQueueCount(String key) {
		return commandCfgMap.get(key).getModeCount();
	}

	public String getDtoClassName(String key) {
		return commandCfgMap.get(key).getBeanName();
	}

	public String getCommandCodeByDtoClassName(String dtoClassName) {
		return dtoToCommandCodeMap.get(dtoClassName);
	}

	public CommandCfgDto getCommandCfgDto(String key) {
		return commandCfgMap.get(key);
	}
	
	public CommandCfgDto getCommandCfgDtoByQue(String key){
		return commandQueMap.get(key);
	}
}
