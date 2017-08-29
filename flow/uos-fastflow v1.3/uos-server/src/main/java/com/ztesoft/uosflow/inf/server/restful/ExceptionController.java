package com.ztesoft.uosflow.inf.server.restful;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.zterc.uos.fastflow.service.ActivityInstanceService;
import com.zterc.uos.fastflow.service.CommandQueueService;
import com.zterc.uos.fastflow.service.ProcessInstanceService;
import com.zterc.uos.fastflow.service.WorkItemService;
import com.zterc.uos.fastflow.state.WMActivityInstanceState;
import com.zterc.uos.fastflow.state.WMProcessInstanceState;
import com.zterc.uos.fastflow.state.WMWorkItemState;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.config.ConfigContext;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.server.PersistProcessModelDto;
import com.ztesoft.uosflow.core.dto.server.PersistProcessModelRemoteDto;
import com.ztesoft.uosflow.core.util.CommandInvokeUtil;
import com.ztesoft.uosflow.inf.persist.client.PersistClientProxy;
import com.ztesoft.uosflow.inf.server.common.ServerProxy;


@Controller
@RequestMapping("/exception")
@Configuration
public class ExceptionController{
	private Logger logger = Logger.getLogger(ExceptionController.class);
	@Autowired
	private WorkItemService workItemService;
	@Autowired
	private ProcessInstanceService processInstanceService;
	@Autowired
	private ActivityInstanceService activityInstanceService;
	@Autowired
	private CommandQueueService commandQueueService;
	
