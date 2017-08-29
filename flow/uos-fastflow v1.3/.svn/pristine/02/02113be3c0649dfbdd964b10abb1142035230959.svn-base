package com.zterc.uos.fastflow.service;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dao.process.CommandQueueDAO;
import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

/**
 * ��ʵ������Ϣ�־û�������
 * 
 * @author gong.yi
 *
 */
public class CommandQueueService {
	private CommandQueueDAO commandQueueDAO;

	public void setCommandQueueDAO(CommandQueueDAO commandQueueDAO) {
		this.commandQueueDAO = commandQueueDAO;
	}
	
	public CommandQueueDto addCommandQueue(CommandQueueDto dto){
		return commandQueueDAO.addCommandQueue(dto);
	}

	public PageDto qryCommandMsgInfoByPid(Map<String, Object> map) {
		return commandQueueDAO.qryCommandMsgInfoByPid(map);
	}

	public CommandQueueDto qryCommandMsgInfoByWid(Map<String, Object> map) {
		return commandQueueDAO.qryCommandMsgInfoByWid(map);
	}
	
	public PageDto queryCommandByCond(Map<String, Object> map) {
		return commandQueueDAO.queryCommandByCond(map);
	}

	public List<CommandQueueDto> qryCommandQueueDtosByPid(
			String processInstanceId) {
		return commandQueueDAO.qryCommandQueueDtosByPid(processInstanceId);
	}

	public void deleteByPid(String processInstanceId) {
		commandQueueDAO.deleteByPid(processInstanceId);
	}

	public CommandQueueDto qryCommandQueueDto(String processInstanceId,
			String commandCode) {
		return commandQueueDAO.qryCommandQueueDto(processInstanceId,commandCode);
	}
}

