package com.zterc.uos.fastflow.dao.process.his.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.dao.process.his.ExceptionHisDAO;
import com.zterc.uos.fastflow.dao.specification.AreaDAO;
import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

@Repository("exceptionHisDAO")
public class ExceptionHisDAOImpl extends AbstractDAOImpl implements ExceptionHisDAO {
	@Autowired
	private AreaDAO areaDAO;

	protected static final String INSERT_EXCEPTION_HIS = "INSERT INTO UOS_EXCEPTION_HIS (ID,PROCESS_INSTANCE_ID, MSG, STATE, DEAL_TIMES, CREATE_DATE, DEAL_DATE, ERROR_INFO,WORKITEM_ID,TACHE_ID,EXCEPTION_TYPE,REASON_CLASS,COMMAND_CODE,AREA_ID)"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";

	protected static final String QUERY_EXCEPTION_HIS = "SELECT UEI.* FROM UOS_EXCEPTION_HIS UEI WHERE 1=1 ";

	protected static final String UPDATE_EXCEPTION_HIS = "UPDATE UOS_EXCEPTION_HIS SET STATE=?, DEAL_TIMES=DEAL_TIMES+1,DEAL_DATE=?,ERROR_INFO=? WHERE ID = ?";

	@Override
	public ExceptionDto queryExceptionHis(Long id) {
		String where = " AND ID = ?";
		return queryObject(ExceptionDto.class, QUERY_EXCEPTION_HIS + where,
				new Object[] { id });
	}

	@Override
	public ExceptionDto createExceptionHis(ExceptionDto exceptionDto) {
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
			saveOrUpdate(buildMap(INSERT_EXCEPTION_HIS, args));
		} catch (Exception e) {
			throw e;
		} finally{
			DsContextHolder.setHoldDs(ds);
		}
		return exceptionDto;
	}

	@Override
	public List<ExceptionDto> queryExceptionsHisByState(String state) {
		String where = " AND UEI.STATE = ?";
		return queryList(ExceptionDto.class, QUERY_EXCEPTION_HIS + where,
				new Object[] { state });
	}

	@Override
	public PageDto queryExceptionsHisByCond(Map<String, Object> paramMap) {
		PageDto pageDto = new PageDto();
		StringBuffer sqlStr = new StringBuffer(QUERY_EXCEPTION_HIS + " AND UEI.STATE=0 ");
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
			if(userAreaId != null){
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
		}
		QueryFilter filter = new QueryFilter();
		if(paramMap.get("sortColumn")!=null&& !"".equals(paramMap.get("sortColumn"))
				&&paramMap.get("sortOrder")!=null&& !"".equals(paramMap.get("sortOrder"))){
			String sortColumn = StringHelper.valueOf(paramMap.get("sortColumn"));
			String sortOrder = StringHelper.valueOf(paramMap.get("sortOrder"));
			if(sortColumn != null && sortOrder != null){
				sortColumn = sortColumn.toUpperCase();
				sortOrder = sortOrder.toUpperCase();
				filter.setOrder(sortOrder);
				filter.setOrderBy(sortColumn);
			}
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

}
