package com.zterc.uos.fastflow.dao.specification;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.HolidayDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public interface HolidayDAO {
	
	public void addHoliday(HolidayDto holidayDto);
	public void modHoliday(HolidayDto holidayDto);
	public void delHoliday(HolidayDto holidayDto);
	public PageDto qryHoliday(Map<String,Object> params);
	public Collection<HolidayDto> qryHolidaysByArea(String areaId, Date validdate);
	public HolidayDto[] qryAllHolidays();
}
