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
	 * ��ѯ����ʵ����ǰ����
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
	 * ��������ʵ�����ҿ�ʼ�ڵ�
	 * @param processInstanceId ����ʵ��id
	 * @return	��ʼ�ڵ�ActivityInstanceDto������Ҳ�������Ϊ��
	 * @author  zhong.kaijie  on 2017/5/3 05:12
	 * @version 1.0.0
	 */
	ActivityInstanceDto getStartActivityInstance(Long processInstanceId);
}
