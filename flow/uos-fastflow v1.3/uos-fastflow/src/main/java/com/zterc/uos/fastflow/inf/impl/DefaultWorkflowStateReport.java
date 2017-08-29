package com.zterc.uos.fastflow.inf.impl;

import java.util.Map;

import com.zterc.uos.fastflow.core.FastflowRunner;
import com.zterc.uos.fastflow.dto.process.CreateWorkOrderParamDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.dto.specification.TacheDto;
import com.zterc.uos.fastflow.inf.WorkflowStateReport;
import com.zterc.uos.fastflow.service.TacheService;

public class DefaultWorkflowStateReport implements WorkflowStateReport {

	private FastflowRunner fastflowRunner;

	private TacheService tacheService;

	public DefaultWorkflowStateReport() {

	}

	public void setFastflowRunner(FastflowRunner fastflowRunner) {
		this.fastflowRunner = fastflowRunner;
	}

	public void setTacheService(TacheService tacheService) {
		this.tacheService = tacheService;
	}

	@Override
	public void reportProcessState(Long processInstanceId, String comment,
			int state,Map<String,String> flowPassMap,String errorInfo,String areaId) {
		// 默认的不进行通知，即流程内部自己流转。
	}

	@Override
	public void createWorkOrder(CreateWorkOrderParamDto workOrderDto) {
		TacheDto tacheDto = tacheService.queryTache(workOrderDto
				.getTacheId());
		if (tacheDto != null) {
			if (tacheDto.getIsAuto() == 1) {// 0 不自动回单，1 自动回单
				fastflowRunner.completeWorkItem(workOrderDto.getWorkitemId()
						.toString(), null, "", workOrderDto.getAreaId(), null,false);
			}
		}
	}

	@Override
	public void processReachedTarget(WorkItemDto workItem) {
		// 默认目标环节到达通知，不进行处理
	}

	@Override
	public void reportCalCondResult(Long processInstanceId, String tacheCode,
			boolean isPassed, Map<String, String> flowPassMap,String areaId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportTimeLimit(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDataToHis(String processInstanceId,String areaId) {
//		fastflowRunner.dataToHis(processInstanceId);
	}

}
