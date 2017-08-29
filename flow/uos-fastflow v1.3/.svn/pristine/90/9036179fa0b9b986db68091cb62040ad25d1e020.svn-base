package com.zterc.uos.fastflow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.parse.MatrixGraphic;

public class WorkflowProcess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String state;
	private List<Activity> activities;
	private Map<String, Activity> activitiesMap;
	private List<Transition> transitions;
	private Map<String, Transition> transitionsMap;
	private MatrixGraphic matrix;

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public Map<String, Transition> getTransitionsMap() {
		return transitionsMap;
	}

	public void setTransitionsMap(Map<String, Transition> transitionsMap) {
		this.transitionsMap = transitionsMap;
	}

	public Map<String, Activity> getActivitiesMap() {
		return activitiesMap;
	}

	public void setActivitiesMap(Map<String, Activity> activitiesMap) {
		this.activitiesMap = activitiesMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Activity getStartActivity() {
		return activities.get(0);
	}

	public Activity getActivityById(String activityDefinitionId) {
		return activitiesMap.get(activityDefinitionId);
	}

	public MatrixGraphic getMatrix() {
		return matrix;
	}

	public void setMatrix(MatrixGraphic matrix) {
		this.matrix = matrix;
	}

	public String getExceptionActivityId() {
		return activities.get(activities.size() - 1).getId();
	}

	public Transition getTransitionByTransitionId(String transitionDefinitionId) {
		return transitionsMap.get(transitionDefinitionId);
	}

	public List<Transition> getTransitionsByToActivityId(
			String activityDefinitionId) {
		List<Transition> list = new ArrayList<Transition>();
        for (int i = 0; i < transitions.size(); i++) {
            Transition tran = transitions.get(i);
            if (tran.getTo().equals(activityDefinitionId)) {
                list.add(tran);
            }
        }
        return list;
	}

}
