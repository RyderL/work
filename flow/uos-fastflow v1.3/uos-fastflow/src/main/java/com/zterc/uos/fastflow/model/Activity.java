package com.zterc.uos.fastflow.model;

import java.io.Serializable;
import java.util.List;

import com.zterc.uos.fastflow.parse.JoinType;

public class Activity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TYPE_START = "Start";
	public static final String TYPE_FINISH = "Finish";
	public static final String TYPE_PARALLEL = "Parallel";
	public static final String TYPE_RELATION = "Relation";
	public static final String TYPE_EXCEPTION = "Exception";
	public static final String TYPE_TACHE = "Tache";
	public static final String TYPE_CONTROL = "Control";

	private String id;
	private String name;
	private String nodeType;
	private String tacheId;
	private String tacheCode;
	private String tacheName;
	private String branchIndex;
	private String nodeIndex;
	private int numOfBranch;
	private String joinType;
	private List<Transition> efferentTransitions;
	private List<Transition> afferentTransitions;

	private int order;

	@SuppressWarnings("all")
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

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getTacheId() {
		return tacheId;
	}

	public void setTacheId(String tacheId) {
		this.tacheId = tacheId;
	}

	public String getTacheCode() {
		return tacheCode;
	}

	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}

	public String getTacheName() {
		return tacheName;
	}

	public void setTacheName(String tacheName) {
		this.tacheName = tacheName;
	}

	public String getBranchIndex() {
		return branchIndex;
	}

	public void setBranchIndex(String branchIndex) {
		this.branchIndex = branchIndex;
	}

	public String getNodeIndex() {
		return nodeIndex;
	}

	public void setNodeIndex(String nodeIndex) {
		this.nodeIndex = nodeIndex;
	}

	public int getNumOfBranch() {
		return numOfBranch;
	}

	public void setNumOfBranch(int numOfBranch) {
		this.numOfBranch = numOfBranch;
	}

	public List<Transition> getEfferentTransitions() {
		return efferentTransitions;
	}

	public void setEfferentTransitions(List<Transition> efferentTransitions) {
		this.efferentTransitions = efferentTransitions;
	}

	public List<Transition> getAfferentTransitions() {
		return afferentTransitions;
	}

	public void setAfferentTransitions(List<Transition> afferentTransitions) {
		this.afferentTransitions = afferentTransitions;
	}

	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

	public boolean isNotRouteActivity() {
		if (this.nodeType.equals(TYPE_TACHE)) {
			return true;
		}
		return false;
	}

	public boolean isEndActivity() {
		return efferentTransitions.size() == 0;
	}

	public boolean isStartActivity() {
		return afferentTransitions.size() == 0;
	}
	
	public boolean isRelation() {
		if (this.nodeType.equals(TYPE_RELATION)) {
			return true;
		}
		return false;
	}
	
	public boolean isParallel() {
		if (this.nodeType.equals(TYPE_PARALLEL)) {
			return true;
		}
		return false;
	}
	
	public boolean isControl() {
		if (this.nodeType.equals(TYPE_CONTROL)) {
			return true;
		}
		return false;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	public JoinType getJoinType() {
		if(this.joinType==null){
			return null;
		}
		return JoinType.fromString(this.joinType);
	}
}
