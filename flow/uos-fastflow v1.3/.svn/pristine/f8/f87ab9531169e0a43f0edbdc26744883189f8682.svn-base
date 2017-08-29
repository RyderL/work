package com.ztesoft.uosflow.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zterc.uos.base.cache.UosCacheClient;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.zterc.uos.fastflow.service.ActivityInstanceService;
import com.zterc.uos.fastflow.service.ProcessAttrService;
import com.zterc.uos.fastflow.service.ProcessInstanceService;
import com.zterc.uos.fastflow.service.ProcessParamService;
import com.zterc.uos.fastflow.service.TransitionInstanceService;
import com.zterc.uos.fastflow.service.WorkItemService;
import com.zterc.uos.fastflow.state.WMProcessInstanceState;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.common.UosFlowProxy;
import com.ztesoft.uosflow.core.config.ConfigContext;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.counter.CounterCenter;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.server.DataToHisDto;
import com.ztesoft.uosflow.core.dto.server.PersistProcessModelDto;
import com.ztesoft.uosflow.core.dto.server.PersistProcessModelRemoteDto;
import com.ztesoft.uosflow.core.uosflow.CommandDtoAsynHolder;
import com.ztesoft.uosflow.inf.client.common.ClientProxy;
import com.ztesoft.uosflow.inf.persist.client.PersistClientProxy;

public class CommandInvokeUtil {
	private static Logger logger = LoggerFactory
			.getLogger(CommandInvokeUtil.class);

	private static UosCacheClient uosCacheClient;
	private static ProcessInstanceService processInstanceService;
	private static ActivityInstanceService activityInstanceService;
	private static WorkItemService workItemService;
	private static TransitionInstanceService transitionInstanceService;
	private static ProcessParamService processParamService;
	private static ProcessAttrService processAttrService;

	public static ProcessAttrService getProcessAttrService() {
		return processAttrService;
	}

	public static void setProcessAttrService(ProcessAttrService processAttrService) {
		CommandInvokeUtil.processAttrService = processAttrService;
	}

	public static ProcessParamService getProcessParamService() {
		return processParamService;
	}

	public static void setProcessParamService(
			ProcessParamService processParamService) {
		CommandInvokeUtil.processParamService = processParamService;
	}

	public static ProcessInstanceService getProcessInstanceService() {
		return processInstanceService;
	}

	public static void setProcessInstanceService(
			ProcessInstanceService processInstanceService) {
		CommandInvokeUtil.processInstanceService = processInstanceService;
	}

	public static ActivityInstanceService getActivityInstanceService() {
		return activityInstanceService;
	}

	public static void setActivityInstanceService(
			ActivityInstanceService activityInstanceService) {
		CommandInvokeUtil.activityInstanceService = activityInstanceService;
	}

	public static WorkItemService getWorkItemService() {
		return workItemService;
	}

	public static void setWorkItemService(WorkItemService workItemService) {
		CommandInvokeUtil.workItemService = workItemService;
	}

	public static TransitionInstanceService getTransitionInstanceService() {
		return transitionInstanceService;
	}

	public static void setTransitionInstanceService(
			TransitionInstanceService transitionInstanceService) {
		CommandInvokeUtil.transitionInstanceService = transitionInstanceService;
	}

	public static UosCacheClient getUosCacheClient() {
		return uosCacheClient;
	}

	public static void setUosCacheClient(UosCacheClient uosCacheClient) {
		CommandInvokeUtil.uosCacheClient = uosCacheClient;
	}

