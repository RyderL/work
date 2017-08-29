package com.ztesoft.uosflow.web.service.flow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.base.cache.UosCacheClient;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.core.FastflowRunner;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.exception.FastflowException;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.zterc.uos.fastflow.service.ActivityInstanceService;
import com.zterc.uos.fastflow.service.CommandQueueService;
import com.zterc.uos.fastflow.service.ProcessAttrService;
import com.zterc.uos.fastflow.service.ProcessInstanceService;
import com.zterc.uos.fastflow.service.ProcessParamDefService;
import com.zterc.uos.fastflow.service.ProcessParamService;
import com.zterc.uos.fastflow.service.TransitionInstanceService;
import com.zterc.uos.fastflow.service.WorkItemService;
import com.zterc.uos.fastflow.state.WMProcessInstanceState;
import com.ztesoft.uosflow.dubbo.dto.client.DubboCreateWorkOrderDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboProcessInstanceJumpDto;
import com.ztesoft.uosflow.dubbo.inf.server.WorkFlowServerInf;

@Service("FlowOperServ")
@Lazy(true)
public class FlowOperServImpl implements FlowOperServ {
	
	private static final Logger logger = LoggerFactory.getLogger(FlowOperServImpl.class);

	@Autowired
	private FastflowRunner runner;

	@Autowired
	private UosCacheClient uosCacheClient;

	@Autowired
	private WorkItemService workItemService;
	
	@Autowired
	private CommandQueueService commandQueueService;
	
	@Autowired
	@Qualifier("workFlowService")
	private WorkFlowServerInf workFlowServerInf;

	@Autowired
	private ProcessInstanceService processInstanceService;
	@Autowired
	private ActivityInstanceService activityInstanceService;
	@Autowired
	private TransitionInstanceService transitionInstanceService;
	@Autowired
	private ProcessParamService processParamService;
	@Autowired
	private ProcessAttrService processAttrService;
	@Autowired
	private ProcessParamDefService processParamDefService;

	@Override
	@Transactional
	public String startFlow(Map<String, Object> map) {
		String result = "";
		try {
			Long processDefineId = LongHelper.valueOf(map.get("processDefineId"));
			String parentActivityInstanceId = StringHelper.valueOf(map
					.get("parentActivityInstanceId"));
			String processDefineName = StringHelper.valueOf(map.get("processDefineName"));
			String areaId = StringHelper.valueOf(map.get("areaId"));
			// 增加流程实例参数
			@SuppressWarnings("all")
			List<Map<String, String>> flowParamList = (List<Map<String, String>>) map
					.get("flowParamList");
			Map<String, String> paramMap = new HashMap<String, String>();
			if (flowParamList != null && flowParamList.size() > 0) {
				for (int i = 0; i < flowParamList.size(); i++) {
					Map<String, String> temmap = flowParamList.get(i);
					paramMap.put(temmap.get("FLOW_PARAM_CODE"),
							temmap.get("FLOW_PARAM_VALUE"));
				}
			}
			if(areaId != null && processDefineId != null
					&& processDefineName != null){
				ProcessInstanceDto processInstanceDto = runner.createProcessInstance(
						processDefineId.toString(), parentActivityInstanceId,
						processDefineName, 1, paramMap, areaId, false);
				runner.startProcessInstance(processInstanceDto.getProcessInstanceId()
						.toString(), areaId,null, false,paramMap);
				commitProcessModel(processInstanceDto.getProcessInstanceId());
				result =  GsonHelper.toJson(processInstanceDto.getProcessInstanceId());
			}
		} catch (FastflowException e) {
			result = GsonHelper.toJson("启动流程失败:"+e.getMessage());
			logger.error("----启动流程异常："+e.getMessage(),e);
		}
		return result;
	}

	@Override
	@Transactional
	public String completeWorkItem(Map<String, Object> map) {

		String workItemId = String.valueOf(map.get("workItemId"));
		Long processInstanceId = LongHelper.valueOf(map.get("processInstanceId"));
		String areaId = String.valueOf(map.get("areaId"));
		if(processInstanceId != null){
			beginProcessModel(processInstanceId);

			// 增加流程实例参数
			@SuppressWarnings("all")
			Map<String, String> flowParamMap = (Map<String, String>) map.get("flowParamMap");
			for(String key:map.keySet()){
				if(!key.equalsIgnoreCase("flowParamMap")&&map.get(key)!=null){
					flowParamMap.put(key, map.get(key).toString());
				}
			}
			logger.info("==========="+flowParamMap+"==========");
			runner.completeWorkItem(workItemId, flowParamMap, null, areaId, null,false);
			commitProcessModel(processInstanceId);
		}
		return GsonHelper.toJson("success");
	}

