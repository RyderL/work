package com.ztesoft.uosflow.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.service.ExceptionService;
import com.zterc.uos.fastflow.service.ProcessInstanceService;
import com.zterc.uos.fastflow.state.WMProcessInstanceState;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.client.CreateWorkOrderDto;
import com.ztesoft.uosflow.core.dto.server.SaveExceptionDto;
import com.ztesoft.uosflow.core.util.CommandPropUtil;
import com.ztesoft.uosflow.core.util.CommandResultDtoUtil;
import com.ztesoft.uosflow.inf.client.common.ClientProxy;
import com.ztesoft.uosflow.util.mq.producer.common.MqProducerProxy;

/**
 * 
 * 流程引擎的核心处理入口
 * 
 * @author gong.yi
 * 
 */
@Component("commandProxy")
public class CommandProxy {
	private Logger logger = LoggerFactory.getLogger(CommandProxy.class);

	@Autowired
	private MqProducerProxy mqProducerProxy;

	@Autowired
	private ExceptionService exceptionService;

	@Autowired
	private ProcessInstanceService processInstanceService;

	public static CommandProxy getInstance() {
		return (CommandProxy) ApplicationContextProxy.getBean("commandProxy");
	}

	public CommandResultDto dealCommand(CommandDto commandDto, Boolean isSyn) {
		return this.dealCommand(commandDto, null, isSyn);
	}

	public CommandResultDto dealCommand(CommandDto commandDto, Long errorId) {
		return this.dealCommand(commandDto, null, null);
	}

	public CommandResultDto dealCommand(CommandDto commandDto, Long errorId,
			Boolean isSyn) {
		logger.info("开始处理："+commandDto.getCommandCode()+"="+commandDto.getProcessInstanceId());
		CommandResultDto commandResultDto = null;
		try {
			// 如果同步
			if (CommandPropUtil.getInstance()
					.isSyn(commandDto.getCommandCode())
					|| (isSyn != null && isSyn)) {
				if (CommandPropUtil.getInstance().isTypeOfServer(
						commandDto.getCommandCode())) {
					commandResultDto = UosFlowProxy.getInstance().dealCommand(
							commandDto);
				} else {
					commandResultDto = ClientProxy.getInstance().dealCommand(
							commandDto);
				}
			}
			// 如果异步
			else {
				commandResultDto = mqProducerProxy.dealCommandAsyn(commandDto);
			}
		} catch (Exception e) {
			logger.error("命令处理异常，消息体："+GsonHelper.toJson(commandDto), e);
			commandResultDto = CommandResultDtoUtil.createCommandResultDto(
					commandDto, false, "消息处理失败:" + e.getMessage(), null);
		}
		logger.info(commandDto.getCommandCode()+"="+commandDto.getProcessInstanceId()+"处理结果："+commandResultDto.isDealFlag()+"===="+ commandResultDto.getDealMsg()+"");
		if (!commandResultDto.isDealFlag()&&!(commandDto instanceof SaveExceptionDto)) {
			if(commandResultDto.getCommandDto()==null){
				commandResultDto.setCommandDto(commandDto);
			}
			saveException(commandResultDto, errorId);
		}
		return commandResultDto;
	}

	public CommandResultDto dealCommand(CommandDto commandDto) {
		return this.dealCommand(commandDto, null, null);
	}

	@Transactional
	public void updateProcessInstance(ExceptionDto error) {
		logger.info("----进入修改流程实例状态----");
		if (error.getProcessInstanceId() != null && error.getProcessInstanceId() != 0) {
			ProcessInstanceDto processInstance = processInstanceService
					.queryProcessInstance(error.getProcessInstanceId()
							.toString());
			processInstance.setState(WMProcessInstanceState.OPEN_RUNNING_INT);
			processInstanceService.updateProcessInstance(processInstance,false);
			logger.info("----修改流程实例状态--结束--");
		}
	}

	@Transactional
	public void updateException(ExceptionDto exceptionDto, String state) {
		logger.info("----进入修改异常记录状态----");
		exceptionDto.setDealDate(DateHelper.getTimeStamp());
		exceptionDto.setState(state);
		exceptionService.updateException(exceptionDto);
		logger.info("----进入修改异常记录状态--结束--");
	}

