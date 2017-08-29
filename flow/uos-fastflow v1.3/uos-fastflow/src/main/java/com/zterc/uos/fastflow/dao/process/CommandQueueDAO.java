package com.zterc.uos.fastflow.dao.process;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public interface CommandQueueDAO {
	public CommandQueueDto addCommandQueue(CommandQueueDto dto);

	public PageDto qryCommandMsgInfoByPid(Map<String, Object> map);

	public CommandQueueDto qryCommandMsgInfoByWid(Map<String, Object> map);
	
	public PageDto queryCommandByCond(Map<String, Object> map);

	public List<CommandQueueDto> qryCommandQueueDtosByPid(
			String processInstanceId);

	public void deleteByPid(String processInstanceId);

	public CommandQueueDto qryCommandQueueDto(String processInstanceId,
			String commandCode);
}
