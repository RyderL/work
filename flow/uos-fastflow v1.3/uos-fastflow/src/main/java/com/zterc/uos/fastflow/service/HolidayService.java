package com.zterc.uos.fastflow.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.dao.specification.HolidayDAO;
import com.zterc.uos.fastflow.dao.specification.HolidaySystemDAO;
import com.zterc.uos.fastflow.dto.specification.HolidayDto;
import com.zterc.uos.fastflow.dto.specification.HolidaySystemDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public class HolidayService {

	private HolidayDAO holidayDAO;
	private HolidaySystemDAO holidaySystemDAO;
	
	public void setHolidayDAO(HolidayDAO holidayDAO) {
		this.holidayDAO = holidayDAO;
	}
	public void setHolidaySystemDAO(HolidaySystemDAO holidaySystemDAO) {
		this.holidaySystemDAO = holidaySystemDAO;
	}
	
	public void addHoliday(HolidayDto holidayDto){
		holidayDAO.addHoliday(holidayDto);
	}

	public void modHoliday(HolidayDto holidayDto){
		holidayDAO.modHoliday(holidayDto);
	}
	
	public void delHoliday(HolidayDto holidayDto){
		holidayDAO.delHoliday(holidayDto);
	}
	
	public PageDto qryHolidayByCond(Map<String,Object> params){
		return holidayDAO.qryHoliday(params);
	}

	public void addHolidaySystem(HolidaySystemDto holidaySystemDto){
		holidaySystemDAO.addHolidaySystem(holidaySystemDto);
	}

	public void modHolidaySystem(HolidaySystemDto holidaySystemDto){
		holidaySystemDAO.modHolidaySystem(holidaySystemDto);
	}

	public void delHolidaySystem(HolidaySystemDto holidaySystemDto){
		holidaySystemDAO.delHolidaySystem(holidaySystemDto);
	}
	
	public PageDto qryHolidaySystemByCond(Map<String,Object> params){
		return holidaySystemDAO.qryHolidaySystemByCond(params);
	}

	public Collection<HolidaySystemDto> qryHolidaySystemsByArea(String areaId) {
		return holidaySystemDAO.qryHolidaySystemsByArea(areaId);
	}
	
	public Collection<HolidayDto> findHolidays(String areaId,
			Date validdate) {
		Collection<HolidayDto> dtos =  holidayDAO.qryHolidaysByArea(areaId, validdate);
		return dtos;
	}
	public HolidayDto[] qryAllHolidays() {
		return holidayDAO.qryAllHolidays();
	}
	public Collection<HolidayDto> findHolidayByArea(String areaId,
			HolidayDto[] holidayDtoes) {
		Collection<HolidayDto> holidays = new ArrayList<HolidayDto>();
		for (int i = 0; i < holidayDtoes.length; i++) {
			if (holidayDtoes[i].getAreaId() != null
					&& areaId.equals(StringHelper.valueOf(holidayDtoes[i].getAreaId()))) {
				holidays.add(holidayDtoes[i]);
			}
		}
		return holidays;
	}
	public Collection<HolidaySystemDto> findHolidaySystemByArea(String areaId,
			HolidaySystemDto[] holidaySystemDtoes) {
		Collection<HolidaySystemDto> holidaySystems = new ArrayList<HolidaySystemDto>();
		for (int i = 0; i < holidaySystemDtoes.length; i++) {
			if (holidaySystemDtoes[i].getAreaId() != null
					&& areaId.equals(StringHelper.valueOf(holidaySystemDtoes[i].getAreaId()))) {
				holidaySystems.add(holidaySystemDtoes[i]);
			}
		}
		return holidaySystems;
	}
	
}
