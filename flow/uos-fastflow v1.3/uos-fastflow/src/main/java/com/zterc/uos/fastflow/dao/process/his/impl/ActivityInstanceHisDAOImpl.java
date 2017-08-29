package com.zterc.uos.fastflow.dao.process.his.impl;

import org.springframework.stereotype.Repository;

import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.process.his.ActivityInstanceHisDAO;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;

@Repository("activityInstanceHisDAO")
public class ActivityInstanceHisDAOImpl extends AbstractDAOImpl implements
		ActivityInstanceHisDAO {
	protected static final String INSERT_ACTIVITY_INSTANCE_HIS = "INSERT INTO UOS_ACTIVITYINSTANCE_HIS(PROCESSINSTANCEID,ACTIVITYDEFINITIONID,"
			+ "ACTIVITYINSTANCEID,NAME,STATE,STARTEDDATE,COMPLETEDDATE,"
			+ "DUEDATE,PRIORITY,ITEMCOMPLETED,ITEMSUM,TACHE_ID,SYN_MESSAGE,DIRECTION,AREA_ID,ROLLBACK_TRANINS,REVERSE,WORKITEMID) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	protected static final String QUERY_ACTIVITY_INSTANCE_HIS = "SELECT PROCESSINSTANCEID,ACTIVITYDEFINITIONID,"
			+ "ACTIVITYINSTANCEID,NAME,STATE,STARTEDDATE,COMPLETEDDATE,DUEDATE,"
			+ "PRIORITY,ITEMCOMPLETED,ITEMSUM,TACHE_ID,SYN_MESSAGE,DIRECTION,ATOM_ACTIVITYINSTANCE_ID," 
			+ "ATOM_ACTIVITYDEFINITIONID,AREA_ID,ROLLBACK_TRANINS,REVERSE,WORKITEMID "
			+ "FROM UOS_ACTIVITYINSTANCE_HIS ";

	@Override
	public ActivityInstanceDto createActivityInstanceHis(
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
		saveOrUpdate(buildMap(INSERT_ACTIVITY_INSTANCE_HIS, args));

		return activityInstance;
	}

	@Override
	public ActivityInstanceDto queryActivityInstanceHis(
			String activityInstanceId) {
		String where = " WHERE ACTIVITYINSTANCEID = ?";
		return queryObject(ActivityInstanceDto.class, QUERY_ACTIVITY_INSTANCE_HIS
				+ where, activityInstanceId);
	}

}
