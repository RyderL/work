package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.dao.specification.HolidaySystemDAO;
import com.zterc.uos.fastflow.dto.specification.HolidaySystemDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public class HolidaySystemDAOImpl extends AbstractDAOImpl implements
		HolidaySystemDAO {

	private String INSERT_HOLIDAYSYSTEM = "INSERT INTO UOS_HOLIDAY_SYSTEM(ID,HOLIDAY_SYSTEM_NAME,BEGIN_DATE,END_DATE," +
            "STATE,STATE_DATE,CREATE_DATE,OPER_TYPE,COMMENTS,AREA_ID) VALUES(?,?,?,?,?,?,?,?,?,?)";
	@Override
	public void addHolidaySystem(HolidaySystemDto holidaySystemDto) {
		holidaySystemDto.setId(SequenceHelper.getId("UOS_HOLIDAY_SYSTEM"));
		Object[] args = new Object[] {holidaySystemDto.getId(),holidaySystemDto.getHolidaySystemName(),
				holidaySystemDto.getBeginDate(),holidaySystemDto.getEndDate(),holidaySystemDto.getState(),
				holidaySystemDto.getStateDate(),holidaySystemDto.getCreateDate(),holidaySystemDto.getOperType(),
				holidaySystemDto.getComments(),holidaySystemDto.getAreaId()};
		saveOrUpdate(buildMap(INSERT_HOLIDAYSYSTEM, args));
	}

	private String UPDATE_HOLIDAYSYSTEM =  "UPDATE UOS_HOLIDAY_SYSTEM SET HOLIDAY_SYSTEM_NAME=?," +
            "BEGIN_DATE=?,END_DATE=?,OPER_TYPE=?,COMMENTS=? WHERE ID=?";
	@Override
	public void modHolidaySystem(HolidaySystemDto holidaySystemDto) {
		Object[] args = new Object[] {holidaySystemDto.getHolidaySystemName(),
				holidaySystemDto.getBeginDate(),holidaySystemDto.getEndDate(),holidaySystemDto.getOperType(),
				holidaySystemDto.getComments(),holidaySystemDto.getId()};
		saveOrUpdate(buildMap(UPDATE_HOLIDAYSYSTEM, args));
	}

	private String DEL_HOLIDAYSYSTEM = "UPDATE UOS_HOLIDAY_SYSTEM SET STATE='10X',STATE_DATE=? WHERE ID = ?";
	@Override
	public void delHolidaySystem(HolidaySystemDto holidaySystemDto) {
		Object[] args = new Object[] {holidaySystemDto.getStateDate(),holidaySystemDto.getId()};
		saveOrUpdate(buildMap(DEL_HOLIDAYSYSTEM, args));
	}
	
	private String QRY_HOLIDAY_SYSTEM = "SELECT ID,HOLIDAY_SYSTEM_NAME,BEGIN_DATE,END_DATE," +
            "STATE,STATE_DATE,CREATE_DATE,OPER_TYPE,COMMENTS" +
            " FROM UOS_HOLIDAY_SYSTEM " +
            "WHERE ROUTE_ID=1 ";
	@Override
	public PageDto qryHolidaySystemByCond(Map<String, Object> params) {
		PageDto pageDto = new PageDto();
		StringBuffer qrySql = new StringBuffer(QRY_HOLIDAY_SYSTEM);
		List<Object> keys = new ArrayList<Object>();
		qrySql.append(" AND STATE = ?");
		keys.add("10A");
		if(params != null){
			QueryFilter queryFilter = new QueryFilter();
			queryFilter.setOrderBy("CREATE_DATE");
			queryFilter.setOrder(QueryFilter.ASC);
			if(params.get("page")!=null&&params.get("pageSize")!=null){
				Page<HolidaySystemDto> page = new Page<HolidaySystemDto>();
				page.setPageNo(IntegerHelper.valueOf(String.valueOf(params.get("page"))));
				page.setPageSize(IntegerHelper.valueOf(String.valueOf(params.get("pageSize"))));
				List<HolidaySystemDto> list = queryList(page,queryFilter,HolidaySystemDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
			}else{
				List<HolidaySystemDto> list = queryList(HolidaySystemDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(list.size());
			}
		}
		return pageDto;
	}

	@Override
	public List<HolidaySystemDto> qryHolidaySystemsByArea(String areaId) {
		String whereStr = " AND STATE<>'10X' AND AREA_ID = ?";
		List<Object> keys = new ArrayList<Object>();
		keys.add(areaId);
		List<HolidaySystemDto> list = queryList(HolidaySystemDto.class,QRY_HOLIDAY_SYSTEM + whereStr,keys.toArray(new Object[]{}));
		return list;
	}

}