	public static CommandResultDto invoke(Object invokeObj,
			CommandDto commandDto) {
		CommandResultDto commandResultDto = new CommandResultDto();
		Method method = null;
		String errorMsg = null;
		PlatformTransactionManager txManager = null;
		DefaultTransactionDefinition def = null;
		TransactionStatus status = null;
		boolean rollBackFlag = true;
		try {
			if(!FastflowConfig.isCacheModel || "persistProcessModel".equals(commandDto.getCommandCode())
					|| "saveCommandQueue".equals(commandDto.getCommandCode())){
				txManager = JDBCHelper.getTransactionManager();
				def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				status = txManager.getTransaction(def);
			}
			//modify by bobping 非同步命令不需要清命令线程变量
			if(!CommandPropUtil.getInstance().isSyn(commandDto.getCommandCode())){
				CommandDtoAsynHolder.clear();
			}
			
			logger.info(commandDto.getProcessInstanceId() + ":"
					+ commandDto.getCommandCode());
			if(FastflowConfig.useInvocation){
				method = invokeObj.getClass().getMethod(
						commandDto.getCommandCode(), CommandDto.class);
				commandResultDto = (CommandResultDto) (method.invoke(invokeObj,
						commandDto));
			}else{
				switch (commandDto.getCommandCode()) {
				case "createProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().createProcessInstance(commandDto);
					break;
				case "startProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().startProcessInstance(commandDto);
					break;
				case "completeWorkItem":
					commandResultDto = UosFlowProxy.getInstance().completeWorkItem(commandDto);
					break;
				case "persistProcessModel":
					commandResultDto = UosFlowProxy.getInstance().persistProcessModel(commandDto);
					break;
				case "rollbackProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().rollbackProcessInstance(commandDto);
					break;
				case "createAndStartProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().createAndStartProcessInstance(commandDto);
					break;
				case "disableWorkItem":
					commandResultDto = UosFlowProxy.getInstance().disableWorkItem(commandDto);
					break;
				case "abortProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().abortProcessInstance(commandDto);
					break;
				case "terminateProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().terminateProcessInstance(commandDto);
					break;
				case "suspendProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().suspendProcessInstance(commandDto);
					break;
				case "resumeProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().resumeProcessInstance(commandDto);
					break;
				case "restartProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().restartProcessInstance(commandDto);
					break;
				case "saveException":
					commandResultDto = UosFlowProxy.getInstance().saveException(commandDto);
					break;
				case "updateProcessInstance":
					commandResultDto = UosFlowProxy.getInstance().updateProcessInstance(commandDto);
					break;
				case "saveCommandQueue":
					commandResultDto = UosFlowProxy.getInstance().saveCommandQueue(commandDto);
					break;
				case "addTache":
					commandResultDto = UosFlowProxy.getInstance().addTache(commandDto);
					break;
				case "modTache":
					commandResultDto = UosFlowProxy.getInstance().modTache(commandDto);
					break;
				case "delTache":
					commandResultDto = UosFlowProxy.getInstance().delTache(commandDto);
					break;
				case "addReturnReason":
					commandResultDto = UosFlowProxy.getInstance().addReturnReason(commandDto);
					break;
				case "modReturnReason":
					commandResultDto = UosFlowProxy.getInstance().modReturnReason(commandDto);
					break;
				case "delReturnReason":
					commandResultDto = UosFlowProxy.getInstance().delReturnReason(commandDto);
					break;
				case "processInstanceJump":
					commandResultDto = UosFlowProxy.getInstance().processInstanceJump(commandDto);
					break;
				case "createWorkOrder":
					commandResultDto = ClientProxy.getInstance().createWorkOrder(commandDto);
					break;
				case "reportProcessState":
					commandResultDto = ClientProxy.getInstance().reportProcessState(commandDto);
					break;
				case "reportCalCondResult":
					commandResultDto = ClientProxy.getInstance().reportCalCondResult(commandDto);
					break;
				case "reportTimeLimit":
					commandResultDto = ClientProxy.getInstance().reportTimeLimit(commandDto);
					break;
				case "dataToHis":
					commandResultDto = UosFlowProxy.getInstance().dataToHis(commandDto);
					break;
				//新增命令执行第三方异步远程持久化 add by bobping on 2017-4-6
				case "persistProcessModelRemote":
					commandResultDto = UosFlowProxy.getInstance().persistProcessModelRemote(commandDto);
					break;
				case "suspendWorkItem":
					commandResultDto = UosFlowProxy.getInstance().suspendWorkItem(commandDto);
					break;
				default:
					break;
				}
			}
		} catch (SecurityException e) {
			errorMsg = "方法安全问题，无法反射：" + commandDto.getCommandCode() + "；异常信息："
					+ e.getMessage();
			logger.error(errorMsg, e);
		} catch (NoSuchMethodException e) {
			errorMsg = "方法不存在：" + commandDto.getCommandCode() + "；异常信息："
					+ e.getMessage();
			logger.error(errorMsg, e);
		} catch (IllegalArgumentException e) {
			errorMsg = "参数不正确：" + commandDto.getCommandCode() + "；异常信息："
					+ e.getMessage();
			logger.error(errorMsg, e);
		} catch (IllegalAccessException e) {
			errorMsg = "无法访问方法：" + commandDto.getCommandCode() + "；异常信息："
					+ e.getMessage();
			logger.error(errorMsg, e);
		} catch (InvocationTargetException e) {
			Throwable ex = null;
			StringBuffer sb = new StringBuffer();
			ex = e.getTargetException();
			sb.append("Error====" + e.getTargetException().getMessage()
					+ "====");
			sb.append(e.getTargetException().getClass().getName() + "\n");
			for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
				sb.append(stackTraceElement.getClassName() + "."
						+ stackTraceElement.getMethodName() + "["
						+ stackTraceElement.getLineNumber() + "]" + "\n");
			}
			errorMsg = "反射方法执行异常：" + commandDto.getCommandCode() + "；异常信息："
					+ sb.toString();
			if (errorMsg.length() > 1000) {
				errorMsg = errorMsg.substring(0, 1000);
			}
			logger.error("反射方法执行异常：" + commandDto.getCommandCode() + "；异常信息："
					+ e.getTargetException().getMessage(),
					e.getTargetException());
		} catch (Exception ex) {
			errorMsg = "执行异常：" + commandDto.getCommandCode() + "；异常信息："
					+ ex.getMessage();
			logger.error(ex.getMessage(), ex);
		} finally {
			try {
				if (errorMsg != null) {
					commandResultDto = CommandResultDtoUtil
							.createCommandResultDto(commandDto, false,
									errorMsg, null);
				}
				if (commandResultDto.getCommandDto() == null) {
					commandResultDto.setCommandDto(commandDto);
				}
				if (commandResultDto.isDealFlag()) {
					if(StringHelper.isEmpty(commandDto.getProcessInstanceId())){
						commandDto.setProcessInstanceId(commandResultDto.getProcessInstanceId());
					}
					
					dealForProcessModel(commandDto);
					if(txManager != null){
						txManager.commit(status);
					}
					rollBackFlag = false;
					CommandDtoAsynHolder.commit(commandDto);
					CounterCenter.increment(commandDto.getCommandCode());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if(rollBackFlag){
					if(txManager != null){
						txManager.rollback(status);
					}
				}
				ProcessLocalHolder.clear();
			}
		}
		return commandResultDto;
	}

