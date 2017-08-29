package com.ztesoft.uosflow.web.service.timelimit;

import java.util.Map;

public interface WorkTimeServ {
	
	public String qryWorkTimeByAreaId(Map<String, Object> map) throws Exception;
	public String delWorkTime(Map<String,Object> map) throws Exception;
	public String addWorkTime(Map<String,Object> map) throws Exception;
	public String modWorkTime(Map<String,Object> map) throws Exception;

}
