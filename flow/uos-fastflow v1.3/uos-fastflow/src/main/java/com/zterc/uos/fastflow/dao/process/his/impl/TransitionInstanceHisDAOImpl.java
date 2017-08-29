package com.zterc.uos.fastflow.dao.process.his.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dao.process.his.TransitionInstanceHisDAO;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.TracerDto;
import com.zterc.uos.fastflow.dto.process.TracerInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;
import com.zterc.uos.fastflow.state.WMActivityInstanceState;

@Repository("transitionInstanceHisDAO")
public class TransitionInstanceHisDAOImpl extends AbstractDAOImpl implements
		TransitionInstanceHisDAO {

	protected static final String INSERT_TRANSITION_INSTANCE_HIS = "INSERT INTO UOS_TRANSITIONINSTANCE_HIS(TRANSITIONINSTANCEID,PROCESSINSTANCEID,"
			+ "TRANSITIONDEFINITIONID,TRANSITIONDEFINITIONNAME,FROMACTIVITYINSTANCEID,"
			+ "TOACTIVITYINSTANCEID,LASTDATE,STATE,ACTION,PARTICIPANTID,MEMO,DIRECTION,AREA_ID) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	@Override
	public TransitionInstanceDto createTransitionInstanceHis(
			TransitionInstanceDto transition) {
		Object[] args = new Object[] { transition.getId(),
				transition.getProcessInstanceId(),
				transition.getTransitionDefinitionId(),
				transition.getTransitionDefinitionName(),
				transition.getFromActivityInstanceId(),
				transition.getToActivityInstanceId(), transition.getLastDate(),
				transition.getState(), transition.getAction(),
				transition.getParticipant(), transition.getMemo(),
				transition.getDirection(), transition.getAreaId() };
		saveOrUpdate(buildMap(INSERT_TRANSITION_INSTANCE_HIS, args));

		return transition;
	}

	@Override
	public TracerInstanceDto findTransitionInstancesByPid(
			String processInstanceId) {
		StringBuffer sql = new StringBuffer(
				"SELECT UT.TRANSITIONINSTANCEID,UT.TRANSITIONDEFINITIONNAME,UT.TRANSITIONDEFINITIONID, ");
		sql.append("UT.FROMACTIVITYINSTANCEID,UA1.NAME FROMNAME,UA1.TACHE_ID FROMTACHEID,UA1.DIRECTION FROMDIRECT,UA1.ACTIVITYDEFINITIONID FROMID, ");
		sql.append("UT.TOACTIVITYINSTANCEID,UA2.NAME TONAME,UA2.TACHE_ID TOTACHEID,UA2.DIRECTION TODIRECT,UA2.ACTIVITYDEFINITIONID TOID,UA2.STATE TOSTATE,UA1.STATE FROMSTATE, ");
		sql.append("UT.DIRECTION TRANSDIRECT ");
		sql.append("FROM UOS_TRANSITIONINSTANCE_HIS UT,UOS_ACTIVITYINSTANCE_HIS UA1,UOS_ACTIVITYINSTANCE_HIS UA2 ");
		sql.append("WHERE UT.FROMACTIVITYINSTANCEID = UA1.ACTIVITYINSTANCEID AND ");
		sql.append("UT.TOACTIVITYINSTANCEID = UA2.ACTIVITYINSTANCEID AND UT.ACTION = 0 ");
		sql.append("AND UT.PROCESSINSTANCEID = ? ORDER BY UT.LASTDATE,UT.FROMACTIVITYINSTANCEID,UT.TRANSITIONINSTANCEID ");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, Long.parseLong(processInstanceId));
			rs = ps.executeQuery();

			Map<Long, TracerDto> map = new HashMap<Long, TracerDto>();
			Hashtable<String, TracerDto> hash = new Hashtable<String, TracerDto>(
					1); // 存放(起点活动实例ID,tracer)
			Collection<ActivityInstanceDto> activityInstancesNoInFrom = new ArrayList<ActivityInstanceDto>(); // 所有处于Running状态的终点
			Collection<TracerDto> tracers = new ArrayList<TracerDto>();
			TracerInstanceDto tracerInstance = new TracerInstanceDto();
			while (rs.next()) {
				int dbloop = 1;
				String transitionInstanceid = rs.getString(dbloop++);
				String transitionName = rs.getString(dbloop++);
				String transitionDefinitionId = rs.getString(dbloop++);
				String fromActivityInstanceid = rs.getString(dbloop++);
				String fromName = rs.getString(dbloop++);
				String fromTacheid = rs.getString(dbloop++);
				String fromDirect = rs.getString(dbloop++);
				String fromid = rs.getString(dbloop++);
				String toActivityInstanceid = rs.getString(dbloop++);
				String toName = rs.getString(dbloop++);
				String toTacheid = rs.getString(dbloop++);
				String toDirect = rs.getString(dbloop++);
				String toid = rs.getString(dbloop++);
				int tostate = rs.getInt(dbloop++);
				int fromstate = rs.getInt(dbloop++);
				String transdirect = rs.getString(dbloop++);
				TracerDto tracer = null;
				Collection<ActivityInstanceDto> toActivityInstances = null;
				Collection<TransitionInstanceDto> transitionInstances = null;
				if (hash.containsKey(fromActivityInstanceid)) {
					// 如果起点活动实例ID已存在
					tracer = (TracerDto) hash.get(fromActivityInstanceid);
					toActivityInstances = tracer.getToActivityInstances();
					transitionInstances = tracer.getTransitionInstances();
				} else {
					// 如果起点活动实例ID不存在,先取hashtable中的值到tracers中，清空hashtable,创建tracer,并写入起点信息
					// 保证hashtable中只有一个元素
					if (hash.size() == 1) {
						TracerDto tracerDto = (TracerDto) hash.values()
								.iterator().next();
						map.put(tracerDto.getFromActivityInstance().getId(),
								tracerDto);
						tracers.add(tracerDto);
						hash.clear();
					}
					tracer = new TracerDto();
					toActivityInstances = new ArrayList<ActivityInstanceDto>();
					transitionInstances = new ArrayList<TransitionInstanceDto>();
					ActivityInstanceDto fromActivityInstance = new ActivityInstanceDto();
					fromActivityInstance.setId(LongHelper
							.valueOf(fromActivityInstanceid));
					fromActivityInstance.setName(fromName);
					fromActivityInstance.setTacheId(LongHelper
							.valueOf(fromTacheid));
					fromActivityInstance.setDirection(fromDirect);
					fromActivityInstance.setActivityDefinitionId(fromid);
					fromActivityInstance.setProcessInstanceId(Long
							.valueOf(processInstanceId));
					fromActivityInstance.setActiveState(String
							.valueOf(fromstate));
					tracer.setFromActivityInstance(fromActivityInstance);
				}
				// 添加终点信息
				ActivityInstanceDto toActivityInstance = new ActivityInstanceDto();
				toActivityInstance.setId(LongHelper
						.valueOf(toActivityInstanceid));
				toActivityInstance.setName(toName);
				toActivityInstance.setTacheId(LongHelper.valueOf(toTacheid));
				toActivityInstance.setDirection(toDirect);
				toActivityInstance.setActivityDefinitionId(toid);
				toActivityInstance.setState(tostate);
				toActivityInstance.setProcessInstanceId(Long
						.valueOf(processInstanceId));
				toActivityInstance.setActiveState(String.valueOf(tostate));
				toActivityInstances.add(toActivityInstance);
				if (!activityInstancesNoInFrom.contains(toActivityInstance)
						&& WMActivityInstanceState.isOpen(toActivityInstance
								.getState())) {
					// 记录处于初始化、挂起或运行状态的活动实例
					activityInstancesNoInFrom.add(toActivityInstance);
				}
				tracer.setToActivityInstances(toActivityInstances);
				// 添加边信息
				TransitionInstanceDto transitionInstance = new TransitionInstanceDto();
				transitionInstance.setId(LongHelper
						.valueOf(transitionInstanceid));
				transitionInstance.setTransitionDefinitionName(transitionName);
				transitionInstance
						.setTransitionDefinitionId(transitionDefinitionId);
				transitionInstance.setDirection(transdirect);
				transitionInstance.setFromActivityInstanceId(LongHelper
						.valueOf(fromActivityInstanceid));
				transitionInstance.setToActivityInstanceId(LongHelper
						.valueOf(toActivityInstanceid));
				transitionInstance.setProcessInstanceId(Long
						.valueOf(processInstanceId));
				transitionInstances.add(transitionInstance);
				tracer.setTransitionInstances(transitionInstances);
				// 放入hashtable
				hash.put(fromActivityInstanceid, tracer);
			}
			if (hash.size() == 1) {
				TracerDto tracer = (TracerDto) hash.values().iterator().next();
				map.put(tracer.getFromActivityInstance().getId(), tracer);
				tracers.add(tracer);
				hash.clear();
			}
			map.clear();
			tracerInstance.setTracers(tracers);
			tracerInstance
					.setActivityInstancesNoInFrom(activityInstancesNoInFrom);
			return tracerInstance;
		} catch (SQLException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			JDBCHelper.close(rs);
			JDBCHelper.close(ps);
			JDBCHelper.close(conn);
		}
	}
}
