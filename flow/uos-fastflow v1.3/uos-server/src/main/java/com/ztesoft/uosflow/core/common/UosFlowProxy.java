package com.ztesoft.uosflow.core.common;

import java.sql.Timestamp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.base.lock.FlowLock;
import com.zterc.uos.base.lock.LockFactory;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.core.FastflowRunner;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonDto;
import com.zterc.uos.fastflow.dto.specification.TacheDto;
import com.zterc.uos.fastflow.exception.FastflowException;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.zterc.uos.fastflow.jdbc.sqlHolder.AsynSqlExecBy3thParamDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.SqlLocalHolder;
import com.zterc.uos.fastflow.service.ReturnReasonService;
import com.zterc.uos.fastflow.service.TacheService;
import com.zterc.uos.fastflow.state.WMProcessInstanceState;
import com.ztesoft.uosflow.core.dbpersist.dto.AsyncSqlDto;
import com.ztesoft.uosflow.core.dbpersist.dto.AsyncSqlDto4Inst;
import com.ztesoft.uosflow.core.dbpersist.util.SqlPersistQueueUtils;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.client.ExecuteSQLAsynBy3thDto;
import com.ztesoft.uosflow.core.dto.server.AbortProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.AddReturnReasonDto;
import com.ztesoft.uosflow.core.dto.server.AddTacheDto;
import com.ztesoft.uosflow.core.dto.server.CompleteWorkItemDto;
import com.ztesoft.uosflow.core.dto.server.CreateProcessInstacneDto;
import com.ztesoft.uosflow.core.dto.server.DataToHisDto;
import com.ztesoft.uosflow.core.dto.server.DelReturnReasonDto;
import com.ztesoft.uosflow.core.dto.server.DelTacheDto;
import com.ztesoft.uosflow.core.dto.server.DisableWorkItemDto;
import com.ztesoft.uosflow.core.dto.server.ModReturnReasonDto;
import com.ztesoft.uosflow.core.dto.server.ModTacheDto;
import com.ztesoft.uosflow.core.dto.server.PersistProcessModelDto;
import com.ztesoft.uosflow.core.dto.server.PersistProcessModelRemoteDto;
import com.ztesoft.uosflow.core.dto.server.ProcessInstanceJumpDto;
import com.ztesoft.uosflow.core.dto.server.RestartProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.ResumeProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.RollbackProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.SaveCommandQueueDto;
import com.ztesoft.uosflow.core.dto.server.SaveExceptionDto;
import com.ztesoft.uosflow.core.dto.server.StartProcessInstacneDto;
import com.ztesoft.uosflow.core.dto.server.SuspendProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.SuspendWorkItemDto;
import com.ztesoft.uosflow.core.dto.server.TerminateProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.UpdateProcessInstanceDto;
import com.ztesoft.uosflow.core.uosflow.CommandDtoAsynHolder;
import com.ztesoft.uosflow.core.util.CommandInvokeUtil;
import com.ztesoft.uosflow.core.util.CommandResultDtoUtil;
import com.ztesoft.uosflow.dubbo.dto.client.DubboAsynSqlExecBy3thDto;
import com.ztesoft.uosflow.inf.persist.client.PersistBy3thClientProxy;

@Component("uosFlowProxy")
public class UosFlowProxy {
	private Logger logger = LoggerFactory.getLogger(UosFlowProxy.class);

	@Autowired
	private FastflowRunner fastflowRunner;
	@Autowired
	private TacheService tacheService;
	@Autowired
	private ReturnReasonService returnReasonService;
	@Autowired
	private SqlPersistQueueUtils sqlPersistQueueUtils;
	public static String[] INST_TABLES=new String[]{"UOS_ACTIVITYINSTANCE","UOS_WORKITEM","UOS_PROCESSINSTANCE","UOS_TRANSITIONINSTANCE"};

	public UosFlowProxy() {
	}

	public static UosFlowProxy getInstance() {
		return (UosFlowProxy) ApplicationContextProxy.getBean("uosFlowProxy");
	}

