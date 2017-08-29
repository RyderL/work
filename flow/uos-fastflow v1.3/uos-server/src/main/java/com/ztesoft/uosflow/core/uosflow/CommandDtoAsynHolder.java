package com.ztesoft.uosflow.core.uosflow;

import java.util.ArrayList;
import java.util.List;

import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class CommandDtoAsynHolder {
	private static ThreadLocal<List<CommandDto>> createWorkOrderLocalListLocal = new ThreadLocal<List<CommandDto>>();

	public static void addCommandDto(CommandDto commandDto) {
		List<CommandDto> commandDtos = createWorkOrderLocalListLocal.get();
		if (commandDtos == null) {
			createWorkOrderLocalListLocal.set(new ArrayList<CommandDto>());
		}
		createWorkOrderLocalListLocal.get().add(commandDto);
	}

	public static void clear() {
		if (createWorkOrderLocalListLocal.get() == null) {
			createWorkOrderLocalListLocal.set(new ArrayList<CommandDto>());
		}
		createWorkOrderLocalListLocal.get().clear();
	}

	public static void commit(CommandDto srcCommandDto) {
		if("persistProcessModel".equals(srcCommandDto.getCommandCode()) || "persistProcessModelRemote".equals(srcCommandDto.getCommandCode())){
			return;
		}
		
		List<CommandDto> arrayList = createWorkOrderLocalListLocal.get();
		if (null == arrayList) {
			//modify by bobping 为空直接返回
			return;
		}
		CommandDto[] list = arrayList.toArray(new CommandDto[] {});
		createWorkOrderLocalListLocal.get().clear();
		
		for (int i = 0; i < list.length; i++) {
			
			CommandDto commandDto = list[i];
			if(!"startProcessInstance".equals(commandDto.getCommandCode())
					&& !"saveCommandQueue".equals(commandDto.getCommandCode())){
				commandDto.setFrom(CommandDto.SYSTEM_SELF);
				commandDto.setTo(srcCommandDto.getFrom());
			}
			CommandProxy.getInstance().dealCommand(commandDto);
		}
	}

	public static void main(String[] args) {
	}
}
