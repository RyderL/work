package com.ztesoft.uosflow.inf.server.dubbo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.constant.CommonDomain;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.service.ActivityInstanceService;
import com.zterc.uos.fastflow.service.ExceptionService;
import com.zterc.uos.fastflow.service.ProcessInstanceService;
import com.zterc.uos.fastflow.service.ProcessParamDefService;
import com.zterc.uos.fastflow.service.WorkItemService;
import com.zterc.uos.fastflow.state.WMProcessInstanceState;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.client.CreateWorkOrderDto;
import com.ztesoft.uosflow.core.dto.client.ReportProcessStateDto;
import com.ztesoft.uosflow.core.dto.server.AbortProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.AddReturnReasonDto;
import com.ztesoft.uosflow.core.dto.server.AddTacheDto;
import com.ztesoft.uosflow.core.dto.server.CompleteWorkItemDto;
import com.ztesoft.uosflow.core.dto.server.CreateProcessInstacneDto;
import com.ztesoft.uosflow.core.dto.server.DelReturnReasonDto;
import com.ztesoft.uosflow.core.dto.server.DelTacheDto;
import com.ztesoft.uosflow.core.dto.server.DisableWorkItemDto;
import com.ztesoft.uosflow.core.dto.server.ModReturnReasonDto;
import com.ztesoft.uosflow.core.dto.server.ModTacheDto;
import com.ztesoft.uosflow.core.dto.server.ProcessInstanceJumpDto;
import com.ztesoft.uosflow.core.dto.server.ResumeProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.RollbackProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.StartProcessInstacneDto;
import com.ztesoft.uosflow.core.dto.server.SuspendProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.SuspendWorkItemDto;
import com.ztesoft.uosflow.core.dto.server.TerminateProcessInstanceDto;
import com.ztesoft.uosflow.core.dto.server.UpdateProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.DubboCommandResultDto;
import com.ztesoft.uosflow.dubbo.dto.result.DubboCreateProcInsResultDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboAbortProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboAddReturnReasonDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboAddTacheDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboCompleteWorkItemDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboCreateProcessInstacneDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboDelReturnReasonDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboDelTacheDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboDisableWorkItemDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboModReturnReasonDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboModTacheDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboProcessInstanceJumpDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboResumeProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboRollbackProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboStartProcessInstacneDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboSuspendProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboSuspendWorkItemDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboTerminateProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboUpdateProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.inf.server.WorkFlowServerInf;
import com.ztesoft.uosflow.inf.server.common.ServerProxy;
import com.ztesoft.uosflow.inf.util.ServerJsonUtil;

public class WorkFlowServerInfImpl implements WorkFlowServerInf {
	private static final Logger logger = LoggerFactory.getLogger(WorkFlowServerInfImpl.class);
	
	@Autowired
	private ExceptionService exceptionService;
	@Autowired
	private WorkItemService workItemService;
	@Autowired
	private ActivityInstanceService activityInstanceService;
	@Autowired
	private ProcessParamDefService processParamDefService;
	@Autowired
	private ProcessInstanceService processInstanceService;
	
