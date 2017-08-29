package com.ztesoft.uosflow.inf.client.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zterc.uos.base.helper.GsonHelper;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.client.CreateWorkOrderDto;
import com.ztesoft.uosflow.core.dto.client.ReportCalCondResultDto;
import com.ztesoft.uosflow.core.dto.client.ReportProcessStateDto;
import com.ztesoft.uosflow.core.dto.client.ReportTimeLimitDto;
import com.ztesoft.uosflow.core.dto.result.CreateWorkOrderResultDto;
import com.ztesoft.uosflow.dubbo.dto.DubboCommandResultDto;
import com.ztesoft.uosflow.dubbo.dto.client.DubboCreateWorkOrderDto;
import com.ztesoft.uosflow.dubbo.dto.client.DubboReportCalCondResultDto;
import com.ztesoft.uosflow.dubbo.dto.client.DubboReportProcessStateDto;
import com.ztesoft.uosflow.dubbo.dto.client.DubboReportTimeLimitDto;
import com.ztesoft.uosflow.dubbo.dto.result.DubboCreateWorkOrderResultDto;
import com.ztesoft.uosflow.dubbo.inf.client.FlowDubboServiceInf;

@Component("workFlowClient")
public class WorkFlowClient{
	
	private static Logger logger = LoggerFactory.getLogger(WorkFlowClient.class);
	
//	private FlowDubboServiceInf flowDubboService;
	
//	private Map<String,FlowDubboServiceInf> dubboClientMap;
//	
//	private Map<String,String> systemBeanMap;
//	
//	public Map<String, String> getSystemBeanMap() {
//		return systemBeanMap;
//	}
//
//	public void setSystemBeanMap(Map<String, String> systemBeanMap) {
//		this.systemBeanMap = systemBeanMap;
//	}
//	
////	public void init(){
////		if(systemBeanMap!=null){
////			dubboClientMap = new HashMap<String, FlowDubboServiceInf>();
////			for(String key:systemBeanMap.keySet()){
////				dubboClientMap.put(key, (FlowDubboServiceInf) ApplicationContextProxy.getBean(systemBeanMap.get(key)));
////			}
////		}
////	}
//	
//	private FlowDubboServiceInf getService(String system){
//		logger.info("system is " + system);
//		if(systemBeanMap!=null){
//			if(dubboClientMap==null){
//				dubboClientMap = new HashMap<String, FlowDubboServiceInf>();
//			}
//			if(dubboClientMap.get(system)==null){
//				try {
//					dubboClientMap.put(system, (FlowDubboServiceInf) ApplicationContextProxy.getBean(systemBeanMap.get(system)));
//				} catch (Exception e) {
//					logger.error("-----dubboClientMap 设置异常："+e.getMessage(),e);
//					throw new FastflowException("flowDubboService为空，请检查配置：[system="
//							+system+"][systemBeanMap="+systemBeanMap+"][dubboClientMap="+dubboClientMap+"]");
//				}
//			}
//		}
//		FlowDubboServiceInf service = null;
//		if(dubboClientMap!=null){
//			service = dubboClientMap.get(system);
//			if(service==null){
//				throw new FastflowException("flowDubboService为空，请检查dubbo:reference的配置：[system="
//						+system+"][systemBeanMap="+systemBeanMap+"][dubboClientMap="+dubboClientMap+"]");
//			}
//		}else{
//			if(flowDubboService==null){
//				flowDubboService = (FlowDubboServiceInf) ApplicationContextProxy.getBean("flowDubboService");
//				logger.info("flowDubboService is " + flowDubboService);
//			}
//			service = flowDubboService;
//			if(service==null){
//				throw new FastflowException("flowDubboService为空，请检查dubbo:reference的配置");
//			}
//		}
//		return service;
//	}
	
