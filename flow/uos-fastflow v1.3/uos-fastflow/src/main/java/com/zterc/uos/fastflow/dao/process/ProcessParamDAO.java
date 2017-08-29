package com.zterc.uos.fastflow.dao.process;

import java.sql.Timestamp;
import java.util.List;

import com.zterc.uos.fastflow.dto.process.ProInstParamDto;

public interface ProcessParamDAO {
	
	ProInstParamDto getProcessParam(Long processInstanceId, String paramName);

	void addProcessParam(Long id, Long processInstanceId, String key, String value, String tacheCode,Timestamp stateDate);

	void updateProcessParam(Long processInstanceId, String key, String value, String tacheCode);
	
	List<ProInstParamDto> getAllProcessParams(Long processInstanceId);

	int countProcessParam(Long processInstanceId, String key);

	void deleteByPid(String processInstanceId);
}
