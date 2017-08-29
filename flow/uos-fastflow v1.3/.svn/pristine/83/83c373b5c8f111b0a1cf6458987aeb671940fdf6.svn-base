package com.zterc.uos.fastflow.core;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.constant.CommonDomain;
import com.zterc.uos.fastflow.core.assist.ActivityContext;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.zterc.uos.fastflow.dto.process.CreateWorkOrderParamDto;
import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.process.ProcessDefinitionDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.dto.specification.FlowLimitDto;
import com.zterc.uos.fastflow.dto.specification.PackageDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonConfigDto;
import com.zterc.uos.fastflow.dto.specification.TacheDto;
import com.zterc.uos.fastflow.dto.specification.TacheLimitDto;
import com.zterc.uos.fastflow.exception.FastflowException;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.zterc.uos.fastflow.inf.WorkflowStateReport;
import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.model.Transition;
import com.zterc.uos.fastflow.model.WorkflowProcess;
import com.zterc.uos.fastflow.model.condition.Condition;
import com.zterc.uos.fastflow.model.condition.Xpression;
import com.zterc.uos.fastflow.parse.CalendarUtil;
import com.zterc.uos.fastflow.parse.JoinType;
import com.zterc.uos.fastflow.service.ActivityInstanceService;
import com.zterc.uos.fastflow.service.AreaService;
import com.zterc.uos.fastflow.service.CommandQueueService;
import com.zterc.uos.fastflow.service.DataToHisService;
import com.zterc.uos.fastflow.service.ExceptionService;
import com.zterc.uos.fastflow.service.FlowLimitService;
import com.zterc.uos.fastflow.service.ProcessAttrService;
import com.zterc.uos.fastflow.service.ProcessDefinitionService;
import com.zterc.uos.fastflow.service.ProcessInstanceService;
import com.zterc.uos.fastflow.service.ProcessPackageService;
import com.zterc.uos.fastflow.service.ProcessParamDefService;
import com.zterc.uos.fastflow.service.ProcessParamService;
import com.zterc.uos.fastflow.service.ReturnReasonService;
import com.zterc.uos.fastflow.service.TacheLimitService;
import com.zterc.uos.fastflow.service.TacheService;
import com.zterc.uos.fastflow.service.TransitionInstanceService;
import com.zterc.uos.fastflow.service.WorkItemService;
import com.zterc.uos.fastflow.state.WMActivityInstanceState;
import com.zterc.uos.fastflow.state.WMAutomationMode;
import com.zterc.uos.fastflow.state.WMProcessInstanceState;
import com.zterc.uos.fastflow.state.WMTransitionInstanceAction;
import com.zterc.uos.fastflow.state.WMTransitionInstanceState;
import com.zterc.uos.fastflow.state.WMWorkItemState;

public class FastflowRunner {
	private static final String LIMIT_DATE = "LIMIT_DATE";
	private static final String ALERT_DATE = "ALERT_DATE";

	private Logger logger = LoggerFactory.getLogger(FastflowRunner.class);

	private ProcessDefinitionService processDefinitionService;
	private ActivityInstanceService activityInstanceService;
	private ProcessInstanceService processInstanceService;
	private ProcessParamService processParamService;
	private ProcessAttrService processAttrService;
	private WorkflowStateReport workflowStateReport;
	private TransitionInstanceService transitionInstanceService;
	private WorkItemService workItemService;
	private TacheService tacheService;
	private ReturnReasonService returnReasonService;
	private ProcessParamDefService processParamDefService;
	private ExceptionService exceptionService;
	private ProcessPackageService processPackageService;
	private FlowLimitService flowLimitService; 
	private TimeLimitClient timeLimitClient;
	private TacheLimitService tacheLimitService;
	@Autowired
	private CommandQueueService commandQueueService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private DataToHisService dataToHisService;

