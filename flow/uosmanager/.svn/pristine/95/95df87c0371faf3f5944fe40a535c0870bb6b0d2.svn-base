package com.ztesoft.uosflow.web.inf.client.dubbo;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ztesoft.uosflow.dubbo.inf.client.FlowDubboServiceInf;
import com.ztesoft.uosflow.web.inf.client.IClient;
import com.ztesoft.uosflow.web.inf.model.RequestDto;
import com.ztesoft.uosflow.web.inf.model.ResponseDto;

public class DubboClient implements IClient {
	private static Logger logger = LoggerFactory.getLogger(DubboClient.class);
	
	private FlowDubboServiceInf flowDubboService;
	
	public FlowDubboServiceInf getFlowDubboService() {
		return flowDubboService;
	}
	public void setFlowDubboService(FlowDubboServiceInf flowDubboService) {
		this.flowDubboService = flowDubboService;
	}
	@Override
	public ResponseDto sendMessage(RequestDto dto) {
		logger.info("-----客户端message："+dto.getCommandCode());
		return this.dealCommand(dto);
	}
	public ResponseDto dealCommand(RequestDto dto) {
		ResponseDto rDto = null;
		try {
			Method method =this.getClass().getMethod(dto.getCommandCode(), RequestDto.class);
			rDto = (ResponseDto) (method.invoke(this, dto));
		} catch (Exception e) {
			logger.error("-------反射调用方法异常",e);
		}
		return rDto;
	}
	
	public ResponseDto qryAreaDatas(RequestDto dto){
		ResponseDto retDto = null;
		logger.debug("-----qryAreaDatas");
//		try{
//			retDto = iomService.qryAreaDatas(dto);
//		}catch(Exception ex){
//			logger.error("-------dubbo异常",ex);
//		}
		return retDto;
	}
	
	public ResponseDto qrySysDatas(RequestDto dto){
		ResponseDto retDto = null;
//		logger.debug("-----qrySysDatas");
//		try{
//			retDto = iomService.qrySysDatas(dto);
//		}catch(Exception ex){
//			logger.error("-------dubbo异常",ex);
//		}
		return retDto;
	}
	
	public ResponseDto qryOrgDatas(RequestDto dto){
		ResponseDto retDto = null;
		logger.debug("-----qryOrgDatas");
//		try{
//			retDto = iomService.qryOrgDatas(dto);
//		}catch(Exception ex){
//			logger.error("-------dubbo异常",ex);
//		}
		return retDto;
	}

	public ResponseDto qryJobDatas(RequestDto dto){
		ResponseDto retDto = null;
		logger.debug("-----qryJobDatas");
//		try{
//			retDto = iomService.qryJobDatas(dto);
//		}catch(Exception ex){
//			logger.error("-------dubbo异常",ex);
//		}
		return retDto;
	}

	public ResponseDto qryStaDatas(RequestDto dto){
		ResponseDto retDto = null;
		logger.debug("-----qryStaDatas");
//		try{
//			retDto = iomService.qryStaDatas(dto);
//		}catch(Exception ex){
//			logger.error("-------dubbo异常",ex);
//		}
		return retDto;
	}

	public ResponseDto qryBizDatas(RequestDto dto){
		ResponseDto retDto = null;
		logger.debug("-----qryBizDatas");
//		try{
//			retDto = iomService.qryBizDatas(dto);
//		}catch(Exception ex){
//			logger.error("-------dubbo异常",ex);
//		}
		return retDto;
	}
}