	public CommandResultDto sendMessage(CommandDto commandDto,FlowDubboServiceInf service) throws Exception {
//		FlowDubboServiceInf service = getService(commandDto.getTo());
		
		String commandCode = commandDto.getCommandCode();
		CommandResultDto commonResultDto = new CommandResultDto();
		if("createWorkOrder".equals(commandCode)){
			CommandResultDto dto = null;
			for(int i=1;i<4;i++){
				try {
					dto = this.createWorkOrder(commandDto,service);
					break;
				} catch (Exception e) {
					logger.error("---调用业务系统创建工单接口异常:"+e.getMessage(),e);
					Thread.sleep(1000l*i);
					if(i==3){
						throw e;
					}
				}
			}
			return dto;
		}else if("reportProcessState".equals(commandCode)){
			for(int i=1;i<4;i++){
				try {
					commonResultDto = this.reportProcessState(commandDto,service);
					break;
				} catch (Exception e) {
					logger.error("---调用业务系统流程状态通知接口异常:"+e.getMessage(),e);
					Thread.sleep(1000l*i);
					if(i==3){
						throw e;
					}
				} 
			}
		}else if("reportCalCondResult".equals(commandCode)){
			for(int i=1;i<4;i++){
				try {
					commonResultDto = this.reportCalCondResult(commandDto,service);
					break;
				} catch (Exception e) {
					logger.error("---调用业务系统流程路由结果通知接口异常:"+e.getMessage(),e);
					Thread.sleep(1000l*i);
					if(i==3){
						throw e;
					}
				} 
			}
		}else if("reportTimeLimit".equals(commandCode)){
			for(int i=1;i<4;i++){
				try {
					commonResultDto = this.reportTimeLimit(commandDto,service);
					break;
				} catch (Exception e) {
					logger.error("---调用业务系统时限计算结果通知接口异常:"+e.getMessage(),e);
					Thread.sleep(1000l*i);
					if(i==3){
						throw e;
					}
				} 
			}
		}
		return commonResultDto;
	}
	
	private CommandResultDto reportTimeLimit(CommandDto commandDto,
			FlowDubboServiceInf service) {
		CommandResultDto commonResultDto = new CommandResultDto();
		ReportTimeLimitDto reportTimeLimitDto = (ReportTimeLimitDto)commandDto;
		DubboReportTimeLimitDto dReportTimeLimitDto = new DubboReportTimeLimitDto();
		dReportTimeLimitDto.setAreaCode(reportTimeLimitDto.getAreaCode());
		dReportTimeLimitDto.setFrom(reportTimeLimitDto.getFrom());
		dReportTimeLimitDto.setPrio(reportTimeLimitDto.getPriority());
		dReportTimeLimitDto.setProcessInstanceId(reportTimeLimitDto.getProcessInstanceId());
		dReportTimeLimitDto.setSerial(reportTimeLimitDto.getSerial());
		dReportTimeLimitDto.setTacheCode(reportTimeLimitDto.getTacheCode());
		dReportTimeLimitDto.setTime(reportTimeLimitDto.getTime());
		dReportTimeLimitDto.setTo(reportTimeLimitDto.getTo());
		dReportTimeLimitDto.setWorkItemId(reportTimeLimitDto.getWorkItemId());
		dReportTimeLimitDto.setLimitDate(reportTimeLimitDto.getLimitDate());
		dReportTimeLimitDto.setAlertDate(reportTimeLimitDto.getAlertDate());
		logger.info("-----业务系统时限计算结果通知入参：："+GsonHelper.toJson(dReportTimeLimitDto));
		DubboCommandResultDto dcommonResultDto = service.reportTimeLimit(dReportTimeLimitDto);
		commonResultDto.setCommandDto(commandDto);
		commonResultDto.setDealFlag(dcommonResultDto.isDealFlag());
		commonResultDto.setDealMsg(dcommonResultDto.getDealMsg());
		commonResultDto.setProcessInstanceId(dcommonResultDto.getProcessInstanceId());
		return commonResultDto;
	}

	private CommandResultDto reportCalCondResult(CommandDto commandDto,
			FlowDubboServiceInf service) {
		CommandResultDto commonResultDto = new CommandResultDto();
		ReportCalCondResultDto reportCalCondResultDto = (ReportCalCondResultDto)commandDto;
		DubboReportCalCondResultDto dReportCalCondResultDto = new DubboReportCalCondResultDto();
		dReportCalCondResultDto.setAreaCode(reportCalCondResultDto.getAreaCode());
		dReportCalCondResultDto.setFrom(reportCalCondResultDto.getFrom());
		dReportCalCondResultDto.setPrio(reportCalCondResultDto.getPriority());
		dReportCalCondResultDto.setProcessInstanceId(reportCalCondResultDto.getProcessInstanceId());
		dReportCalCondResultDto.setSerial(reportCalCondResultDto.getSerial());
		dReportCalCondResultDto.setTacheCode(reportCalCondResultDto.getTacheCode());
		dReportCalCondResultDto.setTime(reportCalCondResultDto.getTime());
		dReportCalCondResultDto.setTo(reportCalCondResultDto.getTo());
		dReportCalCondResultDto.setFlowPassMap(reportCalCondResultDto.getFlowPassMap());
		dReportCalCondResultDto.setIsPassed(reportCalCondResultDto.getIsPassed());
		logger.info("-----业务系统流程路由结果通知入参：："+GsonHelper.toJson(dReportCalCondResultDto));
		DubboCommandResultDto dcommonResultDto = service.reportCalCondResult(dReportCalCondResultDto);
		commonResultDto.setCommandDto(commandDto);
		commonResultDto.setDealFlag(dcommonResultDto.isDealFlag());
		commonResultDto.setDealMsg(dcommonResultDto.getDealMsg());
		commonResultDto.setProcessInstanceId(dcommonResultDto.getProcessInstanceId());
		return commonResultDto;
	}

