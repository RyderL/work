package com.ztesoft.uosflow.inf.client.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.axis.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.util.CommandResultDtoUtil;
import com.ztesoft.uosflow.inf.util.ServerJsonUtil;

@Component("webServiceClient")
public class WebServiceClient{
	private static final Logger logger = LoggerFactory.getLogger(WebServiceClient.class);

	private String wsdlUrl;
	private Map<String,String> wsUrlMap;

	public String getWsdlUrl() {
		return wsdlUrl;
	}

	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}
	
	public Map<String, String> getWsUrlMap() {
		return wsUrlMap;
	}

	public void setWsUrlMap(Map<String, String> wsUrlMap) {
		this.wsUrlMap = wsUrlMap;
	}

//	private Map<String,WorkFlowService> wsClientMap ;
//	
//	private WorkFlowService workFlowService;
//	

//	public void init() throws AxisFault, MalformedURLException {
//		if(wsdlUrl!=null){
//			workFlowService = new WorkFlowServicePortSoapBindingStub(new URL(wsdlUrl), null);
//		}
//		if(wsUrlMap!=null){
//			wsClientMap = new HashMap<String, WorkFlowService>();
//			for(String key:wsUrlMap.keySet()){
//				wsClientMap.put(key, new WorkFlowServicePortSoapBindingStub(new URL(wsUrlMap.get(key)), null));
//			}
//		}
//	}
//	private WorkFlowService getService(String system){
//		WorkFlowService service = null;
//		if(wsClientMap!=null){
//			service = wsClientMap.get(system);
//		}else{
//			service = workFlowService;
//		}
//		if(service==null){
//			throw new FastflowException("ws客户端服务为空。[system="+system+",wsdlUrl="+wsdlUrl+",wsUrlMap="+wsUrlMap+"]");
//		}
//		return service;
//	}
	
	private WorkFlowService getService(String wsdlUrl) throws AxisFault, MalformedURLException{
		WorkFlowService service = new WorkFlowServicePortSoapBindingStub(new URL(wsdlUrl), null);
		return service;
	}

	public CommandResultDto sendMessage(CommandDto commandDto,String wsdlUrl) {
		String retJson = "";
		try {
			WorkFlowService service = getService(wsdlUrl);
			String sendJson = ServerJsonUtil.getJsonFromCommandDto(commandDto);
			logger.info("调用业务接口：" + sendJson);
			retJson = service.methodInvoke(sendJson);
			logger.info("业务接口返回：" + retJson);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "接口调用异常", commandDto.getProcessInstanceId());
		}
		return ServerJsonUtil.getCommandResultDtoFromJson(retJson);
	}
}
