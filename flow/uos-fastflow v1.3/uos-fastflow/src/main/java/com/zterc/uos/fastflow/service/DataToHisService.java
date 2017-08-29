package com.zterc.uos.fastflow.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.fastflow.dao.process.his.ActivityInstanceHisDAO;
import com.zterc.uos.fastflow.dao.process.his.CommandQueueHisDAO;
import com.zterc.uos.fastflow.dao.process.his.ExceptionHisDAO;
import com.zterc.uos.fastflow.dao.process.his.ProcessAttrHisDAO;
import com.zterc.uos.fastflow.dao.process.his.ProcessInstanceHisDAO;
import com.zterc.uos.fastflow.dao.process.his.ProcessParamHisDAO;
import com.zterc.uos.fastflow.dao.process.his.TransitionInstanceHisDAO;
import com.zterc.uos.fastflow.dao.process.his.WorkItemHisDAO;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.process.ProInstAttrDto;
import com.zterc.uos.fastflow.dto.process.ProInstParamDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;

public class DataToHisService {
	private Logger logger = Logger.getLogger(DataToHisService.class);
	private ProcessInstanceHisDAO processInstanceHisDAO;
	private ActivityInstanceHisDAO activityInstanceHisDAO;
	private WorkItemHisDAO workItemHisDAO;
	private TransitionInstanceHisDAO transitionInstanceHisDAO;
	private ExceptionHisDAO exceptionHisDAO;
	private CommandQueueHisDAO commandQueueHisDAO;
	private ProcessAttrHisDAO processAttrHisDAO;
	private ProcessParamHisDAO processParamHisDAO;
	private ProcessInstanceService processInstanceService;
	private ActivityInstanceService activityInstanceService;
	private WorkItemService workItemService;
	private TransitionInstanceService transitionInstanceService;
	private ProcessAttrService processAttrService;
	private ProcessParamService processParamService;
	private ExceptionService exceptionService;
	private CommandQueueService commandQueueService;
	
	public void setProcessInstanceHisDAO(ProcessInstanceHisDAO processInstanceHisDAO) {
		this.processInstanceHisDAO = processInstanceHisDAO;
	}

	public void setActivityInstanceHisDAO(
			ActivityInstanceHisDAO activityInstanceHisDAO) {
		this.activityInstanceHisDAO = activityInstanceHisDAO;
	}

	public void setWorkItemHisDAO(WorkItemHisDAO workItemHisDAO) {
		this.workItemHisDAO = workItemHisDAO;
	}

	public void setTransitionInstanceHisDAO(
			TransitionInstanceHisDAO transitionInstanceHisDAO) {
		this.transitionInstanceHisDAO = transitionInstanceHisDAO;
	}

	public void setExceptionHisDAO(ExceptionHisDAO exceptionHisDAO) {
		this.exceptionHisDAO = exceptionHisDAO;
	}

	public void setCommandQueueHisDAO(CommandQueueHisDAO commandQueueHisDAO) {
		this.commandQueueHisDAO = commandQueueHisDAO;
	}

	public void setProcessAttrHisDAO(ProcessAttrHisDAO processAttrHisDAO) {
		this.processAttrHisDAO = processAttrHisDAO;
	}

	public void setProcessParamHisDAO(ProcessParamHisDAO processParamHisDAO) {
		this.processParamHisDAO = processParamHisDAO;
	}

	public ProcessInstanceService getProcessInstanceService() {
		return processInstanceService;
	}

	public void setProcessInstanceService(
			ProcessInstanceService processInstanceService) {
		this.processInstanceService = processInstanceService;
	}

	public ActivityInstanceService getActivityInstanceService() {
		return activityInstanceService;
	}

	public void setActivityInstanceService(
			ActivityInstanceService activityInstanceService) {
		this.activityInstanceService = activityInstanceService;
	}

	public WorkItemService getWorkItemService() {
		return workItemService;
	}

	public void setWorkItemService(WorkItemService workItemService) {
		this.workItemService = workItemService;
	}

	public TransitionInstanceService getTransitionInstanceService() {
		return transitionInstanceService;
	}

	public void setTransitionInstanceService(
			TransitionInstanceService transitionInstanceService) {
		this.transitionInstanceService = transitionInstanceService;
	}

	public ProcessAttrService getProcessAttrService() {
		return processAttrService;
	}

