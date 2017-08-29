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
		
		//���commandDto.getProcessInstanceId()Ϊ�գ����Ǵ�������ʵ���ӿڣ���������ʵ���ӿڲ��ÿ��������ڴ�ģ�ͳ�ʼ��
		if (!StringHelper.isEmpty(commandDto.getProcessInstanceId())
				&& !("saveCommandQueue".equals(commandDto.getCommandCode())
						|| "persistProcessModel".equals(commandDto.getCommandCode())
						|| "persistProcessModelRemote".equals(commandDto.getCommandCode()))){
			boolean needLock = true;
			
			/**
			 * ����ע���߼������������ڿ��ƣ���ֹ���л��ڶ��̲߳������̲���Ťת������  add by bobping on 
			 */
//			if(FastflowConfig.isCacheModel){
//				//UosFlowProxy����Ľӿ����ڴ�ģʽ��Ҫ��ʼ���ڴ�ģ�Ͷ���
//				if (logger.isInfoEnabled()) {
//					logger.info(commandDto.getCommandCode()+ "--redis--getObject:[key:"+ commandDto.getProcessInstanceId() + "]");
//				}
//				ProcessModel processModel = CommandInvokeUtil.getCache(Long.valueOf(commandDto.getProcessInstanceId()));
//				ProcessLocalHolder.set(processModel);
////				logger.info(commandDto.getCommandCode()+ "--redis--getObject:[key:"+ commandDto.getProcessInstanceId() + "]---processModel:"+GsonHelper.toJson(processModel));
//				
//				//�����첽�־û����saveException��persistProcessModel����Ҫ������
//				if(!commandDto.getCommandCode().equals("saveException")
//						&&!commandDto.getCommandCode().equals("persistProcessModel")){
//					needLock = false;
//				}
//			}
			
			//�����LockFactory����BLANK,����������Ҫ���ڴ������ܲ��Ե�ʱ��ʹ�ã�Ĭ��LOCAL����
			if(LockFactory.isNoLock()){
				needLock = false;
			}
			//���ӿ��أ����û������Ҫ�����������Ĭ��ȫ������������ֻ��������Ҫ������������� add by che.zi 20170729
			if(FastflowConfig.needLockCommand != null && !"".equals(FastflowConfig.needLockCommand)
					 && !FastflowConfig.lockCommands.contains(commandDto.getCommandCode())){
				needLock = false;
				logger.info("----����Ҫ����---�������:"+commandDto.getCommandCode());
			}
			//end 20170729
			
			//���÷ֿ�����Դ
			String ds = DsContextHolder.getHoldDs();
			//�����Ҫ����
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
								throw new FastflowException("���ȴ���ʱ-----processInstanceId="+processInstanceId);
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
				//����Ҫ����
				else{
					this.setProcessModelThreadLocal(commandDto);
					return CommandInvokeUtil.invoke(this, commandDto);
				}
			} catch (Exception e) {
				throw e;
			} finally{
				//��ԭѡ�������Դ
				DsContextHolder.setHoldDs(ds);
			}
		}else{
			return CommandInvokeUtil.invoke(this, commandDto);
		}
	}

	/**
	 * �ڴ�ģʽ���ڱ���processModel�̱߳���
	 * @param commandDto   
	 * @author bobping
	 * @date 2017��3��1��
	 */
	public void setProcessModelThreadLocal(CommandDto commandDto){
		if(FastflowConfig.isCacheModel){
			//UosFlowProxy����Ľӿ����ڴ�ģʽ��Ҫ��ʼ���ڴ�ģ�Ͷ���
			if (logger.isInfoEnabled()) {
				logger.info(commandDto.getCommandCode()+ "--redis--getObject:[key:"+ commandDto.getProcessInstanceId() + "]");
			}
			ProcessModel processModel = CommandInvokeUtil.getCache(Long.valueOf(commandDto.getProcessInstanceId()));
			ProcessLocalHolder.set(processModel);
		}
	}
	
	/**
	 * ��������ʵ��
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
			/** ��������ʵ�� */
			ProcessInstanceDto processInstanceDto = null;
			processInstanceDto = fastflowRunner.createProcessInstanceByCode(
					processDefinitionCode, null, processInstanceName,
					Integer.parseInt(processPriority), formData, areaCode,
					false);

			// logger.error("��������ʵ������: " + (time2 -time1));
			if (processInstanceDto != null) {
				retDto =  CommandResultDtoUtil.createCommandResultDto(
						commandDto, true, "��������ʵ���ɹ�", processInstanceDto
								.getProcessInstanceId().toString());
				retDto.setFlowParamList(processInstanceDto.getFlowParamMap());
				retDto.setFlowPassList(createProcessInstacneDto.getFlowPassList());
				logger.debug("=====>>>createProcessInstance�� "
						+ "��������ʵ���ɹ�����������ͻ��˷��ص�����ʵ��ID��"
						+ processInstanceDto.getProcessInstanceId());
			} else {
				retDto = CommandResultDtoUtil.createCommandResultDto(
						commandDto, false, "��������ʵ��ʧ��", "-1");
				logger.error("��������ʵ��ʧ��!");
			}
		} catch (Exception e) {
			logger.error("��������ʵ���쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "��������ʵ���쳣��", "-1");
			throw e;
		}
		return retDto;
	}

	/**
	 * ��������������ʵ��
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
			/** ��������ʵ�� */
			ProcessInstanceDto processInstanceDto = null;
			processInstanceDto = fastflowRunner.createProcessInstanceByCode(
					processDefinitionCode, null, processInstanceName,
					Integer.parseInt(processPriority), formData, areaCode,
					false);
			long time2 = System.currentTimeMillis();
			if (processInstanceDto != null) {
				retDto = CommandResultDtoUtil.createCommandResultDto(
						commandDto, true, "��������ʵ���ɹ�", processInstanceDto
								.getProcessInstanceId().toString());
				retDto.setFlowParamList(formData);
				retDto.setFlowPassList(createProcessInstacneDto.getFlowPassList());
				logger.debug("=====>>>createProcessInstance�� "
						+ "��������ʵ���ɹ�����������ͻ��˷��ص�����ʵ��ID��"
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
				logger.debug("��������ʵ������: " + (time3 - time2));

			} else {
				retDto = CommandResultDtoUtil.createCommandResultDto(
						commandDto, false, "��������ʵ��ʧ��", "-1");
				logger.error("��������ʵ��ʧ��!");
			}
		} catch (Exception e) {
			logger.error("��������ʵ���쳣, �쳣��Ϣ��" + e, e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "��������ʵ���쳣��", "-1");
			throw e;
		}
		return retDto;
	}

	/**
	 * ��������ʵ��
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
					true, "��������ʵ������ͳɹ�",
					startProcessInstacneDto.getProcessInstanceId());
			logger.debug("=====>>>startProcessInstance�� "
					+ "��������ʵ�������ѷ��ͣ�processInstanceId��" + processInstanceId);
		} catch (Exception e) {
			logger.error("��������ʵ���쳣, �쳣��Ϣ��" + e, e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "��������ʵ��������쳣",
					startProcessInstacneDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	// /**
	// * ִ��������ת
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
	// "ִ��������ת�ɹ�"
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.debug( "=============>>>executeEfferentTransitions�� " +
	// "ִ��������ת�����ѷ��ͣ�activity��" + activity);
	// } catch (Exception e) {
	// logger.error("ִ��������ת�����쳣, �쳣��Ϣ��" + e);
	// retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, false,
	// "ִ��������ת�����쳣, �쳣��Ϣ��" + e
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// }
	// return retDto;
	// }
	//
	// /**
	// * ִ��·��
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
	// "ִ��·������ɹ�"
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.debug( "=============>>>executeTransition�� " +
	// "ִ��·�������ѷ��ͣ�activity��" + activity);
	// } catch (Exception e) {
	// retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, false,
	// "ִ��·�������쳣, �쳣��Ϣ��" + e
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.error("ִ��·�ɷ����쳣, �쳣��Ϣ��" + e, e);
	// }
	// return retDto;
	// }
	//
	// /**
	// * ����ʵ��
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
	// "����ʵ��ת�ɹ�"
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.debug( "=============>>>enableActivityInstances�� " +
	// "����ʵ��ת�����ѷ��ͣ�activity��" + activity);
	// } catch (Exception e) {
	// retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, false,
	// "����ʵ���쳣, �쳣��Ϣ��" + e
	// ,LongUtils.valueOf(processInstance.getProcessInstanceId()));
	// logger.error("����ʵ�������쳣, �쳣��Ϣ��" + e);
	// }
	// return retDto;
	// }

	/**
	 * �ύ������
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
						true, "�ύ������ִ�гɹ�", workItemId);
//			}else{
//				retDto = CommandProxy.getInstance().dealCommand(completeWorkItemDto);
//			}
			
			logger.debug("=============>>>completeWorkItem�� "
					+ "�ύ�����������ѷ��ͣ�workItemId��" + workItemId);
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�ύ������ִ��ʧ��", null);
			logger.error("�ύ������ִ���쳣, �쳣��Ϣ��", e);
			throw e;
		}

		return retDto;
	}

	/**
	 * �ع�����ʵ��
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
					true, "�����ɹ�", processInstanceId);
			logger.debug("==========================>>>rollbackProcessInstance�� "
					+ "�����ɹ���processInstanceId��" + processInstanceId);
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�����쳣", processInstanceId);
			logger.error("�����쳣, �쳣��Ϣ��" + e);
			throw e;
		}
		return retDto;
	}

	/**
	 * ������ת
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
					true, "������ת�ɹ�", processInstanceId);
			logger.debug("==========================>>>processInstanceJump�� "
					+ "������ת�����ѷ��ͣ�processInstanceId��" + processInstanceId);
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "������ת�쳣", processInstanceId);
			throw e;
		}
		return retDto;
	}

	/**
	 * ��������ʵ��
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
					true, "��������ʵ������ͳɹ�",
					abortProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>abortProcessInstance�� "
					+ "��������ʵ�������ѷ��ͣ�processInstanceId��" + processInstanceId);
		} catch (Exception e) {
			logger.error("��������ʵ���쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "��������ʵ��������쳣",
					abortProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * ��ֹ����ʵ��
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
					true, "��ֹ����ʵ������ͳɹ�",
					terminateProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>terminateProcessInstance�� "
					+ "��ֹ����ʵ�������ѷ��ͣ�processInstanceId��" + processInstanceId);
		} catch (Exception e) {
			logger.error("��ֹ����ʵ���쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "��ֹ����ʵ��������쳣",
					terminateProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * ��������ʵ��
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
					true, "��������ʵ������ͳɹ�",
					suspendProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>suspendProcessInstance�� "
					+ "��������ʵ�������ѷ��ͣ�processInstanceId��" + processInstanceId);
		} catch (Exception e) {
			logger.error("��������ʵ���쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "��������ʵ��������쳣",
					suspendProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * �ָ�����ʵ��
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
					true, "�ָ�����ʵ������ͳɹ�",
					resumeProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>resumeProcessInstance�� "
					+ "�ָ�����ʵ�������ѷ��ͣ�processInstanceId��" + processInstanceId);
		} catch (Exception e) {
			logger.error("�ָ�����ʵ���쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�ָ�����ʵ��������쳣",
					resumeProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * ��������ʵ��
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
					true, "��������ʵ������ͳɹ�",
					restartProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>restartProcessInstance�� "
					+ "��������ʵ�������ѷ��ͣ�processInstanceId��" + processInstanceId);
		} catch (Exception e) {
			logger.error("��������ʵ���쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "��������ʵ��������쳣",
					restartProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}

	/**
	 * ���̻��ˣ��˵���
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
			logger.info("--reasonConfigId��" + reasonConfigId+",workItemId:"+workItemId+",reasonCode:"+reasonCode);
			if(!StringHelper.isEmpty(targetWorkItemId)){
				if(FastflowConfig.rollbackBySingle){
					fastflowRunner.disableWorkItemByTarget(workItemId, targetWorkItemId,reasonType, memo, areaId, false, flowPassList);
				}else{
					List<WorkItemDto> col = fastflowRunner.findCanRollBackWorkItemsByTarget(disableWorkItemDto.getProcessInstanceId(),disableWorkItemDto.getWorkitemId(),targetWorkItemId,reasonType,false,areaId);
					fastflowRunner.disableWorkItemByTarget(workItemId, targetWorkItemId,reasonType, memo, areaId, false, flowPassList);

					//���л����˵�������������δ��ɹ�����Ҳ����
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

					//���л����˵�������������δ��ɹ�����Ҳ����
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
					true, "���������˵�����ͳɹ�",
					disableWorkItemDto.getProcessInstanceId());
			logger.debug("=====>>>disableWorkItem�� "
					+ "���������˵������ѷ��ͣ�processInstanceId��"
					+ disableWorkItemDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("----���������˵�ʧ�ܣ�" + e.getMessage(), e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "���������˵�������쳣",
					disableWorkItemDto.getProcessInstanceId());
			throw e;
		}finally{
			retDto.setFlowPassList(flowPassList);
		}
		return retDto;
	}

	/**
	 * �첽�־û��ڴ�ģ��
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public CommandResultDto persistProcessModel(CommandDto commandDto)
			throws Exception {
		logger.info("-------�첽�־û��ڴ�ģ��------commandDto:"+GsonHelper.toJson(commandDto));
		CommandResultDto retDto = null;
		PersistProcessModelDto persistProcessModelDto = (PersistProcessModelDto) commandDto;
		ProcessModel processModel = persistProcessModelDto.getProcessModel();
		try {
			//��sql���浽�̱߳�����־����Ϊtrue
			SqlLocalHolder.setHoldSqlOn(true);
			fastflowRunner.persistProcessModel(processModel);
			if (processModel.getProcessInstanceDto() != null
					&& processModel.getProcessInstanceDto().getState() == WMProcessInstanceState.CLOSED_COMPLETED_INT) {
				CommandInvokeUtil
						.clearCacheForDone(Long.valueOf(persistProcessModelDto
								.getProcessInstanceId()));
			}
			
			//���̱߳�����ȡ�־û�����
			List<AsynSqlExecBy3thParamDto> sqlParams = SqlLocalHolder.get();
			for(AsynSqlExecBy3thParamDto sqlParam : sqlParams){
				String sql = sqlParam.getSqlStr();
				String key = sqlParam.getKey();
				String tableName = sqlParam.getTableName();
				int sqlType = sqlParam.getSqlType();
				int sqlSeq = sqlParam.getSqlSeq();
				
				Object[] paramsList = sqlParam.getParam();
				
				AsyncSqlDto asyncSqlDto = null;
				//����״̬�����г־û�ʹ��AsyncSqlDto
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
					logger.info("����ƽ̨����־û�����"+ GsonHelper.toJson(sqlParam));
				}
				sqlPersistQueueUtils.putToQueue(asyncSqlDto);
			}
			
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "�첽�־û��ڴ�ģ�ͳɹ�", commandDto.getProcessInstanceId());
			logger.info("-------�첽�־û��ڴ�ģ�ͳɹ�------"+GsonHelper.toJson(retDto));
		} catch (Exception e) {
			logger.error("�첽�־û��ڴ�ģ���쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�첽�־û��ڴ�ģ���쳣��", "-1");
			throw e;
		}finally{
			//����߳�sql�̱߳���������sqlLocalHolder�̱߳�����
			SqlLocalHolder.clear();
		}
		return retDto;
	}
	
	/**
	 * 
	 * ���õ��������ƽ���Զ���첽�־û�
	 * @param commandDto
	 * @return
	 * @throws Exception   
	 * @author bobping
	 * @date 2017��4��6��
	 */
	public CommandResultDto persistProcessModelRemote(CommandDto commandDto)
			throws Exception {
		logger.info("-------�첽�־û��ڴ�ģ��------commandDto:"+GsonHelper.toJson(commandDto));
		CommandResultDto retDto = null;
		PersistProcessModelRemoteDto persistProcessModelRemoteDto = (PersistProcessModelRemoteDto) commandDto;
		ProcessModel processModel = persistProcessModelRemoteDto.getProcessModel();
		try {
			//��sql���浽�̱߳�����־����Ϊtrue
			SqlLocalHolder.setHoldSqlOn(true);
			
			fastflowRunner.persistProcessModel(processModel);
			if (processModel.getProcessInstanceDto() != null
					&& processModel.getProcessInstanceDto().getState() == WMProcessInstanceState.CLOSED_COMPLETED_INT) {
				CommandInvokeUtil
				.clearCacheForDone(Long.valueOf(persistProcessModelRemoteDto
						.getProcessInstanceId()));
			}
			
			//���̱߳�����ȡ�־û�����
//			long t1 = System.currentTimeMillis();
			List<AsynSqlExecBy3thParamDto> sqlParams = SqlLocalHolder.get();
			ExecuteSQLAsynBy3thDto executeSQLAsynBy3thDto = new ExecuteSQLAsynBy3thDto();
			executeSQLAsynBy3thDto.setParam(sqlParams);
			PersistBy3thClientProxy.getInstance().executeSQLAsynBy3th(executeSQLAsynBy3thDto);
			
//			logger.error("����Զ�̳־û�dubbo�ӿں�ʱms��"+(System.currentTimeMillis() - t1));
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "���õ����������첽�־û��ڴ�ģ�ͳɹ�", commandDto.getProcessInstanceId());
			
			logger.info("-------���õ����������첽�־û��ڴ�ģ�ͳɹ�------"+GsonHelper.toJson(retDto));
		} catch (Exception e) {
			logger.error("���õ����������첽�־û��ڴ�ģ�ͳɹ��쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "���õ����������첽�־û��ڴ�ģ�ͳɹ��쳣��", "-1");
			throw e;
		} finally{
			//����߳�sql�̱߳���������sqlLocalHolder�̱߳�����
			SqlLocalHolder.clear();
		}
		return retDto;
	}
	
	/**
	 * �洢�����쳣
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
					true, "�洢�����쳣�ɹ�", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("�洢�����쳣�쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�洢�����쳣�쳣��", "-1");
			throw e;
		}
		return retDto;
	}
	/**
	 * ��������
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
			dto.setTacheType(addTacheDto.getTacheType());// ����""���������StringHelper.valueOf����null
			dto.setPackageDefineCodes(addTacheDto.getPackageDefineCodes());
			dto.setCreateDate(DateHelper.getTimeStamp());
			dto.setStateDate(DateHelper.getTimeStamp());
			dto.setEffDate(new Timestamp(addTacheDto.getEffDate().getTime()));
			dto.setExpDate(new Timestamp(addTacheDto.getExpDate().getTime()));
			tacheService.addTache(dto);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "������ӳɹ���", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�������ʧ�ܣ�"+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("�������ʧ�ܣ�"+e.getMessage(),e);
		}
		return retDto;
	}
	

	/**
	 * �����޸�
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
					true, "�����޸ĳɹ���", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�����޸��쳣��"+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("�����޸�ʧ�ܣ�"+e.getMessage(),e);
		}
		return retDto;
	}
	/**
	 * ����ɾ��
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
					true, "����ɾ���ɹ�", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "����ɾ���쳣��"+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("����ɾ��ʧ�ܣ�"+e.getMessage(),e);
		}
		return retDto;
	}

	/**
	 * �쳣ԭ������
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
					true, "�쳣ԭ����ӳɹ�", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�쳣ԭ�����ʧ�ܣ�"+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("�쳣ԭ�����ʧ�ܣ�"+e.getMessage(),e);
		}
		return retDto;
	}

	/**
	 * �쳣ԭ���޸�
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
					true, "�쳣ԭ���޸ĳɹ�", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�쳣ԭ���޸�ʧ�ܣ�"+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("�쳣ԭ���޸�ʧ�ܣ�"+e.getMessage(),e);
		}
		return retDto;
	}
	/**
	 * �쳣ԭ��ɾ��
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
					true, "�쳣ԭ��ɾ���ɹ�", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�쳣ԭ��ɾ��ʧ�ܣ�"+e.getMessage(), commandDto.getProcessInstanceId());
			logger.error("�쳣ԭ��ɾ��ʧ�ܣ�"+e.getMessage(),e);
		}
		return retDto;
	}

	/**
	 * �޸�����ʵ��״̬
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
					true, "�޸�����ʵ������ͳɹ�",
					updateProcessInstanceDto.getProcessInstanceId());
			logger.debug("=====>>>updateProcessInstanceDto�� "
					+ "�޸�����ʵ�������ѷ��ͣ�processInstanceId��" + processInstanceId);
		} catch (Exception e) {
			logger.error("�޸�����ʵ���쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�޸�����ʵ��������쳣",
					updateProcessInstanceDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}
	
	/**
	 * �洢�ӿ���Ϣ
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
					true, "�洢�ӿ���Ϣ�ɹ�", commandDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("�洢�ӿ���Ϣ�쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�洢�ӿ���Ϣ�쳣��", "-1");
			throw e;
		}
		return retDto;
	}
	
	/**
	 * ��ʷ����ת��
	 * @param commandDto
	 * @return
	 */
	public CommandResultDto dataToHis(CommandDto commandDto){
		CommandResultDto retDto = null;
		DataToHisDto dataToHisDto = (DataToHisDto) commandDto;
		try {
			fastflowRunner.dataToHis(dataToHisDto.getProcessInstanceId());
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, true, "��ʷ����ת��ɹ�", dataToHisDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("��ʷ����ת��ӿ���Ϣ�쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto, false, "��ʷ����ת��ʧ��", dataToHisDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}
	/**
	 * ��������
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
					true, "������������ͳɹ�",
					suspendWorkItemDto.getProcessInstanceId());
			logger.debug("=====>>>suspendProcessInstance�� "
					+ "�������������ѷ��ͣ�processInstanceId��" + suspendWorkItemDto.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("���������쳣, �쳣��Ϣ��" + e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "��������������쳣",
					suspendWorkItemDto.getProcessInstanceId());
			throw e;
		}
		return retDto;
	}
}