	@Transactional
	public void saveException(CommandResultDto commandResultDto, Long errorId) {
		try {
			logger.error("saveException:"+commandResultDto.getCommandDto().getCommandCode()+ ";"
					+ commandResultDto.getCommandDto().getProcessInstanceId());

			SaveExceptionDto saveExceptionDto = new SaveExceptionDto();
			ExceptionDto exceptionDto = new ExceptionDto();
			if(commandResultDto.getDealMsg() != null && commandResultDto.getDealMsg().length() > 4000){
				String msg = commandResultDto.getDealMsg().substring(0, 4000);
				commandResultDto.setDealMsg(msg);
			}
			if (errorId == null) {
				exceptionDto.setMsg(JSONObject.toJSONString(commandResultDto.getCommandDto().toMap()));
				exceptionDto.setCreateDate(DateHelper.getTimeStamp());
				exceptionDto.setState(ExceptionDto.ERROR_INIT);
				if("createWorkOrder".equals(commandResultDto.getCommandDto().getCommandCode())){
					CreateWorkOrderDto createWorkOrderDto = (CreateWorkOrderDto) commandResultDto.getCommandDto();
					exceptionDto.setDealTimes(3);
					exceptionDto.setWorkItemId(createWorkOrderDto.getWorkItemId());
					exceptionDto.setTacheId(createWorkOrderDto.getTacheId());
					exceptionDto.setReasonClass("2");
					exceptionDto.setExceptionType("2201");//创建工单接口异常
				}else if("reportProcessState".equals(commandResultDto.getCommandDto().getCommandCode())){
					exceptionDto.setDealTimes(3);
					exceptionDto.setReasonClass("2");
					exceptionDto.setExceptionType("2202");
				}else if("reportCalCondResult".equals(commandResultDto.getCommandDto().getCommandCode())){
					exceptionDto.setDealTimes(3);
					exceptionDto.setReasonClass("2");
					exceptionDto.setExceptionType("2203");
				}else{
					exceptionDto.setDealTimes(0);
				}
				exceptionDto.setCommandCode(commandResultDto.getCommandDto().getCommandCode());
				exceptionDto.setErrorInfo(commandResultDto.getDealMsg());
				exceptionDto.setAreaId(commandResultDto.getCommandDto().getAreaCode());
				if (!StringHelper.isEmpty(commandResultDto.getCommandDto().getProcessInstanceId())) {
					exceptionDto.setProcessInstanceId(LongHelper.valueOf(commandResultDto.getCommandDto().getProcessInstanceId()));
					saveExceptionDto.setProcessInstanceId(commandResultDto.getCommandDto().getProcessInstanceId());
				}else{
					exceptionDto.setProcessInstanceId(0L);
					saveExceptionDto.setProcessInstanceId("0");
				}
				saveExceptionDto.setExceptionDto(exceptionDto);
				saveExceptionDto.setOperType(OperType.INSERT);
			} else {
				exceptionDto.setId(errorId);
				exceptionDto.setProcessInstanceId(LongHelper.valueOf(commandResultDto.getCommandDto().getProcessInstanceId()));
				exceptionDto.setDealDate(DateHelper.getTimeStamp());
				exceptionDto.setState(ExceptionDto.ERROR_INIT);
				exceptionDto.setErrorInfo(commandResultDto.getDealMsg());
				exceptionDto.setAreaId(commandResultDto.getCommandDto().getAreaCode());
				exceptionDto.setCommandCode(commandResultDto.getCommandDto().getCommandCode());
				saveExceptionDto.setExceptionDto(exceptionDto);
				saveExceptionDto.setOperType(OperType.UPDATE);
				saveExceptionDto.setProcessInstanceId(commandResultDto.getCommandDto().getProcessInstanceId());
			}
			saveExceptionDto.setFrom(commandResultDto.getCommandDto().getFrom());
			saveExceptionDto.setTo(commandResultDto.getCommandDto().getTo());
			CommandProxy.getInstance().dealCommand(saveExceptionDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
