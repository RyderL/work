package com.zterc.uos.fastflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.process.ActivityInstanceDAO;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.state.WMActivityInstanceState;

/**
 * （实例）活动实例操作类
 * 
 * @author gong.yi
 *
 */
public class ActivityInstanceService {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityInstanceService.class);

	private ActivityInstanceDAO activityInstanceDAO;

	public void setActivityInstanceDAO(ActivityInstanceDAO activityInstanceDAO) {
		this.activityInstanceDAO = activityInstanceDAO;
	}

	/**
	 * （Server）根据活动实例id查询活动实例
	 * 
	 * @param activityInstanceId
	 * @return ActivityInstanceDto
	 */
	public ActivityInstanceDto queryActivityInstance(String activityInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		ActivityInstanceDto activityInstance = new ActivityInstanceDto();
		if (processModel != null) {
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			for (int i = 0; i < actList.size(); i++) {
				ActivityInstanceDto dto = actList.get(i);
				if (dto != null
						&& dto.getId().longValue() == LongHelper
								.valueOf(activityInstanceId)) {
					return dto;
				}
			}
			//如果内存模型中没有找到对应的活动实例，则从数据库中查找，如果找到了将活动实例回填到内存模型中
			activityInstance = activityInstanceDAO.queryActivityInstance(activityInstanceId);
			if(activityInstance != null){
				activityInstance.setOperType(OperType.DEFAULT);
				processModel.getActivityInstanceDtos().add(activityInstance);
				return activityInstance;
			}
		}
		// 内存模式操作----end-------------

		return activityInstanceDAO.queryActivityInstance(activityInstanceId);
	}

	/**
	 * （Server）创建活动实例
	 * 
	 * @param activity
	 * @param processInstance
	 * @param direction
	 * @param useDB
	 * @return 
	 */
	public ActivityInstanceDto createActivityInstance(Activity activity,
			ProcessInstanceDto processInstance, String direction, boolean useDB) {
		ActivityInstanceDto activityInstance = new ActivityInstanceDto();
		activityInstance.setProcessInstanceId(processInstance
				.getProcessInstanceId());
		activityInstance.setActivityDefinitionId(activity.getId());
		activityInstance.setName(activity.getName());
		if (activity.isStartActivity()&&direction.equals(ActivityInstanceDto.NORMAL_DIRECTION)) {
			activityInstance
					.setState(WMActivityInstanceState.CLOSED_COMPLETED_INT);
			activityInstance.setCompletedDate(DateHelper.getTimeStamp());
			activityInstance.setItemCompleted(1);
		} else {
			activityInstance
					.setState(WMActivityInstanceState.OPEN_INITIATED_INT);
			activityInstance.setStartedDate(DateHelper.getTimeStamp());
			activityInstance.setItemCompleted(0);
		}
		activityInstance.setItemSum(1); // mod 2005-12-12 (一个活动实例仅对应一个工作项)

		activityInstance.setAreaId(processInstance.getAreaId());// add by che.zi
		activityInstance.setPriority(processInstance.getPriority());
		Long tacheId = activity.getTacheId() == null ? null : Long
				.valueOf(activity.getTacheId());
		activityInstance.setTacheId(tacheId);
		activityInstance.setDirection(direction);

		createActivityInstance(activityInstance, useDB);

		return activityInstance;
	}

	/**
	 * （Server）创建活动实例
	 * 
	 * @param activityInstance
	 * @param useDB
	 * @return 
	 */
	public ActivityInstanceDto createActivityInstance(
			ActivityInstanceDto activityInstance, boolean useDB) {
		if(activityInstance.getId()==null){
			activityInstance.setId(SequenceHelper.getIdWithSeed("UOS_ACTIVITYINSTANCE"
					,activityInstance.getProcessInstanceId()==null?"0":activityInstance.getProcessInstanceId().toString()));
		}
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				activityInstanceDAO.createActivityInstance(activityInstance);
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					activityInstance.setOperType(OperType.getOperType(
							activityInstance.getOperType(), OperType.INSERT));
					processModel.getActivityInstanceDtos()
							.add(activityInstance);
				}
				// 内存模式操作----end-------------
			}
		} else {
			activityInstanceDAO.createActivityInstance(activityInstance);
		}

		return activityInstance;
	}

	/**
	 * （Server）更新活动实例
	 * 
	 * @param activityInstance
	 * @param useDB
	 */
	public void updateActivityInstance(ActivityInstanceDto activityInstance,
			boolean useDB) {
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				activityInstanceDAO.updateActivityInstance(activityInstance);
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					List<ActivityInstanceDto> actList = processModel
							.getActivityInstanceDtos();
					for (int i = 0; i < actList.size(); i++) {
						ActivityInstanceDto dto = actList.get(i);
						if (dto != null&& dto.getId().longValue() == activityInstance.getId()) {
							activityInstance.setOperType(OperType.getOperType
									(activityInstance.getOperType(), OperType.UPDATE));
							actList.set(i, activityInstance);
							break;
						}
					}
				}
				// 内存模式操作----end-------------
			}
		} else {
			activityInstanceDAO.updateActivityInstance(activityInstance);
		}
	}

	/**
	 * （Server）根据流程实例id、活动定义id，状态以及方向查询活动实例列表
	 * 
	 * @param processInstanceId
	 * @param activityId
	 * @param state
	 * @param direction
	 * @return
	 */
	public List<ActivityInstanceDto> queryActivityInstancesByState(
			String processInstanceId, String activityId, String state,
			String direction) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			List<ActivityInstanceDto> retActivityInstanceDtos = new ArrayList<ActivityInstanceDto>();
			logger.info(actList.size()+"=======================");
			for(ActivityInstanceDto act:actList){
				logger.info(act.getState()+"===>"+act.getId());
			}
			for (int i = 0; i < actList.size(); i++) {
				ActivityInstanceDto dto = actList.get(i);
				if(activityId!=null){
					if (dto.getActivityDefinitionId().equals(activityId)
							&& state.indexOf(dto.getState() + "") != -1) {
						if (direction != null) {
							if (dto.getDirection().equals(direction)) {
								retActivityInstanceDtos.add(dto);
							}
						} else {
							retActivityInstanceDtos.add(dto);
						}
					}
				}else{
					if (state.indexOf(dto.getState() + "") != -1 && processInstanceId.equals(StringHelper.valueOf(dto.getProcessInstanceId()))) {
						if (direction != null) {
							if (dto.getDirection().equals(direction)) {
								retActivityInstanceDtos.add(dto);
							}
						} else {
//							logger.info(dto.getState()+"===>"+dto.getId());
							retActivityInstanceDtos.add(dto);
						}
					}
				}
			}
			return retActivityInstanceDtos;
		}
		// 内存模式操作----end-------------

		return activityInstanceDAO.queryActivityInstancesByState(
				processInstanceId, activityId, state, direction);
	}
	
	/**
	 * （Server）根据流程实例id、活动定义id，状态查询活动实例列表
	 * 
	 * @param processInstanceId
	 * @param activityId
	 * @param state
	 * @param direction
	 * @return
	 */
	public ActivityInstanceDto queryActivityInstancesByStateDesc(
			String processInstanceId, String activityId, String state) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			logger.info(actList.size()+"=======================");
			for(ActivityInstanceDto act:actList){
				logger.info(act.getState()+"===>"+act.getId());
			}
			for (int i =  actList.size()-1; i > -1; i--) {
				ActivityInstanceDto dto = actList.get(i);
				if(activityId!=null){
					if (dto.getActivityDefinitionId().equals(activityId)
							&& state.indexOf(dto.getState() + "") != -1) {
						return dto;
					}
				}else{
					if (state.indexOf(dto.getState() + "") != -1) {
						return dto;
					}
				}
			}
		}
		// 内存模式操作----end-------------

		return activityInstanceDAO.queryActivityInstancesByStateDesc(processInstanceId, activityId, state);
	}

	/**
	 * （Server）根据活动实例id查询其反向的活动实例id
	 * 
	 * @param activityInstanceId
	 * @return
	 */
	public String queryReverseActivity(Long activityInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			for (int i = 0; i < actList.size(); i++) {
				ActivityInstanceDto dto = actList.get(i);
				if (dto != null && dto.getId().longValue() == activityInstanceId) {
					return StringHelper.valueOf(dto.getReverse());
				}
			}
		}
		// 内存模式操作----end-------------

		return activityInstanceDAO.queryReverseActivity(activityInstanceId);
	}
	
	/**
	 * (Server)
	 * 
	 * @param startActivityInstanceId
	 * @param processInstanceId
	 * @return
	 */
	public List<ActivityInstanceDto> queryActivityInstancesByFrom(
			Long startActivityInstanceId, String processInstanceId) {
		List<ActivityInstanceDto> activityInstanceList = new ArrayList<ActivityInstanceDto>();
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<TransitionInstanceDto> transList = processModel.getTransitionInstanceDtos();
			for(TransitionInstanceDto trans:transList){
				Long fromActId = trans.getFromActivityInstanceId();
				if(fromActId.longValue() == startActivityInstanceId.longValue()){
					Long toActId = trans.getToActivityInstanceId();
					if(StringHelper.valueOf(toActId) != null){
						ActivityInstanceDto toAct = this.queryActivityInstance(StringHelper.valueOf(toActId));
						activityInstanceList.add(toAct);
					}
				}
			}
			if(activityInstanceList.size()>0){
				return activityInstanceList;
			}
		}
		// 内存模式操作----end-------------
		return activityInstanceDAO.queryActivityInstancesByFrom(startActivityInstanceId,processInstanceId);
	}

	/**
	 * 根据活动实例id查询其对应的工作项id
	 * 
	 * @param activityInstanceId
	 * @return
	 */
	public String queryWrokItemId(Long activityInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			for (int i = 0; i < actList.size(); i++) {
				ActivityInstanceDto dto = actList.get(i);
				if (dto != null && dto.getId().longValue() == activityInstanceId) {
					return dto.getWorkItemId().toString();
				}
			}
		}
		// 内存模式操作----end-------------

		return activityInstanceDAO.queryWrokItemId(activityInstanceId);
	}
	
	/**
	 * （Server）根据活动实例id集合和状态查询活动实例列表
	 * 
	 * @param activityInstanceIds
	 * @param state
	 * @return
	 */
	public List<ActivityInstanceDto> findActivityInstances(
			Set<String> activityInstanceIds, int state) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			List<ActivityInstanceDto> retActivityInstanceDtos = new ArrayList<ActivityInstanceDto>();
			for (int i = 0; i < actList.size(); i++) {
				ActivityInstanceDto dto = actList.get(i);
				if (dto != null && activityInstanceIds.contains(dto.getId().toString())
						&& dto.getState() == state) {
					retActivityInstanceDtos.add(dto);
				}
			}
			return retActivityInstanceDtos;
		}
		// 内存模式操作----end-------------

		return activityInstanceDAO.findActivityInstances(activityInstanceIds,state);
	}
	
	/**
	 * （Manager）根据流程实例id查询活动实例id列表
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public List<ActivityInstanceDto> queryActivityInstanceByPid(
			Long processInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			return actList;
		}
		// 内存模式操作----end-------------
		return activityInstanceDAO
				.queryActivityInstanceByPid(processInstanceId);
	}
	
	/**
	 * (Manager)根据流程实例id查询当前的活动实例
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public ActivityInstanceDto qryCurrentActivityByProcInstId(String processInstanceId){
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			return actList.get(actList.size()-1);
		}
		// 内存模式操作----end-------------
		return activityInstanceDAO.qryCurrentActivityByProcInstId(processInstanceId);
	}

	/**
	 * 根据活动定义id查询活动实例（仅适用内存模式）
	 * @param activityId
	 * @return
	 */
	public ActivityInstanceDto queryActivityInstanceByActivityId(String activityId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			for (int i = 0; i < actList.size(); i++) {
				ActivityInstanceDto dto = actList.get(i);
				if (dto != null && dto.getActivityDefinitionId().equals(activityId)) {
					return dto;
				}
			}
		}
		return null;
	}

	/**
	 * 根据流程实例id和活动id查询活动实例信息
	 * @param processInstanceId
	 * @param id
	 * @return
	 */
	public List<ActivityInstanceDto> qryActivityInstances(String processInstanceId, String activityId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ActivityInstanceDto> retActList = new ArrayList<ActivityInstanceDto>();
			List<ActivityInstanceDto> actList = processModel
					.getActivityInstanceDtos();
			logger.info(actList.size() + "=======================");
			for (int i = actList.size() - 1; i > -1; i--) {
				ActivityInstanceDto dto = actList.get(i);
				if (activityId != null) {
					if (dto.getActivityDefinitionId().equals(activityId)) {
						retActList.add(dto);
					}
				} 
			}
			return retActList;
		}
		// 内存模式操作----end-------------

		return activityInstanceDAO.queryActivityInstancesByState(processInstanceId, activityId, null, null);
	}

	public void deleteByPid(String processInstanceId) {
		activityInstanceDAO.deleteByPid(processInstanceId);
	}
}