	@Override
	@Transactional
	@SuppressWarnings("all")
	public String disableWorkItem(Map<String, Object> map) {
		String workItemId = String.valueOf(map.get("workItemId"));
		String reasonCatalogId = String.valueOf(map.get("reasonCatalogId"));
		// String targetActivityId =
		// String.valueOf(map.get("targetActivityId"));
		String areaId = String.valueOf(map.get("areaId"));
		String reasonCode = String.valueOf(map.get("reasonCode"));
		String reasonConfigId = String.valueOf(map.get("reasonConfigId"));
		String returnReasonName = String.valueOf(map.get("returnReasonName"));
		String processInstanceId = String.valueOf(map.get("processInstanceId"));
		
		String memo = returnReasonName + "(" + reasonCode + ")";

		Long pid = LongHelper.valueOf(processInstanceId);
		if(pid != null){
			beginProcessModel(pid);		
			// runner.disableWorkItem(workItemId, reasonCatalogId,
			// targetActivityId, areaId);
			WorkItemDto workItemDto = workItemService.queryWorkItem(workItemId);
			List<WorkItemDto> col = runner.findCanRollBackWorkItems(workItemDto.getProcessInstanceId().toString(),workItemId,reasonCode,reasonConfigId,true,areaId);
			runner.disableWorkItem(workItemId, reasonCode, reasonConfigId, memo,
					areaId, false,null);
			
			Iterator<WorkItemDto> iterator = col.iterator();
			while (iterator.hasNext()) {
				WorkItemDto workItem = iterator.next();
				if(!workItemId.equals(workItem.getWorkItemId().toString())){
					runner.disableWorkItem(workItem.getWorkItemId().toString(), null,
							null, memo, areaId, false,null);
				}
			}
			
			commitProcessModel(pid);
		}
		return GsonHelper.toJson("success");
	}
	
	@Override
	@Transactional
	public String suspendProcessInstance(Map<String, Object> map) {
		String processInstanceId = String.valueOf(map.get("processInstanceId"));
		String areaId = String.valueOf(map.get("areaId"));
		Long pid = LongHelper.valueOf(processInstanceId);
		if(pid != null){
			beginProcessModel(pid);
			
			runner.suspendProcessInstance(processInstanceId, areaId,false,null);

			commitProcessModel(pid);
		}
		return GsonHelper.toJson("success");
	}

	@Override
	@Transactional
	public String resumeProcessInstance(Map<String, Object> map) {
		String processInstanceId = String.valueOf(map.get("processInstanceId"));
		String areaId = String.valueOf(map.get("areaId"));
		Long pid = LongHelper.valueOf(processInstanceId);
		if(pid != null){
			beginProcessModel(pid);
			
			runner.resumeProcessInstance(processInstanceId, true, areaId,false,null);
			
			commitProcessModel(pid);
		}
		return GsonHelper.toJson("success");
	}

	@Override
	@Transactional
	public String cancelProcessInstance(Map<String, Object> map) {
		String processInstanceId = String.valueOf(map.get("processInstanceId"));
		String areaId = String.valueOf(map.get("areaId"));
		Long pid = LongHelper.valueOf(processInstanceId);
		if(pid != null){
			beginProcessModel(pid);
			runner.rollbackProcessInstance(processInstanceId, null, null, areaId,null,false);
			
			commitProcessModel(pid);
		}
		
		return GsonHelper.toJson("success");
	}

	@Override
	@Transactional
	public String terminateProcessInstance(Map<String, Object> map) {
		String processInstanceId = String.valueOf(map.get("processInstanceId"));
		String areaId = String.valueOf(map.get("areaId"));
		Long pid = LongHelper.valueOf(processInstanceId);
		if(pid != null){
			beginProcessModel(pid);
			runner.terminateProcessInstance(processInstanceId, areaId,false);
			
			commitProcessModel(pid);
		}
		return GsonHelper.toJson("success");
	}