	/**
	 * 创建流程实例
	 * 
	 * @param processDefinitionCode
	 *            流程定义编码
	 * @param parentActivityInstanceId
	 *            父环节实例id
	 * @param processInstanceName
	 *            流程实例名称
	 * @param priority
	 *            流程的优先级（暂无处理）
	 * @param processParam
	 *            流程输入参数
	 * @param areaId
	 *            区域id
	 * @return ProcessInstanceDto 流程实例Dto
	 * @throws FastflowException
	 */
	public ProcessInstanceDto createProcessInstanceByCode(
			String processDefinitionCode, String parentActivityInstanceId,
			String processInstanceName, int priority,
			Map<String, String> processParam, String areaId, boolean useDB)
			throws FastflowException {
		try {
			if (processDefinitionCode == null
					|| processDefinitionCode.trim().length() == 0) {
				throw new FastflowException("流程定义编码为空！");
			}
			ProcessDefinitionDto dto = processDefinitionService
					.queryProcessDefinitionByCode(processDefinitionCode);
			if(dto == null || dto.getPackageDefineId() == null) {
				throw new FastflowException("根据流程模板编码『" + processDefinitionCode + "』查询不到相应的流程模板！");
			}
			return createProcessInstance(
					StringHelper.valueOf(dto.getPackageDefineId()),
					parentActivityInstanceId, processInstanceName, priority,
					processParam, areaId, useDB);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}

	/**
	 * 创建流程实例
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @param parentActivityInstanceId
	 *            父环节实例id
	 * @param processInstanceName
	 *            流程实例名称
	 * @param priority
	 *            流程的优先级（暂无处理）
	 * @param processParam
	 *            流程输入参数
	 * @param areaId
	 *            区域id
	 * @return ProcessInstanceDto 流程实例Dto
	 * @throws FastflowException
	 */
	public ProcessInstanceDto createProcessInstance(String processDefinitionId,
			String parentActivityInstanceId, String processInstanceName,
			int priority, Map<String, String> processParam, String areaId,
			boolean useDB) throws FastflowException {
		ProcessInstanceDto processInstance = null;
		try {
			if (processDefinitionId == null
					|| processDefinitionId.trim().length() == 0) {
				throw new FastflowException("流程定义id为空！");
			}

			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processDefinitionId);

			/** 检查流程定义状态 */
			/** 检测流程定义的状态 只有在RELEASED_ACTIVE才可以创建流程 */
			if (!process.getState().equalsIgnoreCase(
					ProcessDefinitionDto.STATE_ACTIVE)) {
				throw new FastflowException("流程定义处于不可用的状态！当前状态："
						+ process.getState());
			}
			/**add by che.zi 20160628 for zmp:889946 begin 
			 * 检测流程定义的有效期 只有在有效时间内才可以创建流程 */
			ProcessDefinitionDto dto = processDefinitionService.queryProcessDefinitionById(processDefinitionId);
			if (dto != null) {
				PackageDto packageDto = processPackageService.qryPackageById(dto.getPackageId());
				if(DateHelper.getTimeStamp().after(packageDto.getExpDate())){
					throw new FastflowException("流程版本已过期，请更新流程版本时间！");
				}
			}
			//add by che.zi 20160628 for zmp:889946 end
			/** 创建流程实例 */
			if (processInstanceName == null
					|| processInstanceName.trim().length() == 0) {
				processInstanceName = process.getName();
			}
			/** 设置流程数据 */
			processInstance = new ProcessInstanceDto();
			processInstance.setCreatedDate(DateHelper.getTimeStamp());
			processInstance.setName(processInstanceName);
			processInstance.setProcessDefineId(processDefinitionId);
			//modify by bobping 流程模板id改成流程模板编码获取异常原因配置
			processInstance.setProcessDefineCode(dto.getPackageDefineCode());
			processInstance.setParentActivityInstanceId(LongHelper
					.valueOf(parentActivityInstanceId));
			processInstance.setParticipantId(-1l);
			processInstance.setParticipantPositionId(-1l);
			processInstance.setProcessDefinitionName(process.getName());
			processInstance
					.setState(WMProcessInstanceState.OPEN_NOTRUNNING_NOTSTARTED
							.intValue());
			processInstance.setPriority(priority);
			processInstance.setAreaId((areaId == null || areaId.equals("")) ? "1":areaId);// add by che.zi 增加区域作队列分区
			/** 如果是子流程,... */
			if (parentActivityInstanceId != null) {
				ActivityInstanceDto parentActivityInstance = activityInstanceService
						.queryActivityInstance(parentActivityInstanceId);
				ProcessInstanceDto parentProcessInstance = processInstanceService
						.queryProcessInstance(String
								.valueOf(parentActivityInstance
										.getProcessInstanceId()));
				processInstance.setOldProcessInstanceId(parentProcessInstance
						.getOldProcessInstanceId());
			}

			/** 将流程数据以及流程参数写入到数据库 */
			processInstance = processInstanceService.createProcessInstance(
					processInstance, useDB);


			// 增加开关，是否实时获取xpdl中环节下一个线条的流程参数回传  modify by che.zi 20170729
			if(FastflowConfig.getTacheParamImm){
				logger.info("-----进入根据xpdl取线条上的流程参数---");
				processParam = processParamDefService.qryProInsTacheParam(process.getStartActivity());
				processParamService.initProcessParam(
						processInstance.getProcessInstanceId(),
						processDefinitionId, processParam, false);
			}else{
				processParam = processParamService.initProcessParam(
						processInstance.getProcessInstanceId(),
						processDefinitionId, processParam, false);
			}
			//end 20170729
			processInstance.setFlowParamMap(processParam);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
		return processInstance;
	}

	/**
	 * 同步启动工作流实例
	 * 
	 * @param processInstanceId
	 *            工作流实例
	 * @param flowParamMap 
	 * @throws FastflowException
	 */
	public void startProcessInstance(String processInstanceId, String areaId,Map<String,String> flowPassMap,
			boolean useDB, Map<String, String> flowParamMap) throws FastflowException {
		try {
			/** 查询流程实例数据 */
			ProcessInstanceDto processInstance = processInstanceService
					.queryProcessInstance(processInstanceId);
			if (processInstance == null) {
				throw new FastflowException("流程实例不存在[processInstanceId="
						+ processInstanceId + "]");
			}
			/** 判断流程实例状态决定能否启动,通过WMProcessInstanceState中的动作矩阵判断 */
			WMProcessInstanceState.fromInt(processInstance.getState())
					.checkTransition(WMProcessInstanceState.OPEN_RUNNING, true);
			/** 修改流程实例状态为执行中，并设置启动日期 */
			processInstance.setState(WMProcessInstanceState.OPEN_RUNNING
					.intValue());
			processInstance.setStartedDate(DateHelper.getTimeStamp());
			
			//add by che.zi 20160720
			//增加流程时限
			if(FastflowConfig.useTimeLimit){ //判断是否使用流程时限
				FlowLimitDto flowLimitDto = flowLimitService.qryFlowLimit(processInstance.getProcessDefineId(), areaId);
				logger.info("-----查询流程时限规则参数，processDefineId："+processInstance.getProcessDefineId()+"--areaId:"+areaId);
				logger.info("-----查询到的流程时限规则flowLimitDto："+GsonHelper.toJson(flowLimitDto));
				if(flowLimitDto != null){
					Date limitDate = null;
					Date alertDate = null;
					Date startDate = processInstance.getStartedDate();
				    if (flowLimitDto.getIsWorkTime().equals(CommonDomain.IS_CALCULATE_WORKTIME_YES)){
				    	/** 对告警日期、完成日期考虑节假日 */
				    	alertDate = timeLimitClient.calculateWorkTime(startDate,flowLimitDto.getAlertValue().intValue(), flowLimitDto.getTimeUnit(),areaId, processInstance.getProcessDefineId());
				    	logger.info("---计算出来的alertDate(考虑节假日):"+alertDate);
				    	limitDate = timeLimitClient.calculateWorkTime(startDate,flowLimitDto.getLimitValue().intValue(),flowLimitDto.getTimeUnit(),areaId,processInstance.getProcessDefineId());
				    	logger.info("---计算出来的limitDate(考虑节假日):"+limitDate);
				    }else{
				    	/** 不考虑节假日 */
                        alertDate = timeLimitClient.getAbsDateByTimeUnit(startDate,flowLimitDto.getTimeUnit(),flowLimitDto.getAlertValue().intValue());
                        logger.info("---计算出来的alertDate(不考虑节假日):"+alertDate);
				    	limitDate = timeLimitClient.getAbsDateByTimeUnit(startDate,flowLimitDto.getTimeUnit(),flowLimitDto.getLimitValue().intValue());
				    	logger.info("---计算出来的limitDate:"+limitDate);
				    }
				    if(limitDate !=null){
				    	 processInstance.setLimitDate(new Timestamp(limitDate.getTime()));
				    }
				    if(alertDate != null){
				    	 processInstance.setAlertDate(new Timestamp(alertDate.getTime()));
				    }
				    //将流程时限结果通知业务侧
				    Map<String,Object> map = new HashMap<String,Object>();
				    map.put("processInstanceId", processInstanceId);
				    map.put("areaId", areaId);
				    map.put("limitDate", DateHelper.parseTime(limitDate));
				    map.put("alertDate", DateHelper.parseTime(alertDate));
				    workflowStateReport.reportTimeLimit(map);
				}else{
					logger.error("----未配置流程时限规则,流程模板id:"+processInstance.getProcessDefineId()
							+",areaId:"+areaId);
				}
			}
			//end
			
			processInstanceService
					.updateProcessInstance(processInstance, useDB);

			/** 取出WorkflowProcess对象 */
			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processInstance
							.getProcessDefineId());
			/** 找到开始节点，并创建开始节点活动实例 */
			Activity startActivity = process.getStartActivity();

			ActivityInstanceDto startActivityInstance = activityInstanceService
					.createActivityInstance(startActivity, processInstance,
							ActivityInstanceDto.NORMAL_DIRECTION, useDB);
// 去掉流程启动的通知
//			/** 如果不是子流程，流程启动成功通知 */
//			if (processInstance.getParentActivityInstanceId() == null
//					|| processInstance.getParentActivityInstanceId().intValue() == 0) {
//				flowPassMap = resetFlowPassMap(
//						processInstance.getProcessInstanceId(), flowPassMap);
//				workflowStateReport.reportProcessState(
//						Long.valueOf(processInstanceId), "",
//						CommonDomain.WM_START_REPORT, flowPassMap);
//			}
			//如果入惨流程参数不为空
			if (flowParamMap != null) {
				/** 设置流程全局变量 */
				processParamService.setProcessParam(
						LongHelper.valueOf(processInstanceId),
						flowParamMap, false);
			}
			executeEfferentTransitions(startActivity, startActivityInstance,
					processInstance, areaId, flowPassMap, useDB);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}

	/**
	 * 提交工作项
	 * 
	 * @param workItemId
	 * @param flowParamMap
	 * @param memo
	 * @param areaId
	 * @param flowPassMap
	 * @param useDB
	 * @throws FastflowException
	 */
	public void completeWorkItem(String workItemId,
			Map<String, String> flowParamMap, String memo, String areaId,
			Map<String, String> flowPassMap,boolean useDB)// map为传入的多个参数，执行人等。页面和外系统
			throws FastflowException {
		logger.info("---进入提交工作项，工作项id:"+workItemId);
		String processInstanceId = null;
		WorkItemDto workItem = null;
		try {
			// 工作项，可以放到Redis缓存中。
			workItem = workItemService.queryWorkItem(workItemId);
			processInstanceId = StringHelper.valueOf(workItem.getProcessInstanceId());	
			//如果工作项是已完成状态，不做任何处理直接返回 mod by che.zi 20160715
			if(WMWorkItemState.CLOSED_COMPLETED_INT == workItem.getState() 
					|| WMWorkItemState.DISABLED_INT == workItem.getState()){
				logger.error("---当前工作项"+workItemId+"已经提交过，不需要再提交！");
				return;
			}
			//end mod 20160715
			/** 检查工作项状态决定能否提交 */
			WMWorkItemState.fromInt(workItem.getState()).checkTransition(
					WMWorkItemState.CLOSED_COMPLETED, true);

			/** 设置工作项目Dto信息 */
			workItem.setState(WMWorkItemState.CLOSED_COMPLETED_INT);
			workItem.setCompletedDate(DateHelper.getTimeStamp());
			workItem.setMemo(memo);
			if (flowParamMap != null) {// 操作人信息载入
				String operatePartyType = StringHelper.valueOf(flowParamMap
						.get("operatePartyType"));
				String operatePartyId = StringHelper.valueOf(flowParamMap
						.get("operatePartyId"));
				String operatePartyName = StringHelper.valueOf(flowParamMap
						.get("operatePartyName"));
				workItem.setOperatePartyType(operatePartyType);
				workItem.setOperatePartyId(operatePartyId);
				workItem.setOperatePartyName(operatePartyName);
			}
			workItemService.updateWorkItem(workItem, useDB);
			// 活动实例可以放到Redis缓存中。
			ActivityInstanceDto activityInstance = activityInstanceService
					.queryActivityInstance(workItem.getActivityInstanceId()
							.toString());

			/** 检查活动实例状态决定能否正常结束 */
			WMActivityInstanceState.fromInt(activityInstance.getState())
					.checkTransition(WMActivityInstanceState.CLOSED_COMPLETED,
							true);

			/** 工作项目完成数目加一,状态为CLOSE_COMPLETED */
			activityInstance.setItemCompleted(1);
			activityInstance.setCompletedDate(DateHelper.getTimeStamp());
			activityInstance.setState(WMActivityInstanceState.CLOSED_COMPLETED
					.intValue());
			activityInstanceService.updateActivityInstance(activityInstance,
					useDB);

			/** 获得流程定义 */
			String processDefinitionId = workItem.getProcessDefineId()
					.toString();
			String activityDefinitionId = workItem.getActivityDefinitionId()
					.toString();

			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processDefinitionId);
			Activity activity = process.getActivityById(activityDefinitionId);

			ProcessInstanceDto processInstance = processInstanceService
					.queryProcessInstance(processInstanceId);

			/** 激活依赖它的活动实例 */
			int state = processInstance.getState();
			logger.info("----流程实例状态："+state);
			if (state == WMProcessInstanceState.OPEN_RUNNING_INT
					|| state == WMProcessInstanceState.OPEN_RUNNING_ROLLBACK_INT) {
				if (flowParamMap != null) {
					/** 设置流程全局变量 */
					processParamService.setProcessParam(
							LongHelper.valueOf(processInstanceId),
							flowParamMap, false);
				}

				if (!activityInstance
						.getDirection()
						.trim()
						.equalsIgnoreCase(ActivityInstanceDto.REVERSE_DIRECTION)) {
					/** 正向 */
					executeEfferentTransitions(activity, activityInstance,
							processInstance, areaId, flowPassMap, useDB);
				} else {
					/** 反向，不管线上条件，无需设置运行时数据 */
					executeReverseTransitions(activity, activityInstance,
							processInstance, areaId, flowPassMap, useDB);
				}
			}
		} catch (Exception ex) {
			logger.error("提交工作项异常，工作项信息为："+GsonHelper.toJson(workItem)+"-异常摘要："+ ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}

	/**
	 * 
	 * 退单
	 * 
	 * @param workItemId
	 * @param reasonCatalogId
	 * @param targetActivityId
	 * @param areaId
	 * @return
	 * @throws FastflowException
	 */
	public void disableWorkItem(String workItemId, String reasonCode,
			String reasonConfigId, String memo, String areaId, boolean useDB, Map<String,String> flowPassMap)
			throws FastflowException {
		WorkItemDto workItemDto = workItemService.queryWorkItem(workItemId);
		// 检查是否为空
		if (workItemDto == null) {
			throw new FastflowException("工作项不存在：" + workItemId);
		}
		// 获取当前的活动实例Dto信息
		ActivityInstanceDto activityInstance = activityInstanceService
				.queryActivityInstance(workItemDto.getActivityInstanceId()
						.toString());
		// 获取流程实例
		String processInstanceId = activityInstance.getProcessInstanceId()
				.toString();
		ProcessInstanceDto processInstance = processInstanceService
				.queryProcessInstance(processInstanceId);
		if(WMProcessInstanceState.OPEN_NOTRUNNING_SUSPENDED_INT==processInstance.getState()){
			processInstance.setState(WMProcessInstanceState.OPEN_RUNNING_INT);
			processInstanceService.updateProcessInstance(processInstance, useDB);

			//处理活动实例
			List<ActivityInstanceDto> suspendActivityInstances = activityInstanceService.queryActivityInstancesByState(
					processInstanceId,
					null,
					String.valueOf(WMActivityInstanceState.OPEN_SUSPENDED_INT),
					null);
			Iterator<ActivityInstanceDto> suspendIter = suspendActivityInstances.iterator();
			Long suspendActInstId = null;
			while (suspendIter.hasNext()) {
				ActivityInstanceDto suspendActivityInstance = (ActivityInstanceDto) suspendIter
						.next();
				suspendActivityInstance.setState(WMActivityInstanceState.OPEN_RUNNING.intValue());
				suspendActInstId = suspendActivityInstance.getId();
				activityInstanceService.updateActivityInstance(suspendActivityInstance, useDB);
			}
			WorkItemDto newWorkItem = workItemDto;
			newWorkItem.setWorkItemId(null);
			newWorkItem.setDirection("1");
			newWorkItem.setState(WMWorkItemState.OPEN_RUNNING.intValue());
			newWorkItem.setStartedDate(DateHelper.getTimeStamp());
			newWorkItem.setActivityInstanceId(suspendActInstId);
			newWorkItem = workItemService.createWorkItem(newWorkItem, useDB);
			this.disableWorkItem(newWorkItem.getWorkItemId().toString(), reasonCode, reasonConfigId, memo, areaId, useDB, flowPassMap);
			return;
		}
		// 检查状态是否能够切换
		WMWorkItemState.states()[workItemDto.getState()].checkTransition(
				WMWorkItemState.DISABLED, true);

		// 更新状态
		workItemDto.setState(WMWorkItemState.DISABLED_INT);
		workItemDto.setCompletedDate(DateHelper.getTimeStamp());
		workItemDto.setMemo(memo);
		workItemService.updateWorkItem(workItemDto, useDB);
		
		String startActiityId = activityInstance.getActivityDefinitionId();
		String activityInstanceId = StringHelper.valueOf(activityInstance
				.getId());


		// 获取流程定义
		WorkflowProcess process = processDefinitionService
				.findWorkflowProcessById(processInstance.getProcessDefineId());

		// 判断流程定义是否空
		if (process == null) {
			throw new FastflowException("流程定义不存在："
					+ processInstance.getProcessDefineId());
		}

		// 获取当前活动完成的工作项个数
		int itemCompleted = activityInstance.getItemCompleted();
		activityInstance.setItemCompleted(++itemCompleted);
		activityInstanceService.updateActivityInstance(activityInstance, useDB);

		// 检查活动的工作项是否都完成了
		if (itemCompleted == activityInstance.getItemSum()) {
			Activity activity = process.getActivityById(startActiityId);

			boolean hasReason = (reasonConfigId != null || reasonCode != null);
			String targetActivityId = null;
			// 存在异常原因
			if (hasReason) {
				targetActivityId = setRollbackInfo(processInstance,
						reasonConfigId, reasonCode, startActiityId, process,
						useDB,areaId);
			} else {
				// 不存在异常原因
				String exActivityId = process.getExceptionActivityId();
				if (exActivityId == null) {
					throw new FastflowException("异常活动节点不存在！请检查XPDL");
				}

				// 从流程属性里面查找流程的目标节点
				targetActivityId = processAttrService.getProcessAttr(
						processInstance.getProcessInstanceId(), exActivityId,
						ProcessAttrService.TARGETACTIVITYID);
				if (StringHelper.isEmpty(targetActivityId)) {
					targetActivityId = process.getStartActivity().getId();
				}
			}
			logger.info("----目标节点id,targetActivityId:"+targetActivityId);
			// 得到目标环节的正向活动实例(closed.completed and direction=1)
			List<ActivityInstanceDto> targetActivityInstances = activityInstanceService
					.queryActivityInstancesByState(processInstanceId,
							targetActivityId,
							WMActivityInstanceState.CLOSED_COMPLETED_INT + "",
							ActivityInstanceDto.NORMAL_DIRECTION);

			logger.info("----目标节点实例targetActivityInstances.size:"+targetActivityInstances.size());
			if (targetActivityInstances.size() > 0) {
				// 如果目标环节有多个处于COMPLETED状态的活动实例，对每个活动实例检查当前活动实例是否可达
				Set<String> canReacheds = new HashSet<String>();
				for (int i = 0; i < targetActivityInstances.size(); i++) {
					ActivityInstanceDto targetActivityInstance = targetActivityInstances
							.get(i);
					String targetActivityInstanceId = targetActivityInstance
							.getId().toString();
					// 获得从目标节点活动实例可达的正向活动实例id集合
					Set<String> canReachActivityInstanceIds = findCanRollbackActivityInstanceIds(
							targetActivityInstanceId, processInstanceId);
					if (canReachActivityInstanceIds
							.contains(activityInstanceId)) {
						canReacheds.addAll(canReachActivityInstanceIds);
					}
				}
				// 查找流程中处于初始状态的活动实例，检查是否可回退，如果是，就回滚
				List<ActivityInstanceDto> initActivityInstances = activityInstanceService
						.queryActivityInstancesByState(
								processInstanceId,
								null,
								WMActivityInstanceState.OPEN_INITIATED_INT + "",
								ActivityInstanceDto.NORMAL_DIRECTION);

				logger.info("----目标节点实例initActivityInstances.size:"+initActivityInstances.size());
				for (int i = 0; i < initActivityInstances.size(); i++) {
					ActivityInstanceDto initActivityInstance = initActivityInstances
							.get(i);

					Activity initActivity = process
							.getActivityById(initActivityInstance
									.getActivityDefinitionId());
					String initActivityInstanceId = initActivityInstance
							.getId().toString();
					if (canReacheds.contains(initActivityInstanceId)) {
						// 结束初始化状态的活动实例，更新其状态
						initActivityInstance.setStartedDate(DateHelper
								.getTimeStamp());
						initActivityInstance.setCompletedDate(DateHelper
								.getTimeStamp());
						initActivityInstance
								.setState(WMActivityInstanceState.CLOSED_ABORTED_INT);
						activityInstanceService.updateActivityInstance(
								initActivityInstance, useDB);
						executeReverseTransitions(initActivity,
								initActivityInstance, processInstance, areaId,
								flowPassMap, useDB);
						canReacheds.remove(initActivityInstanceId);
					}
				}

				fix2ndReturn(canReacheds, processInstance, process, areaId,
						useDB);

				activityInstance.setCompletedDate(DateHelper.getTimeStamp());
				activityInstance
						.setState(WMActivityInstanceState.CLOSED_ABORTED_INT);
				activityInstanceService.updateActivityInstance(
						activityInstance, useDB);

				executeReverseTransitions(activity, activityInstance,
						processInstance, areaId, flowPassMap, useDB);

			} else {
				throw new FastflowException("未找到目标环节！" + "workItemId:"
						+ workItemId + ",reasonCode:" + reasonCode
						+ ",reasonConfigId:" + reasonConfigId);
			}
		}
	}

	/**
	 * add by 陈智堃 2009-12-22 冬至 UR-49902 begin 彻底解决流程引擎二次退单引起的问题
	 * 二次退单问题的根源，在于分支的合并节点在第一次退单的时候已经拆掉（状态为7），当第二次退单退到分支并行节点上方的时候，
	 * 找不到初始化状态的活动实例（也就是合并节点），导致另外一个分支不出拆单
	 * 
	 * 解决思路：遍历从目标环节活动实例可达的正向活动实例，如果存在活动实例的状态为已拆（状态值为7），且活动实例类型为合并
	 * 节点（根据XPDL中节点扩展属性中的“nodeType”来确定），就对该活动实例执行反向流转，从而对另外一个分支产生拆单
	 * 
	 * @throws UOSException
	 * @throws WMWorkflowException
	 */
	private void fix2ndReturn(Set<String> canReachedActivityInstanceIds,
			ProcessInstanceDto processInstance, WorkflowProcess process,
			String areaId, boolean useDB) throws FastflowException {
		String[] canReachedIds = new String[canReachedActivityInstanceIds
				.size()];
		int loop = 0;
		Iterator<String> reachIter = canReachedActivityInstanceIds.iterator();
		while (reachIter.hasNext()) { // 先由HashSet转换为String[]，不然如果在下面直接用canReacheds.toArray()转换会报错
			canReachedIds[loop++] = reachIter.next();
		}
		// 查找目标环节可达活动实例中状态为已拆（7）的活动实例集合
		List<ActivityInstanceDto> canReachedArchivedActivity = activityInstanceService
				.findActivityInstances(canReachedActivityInstanceIds,
						WMActivityInstanceState.ARCHIVED_INT);
		Iterator<ActivityInstanceDto> canReachedIter = canReachedArchivedActivity
				.iterator();
		while (canReachedIter.hasNext()) {
			ActivityInstanceDto archivedActivityInstance = canReachedIter
					.next();
			Activity archivedActivity = process
					.getActivityById(archivedActivityInstance
							.getActivityDefinitionId());
			// 根据nodeType属性判断是否合并节点
			if (archivedActivity!=null && archivedActivity.isRelation()) {
				/**
				 * 该合并节点已拆，表明这次退单是二次退单，需要对该合并节点进行反向转移 且由于该节点已拆，直接反向转移，不创建反向活动实例
				 */
				executeReverseTransitions(archivedActivity,
						archivedActivityInstance, processInstance, areaId,
						null, useDB);

				/**
				 * 一个活动实例不能同时有正向和反向的出边，否则在显示定单流程的时候会报错
				 * 由于这个已拆的合并节点已经有正向的出边了，在经过反向转移之后会创建反向的出边
				 * 需要把这些反向出边的起点改为这个合并节点的反向活动实例上
				 */
				if (archivedActivityInstance.getReverse() != null) { // 查找所有以该合并节点为起点的线条
					List<TransitionInstanceDto> fromArchivedTrans = transitionInstanceService
							.findTransitionInstancesByFromActivity(
									processInstance.getProcessInstanceId()
											.toString(),
									archivedActivityInstance.getId());
					Iterator<TransitionInstanceDto> transIter = fromArchivedTrans
							.iterator();
					while (transIter.hasNext()) {
						TransitionInstanceDto fromArchivedTransInstance = transIter
								.next();
						// 判断是否反向线条
						if (fromArchivedTransInstance.getDirection().equals(
								TransitionInstanceDto.REVERSE_DIRECTION)) { // 反向线条，把起点改为该合并节点的反向节点
							fromArchivedTransInstance
									.setFromActivityInstanceId(archivedActivityInstance
											.getReverse());
							transitionInstanceService.updateTransitionInstance(
									fromArchivedTransInstance, useDB);
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * 在流程中查找目标节点可达的所有活动实例id
	 * 
	 * @param targetActivityInstanceId
	 * @param processInstanceId
	 * @return
	 */
	private Set<String> findCanRollbackActivityInstanceIds(
			String targetActivityInstanceId, String processInstanceId) {
		Set<String> canReacheds = new HashSet<String>();
		canReacheds.add(targetActivityInstanceId);
		logger.info("-----targetActivityInstanceId:"+targetActivityInstanceId);
		List<TransitionInstanceDto> trans = transitionInstanceService
				.findTransitionInstances(processInstanceId,
						TransitionInstanceDto.NORMAL_DIRECTION);
		logger.info("-------trans:"+trans.size());
		// ???算法是否存在误差
		for (int i = 0; i < trans.size(); i++) {
			TransitionInstanceDto tran = trans.get(i);
			logger.info("-------tran:"+GsonHelper.toJson(tran));
			if (canReacheds.contains(tran.getFromActivityInstanceId()
					.toString())) {
				logger.info("-----tran.getToActivityInstanceId():"+tran.getToActivityInstanceId().toString());
				canReacheds.add(tran.getToActivityInstanceId().toString());
			}
		}

		return canReacheds;
	}

	/**
	 * 根据异常原因查找配置，并找到目标节点进行记录
	 * 
	 * @param processInstance
	 * @param reasonConfigId
	 * @param reasonCode
	 * @param startActivityId
	 * @param process
	 * @param useDB
	 * @return
	 * @throws FastflowException
	 */
	private String setRollbackInfo(ProcessInstanceDto processInstance,
			String reasonConfigId, String reasonCode, String startActivityId,
			WorkflowProcess process, boolean useDB,String areaId) throws FastflowException {
		/** 在流程定义中找到异常处理活动 */
		String exActivityId = process.getExceptionActivityId();

		Activity startActivity = process.getActivityById(startActivityId);

		String targetActivityId = null; // 目标环节ID
		String startMode = null; // 启动方式
		String reasonType = null;// 异常类型
		String autoToManual = null; // 自动转人工,默认为false

		// 检查流程实例是否存在目标环节，存在则直接返回
		String tmpTarget = processAttrService.getProcessAttr(
				processInstance.getProcessInstanceId(), exActivityId,
				ProcessAttrService.TARGETACTIVITYID);
		if (tmpTarget != null && !"".equalsIgnoreCase(tmpTarget)) {
			return tmpTarget;
		}

		/**
		 * add by 陈智堃 2011-05-26 UR-76934 数据驱动流程改造
		 * 如果有传入reasonConfigId，则首先从原因配置表中查询目标节点
		 */
		if (reasonCode != null) { // 根据原因配置表中的目标环节ID关联工作项的环节ID，从而查出目标环节的活动定义
									// 这个SQL存在一个隐患，假如流程存在2个相同的环节（活动ID不同，但环节ID一样），这个时候会随机取找到的第一条记录
									// 如果真的存在这种情况，建议把环节拆开，在流程引擎内实在是判断不了
			//modify by bobping 流程模板id改成流程模板编码获取异常原因配置
			ReturnReasonConfigDto returnReasonConfigDto = getTargerActivityIdByReasonCode(reasonCode,
					processInstance.getProcessDefineCode(), startActivity.getTacheId(), areaId);
			if (returnReasonConfigDto != null) {
				// 如果目标环节为0，则代表需要退到开始节点，直接从流程定义中取出开始节点的活动ID
				if (returnReasonConfigDto.getTargetTacheId() == 0) {
					targetActivityId = process.getStartActivity().getId();
				} else {
					WorkItemDto targetWorkItemDto = workItemService
							.queryWorkItemByTacheId(
									processInstance.getProcessInstanceId(),
									returnReasonConfigDto.getTargetTacheId());
					if (targetWorkItemDto != null) {
						targetActivityId = targetWorkItemDto
								.getActivityDefinitionId();
					}
				}
				startMode = returnReasonConfigDto.getStartMode();
				autoToManual = returnReasonConfigDto.getAutoToManual();
				reasonType = returnReasonConfigDto.getReasonType();
			}

			// 如果传入的原因类型是待装，但表里的启动方式不是待装折返或待装自动，则把启动方式改为待装折返
			if (ReturnReasonConfigDto.REASON_TYPE_WAIT.equals(reasonType)
					&& !WMAutomationMode.WAIT.equals(startMode)
					&& !WMAutomationMode.WAIT_TO_AUTO.equals(startMode))
				startMode = WMAutomationMode.WAIT;
		}

		if (StringHelper.isEmpty(targetActivityId)) {
			throw new FastflowException("未找到异常配置：" + ",reasonCode:"
					+ reasonCode + ",processDefineId:"
					+ processInstance.getProcessDefineId() + ",areaId:" + areaId + ",tacheId:" + startActivity.getTacheId());
		}

		// 设置运行时信息
		processAttrService.setProcessAttr(
				processInstance.getProcessInstanceId(), exActivityId,
				ProcessAttrService.TARGETACTIVITYID, targetActivityId, useDB);
		processAttrService.setProcessAttr(
				processInstance.getProcessInstanceId(), exActivityId,
				ProcessAttrService.STARTMODE, startMode, useDB);
		processAttrService.setProcessAttr(
				processInstance.getProcessInstanceId(), exActivityId,
				ProcessAttrService.AUTOTOMANUAL, autoToManual, useDB);

		return targetActivityId;
	}

	//modify by bobping 流程模板id改成流程模板编码获取异常原因配置
	private ReturnReasonConfigDto getTargerActivityIdByReasonCode(
			String reasonCode, String processDefineCode, String tacheId, String areaId) {
		ReturnReasonConfigDto returnReasonConfigDto = returnReasonService
				.getTargerActivityIdByReasonCode(reasonCode, processDefineCode,
						tacheId,areaId);
		if(returnReasonConfigDto == null){
			AreaDto area = areaService.findAreaByAreaId(LongHelper.valueOf(areaId));
			String areaPathCode = area.getPathCode();
			int index = areaPathCode.lastIndexOf(".");
			String upAreaId = "";
			if (index == -1) {
				return returnReasonConfigDto;
			}
			String upAreaPathCode = areaPathCode.substring(0, index);
			index = upAreaPathCode.lastIndexOf(".");
			upAreaId = upAreaPathCode.substring(index + 1);
			if("-1".equals(upAreaId)){
				return returnReasonConfigDto;
			}
			returnReasonConfigDto = this.getTargerActivityIdByReasonCode(reasonCode, processDefineCode, tacheId, upAreaId);
		}
		return returnReasonConfigDto;
	}

	private void executeEfferentTransitions(Activity activity,
			ActivityInstanceDto activityInstance,
			ProcessInstanceDto processInstance, String areaId,
			Map<String, String> processPassMap, boolean useDB)
			throws FastflowException {
		try {
			String processInstanceId = processInstance.getProcessInstanceId()
					.toString();
			/** 取出源活动的所有出边 */
			Iterator<Transition> transitions = activity
					.getEfferentTransitions().iterator();

			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processInstance
							.getProcessDefineId());

			/** 取得当前流程里面所有的成对节点（分支合并） */
			List<ActivityContext> activityList = new ArrayList<ActivityContext>();
			
			while (transitions.hasNext()) {

				Transition currentTransition = transitions.next();

				/** 当前活动的下一条边进行处理逻辑 */
				Condition condition = currentTransition.getCondition();
				/**
				 * 如果当前节点为分支节点，则分支节点的目标节点中，如果有一个不满足，
				 * 需要把整条线上的节点在关系矩阵中的有效性设置为False
				 */
				Activity toActivity = currentTransition.getToActivity();

				/** 计算条件 */
				boolean conditionRet = calculateCondition(processInstanceId, condition, process,
						areaId, toActivity.getTacheCode(),processPassMap);
				logger.info("---计算条件结果："+conditionRet);
				if (conditionRet) {
					List<ActivityInstanceDto> toActInsts = activityInstanceService
							.queryActivityInstancesByState(
									processInstanceId,
									toActivity.getId(),
									String.valueOf(WMActivityInstanceState.OPEN_INITIATED_INT),
									null);
					ActivityInstanceDto toActivityInstance = null;
					if (toActInsts.size() != 0) {
						toActivityInstance = toActInsts.get(0);
					}
					if (toActivityInstance == null) {
						toActivityInstance = activityInstanceService
								.createActivityInstance(toActivity,
										processInstance,
										ActivityInstanceDto.NORMAL_DIRECTION,
										useDB);
					}
					/** 创建转移实例 */
					TransitionInstanceDto tranIns = transitionInstanceService.createTransitionInstance(
							processInstance, toActivityInstance,
							activityInstance, currentTransition,
							TransitionInstanceDto.NORMAL_DIRECTION, useDB);
					
					setRollBackTranInsToActIns(processInstanceId, currentTransition, activityInstance,
							toActivityInstance, tranIns,useDB);

					activityList.add(new ActivityContext(toActivity,
							toActivityInstance));
				}
			}
			if (activityList.size() == 0) {
				if (!activity.isEndActivity()) {
					/** 未找到流程的下一节点 */
					throw new FastflowException(
							"根据条件无法计算出下一个环节process "
									+ processInstance.getName()
									+ " and current activity is "
									+ activity.getName());
				}
			}
			
			if (activity.isControl()&&activityList.size() > 1) {
					/** 控制结构后有多个满足条件的分支 */
					throw new FastflowException(
							"当前节点是控制结构，请确认控制结构后面只有一个分支条件满足，当前满足的分支个数为："+activityList.size());
			}
			// 初始化下节点活动实例
			List<ActivityInstanceDto> runningActivityInstances = activityInstanceService
					.queryActivityInstancesByState(
							processInstanceId,
							null,
							WMActivityInstanceState.OPEN_INITIATED_INT
									+ ","
									+ WMActivityInstanceState.OPEN_SUSPENDED_INT
									+ ","
									+ WMActivityInstanceState.OPEN_RUNNING_INT,
							null);

			// add by ji.dong 2012-11-05 ur:86940
			Long workItembatchId = workItemService.getWorkItemBatchNo();

			for (int i = 0; i < activityList.size(); i++) {
				/** 遍历下一步活动实例 */
				Activity toActivity = activityList.get(i).getActivity();
				ActivityInstanceDto toActivityInstance = activityList.get(i)
						.getInstance();
				/**
				 * mod by 陈智堃 2009-12-18 UR-49815
				 * 有一种特殊情况，假设有2个分支A和B，先由分支外退单到分支A，然后重新流转到分支外，这个时候再退单到分支B，流程会卡死
				 * 这是由于第一次退单时，B没有发拆单，A分支重新流转下来的时候，A和B的合并节点只有A一根线条，分支B的线条断了，再次退单
				 * 到分支B会由于目标节点不可达而卡死 解决思路：A拆单后重新流转到合并节点的时候，由B新增一条补偿线到合并节点处
				 * */
				repairRouteTrans(toActivity, toActivityInstance,
						processInstanceId, areaId, useDB);

				/** 判断当前所有OPEN状态实例到目标活动是否可达,如果toActivity是JoinXOR的话,不需要判断 */
				boolean isCanEnable = false;
				logger.info("runningActivityInstances:"
						+ runningActivityInstances.size());
				isCanEnable = isCanEnable(runningActivityInstances, toActivity,
						process);

				logger.info(!isJoinAndOr(toActivity) + ";" + isCanEnable);
				if (!isJoinAndOr(toActivity) || isCanEnable) {
					toActivityInstance = activityInstanceService
							.queryActivityInstance(toActivityInstance.getId()
									.toString());
					if (toActivityInstance.getState() != WMActivityInstanceState.OPEN_INITIATED_INT) {
						continue;
					}

					// add by ji.dong 2012-11-05 ur:86940
					toActivityInstance.setBatchid(workItembatchId);

					/** 激活环节实例 */
					enableActivityInstance(toActivity, processInstance,
							toActivityInstance, areaId, processPassMap, useDB);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}

	/**
	 * 激活活动实例
	 * 
	 * @param activity
	 *            Activity 待激活的活动定义
	 * @param processInstance
	 *            ProcessInstanceDto 流程实例
	 * @param activityInstance
	 *            ActivityInstanceDto 待激活的活动实例
	 * @throws WMWorkflowException
	 * @throws UOSException
	 * @return Collection
	 * @throws FastflowException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws SQLException
	 */
	private void enableActivityInstance(Activity activity,
			ProcessInstanceDto processInstance,
			ActivityInstanceDto activityInstance, String areaId,
			Map<String, String> flowPassMap, boolean useDB)
			throws FastflowException {
		/** 根据活动实例的状态决定是否可以激活实例 */
		WMActivityInstanceState.fromInt(activityInstance.getState())
				.checkTransition(WMActivityInstanceState.OPEN_RUNNING, true);

		/** 设置活动实例状态为RUNNING,已完成工作项数目为0 */
		activityInstance.setItemCompleted(0);
		activityInstance.setState(WMActivityInstanceState.OPEN_RUNNING_INT);
		Timestamp now = DateHelper.getTimeStamp();
		activityInstance.setStartedDate(now);

		/** 判断是否存在实现的活动 */
		if (activity.isNotRouteActivity()) {
			if (activityInstance.getBatchid() == null) {
				Long workItembatchId = workItemService.getWorkItemBatchNo();
				activityInstance.setBatchid(workItembatchId);
			}

			/** 创建工作项 */
			WorkItemDto workItem = workItemService.createWorkItem(activity,
					processInstance, activityInstance, useDB);
			/** 创建工作项完成 */

			TacheDto tache = tacheService.queryTache(Long.valueOf(activity
					.getTacheId()));
			//add by che.zi 20160721
			//增加环节时限处理
			Date limitDate = null;
			Date alertDate = null;
			if(FastflowConfig.useTimeLimit){
				try {
					TacheLimitDto tacheLimitDto = tacheLimitService.qryTacheLimitByByTAP(activityInstance.getTacheId(),areaId,processInstance.getProcessDefineId());
					logger.info("----查询到的tacheLimitDto:"+GsonHelper.toJson(tacheLimitDto));
					if(tacheLimitDto != null){
						if(tacheLimitDto.getIsWorkTime().equals("1") && workItem.getAreaId() != null){
							alertDate = timeLimitClient.calculateWorkTime(workItem.getStartedDate(), tacheLimitDto
											.getAlertValue().intValue(),tacheLimitDto.getTimeUnit(), workItem.getAreaId(), processInstance.getProcessDefineId());
							limitDate = timeLimitClient.calculateWorkTime(workItem.getStartedDate(),tacheLimitDto.getLimitValue().intValue(), tacheLimitDto.getTimeUnit(),
									workItem.getAreaId(), processInstance.getProcessDefineId());
						}else{
							alertDate = timeLimitClient.getAbsDateByTimeUnit(workItem.getStartedDate(),tacheLimitDto.getTimeUnit(), tacheLimitDto.getAlertValue().intValue());
					        limitDate = timeLimitClient.getAbsDateByTimeUnit(workItem.getStartedDate(),tacheLimitDto.getTimeUnit(), tacheLimitDto.getLimitValue().intValue());
						}
						logger.info("---计算后的完成时限时间limitDate："+limitDate+"---计算后的预警时间alertDate:"+alertDate);
					//	环节时限通过生成工单的流程透传参数传到外系统
//						workItem.setLimitDate(limitDate);
//						workItem.setAlertDate(alertDate);
//						workItemService.updateWorkItem(workItem, useDB);
//
//					    //将流程时限结果通知业务侧
//					    Map<String,Object> map = new HashMap<String,Object>();
//					    map.put("processInstanceId", activityInstance.getProcessInstanceId().toString());
//					    map.put("areaId", areaId);
//					    map.put("limitDate", DateHelper.parseTime(limitDate));
//					    map.put("alertDate", DateHelper.parseTime(alertDate));
//					    map.put("tacheCode", tache.getTacheCode());
//					    map.put("workItemId", workItem.getWorkItemId());
//					    workflowStateReport.reportTimeLimit(map);
					}else{
						logger.error("----未配置环节时限规则,流程模板id:"+processInstance.getProcessDefineId()
								+",areaId:"+areaId+",tacheId:"+activityInstance.getTacheId());
					}
				} catch (NumberFormatException e) {
					logger.error("处理环节时限异常:"+e.getMessage(),e);
					throw new RuntimeException("处理环节时限异常:"+e.getMessage(),e);
				} catch (Exception e) {
					logger.error("处理环节时限异常:"+e.getMessage(),e);
					throw new RuntimeException("处理环节时限异常："+e.getMessage(),e);
				}
			}
			// end 

			/** 更新活动实例状态 */
			activityInstance.setWorkItemId(workItem.getWorkItemId());
			activityInstanceService.updateActivityInstance(activityInstance,
					useDB);

			String relaWorkitemId = null;
			/** 如果反向，relaWorkitemId传原正向工作项 */
			if (activityInstance.getDirection().trim()
					.equals(ActivityInstanceDto.REVERSE_DIRECTION)) {
				String normalActivityInstanceId = activityInstanceService
						.queryReverseActivity(activityInstance.getId());
				/** 获得对应的正向工作项ID */
				relaWorkitemId = activityInstanceService.queryWrokItemId(Long
						.valueOf(normalActivityInstanceId));
			}

			if (tache != null && "FLOW".equals(tache.getTacheType())) {
				int count = 0;
				List<ProcessInstanceDto> subFlows = new ArrayList<ProcessInstanceDto>();
				String packageDefineCodes = tache.getPackageDefineCodes();
				String[] packageDefineArr = packageDefineCodes.split(",");
				Map<String,String> flowParamMap = null;
				for (int i = 0; i < packageDefineArr.length; i++) {
					String packageDefineCode = packageDefineArr[i];
					ProcessInstanceDto subProcessInstance = this
							.createProcessInstanceByCode(packageDefineCode,
									activityInstance.getId().toString(), null,
									activityInstance.getPriority(), flowParamMap,
									workItem.getAreaId(), useDB);
					processParamService.setParentWid(subProcessInstance
							.getProcessInstanceId(), workItem.getWorkItemId()
							.toString(), tache.getTacheCode(), useDB);
					subFlows.add(subProcessInstance);
					count = count + 1;
				}
				workItem.setSubFlowCount(count);
				workItem.setFinishSubFlowCount(0);
				workItemService.updateWorkItem(workItem, useDB);
				for (int i = 0; i < subFlows.size(); i++) {
					ProcessInstanceDto subProcessInstance = subFlows.get(i);
					this.startProcessInstance(subProcessInstance
							.getProcessInstanceId().toString(),
							subProcessInstance.getAreaId(),flowPassMap, useDB,flowParamMap);
				}
				if (tache.getIsAuto() == 1) {
					if (workItem.getState() == WMWorkItemState.OPEN_RUNNING_INT) {
						completeWorkItem(workItem.getWorkItemId().toString(),
								null, null, areaId, flowPassMap, useDB);
					}
				}
			} else {
				if (tache == null) {
					throw new RuntimeException("tache为空：[tache_id="
							+ activity.getTacheId() + "]");
				}else{
					// 是否自动回单
					if (tache.getIsAuto() == 1) {
						if (workItem.getState() == WMWorkItemState.OPEN_RUNNING_INT) {
							completeWorkItem(workItem.getWorkItemId().toString(),
									null, null, areaId, flowPassMap, useDB);
						}
					}else{
						CreateWorkOrderParamDto workOrderDto = new CreateWorkOrderParamDto();
						workOrderDto.setProcessInstanceId(processInstance
								.getProcessInstanceId());
						workOrderDto.setTacheId(Long.valueOf(activity.getTacheId()));
						workOrderDto.setOperId(-1l);
						workOrderDto.setWorkOrderType(null);
						workOrderDto.setWorkitemId(workItem.getWorkItemId());
						workOrderDto.setParticipantId("");
						workOrderDto.setParticipantType("");
						workOrderDto.setRelaWorkitemId(LongHelper
								.valueOf(relaWorkitemId));
						flowPassMap = resetFlowPassMap(
								processInstance.getProcessInstanceId(), flowPassMap);
						// 环节时限通过流程透传参数 在生成工单时一起传到外系统 add 20170328
						if(FastflowConfig.useTimeLimit){
							flowPassMap.put(LIMIT_DATE, DateHelper.parseTime(limitDate));
							flowPassMap.put(ALERT_DATE, DateHelper.parseTime(alertDate));
						}
						// 20170328
						/**获得退单目标环节,如果为开始节点targetStart设置为1，
	                     * 则在撤单或退单到CRM时，流程的所有环节均回退*/
	                    String targetActivityId = getTargetActivityId(processInstance);
	                    WorkflowProcess process = processDefinitionService.findWorkflowProcessById(processInstance.getProcessDefineId());
	                    String targetStart = null;
	                    if (process.getStartActivity().getId().equals(targetActivityId)) {
	                        targetStart = "1";
	                    }
	                    String exActivityId = process.getExceptionActivityId();
	                    if (exActivityId == null) {
	                        throw new FastflowException("流程模板中异常节点不存在！");
	                    }
	                    String startMode = processAttrService.getProcessAttr(processInstance.getProcessInstanceId(), exActivityId, "StartMode");
	                    if ("".equalsIgnoreCase(startMode)) {
	                        startMode = null;
	                    }
	                    /**是否自动转人工*/
	                    String sAutoToManual = processAttrService.getProcessAttr(processInstance.getProcessInstanceId(), exActivityId, "AutoToManual");
	                    boolean IsAutoToManual = "true".equalsIgnoreCase(sAutoToManual) ? true : false;
	                    if (activityInstance.getDirection().equals("1") && (startMode == null || startMode.equals("")) &&
	                        IsAutoToManual) {
	                        /**startMode 为null或""指退单到目标节点后变为正向，IsAutoToManual只有退单时才可能为真*/
	                    	processAttrService.setProcessAttr(processInstance.getProcessInstanceId(), exActivityId, "AutoToManual", "true", useDB);
	                        targetStart = "AutoToManual";
	                    }
	                    //add by 陈智堃 UR-54233 如果是到达目标环节之后的正向节点，需要发个标识给定单层
	                    else if(activityInstance.isIsReachedTarget())
	                    {
	                    	targetStart = "ReachedTarget";
	                    }
						workOrderDto.setFlowPassMap(flowPassMap);
						// 增加开关，是否实时获取xpdl中环节下一个线条的流程参数回传  modify by che.zi 20170729
						Map<String, String> flowParamMap = null;
						if(FastflowConfig.getTacheParamImm){
							logger.info("-----进入根据xpdl取线条上的流程参数---");
							flowParamMap = processParamDefService.qryProInsTacheParam(activity);
						}else{
							flowParamMap = processParamDefService
									.qryProInsTacheParam(
											processInstance.getProcessDefineId(),
											tache.getTacheCode());
						}
						//end 20170729
						workOrderDto.setFlowParamMap(flowParamMap);
						workOrderDto.setDirection(activityInstance.getDirection());
						workOrderDto.setCollaborate(false);
						workOrderDto.setReturnToStart(targetStart);
						workOrderDto.setWorkOrderBatchNo(activityInstance.getBatchid());
						workOrderDto.setAreaId(activityInstance.getAreaId());
						workOrderDto.setTacheCode(tache.getTacheCode());
						workOrderDto.setTacheName(tache.getTacheName());
						workflowStateReport.createWorkOrder(workOrderDto);
					}
				}
			} // end 非子流程
		} // end 存在现实活动
		else {
			completeRoute(activity, processInstance, activityInstance, areaId,
					flowPassMap, useDB);
		}
	}

	private Map<String, String> resetFlowPassMap(Long processInstanceId,
			Map<String, String> flowPassMap) {
		if (flowPassMap == null) {
			flowPassMap = new HashMap<String, String>();
		}
		Map<String, String> map = processParamService
				.getAllProcessParams(processInstanceId);
		for (String key : map.keySet()) {
			if (processParamDefService.isSendParam(key)) {
				flowPassMap.put(key, map.get(key));

			}
		}
		return flowPassMap;
	}

	private void completeRoute(Activity activity,
			ProcessInstanceDto processInstance,
			ActivityInstanceDto activityInstance, String areaId,
			Map<String, String> flowPassMap, boolean useDB)
			throws FastflowException {
		/** 路由节点无法提单因此在此设置活动实例启动时间，完成时间，直接设置活动实例状态为CLOSED_COMPLETED */
		Timestamp now = DateHelper.getTimeStamp();
		activityInstance.setItemCompleted(1);
		activityInstance.setItemSum(1);
		activityInstance.setStartedDate(now);
		activityInstance.setCompletedDate(now);
		activityInstance.setState(WMActivityInstanceState.CLOSED_COMPLETED_INT);
		activityInstanceService.updateActivityInstance(activityInstance, useDB);

		/** 是否到达正向终点 */
		boolean isNormalEnd = activity.isEndActivity()
				&& activityInstance.getDirection().trim()
						.equalsIgnoreCase(ActivityInstanceDto.NORMAL_DIRECTION);

		/** 是否到达反向起点 */
		boolean isReverseStart = activity.isStartActivity()
				&& activityInstance
						.getDirection()
						.trim()
						.equalsIgnoreCase(ActivityInstanceDto.REVERSE_DIRECTION);
		if (isNormalEnd || isReverseStart) {
			if (isReverseStart) {
				/** 如果反向到达开始节点,设置流程状态为ZERO */
				processInstance.setState(WMProcessInstanceState.CLOSED_ZEROED
						.intValue());
			} else {
				/** 正向到达结束节点,设置流程状态为COMPLETED */
				processInstance
						.setState(WMProcessInstanceState.CLOSED_COMPLETED
								.intValue());
			}
			processInstance.setCompletedDate(now);
			processInstanceService
					.updateProcessInstance(processInstance, useDB);
		
			if (isReverseStart) {
				/** 如果反向到达开始节点,通知定单需要判断是退单到开始，还是撤单到开始 */
				String targetActivityId = getExTargetActivityId(processInstance);
				/** 获得目标环节活动ID,调用业务层方法[回退到起点通知] */
				/** 无目标环节，表示撤单，comments传"0"，有目标环节是退单,comments传"1" */
				flowPassMap = resetFlowPassMap(
						processInstance.getProcessInstanceId(), flowPassMap);
				workflowStateReport.reportProcessState(processInstance
						.getProcessInstanceId(),
						((targetActivityId == null || targetActivityId.trim()
								.length() == 0) ? "0" : "1"),
						CommonDomain.WM_ZERO_REPORT, flowPassMap,null,areaId);
			} else {
				/** 调用业务层方法[流程结束通知] */
				flowPassMap = resetFlowPassMap(
						processInstance.getProcessInstanceId(), flowPassMap);
				workflowStateReport.reportProcessState(
						processInstance.getProcessInstanceId(), "",
						CommonDomain.WM_END_REPORT, flowPassMap,null,areaId);
			}
			//流程结束后 将实例数据转存到历史数据表
//			if(FastflowConfig.useHis){
//				workflowStateReport.saveDataToHis(processInstance.getProcessInstanceId().toString());
//			}
			
		} // end 正向结束或者反向节点
		else {
			if (!activityInstance.getDirection().trim()
					.equalsIgnoreCase(ActivityInstanceDto.REVERSE_DIRECTION)) {
				executeEfferentTransitions(activity, activityInstance,
						processInstance, areaId, flowPassMap, useDB);
			} else {
				executeReverseTransitions(activity, activityInstance,
						processInstance, areaId, flowPassMap, useDB);
			}
		}
	}

	/**
	 * 判断一个活动是否是JoinAnd、JoinOr或JoinANDOR(退出)活动
	 * 
	 * @param activity
	 *            Activity 活动(解析XPDL得到的活动对象)
	 * @return boolean
	 */
	private static boolean isJoinAndOr(Activity activity) {
		boolean result = false;
		if (activity.getJoinType() == JoinType.AND
				|| activity.getJoinType() == JoinType.OR) {
			result = true;
		}
		if (activity.isEndActivity()) { // End 活动默认是JoinANDOR
			result = true;
		}
		return result;
	}

	/**
	 * add by 陈智堃 2009-12-18 UR-49815
	 * 有一种特殊情况，假设有2个分支A和B，先由分支外退单到分支A，然后重新流转到分支外，这个时候再退单到分支B，流程会卡死
	 * 这是由于第一次退单时，B没有发拆单，A分支重新流转下来的时候，A和B的合并节点只有A一根线条，分支B的线条断了，再次退单
	 * 到分支B会由于目标节点不可达而卡死 解决思路：A拆单后重新流转到合并节点的时候，由B新增一条补偿线到合并节点处
	 * 
	 * @param activity
	 *            Activity 当前活动
	 * @param activityInstance
	 *            ActivityInstanceDto 当前活动实例
	 * @param processInstanceId
	 *            String 流程实例ID
	 * @throws WMWorkflowException
	 * */
	private void repairRouteTrans(Activity activity,
			ActivityInstanceDto activityInstance, String processInstanceId,
			String areaId, boolean useDB) {
		/** 判断是否路由节点 */
		if (!activity.isNotRouteActivity()) { // 是路由节点
			List<ActivityInstanceDto> archivedActivityInstances = activityInstanceService
					.queryActivityInstancesByState(processInstanceId, activity
							.getId(), String
							.valueOf(WMActivityInstanceState.ARCHIVED_INT),
							ActivityInstanceDto.NORMAL_DIRECTION);

			if (archivedActivityInstances.size() > 0) { // 取最后一个拆掉的活动实例
				List<ActivityInstanceDto> archivedList = new ArrayList<ActivityInstanceDto>(
						archivedActivityInstances);
				ActivityInstanceDto archivedRouteInst = archivedList
						.get(archivedList.size() - 1);
				// 取得最后拆掉活动实例的所有入边
				List<TransitionInstanceDto> archivedTrans = transitionInstanceService
						.findTransitionInstancesByToActivity(processInstanceId,
								archivedRouteInst.getId());
				// 取得当前需要激活的活动实例的所有入边
				List<TransitionInstanceDto> activityTrans = transitionInstanceService
						.findTransitionInstancesByToActivity(processInstanceId,
								activityInstance.getId());

				// 假若拆掉活动实例的入边大于当前活动实例的入边，则当前活动实例需要新增补偿线
				if (archivedTrans.size() > activityTrans.size()) {
					Iterator<TransitionInstanceDto> archivedIter = archivedTrans
							.iterator();
					// 检查拆掉活动实例的入边有哪条是当前活动实例所没有的
					while (archivedIter.hasNext()) {
						TransitionInstanceDto archivedTransDto = archivedIter
								.next();

						Iterator<TransitionInstanceDto> activityIter = activityTrans
								.iterator();
						while (activityIter.hasNext()) {
							TransitionInstanceDto activityTransDto = activityIter
									.next();
							// 这条边2个活动实例都存在
							if (archivedTransDto.getTransitionDefinitionId()
									.equals(activityTransDto
											.getTransitionDefinitionId())) {
								archivedIter.remove();
								activityIter.remove();
							}
						}
					}
					archivedIter = archivedTrans.iterator();
					// 遍历拆掉活动实例比当前活动实例多出来的边
					while (archivedIter.hasNext()) {
						TransitionInstanceDto archivedTransDto = archivedIter
								.next();
						// 原线条作废
						archivedTransDto
								.setState(WMTransitionInstanceState.ARCHIVED_INT);
						transitionInstanceService.updateTransitionInstance(
								archivedTransDto, useDB);
						// 克隆该线条
						TransitionInstanceDto newTrans = archivedTransDto
								.clone();
						if(newTrans != null){
							// 新线条的目标活动实例为当前活动实例，添加补偿线
							newTrans.setToActivityInstanceId(activityInstance
									.getId());
							newTrans.setId(null);
							newTrans.setState(WMTransitionInstanceState.ENABLED_INT);
							// 设置Action,手工添加的线，否则判断是否可达有问题
							newTrans.setAction(WMTransitionInstanceAction.ACTION_COMPENSATE
									.intValue());
							transitionInstanceService.createTransitionInstance(
									newTrans, useDB);
						}
					}
				}
			}
		}
	}

	/**
	 * 判断该点是否可激活,即所有处于活动的点到该点是否可达，如果有可达，返回false
	 * 
	 * @param runningActivityInstances
	 *            Collection 所有处于OPEN状态的活动实例
	 * @param toActivity
	 *            Activity 目标活动
	 * @param process
	 *            WorkflowProcess
	 * @return boolean
	 */
	private boolean isCanEnable(
			List<ActivityInstanceDto> runningActivityInstances,
			Activity toActivity, WorkflowProcess process) {
		if (runningActivityInstances != null
				&& runningActivityInstances.size() > 0) {
			String toActivityId = toActivity.getId();
			Iterator<ActivityInstanceDto> iter = runningActivityInstances
					.iterator();
			while (iter.hasNext()) {
				String runningActivityId = ((ActivityInstanceDto) iter.next())
						.getActivityDefinitionId();
				/** 有open状态的工作项未完成，且与目标节点之间可达 */
				if (!runningActivityId.equalsIgnoreCase(toActivityId)
						&& process.getMatrix().isCanReached(runningActivityId,
								toActivityId)) {
					logger.info(runningActivityId + "=======>" + toActivityId);
					return false;
				}
			}
		}
		return true;
	}

	private boolean calculateCondition(String processInstanceId,
			Condition condition, WorkflowProcess process, String areaId,
			String tacheCode, Map<String, String> flowPassMap) throws FastflowException {
		String con = null;
		boolean t = true;
		if (condition != null) {
			con = ((Xpression) condition.getXpressions().get(0)).getValue()
					.trim();
			if (con == null || con.length() == 0) {
				return true;
			}
			// modify by che.zi 2015-08-19 条件表达式改为流程参数，格式为{$流程参数$=value}
			/*
			 * //改造后存储的格式为{id}name,只需解析Id出来 String compIdStr = ""; if
			 * (con.indexOf('{') != -1) { int startIndex = con.indexOf('{') + 1;
			 * int endIndex = con.indexOf('}', startIndex); compIdStr =
			 * con.substring(startIndex, endIndex); } t =
			 * workflowStateReport.calCond(Long.valueOf(processInstanceId),
			 * Long.valueOf(compIdStr),areaId);
			 */
			/** 计算条件表达式-解析组件 */
			while (con.indexOf('{') != -1) {
				int startIndex = con.indexOf('{') + 1;
				int endIndex = con.indexOf('}', startIndex);
				String conditionStr = con.substring(startIndex, endIndex);
				try {
					Boolean result = executeCondition(conditionStr,
							processInstanceId, tacheCode);
					String value = result.toString().trim();
					/** 字符串替换 */
					con = StringUtils.replace(con, "{" + conditionStr + "}",
							value);
				} catch (Exception ex) {
					logger.error("计算条件失败:" + ex.getMessage() + ",原因:"
							+ ex.getCause());
					throw new FastflowException(ex);
				}
			}
			con = con.replaceAll("与", " && ");
			con = con.replaceAll("或", " || ");
			/** 计算条件表达式-用java自带的JavaScript计算复杂逻辑运算 */
			boolean result = true;
			try {
				ScriptEngine jse = new ScriptEngineManager()
						.getEngineByName("JavaScript");
				String mutilResult = String.valueOf(jse.eval(con));
				result = mutilResult.equals("true");
			} catch (ScriptException e) {
				logger.error("JavaScript计算异常：" + e.getMessage());
			}
			t = condition == null || con.trim().length() == 0 || result;
		}
		//add by che.zi 20160829 zmp:923087
		// 增加判断环节上是否存在参数reportCalCondResult，如果是true的话就将线条计算结果通知业务系统，否则不通知
		String isReport = processParamService.queryProcessParam(LongHelper.valueOf(processInstanceId), "reportCalCondResult");
		logger.info("------reportCalCondResult是否通知："+isReport);
		if("true".equals(isReport)){
			flowPassMap = resetFlowPassMap(LongHelper.valueOf(processInstanceId), flowPassMap);
			workflowStateReport.reportCalCondResult(LongHelper.valueOf(processInstanceId), tacheCode, t, flowPassMap,areaId);
		}
		//end
		return t;
	}

	/**
	 * 根据流程实例参数值，计算条件表达式的结果
	 * 
	 * @param conditionStr
	 * @param processInstanceId
	 * @param tacheCode
	 * @return
	 */
	private Boolean executeCondition(String conditionStr,
			String processInstanceId, String tacheCode) {
		String paramName = conditionStr.substring(
				conditionStr.indexOf("$") + 1, conditionStr.lastIndexOf("$"));
		//reportCalCondResult 是结果是否通知业务系统参数 ，不参与线条组件计算
		if("reportCalCondResult".equals(paramName)){
			return true;
		}
		String conditionVal = conditionStr.substring(
				conditionStr.indexOf("=") + 1, conditionStr.length());
		String paramVal = processParamService.queryProcessParam(
				LongHelper.valueOf(processInstanceId), paramName);
		if (paramVal != null && conditionVal.equalsIgnoreCase(paramVal)) {
			return true;
		}
		return false;
	}

	/**
	 * 得到目标节点信息
	 * 
	 * @param processInstance
	 *            ProcessInstanceDto
	 * @throws IOException
	 * @throws DocumentException
	 * @throws SQLException
	 * @throws WMWorkflowException
	 * @throws UOSException
	 */
	private String getExTargetActivityId(ProcessInstanceDto processInstance)
			throws FastflowException {
		String targetActivityId;
		try {
			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processInstance
							.getProcessDefineId());
			String exActivityId = process.getExceptionActivityId();
			if (exActivityId == null) {
				throw new RuntimeException("异常环节不存在！");
			}
			targetActivityId = processAttrService.getProcessAttr(
					processInstance.getProcessInstanceId(), exActivityId,
					ProcessAttrService.TARGETACTIVITYID);
			if ("".equalsIgnoreCase(targetActivityId)) {
				targetActivityId = null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return targetActivityId;
	}

	/**
	 * 执行反向变迁
	 * 
	 * @param activity
	 * @param activityInstance
	 * @param processInstance
	 * @param areaId
	 * @param useDB
	 * @return
	 */
	private void executeReverseTransitions(Activity activity,
			ActivityInstanceDto activityInstance,
			ProcessInstanceDto processInstance, String areaId,
			Map<String, String> flowPassMap, boolean useDB) {
		String processInstanceId = processInstance.getProcessInstanceId()
				.toString();
		/** 设置流程实例状态为ROLLBACK */
		int state = processInstance.getState();
		if (state != WMProcessInstanceState.OPEN_RUNNING_ROLLBACK_INT) {
			processInstance
					.setState(WMProcessInstanceState.OPEN_RUNNING_ROLLBACK_INT);
			processInstanceService
					.updateProcessInstance(processInstance, useDB);
		}
		ActivityInstanceDto targetActivityInstance = getTargetActivityInstance(processInstance);
		if (targetActivityInstance == null) {
			/** 目标活动实例未找到，调用业务层方法[通知定单变更] */
			flowPassMap = resetFlowPassMap(
					processInstance.getProcessInstanceId(), flowPassMap);
			workflowStateReport.reportProcessState(
					Long.valueOf(processInstanceId), activity.getId(),
					CommonDomain.WM_CHANGE_REPORT, flowPassMap,null,areaId);
			/** 作废流程 */
			processInstance.setState(WMProcessInstanceState.CLOSED_ABORTED_INT);
			processInstance.setCompletedDate(DateHelper.getTimeStamp());
			processInstanceService
					.updateProcessInstance(processInstance, useDB);
		} else {
			String targetActivityId = targetActivityInstance
					.getActivityDefinitionId();
			List<ActivityInstanceDto> fromActivityInstances = getFromActivityInstances(
					activity, activityInstance, targetActivityInstance,
					processInstance, false, areaId, useDB);
			executeFromActivityInstances(fromActivityInstances,
					activityInstance, processInstance, targetActivityId, false,
					areaId, flowPassMap, useDB);
		}
	}

	/**
	 * 
	 * 执行反向流转找到的下一步活动实例
	 * 
	 * @param fromActivityInstances
	 * @param activityInstance
	 * @param processInstance
	 * @param targetActivityId
	 * @param isReverseSubFlow
	 * @param areaId
	 * @return
	 */
	private void executeFromActivityInstances(
			List<ActivityInstanceDto> fromActivityInstances,
			ActivityInstanceDto activityInstance,
			ProcessInstanceDto processInstance, String targetActivityId,
			boolean isReverseSubFlow, String areaId,
			Map<String, String> flowPassMap, boolean useDB) {
		String processInstanceId = processInstance.getProcessInstanceId()
				.toString();
		WorkflowProcess process = processDefinitionService
				.findWorkflowProcessById(processInstance.getProcessDefineId());

		Iterator<ActivityInstanceDto> iterator = fromActivityInstances
				.iterator();
		// add by ji.dong 2012-11-05 ur:86940
		Long workItembatchId = workItemService.getWorkItemBatchNo();

		while (iterator.hasNext()) {
			/**
			 * 对每个下一步正向活动实例，如果已经达到目标环节且目标环节不是开始节点 检查是否存在对应的反向活动实例，如果没有，创建反向活动实例
			 */
			/** 获得正向活动实例 */
			ActivityInstanceDto fromActivityInstance = iterator.next();
			String fromActivityInstanceId = StringHelper
					.valueOf(fromActivityInstance.getId());
			if(fromActivityInstanceId != null){
				/** 获得正向活动 */
				Activity fromActivity = process
						.getActivityById(fromActivityInstance
								.getActivityDefinitionId());

			    logger.info("---------fromActivityInstanceId:" + fromActivityInstanceId);
				ActivityInstanceDto newActivityInstance = null;

				/** 判断是否是到达目标且类型不是折返，如果是，创建正向活动实例，否则创建反向活动实例 */
				boolean isReachedTarget = !isReverseSubFlow
						&& targetActivityId.equalsIgnoreCase(fromActivity.getId())
						&& !targetActivityId.equalsIgnoreCase(process
								.getStartActivity().getId());
				String exActivityId = process.getExceptionActivityId();
				String startMode = processAttrService.getProcessAttr(
						LongHelper.valueOf(processInstanceId), exActivityId,
						ProcessAttrService.STARTMODE);
				if ("".equalsIgnoreCase(startMode)) {
					startMode = null;
				}

				/** 是否待装折返 */
				boolean isWait = startMode == null ? false : WMAutomationMode.WAIT
						.equalsIgnoreCase(startMode);
				/** 是否待装自动 */
				boolean isWaitToAuto = startMode == null ? false : WMAutomationMode.WAIT_TO_AUTO
						.equalsIgnoreCase(startMode);
				/** add by 陈智堃 2010-05-13 UR-54984 begin */
				/** 是否缓装折返 */
				boolean isPause = startMode == null ? false : WMAutomationMode.PAUSE
						.equalsIgnoreCase(startMode);
				/** 是否缓装自动 */
				boolean isPauseToAuto = startMode == null ? false : WMAutomationMode.PAUSE_TO_AUTO
						.equalsIgnoreCase(startMode);
				/** add by 陈智堃 2010-05-13 UR-54984 end */
				/** 是否折返 */
				boolean isReturnBack = startMode == null ? false : WMAutomationMode.RETURNBACK
						.equalsIgnoreCase(startMode);
				/** 是否手工 */
				boolean isManual = startMode == null ? false : WMAutomationMode.MANUAL
						.equalsIgnoreCase(startMode);
				/** 是否改单折返（有通知） */
				boolean isChangeReturnBack = startMode == null ? false : WMAutomationMode.CHANGERETURNBACK
						.equalsIgnoreCase(startMode);
				/** 是否自动 */
				boolean isAutomatic = startMode == null
						|| WMAutomationMode.AUTOMATIC.equalsIgnoreCase(startMode);

				logger.info("----isReachedTarget:"+isReachedTarget +",---startMode:"+startMode);
				if (isReachedTarget
						&& ((isAutomatic || isManual || isWaitToAuto || isPauseToAuto))) {
					/** 到达目标且类型是自动或手工或者目标节点并行节点,创建正向活动实例 */
					newActivityInstance = findCreateActivityInstance(fromActivity,
							processInstance, areaId, useDB);
				} else {
					// 创建反向活动实例
					newActivityInstance = findCreateReverseActivityInstance(
							fromActivity, processInstance, fromActivityInstance,
							useDB);
				}

				/** 建立当前(反向)活动实例到该反向活动实例的变迁 */
				List<Transition> findTransitions = process
						.getTransitionsByToActivityId(activityInstance
								.getActivityDefinitionId());

				Iterator<Transition> transIter = findTransitions.iterator();
				Transition currentTransiton = null;
				while (transIter.hasNext()) {
					currentTransiton = transIter.next();
					if (currentTransiton.getFromActivity().getId()
							.equalsIgnoreCase(fromActivity.getId())) {
						break;
					}
				}

				// 要判断是否创建线条，如果当初正向时由于条件不成立未未创建，则反向也不应创建。
				String tranDefId = ""; // 备份线条ID,currentTransiton对象无法克隆，很麻烦。
				String tranName = ""; // 备份线条名称。

				/**
				 * mod by 陈智堃 2010-04-21 UR-54109 这里加上了是否创建线条的判断之后，各地均出现回退过程中卡单的问题
				 * 经分析流程数据，都是在合并节点到分支上的环节的反向线条没有生成导致的，所以需要去掉这个判断，
				 * 改为在getFromActivityInstances方法中对空线条进行特殊处理
				 */
				if (!fromActivityInstance.getActivityDefinitionId()
						.equalsIgnoreCase(
								activityInstance.getActivityDefinitionId())) {
					List<TransitionInstanceDto> transitionInstanceDtos = null;
					if (activityInstance.getDirection().equals(
							ActivityInstanceDto.NORMAL_DIRECTION)) {
						transitionInstanceDtos = transitionInstanceService
								.findTransitionInstancesByFromAndTo(
										processInstanceId,
										fromActivityInstance.getId(),
										activityInstance.getId());
					} else {
						transitionInstanceDtos = transitionInstanceService
								.findTransitionInstancesByFromAndTo(
										processInstanceId,
										fromActivityInstance.getId(),
										activityInstance.getReverse());
					}

					if (transitionInstanceDtos.size() == 1) { // 两个活动实例之间最多只有一根线条实例。
						// 注意，如果是跳转线则transitiondefinitionid为空值。
						if ((transitionInstanceDtos.get(0)
								.getTransitionDefinitionId() == null)
								|| ("".equalsIgnoreCase(transitionInstanceDtos.get(
										0).getTransitionDefinitionId()))) {
							tranDefId = currentTransiton.getId(); // 备份起来。
							tranName = currentTransiton.getName(); //
							currentTransiton.setId("");
							currentTransiton.setName("跳转线的反向线条");
						}
					}
				}

				transitionInstanceService.createTransitionInstance(processInstance,
						newActivityInstance, activityInstance, currentTransiton,
						TransitionInstanceDto.REVERSE_DIRECTION, useDB);

				// 恢复回去，由于currentTransiton无法克隆，而且又是指针引用，所以很麻烦。
				if ("".equalsIgnoreCase(currentTransiton.getId())) {
					currentTransiton.setId(tranDefId);
					currentTransiton.setName(tranName);
				}

				/**
				 * 从TransitionInstance表中检查以反向活动实例(在目标环节为正向活动实例)为终点的反向转移数(direction=0
				 * ) 与以起点活动实例为起点的正向转移数(direction=1)是否相同
				 */
				boolean bContinue = false; // 是否继续
				if (ActivityInstanceDto.NORMAL_DIRECTION
						.equalsIgnoreCase(newActivityInstance.getDirection())) {
					/* 由于有“回退的目标节点一定不会是路由节点”为前提，可以认为如果newActivityInstance为正向则流程开始转折。 */
					bContinue = true; // 如果正好是目标节点反向单回单产生正向单，肯定要继续。不判断正反向线条数是否相同。
				} else {
					int normal = transitionInstanceService.countTransitionInstance(
							processInstanceId, fromActivityInstanceId, null,
							TransitionInstanceDto.NORMAL_DIRECTION);
					int reverse = transitionInstanceService
							.countTransitionInstance(processInstanceId, null,
									newActivityInstance.getId().toString(),
									TransitionInstanceDto.REVERSE_DIRECTION);
					if (normal == reverse) {
						/** "以反向活动实例为终点的转移和以原正向活动实例为起点的转移个数相同 */
						bContinue = true;
					} else if (normal > reverse) {
						/**
						 * add by 陈智堃 2010-08-31 UR-56038
						 * 当存在控制节点，并且控制节点有控制线条绕过某些环节时，如果从控制线条下方的环节发起退单到控制线条绕过的环节中，
						 * 然后再从该环节发起退单到控制节点前面的时候
						 * ，由于第一次退单的时候控制线条并没有生成反向线条，导致流程回退到控制节点时 由于正反向线条数量不等而出现卡住的现象
						 * 
						 * ———————————— | | 例如：环节1——》控制节点——》环节2——》环节3——》环节4
						 * 
						 * 当环节4退单到环节2，环节2再退单到环节1，流程反向流转到控制节点时，由于4退到2的时候，控制线条不需要产生反向线
						 * 而2退到1时，控制线条也产生不了反向线，所以导致流程卡死
						 * 
						 * 解决思路：判断当前节点是否控制节点，如果是，判断由该控制节点的正向活动实例出发的控制线条的目标环节是否已经拆掉（
						 * 状态为7）， 且没有生成对应的反向线条，如果是，则生成反向控制线条
						 * 
						 * 存在问题：假如环节4退单到环节2，然后再正向流转到环节4，再退单到环节2，环节2再退单到环节1时，由于第一次4——
						 * 》2的
						 * 过程中，控制线条没有生成反向的；而按这个思路去做，2——》1的时候，反向的控制线条的起点将是第一个环节3的拆单
						 * ，而不是 第二个环节3的拆单，这样就导致了流程实例图上，反向的控制节点的位置不对，不过并不影响流程的流转，
						 * 再次正向也没有任何问题
						 * 纯粹是实例图显示的问题。这个问题要彻底解决很麻烦，反正不影响使用，先放着，现场如果要求改了再算了。
						 */
						// 当判断到正向线条数量大于反向线条数量时，先判断当前节点是不是控制节点
						if (fromActivity.isControl()) {
							// 是否需要重新计算
							boolean needReCount = false;
							/** 取出控制节点的所有出边定义 */
							Iterator<Transition> transitions = fromActivity
									.getEfferentTransitions().iterator();
							// 遍历每条出边
							while (transitions.hasNext()) {
								Transition controlTransition = transitions.next();
								// 判断该线条是否控制线条
								if (controlTransition.isControl()) { // 是控制线条，如果正向线条的终点已经拆掉（状态为7），且终点的反向活动实例已完成（状态为5），

									List<TransitionInstanceDto> tInsts = transitionInstanceService
											.findTransitionInstancesByFromActivity(
													processInstanceId,
													LongHelper
															.valueOf(fromActivityInstanceId));

									String reverseAId = "";

									// StringBuffer sql = new
									// StringBuffer("SELECT A1.REVERSE ")
									// .append("FROM UOS_TRANSITIONINSTANCE T JOIN UOS_ACTIVITYINSTANCE A1 ")
									// .append("			ON T.TOACTIVITYINSTANCEID=A1.ACTIVITYINSTANCEID ")
									// .append("  	  JOIN UOS_ACTIVITYINSTANCE A2 ON A1.REVERSE=A2.ACTIVITYINSTANCEID ")
									// .append("WHERE A1.STATE=7 AND A2.STATE=5 AND T.DIRECTION='1' AND T.FROMACTIVITYINSTANCEID=? ")
									// .append("	   AND T.PROCESSINSTANCEID=? AND T.TRANSITIONDEFINITIONID=? ");

									// 上面sql的翻译：一个SQL变多个SQL了。需要考究下
									for (int i = 0; i < tInsts.size(); i++) {
										TransitionInstanceDto tDto = tInsts.get(i);
										if (tDto.getDirection()
												.equals(TransitionInstanceDto.NORMAL_DIRECTION)
												&& controlTransition
														.getId()
														.equals(tDto
																.getTransitionDefinitionId())) {
											ActivityInstanceDto toActDto = activityInstanceService
													.queryActivityInstance(tDto
															.getToActivityInstanceId()
															.toString());
											ActivityInstanceDto toRevActDto = activityInstanceService
													.queryActivityInstance(toActDto
															.getReverse()
															.toString());
											if (toActDto.getState() == WMActivityInstanceState.ARCHIVED_INT
													&& toRevActDto.getState() == WMActivityInstanceState.CLOSED_COMPLETED_INT) {
												reverseAId = toActDto.getReverse()
														.toString();
												break;
											}
										}
									}

									if (!"".equals(reverseAId)) { // 计算控制线条的反向线条有没有生成
										// sql = new
										// StringBuffer("SELECT COUNT(*) ")
										// .append("FROM UOS_TRANSITIONINSTANCE T ")
										// .append("WHERE T.PROCESSINSTANCEID=? AND T.DIRECTION='0' AND T.FROMACTIVITYINSTANCEID=? ")
										// .append("	   AND T.TOACTIVITYINSTANCEID=? AND T.TRANSITIONDEFINITIONID=? ");
										// 翻译去掉了TRANSITIONDEFINITIONID的条件，应该不会有影响把，待考究
										int count = transitionInstanceService
												.countTransitionInstance(
														processInstanceId,
														reverseAId,
														newActivityInstance.getId()
																.toString(),
														TransitionInstanceDto.REVERSE_DIRECTION);
										// 如果没有生成，则补上该反向线条
										if (count == 0) {
											ActivityInstanceDto fromAct = activityInstanceService
													.queryActivityInstance(reverseAId);

											transitionInstanceService
													.createTransitionInstance(
															processInstance,
															newActivityInstance,
															fromAct,
															controlTransition,
															TransitionInstanceDto.REVERSE_DIRECTION,
															useDB);
											needReCount = true;
										}
									}
								}
							}

							// 需要重新计算
							if (needReCount) { // 重新计算反向线条的数量
								reverse = transitionInstanceService
										.countTransitionInstance(
												processInstanceId,
												null,
												newActivityInstance.getId()
														.toString(),
												TransitionInstanceDto.REVERSE_DIRECTION);
								if (normal == reverse) {
									/** "以反向活动实例为终点的转移和以原正向活动实例为起点的转移个数相同 */
									bContinue = true;
								}
							}
						}
						// add by 陈智堃 2010-08-31 UR-56038 end
					}
				}
				logger.info("---bContinue:"+bContinue+",isReachedTarget:"+isReachedTarget+",isReturnBack:"+isReturnBack);
				if (bContinue) {
					/** "以反向活动实例为终点的转移和以原正向活动实例为起点的转移个数相同 */
					if (isReachedTarget) {
						if (!isReturnBack && !isChangeReturnBack && !isWait
								&& !isPause) {
							/**
							 * 到达目标且类型不是折返，不是改单折返，不是待装折返（到达目标点，且为自动，人工或待装自动）
							 * 到达目标环节且目标环节不是起始节点， 以原正向活动实例为终点找起点集合,
							 * 为每个起点创建起点到新正向活动实例的边
							 */
							Iterator<TransitionInstanceDto> transIterator = transitionInstanceService
									.findTransitionInstancesByToActivity(
											processInstanceId,
											fromActivityInstance.getId())
									.iterator();
							while (transIterator.hasNext()) {
								TransitionInstanceDto trans = transIterator.next();
								if (trans
										.getDirection()
										.trim()
										.equals(TransitionInstanceDto.NORMAL_DIRECTION)) {
									trans.setState(WMTransitionInstanceState.ARCHIVED_INT);
									transitionInstanceService
											.updateTransitionInstance(trans, useDB);
									TransitionInstanceDto newTrans = trans.clone();
									if(newTrans != null){
										newTrans.setToActivityInstanceId(newActivityInstance
												.getId());
										newTrans.setId(null);
										newTrans.setState(WMTransitionInstanceState.ENABLED_INT);
										// 设置Action,手工添加的线，否则判断是否可达有问题
										newTrans.setAction(WMTransitionInstanceState.COMPENSATE_ACTION);
										transitionInstanceService
												.createTransitionInstance(newTrans,
														useDB);
									}
								}
							}

							/** 必须归档终点,保证终点的正向CLOSED活动实例唯一 */
							fromActivityInstance
									.setState(WMActivityInstanceState.ARCHIVED_INT);
							activityInstanceService.updateActivityInstance(
									fromActivityInstance, useDB);
							/** 恢复流程实例为正向运行状态 */
							processInstance
									.setState(WMProcessInstanceState.OPEN_RUNNING_INT);
							processInstanceService.updateProcessInstance(
									processInstance, useDB);
							/** 退单结束通知 */
							// mod by 陈智堃 2010-05-14 缓装自动没必要调退单结束通知
							// 待装自动也不调退单结束通知了，本来就会调待装退单结束通知了
							if (!isPauseToAuto && !isWaitToAuto) {
								flowPassMap = resetFlowPassMap(
										processInstance.getProcessInstanceId(),
										flowPassMap);
								workflowStateReport.reportProcessState(
										Long.valueOf(processInstanceId), "",
										CommonDomain.WM_ROLLBACKTONOMAL_REPORT,
										flowPassMap,null,areaId);
							}
						} else if (isWait) {
							/** 待装折返，修改启动方式为待装自动 */
							processAttrService.setProcessAttr(
									LongHelper.valueOf(processInstanceId),
									exActivityId, ProcessAttrService.STARTMODE,
									WMAutomationMode.WAIT_TO_AUTO, false);
						}
						// add by 陈智堃 2010-05-13 UR-54984
						else if (isPause) {
							/** 待装折返，修改启动方式为待装自动 */
							processAttrService.setProcessAttr(
									LongHelper.valueOf(processInstanceId),
									exActivityId, ProcessAttrService.STARTMODE,
									WMAutomationMode.PAUSE_TO_AUTO, false);
						} else if (isReturnBack) {
							/** 待装折返，修改启动方式为待装自动 */
							processAttrService.setProcessAttr(
									LongHelper.valueOf(processInstanceId),
									exActivityId, ProcessAttrService.STARTMODE,
									WMAutomationMode.AUTOMATIC, false);
							logger.info("====修改startMode为AUTOMATIC====");
						} else if (isChangeReturnBack) {
							/** 待装折返，修改启动方式为待装自动 */
							processAttrService.setProcessAttr(
									LongHelper.valueOf(processInstanceId),
									exActivityId, ProcessAttrService.STARTMODE,
									WMAutomationMode.MANUAL, false);
						}
					}
					if (!isReachedTarget || isAutomatic || isManual || isWaitToAuto
							|| isPauseToAuto) {
						/**
						 * 未到达目标或自动、人工、待装自动 将原正向活动实例为起点的边全部ARCHIVED
						 */
						Iterator<TransitionInstanceDto> tranIter = transitionInstanceService
								.findTransitionInstancesByFromActivity(
										processInstanceId,
										fromActivityInstance.getId()).iterator();
						while (tranIter.hasNext()) {
							/** 归档转移线，使之前的转移和活动实例逻辑删除 */
							TransitionInstanceDto trans = tranIter.next();
							trans.setState(WMTransitionInstanceState.ARCHIVED_INT);
							transitionInstanceService.updateTransitionInstance(
									trans, useDB);
						}
					}
					if (isReachedTarget && !isReturnBack && !isChangeReturnBack
							&& !isWait && !isPause) { // 到达目标且类型不是折返
						/** 清空运行时信息中的目标环节和startMode */
						processAttrService.setProcessAttr(
								LongHelper.valueOf(processInstanceId),
								exActivityId, ProcessAttrService.TARGETACTIVITYID,
								"", useDB);
						processAttrService.setProcessAttr(
								LongHelper.valueOf(processInstanceId),
								exActivityId, ProcessAttrService.STARTMODE, "",
								useDB);

						/** 到达目标且类型不是折返，不是改单折返，不是待装折返 */
						if (startMode != null
								&& startMode
										.equalsIgnoreCase(WMAutomationMode.WAIT_TO_AUTO)) {
							/** 到达目标环节且启动方式为待装自动,通知定单并挂起流程 */
							flowPassMap = resetFlowPassMap(
									processInstance.getProcessInstanceId(),
									flowPassMap);
							workflowStateReport.reportProcessState(
									Long.valueOf(processInstanceId),
									targetActivityId,
									CommonDomain.WM_WAITROLLBACK_REPORT, flowPassMap,null,areaId);
							/** 挂起流程 */
							suspendProcessInstance(processInstanceId, areaId, useDB,flowPassMap);
							return;
						}
						/** add by 陈智堃 2010-05-13 UR-54984 */
						if (startMode != null
								&& startMode
										.equalsIgnoreCase(WMAutomationMode.PAUSE_TO_AUTO)) {
							/** 到达目标环节且启动方式为缓装自动,通知定单并挂起流程 */
							flowPassMap = resetFlowPassMap(
									processInstance.getProcessInstanceId(),
									flowPassMap);
							workflowStateReport.reportProcessState(
									Long.valueOf(processInstanceId),
									targetActivityId,
									CommonDomain.WM_ROLLBACK_REPORT, flowPassMap,null,areaId);
							/** 挂起流程 */
							suspendProcessInstance(processInstanceId, areaId, useDB,flowPassMap);
							return;
						}
						if (startMode != null
								&& startMode
										.equalsIgnoreCase(WMAutomationMode.MANUAL)) {
							/** 到达目标环节且启动方式为人工方式,通知定单并作废流程 */
							flowPassMap = resetFlowPassMap(
									processInstance.getProcessInstanceId(),
									flowPassMap);
							workflowStateReport.reportProcessState(
									Long.valueOf(processInstanceId),
									targetActivityId,
									CommonDomain.WM_CHANGE_REPORT, flowPassMap,null,areaId);
							/** 作废流程 */
							processInstance
									.setState(WMProcessInstanceState.CLOSED_ABORTED_INT);
							processInstance.setCompletedDate(DateHelper
									.getTimeStamp());
							processInstanceService.updateProcessInstance(
									processInstance, useDB);
							return;
						}

					}
					// add by ji.dong 2012-11-05 ur:86940
					newActivityInstance.setBatchid(workItembatchId);

					/** 激活活动实例 */
					enableActivityInstance(fromActivity, processInstance,
							newActivityInstance, areaId, flowPassMap, useDB);

					/**
					 * 原正向活动实例归档,保证取得正向活动实例的唯一性（拆单不允许退单，退单对唯一性有要求）
					 * 对折返或退单折返或待装折返，到达目标时不归档
					 */
					if (!(isReachedTarget && (isReturnBack || isChangeReturnBack
							|| isWait || isPause))) {
						/** 没到目标节点，或者 到了目标节点不是折返（自动就不用原正向活动实例，所以需要归档） */
						fromActivityInstance
								.setState(WMActivityInstanceState.ARCHIVED_INT);
						activityInstanceService.updateActivityInstance(
								fromActivityInstance, useDB);
					}
				}
			}
		}
	}

	/**
	 * 得到或创建反向活动实例
	 * 
	 * @param fromActivityInstance
	 *            ActivityInstanceDto 当前活动实例
	 * @param activity
	 *            Activity 反向流转的下一个活动
	 * @param processInstance
	 *            ProcessInstanceDto
	 * @param activityInstance
	 *            ActivityInstanceDto 反向流转的下一个活动的正向活动实例
	 * @return ActivityInstanceDto
	 */
	private ActivityInstanceDto findCreateReverseActivityInstance(
			Activity fromActivity, ProcessInstanceDto processInstance,
			ActivityInstanceDto fromActivityInstance /* 正向活动实例 */, boolean useDB) {
		ActivityInstanceDto reverseActivityInstance = null;
		/** activityInstance是反向流转的下一个活动的正向活动实例 */
		String reverseActivityInstanceId = StringHelper
				.valueOf(fromActivityInstance.getReverse());
		if (reverseActivityInstanceId == null) {
			reverseActivityInstance = activityInstanceService
					.createActivityInstance(fromActivity, processInstance,
							ActivityInstanceDto.REVERSE_DIRECTION, useDB);
			// 将反向活动实例记入正向活动实例的运行时信息中
			fromActivityInstance.setReverse(reverseActivityInstance.getId());
			activityInstanceService.updateActivityInstance(
					fromActivityInstance, useDB);
			reverseActivityInstance.setReverse(fromActivityInstance.getId());
			activityInstanceService.updateActivityInstance(
					reverseActivityInstance, useDB);
		} else {
			/** 反向活动实例存在 */
			reverseActivityInstance = activityInstanceService
					.queryActivityInstance(reverseActivityInstanceId);
		}
		return reverseActivityInstance;
	}

	/**
	 * 创建活动实例,如果JoinAndOr的活动实例没有创建的话
	 * 
	 * @param fromActivity
	 * @param processInstance
	 * @param object
	 * @param areaId
	 * @return
	 */
	private ActivityInstanceDto findCreateActivityInstance(Activity activity,
			ProcessInstanceDto processInstance, String areaId, boolean useDB) {
		String processInstanceId = processInstance.getProcessInstanceId()
				.toString();
		ActivityInstanceDto activityInstance = null;
		List<ActivityInstanceDto> activityInstances = null;
		if (isJoinAndOr(activity)) {
			activityInstances = activityInstanceService
					.queryActivityInstancesByState(
							processInstanceId,
							activity.getId(),
							String.valueOf(WMActivityInstanceState.OPEN_INITIATED_INT),
							ActivityInstanceDto.NORMAL_DIRECTION);
			if (activityInstances.size() != 0) {
				activityInstance = activityInstances.get(0);
			}
		}
		if (activityInstance == null) {
			activityInstance = activityInstanceService.createActivityInstance(
					activity, processInstance,
					ActivityInstanceDto.NORMAL_DIRECTION, useDB);
		}
		return activityInstance;
	}

	/**
	 * 获得反向流转的下一步活动实例集合
	 * 
	 * @param activity
	 *            当前活动
	 * @param activityInstance
	 *            当前活动实例
	 * @param targetActivityInstance
	 *            目标活动实例
	 * @param processInstance
	 *            流程实例
	 * @param isReverseSubFlow
	 *            是否反向子流程
	 * @param areaId
	 * @param useDB
	 * @return
	 */
	private List<ActivityInstanceDto> getFromActivityInstances(
			Activity activity, ActivityInstanceDto activityInstance,
			ActivityInstanceDto targetActivityInstance,
			ProcessInstanceDto processInstance, boolean isReverseSubFlow,
			String areaId, boolean useDB) {
		List<ActivityInstanceDto> canFromActivityInstances = new ArrayList<ActivityInstanceDto>();
		/**
		 * 如果当前活动实例与目标活动实例的活动定义相同，直接返回目标活动实例
		 * 此情况当且仅当启动方式为折返且已到达目标环节并将启动方式改为自动时才会产生
		 */
		if (targetActivityInstance != null
				&& targetActivityInstance.getActivityDefinitionId()
						.equalsIgnoreCase(activity.getId())) {
			canFromActivityInstances.add(targetActivityInstance);
			return canFromActivityInstances;
		}

		// 当前活动是否合并节点
		boolean isRelation = activity.isRelation();

		/** 获得当前活动的正向活动实例 */
		String normalActivityInstanceId = null;
		ActivityInstanceDto normalActivityInstance = null;
		if (activityInstance.getDirection().trim()
				.equalsIgnoreCase(ActivityInstanceDto.REVERSE_DIRECTION)) {
			/** 从运行时信息中找当前的反向活动实例对应的正向活动实例 */
			activityInstance = activityInstanceService
					.queryActivityInstance(activityInstance.getId().toString());
			if (activityInstance.getReverse() != null) {
				normalActivityInstanceId = activityInstance.getReverse()
						.toString();
				normalActivityInstance = activityInstanceService
						.queryActivityInstance(normalActivityInstanceId);
			}
		} else {
			/** 取当前正向活动实例 */
			normalActivityInstanceId = String.valueOf(activityInstance.getId());
			normalActivityInstance = activityInstance;
		}
		/** 获得流程实例 */
		String processInstanceId = activityInstance.getProcessInstanceId()
				.toString();

		/**
		 * 查找以正向活动实例为目标的变迁，找出起点活动实例集合 查找方案是: 先找出流程定义中以当前活动为终点的非反向控制线的边,找出起点活动集合
		 * 对每个起点活动,找其活动实例,并判断该活动实例到当前活动实例对应的正向活动实例是否可达, 如果可达,放入起点活动实例集合
		 * 如果未找到(可能是跳转而来),查找以当前活动实例为终点的起点活动实例集合
		 */
		/** 取出当前活动的所有入边 */
		Iterator<Transition> transitions = activity.getAfferentTransitions()
				.iterator();
		while (transitions.hasNext()) {
			Transition currentTransition = transitions.next();
			/** 判断转移是否是非反向控制线，反向控制线不处理，为了防止循环 */
			
			if (getDirectionFromTransition(currentTransition) == 1) {
				Activity fromActivity = currentTransition.getFromActivity();

				/** 查找目标节点已完成的实例 */
				List<ActivityInstanceDto> findActivityInstances = findMustRollBackActIns(
						processInstanceId, fromActivity.getId(), areaId); // 反向流转时,获取某活动需要产生反向活动实例的正向活动实例
				logger.info("----查找目标节点已完成的实例,findActivityInstances:"+findActivityInstances.size());
				// 一次性过滤所有和目标节点不可达的活动实例，不再逐个判断，提高性能
				canReached(targetActivityInstance, findActivityInstances,
						processInstance);
				logger.info("----过滤后目标节点已完成的实例,findActivityInstances:"+findActivityInstances.size());
				
				/** 获得流转的下一节点的已完成的正向活动实例的个数 */
				if (findActivityInstances.size() > 0) {
					Iterator<ActivityInstanceDto> findIter = findActivityInstances
							.iterator();
					while (findIter.hasNext()) {
						ActivityInstanceDto findActivityInstance = findIter
								.next();
						/**
						 * 如果是控制线,判断到normalActivityInstance是否有边,否则判断到
						 * normalActivityInstance是否可达（这个情况适用于正向控制线）
						 */
						if (currentTransition.isControl()) {
							/** 控制线条 */
							int count = transitionInstanceService
									.countTransitionInstance(
											processInstanceId,
											String.valueOf(findActivityInstance
													.getId()),
											String.valueOf(normalActivityInstance
													.getId()),
											TransitionInstanceDto.NORMAL_DIRECTION);
							if (count > 0) {
								canFromActivityInstances
										.add(findActivityInstance);
							}
						} else {
							/** 普通线条 */
							if (isCanReached(findActivityInstance,
									normalActivityInstance, processInstance)) {
								/**
								 * add by 陈智堃 2010-04-21 UR-54109
								 * 如果流程定义中有连接并行节点与合并节点的空线条，在原来的逻辑里，
								 * 无论正向的时候空线条有没有生成，在查找合并节点的下一步活动集合里都总会把合并节点找出来
								 * 需要增加特殊判断，如果当前节点是合并节点，且下一步活动是并行节点，那么需要再判断之间
								 * 有没有线条连接
								 */
								if (isRelation && fromActivity.isParallel()) {
									// 计算并行节点到合并节点的转移实例数量
									int count = transitionInstanceService
											.countTransitionInstance(
													processInstanceId,
													String.valueOf(findActivityInstance
															.getId()),
													normalActivityInstanceId,
													TransitionInstanceDto.NORMAL_DIRECTION);
									// count>0，即并行节点到合并节点之间有空线条
									if (count > 0)
										canFromActivityInstances
												.add(findActivityInstance);
								} else {
									canFromActivityInstances
											.add(findActivityInstance);
								}
							}
						}
					}
				}
			} // end正向
		} // end所有入边
		logger.info("-----canFromActivityInstances.size:"+canFromActivityInstances.size());
		if (canFromActivityInstances.size() == 0) {
			List<TransitionInstanceDto> trans = transitionInstanceService
					.findTransitionInstancesByToActivity(processInstanceId,
							LongHelper.valueOf(normalActivityInstanceId));
			Iterator<TransitionInstanceDto> tranIter = trans.iterator();
			while (tranIter.hasNext()) {
				TransitionInstanceDto tran = tranIter.next();
				String fromActivityInstanceId = tran
						.getFromActivityInstanceId().toString();
				// 判断目标活动实例到跳转起点活动实例是否可达
				ActivityInstanceDto fromActivityInstance = activityInstanceService
						.queryActivityInstance(fromActivityInstanceId);
				boolean isCanReached = true;
				if (!isReverseSubFlow) {
					isCanReached = isCanReached(targetActivityInstance,
							fromActivityInstance, processInstance);
				}
				if (isCanReached) {
					canFromActivityInstances.add(activityInstanceService
							.queryActivityInstance(fromActivityInstanceId));
				}
			}
		}
		return canFromActivityInstances;
	}

	/**
	 * 判断两个活动实例之间是否可达
	 * 
	 * @param fromActivityInstance
	 * @param toActivityInstance
	 * @param processInstance
	 * @return
	 */
	private boolean isCanReached(ActivityInstanceDto fromActivityInstance,
			ActivityInstanceDto toActivityInstance,
			ProcessInstanceDto processInstance) {
		// add by sujf 2008.10.31 begin
		if (fromActivityInstance.getId().longValue() == toActivityInstance
				.getId().longValue()) {
			return true; // 如果ID相等,就是判断自己和自己是否可达,当然可达.
		}
		// add by sujf 2008.10.31.end
		// add by 陈智堃 2010-10-08 如果起点是开始节点，则肯定可达，直接返回true begin
		if ("开始节点".equals(fromActivityInstance.getName())) { // 虽然这样直接判断名字的方法土了一点，但如果按正规的来查询活动定义再判断活动类别，那太麻烦了，所以还是用土方法算了^_^
			return true;
		}
		// add by 陈智堃 2010-10-08 end

		Set<String> canReachedActivityInstanceIds = findCanRollbackActivityInstanceIds(
				String.valueOf(fromActivityInstance.getId()),
				String.valueOf(processInstance.getProcessInstanceId()));
		if (canReachedActivityInstanceIds.contains(String
				.valueOf(toActivityInstance.getId()))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 过滤toActivityInstances集合中和fromActivityInstance不可达的活动实例
	 * 
	 * @param fromActivityInstance
	 * @param toActivityInstances
	 * @param processInstance
	 * @throws WMWorkflowException
	 * @throws UOSException
	 */
	private void canReached(ActivityInstanceDto fromActivityInstance,
			List<ActivityInstanceDto> toActivityInstances,
			ProcessInstanceDto processInstance) {
		Set<String> canReachedActivityInstanceIds = findCanRollbackActivityInstanceIds(
				String.valueOf(fromActivityInstance.getId()),
				String.valueOf(processInstance.getProcessInstanceId()));
		logger.info("----canReachedActivityInstanceIds.size():"+canReachedActivityInstanceIds.size());
		logger.info("---canReachedActivityInstanceIds:"+GsonHelper.toJson(canReachedActivityInstanceIds));
		Iterator<ActivityInstanceDto> iter = toActivityInstances.iterator();
		while (iter.hasNext()) {
			ActivityInstanceDto toActivityInstance = iter.next();
			logger.info("---toActivityInstance.getId:"+toActivityInstance.getId());
			if (!canReachedActivityInstanceIds.contains(String
					.valueOf(toActivityInstance.getId())))
				iter.remove();
		}
	}

	// 反向流转时,获取某活动需要产生反向活动实例的正向活动实例.
	private List<ActivityInstanceDto> findMustRollBackActIns(
			String processInstanceId, String activityId, String areaId) {
		List<ActivityInstanceDto> list = activityInstanceService
				.queryActivityInstancesByState(
						processInstanceId, // 这句是原来的逻辑。
						activityId,
						String.valueOf(WMActivityInstanceState.CLOSED_COMPLETED_INT),
						ActivityInstanceDto.NORMAL_DIRECTION);

		// 如果“回滚”转移线实例ID有值，则相当于此活动实例已经被产生了反向的活动实例，不能再产生一次，所以就要被剔除。
		for (int i = list.size() - 1; i >= 0; i--) { // 由于是删除单元，所以要倒着循环。
			ActivityInstanceDto actIns = list.get(i);
			// 查找此活动实例在XML中的回滚转移线标识。
			String rollbackTranInsId = actIns.getRollbackTranins();
			if (!StringHelper.isEmpty(rollbackTranInsId)) { // 是否有值。
				list.remove(i);
			}
		}
		return list;
	}

	/**
	 * 得到转移的方向(普通线条默认为正向,控制线条根据起点和终点的索引来判断)
	 * 
	 * @param tran
	 *            Transition
	 * @return int
	 */
	private int getDirectionFromTransition(Transition tran) {
		int direction = 1;
		if (tran.isControl()) {
			int fromNodeIndex = Integer.valueOf(tran.getFromActivity()
					.getNodeIndex());
			int toNodeIndex = Integer.valueOf(tran.getToActivity()
					.getNodeIndex());
			if (fromNodeIndex > toNodeIndex) {
				direction = 0;
			}
		}
		return direction;
	}

	/**
	 * 获得目标环节的正向CLOSED_COMPLETED活动实例
	 * 
	 * @param processInstance
	 * @return
	 */
	private ActivityInstanceDto getTargetActivityInstance(
			ProcessInstanceDto processInstance) {
		String processInstanceId = processInstance.getProcessInstanceId()
				.toString();
		String targetActivityId = getTargetActivityId(processInstance);
		/** 找目标环节对应的正向活动实例(closed.completed and direction=1) */
		List<ActivityInstanceDto> targetActivityInstances = activityInstanceService
				.queryActivityInstancesByState(
						processInstanceId,
						targetActivityId,
						String.valueOf(WMActivityInstanceState.CLOSED_COMPLETED_INT),
						ActivityInstanceDto.NORMAL_DIRECTION);
		if (targetActivityInstances.size() == 0) {
			return null;
		}
		return targetActivityInstances.get(0);
	}

	/**
	 * 获得目标环节的活动定义
	 * 
	 * @param processInstance
	 * @return
	 */
	private String getTargetActivityId(ProcessInstanceDto processInstance) {
		String targetActivityId = null;
		/** 在流程定义中找到异常处理活动 */
		WorkflowProcess process = processDefinitionService
				.findWorkflowProcessById(processInstance.getProcessDefineId());
		String exActivityId = process.getExceptionActivityId();
		targetActivityId = processAttrService.getProcessAttr(
				processInstance.getProcessInstanceId(), exActivityId,
				ProcessAttrService.TARGETACTIVITYID);
		if (targetActivityId == null || "".equalsIgnoreCase(targetActivityId)) {
			/** 目标环节未发现,默认为开始节点 */
			targetActivityId = process.getStartActivity().getId();
		}
		return targetActivityId;
	}

	/**
	 * 查找可以回滚的工作项
	 * 
	 * @param processInstanceId
	 * @param reasonConfigId 
	 * @param reasonId 
	 * @return
	 */
	public List<WorkItemDto> findCanRollBackWorkItems(String processInstanceId) {
		return workItemService.queryWorkItemsByProcess(processInstanceId,
				WMWorkItemState.OPEN_RUNNING_INT, null);
	}

	/**
	 * 查找可以回滚的工作项
	 * 
	 * @param processInstanceId
	 * @param reasonConfigId 
	 * @param reasonId 
	 * @return
	 */
	public List<WorkItemDto> findCanRollBackWorkItems(String processInstanceId,String disabledWorkitemId, String reasonCode, String reasonConfigId,boolean useDB,String areaId) {
		return findCanRollBackWorkItems(processInstanceId,
				WMWorkItemState.OPEN_RUNNING_INT, disabledWorkitemId,reasonCode,reasonConfigId,useDB,areaId);
	}
	/**
	 * 查找可回滚的工作项
	 * 
	 * @param processInstanceId
	 * @param state
	 * @param disabledWorkitemId
	 * @param reasonConfigId 
	 * @param reasonCode 
	 * @return
	 */
	public List<WorkItemDto> findCanRollBackWorkItems(String processInstanceId,
			int state, String disabledWorkitemId, String reasonCode, String reasonConfigId,boolean useDB,String areaId) {

        List<WorkItemDto> workItemIds = new ArrayList<WorkItemDto>();
		/**获取发起退单的工作项Dto*/
        WorkItemDto disableWorkItem = workItemService.queryWorkItem(disabledWorkitemId);
        /**获取发起退单的活动实例*/
        ActivityInstanceDto disableActivityInstance = activityInstanceService.queryActivityInstance(
            String.valueOf(disableWorkItem.getActivityInstanceId()));
        /**获得活动定义ID*/
        String startActivityId = disableWorkItem.getActivityDefinitionId();
        /**获得流程实例*/
        ProcessInstanceDto processInstance = processInstanceService.queryProcessInstance(processInstanceId);
        /**获得流程定义*/
        WorkflowProcess process = processDefinitionService.findWorkflowProcessById(processInstance.getProcessDefineId());
        if (process == null) {
			throw new FastflowException("流程定义不存在："
					+ processInstance.getProcessDefineId());
        }
        String targetActivityId = null;
        boolean hasReason = (reasonConfigId != null || reasonCode != null);
		// 存在异常原因
		if (hasReason) {
			targetActivityId = setRollbackInfo(processInstance,
					reasonConfigId, reasonCode, startActivityId, process,
					useDB,areaId);
		} else {
			// 不存在异常原因
			String exActivityId = process.getExceptionActivityId();
			if (exActivityId == null) {
				throw new FastflowException("异常活动节点不存在！请检查XPDL");
			}
			
			// 从流程属性里面查找流程的目标节点
			targetActivityId = processAttrService.getProcessAttr(
					processInstance.getProcessInstanceId(), exActivityId,
					ProcessAttrService.TARGETACTIVITYID);
			if (StringHelper.isEmpty(targetActivityId)) {
				targetActivityId = process.getStartActivity().getId();
			}
		}
		logger.info("----目标节点id,targetActivityId:"+targetActivityId);

        /**查找流程中处于Running状态的工作项*/
        List<WorkItemDto> runningWorkItems = workItemService.queryWorkItemsByProcess(processInstanceId,
				state, disabledWorkitemId);
        if (runningWorkItems != null) {

    		if (targetActivityId == null || targetActivityId.trim().length() == 0) {
                Iterator<WorkItemDto> iter = runningWorkItems.iterator();
                while (iter.hasNext()) {
                    workItemIds.add(iter.next());
                }
            }else{

                /**得到目标环节的正向活动实例(closed.completed and direction=1)*/
                List<ActivityInstanceDto> targetInstances = new ArrayList<ActivityInstanceDto>(); //可达disableActivityInstance的目标环节的活动实例集合
                List<ActivityInstanceDto> targetActivityInstances = activityInstanceService.queryActivityInstancesByState(
                    processInstanceId, targetActivityId,
                    String.valueOf(WMActivityInstanceState.CLOSED_COMPLETED_INT),
                    ActivityInstanceDto.NORMAL_DIRECTION);
                if (targetActivityInstances.size() > 0) {
                    Iterator<ActivityInstanceDto> iter = targetActivityInstances.iterator();
                    while (iter.hasNext()) {
                        ActivityInstanceDto targetActivityInstance = iter.next();
                        if (isCanReached(targetActivityInstance, disableActivityInstance, processInstance)) {
                            targetInstances.add(targetActivityInstance);
                        }
                    }
                }

                /**对每个处于运行态的工作项循环,判断其是否从目标活动实例可达*/
                Iterator<WorkItemDto> workItemIter = runningWorkItems.iterator();
                while (workItemIter.hasNext()) {
                    /**对每个工作项找其对应的活动实例ID*/
                    WorkItemDto workItem = workItemIter.next();
                    ActivityInstanceDto activityInstance = activityInstanceService.queryActivityInstance(
                        String.valueOf(workItem.getActivityInstanceId()));
                    Iterator<ActivityInstanceDto> targetIter = targetInstances.iterator();
                    while (targetIter.hasNext()) {
                        ActivityInstanceDto targetActivityInstance = targetIter.next();
                        if (isCanReached(targetActivityInstance, activityInstance, processInstance)) {
                            workItemIds.add(workItem);
                        }
                    }
                }
            }
        }
		return workItemIds;
	}

	/**
	 * 撤单
	 * 
	 * @param processInstanceId
	 * @param targetActivityId
	 * @param startMode
	 * @param areaId
	 * @param useDB
	 */
	public void rollbackProcessInstance(String processInstanceId,
			String targetActivityId, String startMode, String areaId,Map<String, String> flowPassMap,
			boolean useDB) {
		ProcessInstanceDto processInstance = processInstanceService
				.queryProcessInstance(processInstanceId);

		/** 获得流程定义 */
		WorkflowProcess process = processDefinitionService
				.findWorkflowProcessById(processInstance.getProcessDefineId());

		/** 在流程定义中找到异常处理活动 */
		if (startMode == null) { // 默认为自动模式
			startMode = WMAutomationMode.AUTOMATIC;
		}

		String exActivityId = process.getExceptionActivityId();

		// 由于流程回退到开始节点时，会根据有没有目标节点区分是撤单还是退单到CRM，
		// 无目标节点，则为撤单流程，不把目标节点写入到流程数据中，以免流程结束时定单状态错误写为“退单到CRM”
		// 默认退回开始节点
		if (targetActivityId == null || targetActivityId.trim().equals("0")
				|| targetActivityId.trim().length() == 0) {
			targetActivityId = process.getStartActivity().getId();
		}
//		processAttrService.setProcessAttr(
//				LongHelper.valueOf(processInstanceId), exActivityId,
//				ProcessAttrService.TARGETACTIVITYID, targetActivityId, useDB);
		processAttrService.setProcessAttr(
				LongHelper.valueOf(processInstanceId), exActivityId,
				ProcessAttrService.STARTMODE, startMode, useDB);

		/** 得到目标活动实例 */
		List<ActivityInstanceDto> targetActivityInstances = activityInstanceService
				.queryActivityInstancesByState(
						processInstanceId,
						targetActivityId,
						String.valueOf(WMActivityInstanceState.CLOSED_COMPLETED_INT),
						ActivityInstanceDto.NORMAL_DIRECTION);

		if (targetActivityInstances.size() <= 0) {
			return;
		}

		String state = WMActivityInstanceState.OPEN_INITIATED_INT + ","
				+ WMActivityInstanceState.OPEN_RUNNING_INT + ","
				+ WMActivityInstanceState.OPEN_SUSPENDED_INT;

		List<ActivityInstanceDto> openActivityInstances = activityInstanceService
				.queryActivityInstancesByState(processInstanceId, null, state,
						ActivityInstanceDto.NORMAL_DIRECTION);

		if (openActivityInstances.size() > 0) {
			ActivityInstanceDto activityInstanceDto = targetActivityInstances.get(0);
			/** 修改流程状态为运行状态 */
			processInstance.setState(WMProcessInstanceState.OPEN_RUNNING_INT);
			processInstanceService
					.updateProcessInstance(processInstance, useDB);

			/** 获得从目标环节活动实例可达的正向活动实例ID集合 */
			Set<String> canReachedActivityInstanceIds = findCanRollbackActivityInstanceIds(
					activityInstanceDto.getId().toString(), processInstanceId);

			fix2ndReturn(canReachedActivityInstanceIds, processInstance,
					process, areaId, useDB);

			/** 对每个活动实例 */
			for (int i = 0; i < openActivityInstances.size(); i++) {
				ActivityInstanceDto activityInstance = openActivityInstances
						.get(i);
				Activity activity = process.getActivityById(activityInstance
						.getActivityDefinitionId());
				String activityInstanceId = activityInstance.getId().toString();
				if (canReachedActivityInstanceIds.contains(activityInstanceId)) {
					/** 正向活动实例CLOSE_ABORTED */
					activityInstance
							.setState(WMActivityInstanceState.CLOSED_ABORTED_INT);
					activityInstance
							.setCompletedDate(DateHelper.getTimeStamp());
					activityInstanceService.updateActivityInstance(
							activityInstance, useDB);

					/** add by 陈智堃 2010-03-17 归档活动实例的同时，应该把对应的工作项也作废 begin */
					if (activityInstance.getWorkItemId() != null) {
						WorkItemDto workItem = workItemService
								.queryWorkItem(activityInstance.getWorkItemId()
										.toString());
						/** 设置工作项目Dto信息 */
						workItem.setState(WMWorkItemState.DISABLED_INT);
						workItem.setCompletedDate(DateHelper.getTimeStamp());
						workItemService.updateWorkItem(workItem, useDB);
					}
					/** add by 陈智堃 2010-03-17 end */

					/** 执行反向活动实例的转移 */
					executeReverseTransitions(activity, activityInstance,
							processInstance, areaId, flowPassMap, useDB);
				}
			}
		}
	}

	/**
	 * 终止流程
	 * 
	 * @param processInstanceId
	 * @param areaId
	 */
	public void terminateProcessInstance(String processInstanceId,
			String areaId, boolean useDB) {
		try {
			// 查询流程实例数据
			ProcessInstanceDto processInstance = processInstanceService
					.queryProcessInstance(processInstanceId);
			if (processInstance == null) {
				throw new FastflowException("流程实例不存在[processInstanceId="
						+ processInstanceId + "]");
			}
			// 判断流程实例状态决定能否终止
			WMProcessInstanceState.states()[processInstance.getState()]
					.checkTransition(WMProcessInstanceState.CLOSED_TERMINATED,
							true);
			// 更新状态
			processInstance.setState(WMProcessInstanceState.CLOSED_TERMINATED
					.intValue());
			// 更新时间
			processInstance.setCompletedDate(DateHelper.getTimeStamp());
			processInstanceService
					.updateProcessInstance(processInstance, useDB);
			// 暂时不加入活动实例、路由实例和子流程的状态转变
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}

	/**
	 * 废弃流程
	 * 
	 * @param processInstanceId
	 * @param areaId
	 * @param useDB
	 */
	public void abortProcessInstance(String processInstanceId, String areaId,
			boolean useDB) {
		try {
			// 查询流程实例数据
			ProcessInstanceDto processInstance = processInstanceService
					.queryProcessInstance(processInstanceId);
			if (processInstance == null) {
				throw new FastflowException("流程实例不存在[processInstanceId="
						+ processInstanceId + "]");
			}
			// 判断流程实例状态决定能否abort
			WMProcessInstanceState.states()[processInstance.getState()]
					.checkTransition(WMProcessInstanceState.CLOSED_ABORTED,
							true);
			// 更新流程状态为abort
			processInstance.setState(WMProcessInstanceState.CLOSED_ABORTED
					.intValue());
			processInstanceService
					.updateProcessInstance(processInstance, useDB);
			
			//add by che.zi 2016-0607 作废当前环节实例和工作项
			List<ActivityInstanceDto> activityInstanceDtos = activityInstanceService.queryActivityInstancesByState(processInstanceId, null, StringHelper.valueOf(WMActivityInstanceState.OPEN_RUNNING.intValue()),null);
			for(ActivityInstanceDto activityInstanceDto:activityInstanceDtos){
				activityInstanceDto.setState(WMActivityInstanceState.CLOSED_ABORTED.intValue());
				activityInstanceService.updateActivityInstance(activityInstanceDto, useDB);
			}
			List<WorkItemDto> workItemDtos = workItemService.queryWorkItemsByProcess(processInstanceId, WMWorkItemState.OPEN_RUNNING.intValue(),null);
			for(WorkItemDto workItemDto:workItemDtos){
				workItemDto.setState(WMWorkItemState.CLOSED_ABORTED.intValue());
				workItemService.updateWorkItem(workItemDto, useDB);
			}
			//end by che.zi 2016-0607
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}

	/**
	 * 挂起流程实例
	 * 
	 * @param processInstanceId
	 * @param areaId
	 * @param useDB
	 */
	public void suspendProcessInstance(String processInstanceId, String areaId,
			boolean useDB,Map<String,String> flowPassMap) {
		try {
			// 查询流程实例数据
			ProcessInstanceDto processInstance = processInstanceService
					.queryProcessInstance(processInstanceId);
			if (processInstance == null) {
				throw new FastflowException("流程实例不存在[processInstanceId="
						+ processInstanceId + "]");
			}
			// 判断流程实例状态决定能否挂起
			WMProcessInstanceState.states()[processInstance.getState()]
					.checkTransition(
							WMProcessInstanceState.OPEN_NOTRUNNING_SUSPENDED,
							true);
//			// 更新流程状态为abort
//			processInstance
//					.setState(WMProcessInstanceState.OPEN_NOTRUNNING_SUSPENDED
//							.intValue());
//			//add by che.zi 20160721
//			// 增加挂起时间
//			processInstance.setSuspendDate(DateHelper.getTimeStamp());
//			//end20160721
//			processInstanceService
//					.updateProcessInstance(processInstance, useDB);
			// 暂时不加入活动实例、路由实例和子流程的状态转变（挂起流程不能把线条禁止掉，不然以后回退有问题）
			cascadeProcessInstanceState(processInstance,
	                WMProcessInstanceState.OPEN_NOTRUNNING_SUSPENDED,
					true,
	                WMActivityInstanceState.OPEN_SUSPENDED,
					false,
	                WMTransitionInstanceState.ENABLED,	// 挂起流程不能把线条禁止掉，不然以后回退有问题
	                false,
					WMWorkItemState.OPEN_SUSPENDED,
					false,
					false,useDB);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}

	/**
	 * 更改流程实例的状态
	 * @param processDTO 流程实例对象
     * @param newProcessState 新的流程实例状态
     * @param throwProcessException 是否抛出流程变迁异常
     * @param newActivityState 新的活动实例状态
     * @param throwActivityException 是否抛出活动变迁异常
     * @param newTransitionState 新的活动变迁状态
     * @param throwTransitionException 是否抛出活动变迁的变迁异常
     * @param newWorkItemState 新的工作项状态
     * @param throwWorkItemException 是否抛出工作项变迁异常
     * @param forceTransitions 是否强迫变迁（引擎调用就是强迫调用）
     * @param useDB 是否操作数据库
	 */
	private void cascadeProcessInstanceState(
			ProcessInstanceDto processInstance,
			WMProcessInstanceState newProcessState, boolean throwProcessException,
			WMActivityInstanceState newActivityState, boolean throwActivityException,
			WMTransitionInstanceState newTransitionState, boolean throwTransitionException,
			WMWorkItemState newWorkItemState, boolean throwWorkItemException, boolean forceTransitions,boolean useDB) {
		 try {
	            int oldProcessState = processInstance.getState();
	            //如果状态前后一样，那么无需修改
	            if (oldProcessState != newProcessState.intValue()) {
	                int action = WMProcessInstanceState.states()[oldProcessState]
	    					.checkTransition(
	    							newProcessState,
	    							throwProcessException && !forceTransitions);
	                if (action == WMProcessInstanceState.ILLEGAL_ACTION && forceTransitions) {
	                    action = WMProcessInstanceState.FORCED_ACTION;
	                }
	                // If the transition is valid, apply the state change.
	                if (action != WMProcessInstanceState.ILLEGAL_ACTION) {
	                    //设置状态
	                	processInstance.setState(newProcessState.intValue());
	                    //设置时间
	                    if (newProcessState.isClosed()) {
	                    	processInstance.setCompletedDate(DateHelper.getTimeStamp());
	                    }
	                    processInstance.setSuspendDate(DateHelper.getTimeStamp());
	                    processInstanceService.updateProcessInstance(processInstance,useDB);
	                    List<ActivityInstanceDto> coll = activityInstanceService.queryActivityInstanceByPid(processInstance.getProcessInstanceId());
	                    Iterator<ActivityInstanceDto> iterator = coll.iterator();
	                    while (iterator.hasNext()) {
	                        ActivityInstanceDto activityDTO = (ActivityInstanceDto) iterator.next();
	                        cascadeActivityInstanceState(activityDTO, newActivityState, throwActivityException,
	                            newTransitionState, throwTransitionException, newWorkItemState,
	                            throwWorkItemException, forceTransitions,useDB);
	                    }
	                }
	            }
	        }catch (FastflowException ex) {
	            throw ex;
	        }catch (Exception ex) {
	            throw new FastflowException("------cascadeProcessInstanceState-修改流程状态异常：", ex);
	        }
	}

	/**
	 * 更改活动实例的状态
     * @param activityDTO ActivityInstanceDto 活动实例对象
     * @param newActivityState WMActivityInstanceState 新的活动实例状态
     * @param throwActivityException boolean 是否抛出活动变迁异常
     * @param newTransitionState WMTransitionInstanceState 新的变迁实例状态
     * @param throwTransitionException boolean 是否抛出变迁异常
     * @param newWorkItemState WMWorkItemState 新的工作项实例状态
     * @param throwWorkItemException boolean 是否抛出工作项异常
     * @param forceTransitions boolean 是否强迫变迁（引擎调用就是强迫调用）
	 * @param useDB
	 */
	private void cascadeActivityInstanceState(ActivityInstanceDto activityDTO,
			WMActivityInstanceState newActivityState,
			boolean throwActivityException,
			WMTransitionInstanceState newTransitionState,
			boolean throwTransitionException, WMWorkItemState newWorkItemState,
			boolean throwWorkItemException, boolean forceTransitions,boolean useDB) {
		try {
			int oldActivityState = activityDTO.getState();
			// 状态如果前后一样，那么无需修改.
			if (oldActivityState != newActivityState.intValue()) {
				int action = WMActivityInstanceState.states()[oldActivityState]
    					.checkTransition(newActivityState,throwActivityException && !forceTransitions);
				if (action == WMActivityInstanceState.ILLEGAL_ACTION
						&& forceTransitions) {
					action = WMActivityInstanceState.FORCED_ACTION;
				}
				// If the transition is valid, apply the state change.
				if (action != WMActivityInstanceState.ILLEGAL_ACTION) {
					// 设置状态
					activityDTO.setState(newActivityState.intValue());
					if (newActivityState.isClosed()
							|| newActivityState.isDisabled()) {
						activityDTO.setCompletedDate(DateHelper.getTimeStamp());
					}
					activityInstanceService.updateActivityInstance(activityDTO,useDB);
					// 处理子流程的情况 -- 暂时不处理
					
					// 如果不是强迫变迁，那么通知引擎
					String processInstanceId = activityDTO
							.getProcessInstanceId().toString();
					String activityInstanceId = activityDTO.getId().toString();
					// 处理活动变迁，如果给定新的状态为空，那么无需处理
					if (newTransitionState != null) {
						List<TransitionInstanceDto> transitionColl = transitionInstanceService.findTransitionInstancesByToActivity(
										processInstanceId, LongHelper.valueOf(activityInstanceId));
						Iterator<TransitionInstanceDto> iterator = transitionColl.iterator();
						while (iterator.hasNext()) {
							TransitionInstanceDto transitionInstanceDTO = (TransitionInstanceDto) iterator
									.next();
							cascadeTransitionInstanceState(
									transitionInstanceDTO, newTransitionState,
									throwTransitionException, forceTransitions,useDB);
						}
					}

					// 处理工作项
					WorkItemDto workItemDTO = workItemService.qryWorkItemByActInstId(LongHelper.valueOf(activityInstanceId));
					if(workItemDTO != null){
						cascadeWorkItemState(workItemDTO, newWorkItemState,
								throwWorkItemException, forceTransitions,useDB);
					}
				}
			}
		} catch (FastflowException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new FastflowException("----cascadeActivityInstanceState----修改活动实例状态异常：", ex);
		}
	}

	/**
	 * 修改工作项状态
	 * @param workItemDTO
	 * @param newWorkItemState
	 * @param throwWorkItemException
	 * @param forceTransitions
	 * @param useDB
	 */
	private void cascadeWorkItemState(WorkItemDto workItemDTO,
			WMWorkItemState newState, boolean throwWorkItemException,
			boolean forceTransitions, boolean useDB) {
		try {
			int oldState = workItemDTO.getState();
			// 状态如果前后一样，那么无需修改.
			if (oldState != newState.intValue()) {
				int action = WMWorkItemState.states()[oldState].checkTransition(newState,
						throwWorkItemException && !forceTransitions);
				if (action == WMWorkItemState.ILLEGAL_ACTION
						&& forceTransitions) {
					action = WMWorkItemState.FORCED_ACTION;
				}
				// If the transition is valid, apply the state change.
				if (action != WMWorkItemState.ILLEGAL_ACTION) {
					// 设置工作项状态
					workItemDTO.setState(newState.intValue());
					// 如果新状态是关闭或是废弃状态，那么设置完成时间
					if (newState.isClosed() || newState.isDisabled()) {
						// 如果老状态处于open或是超时状态并且开始时间为空，那么设置开始时间
						if ((workItemDTO.getStartedDate() == null)
								&& WMWorkItemState.states()[oldState].isOpen()) {
							workItemDTO.setStartedDate(DateHelper.getTimeStamp());
						}
						workItemDTO.setCompletedDate(DateHelper.getTimeStamp());
					}
					workItemService.updateWorkItem(workItemDTO,useDB);
					// 如果不是强迫变迁，那么通知引擎
				}
			}
		} catch (FastflowException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new FastflowException("---修改工作项状态异常----", ex);
		}
	}

	/**
	 * 修改线条状态
	 * @param transitionInstanceDTO
	 * @param newTransitionState
	 * @param throwTransitionException
	 * @param forceTransitions
	 * @param useDB
	 */
	private void cascadeTransitionInstanceState(
			TransitionInstanceDto transitionInstance,
			WMTransitionInstanceState newTransitionState,
			boolean throwTransitionException, boolean forceTransitions,
			boolean useDB) {
		try {
			int oldState = transitionInstance.getState();
			// 状态如果前后一样，那么无需修改.
			if (oldState != newTransitionState.intValue()) {
				int action = WMTransitionInstanceState.states()[oldState].checkTransition(newTransitionState,
						throwTransitionException && !forceTransitions);
				if (action == WMTransitionInstanceState.ILLEGAL_ACTION
						&& forceTransitions) {
					action = WMTransitionInstanceState.FORCED_ACTION;
				}
				// If the transition is valid, apply the state change.
				if (action != WMTransitionInstanceState.ILLEGAL_ACTION) {
					// 设置状态
					transitionInstance.setState(newTransitionState.intValue());
					transitionInstance.setLastDate(DateHelper.getTimeStamp());
					transitionInstanceService
							.updateTransitionInstance(transitionInstance,useDB);
					// 如果不是强迫变迁，那么通知引擎
				}
			}
		} catch (FastflowException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new FastflowException("----修改线条实例状态异常-----", ex);
		}
	}

	/**
	 * 恢复流程实例
	 * 
	 * @param processInstanceId
	 * @param run
	 * @param areaId
	 * @param useDB
	 */
	public void resumeProcessInstance(String processInstanceId, boolean run,
			String areaId, boolean useDB,Map<String,String> flowPassMap) {
		try {
			List<ActivityInstanceDto> activityInstances = null;
			// 查询流程实例数据
			ProcessInstanceDto processInstance = processInstanceService
					.queryProcessInstance(processInstanceId);
			if (processInstance == null) {
				throw new FastflowException("流程实例不存在[processInstanceId="
						+ processInstanceId + "]");
			}
			 /**取出WorkflowProcess对象*/
			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processInstance.getProcessDefineId());
			int state = processInstance.getState();
			if (state == WMProcessInstanceState.OPEN_NOTRUNNING_SUSPENDED_INT
					|| state == WMProcessInstanceState.OPEN_RUNNING_INT) {
                String sSuspendActIds = ",";
				if (state == WMProcessInstanceState.OPEN_NOTRUNNING_SUSPENDED_INT) {
					/**
					 * 如果流程为挂起状态,先处理处于挂起状态的活动实例(处理完挂起状态的活动实例后,
					 * 原先处于初始状态的活动实例可能会发生状态改变（并行节点等）
					 */
					// 更新流程状态为running
					processInstance
							.setState(WMProcessInstanceState.OPEN_RUNNING
									.intValue());

					//add by che.zi 20160725
					// 增加解挂时间
					processInstance.setResumeDate(DateHelper.getTimeStamp());
					if(FastflowConfig.useTimeLimit){
						//流程时限重新计算
						processInstance = this.reCalculateFlowLimit(processInstance);
						//将流程时限结果通知业务侧
					    Map<String,Object> map = new HashMap<String,Object>();
					    map.put("processInstanceId", processInstanceId);
					    map.put("areaId", areaId);
					    map.put("limitDate", DateHelper.parseTime(processInstance.getLimitDate()));
					    map.put("alertDate", DateHelper.parseTime(processInstance.getAlertDate()));
					    workflowStateReport.reportTimeLimit(map);
					}
					//end 20160725
					processInstanceService.updateProcessInstance(
							processInstance, useDB);
					//处理活动实例
					List<ActivityInstanceDto> suspendActivityInstances = activityInstanceService.queryActivityInstancesByState(
							processInstanceId,
							null,
							String.valueOf(WMActivityInstanceState.OPEN_SUSPENDED_INT),
							null);
					Iterator<ActivityInstanceDto> suspendIter = suspendActivityInstances.iterator();
					while (suspendIter.hasNext()) {
						ActivityInstanceDto suspendActivityInstance = (ActivityInstanceDto) suspendIter
								.next();
						Activity activity;
						activity = process.getActivityById(suspendActivityInstance
										.getActivityDefinitionId());

						if (!isJoinAndOr(activity)
								|| canEnableActivity(
										processInstanceId, activity)) {
							suspendActivityInstance
									.setCreateSource(new Long(1));
							 //激活活动实例。
                            sSuspendActIds = sSuspendActIds + suspendActivityInstance.getId() + ","; 
                            enableActivityInstance(activity, processInstance, suspendActivityInstance,areaId,flowPassMap,useDB);
						}
					}
				}
				 /**处理处于初始状态的活动实例,可能是处于同步等待中的*/
                activityInstances = activityInstanceService.queryActivityInstancesByState(processInstanceId, null,
                    String.valueOf(WMActivityInstanceState.OPEN_INITIATED_INT), null);
                Iterator<ActivityInstanceDto> iterator = activityInstances.iterator();
                while (iterator.hasNext()) {
                    ActivityInstanceDto activityInstance = (ActivityInstanceDto) iterator.next();
                    //由于改成待装激活后要判断同步，如果需要转同步的活动实例状态已在本函数的上一个enableSyn调用将状态改为了OPEN_INITIATED_INT
                    //所以这个活动实例在这里还会并再以此执行enableSyn导致SYN_COUNT值多加了1，所以要避免这种情况发生，加上判断。
                    if (sSuspendActIds.indexOf("," + String.valueOf(activityInstance.getId()) + ",") < 0) {
                        Activity activity;
                        activity = process.getActivityById(activityInstance.getActivityDefinitionId());

                        if (!isJoinAndOr(activity) || canEnableActivity(processInstanceId, activity)) {
                            //要同时激活同步等待于它的其他流程活动实例。
                            //如果流程的状态本来就不是挂起，但定单层还是调用了此函数，说明不是流程挂起激活，而是“强制激活”
                            //“强制激活”是为了使同步等待生效，所以不应该调用enableSyn而是要调用enableActivityInstance。
                            //因为enableSyn函数内部有判断同步关系如果校验不通过不会激动活动实例。
                            activityInstance.setCreateSource(new Long(1));
                            enableActivityInstance(activity, processInstance, activityInstance,areaId,flowPassMap,useDB);
                        }
                    }
                }
			}

			if(FastflowConfig.useTimeLimit){
				//重新计算环节时限
				List<WorkItemDto> workItems = workItemService.queryWorkItemsByProcess(processInstanceId, WMWorkItemState.OPEN_RUNNING_INT, null);
				if(workItems != null && workItems.size()>0){
					WorkItemDto workItem = workItems.get(0);
					workItem = this.reCalculateWorkItemTime(workItem,processInstance);
					workItemService.updateWorkItem(workItem, useDB);
					//将环节时限结果通知业务侧
					Map<String,Object> map = new HashMap<String,Object>();
				    map.put("processInstanceId", workItem.getProcessInstanceId().toString());
				    map.put("areaId", areaId);
				    map.put("limitDate", DateHelper.parseTime(workItem.getLimitDate()));
				    map.put("alertDate", DateHelper.parseTime(workItem.getAlertDate()));
				    map.put("tacheCode", workItem.getTacheCode());
				    map.put("workItemId", workItem.getWorkItemId());
				    workflowStateReport.reportTimeLimit(map);
				}
				//end 20160725
			}
			 //待装激活、缓装激活通知的逻辑要挪到前面，否则在定单层会漏掉对暂停定单库的处理并漏调通知CRM的组件。
            //通知定单流程已经激活
            workflowStateReport.reportProcessState(Long.valueOf(processInstanceId), "",
                                           CommonDomain.WM_RESUME_REPORT,flowPassMap,"",areaId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}
	private boolean canEnableActivity(String processInstanceId,
			Activity activity) {
		if (activity == null) {
			throw new FastflowException("---活动节点为空---");
		}
		LinkedList<String> openActivitys = new LinkedList<String>();
		LinkedList<String> judgedActivitys = new LinkedList<String>();

		try {
			traceActivityForEnable(openActivitys, judgedActivitys,
					processInstanceId, activity);
		} catch (Exception ex) {
			throw new FastflowException("----线条路由异常---",ex);
		}
		return openActivitys.size() == 0;
	}

	private void traceActivityForEnable(LinkedList<String> openActivitys,
			LinkedList<String> judgedActivitys, String processInstanceId,
			Activity activity) {
		if (!judgedActivitys.contains(activity.getId())) {
            judgedActivitys.add(activity.getId());
            List<Transition> list = activity.getAfferentTransitions();
            Iterator<Transition> iterator = list.iterator();
            while (iterator.hasNext()) {
                Transition transition = (Transition) iterator.next();
                Activity fromActivity = transition.getFromActivity();
                if (judgedActivitys.contains(fromActivity.getId())) {
                    continue;
                }
                List<ActivityInstanceDto> chunk = activityInstanceService.qryActivityInstances(processInstanceId,
                    fromActivity.getId());
                if (chunk.size() > 0) {
                    Iterator<ActivityInstanceDto> aiIterator = chunk.iterator();
                    while (aiIterator.hasNext()) {
                        ActivityInstanceDto aiDTO = (
                            ActivityInstanceDto)
                            aiIterator.next();
                        int state = aiDTO.getState();
                        if (WMActivityInstanceState.isOpen(state)) {
                            openActivitys.add(fromActivity.getId());
                            return;
                        }
                        else {
                            if (!isJoinAndOr(fromActivity)) {
                                //如果有XOR-JOIN，那么继续回溯
                                traceActivityForEnable(openActivitys,
                                    judgedActivitys,
                                    processInstanceId, fromActivity);
                            }
                        }
                    }
                }
                else {
                    //如果没有活动实例，那么继续回溯
                    traceActivityForEnable(openActivitys,
                                           judgedActivitys,
                                           processInstanceId, fromActivity);
                }
            }
        }
	}

	/**
	 * 重新计算流程时限（剔除流程挂起时间）
	 * @param processInstance
	 * @return
	 * @throws ParseException 
	 */
	private ProcessInstanceDto reCalculateFlowLimit(
			ProcessInstanceDto processInstance) throws ParseException {
		FlowLimitDto flowLimitDto = flowLimitService.qryFlowLimit(processInstance.getProcessDefineId(), processInstance.getAreaId());
		if(flowLimitDto != null){
			Date suspendDate = processInstance.getSuspendDate();
			Date resumeDate = processInstance.getResumeDate();
			Date alertDate = null;
			Date limitDate = null;
			//只计算工作日，需要剔除节假日和下班时间
			if(flowLimitDto.getIsWorkTime().equals("1")){
				//如果在挂起时已经超时，就不计算时限了(只计算挂起时间比限时要早的情况)
				if(processInstance.getAlertDate() != null && processInstance.getAlertDate().after(suspendDate)){
					alertDate = timeLimitClient.reCalculateWorkTime(processInstance.getStartedDate(), suspendDate, resumeDate,
    						flowLimitDto.getAlertValue().intValue(), flowLimitDto.getTimeUnit(),processInstance.getAreaId(), processInstance.getProcessDefineId());
				}
				if(processInstance.getLimitDate() != null && processInstance.getLimitDate().after(suspendDate)){
					limitDate = timeLimitClient.reCalculateWorkTime(processInstance.getStartedDate(), suspendDate, resumeDate,
							flowLimitDto.getLimitValue().intValue(), flowLimitDto.getTimeUnit(),processInstance.getAreaId(),processInstance.getProcessDefineId());
				}
			}else{
				//不计算工作日时，直接计算绝对时间
				CalendarUtil calendarUtil = CalendarUtil.getInstance();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				int timeUnit;

				if(TimeLimitClient.YEAR.equals(flowLimitDto.getTimeUnit()))
					timeUnit = CalendarUtil.YEAR;
				else if(TimeLimitClient.DAY.equals(flowLimitDto.getTimeUnit()))
					timeUnit = CalendarUtil.DAY;
				else
					timeUnit = CalendarUtil.MINUTE;	//即使时限单位是小时，也精确到分钟去算，不然相差50分钟，得出来的还是0小时

				String createDateStr = df.format(processInstance.getStartedDate());
				//计算挂起开始时间到解挂时间的间隔
				long interval = calendarUtil.calculateDateInterval(suspendDate, resumeDate, timeUnit);
				int newAlert;
				int newLimit;
				//如果时限单位是小时，需要换成分钟
				if(TimeLimitClient.HOUR.equals(flowLimitDto.getTimeUnit())){
					newAlert = (int)interval + flowLimitDto.getAlertValue().intValue()*60;
					newLimit = (int)interval + flowLimitDto.getLimitValue().intValue()*60;
				}else{
					newAlert = (int)interval + flowLimitDto.getAlertValue().intValue();
					newLimit = (int)interval + flowLimitDto.getLimitValue().intValue();
				}
				//在原有的时限基础上，加上时间间隔，算出新的时限
				String alertDateStr = calendarUtil.getPreDateTime(createDateStr, timeUnit, newAlert);
				String limitDateStr = calendarUtil.getPreDateTime(createDateStr, timeUnit, newLimit);

				alertDate = df.parse(alertDateStr);
				limitDate = df.parse(limitDateStr);
			}
			if(alertDate != null){
			    processInstance.setAlertDate(new Timestamp(alertDate.getTime()));
			}
			if(limitDate != null){
			    processInstance.setLimitDate(new Timestamp(limitDate.getTime()));
			}
		}
		return processInstance;
	}

	/**
	 * 工作项解挂时，需要重新计算工作项时限，把暂停期间的这段时间剔除
	 * @throws ParseException 
	 */
	public WorkItemDto reCalculateWorkItemTime(WorkItemDto workItem,ProcessInstanceDto processInstance) throws ParseException {
		TacheLimitDto tacheLimitDto = tacheLimitService.qryTacheLimitByByTAP(workItem.getTacheId(), workItem.getAreaId(), processInstance.getProcessDefineId());
		if(tacheLimitDto != null){
			Date suspendDate = processInstance.getSuspendDate();
			Date resumeDate = processInstance.getResumeDate();
			Date alertDate = null;
			Date limitDate = null;
			//只计算工作日，需要剔除节假日和下班时间
			if(tacheLimitDto.getIsWorkTime().equals("1")){
				//如果在挂起时已经超时，就不计算时限了(只计算挂起时间比限时要早的情况)
				if(workItem.getAlertDate() != null && workItem.getAlertDate().after(suspendDate)){
					alertDate = timeLimitClient.reCalculateWorkTime(workItem.getStartedDate(), suspendDate, resumeDate,
    						tacheLimitDto.getAlertValue().intValue(), tacheLimitDto.getTimeUnit(),workItem.getAreaId(), processInstance.getProcessDefineId());
				}
				if(workItem.getLimitDate() != null && workItem.getLimitDate().after(suspendDate)){
					limitDate = timeLimitClient.reCalculateWorkTime(workItem.getStartedDate(), suspendDate, resumeDate,
    						tacheLimitDto.getLimitValue().intValue(), tacheLimitDto.getTimeUnit(),workItem.getAreaId(),processInstance.getProcessDefineId());
				}
			}else{
				//不计算工作日时，直接计算绝对时间
				CalendarUtil calendarUtil = CalendarUtil.getInstance();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				int timeUnit;

				if(TimeLimitClient.YEAR.equals(tacheLimitDto.getTimeUnit()))
					timeUnit = CalendarUtil.YEAR;
				else if(TimeLimitClient.DAY.equals(tacheLimitDto.getTimeUnit()))
					timeUnit = CalendarUtil.DAY;
				else
					timeUnit = CalendarUtil.MINUTE;	//即使时限单位是小时，也精确到分钟去算，不然相差50分钟，得出来的还是0小时

				String createDateStr = df.format(workItem.getStartedDate());
				//计算挂起开始时间到解挂时间的间隔
				long interval = calendarUtil.calculateDateInterval(suspendDate, resumeDate, timeUnit);
				int newAlert;
				int newLimit;
				//如果时限单位是小时，需要换成分钟
				if(TimeLimitClient.HOUR.equals(tacheLimitDto.getTimeUnit())){
					newAlert = (int)interval + tacheLimitDto.getAlertValue().intValue()*60;
					newLimit = (int)interval + tacheLimitDto.getLimitValue().intValue()*60;
				}else{
					newAlert = (int)interval + tacheLimitDto.getAlertValue().intValue();
					newLimit = (int)interval + tacheLimitDto.getLimitValue().intValue();
				}
				//在原有的时限基础上，加上时间间隔，算出新的时限
				String alertDateStr = calendarUtil.getPreDateTime(createDateStr, timeUnit, newAlert);
				String limitDateStr = calendarUtil.getPreDateTime(createDateStr, timeUnit, newLimit);

				alertDate = df.parse(alertDateStr);
				limitDate = df.parse(limitDateStr);
			}
			if(alertDate != null){
				workItem.setAlertDate(alertDate);
			}
			if(limitDate != null){
				workItem.setLimitDate(limitDate);
			}
		}
		return workItem;
	}

	/**
	 * 流程实例跳转
	 * 
	 * @param processInstanceId
	 * @param fromActivityInstanceId
	 * @param toActivityId
	 * @param areaId
	 * @param useDB
	 */
	public void processInstanceJump(String processInstanceId,
			String fromActivityInstanceId, String toActivityId, String areaId,
			boolean useDB, Map<String, String> flowPassMap) {
		ProcessInstanceDto processInstance = processInstanceService.queryProcessInstance(processInstanceId);
		ActivityInstanceDto fromActivityInstance = activityInstanceService.queryActivityInstance(fromActivityInstanceId);
		try {
			if(processInstance.getState() == WMProcessInstanceState.OPEN_RUNNING_ROLLBACK_INT){
				logger.error("-----流程处于回滚中，无法跳转-----");
				return;
			}
			/**如果流程实例状态为调度异常，则将流程状态修改为执行中**/
			if(processInstance.getState() == WMProcessInstanceState.ERROR_INT){
				processInstance.setState(WMProcessInstanceState.OPEN_RUNNING_INT);
				processInstanceService.updateProcessInstance(processInstance, useDB);
			}
			/** Disable起始点的工作项和活动实例 */
			WorkItemDto workItem = workItemService.queryWorkItem(fromActivityInstance.getWorkItemId().toString());
			workItem.setState(WMWorkItemState.DISABLED_INT);
			workItem.setCompletedDate(DateHelper.getTimeStamp());
			workItemService.updateWorkItem(workItem,useDB);
			fromActivityInstance.setState(WMActivityInstanceState.DISABLED_INT);
			fromActivityInstance.setCompletedDate(DateHelper.getTimeStamp());
			fromActivityInstance.setItemCompleted(1);
			activityInstanceService.updateActivityInstance(fromActivityInstance,useDB);
			/** 废弃并行环节的其他工作项 */
			List<WorkItemDto> rollWorkItems = findCanRollBackWorkItems(processInstanceId);
			for(WorkItemDto workItemDto:rollWorkItems){
				workItemDto.setState(WMWorkItemState.DISABLED_INT);
				workItemDto.setCompletedDate(DateHelper.getTimeStamp());
				workItemService.updateWorkItem(workItemDto,useDB);
				
				
				ActivityInstanceDto activityInstanceDto = activityInstanceService.queryActivityInstance
							(workItemDto.getActivityInstanceId().toString());
				activityInstanceDto.setState(WMActivityInstanceState.DISABLED_INT);
				activityInstanceDto.setCompletedDate(DateHelper.getTimeStamp());
				activityInstanceService.updateActivityInstance(activityInstanceDto, useDB);
			}
			/** 获得toActivity */
			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processInstance.getProcessDefineId());
			/** mod by 陈智堃 2010-10-12 原子流程改造 如果传进来的目标原子活动ID不为空，需要特别处理 begin */
			Activity toActivity = process.getActivityById(toActivityId);
			/** mod by 陈智堃 2010-10-12 原子流程改造 end */

			/**
			 * 获得流程中目前所有正向活动实例 Collection allNormalActivityInstances =
			 * findActivityInstancesByState( processInstanceId, null,
			 * String.valueOf(WMActivityInstanceState.CLOSED_COMPLETED_INT),
			 * ActivityInstanceDto.NORMAL_DIRECTION); 获得流程中目标所有正向活动实例 Collection
			 * targetActivityInstacnes = findActivityInstancesByState(
			 * processInstanceId, toActivityId,
			 * String.valueOf(WMActivityInstanceState.CLOSED_COMPLETED_INT),
			 * ActivityInstanceDto.NORMAL_DIRECTION); Iterator iter =
			 * targetActivityInstacnes.iterator(); 目标曾经到达过的活动实例集合 Collection
			 * canReachedActivityInstanceIds = new ArrayList(); while
			 * (iter.hasNext()) { ActivityInstanceDto targetActivityInstacne =
			 * (ActivityInstanceDto) iter.next();
			 * 目标环节曾经到达过的活动实例，如果正向活动实例allNormalActivityInstances在这些活动实例中
			 * ，需要置为归档状态 canReachedActivityInstanceIds.addAll(
			 * findCanRollbackActivityInstanceIds(
			 * String.valueOf(targetActivityInstacne.getId()),
			 * String.valueOf(processInstanceId))); } Iterator alliter =
			 * allNormalActivityInstances.iterator(); while (alliter.hasNext())
			 * { //对每一条正在运行中的正向活动实例进行过滤 ActivityInstanceDto
			 * allNormalActivityInstance = (ActivityInstanceDto) alliter.next();
			 * if (canReachedActivityInstanceIds.contains(String.valueOf(
			 * allNormalActivityInstance.getId()))) {
			 * allNormalActivityInstance.setState
			 * (WMActivityInstanceState.ARCHIVED);
			 * activityInstanceDao.updateActivityInstance
			 * (allNormalActivityInstance);
			 * 
			 * } }
			 */
			/** 获得toActivityInstance */
			String direction = TransitionInstanceDto.NORMAL_DIRECTION;
			ActivityInstanceDto toActivityInstance = findCreateActivityInstance(toActivity, processInstance, null,useDB);

			/** 创建变迁 */
			/** 找以fromActivityIntance为终点的起点实例,创建每个起点到toActivityInstance的边 */
			List<TransitionInstanceDto> fromFromTrans = transitionInstanceService.findTransitionInstancesByToActivity(processInstanceId,
					LongHelper.valueOf(fromActivityInstanceId));
			for(int i = 0;i<fromFromTrans.size();i++){
				TransitionInstanceDto tran = fromFromTrans.get(i);
				/** disable原边实例 */
				tran.setState(WMTransitionInstanceState.DISABLED_INT);
				transitionInstanceService.updateTransitionInstance(tran,useDB);
				/** 创建新边 */
				String fromFromActivityInstanceId = tran.getFromActivityInstanceId().toString();
				ActivityInstanceDto fromFromActivityInstance = activityInstanceService.queryActivityInstance(fromFromActivityInstanceId);
				TransitionInstanceDto tranIns = transitionInstanceService
						.createTransitionInstance(processInstance, toActivityInstance, fromFromActivityInstance, null, direction, useDB); 

				setRollBackTranInsToActIns(processInstanceId, null, fromActivityInstance, toActivityInstance,
						tranIns,useDB);
			}

			Long workItembatchId = workItemService.getWorkItemBatchNo();
			toActivityInstance.setBatchid(workItembatchId);
			
			enableActivityInstance(toActivity, processInstance,
					toActivityInstance, areaId, flowPassMap, useDB);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}
	
	/**
	 * 如果是从大到小的回转线，要在“活动实例参数”中记录哪些活动实例被“回滚”掉了，有用。
	 * currentTransition如果为空表示是直接跳转直连线,没有转移线规格标识.
	 */
	private void setRollBackTranInsToActIns(String processInstanceId, Transition currentTransition
			,ActivityInstanceDto activityInstance, ActivityInstanceDto toActivityInstance
			,TransitionInstanceDto tranIns,Boolean useDB){
		if ((currentTransition == null) || (getDirectionFromTransition(currentTransition) == 0)) { // 如果目标节点nodeindex小于起始节点。或为跳转直连线.
			ActivityInstanceDto beginActivityInstance = activityInstanceService.queryActivityInstancesByStateDesc(processInstanceId, toActivityInstance.getActivityDefinitionId()
					, WMActivityInstanceState.CLOSED_COMPLETED_INT+"");			
			if (beginActivityInstance==null) {
				return;
			}
			
			//找个某个活动实例节点后面的所有活动实例
			List<ActivityInstanceDto> list = new ArrayList<ActivityInstanceDto>();
			queryAllActivityInstancesByFrom(list,beginActivityInstance.getId(),processInstanceId);
			if (currentTransition == null) { // 直接跳转线(手工拉线).
				boolean bFind = false; // 是否在beginActInsId后面节点中能找到activityInstance,如果能则说明发生了"回滚".
				for(int i = 0;i<list.size();i++){
					if (activityInstance.getId().longValue()==list.get(i).getId().longValue()) {
						bFind = true;
						break;
					}
				}
				if (!bFind) {
					return;
				}
			}
			
//			List<ActivityInstanceDto> signList = new ArrayList<>();
//			//计算某个活动实例到当前跳转实例之间的所有活动实例
//			for(int i = 0;i<list.size();i++){
//				if(true){
//					signList.add(list.get(i));
//				}
//			}
			
			// 开始设置流程实例XML中活动实例的回滚线实例标识
			for (int i = 0; i < list.size(); i++) {
				ActivityInstanceDto activityInstanceDto = list.get(i);
				activityInstanceDto.setRollbackTranins(tranIns.getId().longValue()+"");
				activityInstanceService.updateActivityInstance(activityInstanceDto, useDB);
			}
		}
	}
	
	private void  queryAllActivityInstancesByFrom(List<ActivityInstanceDto> resList,Long startActivityInstanceId,String processInstanceId){
		List<ActivityInstanceDto> list = activityInstanceService.queryActivityInstancesByFrom(startActivityInstanceId,processInstanceId);
		for(int i = 0;i<list.size();i++){
			if(!resList.contains(list.get(i))){
				resList.add(list.get(i));
				queryAllActivityInstancesByFrom(resList,list.get(i).getId(),processInstanceId);
			}
		}
	}


	/**
	 * 重新启动流程实例
	 * 
	 * @param processInstanceId
	 * @param areaId
	 * @param useDB
	 */
	public void restartProcessInstance(String processInstanceId, String areaId,
			boolean useDB) {
		// TODO Auto-generated method stub

	}

	public void persistProcessModel(ProcessModel persistProcessModel) {
		if (persistProcessModel == null) {
			return;
		}
		logger.info("进入方法persistProcessModel:"
				+ persistProcessModel.getProcessInstanceId());
		ProcessInstanceDto processInstanceDto = persistProcessModel
				.getProcessInstanceDto();
		if (processInstanceDto != null) {
			if (OperType.isInsert(processInstanceDto.getOperType())) {
				processInstanceService.createProcessInstance(
						processInstanceDto, true);
				logger.info("进入方法createProcessInstance");
			} else {
				processInstanceService.updateProcessInstance(
						processInstanceDto, true);
				logger.info("进入方法updateProcessInstance");
			}
		}

		List<ActivityInstanceDto> activityInstanceDtos = persistProcessModel
				.getActivityInstanceDtos();
		for (int i = 0; i < activityInstanceDtos.size(); i++) {
			ActivityInstanceDto activityInstanceDto = activityInstanceDtos
					.get(i);
			if (OperType.isInsert(activityInstanceDto.getOperType())) {
				activityInstanceService.createActivityInstance(
						activityInstanceDto, true);
				logger.info("进入方法createActivityInstance："
						+ activityInstanceDto.getId());
			} else {
				activityInstanceService.updateActivityInstance(
						activityInstanceDto, true);
				logger.info("进入方法updateActivityInstance："
						+ activityInstanceDto.getId());
			}
		}

		List<TransitionInstanceDto> transitionInstanceDtos = persistProcessModel
				.getTransitionInstanceDtos();
		for (int i = 0; i < transitionInstanceDtos.size(); i++) {
			TransitionInstanceDto transitionInstanceDto = transitionInstanceDtos
					.get(i);
			if (OperType.isInsert(transitionInstanceDto.getOperType())) {
				transitionInstanceService.createTransitionInstance(
						transitionInstanceDto, true);
			} else {
				transitionInstanceService.updateTransitionInstance(
						transitionInstanceDto, true);
			}
		}

		List<WorkItemDto> workItemDtos = persistProcessModel.getWorkItemDtos();
		for (int i = 0; i < workItemDtos.size(); i++) {
			WorkItemDto workItemDto = workItemDtos.get(i);
			if (OperType.isInsert(workItemDto.getOperType())) {
				workItemService.createWorkItem(workItemDto, true);
				logger.info("进入方法createWorkItem：" + workItemDto.getWorkItemId());
			} else {
				workItemService.updateWorkItem(workItemDto, true);
				logger.info("进入方法updateWorkItem：" + workItemDto.getWorkItemId());
			}
		}

		Map<String, String> attrMap = persistProcessModel.getAttrMap();
		for (String key : attrMap.keySet()) {
			String[] keys = key.split("_");
			processAttrService.setProcessAttr(
					persistProcessModel.getProcessInstanceId(), keys[0],
					keys[1], attrMap.get(key), true);
		}

		Map<String, String> paramMap = persistProcessModel.getParamMap();
		processParamService.setProcessParam(
				persistProcessModel.getProcessInstanceId(), paramMap, true);

	}

	/**
	 * 存储流程异常
	 * 
	 * @param exceptionDto
	 * @param operType
	 */
	@SuppressWarnings("unchecked")
	public void saveException(ExceptionDto exceptionDto, Integer operType) {
		if (OperType.isInsert(operType)) {
			exceptionService.createException(exceptionDto);
		} else {
			exceptionService.updateException(exceptionDto);
		}
		String commandCode = exceptionDto.getCommandCode();
		if(!"reportProcessState".equals(commandCode) 
				&& !"createWorkOrder".equals(commandCode)
				&& !"reportCalCondResult".equals(commandCode)
				&& !"reportTimeLimit".equals(commandCode)
				&& !"disableWorkItem".equals(commandCode)
				&& !"persistProcessModel".equals(commandCode)
				&& !"persistProcessModelRemote".equals(commandCode)
				&& !"saveException".equals(commandCode)
				&& !"saveCommandQueue".equals(commandCode)){
			if (exceptionDto.getProcessInstanceId() != null) {
				ProcessInstanceDto processInstance = processInstanceService
						.queryProcessInstance(exceptionDto.getProcessInstanceId()
								.toString());
				if (processInstance != null) {
					logger.error("流程实例id："+ exceptionDto.getProcessInstanceId().toString());
					processInstance.setState(WMProcessInstanceState.ERROR_INT);
					processInstanceService.updateProcessInstance(processInstance,false);

					//add by che.zi 2016-0830 begin zmp:882598
					//异常通知业务系统
					try {
						Map<String,String> flowPassMap = null;
						if(exceptionDto.getMsg() != null && !"".equals(exceptionDto.getMsg())){
							Map<String,Object> map = GsonHelper.toMap(exceptionDto.getMsg());
							flowPassMap = (Map<String, String>) map.get("FLOW_PASS_LIST");
						}
						workflowStateReport.reportProcessState(exceptionDto.getProcessInstanceId(),
								exceptionDto.getId().toString(), CommonDomain.WM_ERROR_REPORT, flowPassMap,
								exceptionDto.getErrorInfo(),exceptionDto.getAreaId());
					} catch (Exception e) {
						logger.error("----调用业务侧异常通知接口异常，异常信息："+e.getMessage(),e);
					}
					//add by che.zi 2016-0830 end
				}
			}
		}
	}


	/**
	 * 修改流程实例状态
	 * 
	 * @param processInstanceId
	 * @param areaId
	 * @param state
	 * @param useDB
	 * @author che.zi 20160602
	 */
	public void updateProcessInstance(String processInstanceId, String areaId,String state,
			boolean useDB) {
		try {
			// 查询流程实例数据
			ProcessInstanceDto processInstance = processInstanceService
					.queryProcessInstance(processInstanceId);
			if (processInstance == null) {
				throw new FastflowException("流程实例不存在[processInstanceId="
						+ processInstanceId + "]");
			}
			// 更新流程状态
			Long stateLong = LongHelper.valueOf(state);
			if(stateLong != null){
				processInstance
					.setState(stateLong.intValue());
				processInstanceService
					.updateProcessInstance(processInstance, useDB);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof FastflowException) {
				throw (FastflowException) ex;
			}
			throw new FastflowException(ex);
		}
	}
	
	/**
	 * 判断当前工作项是否可提交（用于内存模式，看当前环节前一环节是否已完成）
	 * @param workItemId
	 * @param useDB
	 * @return
	 */
	public boolean canCompleteWorkItem(String workItemId,boolean useDB){
		if(FastflowConfig.isCacheModel && !useDB){
			WorkItemDto workItem = workItemService.queryWorkItem(workItemId);
			
			/** 获得流程定义 */
			String processDefinitionId = workItem.getProcessDefineId()
					.toString();
			String activityDefinitionId = workItem.getActivityDefinitionId()
					.toString();

			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processDefinitionId);
			Activity activity = process.getActivityById(activityDefinitionId);
			/** 取出当前活动的所有入边 */
			Iterator<Transition> transitions = activity.getAfferentTransitions()
					.iterator();
			while (transitions.hasNext()) {
				Transition currentTransition = transitions.next();
				Activity fromActivity = currentTransition.getFromActivity();
				ActivityInstanceDto activityInstanceDto = activityInstanceService.queryActivityInstanceByActivityId(fromActivity.getId());
				if(activityInstanceDto != null){
					int state = activityInstanceDto.getState();
					if (WMActivityInstanceState.OPEN_INITIATED_INT == state
							|| WMActivityInstanceState.OPEN_SUSPENDED_INT == state
							|| WMActivityInstanceState.OPEN_RUNNING_INT == state) {
						logger.info("--未完成活动实例----"+state+"====>"+activityInstanceDto.getId());
						return false;
					}
				}
			}
		}
		return true;
	}

	
	public ProcessDefinitionService getProcessDefinitionService() {
		return processDefinitionService;
	}

	public void setProcessDefinitionService(
			ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}

	public ActivityInstanceService getActivityInstanceService() {
		return activityInstanceService;
	}

	public void setActivityInstanceService(
			ActivityInstanceService activityInstanceService) {
		this.activityInstanceService = activityInstanceService;
	}

	public ProcessInstanceService getProcessInstanceService() {
		return processInstanceService;
	}

	public void setProcessInstanceService(
			ProcessInstanceService processInstanceService) {
		this.processInstanceService = processInstanceService;
	}

	public ProcessParamService getProcessParamService() {
		return processParamService;
	}

	public void setProcessParamService(ProcessParamService processParamService) {
		this.processParamService = processParamService;
	}

	public WorkflowStateReport getWorkflowStateReport() {
		return workflowStateReport;
	}

	public void setWorkflowStateReport(WorkflowStateReport workflowStateReport) {
		this.workflowStateReport = workflowStateReport;
	}

	public TransitionInstanceService getTransitionInstanceService() {
		return transitionInstanceService;
	}

	public void setTransitionInstanceService(
			TransitionInstanceService transitionInstanceService) {
		this.transitionInstanceService = transitionInstanceService;
	}

	public WorkItemService getWorkItemService() {
		return workItemService;
	}

	public void setWorkItemService(WorkItemService workItemService) {
		this.workItemService = workItemService;
	}

	public TacheService getTacheService() {
		return tacheService;
	}

	public void setTacheService(TacheService tacheService) {
		this.tacheService = tacheService;
	}

	public ProcessAttrService getProcessAttrService() {
		return processAttrService;
	}

	public void setProcessAttrService(ProcessAttrService processAttrService) {
		this.processAttrService = processAttrService;
	}

	public ProcessParamDefService getProcessParamDefService() {
		return processParamDefService;
	}

	public void setProcessParamDefService(
			ProcessParamDefService processParamDefService) {
		this.processParamDefService = processParamDefService;
	}

	public ReturnReasonService getReturnReasonService() {
		return returnReasonService;
	}

	public void setReturnReasonService(ReturnReasonService returnReasonService) {
		this.returnReasonService = returnReasonService;
	}

	public ExceptionService getExceptionService() {
		return exceptionService;
	}

	public void setExceptionService(ExceptionService exceptionService) {
		this.exceptionService = exceptionService;
	}

	public ProcessPackageService getProcessPackageService() {
		return processPackageService;
	}

	public void setProcessPackageService(ProcessPackageService processPackageService) {
		this.processPackageService = processPackageService;
	}

	public void setTimeLimitClient(TimeLimitClient timeLimitClient) {
		this.timeLimitClient = timeLimitClient;
	}

	public void setTacheLimitService(TacheLimitService tacheLimitService) {
		this.tacheLimitService = tacheLimitService;
	}

	public void setFlowLimitService(FlowLimitService flowLimitService) {
		this.flowLimitService = flowLimitService;
	}

	public void saveCommandQueue(CommandQueueDto commandQueueDto) {
		commandQueueService.addCommandQueue(commandQueueDto);
	}

	public void dataToHis(String processInstanceId) {
		try {
			dataToHisService.saveDataToHis(processInstanceId);
		} catch (Exception e) {
			logger.error("----数据转存异常："+e.getMessage(),e);
			throw new FastflowException(e);
		}
	}

	/**
	 * 退单  通过直接传入目标环节的工作项id，不根据退单原因查找目标环节
	 * @param workItemId 当前环节的工作项id
	 * @param targetWorkItemId 目标环节的工作项id
	 * @param reasonType 异常原因类型
	 * @param memo 备注
	 * @param areaId 区域id
	 * @param useDB 是否直接操作数据库
	 * @param flowPassMap 流程透传参数
	 */
	public void disableWorkItemByTarget(String workItemId, String targetWorkItemId,String reasonType,
			String memo, String areaId, boolean useDB,
			Map<String, String> flowPassMap) {
		String processInstanceId = null;
		try {
			WorkItemDto workItem = workItemService.queryWorkItem(workItemId);
			processInstanceId = workItem.getProcessInstanceId().toString();
			ProcessInstanceDto processInstance = processInstanceService.queryProcessInstance(processInstanceId);
			/**获得流程定义*/
			WorkflowProcess process = processDefinitionService.findWorkflowProcessById(processInstance.getProcessDefineId());
			if (process == null) {
				throw new FastflowException("流程定义不存在："
						+ processInstance.getProcessDefineId());
			}
			/**在流程定义中找到异常处理活动*/
			String exActivityId = process.getExceptionActivityId();
			if (exActivityId == null) {
				throw new FastflowException("异常活动节点不存在！请检查XPDL");
			}
			//根据目标环节工作项，获取目标活动节点targetActivityId
			WorkItemDto targetWorkItem = workItemService.queryWorkItem(targetWorkItemId);
			if(targetWorkItem == null){
				throw new FastflowException("---目标环节工作项不存在---targetWorkItemId:"+targetWorkItemId);
			}
			String targetActivityId = targetWorkItem.getActivityDefinitionId();
			
			String tmpTarget = processAttrService.getProcessAttr(
					processInstance.getProcessInstanceId(), exActivityId,
					ProcessAttrService.TARGETACTIVITYID);
			if (tmpTarget == null || "".equalsIgnoreCase(tmpTarget)){
				String startMode = null;

			    if("10W".equals(reasonType)){
			    	startMode = WMAutomationMode.WAIT;
			    }else if("10Q".equals(reasonType)){
			    	startMode = WMAutomationMode.CHANGERETURNBACK;
			    }else if("10B".equals(reasonType)){
			    	startMode = WMAutomationMode.PAUSE;
			    }else{
			    	startMode = WMAutomationMode.RETURNBACK;
			    }

				// 设置运行时信息
				processAttrService.setProcessAttr(
						processInstance.getProcessInstanceId(), exActivityId,
						ProcessAttrService.TARGETACTIVITYID, targetActivityId, useDB);
				processAttrService.setProcessAttr(
						processInstance.getProcessInstanceId(), exActivityId,
						ProcessAttrService.STARTMODE, startMode, useDB);
				processAttrService.setProcessAttr(
						processInstance.getProcessInstanceId(), exActivityId,
						ProcessAttrService.AUTOTOMANUAL, "false", useDB);

			}
		} catch (Exception e) {
			throw new FastflowException("----disableWorkItemByTarget---根据目标环节退单失败，异常原因："+e.getMessage(),e);
		}
		disableWorkItem(workItemId, null, null, memo, areaId, useDB, flowPassMap);
	}

	/**
	 * 根据退单目标环节 查询所有可以回滚的工作项集合
	 * @param processInstanceId 当前流程实例id
	 * @param disabledWorkitemId 当前工作项id
	 * @param targetWorkItemId 目标环节工作项id
	 * @param reasonType 异常原因类型
	 * @param useDB
	 * @param areaId
	 * @return
	 */
	public List<WorkItemDto> findCanRollBackWorkItemsByTarget(
			String processInstanceId, String disabledWorkitemId,
			String targetWorkItemId, String reasonType, boolean useDB, String areaId) {
		List<WorkItemDto> workItemIds = new ArrayList<WorkItemDto>();
		/**获取发起退单的工作项Dto*/
        WorkItemDto disableWorkItem = workItemService.queryWorkItem(disabledWorkitemId);
        /**获取发起退单的活动实例*/
        ActivityInstanceDto disableActivityInstance = activityInstanceService.queryActivityInstance(
            String.valueOf(disableWorkItem.getActivityInstanceId()));
        /**获得流程实例*/
        ProcessInstanceDto processInstance = processInstanceService.queryProcessInstance(processInstanceId);
        /**获得流程定义*/
        WorkflowProcess process = processDefinitionService.findWorkflowProcessById(processInstance.getProcessDefineId());
        if (process == null) {
			throw new FastflowException("流程定义不存在："
					+ processInstance.getProcessDefineId());
        }

		//根据目标环节工作项，获取目标活动节点targetActivityId
		WorkItemDto targetWorkItem = workItemService.queryWorkItem(targetWorkItemId);
		String targetActivityId = targetWorkItem.getActivityDefinitionId();
		
		logger.info("----目标节点id,targetActivityId:"+targetActivityId);

        /**查找流程中处于Running状态的工作项*/
        List<WorkItemDto> runningWorkItems = workItemService.queryWorkItemsByProcess(processInstanceId,
        		WMWorkItemState.OPEN_RUNNING_INT, disabledWorkitemId);
        if (runningWorkItems != null) {
        }

		if (targetActivityId == null || targetActivityId.trim().length() == 0) {
            Iterator<WorkItemDto> iter = runningWorkItems.iterator();
            while (iter.hasNext()) {
                workItemIds.add(iter.next());
            }
        }else{

            /**得到目标环节的正向活动实例(closed.completed and direction=1)*/
            List<ActivityInstanceDto> targetInstances = new ArrayList<ActivityInstanceDto>(); //可达disableActivityInstance的目标环节的活动实例集合
            List<ActivityInstanceDto> targetActivityInstances = activityInstanceService.queryActivityInstancesByState(
                processInstanceId, targetActivityId,
                String.valueOf(WMActivityInstanceState.CLOSED_COMPLETED_INT),
                ActivityInstanceDto.NORMAL_DIRECTION);
            if (targetActivityInstances.size() > 0) {
                Iterator<ActivityInstanceDto> iter = targetActivityInstances.iterator();
                while (iter.hasNext()) {
                    ActivityInstanceDto targetActivityInstance = iter.next();
                    if (isCanReached(targetActivityInstance, disableActivityInstance, processInstance)) {
                        targetInstances.add(targetActivityInstance);
                    }
                }
            }

            /**对每个处于运行态的工作项循环,判断其是否从目标活动实例可达*/
            Iterator<WorkItemDto> workItemIter = runningWorkItems.iterator();
            while (workItemIter.hasNext()) {
                /**对每个工作项找其对应的活动实例ID*/
                WorkItemDto workItem = workItemIter.next();
                ActivityInstanceDto activityInstance = activityInstanceService.queryActivityInstance(
                    String.valueOf(workItem.getActivityInstanceId()));
                Iterator<ActivityInstanceDto> targetIter = targetInstances.iterator();
                while (targetIter.hasNext()) {
                    ActivityInstanceDto targetActivityInstance = targetIter.next();
                    if (isCanReached(targetActivityInstance, activityInstance, processInstance)) {
                        workItemIds.add(workItem);
                    }
                }
            }
        }
		return workItemIds;
	}

	public Boolean isCanRollBack(Long targetTacheId, String processInstanceId) {
		if(targetTacheId != null){
			if( targetTacheId.longValue() == 0){
				return true;
			}else{
				WorkItemDto targetWorkItem = workItemService.queryWorkItemByTacheId(LongHelper.valueOf(processInstanceId), targetTacheId);
				if(targetWorkItem != null){
					return true;
				}
			}
		}else{
			logger.error("----targetTacheId为空---");
		}
		return false;
	}

	/**
	 * 根据目标环节判断是否能退单（目标环节正向活动实例是否存在）
	 * @param workItemId
	 * @param targetWorkItemId
	 * @return
	 */
	public Boolean isCanDisableWorkItem(String workItemId,
			String targetWorkItemId) {
		WorkItemDto workItem = workItemService.queryWorkItem(workItemId);
		String processInstanceId = workItem.getProcessInstanceId()
				.toString();
		// 根据目标环节工作项，获取目标活动节点targetActivityId
		WorkItemDto targetWorkItem = workItemService
				.queryWorkItem(targetWorkItemId);
		String targetActivityId = targetWorkItem.getActivityDefinitionId();
		List<ActivityInstanceDto> targetActivityInstances = activityInstanceService.queryActivityInstancesByState(
	                processInstanceId, targetActivityId,
	                String.valueOf(WMActivityInstanceState.CLOSED_COMPLETED_INT),
	                ActivityInstanceDto.NORMAL_DIRECTION);
		if (targetActivityInstances.size() < 0) {
			throw new FastflowException("----未找到目标环节的正向活动实例---");
		}
		return true;
	}

	/**
	 * 根据退单原因判断能否退单（目标环节正向活动实例是否存在）
	 * @param workItemId
	 * @param reasonCode
	 * @param reasonConfigId
	 * @return
	 */
	public Boolean isCanDisableWorkItem(String workItemId, String reasonCode,
			String reasonConfigId,Boolean useDB,String areaId) {
		WorkItemDto workItemDto = workItemService.queryWorkItem(workItemId);
		// 检查是否为空
		if (workItemDto == null) {
			throw new FastflowException("工作项不存在：" + workItemId);
		}
		// 获取当前的活动实例Dto信息
		ActivityInstanceDto activityInstance = activityInstanceService
				.queryActivityInstance(workItemDto.getActivityInstanceId()
						.toString());
		// 获取流程实例
		String processInstanceId = activityInstance.getProcessInstanceId()
				.toString();
		ProcessInstanceDto processInstance = processInstanceService.queryProcessInstance(processInstanceId);
		String startActiityId = activityInstance.getActivityDefinitionId();

		// 获取流程定义
		WorkflowProcess process = processDefinitionService
				.findWorkflowProcessById(processInstance.getProcessDefineId());

		// 判断流程定义是否空
		if (process == null) {
			throw new FastflowException("流程定义不存在："
					+ processInstance.getProcessDefineId());
		}

		boolean hasReason = (reasonConfigId != null || reasonCode != null);
		String targetActivityId = null;
		// 存在异常原因
		if (hasReason) {
			targetActivityId = setRollbackInfo(processInstance,
					reasonConfigId, reasonCode, startActiityId, process,
					useDB,areaId);
		} else {
			// 不存在异常原因
			String exActivityId = process.getExceptionActivityId();
			if (exActivityId == null) {
				throw new FastflowException("异常活动节点不存在！请检查XPDL");
			}

			// 从流程属性里面查找流程的目标节点
			targetActivityId = processAttrService.getProcessAttr(
					processInstance.getProcessInstanceId(), exActivityId,
					ProcessAttrService.TARGETACTIVITYID);
			if (StringHelper.isEmpty(targetActivityId)) {
				targetActivityId = process.getStartActivity().getId();
			}
		}
		logger.info("----目标节点id,targetActivityId:"+targetActivityId);
		// 得到目标环节的正向活动实例(closed.completed and direction=1)
		List<ActivityInstanceDto> targetActivityInstances = activityInstanceService
				.queryActivityInstancesByState(processInstanceId,
						targetActivityId,
						WMActivityInstanceState.CLOSED_COMPLETED_INT + "",
						ActivityInstanceDto.NORMAL_DIRECTION);
		if(targetActivityInstances.size()<0){
			throw new FastflowException("----未找到目标环节的正向活动实例---");
		}
		return true;
	}
	
	/**
	 * 挂起工作项
	 * @param workItemId
	 * @param flowPassMap 
	 * @param areaId 
	 */
	public void suspendWorkItem(String workItemId, Map<String, String> flowPassMap, String areaId,boolean useDB){
		String processInstanceId = null;
		try {
			WorkItemDto workItem = workItemService.queryWorkItem(workItemId);
			// 判断工作项状态决定能否挂起
			WMWorkItemState.states()[workItem.getState()].checkTransition(
					WMWorkItemState.CLOSED_ABORTED, true);
			/** 当前工作项设置为Abort状态 */
			workItem.setState(WMWorkItemState.CLOSED_ABORTED_INT);
			workItem.setCompletedDate(DateHelper.getTimeStamp());
			workItemService.updateWorkItem(workItem, useDB);
			processInstanceId = workItem.getProcessInstanceId().toString();
			ProcessInstanceDto processInstance = processInstanceService
					.queryProcessInstance(processInstanceId);
			String activityInstanceId = workItem.getActivityInstanceId()
					.toString();
			ActivityInstanceDto activityInstance = activityInstanceService
					.queryActivityInstance(activityInstanceId);
			activityInstance
					.setState(WMActivityInstanceState.OPEN_SUSPENDED_INT);
			activityInstanceService.updateActivityInstance(activityInstance,
					useDB);
			/** 如果当前流程中的活动实例都不处于Running状态，如果是，将当前流程设置为suspend状态 */
			List<ActivityInstanceDto> runningActivityInstances = activityInstanceService
					.queryActivityInstancesByState(
							processInstanceId,
							null,
							StringHelper
									.valueOf(WMActivityInstanceState.OPEN_RUNNING_INT),
							null);
			if (runningActivityInstances.size() == 0) {
				processInstance
						.setState(WMProcessInstanceState.OPEN_NOTRUNNING_SUSPENDED_INT);
				processInstanceService.updateProcessInstance(processInstance,
						useDB);
				/** 发出流程已被挂起的通知 */
				workflowStateReport.reportProcessState(
						Long.valueOf(processInstanceId), "",
						CommonDomain.WM_ROLLBACK_REPORT, flowPassMap, null,
						areaId);
			}
		} catch (Exception e) {
			throw new FastflowException("挂起工作项异常，异常原因：" + e.getMessage(), e);
		}
	}
}
