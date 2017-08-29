package com.zterc.uos.fastflow.dao.process.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.process.WorkItemDAO;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.AsynSqlExecBy3thParamDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.SqlLocalHolder;

public class WorkItemDAOImpl extends AbstractDAOImpl implements WorkItemDAO {

	protected static final String INSERT_WORKITEM = "INSERT INTO UOS_WORKITEM(WORKITEMID,OLD_WORKITEMID,"
			+ "ACTIVITYDEFINITIONID,ACTIVITYINSTANCEID,PACKAGEDEFINEID,"
			+ "PROCESSINSTANCEID,PROCESSINSTANCENAME,TACHE_ID,NAME,"
			+ "PARTICIPANTID,PARTICIPANTPOSITIONID,ORGANIZATIONID,"
			+ "ASSIGNEDDATE,STARTEDDATE,COMPLETEDDATE,PRIORITY,DUEDATE,"
			+ "STATE,MEMO,BATCH_ID,CREATE_SOURCE,AREA_ID,"
			+ "PARTY_TYPE,PARTY_ID,PARTY_NAME,MANUAL_PARTY_TYPE,MANUAL_PARTY_ID,"
			+ "MANUAL_PARTY_NAME,IS_AUTO) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	protected static final String UPDATE_WORKITEM = "UPDATE UOS_WORKITEM SET STARTEDDATE=?,COMPLETEDDATE=?,PRIORITY=?,"
			+ "DUEDATE=?,STATE=?,MEMO=?,SUBFLOW_COUNT=?,FINISH_SUBFLOW_COUNT=?,OPERATE_PARTY_TYPE=?,OPERATE_PARTY_ID=?,OPERATE_PARTY_NAME=?," +
			" LIMIT_DATE="+JDBCHelper.getDialect().getFormatDate()+",ALERT_DATE="+JDBCHelper.getDialect().getFormatDate()+" "
			+ " WHERE WORKITEMID = ?";

	protected static final String QUERY_WORKITEM = "SELECT WORKITEMID,OLD_WORKITEMID,ACTIVITYDEFINITIONID,ACTIVITYINSTANCEID,"
			+ "PACKAGEDEFINEID,PROCESSINSTANCEID,PROCESSINSTANCENAME,TACHE_ID,NAME,"
			+ "PARTICIPANTID,PARTICIPANTPOSITIONID,"
			+ "ORGANIZATIONID,ASSIGNEDDATE,STARTEDDATE,COMPLETEDDATE,"
			+ "PRIORITY,DUEDATE,STATE,MEMO,SUBFLOW_COUNT,FINISH_SUBFLOW_COUNT, "
			+ "PARTY_TYPE,PARTY_ID,PARTY_NAME,MANUAL_PARTY_TYPE,MANUAL_PARTY_ID,"
			+ "MANUAL_PARTY_NAME,OPERATE_PARTY_TYPE,OPERATE_PARTY_ID,OPERATE_PARTY_NAME,IS_AUTO"
			+ " FROM UOS_WORKITEM ";

	@Override
	public Long getWorkItemBatchNo() {
		return SequenceHelper.getId("WO_WORK_ORDER_BATCH");
	}

	@Override
	public WorkItemDto createWorkItem(WorkItemDto workItem) {
		Object[] args = new Object[] {
				workItem.getWorkItemId(),
				workItem.getOldWorkItemId(),
				workItem.getActivityDefinitionId(),
				workItem.getActivityInstanceId(),
				workItem.getProcessDefineId(),
				workItem.getProcessInstanceId(),
				workItem.getProcessInstanceName(),
				workItem.getTacheId(),
				workItem.getName(),
				workItem.getParticipantId(),
				workItem.getParticipantPositionId(),
				workItem.getOrganizationId(),
				workItem.getAssignedDate(),
				workItem.getStartedDate(),
				workItem.getCompletedDate(),
				workItem.getPriority(),
				workItem.getDueDate(),
				workItem.getState(),
				workItem.getMemo(),
				workItem.getBatchid(),
				workItem.getCreateSource() == null ? 0 : workItem
						.getCreateSource(), workItem.getAreaId(),
				workItem.getPartyType(), workItem.getPartyId(),
				workItem.getPartyName(), workItem.getManualPartyType(),
				workItem.getManualPartyId(), workItem.getManualPartyName(),
				workItem.getIsAuto() };
		if(!SqlLocalHolder.isHoldSqlOn()){
			saveOrUpdate(buildMap(INSERT_WORKITEM, args));
		}else{

			AsynSqlExecBy3thParamDto sqlParam = new AsynSqlExecBy3thParamDto(INSERT_WORKITEM,
					StringHelper.valueOf(workItem.getWorkItemId()), args,
					AsynSqlExecBy3thParamDto.INSERT, "UOS_WORKITEM",workItem.getState());
			SqlLocalHolder.addSqlParam(sqlParam);
		}

		return workItem;
	}

