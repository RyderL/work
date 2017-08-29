package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dao.specification.WorkTimeDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.WorkTimeDto;

public class WorkTimeDAOImpl extends AbstractDAOImpl implements WorkTimeDAO {
	private static Logger logger = Logger.getLogger(WorkTimeDAOImpl.class);
	
	private String INSERT_WORK_TIME = "INSERT INTO UOS_WORK_TIME(ID,WORK_TIME_NAME,WORK_TIME_RULE,EFF_DATE,EXP_DATE,STATE,STATE_DATE,CREATE_DATE,COMMENTS,AREA_ID) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?)";
	@Override
	public void addWorkTime(WorkTimeDto workTimeDto) {
		workTimeDto.setId(SequenceHelper.getId("UOS_WORK_TIME"));
		Object[] args = new Object[] {workTimeDto.getId(),workTimeDto.getWorkTimeName(),workTimeDto.getWorkTimeRule(),
				workTimeDto.getEffDate(),workTimeDto.getExpDate(),workTimeDto.getState(),workTimeDto.getStateDate(),
				workTimeDto.getCreateDate(),workTimeDto.getComments(),workTimeDto.getAreaId()};
		saveOrUpdate(buildMap(INSERT_WORK_TIME, args));
	}
	
	private String UPDATE_WORK_TIME="UPDATE UOS_WORK_TIME SET WORK_TIME_NAME=?,WORK_TIME_RULE=?" +
            ",EFF_DATE=?,EXP_DATE=?,COMMENTS=? WHERE ID=?";
	@Override
	public void modWorkTime(WorkTimeDto workTimeDto) {
		Object[] args = new Object[] {workTimeDto.getWorkTimeName(),workTimeDto.getWorkTimeRule(),workTimeDto.getEffDate(),
				workTimeDto.getExpDate(),workTimeDto.getComments(),workTimeDto.getId()};
		saveOrUpdate(buildMap(UPDATE_WORK_TIME, args));
	}
	
	private String DEL_WORK_TIME = "UPDATE UOS_WORK_TIME SET STATE='10X',STATE_DATE=? WHERE ID = ?";
	@Override
	public void delWorkTime(WorkTimeDto workTimeDto) {
		Object[] args = new Object[] {workTimeDto.getStateDate(),workTimeDto.getId()};
		saveOrUpdate(buildMap(DEL_WORK_TIME, args));
	}
	
	private String QRY_WORK_TIME = "SELECT ID,WORK_TIME_NAME,WORK_TIME_RULE,EFF_DATE,EXP_DATE," +
            "STATE,STATE_DATE,CREATE_DATE,COMMENTS,AREA_ID FROM UOS_WORK_TIME WHERE ROUTE_ID=1";
	@Override
	public WorkTimeDto qryWorkTimeById(Long id) {
		String sqlWhere = " AND ID=? ";
		return queryObject(WorkTimeDto.class, QRY_WORK_TIME + sqlWhere, id);
	}
	
	@Override
	public PageDto qryWorkTimeByCond(Map<String, Object> params) {
		PageDto pageDto = new PageDto();
		StringBuffer qrySql = new StringBuffer(QRY_WORK_TIME);
		qrySql.append(" AND STATE='10A' ");
		if(params != null){
			if(params.get("areaIds") != null && !"".equals(params.get("areaIds"))){
				qrySql.append(" AND AREA_ID IN (").append(StringHelper.valueOf(params.get("areaIds"))).append(")");
			}
			logger.info("-----≤È—Øsql:"+qrySql.toString());
			List<WorkTimeDto> list = queryList(WorkTimeDto.class,qrySql.toString());
			pageDto.setRows(list);
			pageDto.setTotal(list.size());
		}
		return pageDto;
	}

	@Override
	public WorkTimeDto[] findActiveWorkTimes(Date inputDate) {
		String sql = QRY_WORK_TIME + " AND EFF_DATE<="+JDBCHelper.getDialect().getFormatDate()+
            " AND EXP_DATE>="+JDBCHelper.getDialect().getFormatDate()+" AND STATE<>'10X' ORDER BY ID";
		List<WorkTimeDto> list = queryList(WorkTimeDto.class, sql, new Object[]{DateHelper.parseTime(inputDate),
			DateHelper.parseTime(inputDate)});
		if(list  == null){
			return new WorkTimeDto[0];
		}
		return list.toArray(new WorkTimeDto[list.size()]);
	}

}
