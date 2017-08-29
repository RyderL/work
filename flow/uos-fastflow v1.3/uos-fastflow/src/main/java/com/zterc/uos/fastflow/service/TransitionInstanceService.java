package com.zterc.uos.fastflow.service;

import java.util.ArrayList;
import java.util.List;

import com.zterc.uos.fastflow.dao.process.ActivityInstanceDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.process.TransitionInstanceDAO;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.TracerInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.zterc.uos.fastflow.model.Transition;
import com.zterc.uos.fastflow.state.WMTransitionInstanceAction;
import com.zterc.uos.fastflow.state.WMTransitionInstanceState;

/**
 * （实例）转移实例操作类
 * 
 * @author gong.yi
 *
 */
public class TransitionInstanceService {
	private static Logger logger = Logger.getLogger(TransitionInstanceService.class);

	private TransitionInstanceDAO transitionInstanceDAO;

	private ActivityInstanceDAO activityInstanceDAO;

	public void setTransitionInstanceDAO(
			TransitionInstanceDAO transitionInstanceDAO) {
		this.transitionInstanceDAO = transitionInstanceDAO;
	}

	/**
	 * （Server）创建转移实例
	 * 
	 * @param processInstance
	 * @param toActivityInstance
	 * @param fromActivityInstance
	 * @param currentTransition
	 * @param direction
	 * @param useDB
	 * @return
	 */
	public TransitionInstanceDto createTransitionInstance(
			ProcessInstanceDto processInstance,
			ActivityInstanceDto toActivityInstance,
			ActivityInstanceDto fromActivityInstance,
			Transition currentTransition, String direction, boolean useDB) {
		TransitionInstanceDto transitionInstanceDto = new TransitionInstanceDto();
		transitionInstanceDto
				.setAction(WMTransitionInstanceAction.ACTION_COMPLETE
						.intValue());
		transitionInstanceDto.setFromActivityInstanceId(fromActivityInstance
				.getId());
		transitionInstanceDto.setLastDate(DateHelper.getTimeStamp());
		transitionInstanceDto.setProcessInstanceId(processInstance
				.getProcessInstanceId());
		transitionInstanceDto.setState(WMTransitionInstanceState.ENABLED_INT);
		transitionInstanceDto.setToActivityInstanceId(toActivityInstance
				.getId());
		String transId = currentTransition == null ? null : currentTransition
				.getId();
		transitionInstanceDto.setTransitionDefinitionId(transId);
		String transName = currentTransition == null ? "跳转" : currentTransition
				.getName();
		transitionInstanceDto.setTransitionDefinitionName(transName);
		transitionInstanceDto.setDirection(direction);
		transitionInstanceDto.setAreaId(processInstance.getAreaId());
		return createTransitionInstance(transitionInstanceDto, useDB);
	}

	/**
	 * （Server）根据toActivityId和流程实例id查找转移实例列表
	 * 
	 * @param processInstanceId
	 * @param toActivityId
	 * @return
	 */
	public List<TransitionInstanceDto> findTransitionInstancesByToActivity(
			String processInstanceId, Long toActivityId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<TransitionInstanceDto> transitionInstanceDtos = processModel
					.getTransitionInstanceDtos();
			List<TransitionInstanceDto> retTransitionInstanceDtos = new ArrayList<TransitionInstanceDto>();
			for (int i = 0; i < transitionInstanceDtos.size(); i++) {
				TransitionInstanceDto dto = transitionInstanceDtos.get(i);
				if (dto != null
						&& dto.getToActivityInstanceId().longValue() == toActivityId) {
					retTransitionInstanceDtos.add(dto);
				}
			}
			return retTransitionInstanceDtos;
		}
		// 内存模式操作----end-------------