	@RequestMapping(value = "/createNextTache",produces = "text/html;charset=UTF-8")
    public @ResponseBody String createNextTache(String param) {
		String result = "{\"DEAL_FLAG\":\"0\",\"DEAL_MSG\":\"接口调用成功\"}";
		boolean useDB = true;
		if(param != null && !"".equals(param)){
			Map<String,Object> paramMap = GsonHelper.toMap(param);
			String workItemId = StringHelper.valueOf(paramMap.get(InfConstant.INF_WORKITEMID));
			String processInstanceId = StringHelper.valueOf(paramMap.get(InfConstant.INF_PROCESSINSTANCEID));
			if(FastflowConfig.isCacheModel){
				useDB = false;
				//在内存模式需要初始化内存模型对象
				ProcessModel processModel = CommandInvokeUtil.getCache(Long.valueOf(processInstanceId));
				ProcessLocalHolder.set(processModel);
			}
			WorkItemDto workItem = workItemService.queryWorkItem(workItemId);
			if(workItem.getState() == WMWorkItemState.CLOSED_COMPLETED_INT){
				logger.info("---进入工作项提交成功，但是没出下一环节---workitemId:"+workItemId);
				ActivityInstanceDto curActivityInstanceDto = activityInstanceService.qryCurrentActivityByProcInstId(processInstanceId);
				ProcessInstanceDto processInstanceDto = processInstanceService.queryProcessInstance(processInstanceId);
				if(processInstanceDto.getState() == WMProcessInstanceState.ERROR_INT){
					if(ActivityInstanceDto.NORMAL_DIRECTION.equals(curActivityInstanceDto.getDirection())){
						processInstanceDto.setState(WMProcessInstanceState.OPEN_RUNNING_INT);
					}else{
						processInstanceDto.setState(WMProcessInstanceState.OPEN_RUNNING_ROLLBACK_INT);
					}
					processInstanceService.updateProcessInstance(processInstanceDto, useDB);
				}
				if(workItem.getActivityInstanceId().equals(curActivityInstanceDto.getId())){
					logger.info("---进入工作项提交成功，没生成工单，但是也没出下一个工作项---workitemId:"+workItemId);
					curActivityInstanceDto.setState(WMActivityInstanceState.OPEN_RUNNING_INT);
					curActivityInstanceDto.setStartedDate(DateHelper.getTimeStamp());
					activityInstanceService.updateActivityInstance(curActivityInstanceDto, useDB);
					workItem.setState(WMWorkItemState.OPEN_RUNNING_INT);
					workItemService.updateWorkItem(workItem, useDB);
					commitProcessModel(processInstanceId);
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("workItemId", workItemId);
					map.put("commandCode", "completeWorkItem");
					CommandQueueDto commandQueueDto = commandQueueService.qryCommandMsgInfoByWid(map);
					result = ServerProxy.getInstance().dealForJson(commandQueueDto.getCommandMsg());
				}else{
					logger.info("---进入工作项提交成功，没有生成工单，但是出了下一个工作项---workitemId:"+workItemId);
					String curWorkItemDtoId = activityInstanceService.queryWrokItemId(curActivityInstanceDto.getId());
					WorkItemDto curWorkItemDto = workItemService.queryWorkItem(curWorkItemDtoId);
					if(curWorkItemDto.getStartedDate().getTime() > workItem.getStartedDate().getTime()){
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("workItemId", curWorkItemDtoId);
						map.put("commandCode", "createWorkOrder");
						CommandQueueDto commandQueueDto = commandQueueService.qryCommandMsgInfoByWid(map);
						result = ServerProxy.getInstance().dealForJson(commandQueueDto.getCommandMsg());
					}
				}
			}else{
				logger.info("---进入工作项提交失败，没有出下一个环节---workitemId:"+workItemId);
				ProcessInstanceDto processInstanceDto = processInstanceService.queryProcessInstance(processInstanceId);
				if(processInstanceDto.getState() == WMProcessInstanceState.ERROR_INT){
					ActivityInstanceDto activityInstanceDto = activityInstanceService.queryActivityInstance(StringHelper.valueOf(workItem.getActivityInstanceId()));
					if(ActivityInstanceDto.NORMAL_DIRECTION.equals(activityInstanceDto.getDirection())){
						processInstanceDto.setState(WMProcessInstanceState.OPEN_RUNNING_INT);
					}else{
						processInstanceDto.setState(WMProcessInstanceState.OPEN_RUNNING_ROLLBACK_INT);
					}
					processInstanceService.updateProcessInstance(processInstanceDto, useDB);
				}
				commitProcessModel(processInstanceId);
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("workItemId", workItemId);
				map.put("commandCode", "completeWorkItem");
				CommandQueueDto commandQueueDto = commandQueueService.qryCommandMsgInfoByWid(map);
				result = ServerProxy.getInstance().dealForJson(commandQueueDto.getCommandMsg());
			}
		}
		return result;
    }
	private void commitProcessModel(String processInstanceId) {
		// 内存模型提交
		if (FastflowConfig.isCacheModel) {
			ProcessModel processModel = ProcessLocalHolder.get();
			if (processModel != null) {
				ProcessModel persistProcessModel = processModel
						.resetForPersist();

				CommandInvokeUtil.setCache(processModel);

				// 异步持久化模型
				if (ConfigContext.isPersistAfterDone()) {
					if (processModel.getProcessInstanceDto() != null
							&& processModel.getProcessInstanceDto().getState() == WMProcessInstanceState.CLOSED_COMPLETED_INT) {
						PersistProcessModelDto persistProcessModelDto = new PersistProcessModelDto();
						persistProcessModelDto.setProcessModel(processModel);
						persistProcessModelDto
								.setProcessInstanceId(processModel
										.getProcessInstanceDto()
										.getProcessInstanceId().toString());

						if (FastflowConfig.usePersistSelf) {
							CommandProxy.getInstance().dealCommand(
									persistProcessModelDto);
						} else {
							PersistClientProxy.getInstance().sendMessage(
									persistProcessModelDto);
						}
						// 竣工后写历史表
						if (FastflowConfig.useHis) {
							CommandInvokeUtil.saveDataToHis(StringHelper
									.valueOf(processModel
											.getProcessInstanceDto()
											.getProcessInstanceId()),
									processModel.getProcessInstanceDto()
											.getAreaId());
						}
					}
				} else if (persistProcessModel != null) {

					// usePersistBy3th第三方持久化的开关优先级高于usePersistSelf add by
					// bobping
					if (!FastflowConfig.usePersistBy3th) {
						PersistProcessModelDto persistProcessModelDto = new PersistProcessModelDto();
						if (FastflowConfig.usePersistSelf) {
							persistProcessModelDto
									.setProcessModel(persistProcessModel);
							persistProcessModelDto
									.setProcessInstanceId(processModel
											.getProcessInstanceDto()
											.getProcessInstanceId().toString());
							CommandProxy.getInstance().dealCommand(
									persistProcessModelDto);
						} else {
							PersistClientProxy.getInstance().sendMessage(
									persistProcessModelDto);
						}
					} else {
						// 使用第三方机制远程异步持久化 add by bobping
						PersistProcessModelRemoteDto persistProcessModelRemoteDto = new PersistProcessModelRemoteDto();
						persistProcessModelRemoteDto
								.setProcessModel(persistProcessModel);
						persistProcessModelRemoteDto
								.setProcessInstanceId(processModel
										.getProcessInstanceDto()
										.getProcessInstanceId().toString());
						CommandProxy.getInstance().dealCommand(
								persistProcessModelRemoteDto);
					}

					// 竣工后写历史表
					if (FastflowConfig.useHis) {
						ProcessInstanceDto processInstanceDto = persistProcessModel
								.getProcessInstanceDto();
						if (processInstanceDto != null) {
							if (WMProcessInstanceState.CLOSED_COMPLETED_INT == processInstanceDto
									.getState()
									|| WMProcessInstanceState.CLOSED_ZEROED_INT == processInstanceDto
											.getState()) {
								CommandInvokeUtil.saveDataToHis(processInstanceId,
										processInstanceDto.getAreaId());
							}
						}
					}
				}
			}
		} else {
			// 竣工后写历史表
			if (FastflowConfig.useHis) {
				ProcessInstanceDto processInstanceDto = processInstanceService
						.queryProcessInstance(processInstanceId);
				if (processInstanceDto != null) {
					if (WMProcessInstanceState.CLOSED_COMPLETED_INT == processInstanceDto
							.getState()
							|| WMProcessInstanceState.CLOSED_ZEROED_INT == processInstanceDto
									.getState()) {
						CommandInvokeUtil.saveDataToHis(processInstanceId,
								processInstanceDto.getAreaId());
					}
				}
			}
		}
	}
}
