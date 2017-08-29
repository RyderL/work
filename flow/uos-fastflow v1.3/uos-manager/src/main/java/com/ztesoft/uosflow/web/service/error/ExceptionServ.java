package com.ztesoft.uosflow.web.service.error;

import java.util.Map;

public interface ExceptionServ {

	public String qryUosFlowErrorsByCond(Map<String,Object> map) throws Exception;
	public String dealException(Map<String,Object> paramMap);
	public String dealExceptions(Map<String,Object> paramMap);

}
