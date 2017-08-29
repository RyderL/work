package com.zterc.uos.fastflow.model;

import java.io.Serializable;

import com.zterc.uos.fastflow.model.condition.Condition;

public class Transition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String TYPE_CONTROL = "Control";
	public static final String TYPE_NORMAL = "Normal";
	
    public static final String CONTROL_LINE_0 = "0";//反向
    public static final String CONTROL_LINE_1 = "1";//正向

	private String id;
	private String name;
	private String from;
	private String to;
	private String lineType;
	private Condition condition;

	private WorkflowProcess workflowProcess;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

	public Activity getToActivity() {
		return workflowProcess.getActivitiesMap().get(to);
	}
	
	public Activity getFromActivity() {
		return workflowProcess.getActivitiesMap().get(from);
	}
	 
	public boolean isControl() {
		return this.lineType.equals(TYPE_CONTROL);
	}

	public boolean isNormal() {
		return this.lineType.equals(TYPE_NORMAL);
	}
}
