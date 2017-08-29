package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dao.specification.HolidayDAO;
import com.zterc.uos.fastflow.dto.specification.HolidayDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public class HolidayDAOImpl extends AbstractDAOImpl implements HolidayDAO {

	private String INSERT_HOLIDAY = "INSERT INTO UOS_HOLIDAY(ID,HOLIDAY_NAME,HOLIDAY_RULE,TIME_UNIT,EFF_DATE," +
            "EXP_DATE,STATE,STATE_DATE,CREATE_DATE,COMMENTS,AREA_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	@Override
	public void addHoliday(HolidayDto holidayDto) {
		holidayDto.setId(SequenceHelper.getId("UOS_HOLIDAY"));
		Object[] args = new Object[] {holidayDto.getId(),holidayDto.getHolidayName(),holidayDto.getHolidayRule(),
				holidayDto.getTimeUnit(),holidayDto.getEffDate(),holidayDto.getExpDate(),holidayDto.getState(),
				holidayDto.getStateDate(),holidayDto.getCreateDate(),holidayDto.getComments(),holidayDto.getAreaId()};
		saveOrUpdate(buildMap(INSERT_HOLIDAY, args));
	}

	private String UPDATE_HOLIDAY = "UPDATE UOS_HOLIDAY SET HOLIDAY_NAME=?,HOLIDAY_RULE=?,TIME_UNIT=?,EFF_DATE=?,EXP_DATE=?," +
            "COMMENTS=? WHERE ID=?";
	@Override
	public void modHoliday(HolidayDto holidayDto) {
		Object[] args = new Object[] {holidayDto.getHolidayName(),holidayDto.getHolidayRule(),
				holidayDto.getTimeUnit(),holidayDto.getEffDate(),holidayDto.getExpDate(),
				holidayDto.getComments(),holidayDto.getId()};
		saveOrUpdate(buildMap(UPDATE_HOLIDAY, args));
	}

	private String DEL_HOLIDAY = "UPDATE UOS_HOLIDAY SET STATE='10X',STATE_DATE=? WHERE ID = ?";
	@Override
	public void delHoliday(HolidayDto holidayDto) {
		Object[] args = new Object[] {holidayDto.getStateDate(),holidayDto.getId()};
		saveOrUpdate(buildMap(DEL_HOLIDAY, args));
	}
	@Override
	public PageDto qryHoliday(Map<String,Object> params) {
		PageDto pageDto = new PageDto();
		StringBuffer qrySql = new StringBuffer("SELECT ID,HOLIDAY_NAME,HOLIDAY_RULE,TIME_UNIT,EFF_DATE," +
            "EXP_DATE,STATE,STATE_DATE,CREATE_DATE,COMMENTS FROM UOS_HOLIDAY WHERE ROUTE_ID=1 ");
		List<Object> keys = new ArrayList<Object>();
		qrySql.append(" AND STATE = ?");
		keys.add("10A");
		List<HolidayDto> list = queryList(HolidayDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
		pageDto.setRows(list);
		pageDto.setTotal(list.size());
		return pageDto;
	}
	@Override
	public Collection<HolidayDto> qryHolidaysByArea(String areaId, Date validdate) {
		Collection<HolidayDto> col = new ArrayList<HolidayDto>();
    	String sql = "SELECT ID,HOLIDAY_NAME,HOLIDAY_RULE,TIME_UNIT,EFF_DATE," +
            "EXP_DATE,STATE,STATE_DATE,CREATE_DATE,COMMENTS,AREA_ID" +
            " FROM UOS_HOLIDAY" +
            " WHERE ROUTE_ID=1 AND STATE<>'10X' AND AREA_ID=? AND EFF_DATE<="+JDBCHelper.getDialect().getFormatDate()+
            " AND EXP_DATE>="+JDBCHelper.getDialect().getFormatDate();
    	
    	List<HolidayDto> list = queryList(HolidayDto.class, sql, new Object[]{areaId,DateHelper.parseTime(validdate),DateHelper.parseTime(validdate)});
		for(HolidayDto holidayDto: list){
			col.add(holidayDto);
		}
    	return col;
	}
	@Override
	public HolidayDto[] qryAllHolidays() {
		String sql = "SELECT ID,HOLIDAY_NAME,HOLIDAY_RULE,TIME_UNIT,EFF_DATE," +
	            "EXP_DATE,STATE,STATE_DATE,CREATE_DATE,COMMENTS,AREA_ID" +
	            " FROM UOS_HOLIDAY" +
	            " WHERE ROUTE_ID=1 AND STATE<>?";
    	List<HolidayDto> list = queryList(HolidayDto.class, sql, new Object[]{"10X"});
    	if(list == null){
    		return new HolidayDto[0];
    	}
		return list.toArray(new HolidayDto[list.size()]);
	}

}