	public void setProcessAttrService(ProcessAttrService processAttrService) {
		this.processAttrService = processAttrService;
	}

	public ProcessParamService getProcessParamService() {
		return processParamService;
	}

	public void setProcessParamService(ProcessParamService processParamService) {
		this.processParamService = processParamService;
	}

	public ExceptionService getExceptionService() {
		return exceptionService;
	}

	public void setExceptionService(ExceptionService exceptionService) {
		this.exceptionService = exceptionService;
	}

	public CommandQueueService getCommandQueueService() {
		return commandQueueService;
	}

	public void setCommandQueueService(CommandQueueService commandQueueService) {
		this.commandQueueService = commandQueueService;
	}

	public void saveDataToHis(String processInstanceId) throws Exception{
		if(processInstanceId == null || "".equals(processInstanceId)){
			throw new Exception("数据转存传入的processInstanceId为空，请检查数据准确性！");
		}
		//查询在途数据
		ProcessInstanceDto processInstanceDto = processInstanceService.queryProcessInstance(processInstanceId);
		if(processInstanceDto != null){
			List<ActivityInstanceDto> activityInstances = activityInstanceService.queryActivityInstanceByPid(LongHelper.valueOf(processInstanceId));
			List<TransitionInstanceDto> transitionInstanceDtos = transitionInstanceService.findTransitionInstances(processInstanceId);
			List<WorkItemDto> workItemDtos = workItemService.queryWorkItemsByPid(processInstanceId);
			List<ProInstAttrDto> proInstAttrDtos = processAttrService.getAllProcessAttrs(LongHelper.valueOf(processInstanceId));
			List<ProInstParamDto> proInstParamDtos = processParamService.getAllProcessParamsByPid(LongHelper.valueOf(processInstanceId));
			List<ExceptionDto> exceptionDtos = exceptionService.queryExceptionsByPid(processInstanceId);
			List<CommandQueueDto> commandQueueDtos = commandQueueService.qryCommandQueueDtosByPid(processInstanceId);
			//转存到历史表
			processInstanceHisDAO.createProcessInstanceHis(processInstanceDto);
			if(activityInstances != null && activityInstances.size() > 0){
				for(ActivityInstanceDto activityInstanceDto:activityInstances){
					activityInstanceHisDAO.createActivityInstanceHis(activityInstanceDto);
				}
			}
			if(workItemDtos != null && workItemDtos.size() > 0){
				for(WorkItemDto workItemDto:workItemDtos){
					workItemHisDAO.createWorkItemHis(workItemDto);
				}
			}
			if(transitionInstanceDtos != null && transitionInstanceDtos.size() > 0){
				for(TransitionInstanceDto transitionInstanceDto:transitionInstanceDtos){
					transitionInstanceHisDAO.createTransitionInstanceHis(transitionInstanceDto);
				}
			}
			if(proInstAttrDtos != null && proInstAttrDtos.size() > 0){
				for(ProInstAttrDto proInstAttrDto:proInstAttrDtos){
					processAttrHisDAO.saveProInsAttrHis(proInstAttrDto);
				}
			}
			if(proInstParamDtos != null && proInstParamDtos.size() > 0){
				for(ProInstParamDto proInstParamDto:proInstParamDtos){
					processParamHisDAO.addProcessParamHis(proInstParamDto);
				}
			}
			if(exceptionDtos != null && exceptionDtos.size() > 0 ){
				for(ExceptionDto exceptionDto:exceptionDtos){
					exceptionHisDAO.createExceptionHis(exceptionDto);
				}
			}
			if(commandQueueDtos != null && commandQueueDtos.size() > 0){
				for(CommandQueueDto commandQueueDto:commandQueueDtos){
					commandQueueHisDAO.addCommandQueueHis(commandQueueDto);
				}
			}
			
			//删除过程表的数据
			processInstanceService.deleteProcessInstance(processInstanceId);
			processAttrService.deleteByPid(processInstanceId);
			processParamService.deleteByPid(processInstanceId);
			activityInstanceService.deleteByPid(processInstanceId);
			transitionInstanceService.deleteByPid(processInstanceId);
			workItemService.deleteByPid(processInstanceId);
			exceptionService.deleteByPid(processInstanceId);
			commandQueueService.deleteByPid(processInstanceId);
		}else{
			logger.error("-----根据流程实例id:"+processInstanceId+"没有找到流程实例，请确认数据存在！-----");
		}
	}

}