	public CommandResultDto dealCommand(CommandDto commandDto) {
		ProcessLocalHolder.clear();
		
		//如果commandDto.getProcessInstanceId()为空，就是创建流程实例接口，创建流程实例接口不用考虑锁和内存模型初始化
		if (!StringHelper.isEmpty(commandDto.getProcessInstanceId())
				&& !("saveCommandQueue".equals(commandDto.getCommandCode())
						|| "persistProcessModel".equals(commandDto.getCommandCode())
						|| "persistProcessModelRemote".equals(commandDto.getCommandCode()))){
			boolean needLock = true;
			
			/**
			 * 以下注释逻辑移至锁机制内控制，防止并行环节多线程并发流程不能扭转的问题  add by bobping on 
			 */
//			if(FastflowConfig.isCacheModel){
//				//UosFlowProxy里面的接口在内存模式需要初始化内存模型对象
//				if (logger.isInfoEnabled()) {
//					logger.info(commandDto.getCommandCode()+ "--redis--getObject:[key:"+ commandDto.getProcessInstanceId() + "]");
//				}
//				ProcessModel processModel = CommandInvokeUtil.getCache(Long.valueOf(commandDto.getProcessInstanceId()));
//				ProcessLocalHolder.set(processModel);
////				logger.info(commandDto.getCommandCode()+ "--redis--getObject:[key:"+ commandDto.getProcessInstanceId() + "]---processModel:"+GsonHelper.toJson(processModel));
//				
//				//不是异步持久化命令（saveException和persistProcessModel不需要加锁）
//				if(!commandDto.getCommandCode().equals("saveException")
//						&&!commandDto.getCommandCode().equals("persistProcessModel")){
//					needLock = false;
//				}
//			}
			
			//如果配LockFactory配置BLANK,不加锁（主要在于串行性能测试的时候使用，默认LOCAL锁）
			if(LockFactory.isNoLock()){
				needLock = false;
			}
			//增加开关，如果没有配置要加锁的命令，则默认全部加锁，否则只给配置了要加锁的命令加锁 add by che.zi 20170729
			if(FastflowConfig.needLockCommand != null && !"".equals(FastflowConfig.needLockCommand)
					 && !FastflowConfig.lockCommands.contains(commandDto.getCommandCode())){
				needLock = false;
				logger.info("----不需要加锁---命令编码:"+commandDto.getCommandCode());
			}
			//end 20170729
			
			//设置分库数据源
			String ds = DsContextHolder.getHoldDs();
			//如果需要加锁
			try {
				DsContextHolder.setDsForInstance(Long.valueOf(commandDto.getProcessInstanceId()));
				if(needLock){
					String processInstanceId = commandDto.getProcessInstanceId().intern();
//					synchronized(processInstanceId){
						FlowLock lock = null;
						try {
							lock = LockFactory.getLock(commandDto.getProcessInstanceId());
							if(lock.acquire()){
								this.setProcessModelThreadLocal(commandDto);
								return CommandInvokeUtil.invoke(this, commandDto);
							}else{
								throw new FastflowException("锁等待超时-----processInstanceId="+processInstanceId);
							}
						} catch (Exception e) {
							throw e;
						} finally{
							if(lock!=null){
								lock.release();
							}
						}
//					}
				}
				//不需要加锁
				else{
					this.setProcessModelThreadLocal(commandDto);
					return CommandInvokeUtil.invoke(this, commandDto);
				}
			} catch (Exception e) {
				throw e;
			} finally{
				//还原选择的数据源
				DsContextHolder.setHoldDs(ds);
			}
		}else{
			return CommandInvokeUtil.invoke(this, commandDto);
		}
	}

	/**
	 * 内存模式下在保存processModel线程变量
	 * @param commandDto   
	 * @author bobping
	 * @date 2017年3月1日
	 */
	public void setProcessModelThreadLocal(CommandDto commandDto){
		if(FastflowConfig.isCacheModel){
			//UosFlowProxy里面的接口在内存模式需要初始化内存模型对象
			if (logger.isInfoEnabled()) {
				logger.info(commandDto.getCommandCode()+ "--redis--getObject:[key:"+ commandDto.getProcessInstanceId() + "]");
			}
			ProcessModel processModel = CommandInvokeUtil.getCache(Long.valueOf(commandDto.getProcessInstanceId()));
			ProcessLocalHolder.set(processModel);
		}
	}
	
	/**
	 * 创建流程实例
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto createProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		CreateProcessInstacneDto createProcessInstacneDto = (CreateProcessInstacneDto) commandDto;
		String processDefinitionCode = createProcessInstacneDto
				.getFlowPackageCode();
		String processInstanceName = "";
		String processPriority = createProcessInstacneDto.getPriority();
		String areaCode = createProcessInstacneDto.getAreaCode();
		Map<String, String> formData = new HashMap<String, String>();
		Map<String, String> flowParamMap = createProcessInstacneDto.getFlowParamList();
		try {
			if (flowParamMap != null && flowParamMap.size() > 0) {
				for (String key : flowParamMap.keySet()) {
					formData.put(key, flowParamMap.get(key));
				}
			}
			/** 创建流程实例 */
			ProcessInstanceDto processInstanceDto = null;
			processInstanceDto = fastflowRunner.createProcessInstanceByCode(
					processDefinitionCode, null, processInstanceName,
					Integer.parseInt(processPriority), formData, areaCode,
					false);

