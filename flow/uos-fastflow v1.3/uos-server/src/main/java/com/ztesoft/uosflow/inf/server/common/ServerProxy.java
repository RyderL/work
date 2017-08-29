package com.ztesoft.uosflow.inf.server.common;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.zterc.uos.fastflow.service.CommandQueueService;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.config.ConfigContext;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.server.CompleteWorkItemDto;
import com.ztesoft.uosflow.core.dto.server.SaveCommandQueueDto;
import com.ztesoft.uosflow.core.util.CommandPropUtil;
import com.ztesoft.uosflow.inf.util.HttpClientUtils;
import com.ztesoft.uosflow.inf.util.JvmParamUtil;
import com.ztesoft.uosflow.inf.util.ServerJsonUtil;

/**
 * 
 * server�˵Ľӿں��Ĵ����� ��Ҫ�ǿ��ǵ��ӿ�ģʽ�Ĳ�һ����������dealForJson/������dealForXml֮�ࡣ
 * ���ģʽ�����Խ�һ���Ż�����ʱ������
 * 
 * @author gong.yi
 * 
 */
@Component("serverProxy")
public class ServerProxy {
	private Logger logger = LoggerFactory.getLogger(ServerProxy.class);
	@Autowired
	private CommandQueueService commandQueueService;

	/**
	 * ˽�л��޲ι��캯��
	 */
	private ServerProxy() {
	}

	public static ServerProxy getInstance() {
		return (ServerProxy) ApplicationContextProxy.getBean("serverProxy");
	}

