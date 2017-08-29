package com.ztesoft.uosflow.web.service.timelimit;

import java.util.Map;

public interface FlowLimitServ {

	public String qryFlowLimitByPackageDefine(Map<String,Object> map);
	public String addFlowLimit(Map<String,Object> map) throws Exception;
	public String modFlowLimit(Map<String,Object> map) throws Exception;
	public String delFlowLimit(Map<String,Object> map) throws Exception;
}