		return transitionInstanceDAO.findTransitionInstancesByToActivity(
				processInstanceId, toActivityId);
	}
	
	/**
	 * （Server）根据fromActivityId和流程实例id查找转移实例列表
	 * 
	 * @param processInstanceId
	 * @param fromActivityId
	 * @return
	 */
	public List<TransitionInstanceDto> findTransitionInstancesByFromActivity(
			String processInstanceId, Long fromActivityId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<TransitionInstanceDto> transitionInstanceDtos = processModel
					.getTransitionInstanceDtos();
			List<TransitionInstanceDto> retTransitionInstanceDtos = new ArrayList<TransitionInstanceDto>();
			for (int i = 0; i < transitionInstanceDtos.size(); i++) {
				TransitionInstanceDto dto = transitionInstanceDtos.get(i);
				if (dto != null
						&& dto.getFromActivityInstanceId().longValue() == fromActivityId) {
					retTransitionInstanceDtos.add(dto);
				}
			}
			return retTransitionInstanceDtos;
		}
		// 内存模式操作----end-------------

		return transitionInstanceDAO.findTransitionInstancesByFromActivity(
				processInstanceId, fromActivityId);
	}
	
	/**
	 * （Server）根据fromActivityId、toActivityId和流程实例id查找转移实例列表
	 * 
	 * @param processInstanceId
	 * @param fromActivityId
	 * @param toActivityId
	 * @return
	 */
	public List<TransitionInstanceDto> findTransitionInstancesByFromAndTo(
			String processInstanceId, Long fromActivityId, Long toActivityId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<TransitionInstanceDto> transitionInstanceDtos = processModel
					.getTransitionInstanceDtos();
			List<TransitionInstanceDto> retTransitionInstanceDtos = new ArrayList<TransitionInstanceDto>();
			for (int i = 0; i < transitionInstanceDtos.size(); i++) {
				TransitionInstanceDto dto = transitionInstanceDtos.get(i);
				if (dto != null
						&& dto.getFromActivityInstanceId().longValue() == fromActivityId
						&& dto.getToActivityInstanceId().longValue() == toActivityId) {
					retTransitionInstanceDtos.add(dto);
				}
			}
			return retTransitionInstanceDtos;
		}
		// 内存模式操作----end-------------

		return transitionInstanceDAO.findTransitionInstancesByFromAndTo(
				processInstanceId, fromActivityId,toActivityId);
	}
	
	/**
	 * （Server）根据流程实例id和方向查找转移实例列表
	 * 
	 * @param processInstanceId
	 * @param direction
	 * @return
	 */
	public List<TransitionInstanceDto> findTransitionInstances(
			String processInstanceId, String direction) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<TransitionInstanceDto> transitionInstanceDtos = processModel
					.getTransitionInstanceDtos();
			List<TransitionInstanceDto> retTransitionInstanceDtos = new ArrayList<TransitionInstanceDto>();
			for (int i = 0; i < transitionInstanceDtos.size(); i++) {
				TransitionInstanceDto dto = transitionInstanceDtos.get(i);
				if (dto != null
						&& dto.getState() == 0
						&& dto.getDirection().equals(direction)) {
					retTransitionInstanceDtos.add(dto);
				}
			}
			return retTransitionInstanceDtos;
		}
		// 内存模式操作----end-------------
		List<TransitionInstanceDto> result=transitionInstanceDAO.findTransitionInstances(processInstanceId, direction);
		result=reSortTransitionInstances(result);
		return result;
	}



	/**
	 * 重新给TransitionInstanceDto排序，以确保不受时间、id大小的影响；
	 * 解决退单 TransitionInstanceDto顺序不对，导致找不到可退单活动实例的问题
	 * @param transitionInstanceDtos  原transitionInstance集合
	 * @return	修正排序后的transitionInstance集合
	 * @author  zhong.kaijie  on 2017/5/2 03:30
	 * @version 1.0.0
	 */
	public  List<TransitionInstanceDto> reSortTransitionInstances(List<TransitionInstanceDto> transitionInstanceDtos){
		return reSortTransitionInstances(transitionInstanceDtos,null);
	}
	/**
	 * 重新给TransitionInstanceDto排序，以确保不受时间、id大小的影响；
	 * 解决退单 TransitionInstanceDto顺序不对，导致找不到可退单活动实例的问题
	 * @param transitionInstanceDtos  原transitionInstance集合
	 * @return	修正排序后的transitionInstance集合
	 * @author  zhong.kaijie  on 2017/5/2 03:30
	 * @version 1.0.0
	 */
	public  List<TransitionInstanceDto> reSortTransitionInstances(List<TransitionInstanceDto> transitionInstanceDtos,Long startActivityId){
		List<TransitionInstanceDto> result=null;
		List<Long> toActivities=null;
		if (CollectionUtils.isNotEmpty(transitionInstanceDtos)){
			TransitionInstanceDto  firstTI= transitionInstanceDtos.get(0);
			Long processInstId=firstTI.getProcessInstanceId();
			if (processInstId!=null&&processInstId>0&&(startActivityId==null||startActivityId==0)){
				//查找开始节点的ActivityInstanceId
				ActivityInstanceDto  startActivityInstance= activityInstanceDAO.getStartActivityInstance(processInstId);
				if (logger.isInfoEnabled()&&startActivityInstance!=null)
					logger.info("找到开始节点:"+startActivityInstance.getId());
				if (startActivityInstance!=null){
					startActivityId=startActivityInstance.getId();
				}
			}
			if(startActivityId!=null&&startActivityId.longValue()!=firstTI.getFromActivityInstanceId().longValue()){
				//如果传入的startActivityId不为空，且第一条线条记录的FromActivityInstanceId不等于startActivityId
				//表示第一条记录不是开始节点，需要重新查找开始节点
				for (int i=0;i<transitionInstanceDtos.size();i++){
					if (startActivityId.longValue()==transitionInstanceDtos.get(i).getFromActivityInstanceId().longValue()){
						firstTI=transitionInstanceDtos.get(i);
						logger.info("找到开始线条节点:"+firstTI.getId());
						break;
					}
				}
			}
			result=new ArrayList<>(transitionInstanceDtos.size());
			toActivities=new ArrayList<>(transitionInstanceDtos.size());
			//第一个务必是开始节点
			result.add(firstTI);
			toActivities.add(firstTI.getFromActivityInstanceId());
			toActivities.add(firstTI.getToActivityInstanceId());
			int maxReSortCount=transitionInstanceDtos.size();
			while (transitionInstanceDtos.size()!=result.size()&&maxReSortCount>0){
				for (int i=0;i<transitionInstanceDtos.size();i++){
					TransitionInstanceDto transitionInstanceDto=transitionInstanceDtos.get(i);
					if (result.contains(transitionInstanceDto)){
						continue;
					}
					if (toActivities.contains(transitionInstanceDto.getFromActivityInstanceId())){
						//表示result集合中已经正确找到上一条transitionInstance;
						result.add(transitionInstanceDto);
						toActivities.add(transitionInstanceDto.getToActivityInstanceId());
					}else{
						//表示result集合没有正确找到上一条transitionInstance

					}
				}
				maxReSortCount--;
			}
			if (result.size()!=transitionInstanceDtos.size()){
				logger.error("ProcessInstanceId="+firstTI.getProcessInstanceId()+"的transitionInstanceDtos存在问题，有"+
						(transitionInstanceDtos.size()-result.size())+"条记录无法正确找到上一条记录，请查验数据!!");
			}
		}else{
			result=new ArrayList<>(0);
		}
		return result;
	}



	/**
	 * （Server）根据条件查找转移实例的数量
	 * 
	 * @param processInstanceId
	 * @param fromActivityInstanceId
	 * @param toActivityInstanceId
	 * @param direction
	 * @return
	 */
	public int countTransitionInstance(String processInstanceId,
			String fromActivityInstanceId, String toActivityInstanceId, String direction) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<TransitionInstanceDto> transitionInstanceDtos = processModel
					.getTransitionInstanceDtos();
			int count = 0;
			logger.debug("----transitionInstanceDtos.size:"+transitionInstanceDtos.size());
			for (int i = 0; i < transitionInstanceDtos.size(); i++) {
				TransitionInstanceDto dto = transitionInstanceDtos.get(i);
//				logger.debug("----dto.getDirection:"+dto.getDirection()+"--dto.getFromActivityInstanceId:"+dto.getFromActivityInstanceId()+",--dto.getToActivityInstanceId:"+dto.getToActivityInstanceId());
				if (dto != null&& dto.getState() == 0
						&& dto.getDirection().equals(direction)) {
					if(fromActivityInstanceId!=null
							&&toActivityInstanceId!=null){
						if(fromActivityInstanceId.equals(StringHelper.valueOf(dto.getFromActivityInstanceId()))
								 && toActivityInstanceId.equals(StringHelper.valueOf(dto.getToActivityInstanceId()))){
							count++;
						}
					}else if(toActivityInstanceId==null&&fromActivityInstanceId!=null&&fromActivityInstanceId.equals(StringHelper.valueOf(dto.getFromActivityInstanceId()))){
						count++;
					}else if(fromActivityInstanceId==null&&toActivityInstanceId!=null&&toActivityInstanceId.equals(StringHelper.valueOf(dto.getToActivityInstanceId()))){
						count++;
					}
				}
			}
			return count;
		}
		// 内存模式操作----end-------------
	
		return transitionInstanceDAO.countTransitionInstance(processInstanceId, fromActivityInstanceId, toActivityInstanceId, direction);
	}

	/**
	 * （Server）更新转移实例
	 * 
	 * @param transition
	 * @param useDB
	 */
	public void updateTransitionInstance(TransitionInstanceDto transition,
			boolean useDB) {
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				transitionInstanceDAO.updateTransitionInstance(transition);
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					List<TransitionInstanceDto> transitionInstanceDtos = processModel
							.getTransitionInstanceDtos();
					for (int i = 0; i < transitionInstanceDtos.size(); i++) {
						TransitionInstanceDto dto = transitionInstanceDtos
								.get(i);
						if (dto != null
								&& dto.getId().longValue() == transition
										.getId()) {
							transition.setOperType(OperType.getOperType(
									transition.getOperType(), OperType.UPDATE));
							break;
						}
					}
				}
				// 内存模式操作----end-------------
			}
		} else {
			transitionInstanceDAO.updateTransitionInstance(transition);
		}
	}

	/**
	 * （Server）创建转移实例
	 * 
	 * @param transition
	 * @param useDB
	 * @return
	 */
	public TransitionInstanceDto createTransitionInstance(
			TransitionInstanceDto transition, boolean useDB) {
		if (transition.getId() == null) {
			transition.setId(SequenceHelper.getIdWithSeed("UOS_TRANSITIONINSTANCE"
					,transition.getProcessInstanceId()==null?"0":transition.getProcessInstanceId().toString()));
		}

		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				transitionInstanceDAO.createTransitionInstance(transition);
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					transition.setOperType(OperType.getOperType(
							transition.getOperType(), OperType.INSERT));
					processModel.getTransitionInstanceDtos().add(transition);
				}
				// 内存模式操作----end-------------
			}
		} else {
			transitionInstanceDAO.createTransitionInstance(transition);
		}

		return transition;
	}

	// ===============TODO=======待优化==========begin===============
	/**
	 * （是否需要内存模式获取？？？？）流程实例xml查找和组装
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public TracerInstanceDto findTransitionInstancesByPid(
			String processInstanceId) {
		return transitionInstanceDAO
				.findTransitionInstancesByPid(processInstanceId);
	}
	// ===============TODO=======================end=========

	public static void main(String[] args) {
		List<String> t = new ArrayList<String>();
		t.add("1");
		t.add("2");
		t.add("3");
		t.add(1, "4");
		t.remove(1);
		System.out.println(t);
	}

	public List<TransitionInstanceDto> findTransitionInstances(
			String processInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<TransitionInstanceDto> transitionInstanceDtos = processModel
					.getTransitionInstanceDtos();
			return transitionInstanceDtos;
		}
		// 内存模式操作----end-------------
		return transitionInstanceDAO.findTransitionInstances(processInstanceId);
	}

	public void deleteByPid(String processInstanceId) {
		transitionInstanceDAO.deleteByPid(processInstanceId);
	}

	public void setActivityInstanceDAO(ActivityInstanceDAO activityInstanceDAO) {
		this.activityInstanceDAO = activityInstanceDAO;
	}

	public ActivityInstanceDAO getActivityInstanceDAO() {
		return activityInstanceDAO;
	}
}