	/**
	 * ���json��ʽ�Ľӿڽ�������
	 * 
	 * @param jsonParams
	 * @return
	 */
	public String dealForJson(String jsonParams) {
		if (logger.isInfoEnabled()) {
			logger.info("������Ϣ��" + jsonParams);
		}
		String retJson = null;
		Map<String, Object> paramsMap = GsonHelper.toMap(jsonParams);
		String commandCode = StringHelper.valueOf(paramsMap.get(InfConstant.INF_COMMAND_CODE));
		String processInstanceId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_PROCESSINSTANCEID));
		boolean isTypeOfServer = true;
		if(commandCode!=null&&commandCode.startsWith("qry")){
			retJson = QueryProxy.getInstance().dealForJson(jsonParams);
			if (ConfigContext.isNeedInfPersist()) {
				// �ڽӿڱ��в���
				SaveCommandQueueDto saveCommandQueueDto = new SaveCommandQueueDto();
				CommandQueueDto dto = new CommandQueueDto();
				dto.setCommandCode(commandCode);
				dto.setCommandMsg(jsonParams);
				dto.setCommandResultMsg(retJson);
				Map<String,Object> retMap = GsonHelper.toMap(retJson);
				dto.setProcessInstanceId(LongHelper.valueOf(paramsMap.get(InfConstant.INF_PROCESSINSTANCEID))==null ?LongHelper.valueOf(retMap.get(InfConstant.INF_PROCESSINSTANCEID)):LongHelper.valueOf(paramsMap.get(InfConstant.INF_PROCESSINSTANCEID)));
				dto.setState(0L);
				dto.setCreateDate(DateHelper.getTimeStamp());
				dto.setRoute(0L);
				dto.setAreaId(StringHelper.valueOf(paramsMap.get(InfConstant.INF_AREA_CODE)));
				saveCommandQueueDto.setCommandQueueDto(dto);
				saveCommandQueueDto.setProcessInstanceId(StringHelper.valueOf(dto.getProcessInstanceId()) == null?"":StringHelper.valueOf(dto.getProcessInstanceId()));
				if(StringHelper.isEmpty(saveCommandQueueDto.getProcessInstanceId())){
					saveCommandQueueDto.setProcessInstanceId("10");
				}
				CommandProxy.getInstance().dealCommand(saveCommandQueueDto);
			}
		}else{
			// ��������
			CommandDto cmdDto = ServerJsonUtil.getCommandDtoFromJson(commandCode,paramsMap);

			isTypeOfServer = CommandPropUtil.getInstance().isTypeOfServer(commandCode);
			String localAddr = JvmParamUtil.getInstance().getServerAddr();
			logger.info("----��ǰ�����ַ��"+localAddr);
			if(!FastflowConfig.isCacheModel && isTypeOfServer && ConfigContext.isNeedInfPersist()){
				if(!"createProcessInstance".equals(commandCode)
						&& !"createAndStartProcessInstance".equals(commandCode)){
					CommandQueueDto oldCommandQueueDto = commandQueueService.qryCommandQueueDto(processInstanceId,"createProcessInstance");
					String oldAddr = oldCommandQueueDto.getServerAddr();
					logger.info("---��������������ķ�������ַ��"+oldAddr);
					//��ǰ��������ַ�ʹ�������������ķ�������ַ��ͬ��Ҫ������͵�������������ķ�������ַ��
					if(localAddr != null && oldAddr !=null && !localAddr.equals(oldAddr)){
						String url = "http://"+oldAddr+"/restful/call";
						logger.info("----http�����ַ:"+url);
						String result = HttpClientUtils.sendHttpPost(url, jsonParams);
						return result;
					}
				}
			}
			if(cmdDto != null){
				CommandResultDto result = dealForCommandDto(cmdDto);
				retJson = ServerJsonUtil.getJsonFromCommandResultDto(result);
			}
		}
		return retJson;
	}

	public CommandResultDto dealForCommandDto(CommandDto commandDto) {
		String localAddr = JvmParamUtil.getInstance().getServerAddr();
		logger.info("----��ǰ�����ַ��"+localAddr);
		boolean isTypeOfServer = CommandPropUtil.getInstance().isTypeOfServer(
				commandDto.getCommandCode());
		if(!FastflowConfig.isCacheModel && isTypeOfServer && ConfigContext.isNeedInfPersist()){
			if(!"createProcessInstance".equals(commandDto.getCommandCode())
					&& !"createAndStartProcessInstance".equals(commandDto.getCommandCode())){
				CommandQueueDto oldCommandQueueDto = commandQueueService.qryCommandQueueDto(commandDto.getProcessInstanceId(),"createProcessInstance");
				if(oldCommandQueueDto != null){
					String oldAddr = oldCommandQueueDto.getServerAddr();
					logger.info("---��������������ķ�������ַ��"+oldAddr);
					//��ǰ��������ַ�ʹ�������������ķ�������ַ��ͬ��Ҫ������͵�������������ķ�������ַ��
					if(localAddr != null && oldAddr !=null && !localAddr.equals(oldAddr)){
						String url = "http://"+oldAddr+"/restful/call";
						logger.info("----http�����ַ:"+url);
						String result = HttpClientUtils.sendHttpPost(url, GsonHelper.toJson(commandDto));
						return ServerJsonUtil.getCommandResultDtoFromJson(result);
					}
				}
			}
		}
		CommandResultDto commandResultDto = CommandProxy.getInstance().dealCommand(commandDto);

		if (ConfigContext.isNeedInfPersist() && isTypeOfServer) {
			if(StringHelper.isEmpty(commandDto.getProcessInstanceId()) && StringHelper.isEmpty(commandResultDto.getProcessInstanceId())){
				commandDto.setProcessInstanceId("10");
			}
			// �ڽӿڱ��в���
			SaveCommandQueueDto saveCommandQueueDto = new SaveCommandQueueDto();
			CommandQueueDto dto = new CommandQueueDto();
			dto.setCommandCode(commandDto.getCommandCode());
			dto.setCommandMsg(GsonHelper.toJson(commandDto.toMap()));
			dto.setCommandResultMsg(ServerJsonUtil.getJsonFromCommandResultDto(commandResultDto));
			dto.setProcessInstanceId(LongHelper.valueOf(commandDto.getProcessInstanceId())==null ?LongHelper.valueOf(commandResultDto.getProcessInstanceId()):LongHelper.valueOf(commandDto.getProcessInstanceId()));
			dto.setState(0L);
			dto.setCreateDate(DateHelper.getTimeStamp());
			dto.setRoute(0L);
			if("completeWorkItem".equals(commandDto.getCommandCode())){
				CompleteWorkItemDto completeWorkItemDto = (CompleteWorkItemDto) commandDto;
				dto.setWorkItemId(LongHelper.valueOf(completeWorkItemDto.getWorkitemId()));
			}
			dto.setAreaId(commandDto.getAreaCode());
			dto.setServerAddr(localAddr);
			saveCommandQueueDto.setCommandQueueDto(dto);
			saveCommandQueueDto.setProcessInstanceId(commandDto.getProcessInstanceId()==null? commandResultDto.getProcessInstanceId():commandDto.getProcessInstanceId());
			CommandProxy.getInstance().dealCommand(saveCommandQueueDto);
		}
		return commandResultDto;
	}
}
