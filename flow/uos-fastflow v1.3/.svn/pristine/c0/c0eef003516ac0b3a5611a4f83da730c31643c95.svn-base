package com.zterc.uos.fastflow.dao.process;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public interface ProcessInstanceDAO {
	
	ProcessInstanceDto queryProcessInstance(String processInstanceId);

	ProcessInstanceDto createProcessInstance(ProcessInstanceDto processInstance);

	void updateProcessInstance(ProcessInstanceDto processInstance);

	PageDto queryProcessInstancesByCond(Map<String, Object> paramMap);

	List<Map<String,Object>> qryProcInstStateCount(String states);

	List<Map<String, Object>> qryProcDefineUseCount();

	PageDto qryExceptionFlow(Map<String, Object> paramMap);

	void deleteProcessInstance(String processInstanceId);
}