	@Override
	@Transactional
	public String processInstanceJump(Map<String, Object> map) {
		String processInstanceId = String.valueOf(map.get("processInstanceId"));
		String areaId = String.valueOf(map.get("areaId"));
		String toActivityId = StringHelper.valueOf(map.get("targetActivityId"));
		String fromActivityInstanceId = StringHelper.valueOf(map.get("fromActivityInstanceId"));

		Long pid = LongHelper.valueOf(processInstanceId);
		if(pid != null){
			beginProcessModel(pid);
			runner.processInstanceJump(processInstanceId, fromActivityInstanceId,
					toActivityId, areaId,false,null);
			
			commitProcessModel(pid);
		}
		return GsonHelper.toJson("success");
	}

	private void beginProcessModel(Long processInstanceId) {
		if (FastflowConfig.isCacheModel) {
			ProcessLocalHolder.clear();
			// 内存模型初始化
			if (processInstanceId != null) {
				ProcessModel processModel = uosCacheClient
						.getObject(ProcessModel.PROCESS_INSTANCE_MODEL
								+ processInstanceId, ProcessModel.class,
								processInstanceId);
				// logger.info("query:"+processModel.getActivityInstanceDtos().size()+"==="+processInstanceId);
				if (processModel == null) {
					processModel = new ProcessModel();
					ProcessInstanceDto processInstanceDto = processInstanceService
							.queryProcessInstance(processInstanceId.toString());
					processInstanceDto.setOperType(OperType.DEFAULT);
					List<ActivityInstanceDto> activityInstances = activityInstanceService
							.queryActivityInstanceByPid(processInstanceId);
					List<WorkItemDto> workItemDtos = workItemService
							.queryWorkItemsByProcess(
									processInstanceId.toString(), -1, null);
					List<TransitionInstanceDto> transitionInstanceDtos = transitionInstanceService
							.findTransitionInstances(
									processInstanceId.toString(), "1");
					List<TransitionInstanceDto> transitionInstanceDtos2 = transitionInstanceService
							.findTransitionInstances(
									processInstanceId.toString(), "0");
					if (transitionInstanceDtos != null
							&& transitionInstanceDtos2 != null) {
						transitionInstanceDtos.addAll(transitionInstanceDtos2);
					}
					Map<String, String> paramMap = processParamService
							.getAllProcessParams(processInstanceId);
					Map<String, String> attrMap = processAttrService
							.getProcessAttrsByPId(processInstanceId);
					processModel.setProcessInstanceDto(processInstanceDto);
					processModel.setActivityInstanceDtos(activityInstances);
					processModel.setWorkItemDtos(workItemDtos);
					processModel
							.setTransitionInstanceDtos(transitionInstanceDtos);
					processModel.setProcessInstanceId(processInstanceId);
					processModel.setParamMap(paramMap);
					processModel.setAttrMap(attrMap);
				}
				ProcessLocalHolder.set(processModel);
			}
		}
	}

	private void commitProcessModel(Long processInstanceId) {
		if (FastflowConfig.isCacheModel) {
			// 内存模型提交
			ProcessModel processModel = ProcessLocalHolder.get();
			if (processModel != null) {
				ProcessModel persistProcessModel = processModel
						.resetForPersist();
				// 异步持久化模型
				if (persistProcessModel != null) {
					runner.persistProcessModel(persistProcessModel);
					if(FastflowConfig.useHis){
						ProcessInstanceDto processInstanceDto = processModel.getProcessInstanceDto();
						if(WMProcessInstanceState.CLOSED_COMPLETED_INT == processInstanceDto.getState()
								|| WMProcessInstanceState.CLOSED_ZEROED_INT == processInstanceDto.getState()){
							String pid = StringHelper.valueOf(processInstanceId);
							if(pid != null){
								runner.dataToHis(pid);
							}
						}
					}
				}
//				logger.info("save:"+processModel.getActivityInstanceDtos().size()+"==="+processInstanceId);
				try {
					uosCacheClient.setObject(ProcessModel.PROCESS_INSTANCE_MODEL
							+ processInstanceId, processModel, processInstanceId);
				} catch (Exception e) {
					logger.error("---放入缓存异常："+e.getMessage(),e);
				}
			}
			ProcessLocalHolder.clear();
		}else{
			//竣工后写历史表
			if(FastflowConfig.useHis){
				ProcessInstanceDto processInstanceDto = processInstanceService.queryProcessInstance(StringHelper.valueOf(processInstanceId));
				if(WMProcessInstanceState.CLOSED_COMPLETED_INT == processInstanceDto.getState()
						|| WMProcessInstanceState.CLOSED_ZEROED_INT == processInstanceDto.getState()){
					String pid = StringHelper.valueOf(processInstanceDto.getProcessInstanceId());
					if(pid != null){
						runner.dataToHis(pid);
					}
				}
			}
		}
	}

