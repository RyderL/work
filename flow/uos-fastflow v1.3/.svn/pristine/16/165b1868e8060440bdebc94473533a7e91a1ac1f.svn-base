package com.zterc.uos.fastflow.dao.process.his.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.dao.process.his.ProcessInstanceHisDAO;
import com.zterc.uos.fastflow.dao.specification.AreaDAO;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

@Repository("processInstanceHisDAO")
public class ProcessInstanceHisDAOImpl extends AbstractDAOImpl implements
		ProcessInstanceHisDAO {
	@Autowired
	private AreaDAO areaDAO;
	protected static final String INSERT_PROCESS_INSTANCE_HIS = "INSERT INTO UOS_PROCESSINSTANCE_HIS(PROCESSINSTANCEID,PACKAGEDEFINEID, PROCESSDEFINITIONNAME,"
			+ "PARENTACTIVITYINSTANCEID,NAME,CREATEDDATE,STARTEDDATE,COMPLETEDDATE,DUEDATE,PRIORITY,STATE,"
			+ "PARTICIPANTID,PARTICIPANTPOSITIONID,OLD_PROCESSINSTANCEID,FLAG,AREA_ID,LIMIT_DATE,ALERT_DATE)  "
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',?,?,?)";

	protected static final String QUERY_PROCESS_INSTANCE_HIS = "SELECT PROCESSINSTANCEID,PACKAGEDEFINEID, PROCESSDEFINITIONNAME,"
			+ "PARENTACTIVITYINSTANCEID,NAME,CREATEDDATE,STARTEDDATE,COMPLETEDDATE,DUEDATE,"
			+ "PRIORITY,STATE,PARTICIPANTID,PARTICIPANTPOSITIONID,OLD_PROCESSINSTANCEID,SIGN,AREA_ID "
			+ "FROM UOS_PROCESSINSTANCE_HIS ";

	@Override
	public ProcessInstanceDto createProcessInstanceHis(
			ProcessInstanceDto processInstance) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(Long.valueOf(processInstance.getProcessInstanceId()));
			Object[] args = new Object[] {
					processInstance.getProcessInstanceId(),
					processInstance.getProcessDefineId(),
					processInstance.getProcessDefinitionName(),
					processInstance.getParentActivityInstanceId(),
					processInstance.getName(),
					processInstance.getCreatedDate(),
					processInstance.getStartedDate(),
					processInstance.getCompletedDate(),
					processInstance.getDueDate(),
					processInstance.getPriority(),
					processInstance.getState(),
					processInstance.getParticipantId(),
					processInstance.getParticipantPositionId(),
					processInstance.getOldProcessInstanceId() == null ? processInstance
							.getProcessInstanceId() : processInstance
							.getOldProcessInstanceId(),
					processInstance.getAreaId(),
					DateHelper.parseTime(processInstance.getLimitDate()),
					DateHelper.parseTime(processInstance.getAlertDate())};
			saveOrUpdate(buildMap(INSERT_PROCESS_INSTANCE_HIS, args));

			return processInstance;

		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public PageDto queryProcessInstancesHisByCond(Map<String, Object> paramMap) {
		PageDto pageDto = new PageDto();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(
				"SELECT UPI.PROCESSINSTANCEID, UPI.PACKAGEDEFINEID, UPI.PROCESSDEFINITIONNAME, UPI.PARENTACTIVITYINSTANCEID, UPI.NAME,")
				.append(" UPI.CREATEDDATE, UPI.STARTEDDATE, UPI.COMPLETEDDATE, UPI.DUEDATE, UPI.PRIORITY, UPI.STATE,")
				.append(" UPI.PARTICIPANTID, UPI.PARTICIPANTPOSITIONID, UPI.OLD_PROCESSINSTANCEID, UPI.AREA_ID,UPD.PACKAGEDEFINECODE,UA1.AREA_NAME ")
				.append(" FROM UOS_PROCESSINSTANCE_HIS UPI")
				.append(" LEFT JOIN UOS_PROCESSDEFINE UPD ON UPD.PACKAGEDEFINEID=UPI.PACKAGEDEFINEID")
				.append(" LEFT JOIN UOS_AREA UA3 ON UA3.AREA_ID = UA3.PARENT_ID ")
				.append(" LEFT JOIN UOS_AREA UA1 ON UA1.AREA_ID = UPI.AREA_ID ")
				.append(" WHERE 1=1 ");
		boolean isQrySubFlow = false;// ������ӵ�е�������
		StringBuffer unionSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		if (null != paramMap.get("processInstanceId")
				&& !"".equals(paramMap.get("processInstanceId"))) {
			sqlStr.append(" AND UPI.PROCESSINSTANCEID = ? ");
			params.add(StringHelper.valueOf(paramMap.get("processInstanceId")));
			isQrySubFlow = true;
			unionSql.append(
					"UNION SELECT UP.PROCESSINSTANCEID,UP.PACKAGEDEFINEID,UP.PROCESSDEFINITIONNAME,UP.PARENTACTIVITYINSTANCEID,UP.NAME,UP.CREATEDDATE,UP.STARTEDDATE,")
					.append("UP.COMPLETEDDATE,UP.DUEDATE,UP.PRIORITY,UP.STATE,UP.PARTICIPANTID,UP.PARTICIPANTPOSITIONID," +
							"UP.OLD_PROCESSINSTANCEID,UP.AREA_ID,UP2.PACKAGEDEFINECODE,UA2.AREA_NAME ")
					.append(" FROM UOS_PROCESSINSTANCE_HIS UP JOIN UOS_ACTIVITYINSTANCE_HIS UA ON UP.PARENTACTIVITYINSTANCEID = UA.ACTIVITYINSTANCEID")
					.append(" LEFT JOIN UOS_PROCESSDEFINE UP2 ON UP2.PACKAGEDEFINEID=UP.PACKAGEDEFINEID")
					.append(" LEFT JOIN UOS_AREA UA2 ON UA2.AREA_ID = UP.AREA_ID ")
					.append(" WHERE 1=1 AND UA.PROCESSINSTANCEID = ? ");
		}
		if (null != paramMap.get("startDate")
				&& !"".equals(paramMap.get("startDate"))) {
			sqlStr.append(" AND UPI.CREATEDDATE > ").append(
					JDBCHelper.getDialect().getFormatDate());
			params.add(StringHelper.valueOf(paramMap.get("startDate")));
		}
		if (null != paramMap.get("endDate")
				&& !"".equals(paramMap.get("endDate"))) {
			sqlStr.append(" AND UPI.CREATEDDATE < ").append(
					JDBCHelper.getDialect().getFormatDate());
			params.add(StringHelper.valueOf(paramMap.get("endDate")));
		}
		if (null != paramMap.get("state")
				&& !"".equals(paramMap.get("state"))) {
			sqlStr.append(" AND UPI.STATE =? ");
			params.add(StringHelper.valueOf(paramMap.get("state")));
		}
		if (isQrySubFlow) {
			sqlStr.insert(0, "SELECT PROCESSINSTANCEID,PACKAGEDEFINEID,PROCESSDEFINITIONNAME, NAME,CREATEDDATE,STARTEDDATE," 
					+"COMPLETEDDATE,DUEDATE,PRIORITY,STATE,PARTICIPANTID,PARTICIPANTPOSITIONID,OLD_PROCESSINSTANCEID,AREA_ID,"
					+"PACKAGEDEFINECODE,AREA_NAME FROM(");
			sqlStr.append(unionSql).append(") A WHERE 1=1 ");
			params.add(StringHelper.valueOf(paramMap.get("processInstanceId")));
		}
		if (null != paramMap.get("userAreaId")
				&& !"".equals(paramMap.get("userAreaId"))) {
			String userAreaId = StringHelper.valueOf(paramMap.get("userAreaId"));
			if(userAreaId != null){
				if(isQrySubFlow){
					sqlStr.append(" AND  A.AREA_ID in (").append(userAreaId);
				}else{
					sqlStr.append(" AND  UPI.AREA_ID in (").append(userAreaId);
				} 
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
			filter.setOrder(QueryFilter.DESC);
			filter.setOrderBy("CREATEDDATE");
		}
		
		if(paramMap.get("pageIndex")!=null&&paramMap.get("pageSize")!=null){
			Page<ProcessInstanceDto> page = new Page<ProcessInstanceDto>();
			page.setPageNo(IntegerHelper.valueOf(String.valueOf(paramMap.get("pageIndex"))));
			page.setPageSize(IntegerHelper.valueOf(String.valueOf(paramMap.get("pageSize"))));
			List<ProcessInstanceDto> list = queryList(page,filter,ProcessInstanceDto.class,sqlStr.toString(),params.toArray(new Object[]{}));
			pageDto.setRows(list);
			pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
		}else{
			List<ProcessInstanceDto> list = queryList(ProcessInstanceDto.class,sqlStr.toString(),params.toArray(new Object[]{}));
			pageDto.setRows(list);
			pageDto.setTotal(list.size());
		}
		return pageDto;
	}

	@Override
	public ProcessInstanceDto queryProcessInstance(String processInstanceId) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(Long.valueOf(processInstanceId));
			String where = " WHERE PROCESSINSTANCEID = ?";
			return queryObject(ProcessInstanceDto.class, QUERY_PROCESS_INSTANCE_HIS
					+ where, processInstanceId);
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

}
