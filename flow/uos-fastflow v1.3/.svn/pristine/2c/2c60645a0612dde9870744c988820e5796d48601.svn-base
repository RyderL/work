package com.zterc.uos.fastflow.dao.process.his.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.zterc.uos.fastflow.dao.process.his.CommandQueueHisDAO;
import com.zterc.uos.fastflow.dao.specification.AreaDAO;
import com.zterc.uos.fastflow.dto.process.CommandQueueDto;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

@Repository("commandQueueHisDAO")
public class CommandQueueHisDAOImpl extends AbstractDAOImpl implements CommandQueueHisDAO {
	@Autowired
	private AreaDAO areaDAO;
	private static final Logger logger = LoggerFactory.getLogger(CommandQueueHisDAOImpl.class);
	
	protected static final String QUERY_COMMAND_QUEUE_HIS = "SELECT UCQ.* FROM UOS_COMMAND_QUEUE_HIS UCQ  LEFT JOIN UOS_AREA UA3 ON UA3.AREA_ID = UA3.PARENT_ID LEFT JOIN UOS_AREA UA1 ON UA1.AREA_ID = UCQ.AREA_ID ";

	private static final String INSERT_COMMAND_QUEUE_HIS="INSERT INTO UOS_COMMAND_QUEUE_HIS(ID,COMMAND_CODE,COMMAND_MSG,PROCESS_INSTANCE_ID"
							+ ",STATE,CREATE_DATE,ROUTE,COMMAND_RESULT_MSG,WORKITEM_ID,AREA_ID)VALUES(?,?,?,?,?,?,?,?,?,?)";
	@Override
	public CommandQueueDto addCommandQueueHis(CommandQueueDto dto) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(dto.getId());
			String resultMsg = dto.getCommandResultMsg();
			if(resultMsg.length()>1300){
				resultMsg = resultMsg.substring(0, 1300);
			}
			Object[] args = new Object[] {dto.getId(),dto.getCommandCode(),dto.getCommandMsg(),dto.getProcessInstanceId(),
					dto.getState(),dto.getCreateDate(),dto.getRoute(),resultMsg,dto.getWorkItemId(),dto.getAreaId()};
			saveOrUpdate(buildMap(INSERT_COMMAND_QUEUE_HIS, args));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
		return dto;
	}
	@Override
	public PageDto qryCommandMsgInfoHisByPid(Map<String, Object> paramMap) {
		PageDto pageDto = new PageDto();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT ID,COMMAND_CODE,COMMAND_MSG,PROCESS_INSTANCE_ID,STATE,CREATE_DATE,COMMAND_RESULT_MSG,WORKITEM_ID ")
		.append(" FROM UOS_COMMAND_QUEUE_HIS WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if (null != paramMap.get("processInstanceId")
				&& !"".equals(paramMap.get("processInstanceId"))) {
			sqlStr.append(" AND PROCESS_INSTANCE_ID = ? ");
			params.add(StringHelper.valueOf(paramMap.get("processInstanceId")));
		}
		QueryFilter filter = new QueryFilter();
		filter.setOrder(QueryFilter.DESC);
		filter.setOrderBy("CREATEDDATE");
		if(paramMap.get("pageIndex")!=null&&paramMap.get("pageSize")!=null){
			Page<CommandQueueDto> page = new Page<CommandQueueDto>();
			page.setPageNo(IntegerHelper.valueOf(String.valueOf(paramMap.get("pageIndex"))));
			page.setPageSize(IntegerHelper.valueOf(String.valueOf(paramMap.get("pageSize"))));
			List<CommandQueueDto> list = queryList(page,filter,CommandQueueDto.class,sqlStr.toString(),params.toArray(new Object[]{}));
			pageDto.setRows(list);
			pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
		}else{
			List<CommandQueueDto> list = queryList(CommandQueueDto.class,sqlStr.toString(),params.toArray(new Object[]{}));
			pageDto.setRows(list);
			pageDto.setTotal(list.size());
		}
		return pageDto;
	}
	@Override
	public CommandQueueDto qryCommandMsgInfoHisByWid(Map<String, Object> map) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT ID,COMMAND_CODE,COMMAND_MSG,PROCESS_INSTANCE_ID,STATE,CREATE_DATE,COMMAND_RESULT_MSG")
		.append(" FROM UOS_COMMAND_QUEUE_HIS WHERE 1=1 AND WORKITEM_ID=?");
		String workItemId = StringHelper.valueOf(map.get("workItemId"));
		return queryObject(CommandQueueDto.class, sqlStr.toString(), new Object[]{workItemId});
	}
	@Override
	public PageDto queryCommandHisByCond(Map<String, Object> paramMap) {
		PageDto pageDto = new PageDto();
		StringBuffer sqlStr = new StringBuffer(QUERY_COMMAND_QUEUE_HIS)
				.append(" WHERE 1=1 AND UCQ.STATE=0 ");
		List<Object> params = new ArrayList<Object>();
		if (paramMap.get("processInstanceId") != null) {
			sqlStr.append(" AND UCQ.PROCESS_INSTANCE_ID = ? ");
			params.add(LongHelper.valueOf(paramMap.get("processInstanceId")));
		}
		if (paramMap.get("startDate") != null) {
			sqlStr.append(" AND UCQ.CREATE_DATE > ").append(
					JDBCHelper.getDialect().getFormatDate());
			params.add(StringHelper.valueOf(paramMap.get("startDate")));
		}
		if (paramMap.get("endDate") != null) {
			sqlStr.append(" AND UCQ.CREATE_DATE < ").append(
					JDBCHelper.getDialect().getFormatDate());
			params.add(StringHelper.valueOf(paramMap.get("endDate")));
		}
		if (null != paramMap.get("userAreaId")
				&& !"".equals(paramMap.get("userAreaId"))) {
			String userAreaId = StringHelper.valueOf(paramMap.get("userAreaId")); 
			sqlStr.append(" AND  UCQ.AREA_ID in (").append(userAreaId);
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
			filter.setOrderBy("CREATE_DATE");
			filter.setOrder(QueryFilter.DESC);
		}
		if(paramMap.get("pageIndex")!=null&&paramMap.get("pageSize")!=null){
			Page<CommandQueueDto> page = new Page<CommandQueueDto>();
			page.setPageNo(IntegerHelper.valueOf(String.valueOf(paramMap.get("pageIndex"))));
			page.setPageSize(IntegerHelper.valueOf(String.valueOf(paramMap.get("pageSize"))));
			List<CommandQueueDto> list = queryList(page,filter,CommandQueueDto.class,sqlStr.toString(),params.toArray(new Object[]{}));
			pageDto.setRows(list);
			pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
		}else{
			List<CommandQueueDto> list = queryList(CommandQueueDto.class,sqlStr.toString(),params.toArray(new Object[]{}));
			pageDto.setRows(list);
			pageDto.setTotal(list.size());
		}
		return pageDto;
	}

}
