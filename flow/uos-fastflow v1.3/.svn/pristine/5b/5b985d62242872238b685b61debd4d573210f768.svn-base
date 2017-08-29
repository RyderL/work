package com.zterc.uos.fastflow.dao.process;

import java.util.List;

import com.zterc.uos.fastflow.dto.process.TracerInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;

public interface TransitionInstanceDAO {

	TransitionInstanceDto createTransitionInstance(
			TransitionInstanceDto transition);

	void updateTransitionInstance(TransitionInstanceDto transition);

	List<TransitionInstanceDto> findTransitionInstancesByToActivity(
			String processInstanceId, Long toActivityId);

	TracerInstanceDto findTransitionInstancesByPid(String processInstanceId);

	List<TransitionInstanceDto> findTransitionInstances(
			String processInstanceId, String direction);

	List<TransitionInstanceDto> findTransitionInstancesByFromActivity(
			String processInstanceId, Long fromActivityId);

	int countTransitionInstance(String processInstanceId,
			String fromActivityInstanceId, String toActivityInstanceId,
			String direction);

	List<TransitionInstanceDto> findTransitionInstancesByFromAndTo(
			String processInstanceId, Long fromActivityId, Long toActivityId);

	List<TransitionInstanceDto> findTransitionInstances(String processInstanceId);

	void deleteByPid(String processInstanceId);

}
