package com.ztesoft.uosflow.web.service.timelimit;

import java.util.Map;

public interface HolidayServ {
	
	public String qryHolidayModelByAreaId(Map<String, Object> map) throws Exception;
	public String delHolidayModel(Map<String,Object> map) throws Exception;
	public String addHolidayModel(Map<String,Object> map) throws Exception;
	public String modHolidayModel(Map<String,Object> map) throws Exception;
	
	public String qryHolidaySystemsByArea(Map<String, Object> map) throws Exception;
	public String delHolidaySystems(Map<String,Object> map) throws Exception;
	public String addHolidaySystems(Map<String,Object> map) throws Exception;
	public String modHolidaySystems(Map<String,Object> map) throws Exception;

}
