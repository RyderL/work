package com.zterc.uos.fastflow.service;

import java.util.Map;

import com.zterc.uos.fastflow.dao.process.his.CommandQueueHisDAO;
import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

/**
 * @author che.zi
 *
 */
public class CommandQueueHisService {
	private CommandQueueHisDAO commandQueueHisDAO;

	public void setCommandQueueHisDAO(CommandQueueHisDAO commandQueueHisDAO) {
		this.commandQueueHisDAO = commandQueueHisDAO;
	}

	public PageDto qryCommandMsgInfoByPid(Map<String, Object> map) {
		return commandQueueHisDAO.qryCommandMsgInfoHisByPid(map);
	}

	public CommandQueueDto qryCommandMsgInfoByWid(Map<String, Object> map) {
		return commandQueueHisDAO.qryCommandMsgInfoHisByWid(map);
	}
	
	public PageDto queryCommandByCond(Map<String, Object> map) {
		return commandQueueHisDAO.queryCommandHisByCond(map);
	}
}

