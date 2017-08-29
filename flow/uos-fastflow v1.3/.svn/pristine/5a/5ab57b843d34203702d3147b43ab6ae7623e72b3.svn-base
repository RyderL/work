package com.zterc.uos.fastflow.dao.process.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.dao.process.ExceptionDAO;
import com.zterc.uos.fastflow.dao.specification.AreaDAO;
import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public class ExceptionDAOImpl extends AbstractDAOImpl implements ExceptionDAO {
	@Autowired
	private AreaDAO areaDAO;

	protected static final String INSERT_EXCEPTION = "INSERT INTO UOS_EXCEPTION (ID,PROCESS_INSTANCE_ID, MSG, STATE, DEAL_TIMES, CREATE_DATE, DEAL_DATE, ERROR_INFO,WORKITEM_ID,TACHE_ID,EXCEPTION_TYPE,REASON_CLASS,COMMAND_CODE,AREA_ID)"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";

	protected static final String QUERY_EXCEPTION = "SELECT UEI.ID,UEI.MSG,UEI.STATE,UEI.DEAL_DATE,UEI.CREATE_DATE,UEI.ERROR_INFO,UEI.PROCESS_INSTANCE_ID,UEI.WORKITEM_ID,UEI.TACHE_ID,UEI.EXCEPTION_TYPE,UEI.REASON_CLASS,UEI.COMMAND_CODE,UEI.AREA_ID FROM UOS_EXCEPTION UEI WHERE 1=1 ";

	protected static final String UPDATE_EXCEPTION = "UPDATE UOS_EXCEPTION SET STATE=?, DEAL_TIMES=DEAL_TIMES+1,DEAL_DATE=?,ERROR_INFO=? WHERE ID = ?";

	@Override
	public ExceptionDto queryException(Long id) {
		String where = " AND ID = ?";
		return queryObject(ExceptionDto.class, QUERY_EXCEPTION + where,
				new Object[] { id });
	}

	@Override
	public ExceptionDto createException(ExceptionDto exceptionDto) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(exceptionDto.getId());
			Object[] args = new Object[] { exceptionDto.getId(),
					exceptionDto.getProcessInstanceId(), exceptionDto.getMsg(),
					exceptionDto.getState(), exceptionDto.getDealTimes(),
					exceptionDto.getCreateDate(), exceptionDto.getDealDate(),
					exceptionDto.getErrorInfo(),exceptionDto.getWorkItemId(),
					exceptionDto.getTacheId(),exceptionDto.getExceptionType(),
					exceptionDto.getReasonClass(),exceptionDto.getCommandCode(),exceptionDto.getAreaId()};
				saveOrUpdate(buildMap(INSERT_EXCEPTION, args));
		} catch (Exception e) {
			throw e;
		} finally{
			DsContextHolder.setHoldDs(ds);
		}
		return exceptionDto;
	}

	@Override
	public void updateException(ExceptionDto exceptionDto) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(exceptionDto.getId());
			Object[] args = new Object[] { exceptionDto.getState(),
					exceptionDto.getDealDate(), exceptionDto.getErrorInfo(),
					exceptionDto.getId() };
			saveOrUpdate(buildMap(UPDATE_EXCEPTION, args));
		} catch (Exception e) {
			throw e;
		} finally{
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public List<ExceptionDto> queryExceptionsByState(String state) {
		String where = " AND UEI.STATE = ?";
		return queryList(ExceptionDto.class, QUERY_EXCEPTION + where,
				new Object[] { state });
	}

	@Override
	public PageDto queryExceptionsByCond(Map<String, Object> paramMap) {
		PageDto pageDto = new PageDto();
		StringBuffer sqlStr = new StringBuffer(QUERY_EXCEPTION+" AND UEI.STATE=0 ");
		List<Object> params = new ArrayList<Object>();
		if (paramMap.get("processInstanceId") != null) {
			sqlStr.append(" AND UEI.PROCESS_INSTANCE_ID = ? ");
			params.add(LongHelper.valueOf(paramMap.get("processInstanceId")));
		}
		if (paramMap.get("startDate") != null) {
			sqlStr.append(" AND UEI.CREATE_DATE > ").append(
					JDBCHelper.getDialect().getFormatDate());
			params.add(StringHelper.valueOf(paramMap.get("startDate")));
		}
		if (paramMap.get("endDate") != null) {
			sqlStr.append(" AND UEI.CREATE_DATE < ").append(
					JDBCHelper.getDialect().getFormatDate());
			params.add(StringHelper.valueOf(paramMap.get("endDate")));
		}
		if (null != paramMap.get("userAreaId")
				&& !"".equals(paramMap.get("userAreaId"))) {
			String userAreaId = StringHelper.valueOf(paramMap.get("userAreaId")); 
			sqlStr.append(" AND  UEI.AREA_ID in (").append(userAreaId);
			AreaDto areaDto = areaDAO.findAreaByAreaId(LongHelper.valueOf(userAreaId));
			AreaDto[] areas = areaDAO.findAreasByPathCode(areaDto.getPathCode());
			for(AreaDto area:areas){
				if(!userAreaId.equals(StringHelper.valueOf(area.getAreaId()))){
					sqlStr.append(",").append(area.getAreaId());
				}
			}
			sqlStr.append(") ");
		}
		QueryFilter filter = new QueryFilter();
		if(paramMap.get("sortColumn")!=null&& !"".equals(paramMap.get("sortColumn"))
				&&paramMap.get("sortOrder")!=null&& !"".equals(paramMap.get("sortOrder"))){
			String sortColumn = StringHelper.valueOf(paramMap.get("sortColumn")).toUpperCase();
			String sortOrder = StringHelper.valueOf(paramMap.get("sortOrder")).toUpperCase();
			filter.setOrder(sortOrder);
			filter.setOrderBy(sortColumn);
		}else{
			filter.setOrderBy("UEI.CREATE_DATE");
			filter.setOrder(QueryFilter.DESC);
		}
		if(paramMap.get("pageIndex")!=null&&paramMap.get("pageSize")!=null){
			Page<ExceptionDto> page = new Page<ExceptionDto>();
			page.setPageNo(IntegerHelper.valueOf(String.valueOf(paramMap.get("pageIndex"))));
			page.setPageSize(IntegerHelper.valueOf(String.valueOf(paramMap.get("pageSize"))));
			List<ExceptionDto> list = queryList(page,filter,ExceptionDto.class,sqlStr.toString(),params.toArray(new Object[]{}));
			pageDto.setRows(list);
			pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
		}else{
			List<ExceptionDto> list = queryList(ExceptionDto.class,sqlStr.toString(),params.toArray(new Object[]{}));
			pageDto.setRows(list);
			pageDto.setTotal(list.size());
		}
		return pageDto;
	}

	@Override
	public List<ExceptionDto> queryExceptionsByPid(String processInstanceId) {
		String sql = "SELECT ID,MSG,STATE,COMMAND_CODE,DEAL_TIMES,CREATE_DATE,DEAL_DATE," +
				"ERROR_INFO,PROCESS_INSTANCE_ID,WORKITEM_ID,TACHE_ID,EXCEPTION_TYPE," +
				"REASON_CLASS,AREA_ID FROM UOS_EXCEPTION WHERE PROCESS_INSTANCE_ID=?";
		
		return queryList(ExceptionDto.class, sql, new Object[]{processInstanceId});
	}

	@Override
	public void deleteByPid(String processInstanceId) {
		String sql = "DELETE FROM UOS_EXCEPTION WHERE PROCESS_INSTANCE_ID=?";
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(Long.valueOf(processInstanceId));
			Object[] args = new Object[] {processInstanceId};
			saveOrUpdate(buildMap(sql, args));
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

}