			// logger.error("创建流程实例消耗: " + (time2 -time1));
			if (processInstanceDto != null) {
				retDto =  CommandResultDtoUtil.createCommandResultDto(
						commandDto, true, "创建流程实例成功", processInstanceDto
								.getProcessInstanceId().toString());
				retDto.setFlowParamList(processInstanceDto.getFlowParamMap());
				retDto.setFlowPassList(createProcessInstacneDto.getFlowPassList());
				logger.debug("=====>>>createProcessInstance： "
						+ "创建流程实例成功，流程引擎客户端返回的流程实例ID："
						+ processInstanceDto.getProcessInstanceId());
			} else {
				retDto = CommandResultDtoUtil.createCommandResultDto(
						commandDto, false, "创建流程实例失败", "-1");
				logger.error("创建流程实例失败!");
			}
		} catch (Exception e) {
			logger.error("启动流程实例异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "创建流程实例异常！", "-1");
			throw e;
		}
		return retDto;
	}

	/**
	 * 创建并启动流程实例
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto createAndStartProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		CreateProcessInstacneDto createProcessInstacneDto = (CreateProcessInstacneDto) commandDto;
		String processDefinitionCode = createProcessInstacneDto
				.getFlowPackageCode();
		String processInstanceName = "";
		String processPriority = createProcessInstacneDto.getPriority();
		String areaCode = createProcessInstacneDto.getAreaCode();
		Map<String, String> formData = new HashMap<String, String>();
		Map<String, String> flowParamMap = createProcessInstacneDto.getFlowParamList();
		try {
			if (flowParamMap != null && flowParamMap.size() > 0) {
				for (String key : flowParamMap.keySet()) {
					formData.put(key, flowParamMap.get(key));
				}
			}
			/** 创建流程实例 */
			ProcessInstanceDto processInstanceDto = null;
			processInstanceDto = fastflowRunner.createProcessInstanceByCode(
					processDefinitionCode, null, processInstanceName,
					Integer.parseInt(processPriority), formData, areaCode,
					false);
			long time2 = System.currentTimeMillis();
			if (processInstanceDto != null) {
				retDto = CommandResultDtoUtil.createCommandResultDto(
						commandDto, true, "创建流程实例成功", processInstanceDto
								.getProcessInstanceId().toString());
				retDto.setFlowParamList(formData);
				retDto.setFlowPassList(createProcessInstacneDto.getFlowPassList());
				logger.debug("=====>>>createProcessInstance： "
						+ "创建流程实例成功，流程引擎客户端返回的流程实例ID："
						+ processInstanceDto.getProcessInstanceId());

				StartProcessInstacneDto startProcessInstacneDto = new StartProcessInstacneDto();
				startProcessInstacneDto.setSerial(commandDto.getSerial());
				startProcessInstacneDto.setFrom(commandDto.getFrom());
				startProcessInstacneDto.setTo(commandDto.getTo());
				startProcessInstacneDto.setAreaCode(commandDto.getAreaCode());
				startProcessInstacneDto.setPriority(processPriority);
				startProcessInstacneDto.setProcessInstanceId(processInstanceDto
						.getProcessInstanceId().toString());
				startProcessInstacneDto.setFlowPassList(createProcessInstacneDto.getFlowPassList());
				startProcessInstacneDto.setFlowParamList(formData);

				CommandDtoAsynHolder.addCommandDto(startProcessInstacneDto);

				long time3 = System.currentTimeMillis();
				logger.debug("启动流程实例消耗: " + (time3 - time2));

			} else {
				retDto = CommandResultDtoUtil.createCommandResultDto(
						commandDto, false, "创建流程实例失败", "-1");
				logger.error("创建流程实例失败!");
			}
		} catch (Exception e) {
			logger.error("启动流程实例异常, 异常信息：" + e, e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "创建流程实例异常！", "-1");
			throw e;
		}
		return retDto;
	}

	/**
	 * 启动流程实例
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto startProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		StartProcessInstacneDto startProcessInstacneDto = (StartProcessInstacneDto) commandDto;

		String processInstanceId = startProcessInstacneDto
				.getProcessInstanceId();
		String areaId = startProcessInstacneDto.getAreaCode();
		Map<String,String> flowPassList = startProcessInstacneDto.getFlowPassList();
		Map<String,String> flowParamList = startProcessInstacneDto.getFlowParamList();
		try {
			fastflowRunner.startProcessInstance(processInstanceId, areaId,flowPassList,
					false,flowParamList);

			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "启动流程实例命令发送成功",
					startProcessInstacneDto.getProcessInstanceId());
			logger.debug("=====>>>startProcessInstance： "
					+ "启动流程实例命令已发送，processInstanceId：" + processInstanceId);
		} catch (Exception e) {
			logger.error("启动流程实例异常, 异常信息：" + e, e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "启动流程实例命令发送异常",
					startProcessInstacneDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	// /**
	// * 执行正向流转
	// * @param commandDto
	// * @return
	// */
	// public CommandResultDto executeEfferentTransitions(CommandDto commandDto)
	// {
	// CommandResultDto retDto = null;
	//
	// ExecuteEfferentTransitionsDto efferentTransitionsDto =
	// (ExecuteEfferentTransitionsDto)commandDto;
	//
	// Activity activity = efferentTransitionsDto.getActivity();
	// ActivityInstanceDto activityInstance =
	// efferentTransitionsDto.getActivityInstance();
	// ProcessInstanceDto processInstance =
	// efferentTransitionsDto.getProcessInstance();
	// try {
	// fastflowRunner.executeEfferentTransitionsNew(activity, activityInstance,
	// processInstance,efferentTransitionsDto.getAreaCode());
	// retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, true,
	// "执行正向流转成功"
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.debug( "=============>>>executeEfferentTransitions： " +
	// "执行正向流转命令已发送，activity：" + activity);
	// } catch (Exception e) {
	// logger.error("执行正向流转发送异常, 异常信息：" + e);
	// retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, false,
	// "执行正向流转发送异常, 异常信息：" + e
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// }
	// return retDto;
	// }
	//
	// /**
	// * 执行路由
	// * @param commandDto
	// * @return
	// */
	// public CommandResultDto executeTransition(CommandDto commandDto){
	// CommandResultDto retDto = null;
	//
	// ExecuteTransitionDto efferentTransitionsDto =
	// (ExecuteTransitionDto)commandDto;
	//
	// Activity activity = efferentTransitionsDto.getActivity();
	// ActivityInstanceDto activityInstance =
	// efferentTransitionsDto.getActivityInstance();
	// ProcessInstanceDto processInstance =
	// efferentTransitionsDto.getProcessInstance();
	// boolean splitXOR = efferentTransitionsDto.getSplitXOR();
	// String areaId = efferentTransitionsDto.getAreaCode();
	// Transition transition = efferentTransitionsDto.getTransition();
	// WorkflowProcess process = efferentTransitionsDto.getProcess();
	// try {
	// fastflowRunner.executeTransition(transition,activity,activityInstance,process,processInstance,splitXOR,areaId);
	// retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, true,
	// "执行路由命令成功"
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.debug( "=============>>>executeTransition： " +
	// "执行路由命令已发送，activity：" + activity);
	// } catch (Exception e) {
	// retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, false,
	// "执行路由命令异常, 异常信息：" + e
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.error("执行路由发送异常, 异常信息：" + e, e);
	// }
	// return retDto;
	// }
	//
	// /**
	// * 激活活动实例
	// * @param commandDto
	// * @return
	// * @throws Exception
	// */
	// public CommandResultDto enableActivityInstances(CommandDto commandDto){
	// CommandResultDto retDto = null;
	//
	// EnableActivityInstancesDto efferentTransitionsDto =
	// (EnableActivityInstancesDto)commandDto;
	//
	// Activity activity = efferentTransitionsDto.getActivity();
	// ActivityInstanceDto activityInstance =
	// efferentTransitionsDto.getActivityInstance();
	// ArrayList<ActivityContext> activityList =
	// efferentTransitionsDto.getActivityList();
	// WorkflowProcess process = efferentTransitionsDto.getProcess();
	// ProcessInstanceDto processInstance =
	// efferentTransitionsDto.getProcessInstance();
	// String areaId = efferentTransitionsDto.getAreaCode();
	// try {
	// fastflowRunner.enableActivityInstances(activityList,activity,activityInstance,processInstance,
	// process,areaId);
	//
	// retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, true,
	// "激活活动实例转成功"
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.debug( "=============>>>enableActivityInstances： " +
	// "激活活动实例转命令已发送，activity：" + activity);
	// } catch (Exception e) {
	// retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, false,
	// "激活活动实例异常, 异常信息：" + e
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.error("激活活动实例发送异常, 异常信息：" + e);
	// }
	// return retDto;
	// }

	/**
	 * 提交工作项
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto completeWorkItem(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;

		CompleteWorkItemDto completeWorkItemDto = (CompleteWorkItemDto) commandDto;

		String workItemId = completeWorkItemDto.getWorkitemId();
		String memo = completeWorkItemDto.getMemo();
		String areaId = completeWorkItemDto.getAreaCode();
		Map<String, String> flowParamMap = completeWorkItemDto.getFlowParamMap();
		Map<String, String> flowPassMap = completeWorkItemDto.getFlowPassMap();
		try {
//			boolean canComplete = fastflowRunner.canCompleteWorkItem(workItemId, false);
//			if(canComplete){
				fastflowRunner.completeWorkItem(workItemId, flowParamMap, memo, areaId,
						flowPassMap, false);
				retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
						true, "提交工作项执行成功", workItemId);
//			}else{
//				retDto = CommandProxy.getInstance().dealCommand(completeWorkItemDto);
//			}
			
			logger.debug("=============>>>completeWorkItem： "
					+ "提交工作项命令已发送，workItemId：" + workItemId);
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "提交工作项执行失败", null);
			logger.error("提交工作项执行异常, 异常信息：", e);
			throw e;
		}

		return retDto;
	}

	/**
	 * 回滚流程实例
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto rollbackProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;

		RollbackProcessInstanceDto rollbackProcessInstanceDto = (RollbackProcessInstanceDto) commandDto;

		String processInstanceId = rollbackProcessInstanceDto.getProcessInstanceId();
		String areaId = rollbackProcessInstanceDto.getAreaCode();
		Map<String, String> flowPassMap = rollbackProcessInstanceDto.getFlowPassList();
		String startMode = rollbackProcessInstanceDto.getStartMode();
		try {
			fastflowRunner.rollbackProcessInstance(
					processInstanceId.toString(), null, startMode,
					areaId,flowPassMap,false);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "撤单成功", processInstanceId);
			logger.debug("==========================>>>rollbackProcessInstance： "
					+ "撤单成功，processInstanceId：" + processInstanceId);
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "撤单异常", processInstanceId);
			logger.error("撤单异常, 异常信息：" + e);
			throw e;
		}
		return retDto;
	}

	/**
	 * 流程跳转
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto processInstanceJump(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;

		ProcessInstanceJumpDto processInstanceJumpDto = (ProcessInstanceJumpDto) commandDto;
 
		String processInstanceId = processInstanceJumpDto
				.getProcessInstanceId().toString();
		String fromActivityInstanceId = processInstanceJumpDto
				.getFromActivityInstanceId();
		String toActivityId = processInstanceJumpDto.getToActivityId();
		String areaId = processInstanceJumpDto.getAreaCode();
		logger.info("---processInstanceId:" + processInstanceId
				+ "--fromActivityInstanceId:" + fromActivityInstanceId
				+ "--toActivityId:" + toActivityId);
		try {
			fastflowRunner.processInstanceJump(processInstanceId,
					fromActivityInstanceId, toActivityId, areaId,false,processInstanceJumpDto.getFlowPassList());

			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "流程跳转成功", processInstanceId);
			logger.debug("==========================>>>processInstanceJump： "
					+ "流程跳转命令已发送，processInstanceId：" + processInstanceId);
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "流程跳转异常", processInstanceId);
			throw e;
		}
		return retDto;
	}

	/**
	 * 废弃流程实例
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto abortProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		AbortProcessInstanceDto abortProcessInstanceDto = (AbortProcessInstanceDto) commandDto;

		String processInstanceId = abortProcessInstanceDto
				.getProcessInstanceId().toString();
		String areaId = abortProcessInstanceDto.getAreaCode();
		try {
			fastflowRunner.abortProcessInstance(processInstanceId, areaId,false);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "废弃流程实例命令发送成功",
					abortProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>abortProcessInstance： "
					+ "废弃流程实例命令已发送，processInstanceId：" + processInstanceId);
		} catch (Exception e) {
			logger.error("废弃流程实例异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "废弃流程实例命令发送异常",
					abortProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * 终止流程实例
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto terminateProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		TerminateProcessInstanceDto terminateProcessInstanceDto = (TerminateProcessInstanceDto) commandDto;

		String processInstanceId = terminateProcessInstanceDto
				.getProcessInstanceId().toString();
		String areaId = terminateProcessInstanceDto.getAreaCode();
		try {
			fastflowRunner.terminateProcessInstance(processInstanceId, areaId,false);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "终止流程实例命令发送成功",
					terminateProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>terminateProcessInstance： "
					+ "终止流程实例命令已发送，processInstanceId：" + processInstanceId);
		} catch (Exception e) {
			logger.error("终止流程实例异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "终止流程实例命令发送异常",
					terminateProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * 挂起流程实例
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto suspendProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		SuspendProcessInstanceDto suspendProcessInstanceDto = (SuspendProcessInstanceDto) commandDto;

		String processInstanceId = suspendProcessInstanceDto
				.getProcessInstanceId().toString();
		String areaId = suspendProcessInstanceDto.getAreaCode();
		Map<String,String> flowPassList = suspendProcessInstanceDto.getFlowPassList();
		try {
			fastflowRunner.suspendProcessInstance(processInstanceId, areaId,false,flowPassList);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "挂起流程实例命令发送成功",
					suspendProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>suspendProcessInstance： "
					+ "挂起流程实例命令已发送，processInstanceId：" + processInstanceId);
		} catch (Exception e) {
			logger.error("挂起流程实例异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "挂起流程实例命令发送异常",
					suspendProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * 恢复流程实例
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto resumeProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		ResumeProcessInstanceDto resumeProcessInstanceDto = (ResumeProcessInstanceDto) commandDto;

		String processInstanceId = resumeProcessInstanceDto
				.getProcessInstanceId().toString();
		String areaId = resumeProcessInstanceDto.getAreaCode();
		Map<String,String> flowPassList = resumeProcessInstanceDto.getFlowPassList();
		try {
			fastflowRunner.resumeProcessInstance(processInstanceId,
					Boolean.valueOf(true), areaId,false,flowPassList);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "恢复流程实例命令发送成功",
					resumeProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>resumeProcessInstance： "
					+ "恢复流程实例命令已发送，processInstanceId：" + processInstanceId);
		} catch (Exception e) {
			logger.error("恢复流程实例异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "恢复流程实例命令发送异常",
					resumeProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * 重启流程实例
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto restartProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		RestartProcessInstanceDto restartProcessInstanceDto = (RestartProcessInstanceDto) commandDto;

		String processInstanceId = restartProcessInstanceDto
				.getProcessInstanceId().toString();
		String areaId = restartProcessInstanceDto.getAreaCode();
		try {
			fastflowRunner.restartProcessInstance(processInstanceId, areaId,false);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "重启流程实例命令发送成功",
					restartProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>restartProcessInstance： "
					+ "重启流程实例命令已发送，processInstanceId：" + processInstanceId);
		} catch (Exception e) {
			logger.error("重启流程实例异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "重启流程实例命令发送异常",
					restartProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * 流程回退（退单）
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto disableWorkItem(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = new CommandResultDto();
		DisableWorkItemDto disableWorkItemDto = (DisableWorkItemDto) commandDto;
		String workItemId = disableWorkItemDto.getWorkitemId();
		String reasonConfigId = disableWorkItemDto.getReasonCfgId();
		String memo = disableWorkItemDto.getMemo();
		String areaId = disableWorkItemDto.getAreaCode();
		String reasonCode = disableWorkItemDto.getReasonCode();
		Map<String,String> flowPassList = disableWorkItemDto.getFlowPassList();
		String targetWorkItemId = disableWorkItemDto.getTargetWorkItemId();
		String reasonType = disableWorkItemDto.getReasonType();
		try {
			logger.info("--reasonConfigId：" + reasonConfigId+",workItemId:"+workItemId+",reasonCode:"+reasonCode);
			if(!StringHelper.isEmpty(targetWorkItemId)){
				if(FastflowConfig.rollbackBySingle){
					fastflowRunner.disableWorkItemByTarget(workItemId, targetWorkItemId,reasonType, memo, areaId, false, flowPassList);
				}else{
					List<WorkItemDto> col = fastflowRunner.findCanRollBackWorkItemsByTarget(disableWorkItemDto.getProcessInstanceId(),disableWorkItemDto.getWorkitemId(),targetWorkItemId,reasonType,false,areaId);
					fastflowRunner.disableWorkItemByTarget(workItemId, targetWorkItemId,reasonType, memo, areaId, false, flowPassList);

					//并行环节退单，将并行其他未完成工作项也作废
					Iterator<WorkItemDto> iterator = col.iterator();
					while (iterator.hasNext()) {
						WorkItemDto workItem = iterator.next();
						if(!workItemId.equals(workItem.getWorkItemId().toString())){
							fastflowRunner.disableWorkItemByTarget(workItem.getWorkItemId().toString(), targetWorkItemId,reasonType, memo, areaId, false, flowPassList);
						}
					}
				}
			}else{
				if(FastflowConfig.rollbackBySingle){
					fastflowRunner.disableWorkItem(workItemId,reasonCode, reasonConfigId, memo, areaId,false,flowPassList);
				}else{
					List<WorkItemDto> col = fastflowRunner.findCanRollBackWorkItems(disableWorkItemDto.getProcessInstanceId(),disableWorkItemDto.getWorkitemId(),reasonCode,reasonConfigId,false,areaId);
					fastflowRunner.disableWorkItem(workItemId,reasonCode, reasonConfigId, memo, areaId,false,flowPassList);

					//并行环节退单，将并行其他未完成工作项也作废
					Iterator<WorkItemDto> iterator = col.iterator();
					while (iterator.hasNext()) {
						WorkItemDto workItem = iterator.next();
						if(!workItemId.equals(workItem.getWorkItemId().toString())){
							fastflowRunner.disableWorkItem(workItem.getWorkItemId().toString(), reasonCode,
										reasonConfigId, memo, areaId, false, flowPassList);
						}
					}
				}
			}
			
			//end
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "流程引擎退单命令发送成功",
					disableWorkItemDto.getProcessInstanceId());
			logger.debug("=====>>>disableWorkItem： "
					+ "流程引擎退单命令已发送，processInstanceId："
					+ disableWorkItemDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("----流程引擎退单失败：" + e.getMessage(), e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "流程引擎退单命令发送异常",
					disableWorkItemDto.getProcessInstanceId());
			throw e;
		}finally{
			retDto.setFlowPassList(flowPassList);
		}
		return retDto;
	}

	/**
	 * 异步持久化内存模型
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto persistProcessModel(CommandDto commandDto)
			throws Exception {
		logger.info("-------异步持久化内存模型------commandDto:"+GsonHelper.toJson(commandDto));
		CommandResultDto retDto = null;
		PersistProcessModelDto persistProcessModelDto = (PersistProcessModelDto) commandDto;
		ProcessModel processModel = persistProcessModelDto.getProcessModel();
		try {
			//将sql保存到线程变量标志设置为true
			SqlLocalHolder.setHoldSqlOn(true);
			fastflowRunner.persistProcessModel(processModel);
			if (processModel.getProcessInstanceDto() != null
					&& processModel.getProcessInstanceDto().getState() == WMProcessInstanceState.CLOSED_COMPLETED_INT) {
				CommandInvokeUtil
						.clearCacheForDone(Long.valueOf(persistProcessModelDto
								.getProcessInstanceId()));
			}
			
			//从线程变量获取持久化对象
			List<AsynSqlExecBy3thParamDto> sqlParams = SqlLocalHolder.get();
			for(AsynSqlExecBy3thParamDto sqlParam : sqlParams){
				String sql = sqlParam.getSqlStr();
				String key = sqlParam.getKey();
				String tableName = sqlParam.getTableName();
				int sqlType = sqlParam.getSqlType();
				int sqlSeq = sqlParam.getSqlSeq();
				
				Object[] paramsList = sqlParam.getParam();
				
				AsyncSqlDto asyncSqlDto = null;
				//过滤状态不敏感持久化使用AsyncSqlDto
				if(ArrayUtils.contains(INST_TABLES, tableName)){
					asyncSqlDto = new AsyncSqlDto4Inst();
				}else{
					asyncSqlDto = new AsyncSqlDto();
				}
				asyncSqlDto.setSqlStr(sql);
				asyncSqlDto.setKey(key);
				asyncSqlDto.setParams(paramsList);
				asyncSqlDto.setTableName(tableName);
				asyncSqlDto.setSqlSeq(sqlSeq);
				if(DubboAsynSqlExecBy3thDto.INSERT == sqlType){
					asyncSqlDto.setSqlType(1);
				}else if(DubboAsynSqlExecBy3thDto.UPDATE == sqlType){
					asyncSqlDto.setSqlType(2);
				}
				if(logger.isInfoEnabled()){
					logger.info("流程平台传入持久化对象："+ GsonHelper.toJson(sqlParam));
				}
				sqlPersistQueueUtils.putToQueue(asyncSqlDto);
			}
			
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "异步持久化内存模型成功", commandDto.getProcessInstanceId());
			logger.info("-------异步持久化内存模型成功------"+GsonHelper.toJson(retDto));
		} catch (Exception e) {
			logger.error("异步持久化内存模型异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "异步持久化内存模型异常！", "-1");
			throw e;
		}finally{
			//清除线程sql线程变量（重置sqlLocalHolder线程变量）
			SqlLocalHolder.clear();
		}
		return retDto;
	}
	
	/**
	 * 
	 * 调用第三方机制进行远程异步持久化
	 * @param commandDto
	 * @return
	 * @throws Exception   
	 * @author bobping
	 * @date 2017年4月6日
	 */
	public CommandResultDto persistProcessModelRemote(CommandDto commandDto)
			throws Exception {
		logger.info("-------异步持久化内存模型------commandDto:"+GsonHelper.toJson(commandDto));
		CommandResultDto retDto = null;
		PersistProcessModelRemoteDto persistProcessModelRemoteDto = (PersistProcessModelRemoteDto) commandDto;
		ProcessModel processModel = persistProcessModelRemoteDto.getProcessModel();
		try {
			//将sql保存到线程变量标志设置为true
			SqlLocalHolder.setHoldSqlOn(true);
			
			fastflowRunner.persistProcessModel(processModel);
			if (processModel.getProcessInstanceDto() != null
					&& processModel.getProcessInstanceDto().getState() == WMProcessInstanceState.CLOSED_COMPLETED_INT) {
				CommandInvokeUtil
				.clearCacheForDone(Long.valueOf(persistProcessModelRemoteDto
						.getProcessInstanceId()));
			}
			
			//从线程变量获取持久化对象
//			long t1 = System.currentTimeMillis();
			List<AsynSqlExecBy3thParamDto> sqlParams = SqlLocalHolder.get();
			ExecuteSQLAsynBy3thDto executeSQLAsynBy3thDto = new ExecuteSQLAsynBy3thDto();
			executeSQLAsynBy3thDto.setParam(sqlParams);
			PersistBy3thClientProxy.getInstance().executeSQLAsynBy3th(executeSQLAsynBy3thDto);
			
//			logger.error("调用远程持久化dubbo接口耗时ms："+(System.currentTimeMillis() - t1));
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "调用第三方机制异步持久化内存模型成功", commandDto.getProcessInstanceId());
			
			logger.info("-------调用第三方机制异步持久化内存模型成功------"+GsonHelper.toJson(retDto));
		} catch (Exception e) {
			logger.error("调用第三方机制异步持久化内存模型成功异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "调用第三方机制异步持久化内存模型成功异常！", "-1");
			throw e;
		} finally{
			//清除线程sql线程变量（重置sqlLocalHolder线程变量）
			SqlLocalHolder.clear();
		}
		return retDto;
	}
	
	/**
	 * 存储流程异常
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto saveException(CommandDto commandDto)throws Exception {
		CommandResultDto retDto = null;
		SaveExceptionDto saveExceptionDto = (SaveExceptionDto) commandDto;
		try {
			fastflowRunner.saveException(saveExceptionDto.getExceptionDto(),saveExceptionDto.getOperType());
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "存储流程异常成功", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("存储流程异常异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "存储流程异常异常！", "-1");
			throw e;
		}
		return retDto;
	}
	/**
	 * 环节新增
	 * @param commandDto
	 * @return
	 */
	public CommandResultDto addTache(CommandDto commandDto){
		CommandResultDto retDto = null;
		try {
			AddTacheDto addTacheDto = (AddTacheDto) commandDto;
			TacheDto dto = new TacheDto();
			dto.setTacheCode(addTacheDto.getTacheCode());
			dto.setTacheName(addTacheDto.getTacheName());
			dto.setTacheCatalogId(LongHelper.valueOf(addTacheDto.getTacheCatalogId()));
			dto.setState("10A");
			dto.setIsAuto(0);
			dto.setTacheType(addTacheDto.getTacheType());// 存在""的情况，用StringHelper.valueOf会变成null
			dto.setPackageDefineCodes(addTacheDto.getPackageDefineCodes());
			dto.setCreateDate(DateHelper.getTimeStamp());
			dto.setStateDate(DateHelper.getTimeStamp());
			dto.setEffDate(new Timestamp(addTacheDto.getEffDate().getTime()));
			dto.setExpDate(new Timestamp(addTacheDto.getExpDate().getTime()));
			tacheService.addTache(dto);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "环节添加成功！", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "环节添加失败："+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("环节添加失败："+e.getMessage(),e);
		}
		return retDto;
	}
	

	/**
	 * 环节修改
	 * @param commandDto
	 * @return
	 */
	public CommandResultDto modTache(CommandDto commandDto){
		CommandResultDto retDto = null;
		try {
			ModTacheDto modTacheDto = (ModTacheDto)commandDto;
			TacheDto dto = new TacheDto();
			dto.setId(LongHelper.valueOf(modTacheDto.getTacheId()));
			dto.setTacheName(modTacheDto.getTacheName());
			dto.setTacheCatalogId(LongHelper.valueOf(modTacheDto.getTacheCatalogId()));
			dto.setIsAuto(0);
			dto.setPackageDefineCodes(modTacheDto.getPackageDefineCodes());
			dto.setStateDate(DateHelper.getTimeStamp());
			dto.setEffDate(new Timestamp(modTacheDto.getEffDate().getTime()));
			dto.setExpDate(new Timestamp(modTacheDto.getExpDate().getTime()));
			tacheService.modTache(dto);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "环节修改成功！", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "环节修改异常："+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("环节修改失败："+e.getMessage(),e);
		}
		return retDto;
	}
	/**
	 * 环节删除
	 * @param commandDto
	 * @return
	 */
	public CommandResultDto delTache(CommandDto commandDto){
		CommandResultDto retDto = null;
		try {
			DelTacheDto delTacheDto = (DelTacheDto) commandDto;
			TacheDto dto = new TacheDto();
			dto.setId(LongHelper.valueOf(delTacheDto.getTacheId()));
			dto.setStateDate(DateHelper.getTimeStamp());
			tacheService.delTache(dto);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "环节删除成功", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "环节删除异常："+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("环节删除失败："+e.getMessage(),e);
		}
		return retDto;
	}

	/**
	 * 异常原因新增
	 * @param params
	 * @return
	 */
	public CommandResultDto addReturnReason(CommandDto commandDto){
		CommandResultDto retDto = null;
		try {
			AddReturnReasonDto addReturnReasonDto = (AddReturnReasonDto) commandDto;
			ReturnReasonDto dto = new ReturnReasonDto();
			dto.setReasonCode(addReturnReasonDto.getReasonCode());
			dto.setReasonCatalogId(LongHelper.valueOf(addReturnReasonDto.getReasonCatalogId()));
			dto.setReasonType(addReturnReasonDto.getReasonType());
			dto.setReturnReasonName(addReturnReasonDto.getReasonName());
			dto.setComments(addReturnReasonDto.getComment());
			dto.setRecommendMeans(addReturnReasonDto.getRecommandMeans());
			Timestamp curTime = DateHelper.getTimeStamp();
			dto.setCreateDate(curTime);
			dto.setStateDate(curTime);
			returnReasonService.addReturnReason(dto);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "异常原因添加成功", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "异常原因添加失败："+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("异常原因添加失败："+e.getMessage(),e);
		}
		return retDto;
	}

	/**
	 * 异常原因修改
	 * @param params
	 * @return
	 */
	public CommandResultDto modReturnReason(CommandDto commandDto){
		CommandResultDto retDto = null;
		try {
			ModReturnReasonDto modReturnReasonDto = (ModReturnReasonDto)commandDto;
			ReturnReasonDto dto = new ReturnReasonDto();
			dto.setReasonCatalogId(LongHelper.valueOf(modReturnReasonDto.getReasonCatalogId()));
			dto.setReturnReasonName(StringHelper.valueOf(modReturnReasonDto.getReasonName()));
			dto.setStateDate(DateHelper.getTimeStamp());
			dto.setId(LongHelper.valueOf(modReturnReasonDto.getReasonId()));
			returnReasonService.modReturnReason(dto);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "异常原因修改成功", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "异常原因修改失败："+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("异常原因修改失败："+e.getMessage(),e);
		}
		return retDto;
	}
	/**
	 * 异常原因删除
	 * @param params
	 * @return
	 */
	public CommandResultDto delReturnReason(CommandDto commandDto){
		CommandResultDto retDto = null;
		try {
			DelReturnReasonDto delReturnReasonDto = (DelReturnReasonDto)commandDto;
			ReturnReasonDto dto = new ReturnReasonDto();
			dto.setId(LongHelper.valueOf(delReturnReasonDto.getReasonId()));
			returnReasonService.delReturnReason(dto);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "异常原因删除成功", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "异常原因删除失败："+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("异常原因删除失败："+e.getMessage(),e);
		}
		return retDto;
	}

	/**
	 * 修改流程实例状态
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto updateProcessInstance(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		UpdateProcessInstanceDto updateProcessInstanceDto = (UpdateProcessInstanceDto) commandDto;

		String processInstanceId = updateProcessInstanceDto
				.getProcessInstanceId().toString();
		String areaId = updateProcessInstanceDto.getAreaCode();
		String state = updateProcessInstanceDto.getState();
		try {
			fastflowRunner.updateProcessInstance(processInstanceId, areaId,state,false);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "修改流程实例命令发送成功",
					updateProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>updateProcessInstanceDto： "
					+ "修改流程实例命令已发送，processInstanceId：" + processInstanceId);
		} catch (Exception e) {
			logger.error("修改流程实例异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "修改流程实例命令发送异常",
					updateProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}
	
	/**
	 * 存储接口消息
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto saveCommandQueue(CommandDto commandDto)throws Exception {
		CommandResultDto retDto = null;
		SaveCommandQueueDto saveCommandQueueDto = (SaveCommandQueueDto) commandDto;
		try {
			fastflowRunner.saveCommandQueue(saveCommandQueueDto.getCommandQueueDto());
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "存储接口消息成功", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("存储接口消息异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "存储接口消息异常！", "-1");
			throw e;
		}
		return retDto;
	}
	
	/**
	 * 历史数据转存
	 * @param commandDto
	 * @return
	 */
	public CommandResultDto dataToHis(CommandDto commandDto){
		CommandResultDto retDto = null;
		DataToHisDto dataToHisDto = (DataToHisDto) commandDto;
		try {
			fastflowRunner.dataToHis(dataToHisDto.getProcessInstanceId());
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, true, "历史数据转存成功", dataToHisDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("历史数据转存接口消息异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, false, "历史数据转存失败", dataToHisDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}
	/**
	 * 挂起工作项
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto suspendWorkItem(CommandDto commandDto)
			throws Exception {
		CommandResultDto retDto = null;
		SuspendWorkItemDto suspendWorkItemDto = (SuspendWorkItemDto) commandDto;

		String areaId = suspendWorkItemDto.getAreaCode();
		Map<String,String> flowPassList = suspendWorkItemDto.getFlowPassList();
		String workItemId = suspendWorkItemDto.getWorkItemId();
		try {
			fastflowRunner.suspendWorkItem(workItemId, flowPassList, areaId, false);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "挂起工作项命令发送成功",
					suspendWorkItemDto.getProcessInstanceId());
			logger.debug("=====>>>suspendProcessInstance： "
					+ "挂起工作项命令已发送，processInstanceId：" + suspendWorkItemDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("挂起工作项异常, 异常信息：" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "挂起工作项命令发送异常",
					suspendWorkItemDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}
}
