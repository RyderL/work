package com.zterc.uos.fastflow.dao.process;

import java.util.List;
import java.util.Set;

import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;

public interface ActivityInstanceDAO {

	ActivityInstanceDto queryActivityInstance(String activityInstanceId);

	ActivityInstanceDto createActivityInstance(
			ActivityInstanceDto activityInstance);

	void updateActivityInstance(ActivityInstanceDto activityInstance);

	List<ActivityInstanceDto> queryActivityInstancesByState(
			String processInstanceId, String activityId, String state,
			String direction);
	
	ActivityInstanceDto queryActivityInstancesByStateDesc(
			String processInstanceId, String activityId, String state);

	String queryReverseActivity(Long id);

	String queryWrokItemId(Long id);

	List<ActivityInstanceDto> queryActivityInstanceByPid(Long processInstanceId);

	List<ActivityInstanceDto> findActivityInstances(
			Set<String> canReachedActivityInstanceIds, int state);

	/**
	 * 查询流程实例当前环节
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public ActivityInstanceDto qryCurrentActivityByProcInstId(
			String processInstanceId);

	List<ActivityInstanceDto> queryActivityInstancesByFrom(
			Long startActivityInstanceId, String processInstanceId);

	void deleteByPid(String processInstanceId);

	/**
	 * 根据流程实例查找开始节点
	 * @param processInstanceId 流程实例id
	 * @return	开始节点ActivityInstanceDto，如果找不到，则为空
	 * @author  zhong.kaijie  on 2017/5/3 05:12
	 * @version 1.0.0
	 */
	ActivityInstanceDto getStartActivityInstance(Long processInstanceId);
}
