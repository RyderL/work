package com.ztesoft.uosflow.core.uosflow;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.fastflow.dto.process.CreateWorkOrderParamDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.inf.WorkflowStateReport;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.client.CreateWorkOrderDto;
import com.ztesoft.uosflow.core.dto.client.ReportCalCondResultDto;
import com.ztesoft.uosflow.core.dto.client.ReportProcessStateDto;
import com.ztesoft.uosflow.core.dto.client.ReportTimeLimitDto;
import com.ztesoft.uosflow.core.dto.server.DataToHisDto;

public class ExtWorkflowStateReport implements WorkflowStateReport {

	private Logger logger = LoggerFactory.getLogger(ExtWorkflowStateReport.class);

	public ExtWorkflowStateReport() {
	}

	public void reportProcessState(Long processInstanceId, String comment,
			int state, Map<String, String> flowPassMap,String errorInfo,String areaId) {
		logger.debug("=================== comes into reportProcessState...");
		ReportProcessStateDto commandDto = new ReportProcessStateDto();
		commandDto.setProcessInstanceId(processInstanceId.toString());
		commandDto.setComment(comment);
		commandDto.setState(state);
		commandDto.setFlowPassMap(flowPassMap);
		commandDto.setErrMsg(errorInfo);
		commandDto.setAreaCode(areaId);
		CommandDtoAsynHolder.addCommandDto(commandDto);
	}

	/**
	 * 
	 * @param workOrderDto
	 * @return @
	 */
	public void createWorkOrder(CreateWorkOrderParamDto workOrderDto) {
		logger.debug("=================== comes into createWorkOrder...");
		CreateWorkOrderDto commandDto = new CreateWorkOrderDto();

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(InfConstant.INF_COMMAND_CODE, commandDto.getCommandCode());
		paramsMap.put(InfConstant.INF_SERIAL, commandDto.getSerial());
		paramsMap.put(InfConstant.INF_FROM, commandDto.getFrom());
		paramsMap.put(InfConstant.INF_TO, commandDto.getTo());
		paramsMap.put(InfConstant.INF_AREA_CODE, workOrderDto.getAreaId());
		paramsMap.put(InfConstant.INF_PROCESSINSTANCEID, workOrderDto.getProcessInstanceId().toString());
		paramsMap.put(InfConstant.INF_TACHEID, workOrderDto.getTacheId().toString());
		paramsMap.put(InfConstant.INF_TACHECODE, workOrderDto.getTacheCode().toString());
		paramsMap.put(InfConstant.INF_TACHENAME, workOrderDto.getTacheName());
		paramsMap.put(InfConstant.INF_WORKITEMID, workOrderDto.getWorkitemId().toString());
		paramsMap.put(InfConstant.INF_RELA_WORKITEMID, workOrderDto.getRelaWorkitemId());
		paramsMap.put(InfConstant.INF_DIRECTION, workOrderDto.getDirection().toString());
		paramsMap.put(InfConstant.INF_BATCHID, workOrderDto
				.getWorkOrderBatchNo().toString());
		paramsMap.put(InfConstant.INF_RETURNTOSTART, workOrderDto.getReturnToStart());
		if (workOrderDto.getFlowPassMap() != null
				&& workOrderDto.getFlowPassMap().size() != 0) {
			paramsMap.put(InfConstant.INF_FLOW_PASS_LIST,
					workOrderDto.getFlowPassMap());
		}
		if (workOrderDto.getFlowParamMap() != null
				&& workOrderDto.getFlowParamMap().size() != 0) {
			paramsMap.put(InfConstant.INF_FLOW_PARAM_LIST,
					workOrderDto.getFlowParamMap());
		}

		commandDto.init(paramsMap);

		CommandDtoAsynHolder.addCommandDto(commandDto);
	}

	@Override
	public void processReachedTarget(WorkItemDto workItem) {

	}

	@Override
	public void reportCalCondResult(Long processInstanceId, String tacheCode,
			boolean isPassed, Map<String, String> flowPassMap,String areaId) {
		logger.debug("=================== comes into reportCalCondResult...");
		String isPass = "false";
		if(isPassed){
			isPass = "true";
		}
		ReportCalCondResultDto commandDto = new ReportCalCondResultDto();
		commandDto.setProcessInstanceId(processInstanceId.toString());
		commandDto.setTacheCode(tacheCode);
		commandDto.setIsPassed(isPass);
		commandDto.setFlowPassMap(flowPassMap);
		commandDto.setAreaCode(areaId);
		CommandDtoAsynHolder.addCommandDto(commandDto);
	}

	@Override
	public void reportTimeLimit(Map<String, Object> map) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ReportTimeLimitDto commandDto = new ReportTimeLimitDto();
		paramsMap.put(InfConstant.INF_COMMAND_CODE, commandDto.getCommandCode());
		paramsMap.put(InfConstant.INF_SERIAL, commandDto.getSerial());
		paramsMap.put(InfConstant.INF_FROM, commandDto.getFrom());
		paramsMap.put(InfConstant.INF_TO, commandDto.getTo());
		paramsMap.put(InfConstant.INF_AREA_CODE, MapUtils.getString(map, "areaId"));
		paramsMap.put(InfConstant.INF_PROCESSINSTANCEID, MapUtils.getString(map, "processInstanceId"));
		paramsMap.put(InfConstant.INF_TACHECODE, MapUtils.getString(map, "tacheCode"));
		paramsMap.put(InfConstant.INF_WORKITEMID, MapUtils.getString(map, "workItemId"));
		paramsMap.put(InfConstant.INF_ALERT_DATE, MapUtils.getString(map, "alertDate"));
		paramsMap.put(InfConstant.INF_LIMIT_DATE, MapUtils.getString(map, "limitDate"));

		commandDto.init(paramsMap);

		CommandDtoAsynHolder.addCommandDto(commandDto);
	}

	@Override
	public void saveDataToHis(String processInstanceId,String areaId) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		DataToHisDto commandDto = new DataToHisDto();
		paramsMap.put(InfConstant.INF_COMMAND_CODE, commandDto.getCommandCode());
		paramsMap.put(InfConstant.INF_SERIAL, commandDto.getSerial());
		paramsMap.put(InfConstant.INF_FROM, commandDto.getFrom());
		paramsMap.put(InfConstant.INF_TO, commandDto.getTo());
		paramsMap.put(InfConstant.INF_PROCESSINSTANCEID, processInstanceId);
		paramsMap.put(InfConstant.INF_AREA_CODE, areaId);
		commandDto.init(paramsMap);
		CommandProxy.getInstance().dealCommand(commandDto);
	};
}
