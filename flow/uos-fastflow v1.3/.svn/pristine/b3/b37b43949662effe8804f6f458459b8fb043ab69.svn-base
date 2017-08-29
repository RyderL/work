package com.zterc.uos.fastflow.service;

import java.util.Map;

import com.zterc.uos.fastflow.dao.specification.FlowLimitDAO;
import com.zterc.uos.fastflow.dto.specification.FlowLimitDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public class FlowLimitService {
	private FlowLimitDAO flowLimitDAO;

	public void setFlowLimitDAO(FlowLimitDAO flowLimitDAO) {
		this.flowLimitDAO = flowLimitDAO;
	}

	public void addFlowLimit(FlowLimitDto flowLimitDto){
		flowLimitDAO.addFlowLimit(flowLimitDto);
	}
	
	public void modFlowLimit(FlowLimitDto flowLimitDto){
		flowLimitDAO.modFlowLimit(flowLimitDto);
	}
	
	public void delFlowLimit(FlowLimitDto flowLimitDto){
		flowLimitDAO.delFlowLimit(flowLimitDto);
	}
	
	public PageDto qryFlowLimitByCond(Map<String,Object> params){
		return flowLimitDAO.qryFlowLimitByCond(params);
	}

	public FlowLimitDto qryFlowLimit(String packageDefineId,String areaId){
		FlowLimitDto flowLimitDto = flowLimitDAO.qryFlowLimit(packageDefineId, areaId);
		return flowLimitDto;
	}

}