	@Override
	public String reCreateWorkOrder(Map<String, Object> map) {
		String result = "success";
		 try {
			CommandQueueDto commandQueueDto = commandQueueService.qryCommandMsgInfoByWid(map);
			String commandMsg = null;
			if(commandQueueDto != null){
				commandMsg = commandQueueDto.getCommandMsg();
			}else{
				String processInstanceId = StringHelper.valueOf(map.get("processInstanceId"));
				String workItemId = StringHelper.valueOf(map.get("workItemId"));
				WorkItemDto workItemDto = workItemService.queryWorkItem(workItemId);
				ActivityInstanceDto activityInstanceDto = activityInstanceService
						.queryActivityInstance(workItemDto
								.getActivityInstanceId().toString());
				DubboCreateWorkOrderDto dto = new DubboCreateWorkOrderDto();
				dto.setSerial(UUID.randomUUID().toString());
				dto.setAreaCode(workItemDto.getAreaId());
				dto.setDirection(activityInstanceDto.getDirection());
				dto.setWorkItemId(workItemId);
				dto.setTacheCode(workItemDto.getTacheCode());
				dto.setTacheId(workItemDto.getTacheId().toString());
				dto.setTacheName(workItemDto.getName());
				dto.setBatchId(activityInstanceDto.getBatchid().toString());
				if(activityInstanceDto.getReverse() != null){
					ActivityInstanceDto relaActivityInstance = activityInstanceService.queryActivityInstance(activityInstanceDto.getReverse().toString());
					dto.setRelaWorkItemId(relaActivityInstance.getWorkItemId().toString());
				}
				Map<String, String> flowParamMap = processParamDefService
						.qryProInsTacheParam(
								workItemDto.getProcessDefineId(),
								workItemDto.getTacheCode());
				dto.setFlowParamMap(flowParamMap);
				dto.setProcessInstanceId(processInstanceId);
				commandMsg = GsonHelper.toJson(dto);
			}
			workFlowServerInf.executeCommand(commandMsg);
		} catch (Exception e) {
			result = "fail";
			logger.error("-----消息重投异常："+e.getMessage(),e);
		}
		return GsonHelper.toJson(result);
	}
	
	@Override
	public String reExcuteMsg(Map<String, Object> map) {
		String result = "success";
		 try {
			 String commandMsg = StringHelper.valueOf(map.get("commandMsg"));
			 logger.info("----commandMsg:"+commandMsg);
			 workFlowServerInf.executeCommand(commandMsg);
		} catch (Exception e) {
			result = "fail";
			logger.error("-----消息重投异常："+e.getMessage(),e);
		}
		return GsonHelper.toJson(result);
	}
	
	@SuppressWarnings("unchecked")
	public String processInstanceJumpForServer(Map<String, Object> map) {
		String processInstanceId = String.valueOf(map.get("processInstanceId"));
		String areaId = String.valueOf(map.get("areaId"));
		String toActivityId = StringHelper.valueOf(map.get("targetActivityId"));
		String fromActivityInstanceId = StringHelper.valueOf(map.get("fromActivityInstanceId"));
		Map<String, String> flowPassList = MapUtils.getMap(map, "flowPassList");
		
		beginProcessModel(LongHelper.valueOf(processInstanceId));
		
//		runner.processInstanceJump(processInstanceId, fromActivityInstanceId,
//				toActivityId, areaId,false);
		DubboProcessInstanceJumpDto dubboProcessInstanceJumpDto = new DubboProcessInstanceJumpDto();
		dubboProcessInstanceJumpDto.setAreaCode(areaId);
		dubboProcessInstanceJumpDto.setProcessInstanceId(processInstanceId);
		dubboProcessInstanceJumpDto.setToActivityId(toActivityId);
		dubboProcessInstanceJumpDto.setFromActivityInstanceId(fromActivityInstanceId);
		dubboProcessInstanceJumpDto.setFlowPassList(flowPassList);
		workFlowServerInf.processInstanceJump(dubboProcessInstanceJumpDto);
		
		commitProcessModel(LongHelper.valueOf(processInstanceId));
		return GsonHelper.toJson("success");
	}
}
