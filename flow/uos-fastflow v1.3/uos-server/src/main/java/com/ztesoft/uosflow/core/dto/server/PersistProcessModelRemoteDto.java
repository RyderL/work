package com.ztesoft.uosflow.core.dto.server;

import java.util.Map;

import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class PersistProcessModelRemoteDto extends CommandDto {
	private static final long serialVersionUID = 1L;
	
	private ProcessModel processModel;
	
	public ProcessModel getProcessModel() {
		return processModel;
	}

	public void setProcessModel(ProcessModel processModel) {
		this.processModel = processModel;
	}

	public PersistProcessModelRemoteDto(){
		this.setCommandCode("persistProcessModelRemote");
	}

	@Override
	public Map<String,Object> toMap(){
		Map<String,Object> map = super.toMap();
		map.put("PROCESSMODEL", processModel);
		return map;
	}
}