	@Override
	public DubboCreateProcInsResultDto createProcessInstance(
			DubboCreateProcessInstacneDto dCreateProcessInstacneDto) {
		logger.info("------���봴����������------");
		logger.info("----dubbo---�����������,createProcessInstance��"
				+ GsonHelper.toJson(dCreateProcessInstacneDto));
		CreateProcessInstacneDto createProcessInstacneDto = new CreateProcessInstacneDto();
		createProcessInstacneDto.setCommandCode(dCreateProcessInstacneDto.getCommandCode());
		createProcessInstacneDto.setSerial(UUID.randomUUID().toString());
		createProcessInstacneDto.setTime(DateHelper.getTime());
		createProcessInstacneDto.setFrom(dCreateProcessInstacneDto.getFrom());
		createProcessInstacneDto.setTo(dCreateProcessInstacneDto.getTo());
		createProcessInstacneDto.setAreaCode(dCreateProcessInstacneDto
				.getAreaCode());
		createProcessInstacneDto.setProcessInstanceId(dCreateProcessInstacneDto
				.getProcessInstanceId());
		createProcessInstacneDto.setFlowPackageCode(dCreateProcessInstacneDto
				.getFlowPackageCode());
		createProcessInstacneDto.setFlowParamList(dCreateProcessInstacneDto.getFlowParamList());
		createProcessInstacneDto.setFlowPassList(dCreateProcessInstacneDto.getFlowPassList());

		logger.info("----�����������,createProcessInstance��"
				+ GsonHelper.toJson(createProcessInstacneDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				createProcessInstacneDto);
		logger.info("----��������return,json��" + GsonHelper.toJson(result));

		DubboCreateProcInsResultDto resultDto = new DubboCreateProcInsResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result
				.getProcessInstanceId());
		resultDto.setFlowParamList(result.getFlowParamList());
		resultDto.setFlowPassList(result.getFlowPassList());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---��������return,json��"
				+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto startProcessInstance(
			DubboStartProcessInstacneDto dStartProcessInstance) {
		logger.info("------����������������------");
		logger.info("----dubbo---�����������,startProcessInstance��"
				+ GsonHelper.toJson(dStartProcessInstance));
		StartProcessInstacneDto startProcessInstacneDto = new StartProcessInstacneDto();
		startProcessInstacneDto.setSerial(UUID.randomUUID().toString());
		startProcessInstacneDto.setTime(DateHelper.getTime());
		startProcessInstacneDto.setFrom(dStartProcessInstance.getFrom());
		startProcessInstacneDto.setTo(dStartProcessInstance.getTo());
		startProcessInstacneDto
				.setAreaCode(dStartProcessInstance.getAreaCode());
		startProcessInstacneDto.setProcessInstanceId(dStartProcessInstance
				.getProcessInstanceId());
		startProcessInstacneDto.setFlowPassList(dStartProcessInstance.getFlowPassList());
		startProcessInstacneDto.setFlowParamList(dStartProcessInstance.getFlowParamList());

		logger.info("----�����������,startProcessInstance��"
				+ GsonHelper.toJson(startProcessInstacneDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				startProcessInstacneDto);
		logger.info("----�����������return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---�����������return,json��"
				+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto completeWorkItem(
			DubboCompleteWorkItemDto dCompleteWorkItemDto) {
		logger.info("------�����ύ����������------");
		logger.info("----dubbo---�ύ���������,completeWorkItem��"
				+ GsonHelper.toJson(dCompleteWorkItemDto));
		CompleteWorkItemDto completeWorkItemDto = new CompleteWorkItemDto();
		completeWorkItemDto.setSerial(UUID.randomUUID().toString());
		completeWorkItemDto.setTime(DateHelper.getTime());
		completeWorkItemDto.setFrom(dCompleteWorkItemDto.getFrom());
		completeWorkItemDto.setTo(dCompleteWorkItemDto.getTo());
		completeWorkItemDto.setAreaCode(dCompleteWorkItemDto.getAreaCode());
		completeWorkItemDto.setProcessInstanceId(dCompleteWorkItemDto
				.getProcessInstanceId());
		completeWorkItemDto.setWorkitemId(dCompleteWorkItemDto.getWorkitemId());
		completeWorkItemDto.setFlowParamMap(dCompleteWorkItemDto.getFlowParamList());
		completeWorkItemDto.setFlowPassMap(dCompleteWorkItemDto.getFlowPassList());

		logger.info("----�ύ���������,completeWorkItem��"
				+ GsonHelper.toJson(completeWorkItemDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				completeWorkItemDto);
		logger.info("----�ύ���������return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---�ύ���������return,json��"
				+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto rollbackProcessInstance(
			DubboRollbackProcessInstanceDto dRollbackProcessInstanceDto) {
		logger.info("------���볷������------");
		logger.info("----dubbo---�������,rollbackProcessInstance��"
				+ GsonHelper.toJson(dRollbackProcessInstanceDto));
		RollbackProcessInstanceDto rollbackProcessInstanceDto = new RollbackProcessInstanceDto();
		rollbackProcessInstanceDto.setSerial(UUID.randomUUID().toString());
		rollbackProcessInstanceDto.setTime(DateHelper.getTime());
		rollbackProcessInstanceDto.setFrom(dRollbackProcessInstanceDto
				.getFrom());
		rollbackProcessInstanceDto.setTo(dRollbackProcessInstanceDto.getTo());
		rollbackProcessInstanceDto.setAreaCode(dRollbackProcessInstanceDto
				.getAreaCode());
		rollbackProcessInstanceDto
				.setProcessInstanceId(dRollbackProcessInstanceDto
						.getProcessInstanceId());
		rollbackProcessInstanceDto.setReasonCode(dRollbackProcessInstanceDto
				.getReasonCode());
		rollbackProcessInstanceDto.setFlowPassList(dRollbackProcessInstanceDto.getFlowPassList());
		rollbackProcessInstanceDto.setStartMode(dRollbackProcessInstanceDto.getStartMode());
		
		logger.info("----�������,rollbackProcessInstance��"
				+ GsonHelper.toJson(rollbackProcessInstanceDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				rollbackProcessInstanceDto);
		logger.info("----�������return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---�������return,json��"
				+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto disableWorkItem(
			DubboDisableWorkItemDto dDisableWorkItemDto) {
		logger.info("------�����˵�����------");
		logger.info("----dubbo---�˵����,disableWorkItem��"
				+ GsonHelper.toJson(dDisableWorkItemDto));
		DisableWorkItemDto disableWorkItemDto = new DisableWorkItemDto();
		disableWorkItemDto.setSerial(UUID.randomUUID().toString());
		disableWorkItemDto.setTime(DateHelper.getTime());
		disableWorkItemDto.setFrom(dDisableWorkItemDto.getFrom());
		disableWorkItemDto.setTo(dDisableWorkItemDto.getTo());
		disableWorkItemDto.setAreaCode(dDisableWorkItemDto.getAreaCode());
		disableWorkItemDto.setProcessInstanceId(dDisableWorkItemDto
				.getProcessInstanceId());
		disableWorkItemDto.setWorkitemId(dDisableWorkItemDto.getWorkitemId());
		disableWorkItemDto.setReasonCode(dDisableWorkItemDto.getReasonCode());
		disableWorkItemDto.setMemo(dDisableWorkItemDto.getMemo());
		disableWorkItemDto.setReasonType(dDisableWorkItemDto.getReasonType());
		disableWorkItemDto.setReasonCfgId(dDisableWorkItemDto.getReasonCfgId());
		disableWorkItemDto.setFlowPassList(dDisableWorkItemDto.getFlowPassList());
		disableWorkItemDto.setTargetWorkItemId(dDisableWorkItemDto.getTargetWorkItemId());

		logger.info("----�˵����,disableWorkItem��"
				+ GsonHelper.toJson(disableWorkItemDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				disableWorkItemDto);
		logger.info("----�˵����return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---�˵�return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}
	

	@Override
	public DubboCommandResultDto abortProcessInstance(
			DubboAbortProcessInstanceDto dAbortProcessInstanceDto) {
		logger.info("------����������������------");
		logger.info("----dubbo---�����������,abortProcessInstanceDto��"
				+ GsonHelper.toJson(dAbortProcessInstanceDto));
		AbortProcessInstanceDto abortProcessInstanceDto = new AbortProcessInstanceDto();
		abortProcessInstanceDto.setSerial(UUID.randomUUID().toString());
		abortProcessInstanceDto.setTime(DateHelper.getTime());
		abortProcessInstanceDto.setFrom(dAbortProcessInstanceDto.getFrom());
		abortProcessInstanceDto.setTo(dAbortProcessInstanceDto.getTo());
		abortProcessInstanceDto.setAreaCode(dAbortProcessInstanceDto.getAreaCode());
		abortProcessInstanceDto.setProcessInstanceId(dAbortProcessInstanceDto.getProcessInstanceId());

		logger.info("----�����������,abortProcessInstanceDto��"
				+ GsonHelper.toJson(abortProcessInstanceDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				abortProcessInstanceDto);
		logger.info("----��������return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---��������return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public boolean dealException(Long exceptionId) {
		try {
			ExceptionDto exceptionDto =exceptionService.queryException(exceptionId);

			if(exceptionDto.getMsg() != null && !"null".equals(exceptionDto.getMsg()) 
					&& !"".equals(exceptionDto.getMsg())){
				// ��������
				CommandDto cmdDto = ServerJsonUtil.getCommandDtoFromJson(exceptionDto.getMsg());
				if(cmdDto != null){
					PlatformTransactionManager txManager = null;
					DefaultTransactionDefinition def = null;
					TransactionStatus status = null;
					try {
						txManager = JDBCHelper.getTransactionManager();
						def = new DefaultTransactionDefinition();
						def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
						status = txManager.getTransaction(def);
						logger.info("---�ֶ�����ʼ----");
						CommandProxy.getInstance().updateProcessInstance(exceptionDto);

						logger.info("---��Ϣ��Ͷ��ʼ----");
						CommandResultDto resultDto = CommandProxy.getInstance()
								.dealCommand(cmdDto, exceptionDto.getId(), true);
						logger.info("---��Ϣ��Ͷ����----�����"+resultDto.isDealFlag());

						if (resultDto.isDealFlag()) {
							CommandProxy.getInstance().updateException(exceptionDto,ExceptionDto.ERROR_HANDLE_FINISHED);
							txManager.commit(status);
							logger.info("---�ֶ������ύ----");
							return true;
						}
					} catch (Exception e) {
						logger.error("----�쳣�����쳣���쳣��Ϣ��"+e.getMessage(),e);
						txManager.rollback(status);
						logger.error("---�ֶ�����ع�----");
					}
				}else{
					return false;
				}
			}else{
				if("reportProcessState".equals(exceptionDto.getCommandCode())){
					ProcessInstanceDto processInstance = processInstanceService.queryProcessInstance(exceptionDto.getProcessInstanceId().toString());
					if(processInstance != null){
						ReportProcessStateDto commandDto = new ReportProcessStateDto();
						Map<String, Object> paramsMap = new HashMap<String, Object>();
						paramsMap.put(InfConstant.INF_COMMAND_CODE, "reportProcessState");
						paramsMap.put(InfConstant.INF_SERIAL, UUID.randomUUID());
						paramsMap.put(InfConstant.INF_FROM, "FLOW");
						paramsMap.put(InfConstant.INF_TO, "IOM");
						paramsMap.put(InfConstant.INF_PROCESSINSTANCEID,
								exceptionDto.getProcessInstanceId());
						paramsMap.put(InfConstant.INF_COMMENT, "");
						String state = "";
						if (WMProcessInstanceState.CLOSED_COMPLETED_INT == processInstance.getState()) {
							state = String.valueOf(CommonDomain.WM_END_REPORT);
						} else if (WMProcessInstanceState.OPEN_RUNNING_INT == processInstance.getState()) {
							state = String.valueOf(CommonDomain.WM_START_REPORT);
						} 
						paramsMap.put(InfConstant.INF_STATE, state);
						commandDto.init(paramsMap);
						logger.error("---msgΪnull���ֶ�ƴװ����Ϣ���ݣ�"+ServerJsonUtil.getJsonFromCommandDto(commandDto));

						CommandResultDto resultDto = CommandProxy.getInstance()
								.dealCommand(commandDto, exceptionDto.getId(), true);

						logger.error("---msgΪnull���ֶ�ƴװ����Ϣ���ݺ�����ִ�н����"+resultDto.isDealFlag());
						if (resultDto.isDealFlag()) {
							CommandProxy.getInstance().updateException(exceptionDto,ExceptionDto.ERROR_HANDLE_FINISHED);
							return true;
						}
					}
				}else if("createWorkOrder".equals(exceptionDto.getCommandCode())){
					CreateWorkOrderDto commandDto = new CreateWorkOrderDto();
					ProcessInstanceDto processInstance = processInstanceService
							.queryProcessInstance(exceptionDto.getProcessInstanceId().toString());
					if (processInstance != null) {
						Map<String, Object> paramsMap = new HashMap<String, Object>();
						paramsMap.put(InfConstant.INF_COMMAND_CODE,
								commandDto.getCommandCode());
						paramsMap.put(InfConstant.INF_SERIAL,
								commandDto.getSerial());
						paramsMap.put(InfConstant.INF_FROM, commandDto.getFrom());
						paramsMap.put(InfConstant.INF_TO, commandDto.getTo());
						paramsMap.put(InfConstant.INF_AREA_CODE,
								processInstance.getAreaId());
						paramsMap.put(InfConstant.INF_PROCESSINSTANCEID, exceptionDto
								.getProcessInstanceId().toString());
						WorkItemDto workItemDto = workItemService.queryWorkItem(exceptionDto
								.getWorkItemId().toString());
						ActivityInstanceDto activityInstanceDto = activityInstanceService
								.queryActivityInstance(workItemDto
										.getActivityInstanceId().toString());
						paramsMap.put(InfConstant.INF_TACHEID, workItemDto
								.getTacheId().toString());
						paramsMap.put(InfConstant.INF_TACHECODE, workItemDto
								.getTacheCode().toString());
						paramsMap.put(InfConstant.INF_WORKITEMID, workItemDto
								.getWorkItemId().toString());
						paramsMap.put(InfConstant.INF_DIRECTION,
								activityInstanceDto.getDirection().toString());
						paramsMap.put(InfConstant.INF_BATCHID, activityInstanceDto
								.getBatchid().toString());
						Map<String, String> flowParamMap = processParamDefService
								.qryProInsTacheParam(
										processInstance.getProcessDefineId(),
										workItemDto.getTacheCode());
						paramsMap.put(InfConstant.INF_FLOW_PARAM_LIST,
								flowParamMap);

						commandDto.init(paramsMap);
						logger.error("---msgΪnull���ֶ�ƴװ����Ϣ���ݣ�"
								+ ServerJsonUtil.getJsonFromCommandDto(commandDto));

						CommandResultDto resultDto = CommandProxy.getInstance()
								.dealCommand(commandDto, exceptionDto.getId(), true);

						logger.error("---msgΪnull���ֶ�ƴװ����Ϣ���ݺ�����ִ�н����"
								+ resultDto.isDealFlag());
						if (resultDto.isDealFlag()) {
							CommandProxy.getInstance().updateException(exceptionDto,ExceptionDto.ERROR_HANDLE_FINISHED);
							return true;
						}
					}
				}
			}
			
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}

	@Override
	public String executeCommand(String requestJson) {
		if (logger.isInfoEnabled()) {
			logger.info("������Ϣ��" + requestJson);
		}
		return ServerProxy.getInstance().dealForJson(requestJson);
	}

	@Override
	public DubboCommandResultDto addTache(DubboAddTacheDto dubboAddTacheDto) {
		logger.info("------����������������------");
		logger.info("----dubbo---�����������,dubboAddTacheDto��"
				+ GsonHelper.toJson(dubboAddTacheDto));
		AddTacheDto addTacheDto = new AddTacheDto();
		addTacheDto.setSerial(UUID.randomUUID().toString());
		addTacheDto.setTime(DateHelper.getTime());
		addTacheDto.setFrom(dubboAddTacheDto.getFrom());
		addTacheDto.setTo(dubboAddTacheDto.getTo());
		addTacheDto.setAreaCode(dubboAddTacheDto.getAreaCode());
		addTacheDto.setProcessInstanceId(dubboAddTacheDto.getProcessInstanceId());
		addTacheDto.setTacheCode(dubboAddTacheDto.getTacheCode());
		addTacheDto.setTacheCatalogId(dubboAddTacheDto.getTacheCatalogId());
		addTacheDto.setTacheName(dubboAddTacheDto.getTacheName());
		addTacheDto.setTacheType(dubboAddTacheDto.getTacheType());
		addTacheDto.setPackageDefineCodes(dubboAddTacheDto.getPackageDefineCodes());
		addTacheDto.setEffDate(dubboAddTacheDto.getEffDate());
		addTacheDto.setExpDate(dubboAddTacheDto.getExpDate());
		logger.info("----�����������,addTacheDto��"
				+ GsonHelper.toJson(addTacheDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				addTacheDto);
		logger.info("----��������return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---��������return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto modTache(DubboModTacheDto dubboModTacheDto) {

		logger.info("------�����޸Ļ�������------");
		logger.info("----dubbo---�޸Ļ������,dubboModTacheDto��"
				+ GsonHelper.toJson(dubboModTacheDto));
		ModTacheDto modTacheDto = new ModTacheDto();
		modTacheDto.setSerial(UUID.randomUUID().toString());
		modTacheDto.setTime(DateHelper.getTime());
		modTacheDto.setFrom(dubboModTacheDto.getFrom());
		modTacheDto.setTo(dubboModTacheDto.getTo());
		modTacheDto.setAreaCode(dubboModTacheDto.getAreaCode());
		modTacheDto.setProcessInstanceId(dubboModTacheDto.getProcessInstanceId());
		modTacheDto.setTacheId(dubboModTacheDto.getTacheId());
		modTacheDto.setTacheCatalogId(dubboModTacheDto.getTacheCatalogId());
		modTacheDto.setTacheName(dubboModTacheDto.getTacheName());
		modTacheDto.setPackageDefineCodes(dubboModTacheDto.getPackageDefineCodes());
		modTacheDto.setEffDate(dubboModTacheDto.getEffDate());
		modTacheDto.setExpDate(dubboModTacheDto.getExpDate());

		logger.info("----�޸Ļ������,modTacheDto��"
				+ GsonHelper.toJson(modTacheDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				modTacheDto);
		logger.info("----�޸Ļ���return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---�޸Ļ���return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto delTache(DubboDelTacheDto dubboDelTacheDto) {
		logger.info("------����ɾ����������------");
		logger.info("----dubbo---ɾ���������,dubboDelTacheDto��"
				+ GsonHelper.toJson(dubboDelTacheDto));
		DelTacheDto delTacheDto = new DelTacheDto();
		delTacheDto.setSerial(UUID.randomUUID().toString());
		delTacheDto.setTime(DateHelper.getTime());
		delTacheDto.setFrom(dubboDelTacheDto.getFrom());
		delTacheDto.setTo(dubboDelTacheDto.getTo());
		delTacheDto.setAreaCode(dubboDelTacheDto.getAreaCode());
		delTacheDto.setProcessInstanceId(dubboDelTacheDto.getProcessInstanceId());
		delTacheDto.setTacheId(dubboDelTacheDto.getTacheId());

		logger.info("----ɾ���������,delTacheDto��"
				+ GsonHelper.toJson(delTacheDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				delTacheDto);
		logger.info("----ɾ������return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---ɾ������return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto addReturnReason(
			DubboAddReturnReasonDto dubboAddReturnReasonDto) {
		logger.info("------���������쳣ԭ������------");
		logger.info("----dubbo---�����쳣ԭ�����,dubboAddReturnReasonDto��"
				+ GsonHelper.toJson(dubboAddReturnReasonDto));
		AddReturnReasonDto addReturnReasonDto = new AddReturnReasonDto();
		addReturnReasonDto.setSerial(UUID.randomUUID().toString());
		addReturnReasonDto.setTime(DateHelper.getTime());
		addReturnReasonDto.setFrom(dubboAddReturnReasonDto.getFrom());
		addReturnReasonDto.setTo(dubboAddReturnReasonDto.getTo());
		addReturnReasonDto.setAreaCode(dubboAddReturnReasonDto.getAreaCode());
		addReturnReasonDto.setProcessInstanceId(dubboAddReturnReasonDto.getProcessInstanceId());
		addReturnReasonDto.setReasonCode(dubboAddReturnReasonDto.getReasonCode());
		addReturnReasonDto.setReasonCatalogId(dubboAddReturnReasonDto.getReasonCatalogId());
		addReturnReasonDto.setReasonName(dubboAddReturnReasonDto.getReasonName());
		addReturnReasonDto.setReasonType(dubboAddReturnReasonDto.getReasonType());
		addReturnReasonDto.setComment(dubboAddReturnReasonDto.getComment());
		addReturnReasonDto.setRecommandMeans(dubboAddReturnReasonDto.getRecommandMeans());

		logger.info("----�����쳣ԭ�����,addTacheDto��"
				+ GsonHelper.toJson(addReturnReasonDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				addReturnReasonDto);
		logger.info("----�����쳣ԭ��return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---�����쳣ԭ��return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto modReturnReason(
			DubboModReturnReasonDto dubboModReturnReasonDto) {

		logger.info("------�����޸��쳣ԭ������------");
		logger.info("----dubbo---�޸��쳣ԭ�����,dubboModReturnReasonDto��"
				+ GsonHelper.toJson(dubboModReturnReasonDto));
		ModReturnReasonDto modReturnReasonDto = new ModReturnReasonDto();
		modReturnReasonDto.setSerial(UUID.randomUUID().toString());
		modReturnReasonDto.setTime(DateHelper.getTime());
		modReturnReasonDto.setFrom(dubboModReturnReasonDto.getFrom());
		modReturnReasonDto.setTo(dubboModReturnReasonDto.getTo());
		modReturnReasonDto.setAreaCode(dubboModReturnReasonDto.getAreaCode());
		modReturnReasonDto.setProcessInstanceId(dubboModReturnReasonDto.getProcessInstanceId());
		modReturnReasonDto.setReasonId(dubboModReturnReasonDto.getReasonId());
		modReturnReasonDto.setReasonCatalogId(dubboModReturnReasonDto.getReasonCatalogId());
		modReturnReasonDto.setReasonName(dubboModReturnReasonDto.getReasonName());

		logger.info("----�޸��쳣ԭ�����,modReturnReasonDto��"
				+ GsonHelper.toJson(modReturnReasonDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				modReturnReasonDto);
		logger.info("----�޸��쳣ԭ��return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---�޸��쳣ԭ��return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto delReturnReason(
			DubboDelReturnReasonDto dubboDelReturnReasonDto) {
		logger.info("------����ɾ���쳣ԭ������------");
		logger.info("----dubbo---ɾ���쳣ԭ�����,dubboDelReturnReasonDto��"
				+ GsonHelper.toJson(dubboDelReturnReasonDto));
		DelReturnReasonDto delReturnReasonDto = new DelReturnReasonDto();
		delReturnReasonDto.setSerial(UUID.randomUUID().toString());
		delReturnReasonDto.setTime(DateHelper.getTime());
		delReturnReasonDto.setFrom(dubboDelReturnReasonDto.getFrom());
		delReturnReasonDto.setTo(dubboDelReturnReasonDto.getTo());
		delReturnReasonDto.setAreaCode(dubboDelReturnReasonDto.getAreaCode());
		delReturnReasonDto.setProcessInstanceId(dubboDelReturnReasonDto.getProcessInstanceId());
		delReturnReasonDto.setReasonId(dubboDelReturnReasonDto.getReasonId());

		logger.info("----ɾ���쳣ԭ�����,delReturnReasonDto��"
				+ GsonHelper.toJson(delReturnReasonDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				delReturnReasonDto);
		logger.info("----ɾ���쳣ԭ��return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---ɾ���쳣ԭ��return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto updateProcessInstance(
			DubboUpdateProcessInstanceDto dubboUpdateProcessInstanceDto) {

		logger.info("------�����޸�����״̬����------");
		logger.info("----dubbo---�޸�����״̬���,dubboUpdateProcessInstanceDto��"
				+ GsonHelper.toJson(dubboUpdateProcessInstanceDto));
		UpdateProcessInstanceDto updateProcessInstanceDto = new UpdateProcessInstanceDto();
		updateProcessInstanceDto.setSerial(UUID.randomUUID().toString());
		updateProcessInstanceDto.setTime(DateHelper.getTime());
		updateProcessInstanceDto.setFrom(dubboUpdateProcessInstanceDto.getFrom());
		updateProcessInstanceDto.setTo(dubboUpdateProcessInstanceDto.getTo());
		updateProcessInstanceDto.setAreaCode(dubboUpdateProcessInstanceDto.getAreaCode());
		updateProcessInstanceDto.setProcessInstanceId(dubboUpdateProcessInstanceDto.getProcessInstanceId());
		updateProcessInstanceDto.setState(dubboUpdateProcessInstanceDto.getState());

		logger.info("----�޸�����״̬���,updateProcessInstanceDto��"
				+ GsonHelper.toJson(updateProcessInstanceDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				updateProcessInstanceDto);
		logger.info("----�޸�����״̬return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---�޸�����״̬return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto processInstanceJump(
			DubboProcessInstanceJumpDto dubboProcessInstanceJumpDto) {

		logger.info("------����������ת����------");
		logger.info("----dubbo---������ת���,dubboProcessInstanceJumpDto��"
				+ GsonHelper.toJson(dubboProcessInstanceJumpDto));
		ProcessInstanceJumpDto processInstanceJumpDto = new ProcessInstanceJumpDto();
		processInstanceJumpDto.setSerial(UUID.randomUUID().toString());
		processInstanceJumpDto.setTime(DateHelper.getTime());
		processInstanceJumpDto.setFrom(dubboProcessInstanceJumpDto.getFrom());
		processInstanceJumpDto.setTo(dubboProcessInstanceJumpDto.getTo());
		processInstanceJumpDto.setAreaCode(dubboProcessInstanceJumpDto.getAreaCode());
		processInstanceJumpDto.setProcessInstanceId(dubboProcessInstanceJumpDto.getProcessInstanceId());
		processInstanceJumpDto.setFromActivityInstanceId(dubboProcessInstanceJumpDto.getFromActivityInstanceId());
		processInstanceJumpDto.setToActivityId(dubboProcessInstanceJumpDto.getToActivityId());
		processInstanceJumpDto.setFlowPassList(dubboProcessInstanceJumpDto.getFlowPassList());
		
		logger.info("----������ת���,processInstanceJumpDto��"
				+ GsonHelper.toJson(processInstanceJumpDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				processInstanceJumpDto);
		logger.info("----������תreturn,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---������תreturn,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto terminateProcessInstance(
			DubboTerminateProcessInstanceDto dubboTerminateProcessInstanceDto) {
		logger.info("----������ֹ��������----dubbo---���json��"+GsonHelper.toJson(dubboTerminateProcessInstanceDto));
		
		TerminateProcessInstanceDto terminateProcessInstanceDto = new TerminateProcessInstanceDto();
		terminateProcessInstanceDto.setSerial(UUID.randomUUID().toString());
		terminateProcessInstanceDto.setTime(DateHelper.getTime());
		terminateProcessInstanceDto.setFrom(dubboTerminateProcessInstanceDto.getFrom());
		terminateProcessInstanceDto.setTo(dubboTerminateProcessInstanceDto.getTo());
		terminateProcessInstanceDto.setAreaCode(dubboTerminateProcessInstanceDto.getAreaCode());
		terminateProcessInstanceDto.setProcessInstanceId(dubboTerminateProcessInstanceDto.getProcessInstanceId());

		logger.info("----��ֹ�������,terminateProcessInstanceDto��"
				+ GsonHelper.toJson(terminateProcessInstanceDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				terminateProcessInstanceDto);
		logger.info("----��ֹ����return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---��ֹ����return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto suspendProcessInstance(
			DubboSuspendProcessInstanceDto dubboSuspendProcessInstanceDto) {
		logger.info("----���������������----dubbo---���json��"+GsonHelper.toJson(dubboSuspendProcessInstanceDto));
		
		SuspendProcessInstanceDto suspendProcessInstanceDto = new SuspendProcessInstanceDto();
		suspendProcessInstanceDto.setSerial(UUID.randomUUID().toString());
		suspendProcessInstanceDto.setTime(DateHelper.getTime());
		suspendProcessInstanceDto.setFrom(dubboSuspendProcessInstanceDto.getFrom());
		suspendProcessInstanceDto.setTo(dubboSuspendProcessInstanceDto.getTo());
		suspendProcessInstanceDto.setAreaCode(dubboSuspendProcessInstanceDto.getAreaCode());
		suspendProcessInstanceDto.setProcessInstanceId(dubboSuspendProcessInstanceDto.getProcessInstanceId());
		suspendProcessInstanceDto.setFlowPassList(dubboSuspendProcessInstanceDto.getFlowPassList());
		
		logger.info("----�����������,suspendProcessInstanceDto��"
				+ GsonHelper.toJson(suspendProcessInstanceDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				suspendProcessInstanceDto);
		logger.info("----��������return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---��������return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto resumeProcessInstance(
			DubboResumeProcessInstanceDto dubboResumeProcessInstanceDto) {
		logger.info("----��������������----dubbo---���json��"+GsonHelper.toJson(dubboResumeProcessInstanceDto));
		
		ResumeProcessInstanceDto resumeProcessInstanceDto = new ResumeProcessInstanceDto();
		resumeProcessInstanceDto.setSerial(UUID.randomUUID().toString());
		resumeProcessInstanceDto.setTime(DateHelper.getTime());
		resumeProcessInstanceDto.setFrom(dubboResumeProcessInstanceDto.getFrom());
		resumeProcessInstanceDto.setTo(dubboResumeProcessInstanceDto.getTo());
		resumeProcessInstanceDto.setAreaCode(dubboResumeProcessInstanceDto.getAreaCode());
		resumeProcessInstanceDto.setProcessInstanceId(dubboResumeProcessInstanceDto.getProcessInstanceId());
		resumeProcessInstanceDto.setFlowPassList(dubboResumeProcessInstanceDto.getFlowPassList());
		
		logger.info("----����������,resumeProcessInstanceDto��"
				+ GsonHelper.toJson(resumeProcessInstanceDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				resumeProcessInstanceDto);
		logger.info("----�������return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---�������return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}

	@Override
	public DubboCommandResultDto suspendWorkItem(
			DubboSuspendWorkItemDto dubboSuspendWorkItemDto) {
		logger.info("----���������������----dubbo---���json��"+GsonHelper.toJson(dubboSuspendWorkItemDto));
		
		SuspendWorkItemDto suspendWorkItemDto = new SuspendWorkItemDto();
		suspendWorkItemDto.setSerial(UUID.randomUUID().toString());
		suspendWorkItemDto.setTime(DateHelper.getTime());
		suspendWorkItemDto.setFrom(dubboSuspendWorkItemDto.getFrom());
		suspendWorkItemDto.setTo(dubboSuspendWorkItemDto.getTo());
		suspendWorkItemDto.setAreaCode(dubboSuspendWorkItemDto.getAreaCode());
		suspendWorkItemDto.setProcessInstanceId(dubboSuspendWorkItemDto.getProcessInstanceId());
		suspendWorkItemDto.setFlowPassList(dubboSuspendWorkItemDto.getFlowPassList());
		suspendWorkItemDto.setWorkItemId(dubboSuspendWorkItemDto.getWorkItemId());
		
		logger.info("----�����������,suspendWorkItemDto��"
				+ GsonHelper.toJson(suspendWorkItemDto));
		CommandResultDto result = ServerProxy.getInstance().dealForCommandDto(
				suspendWorkItemDto);
		logger.info("----��������return,json��" + GsonHelper.toJson(result));

		DubboCommandResultDto resultDto = new DubboCommandResultDto();
		resultDto.setDealFlag(result.isDealFlag());
		resultDto.setDealMsg(result.getDealMsg());
		resultDto.setProcessInstanceId(result.getProcessInstanceId());
		resultDto.setSerial(result.getSerial());
		logger.info("----dubbo---��������return,json��"+ GsonHelper.toJson(resultDto));
		return resultDto;
	}
}
