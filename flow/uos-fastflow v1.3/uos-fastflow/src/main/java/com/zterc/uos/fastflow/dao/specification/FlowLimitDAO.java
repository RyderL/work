package com.zterc.uos.fastflow.dao.specification;

import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.FlowLimitDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public interface FlowLimitDAO {
	
	public void addFlowLimit(FlowLimitDto flowLimitDto);
	
	public void modFlowLimit(FlowLimitDto flowLimitDto);
	
	public void delFlowLimit(FlowLimitDto flowLimitDto);

	public PageDto qryFlowLimitByCond(Map<String,Object> params);
	
	public FlowLimitDto qryFlowLimit(String packageDefineId,String areaId);
}
