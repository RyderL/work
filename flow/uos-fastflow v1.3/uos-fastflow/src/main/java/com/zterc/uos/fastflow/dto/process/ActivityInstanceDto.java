package com.zterc.uos.fastflow.dto.process;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityInstanceDto implements Serializable,Cloneable {

	// ������
	public static final String NORMAL_DIRECTION = "1";
	// ������
	public static final String REVERSE_DIRECTION = "0";

	static final long serialVersionUID = 4773291777488159051L;

	private String participant;

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	/** �ʵ��ID */
	private Long id;
	/** ����� */
	private String name;
	/** �����ID */
	private String activityDefinitionId;
	/** ����ʵ��ID */
	private Long processInstanceId;
	/** �״̬ */
	private int state;
	/** ���ȼ� */
	private int priority;
	/** ��ʼʱ�� */
	private Timestamp startedDate;
	/** ���ʱ�� */
	private Timestamp completedDate;
	/** ����ʱ�� */
	private Timestamp dueDate;
	/** ��������Ŀ */
	private int itemSum;
	/** �Ѿ���ɵĹ�������Ŀ */
	private int itemCompleted;
	/** ���ڶ��� **/
	private Long tacheId;
	/** ���� **/
	private String direction;
	/** ͬ����Ϣ **/
	private Long synMessage;
	/** �Ƿ񵽴�Ŀ�� **/
	private boolean isReachedTarget = false;
	/** ����ʵ��ID **/
	private Long reverse;
	/** �ʵ����Ӧ�Ĺ����� **/
	private Long workItemId;
	/** add by ���ǈ� 2010-09-21 ԭ�����̸��� ԭ�����̻ʵ��ID */
	private Long atomActivityInstanceId;
	/** add by ���ǈ� 2010-09-27 ԭ�����̸��� ԭ�����̻ */
	private String atomActivityDefinitionId;
	/* add by ji.dong 2012-11-05 ur:86940 */
	private Long batchid;
	// add by ji.dong 2013-03-06 ur:26538
	private Long createSource;
	/** �����״̬ */
	private String activeState;
	// ���� add by che.zi 2014-12-23
	private String areaId;
	
	private String rollbackTranins;
	
	private Integer operType;
	
	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	private transient PropertyChangeSupport propertyChangeListeners = new PropertyChangeSupport(this);

	public ActivityInstanceDto() {
	}

	public ActivityInstanceDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivityDefinitionId() {
		return activityDefinitionId;
	}

	public void setActivityDefinitionId(String activityDefinitionId) {
		this.activityDefinitionId = activityDefinitionId;
	}

	public int getState() {
		return state;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getTacheId() {
		return tacheId;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setTacheId(Long tacheId) {
		this.tacheId = tacheId;
	}

	public void setSynMessage(Long synMessage) {
		this.synMessage = synMessage;
	}

	public Long getSynMessage() {
		return synMessage;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Timestamp getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Timestamp startedDate) {
		this.startedDate = startedDate;
	}

	public Timestamp getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Timestamp completedDate) {
		this.completedDate = completedDate;
	}

	public Timestamp getDueDate() {
		return dueDate;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	public int getItemSum() {
		return itemSum;
	}

	public void setItemSum(int itemSum) {
		this.itemSum = itemSum;
	}

	public int getItemCompleted() {
		return itemCompleted;
	}

	public void setItemCompleted(int itemCompleted) {
		this.itemCompleted = itemCompleted;
	}

	public int hashCode() {
		return id.hashCode();
	}

	public String toString() {
		return "ACTIVITYINSTANCE ID=" + id + ",NAME=" + name;
	}

	public String getActiveState() {
		return activeState;
	}

	public void setActiveState(String activeState) {
		this.activeState = activeState;
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeListeners.removePropertyChangeListener(l);
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeListeners.addPropertyChangeListener(l);
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public boolean isIsReachedTarget() {
		return isReachedTarget;
	}

	public void setIsReachedTarget(boolean isReachedTarget) {
		this.isReachedTarget = isReachedTarget;
	}

	public Long getReverse() {
		return reverse;
	}

	public void setReverse(Long reverse) {
		this.reverse = reverse;
	}

	public Long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(Long workItemId) {
		this.workItemId = workItemId;
	}

	public Long getAtomActivityInstanceId() {
		return atomActivityInstanceId;
	}

	public void setAtomActivityInstanceId(Long atomActivityInstanceId) {
		this.atomActivityInstanceId = atomActivityInstanceId;
	}

	public String getAtomActivityDefinitionId() {
		return atomActivityDefinitionId;
	}

	public void setAtomActivityDefinitionId(String atomActivityDefinitionId) {
		this.atomActivityDefinitionId = atomActivityDefinitionId;
	}

	public Long getBatchid() {
		return batchid;
	}

	public void setBatchid(Long batchid) {
		this.batchid = batchid;
	}

	// add by ji.dong 2013-03-06 ur:26538
	public Long getCreateSource() {
		return createSource;
	}

	public void setCreateSource(Long createSource) {
		this.createSource = createSource;
	}

	// end

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	public String getRollbackTranins() {
		return rollbackTranins;
	}

	public void setRollbackTranins(String rollbackTranins) {
		this.rollbackTranins = rollbackTranins;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("processInstanceId", "PROCESSINSTANCEID");
		mapper.put("activityDefinitionId", "ACTIVITYDEFINITIONID");
		mapper.put("id", "ACTIVITYINSTANCEID");
		mapper.put("name", "NAME");
		mapper.put("state", "STATE");
		mapper.put("reverse", "REVERSE");
		mapper.put("startedDate", "STARTEDDATE");
		mapper.put("completedDate", "COMPLETEDDATE");
		mapper.put("dueDate", "DUEDATE");
		mapper.put("priority", "PRIORITY");
		mapper.put("itemCompleted", "ITEMCOMPLETED");
		mapper.put("itemSum", "ITEMSUM");
		mapper.put("tacheId", "TACHE_ID");
		mapper.put("synMessage", "SYN_MESSAGE");
		mapper.put("direction", "DIRECTION");
		mapper.put("atomActivityInstanceId", "ATOM_ACTIVITYINSTANCE_ID");
		mapper.put("atomActivityDefinitionId", "ATOM_ACTIVITYDEFINITIONID");
		mapper.put("areaId", "AREA_ID");
		mapper.put("rollbackTranins", "ROLLBACK_TRANINS");
		return mapper;
	}
	
	@Override
	public ActivityInstanceDto clone(){
		try {
			return (ActivityInstanceDto)super.clone();
		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return ((ActivityInstanceDto)obj).getId().longValue() == this.getId().longValue();
	}
	
}