	private CommandResultDto reportProcessState(CommandDto commandDto,
			FlowDubboServiceInf service) {
		CommandResultDto commonResultDto = new CommandResultDto();
		ReportProcessStateDto reportProcessStateDto = (ReportProcessStateDto)commandDto;
		DubboReportProcessStateDto dReportProcessStateDto = new DubboReportProcessStateDto();
		dReportProcessStateDto.setAreaCode(reportProcessStateDto.getAreaCode());
		dReportProcessStateDto.setComment(reportProcessStateDto.getComment());
		dReportProcessStateDto.setFrom(reportProcessStateDto.getFrom());
		dReportProcessStateDto.setPrio(reportProcessStateDto.getPriority());
		dReportProcessStateDto.setProcessInstanceId(reportProcessStateDto.getProcessInstanceId());
		dReportProcessStateDto.setSerial(reportProcessStateDto.getSerial());
		dReportProcessStateDto.setState(reportProcessStateDto.getState());
		dReportProcessStateDto.setTime(reportProcessStateDto.getTime());
		dReportProcessStateDto.setTo(reportProcessStateDto.getTo());
		dReportProcessStateDto.setFlowPassMap(reportProcessStateDto.getFlowPassMap());
		dReportProcessStateDto.setErrMsg(reportProcessStateDto.getErrMsg());
		logger.info("-----业务系统流程状态通知入参：："+GsonHelper.toJson(dReportProcessStateDto));
		DubboCommandResultDto dcommonResultDto = service.reportProcessState(dReportProcessStateDto);
		commonResultDto.setCommandDto(commandDto);
		commonResultDto.setDealFlag(dcommonResultDto.isDealFlag());
		commonResultDto.setDealMsg(dcommonResultDto.getDealMsg());
		commonResultDto.setProcessInstanceId(dcommonResultDto.getProcessInstanceId());
		return commonResultDto;
	}

	private CommandResultDto createWorkOrder(CommandDto commandDto,
			FlowDubboServiceInf service) {
		CreateWorkOrderDto createWorkOrderDto = (CreateWorkOrderDto)commandDto;
		DubboCreateWorkOrderDto dCreateWorkOrderDto = new DubboCreateWorkOrderDto();
		dCreateWorkOrderDto.setAreaCode(createWorkOrderDto.getAreaCode());
		dCreateWorkOrderDto.setBatchId(createWorkOrderDto.getBatchId());
		dCreateWorkOrderDto.setDirection(createWorkOrderDto.getDirection());
		dCreateWorkOrderDto.setFlowParamMap(createWorkOrderDto.getFlowParamMap());
		dCreateWorkOrderDto.setFlowPassMap(createWorkOrderDto.getFlowPassMap());
		dCreateWorkOrderDto.setFrom(createWorkOrderDto.getFrom());
		dCreateWorkOrderDto.setPrio(createWorkOrderDto.getPriority());
		dCreateWorkOrderDto.setProcessInstanceId(createWorkOrderDto.getProcessInstanceId());
		dCreateWorkOrderDto.setSerial(createWorkOrderDto.getSerial());
		dCreateWorkOrderDto.setTacheCode(createWorkOrderDto.getTacheCode());
		dCreateWorkOrderDto.setTacheName(createWorkOrderDto.getTacheName());
		dCreateWorkOrderDto.setTacheId(createWorkOrderDto.getTacheId());
		dCreateWorkOrderDto.setTime(createWorkOrderDto.getTime());
		dCreateWorkOrderDto.setTo(createWorkOrderDto.getTo());
		dCreateWorkOrderDto.setWorkItemId(createWorkOrderDto.getWorkItemId());
		dCreateWorkOrderDto.setRelaWorkItemId(createWorkOrderDto.getRelaWorkItemId());
		dCreateWorkOrderDto.setReturnToStart(createWorkOrderDto.getReturnToStart());
		logger.info("-----业务系统创建工单入参：："+GsonHelper.toJson(dCreateWorkOrderDto));
		DubboCreateWorkOrderResultDto dcreateWorkOrderResultDto = service.createWorkOrder(dCreateWorkOrderDto);
		CreateWorkOrderResultDto dto = new CreateWorkOrderResultDto();
		dto.setCommandDto(commandDto);
		dto.setDealFlag(dcreateWorkOrderResultDto.isDealFlag());
		dto.setDealMsg(dcreateWorkOrderResultDto.getDealMsg());
		dto.setProcessInstanceId(dcreateWorkOrderResultDto.getProcessInstanceId());
		return dto;
	}

}
