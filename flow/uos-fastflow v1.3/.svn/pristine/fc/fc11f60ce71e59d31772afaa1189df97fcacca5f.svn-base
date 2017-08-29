package com.ztesoft.uosflow.inf.client.inf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.fastflow.dto.specification.MultiClientDto;
import com.zterc.uos.fastflow.exception.FastflowException;
import com.zterc.uos.fastflow.service.MultiClientService;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.dubbo.inf.client.FlowDubboServiceInf;
import com.ztesoft.uosflow.inf.client.dubbo.WorkFlowClient;
import com.ztesoft.uosflow.inf.client.ws.WebServiceClient;

public class FlowClient implements IClient {
	private static Logger logger = Logger.getLogger(FlowClient.class);
	@Autowired
	private MultiClientService multiClientService;
	@Autowired
	private WorkFlowClient workFlowClient;
	@Autowired
	private WebServiceClient webServiceClient;
	private Map<String,MultiClientDto> systemClientMap = new HashMap<String, MultiClientDto>();
	
	public void init(){
		List<MultiClientDto> clients = multiClientService.qryAllMultiClientDto();
		Iterator<MultiClientDto> it = clients.iterator();
		while(it.hasNext()){
			MultiClientDto clientDto = it.next();
			String system = clientDto.getSystem();
			systemClientMap.put(system, clientDto);
		}
	}

	@Override
	public CommandResultDto sendMessage(CommandDto commandDto) throws Exception {
		CommandResultDto commandResultDto = null;
		String system = commandDto.getTo();
		MultiClientDto client = systemClientMap.get(system);
		if(client != null){
			if("dubbo".equals(client.getInfType())){
				FlowDubboServiceInf service = (FlowDubboServiceInf) ApplicationContextProxy.getBean(client.getSystemBean());
				if(service == null){
					throw new FastflowException("========flowDubboService为空,请检查dubbo:reference和uos_multi_client_cfg的配置:system="+system+",systemClientMap=["+GsonHelper.toJson(systemClientMap)+"]");
				}
				commandResultDto = workFlowClient.sendMessage(commandDto, service);
			}else if("webService".equals(client.getInfType())){
				commandResultDto = webServiceClient.sendMessage(commandDto,client.getWsdlUrl());
			}
		}else{
			throw new FastflowException("客户端多租户配置为空，请检查uos_multi_client_cfg的配置：[system="+system+"]");
		}
		return commandResultDto;
	}

}