	public static ProcessModel getCache(Long processInstanceId) {
		 ProcessModel processModel = uosCacheClient.getObject(ProcessModel.PROCESS_INSTANCE_MODEL
				+ processInstanceId, ProcessModel.class,
				Long.valueOf(processInstanceId));
		 if(processModel == null){
			 processModel = new ProcessModel();
			 ProcessInstanceDto processInstanceDto = processInstanceService.queryProcessInstance(processInstanceId.toString());
			 processInstanceDto.setOperType(OperType.DEFAULT);
			 List<ActivityInstanceDto> activityInstances = activityInstanceService.queryActivityInstanceByPid(processInstanceId);
			 List<WorkItemDto> workItemDtos = workItemService.queryWorkItemsByProcess(processInstanceId.toString(), -1, null);
			 List<TransitionInstanceDto> transitionInstanceDtos = transitionInstanceService.findTransitionInstances(processInstanceId.toString(),"1");
			 List<TransitionInstanceDto> transitionInstanceDtos2 = transitionInstanceService.findTransitionInstances(processInstanceId.toString(),"0");
			 if(transitionInstanceDtos != null && transitionInstanceDtos2 != null){
				 transitionInstanceDtos.addAll(transitionInstanceDtos2);
			 }
			 Map<String,String> paramMap = processParamService.getAllProcessParams(processInstanceId);
			 Map<String,String> attrMap = processAttrService.getProcessAttrsByPId(processInstanceId);
			 processModel.setProcessInstanceDto(processInstanceDto);
			 processModel.setActivityInstanceDtos(activityInstances);
			 processModel.setWorkItemDtos(workItemDtos);
			 processModel.setTransitionInstanceDtos(transitionInstanceDtos);
			 processModel.setProcessInstanceId(processInstanceId);
			 processModel.setParamMap(paramMap);
			 processModel.setAttrMap(attrMap);
		 }
		 return processModel;
	}

	public static void setCache(ProcessModel processModel){
		uosCacheClient.setObject(ProcessModel.PROCESS_INSTANCE_MODEL
				+ processModel.getProcessInstanceDto().getProcessInstanceId(),
				processModel, Long.valueOf(processModel.getProcessInstanceDto()
						.getProcessInstanceId()));
	}

