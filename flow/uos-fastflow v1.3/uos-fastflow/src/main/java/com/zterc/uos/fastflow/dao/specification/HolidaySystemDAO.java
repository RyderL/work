package com.zterc.uos.fastflow.dao.specification;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.HolidaySystemDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public interface HolidaySystemDAO {
	
	public void addHolidaySystem(HolidaySystemDto holidaySystemDto);

	public void modHolidaySystem(HolidaySystemDto holidaySystemDto);
	
	public void delHolidaySystem(HolidaySystemDto holidaySystemDto);
	
	public PageDto qryHolidaySystemByCond(Map<String,Object> params);
	
	public List<HolidaySystemDto> qryHolidaySystemsByArea(String areaId);
}
