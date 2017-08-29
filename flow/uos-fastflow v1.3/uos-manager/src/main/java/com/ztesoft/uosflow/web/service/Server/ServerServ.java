package com.ztesoft.uosflow.web.service.Server;

import java.util.Map;

public interface ServerServ {
	public String getServerThreadInfo(Map<String,Object>  map)throws Exception;
	
	public String getCounterInfo(Map<String,Object>  map)throws Exception;
	
	public String clearCounterInfo(Map<String,Object>  map)throws Exception;
	
	public String clearStatics(Map<String,Object>  map)throws Exception;
}
