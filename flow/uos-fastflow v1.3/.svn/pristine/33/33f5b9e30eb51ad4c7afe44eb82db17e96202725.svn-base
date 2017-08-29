package com.zterc.uos.fastflow.holder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.holder.OperType;

public class ProcessModel implements Serializable {

	public static final String PROCESS_INSTANCE_MODEL = "FLOW-PM-";
	/**
	 * 
	 */
	private Long processInstanceId;
	private static final long serialVersionUID = 1L;
	private ProcessInstanceDto processInstanceDto;// Á÷³ÌÊµÀýDto
	private Map<String, String> paramMap = new HashMap<String, String>();
	private Map<String, String> attrMap = new HashMap<String, String>();
	private List<ActivityInstanceDto> activityInstanceDtos = new ArrayList<ActivityInstanceDto>();
	private List<TransitionInstanceDto> transitionInstanceDtos = new ArrayList<TransitionInstanceDto>();
	private List<WorkItemDto> workItemDtos = new ArrayList<WorkItemDto>();
	
	private Map<String,String> updateKeyMap = new HashMap<String,String>();

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public ProcessInstanceDto getProcessInstanceDto() {
		return processInstanceDto;
	}

	public void setProcessInstanceDto(ProcessInstanceDto processInstanceDto) {
		this.processInstanceDto = processInstanceDto;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public List<ActivityInstanceDto> getActivityInstanceDtos() {
		return activityInstanceDtos;
	}

	public void setActivityInstanceDtos(
			List<ActivityInstanceDto> activityInstanceDtos) {
		this.activityInstanceDtos = activityInstanceDtos;
	}

	public List<TransitionInstanceDto> getTransitionInstanceDtos() {
		return transitionInstanceDtos;
	}

	public void setTransitionInstanceDtos(
			List<TransitionInstanceDto> transitionInstanceDtos) {
		this.transitionInstanceDtos = transitionInstanceDtos;
	}

	public List<WorkItemDto> getWorkItemDtos() {
		return workItemDtos;
	}

	public void setWorkItemDtos(List<WorkItemDto> workItemDtos) {
		this.workItemDtos = workItemDtos;
	}

	public Map<String, String> getAttrMap() {
		return attrMap;
	}

	public void setAttrMap(Map<String, String> attrMap) {
		this.attrMap = attrMap;
	}
	
	public Map<String, String> getUpdateKeyMap() {
		return updateKeyMap;
	}

	public void setUpdateKeyMap(Map<String, String> updateKeyMap) {
		this.updateKeyMap = updateKeyMap;
	}

	public ProcessModel resetForPersist() {
		boolean isDirty = false;
		ProcessModel persistProcessModel = new ProcessModel();
		
		persistProcessModel.setProcessInstanceId(processInstanceId);

		if (OperType.isPersist(this.processInstanceDto.getOperType())) {
			isDirty = true;
			persistProcessModel.setProcessInstanceDto(processInstanceDto
					.clone());
		}
		processInstanceDto.setOperType(OperType.DEFAULT);

		persistProcessModel
				.setActivityInstanceDtos(new ArrayList<ActivityInstanceDto>());
		for (int i = 0; i < activityInstanceDtos.size(); i++) {
			ActivityInstanceDto activityInstanceDto = activityInstanceDtos
					.get(i);
			if (OperType.isPersist(activityInstanceDto.getOperType())) {
				isDirty = true;
				persistProcessModel.getActivityInstanceDtos().add(
						activityInstanceDto.clone());
			}
			activityInstanceDto.setOperType(OperType.DEFAULT);
		}

		persistProcessModel
				.setTransitionInstanceDtos(new ArrayList<TransitionInstanceDto>());
		for (int i = 0; i < transitionInstanceDtos.size(); i++) {
			TransitionInstanceDto transitionInstanceDto = transitionInstanceDtos
					.get(i);
			if (OperType.isPersist(transitionInstanceDto.getOperType())) {
				isDirty = true;
				persistProcessModel.getTransitionInstanceDtos().add(
						transitionInstanceDto.clone());
			}
			transitionInstanceDto.setOperType(OperType.DEFAULT);
		}

		persistProcessModel.setWorkItemDtos(new ArrayList<WorkItemDto>());
		for (int i = 0; i < workItemDtos.size(); i++) {
			WorkItemDto workItemDto = workItemDtos.get(i);
			if (OperType.isPersist(workItemDto.getOperType())) {
				isDirty = true;
				persistProcessModel.getWorkItemDtos().add(workItemDto.clone());
			}
			workItemDto.setOperType(OperType.DEFAULT);
		}
		
		if(updateKeyMap!=null&&updateKeyMap.size()>0){
			persistProcessModel.setUpdateKeyMap(new HashMap<>(updateKeyMap));
			
			for(String key:attrMap.keySet()){
				if(updateKeyMap.containsKey(key)){
					persistProcessModel.getAttrMap().put(key, attrMap.get(key));
				}
			}
			
			for(String key:paramMap.keySet()){
				if(updateKeyMap.containsKey(key)){
					persistProcessModel.getParamMap().put(key, paramMap.get(key));
				}
			}
			
			updateKeyMap.clear();
		}
		
		if (isDirty) {
			return persistProcessModel;
		}
		return null;
	}
}
