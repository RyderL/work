package com.zterc.uos.fastflow.dao.process.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zterc.uos.base.dialect.DaasDialect;
import com.zterc.uos.base.dialect.MySqlDialect;
import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.dao.process.ProcessInstanceDAO;
import com.zterc.uos.fastflow.dao.specification.AreaDAO;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.AsynSqlExecBy3thParamDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.SqlLocalHolder;

public class ProcessInstanceDAOImpl extends AbstractDAOImpl implements
		ProcessInstanceDAO {
	@Autowired
	private AreaDAO areaDAO;
	//modify by bobping 流程模板id改成流程模板编码获取异常原因配置
	protected static final String INSERT_PROCESS_INSTANCE = "INSERT INTO UOS_PROCESSINSTANCE(PROCESSINSTANCEID,PACKAGEDEFINEID,PACKAGEDEFINECODE, PROCESSDEFINITIONNAME,"
			+ "PARENTACTIVITYINSTANCEID,NAME,CREATEDDATE,STARTEDDATE,COMPLETEDDATE,DUEDATE,PRIORITY,STATE,"
			+ "PARTICIPANTID,PARTICIPANTPOSITIONID,OLD_PROCESSINSTANCEID,FLAG,AREA_ID,LIMIT_DATE,ALERT_DATE)  "
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',?,?,?)";

	protected static final String UPDATE_PROCESS_INSTANCE = "UPDATE UOS_PROCESSINSTANCE SET PACKAGEDEFINEID=?,PROCESSDEFINITIONNAME=?, "
			+ "NAME=?,STARTEDDATE=?,COMPLETEDDATE=?,DUEDATE=?,PRIORITY=?,STATE=?,"
			+ "PARTICIPANTID=?,PARTICIPANTPOSITIONID=?,OLD_PROCESSINSTANCEID=? "
			+ ",LIMIT_DATE=?,ALERT_DATE=?,SUSPEND_DATE=?,RESUME_DATE=?" +
			" WHERE PROCESSINSTANCEID=?";

	//modify by bobping 流程模板id改成流程模板编码获取异常原因配置
	protected static final String QUERY_PROCESS_INSTANCE = "SELECT PROCESSINSTANCEID,PACKAGEDEFINEID,PACKAGEDEFINECODE, PROCESSDEFINITIONNAME,"
			+ "PARENTACTIVITYINSTANCEID,NAME,CREATEDDATE,STARTEDDATE,COMPLETEDDATE,DUEDATE,"
			+ "PRIORITY,STATE,PARTICIPANTID,PARTICIPANTPOSITIONID,OLD_PROCESSINSTANCEID,SIGN,AREA_ID "
			+ "FROM UOS_PROCESSINSTANCE ";

	@Override
	public ProcessInstanceDto queryProcessInstance(String processInstanceId) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(Long.valueOf(processInstanceId));
			String where = " WHERE PROCESSINSTANCEID = ?";
			return queryObject(ProcessInstanceDto.class, QUERY_PROCESS_INSTANCE
					+ where, processInstanceId);
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public ProcessInstanceDto createProcessInstance(
			ProcessInstanceDto processInstance) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(Long.valueOf(processInstance.getProcessInstanceId()));
			Object[] args = new Object[] {
					processInstance.getProcessInstanceId(),
					processInstance.getProcessDefineId(),
					//modify by bobping 流程模板id改成流程模板编码获取异常原因配置
					processInstance.getProcessDefineCode(),
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
			if(!SqlLocalHolder.isHoldSqlOn()){
				saveOrUpdate(buildMap(INSERT_PROCESS_INSTANCE, args));
			}else{

				AsynSqlExecBy3thParamDto sqlParam = new AsynSqlExecBy3thParamDto(INSERT_PROCESS_INSTANCE,
						StringHelper.valueOf(processInstance.getProcessInstanceId()), args,
						AsynSqlExecBy3thParamDto.INSERT, "UOS_PROCESSINSTANCE",processInstance.getState());
				SqlLocalHolder.addSqlParam(sqlParam);
			}

			return processInstance;

		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public void updateProcessInstance(ProcessInstanceDto processInstance) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(Long.valueOf(processInstance.getProcessInstanceId()));
			Object[] args = new Object[] { processInstance.getProcessDefineId(),
					processInstance.getProcessDefinitionName(),
					processInstance.getName(), processInstance.getStartedDate(),
					processInstance.getCompletedDate(),
					processInstance.getDueDate(), processInstance.getPriority(),
					processInstance.getState(), processInstance.getParticipantId(),
					processInstance.getParticipantPositionId(),
					processInstance.getOldProcessInstanceId(),
					processInstance.getLimitDate(),
					processInstance.getAlertDate(),
					processInstance.getSuspendDate(),
					processInstance.getResumeDate(),
					processInstance.getProcessInstanceId()};
			if(!SqlLocalHolder.isHoldSqlOn()){
				saveOrUpdate(buildMap(UPDATE_PROCESS_INSTANCE, args));
			}else{

				AsynSqlExecBy3thParamDto sqlParam = new AsynSqlExecBy3thParamDto(UPDATE_PROCESS_INSTANCE,
						StringHelper.valueOf(processInstance.getProcessInstanceId()), args,
						AsynSqlExecBy3thParamDto.UPDATE, "UOS_PROCESSINSTANCE",processInstance.getState());
				SqlLocalHolder.addSqlParam(sqlParam);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public PageDto queryProcessInstancesByCond(Map<String, Object> paramMap) {
		PageDto pageDto = new PageDto();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(
				"SELECT UPI.PROCESSINSTANCEID, UPI.PACKAGEDEFINEID, UPI.PROCESSDEFINITIONNAME, UPI.PARENTACTIVITYINSTANCEID, UPI.NAME,")
				.append(" UPI.CREATEDDATE, UPI.STARTEDDATE, UPI.COMPLETEDDATE, UPI.DUEDATE, UPI.PRIORITY, UPI.STATE,")
				.append(" UPI.PARTICIPANTID, UPI.PARTICIPANTPOSITIONID, UPI.OLD_PROCESSINSTANCEID, UPI.AREA_ID,UPD.PACKAGEDEFINECODE,UA1.AREA_NAME ")
				.append(" FROM UOS_PROCESSINSTANCE UPI")
				.append(" LEFT JOIN UOS_PROCESSDEFINE UPD ON UPD.PACKAGEDEFINEID=UPI.PACKAGEDEFINEID")
//				.append(" LEFT JOIN UOS_AREA UA3 ON UA3.AREA_ID = UA3.PARENT_ID ")
				.append(" LEFT JOIN UOS_AREA UA1 ON UA1.AREA_ID = UPI.AREA_ID ")
				.append(" WHERE 1=1 ");
		boolean isQrySubFlow = false;// 是否包含子流程
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
					.append(" FROM UOS_PROCESSINSTANCE UP JOIN UOS_ACTIVITYINSTANCE UA ON UP.PARENTACTIVITYINSTANCEID = UA.ACTIVITYINSTANCEID")
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
			String sortColumn = StringHelper.valueOf(paramMap.get("sortColumn")).toUpperCase();
			String sortOrder = StringHelper.valueOf(paramMap.get("sortOrder")).toUpperCase();
			filter.setOrder(sortOrder);
			filter.setOrderBy(sortColumn);
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

	@SuppressWarnings("all")
	@Override
	public List<Map<String,Object>> qryProcInstStateCount(String states) {
		String sql = "SELECT CASE UP.STATE WHEN 0 THEN '初始化' WHEN 1 THEN '挂起' WHEN 2 THEN '正常执行中'" +
				" WHEN 3 THEN '作废' WHEN 4 THEN '终止' WHEN 5 THEN '已完成' WHEN 6 THEN '归档'" +
				" WHEN 7 THEN '调度异常' WHEN 8 THEN '归零' WHEN 9 THEN '流程回退中' END AS name," +
				" COUNT(1) AS value" +
				" FROM UOS_PROCESSINSTANCE UP";
		if(states != null && !"".equals(states)){
			sql += " WHERE UP.STATE IN ("+states+")";
		}
		sql += " GROUP BY UP.STATE";
		List<Map<String,Object>> list = JDBCHelper.getJdbcTemplate().queryForList(sql);
		return list;
	}

	@Override
	public List<Map<String, Object>> qryProcDefineUseCount() {
		String sql = "";
		if(JDBCHelper.getDialect() instanceof DaasDialect){
			// daas 不支持对结果集再排序，所以用java自己再排序（升序），然后取最后6条记录
			sql = "SELECT UPD.NAME AS name,COUNT(1) AS value " +
					" FROM UOS_PROCESSINSTANCE UPI " +
					" LEFT JOIN UOS_PROCESSDEFINE UPD ON UPI.PACKAGEDEFINEID=UPD.PACKAGEDEFINEID" +
					" GROUP BY UPI.PACKAGEDEFINEID";
			List<Map<String,Object>> list = JDBCHelper.getJdbcTemplate().queryForList(sql);
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> o1,
						Map<String, Object> o2) {
					String value1 = (String) o1.get("value");
					String value2 = (String) o2.get("value");
					return value1.compareTo(value2);
				}
			});
		   if(list.size()>6){
			   for(int i=0;i<list.size()-6;i++){
				   list.remove(i);
			   }
		   }
		   return list;
		}else if(JDBCHelper.getDialect() instanceof MySqlDialect){
			sql = "SELECT UPD.NAME AS name,COUNT(1) AS value " +
					" FROM UOS_PROCESSINSTANCE UPI " +
					" LEFT JOIN UOS_PROCESSDEFINE UPD ON UPI.PACKAGEDEFINEID=UPD.PACKAGEDEFINEID" +
					" GROUP BY UPI.PACKAGEDEFINEID" +
					" ORDER BY VALUE DESC" +
					" LIMIT 0,6";
		}else{
			sql = "SELECT * FROM (SELECT UPD.NAME AS name,COUNT(1) AS value,UPI.PACKAGEDEFINEID " +
					" FROM UOS_PROCESSINSTANCE UPI " +
					" LEFT JOIN UOS_PROCESSDEFINE UPD ON UPI.PACKAGEDEFINEID=UPD.PACKAGEDEFINEID" +
					" GROUP BY UPI.PACKAGEDEFINEID,UPD.NAME" +
					" ORDER BY VALUE DESC ) A " +
					" WHERE ROWNUM >=1 AND ROWNUM <=6 ";
		}
		List<Map<String,Object>> list = JDBCHelper.getJdbcTemplate().queryForList(sql);
		return list;
	}

	@Override
	public PageDto qryExceptionFlow(Map<String, Object> paramMap) {
		PageDto pageDto = new PageDto();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(
				"SELECT UPI.PROCESSINSTANCEID, UPI.PACKAGEDEFINEID, UPI.PROCESSDEFINITIONNAME, UPI.PARENTACTIVITYINSTANCEID, UPI.NAME,")
				.append(" UPI.CREATEDDATE, UPI.STARTEDDATE, UPI.COMPLETEDDATE, UPI.DUEDATE, UPI.PRIORITY, UPI.STATE,")
				.append(" UPI.PARTICIPANTID, UPI.PARTICIPANTPOSITIONID, UPI.OLD_PROCESSINSTANCEID, UPI.AREA_ID,UPD.PACKAGEDEFINECODE")
				.append(" FROM UOS_PROCESSINSTANCE UPI")
				.append(" LEFT JOIN UOS_PROCESSDEFINE UPD ON UPD.PACKAGEDEFINEID=UPI.PACKAGEDEFINEID")
				.append(" WHERE UPI.STATE=7 ");
		List<Object> params = new ArrayList<Object>();
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
	public void deleteProcessInstance(String processInstanceId) {
		String sql = "DELETE FROM UOS_PROCESSINSTANCE WHERE PROCESSINSTANCEID=?";
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