	public static void clearCacheForDone(Long processInstanceId) {
		logger.info("clearCacheForDone:" + processInstanceId);
		// 流程竣工缓存清理
		if (FastflowConfig.isCacheModel) {
			if (processInstanceId != null) {
				uosCacheClient.del(ProcessModel.PROCESS_INSTANCE_MODEL
						+ processInstanceId, processInstanceId);
				ProcessLocalHolder.clear();
			}
		}
	}

	private static void dealForProcessModel(CommandDto commandDto) {
		if(CommandPropUtil.getInstance().isTypeOfServer(
				commandDto.getCommandCode()) && !("saveCommandQueue".equals(commandDto.getCommandCode()) 
				|| "persistProcessModel".equals(commandDto.getCommandCode()) || "persistProcessModelRemote".equals(commandDto.getCommandCode())
				|| "dataToHis".equals(commandDto.getCommandCode()))){
			// 内存模型提交
			if (FastflowConfig.isCacheModel) {
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					ProcessModel persistProcessModel = processModel
							.resetForPersist();
					if (logger.isInfoEnabled()) {
						logger.info(commandDto.getCommandCode()
								+ "--redis--setObject:[key:"
								+ processModel.getProcessInstanceDto()
										.getProcessInstanceId() + "]");
					}

					if (CommandPropUtil.getInstance().isTypeOfServer(
							commandDto.getCommandCode())) {
						setCache(processModel);
					}

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

							if(FastflowConfig.usePersistSelf){
								CommandProxy.getInstance().dealCommand(
										persistProcessModelDto);
							}else{
								PersistClientProxy.getInstance().sendMessage(persistProcessModelDto);
							}
							//竣工后写历史表
							if(FastflowConfig.useHis){
								saveDataToHis(StringHelper.valueOf(processModel
										.getProcessInstanceDto()
										.getProcessInstanceId()),commandDto.getAreaCode());
							}
						}
					} else if (persistProcessModel != null) {

						//usePersistBy3th第三方持久化的开关优先级高于usePersistSelf add by bobping
						if(!FastflowConfig.usePersistBy3th){
							PersistProcessModelDto persistProcessModelDto = new PersistProcessModelDto();
							if(FastflowConfig.usePersistSelf){
								persistProcessModelDto.setProcessModel(persistProcessModel);
								persistProcessModelDto.setProcessInstanceId(processModel
										.getProcessInstanceDto().getProcessInstanceId()
										.toString());
								CommandProxy.getInstance().dealCommand(
										persistProcessModelDto);
							}else{
								PersistClientProxy.getInstance().sendMessage(persistProcessModelDto);
							}
						}else{
							//使用第三方机制远程异步持久化 add by bobping
							PersistProcessModelRemoteDto persistProcessModelRemoteDto = new PersistProcessModelRemoteDto();
							persistProcessModelRemoteDto.setProcessModel(persistProcessModel);
							persistProcessModelRemoteDto.setProcessInstanceId(processModel
									.getProcessInstanceDto().getProcessInstanceId()
									.toString());
//							long t1 = System.currentTimeMillis();
							CommandProxy.getInstance().dealCommand(
									persistProcessModelRemoteDto);
//							logger.error("远程数据持久化处理耗时：ms" + (System.currentTimeMillis() - t1));
						}

						//竣工后写历史表
						if(FastflowConfig.useHis){
							ProcessInstanceDto processInstanceDto = persistProcessModel.getProcessInstanceDto();
							if(processInstanceDto != null){
								if(WMProcessInstanceState.CLOSED_COMPLETED_INT == processInstanceDto.getState()
										|| WMProcessInstanceState.CLOSED_ZEROED_INT == processInstanceDto.getState()){
									saveDataToHis(StringHelper.valueOf(processInstanceDto.getProcessInstanceId()),commandDto.getAreaCode());
								}
							}
						}
					}
				}
			}else{
				//竣工后写历史表
				if(FastflowConfig.useHis){
					ProcessInstanceDto processInstanceDto = processInstanceService.queryProcessInstance(commandDto.getProcessInstanceId());
					if(processInstanceDto != null){
						if(WMProcessInstanceState.CLOSED_COMPLETED_INT == processInstanceDto.getState()
								|| WMProcessInstanceState.CLOSED_ZEROED_INT == processInstanceDto.getState()){
							saveDataToHis(commandDto.getProcessInstanceId(),commandDto.getAreaCode());
						}
					}
				}
			}
		}
	}
	
	public static void saveDataToHis(String processInstanceId,String areaId) {
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