	@Override
	public WorkItemDto queryWorkItem(String workItemId) {
		String where = " WHERE WORKITEMID=?";
		return queryObject(WorkItemDto.class, QUERY_WORKITEM + where,
				workItemId);
	}

	@Override
	public WorkItemDto queryWorkItemByTacheId(Long processInstanceId,
			Long targetTacheId) {
		String where = " WHERE TACHE_ID= ? AND PROCESSINSTANCEID = ? ORDER BY STARTEDDATE DESC,WORKITEMID DESC";
		return queryObject(WorkItemDto.class, QUERY_WORKITEM + where,
				targetTacheId, processInstanceId);
	}

	@Override
	public void updateWorkItem(WorkItemDto workItem) {
		Object[] args = new Object[] { workItem.getStartedDate(),
				workItem.getCompletedDate(), workItem.getPriority(),
				workItem.getDueDate(), workItem.getState(), workItem.getMemo(),
				workItem.getSubFlowCount(), workItem.getFinishSubFlowCount(),
				workItem.getOperatePartyType(), workItem.getOperatePartyId(),
				workItem.getOperatePartyName(),
				DateHelper.parseTime(workItem.getLimitDate()),DateHelper.parseTime(workItem.getAlertDate()),
				workItem.getWorkItemId() };
		if(!SqlLocalHolder.isHoldSqlOn()){
			saveOrUpdate(buildMap(UPDATE_WORKITEM, args));
		}else{

			AsynSqlExecBy3thParamDto sqlParam = new AsynSqlExecBy3thParamDto(UPDATE_WORKITEM,
					StringHelper.valueOf(workItem.getWorkItemId()), args,
					AsynSqlExecBy3thParamDto.UPDATE, "UOS_WORKITEM",workItem.getState());
			SqlLocalHolder.addSqlParam(sqlParam);
		}
	}

	@Override
	public PageDto findWorkItemByCond(Map<String, Object> paramMap) {
		PageDto pageDto = new PageDto();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(
				"SELECT UW.WORKITEMID,UW.OLD_WORKITEMID,UW.ACTIVITYDEFINITIONID,UW.ACTIVITYINSTANCEID,")
				.append("UW.PACKAGEDEFINEID,UW.PROCESSINSTANCEID,UW.PROCESSINSTANCENAME,UW.TACHE_ID,UW.NAME,")
				.append("UW.PARTICIPANTID,UW.PARTICIPANTPOSITIONID,UW.ORGANIZATIONID,UW.ASSIGNEDDATE,UW.STARTEDDATE,UW.COMPLETEDDATE,")
				.append("UW.PRIORITY,UW.DUEDATE,UW.STATE,UW.MEMO,UT.TACHE_CODE,UW.AREA_ID,UA.DIRECTION, ")
				.append("UW.PARTY_TYPE,UW.PARTY_ID,UW.PARTY_NAME,UW.MANUAL_PARTY_TYPE,UW.MANUAL_PARTY_ID,")
				.append("UW.MANUAL_PARTY_NAME,UW.OPERATE_PARTY_TYPE,UW.OPERATE_PARTY_ID,UW.OPERATE_PARTY_NAME,UW.IS_AUTO,UA1.AREA_NAME ")
				.append(" FROM UOS_WORKITEM UW LEFT JOIN "+FastflowConfig.tacheTableName+" UT ON UW.TACHE_ID = UT.ID ")
				.append(" LEFT JOIN UOS_ACTIVITYINSTANCE UA ON UW.ACTIVITYINSTANCEID=UA.ACTIVITYINSTANCEID ")
				.append(" LEFT JOIN UOS_AREA UA1 ON UA1.AREA_ID = UW.AREA_ID")
				.append(" WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (null != paramMap.get("workitemId")
				&& !"".equals(paramMap.get("workitemId"))) {
			sqlStr.append(" AND UW.WORKITEMID = ? ");
			params.add(LongHelper.valueOf(paramMap.get("workitemId")));
		}
		if (null != paramMap.get("processInstanceId")
				&& !"".equals(paramMap.get("processInstanceId"))) {
			sqlStr.append(" AND UW.PROCESSINSTANCEID = ? ");
			params.add(LongHelper.valueOf(paramMap.get("processInstanceId")));
		}
		if (null != paramMap.get("STATE_ID")
				&& !"".equals(paramMap.get("STATE_ID"))) {
			sqlStr.append(" AND STATE = ?");
			params.add((String) (paramMap.get("STATE_ID")));
		}
		if (null != paramMap.get("startDate")
				&& !"".equals(paramMap.get("startDate"))) {
			sqlStr.append(" AND STARTEDDATE > ").append(
					JDBCHelper.getDialect().getFormatDate());
			params.add((String) paramMap.get("startDate"));
		}
		if (null != paramMap.get("completedDate")
				&& !"".equals(paramMap.get("completedDate"))) {
			sqlStr.append(" AND COMPLETEDDATE <  ").append(
					JDBCHelper.getDialect().getFormatDate());
			params.add((String) paramMap.get("completedDate"));
		}
		if (null != paramMap.get("areaId")
				&& !"".equals(paramMap.get("areaId"))) {
			sqlStr.append(" AND AREA_ID = ? ");
			params.add((String) paramMap.get("areaId"));
		}
		QueryFilter queryFilter = new QueryFilter();
		if (paramMap.get("sortColumn") != null
				&& !"".equals(paramMap.get("sortColumn"))
				&& paramMap.get("sortOrder") != null
				&& !"".equals(paramMap.get("sortOrder"))) {
			String sortColumn = StringHelper
					.valueOf(paramMap.get("sortColumn")).toUpperCase();
			String sortOrder = StringHelper.valueOf(paramMap.get("sortOrder"))
					.toUpperCase();
			if(!"workItemId".equalsIgnoreCase(sortColumn)){
				queryFilter.setOrderBy(sortColumn+",WORKITEMID");
				queryFilter.setOrder(sortOrder+","+QueryFilter.ASC);
			}else{
				queryFilter.setOrderBy(sortColumn);
				queryFilter.setOrder(sortOrder);
			}
		} else {
			queryFilter.setOrderBy("ASSIGNEDDATE,WORKITEMID");
			queryFilter.setOrder(QueryFilter.ASC+","+QueryFilter.ASC);
		}
		if (paramMap.get("pageIndex") != null
				&& paramMap.get("pageSize") != null) {
			Page<WorkItemDto> page = new Page<WorkItemDto>();
			page.setPageNo(IntegerHelper.valueOf(String.valueOf(paramMap
					.get("pageIndex"))));
			page.setPageSize(IntegerHelper.valueOf(String.valueOf(paramMap
					.get("pageSize"))));
			List<WorkItemDto> list = queryList(page, queryFilter,
					WorkItemDto.class, sqlStr.toString(),
					params.toArray(new Object[] {}));
			pageDto.setRows(list);
			pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
		} else {
			List<WorkItemDto> list = queryList(WorkItemDto.class,queryFilter,
					sqlStr.toString(), params.toArray(new Object[] {}));
			pageDto.setRows(list);
			pageDto.setTotal(list.size());
		}
		return pageDto;
	}

