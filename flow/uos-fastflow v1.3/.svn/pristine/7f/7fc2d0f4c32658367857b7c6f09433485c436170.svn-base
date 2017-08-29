package com.zterc.uos.fastflow.dao.process.his.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.process.his.WorkItemHisDAO;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

@Repository("workItemHisDAO")
public class WorkItemHisDAOImpl extends AbstractDAOImpl implements WorkItemHisDAO {

	protected static final String INSERT_WORKITEM_HIS = "INSERT INTO UOS_WORKITEM_HIS(WORKITEMID,OLD_WORKITEMID,"
			+ "ACTIVITYDEFINITIONID,ACTIVITYINSTANCEID,PACKAGEDEFINEID,"
			+ "PROCESSINSTANCEID,PROCESSINSTANCENAME,TACHE_ID,NAME,"
			+ "PARTICIPANTID,PARTICIPANTPOSITIONID,ORGANIZATIONID,"
			+ "ASSIGNEDDATE,STARTEDDATE,COMPLETEDDATE,PRIORITY,DUEDATE,"
			+ "STATE,MEMO,BATCH_ID,CREATE_SOURCE,AREA_ID,"
			+ "PARTY_TYPE,PARTY_ID,PARTY_NAME,MANUAL_PARTY_TYPE,MANUAL_PARTY_ID,"
			+ "MANUAL_PARTY_NAME,IS_AUTO) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	@Override
	public WorkItemDto createWorkItemHis(WorkItemDto workItem) {
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
		saveOrUpdate(buildMap(INSERT_WORKITEM_HIS, args));

		return workItem;
	}

	@Override
	public PageDto findWorkItemHisByCond(Map<String, Object> paramMap) {
		PageDto pageDto = new PageDto();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(
				"SELECT UW.WORKITEMID,UW.OLD_WORKITEMID,UW.ACTIVITYDEFINITIONID,UW.ACTIVITYINSTANCEID,")
				.append("UW.PACKAGEDEFINEID,UW.PROCESSINSTANCEID,UW.PROCESSINSTANCENAME,UW.TACHE_ID,UW.NAME,")
				.append("UW.PARTICIPANTID,UW.PARTICIPANTPOSITIONID,UW.ORGANIZATIONID,UW.ASSIGNEDDATE,UW.STARTEDDATE,UW.COMPLETEDDATE,")
				.append("UW.PRIORITY,UW.DUEDATE,UW.STATE,UW.MEMO,UT.TACHE_CODE,UW.AREA_ID,UA.DIRECTION, ")
				.append("UW.PARTY_TYPE,UW.PARTY_ID,UW.PARTY_NAME,UW.MANUAL_PARTY_TYPE,UW.MANUAL_PARTY_ID,")
				.append("UW.MANUAL_PARTY_NAME,UW.OPERATE_PARTY_TYPE,UW.OPERATE_PARTY_ID,UW.OPERATE_PARTY_NAME,UW.IS_AUTO,UA1.AREA_NAME ")
				.append(" FROM UOS_WORKITEM_HIS UW LEFT JOIN "+FastflowConfig.tacheTableName+" UT ON UW.TACHE_ID = UT.ID ")
				.append(" LEFT JOIN UOS_ACTIVITYINSTANCE_HIS UA ON UW.ACTIVITYINSTANCEID=UA.ACTIVITYINSTANCEID ")
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

}
