package com.zterc.uos.fastflow.inf;

import java.util.Map;

import com.zterc.uos.fastflow.dto.process.CreateWorkOrderParamDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;

public interface WorkflowStateReport {

	public void reportProcessState(Long processInstanceId, String comment,
			int state,Map<String,String> flowPassMap, String errorInfo,String areaId);

	public void createWorkOrder(CreateWorkOrderParamDto workOrderDto);

	public void processReachedTarget(WorkItemDto workItem);
	
	public void reportCalCondResult(Long processInstanceId, String tacheCode,
			boolean isPassed,Map<String,String> flowPassMap,String areaId);

	public void reportTimeLimit(Map<String,Object> map);

	public void saveDataToHis(String processInstanceId,String areaId);
}