	@Override
	public List<WorkItemDto> queryWorkItemsByProcess(String processInstanceId,
			int state, String disableWorkItemId) {
		String where = "WHERE PROCESSINSTANCEID = ? AND WORKITEMID <> ?";
		List<Object> params = new ArrayList<Object>();
		params.add(processInstanceId);
		params.add(disableWorkItemId == null ? "-1" : disableWorkItemId);
		if(state != -1){
			where += " AND STATE = ? ";
			params.add(state);
		}
		where += (" ORDER BY STARTEDDATE ASC");
		return queryList(WorkItemDto.class, QUERY_WORKITEM + where,params.toArray(new Object[params.size()]));
	}

	@Override
	public List<Map<String, Object>> qryWorkItemStateCount(
			Map<String, Object> paramMap) {
		String sql = "SELECT CASE UP.STATE WHEN 0 THEN '挂起' WHEN 1 THEN '正常执行中' WHEN 2 THEN '作废'" +
				" WHEN 3 THEN '终止' WHEN 4 THEN '已完成' WHEN 6 THEN '禁用' END AS name," +
				" COUNT(1) AS value" +
				" FROM UOS_WORKITEM UP";
		if(paramMap != null && paramMap.get("states") != null && !"".equals(paramMap.get("states"))){
			sql += " WHERE UP.STATE IN (" + StringHelper.valueOf(paramMap.get("states")) + ")";
		}
		sql += " GROUP BY UP.STATE";
		List<Map<String,Object>> list = JDBCHelper.getJdbcTemplate().queryForList(sql);
		return list;
	}

	@Override
	public WorkItemDto qryWorkItemByActInstId(Long activityInstanceId) {
		String where = " WHERE ACTIVITYINSTANCEID=?";
		return queryObject(WorkItemDto.class, QUERY_WORKITEM + where,
				activityInstanceId);
	}

	@Override
	public List<WorkItemDto> queryWorkItemsByPid(String processInstanceId) {
		String where = "WHERE PROCESSINSTANCEID = ? ";
		return queryList(WorkItemDto.class, QUERY_WORKITEM + where,new Object[]{processInstanceId});
	}

	@Override
	public void deleteByPid(String processInstanceId) {
		String sql = "DELETE FROM UOS_WORKITEM WHERE PROCESSINSTANCEID=?";
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

	@Override
	public List<WorkItemDto> queryUndoWorkItems(Map<String, Object> params) {
		String processInstanceId = StringHelper.valueOf(params.get("processInstanceId"));
		String where = "WHERE PROCESSINSTANCEID = ? AND STATE=1";
		return queryList(WorkItemDto.class, QUERY_WORKITEM + where,new Object[]{processInstanceId});
	}
}
