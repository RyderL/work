package com.zterc.uos.fastflow.dao.process.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dao.process.ActivityInstanceDAO;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.AsynSqlExecBy3thParamDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.SqlLocalHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class ActivityInstanceDAOImpl extends AbstractDAOImpl implements
		ActivityInstanceDAO {
	private static Logger logger=Logger.getLogger(ActivityInstanceDAOImpl.class);
	protected static final String INSERT_ACTIVITY_INSTANCE = "INSERT INTO UOS_ACTIVITYINSTANCE(PROCESSINSTANCEID,ACTIVITYDEFINITIONID,"
			+ "ACTIVITYINSTANCEID,NAME,STATE,STARTEDDATE,COMPLETEDDATE,"
			+ "DUEDATE,PRIORITY,ITEMCOMPLETED,ITEMSUM,TACHE_ID,SYN_MESSAGE,DIRECTION,AREA_ID,ROLLBACK_TRANINS,REVERSE,WORKITEMID) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	protected static final String QUERY_ACTIVITY_INSTANCE = "SELECT PROCESSINSTANCEID,ACTIVITYDEFINITIONID,"
			+ "ACTIVITYINSTANCEID,NAME,STATE,STARTEDDATE,COMPLETEDDATE,DUEDATE,"
			+ "PRIORITY,ITEMCOMPLETED,ITEMSUM,TACHE_ID,SYN_MESSAGE,DIRECTION,ATOM_ACTIVITYINSTANCE_ID," 
			+ "ATOM_ACTIVITYDEFINITIONID,AREA_ID,ROLLBACK_TRANINS,REVERSE,WORKITEMID "
			+ "FROM UOS_ACTIVITYINSTANCE ";

	protected static final String UPDATE_ACTIVITY_INSTANCE = "UPDATE UOS_ACTIVITYINSTANCE SET WORKITEMID = ?,STATE = ?,STARTEDDATE = ?,"
			+ "COMPLETEDDATE = ?,DUEDATE = ?,PRIORITY = ?,ITEMCOMPLETED = ?,ITEMSUM = ?,DIRECTION = ?,ROLLBACK_TRANINS=?,REVERSE=? " 
			+ "WHERE ACTIVITYINSTANCEID = ?";

	@Override
	public ActivityInstanceDto queryActivityInstance(String activityInstanceId) {
		String where = " WHERE ACTIVITYINSTANCEID = ?";
		return queryObject(ActivityInstanceDto.class, QUERY_ACTIVITY_INSTANCE
				+ where, activityInstanceId);
	}


	@Override
	public ActivityInstanceDto getStartActivityInstance(Long processInstanceId) {
		ActivityInstanceDto result=null;
		String where = " WHERE NAME='开始节点' AND PROCESSINSTANCEID =? ORDER BY STARTEDDATE,ACTIVITYINSTANCEID";
		try{
			List<ActivityInstanceDto> ret= queryList(ActivityInstanceDto.class, QUERY_ACTIVITY_INSTANCE
					+ where, new Object[] {processInstanceId});

			if (CollectionUtils.isNotEmpty(ret)){
				result=ret.get(0);
			}
		}catch(Exception e){
		   logger.error("根据流程实例id="+processInstanceId+"查找开始节点失败!",e);
		}
		return result;
	}

	@Override
	public ActivityInstanceDto createActivityInstance(
			ActivityInstanceDto activityInstance) {
		Object[] args = new Object[] { activityInstance.getProcessInstanceId(),
				activityInstance.getActivityDefinitionId(),
				activityInstance.getId(), activityInstance.getName(),
				activityInstance.getState(), activityInstance.getStartedDate(),
				activityInstance.getCompletedDate(),
				activityInstance.getDueDate(), activityInstance.getPriority(),
				activityInstance.getItemCompleted(),
				activityInstance.getItemSum(), activityInstance.getTacheId(),
				activityInstance.getSynMessage(),
				activityInstance.getDirection(), activityInstance.getAreaId(),activityInstance.getRollbackTranins()
				,activityInstance.getReverse(),activityInstance.getWorkItemId() };
		//modify by bobping 
		if(!SqlLocalHolder.isHoldSqlOn()){
			saveOrUpdate(buildMap(INSERT_ACTIVITY_INSTANCE, args));
		}else{

			AsynSqlExecBy3thParamDto sqlParam = new AsynSqlExecBy3thParamDto(INSERT_ACTIVITY_INSTANCE,
					StringHelper.valueOf(activityInstance.getId()), args,
					AsynSqlExecBy3thParamDto.INSERT, "UOS_ACTIVITYINSTANCE",activityInstance.getState());
			SqlLocalHolder.addSqlParam(sqlParam);
		}

		return activityInstance;
	}

	@Override
	public void updateActivityInstance(ActivityInstanceDto activityInstance) {
		Object[] args = new Object[] { activityInstance.getWorkItemId(),
				activityInstance.getState(), activityInstance.getStartedDate(),
				activityInstance.getCompletedDate(),
				activityInstance.getDueDate(), activityInstance.getPriority(),
				activityInstance.getItemCompleted(),
				activityInstance.getItemSum(), activityInstance.getDirection(),
				activityInstance.getRollbackTranins(),activityInstance.getReverse(),
				activityInstance.getId() };
		if(!SqlLocalHolder.isHoldSqlOn()){
			saveOrUpdate(buildMap(UPDATE_ACTIVITY_INSTANCE, args));
		}else{

			AsynSqlExecBy3thParamDto sqlParam = new AsynSqlExecBy3thParamDto(UPDATE_ACTIVITY_INSTANCE,
					StringHelper.valueOf(activityInstance.getId()), args,
					AsynSqlExecBy3thParamDto.UPDATE, "UOS_ACTIVITYINSTANCE",activityInstance.getState());
			SqlLocalHolder.addSqlParam(sqlParam);
		}
	}

	@Override
	public List<ActivityInstanceDto> queryActivityInstancesByState(
			String processInstanceId, String activityId, String state,
			String direction) {
		List<Object> argsList = new ArrayList<Object>();
		StringBuffer sqlStr = new StringBuffer(" WHERE PROCESSINSTANCEID = ? ");
		argsList.add(processInstanceId);
		if (activityId != null) {
			sqlStr.append(" AND ACTIVITYDEFINITIONID=? ");
			argsList.add(activityId);
		}
		if (state != null) {
			sqlStr.append(" AND STATE IN (" + state + ") ");
		}
		if (direction != null) {
			sqlStr.append(" AND DIRECTION=? ");
			argsList.add(direction);
		}
		sqlStr.append(" ORDER BY STARTEDDATE ASC");
		return queryList(ActivityInstanceDto.class, QUERY_ACTIVITY_INSTANCE
				+ sqlStr.toString(), argsList.toArray(new Object[] {}));
	}
	
	protected static final String QUERY_ACTIVITY_INSTANCE_FROM = "SELECT UA.PROCESSINSTANCEID,UA.ACTIVITYDEFINITIONID,"
			+ "UA.ACTIVITYINSTANCEID,UA.NAME,UA.STATE,UA.STARTEDDATE,UA.COMPLETEDDATE,UA.DUEDATE,"
			+ "UA.PRIORITY,UA.ITEMCOMPLETED,UA.ITEMSUM,UA.TACHE_ID,UA.SYN_MESSAGE,UA.DIRECTION,UA.ATOM_ACTIVITYINSTANCE_ID," 
			+ "UA.ATOM_ACTIVITYDEFINITIONID,UA.AREA_ID,UA.ROLLBACK_TRANINS,UA.REVERSE,UA.WORKITEMID "
			+ "FROM UOS_ACTIVITYINSTANCE UA,UOS_TRANSITIONINSTANCE UT " 
			+ "WHERE UA.ACTIVITYINSTANCEID =  UT.TOACTIVITYINSTANCEID " +
			  "AND UT.FROMACTIVITYINSTANCEID = ? " +
			  "AND UT.PROCESSINSTANCEID = ?";

	@Override
	public List<ActivityInstanceDto> queryActivityInstancesByFrom(
			Long startActivityInstanceId, String processInstanceId) {
		return queryList(ActivityInstanceDto.class, QUERY_ACTIVITY_INSTANCE_FROM,
				startActivityInstanceId,processInstanceId);
	}

	@Override
	public ActivityInstanceDto queryActivityInstancesByStateDesc(
			String processInstanceId, String activityId, String state) {
		List<Object> argsList = new ArrayList<Object>();
		StringBuffer sqlStr = new StringBuffer(" WHERE PROCESSINSTANCEID = ? ");
		argsList.add(processInstanceId);
		if (activityId != null) {
			sqlStr.append(" AND ACTIVITYDEFINITIONID=? ");
			argsList.add(activityId);
		}
		if (state != null) {
			sqlStr.append(" AND STATE IN (" + state + ") ");
		}
		
		sqlStr.append("  ORDER BY  STARTEDDATE DESC,ACTIVITYDEFINITIONID DESC");
		return queryObject(ActivityInstanceDto.class, QUERY_ACTIVITY_INSTANCE
				+ sqlStr.toString(), argsList.toArray(new Object[] {}));
	}

	@Override
	public List<ActivityInstanceDto> findActivityInstances(
			Set<String> canReachedActivityInstanceIds, int state) {
		if (canReachedActivityInstanceIds == null
				|| canReachedActivityInstanceIds.size() == 0) {
			return new ArrayList<ActivityInstanceDto>();
		}
		String where = "WHERE STATE=? AND ACTIVITYINSTANCEID IN (";
		StringBuffer sb = new StringBuffer();
		for (String v : canReachedActivityInstanceIds) {
			sb.append(v).append(",");
		}
		where+=sb.toString().subSequence(0, sb.toString().length() - 2);
		where+=") ORDER BY STARTEDDATE,ACTIVITYINSTANCEID";
//		return queryList(
//				ActivityInstanceDto.class,
//				QUERY_ACTIVITY_INSTANCE + where,
//				new Object[] {state});
		List<ActivityInstanceDto> list = new ArrayList<ActivityInstanceDto>();
		List<Map<String,Object>> ret = JDBCHelper.getJdbcTemplate().queryForList(QUERY_ACTIVITY_INSTANCE + where,new Object[]{state});
		if(ret != null && ret.size()>0){
			for(Map<String,Object> map:ret){
				ActivityInstanceDto dto = new ActivityInstanceDto();
				dto.setProcessInstanceId(LongHelper.valueOf(map.get("PROCESSINSTANCEID")));
				dto.setActivityDefinitionId(StringHelper.valueOf(map.get("ACTIVITYDEFINITIONID")));
				dto.setId(LongHelper.valueOf(map.get("ACTIVITYINSTANCEID")));
				dto.setName(StringHelper.valueOf(map.get("NAME")));
				dto.setState(IntegerHelper.valueOf(map.get("STATE")));
				dto.setReverse(LongHelper.valueOf(map.get("REVERSE")));
				dto.setStartedDate(StringHelper.valueOf(map.get("STARTEDDATE")) == null ? null:Timestamp.valueOf(StringHelper.valueOf(map.get("STARTEDDATE"))));
				dto.setDueDate(StringHelper.valueOf(map.get("DUEDATE")) == null? null:Timestamp.valueOf(StringHelper.valueOf(map.get("DUEDATE"))));
				dto.setCompletedDate(StringHelper.valueOf(map.get("COMPLETEDDATE")) == null? null:Timestamp.valueOf(StringHelper.valueOf(map.get("COMPLETEDDATE"))));
				dto.setPriority(IntegerHelper.valueOf(map.get("PRIORITY")));
				dto.setItemCompleted(IntegerHelper.valueOf(map.get("ITEMCOMPLETED")));
				dto.setItemSum(IntegerHelper.valueOf(map.get("ITEMSUM")));
				dto.setTacheId(LongHelper.valueOf(map.get("TACHE_ID")));
				dto.setSynMessage(LongHelper.valueOf(map.get("SYN_MESSAGE")));
				dto.setDirection(StringHelper.valueOf(map.get("DIRECTION")));
				dto.setAtomActivityInstanceId(LongHelper.valueOf(map.get("ATOM_ACTIVITYINSTANCE_ID")));
				dto.setActivityDefinitionId(StringHelper.valueOf(map.get("ATOM_ACTIVITYDEFINITIONID")));
				dto.setAreaId(StringHelper.valueOf(map.get("AREA_ID")));
				dto.setRollbackTranins(StringHelper.valueOf(map.get("ROLLBACK_TRANINS")));
				list.add(dto);
			}
		}
		return list;
	}

	@Override
	public String queryReverseActivity(Long id) {
		return queryForString(
				"SELECT REVERSE FROM  UOS_ACTIVITYINSTANCE WHERE ACTIVITYINSTANCEID=? ",
				new Object[] { id });
	}

	@Override
	public String queryWrokItemId(Long id) {
		return queryForString(
				"SELECT WORKITEMID FROM  UOS_ACTIVITYINSTANCE WHERE ACTIVITYINSTANCEID=?",
				new Object[] { id });
	}

	@Override
	public List<ActivityInstanceDto> queryActivityInstanceByPid(
			Long processInstanceId) {
		return queryActivityInstancesByState(processInstanceId.toString(),
				null, null, null);
	}

	private static final String QUERY_ACTIVITYINSTANCE = "SELECT PROCESSINSTANCEID,ACTIVITYDEFINITIONID,"
			+ "ACTIVITYINSTANCEID,NAME,STATE,STARTEDDATE,COMPLETEDDATE,DUEDATE,"
			+ "PRIORITY,ITEMCOMPLETED,ITEMSUM,TACHE_ID,SYN_MESSAGE,DIRECTION,ATOM_ACTIVITYINSTANCE_ID," 
			+ "ATOM_ACTIVITYDEFINITIONID,AREA_ID,ROLLBACK_TRANINS,REVERSE,WORKITEMID "
			+ " FROM UOS_ACTIVITYINSTANCE"
			+ " WHERE PROCESSINSTANCEID=? ORDER BY STARTEDDATE DESC,ACTIVITYINSTANCEID DESC";

	@Override
	public ActivityInstanceDto qryCurrentActivityByProcInstId(
			String processInstanceId) {
		return queryObject(ActivityInstanceDto.class, QUERY_ACTIVITYINSTANCE,
				processInstanceId);
	}

	@Override
	public void deleteByPid(String processInstanceId) {
		String sql = "DELETE FROM UOS_ACTIVITYINSTANCE WHERE PROCESSINSTANCEID=?";
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
