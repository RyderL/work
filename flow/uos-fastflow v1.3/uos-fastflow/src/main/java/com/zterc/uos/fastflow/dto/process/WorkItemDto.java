package com.zterc.uos.fastflow.dto.process;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkItemDto implements Serializable,Cloneable {
	static final long serialVersionUID = -6597085660166459770L;

	/** 工作项ID */
	private Long workItemId; //
	private Long oldWorkItemId; //
	private String processDefineId; //

	private Long processInstanceId; //
	private String processInstanceName; //
	private Long tacheId; //
	/** 工作项名称 */
	private String name; //
	/** 活动定义ID */
	private String activityDefinitionId; //
	/** 活动实例ID */
	private Long activityInstanceId; //
	/** 工作项状态 */
	private int state; //
	/** 工作项优先级 */
	private int priority; //
	/** 工作项的参与者ID */
	private int participantId; //
	/** 工作项的参与者职位ID */
	private int participantPositionId; //
	/** 工作项分配到组织的ID */
	private int organizationId; //
	/** 工作项提单人ID */
	//
	/** 工作项提单人职位ID */
	//
	/** 分配时间 */
	private Timestamp assignedDate;
	/** 开始时间 */
	private Timestamp startedDate;
	/** 完成时间 */
	private Timestamp completedDate;
	/** 持续时间 */
	private Timestamp dueDate;
	/** 注释 */
	private String memo;

	/* add by ji.dong 2012-11-05 ur:86940 */
	private Long batchid;

	// add by ji.dong 2013-03-06 ur:26538
	private Long createSource;
	// 区域 add by che.zi 2014-12-23
	private String areaId;
	// 环节编码
	private String tacheCode;
	private int subFlowCount;// 子流程数量
	private int finishSubFlowCount;// 完成子流程数量
	
	private Integer operType;
	
	private String partyType;//参与人
	private String partyId;
	private String partyName;
	private String manualPartyType;//人工执行人
	private String manualPartyId;
	private String manualPartyName;
	private String operatePartyType;//操作人
	private String operatePartyId;
	private String operatePartyName;
	private int isAuto;//是否自动回单：0 否 1 是
	private String direction;//方向，正向：1反向：0
	
	private Date limitDate;
	private Date alertDate;
	private String areaName;
	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public WorkItemDto() {
	}

	public WorkItemDto(Long workItemId, String name) {
		this.workItemId = workItemId;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTacheId() {
		return tacheId;
	}

	public void setTacheId(Long tacheId) {
		this.tacheId = tacheId;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}

	public Long getOldWorkItemId() {
		return oldWorkItemId;
	}

	public void setOldWorkItemId(Long oldWorkItemId) {
		this.oldWorkItemId = oldWorkItemId;
	}

	public String getActivityDefinitionId() {
		return activityDefinitionId;
	}

	public void setActivityDefinitionId(String activityDefinitionId) {
		this.activityDefinitionId = activityDefinitionId;
	}

	public Long getActivityInstanceId() {
		return activityInstanceId;
	}

	public void setActivityInstanceId(Long activityInstanceId) {
		this.activityInstanceId = activityInstanceId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getParticipantId() {
		return participantId;
	}

	public int getParticipantPositionId() {
		return participantPositionId;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
	}

	public void setParticipantPositionId(int participantPositionId) {
		this.participantPositionId = participantPositionId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Timestamp getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Timestamp assignedDate) {
		this.assignedDate = assignedDate;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int hashCode() {
		return workItemId.hashCode();
	}

	public String toString() {
		return "WORKITEM ID=" + workItemId + ",NAME=" + name;
	}

	public String getProcessDefineId() {
		return processDefineId;
	}

	public void setProcessDefineId(String processDefineId) {
		this.processDefineId = processDefineId;
	}

	public Long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(Long workItemId) {
		this.workItemId = workItemId;
	}

	public Long getBatchid() {
		return batchid;
	}

	public void setBatchid(Long batchid) {
		this.batchid = batchid;
	}

	// add by ji.dong 2013-03-06 ur:26538 begin
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

	public String getTacheCode() {
		return tacheCode;
	}

	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}

	public int getSubFlowCount() {
		return subFlowCount;
	}

	public void setSubFlowCount(int subFlowCount) {
		this.subFlowCount = subFlowCount;
	}

	public int getFinishSubFlowCount() {
		return finishSubFlowCount;
	}

	public void setFinishSubFlowCount(int finishSubFlowCount) {
		this.finishSubFlowCount = finishSubFlowCount;
	}
	
	public String getPartyType() {
		return partyType;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getManualPartyType() {
		return manualPartyType;
	}

	public void setManualPartyType(String manualPartyType) {
		this.manualPartyType = manualPartyType;
	}

	public String getManualPartyId() {
		return manualPartyId;
	}

	public void setManualPartyId(String manualPartyId) {
		this.manualPartyId = manualPartyId;
	}

	public String getManualPartyName() {
		return manualPartyName;
	}

	public void setManualPartyName(String manualPartyName) {
		this.manualPartyName = manualPartyName;
	}

	public String getOperatePartyType() {
		return operatePartyType;
	}

	public void setOperatePartyType(String operatePartyType) {
		this.operatePartyType = operatePartyType;
	}

	public String getOperatePartyId() {
		return operatePartyId;
	}

	public void setOperatePartyId(String operatePartyId) {
		this.operatePartyId = operatePartyId;
	}

	public String getOperatePartyName() {
		return operatePartyName;
	}

	public void setOperatePartyName(String operatePartyName) {
		this.operatePartyName = operatePartyName;
	}

	public int getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("workItemId", "WORKITEMID");
		mapper.put("oldWorkItemId", "OLD_WORKITEMID");
		mapper.put("activityDefinitionId", "ACTIVITYDEFINITIONID");
		mapper.put("activityInstanceId", "ACTIVITYINSTANCEID");
		mapper.put("processDefineId", "PACKAGEDEFINEID");
		mapper.put("processInstanceId", "PROCESSINSTANCEID");
		mapper.put("processInstanceName", "PROCESSINSTANCENAME");
		mapper.put("tacheId", "TACHE_ID");
		mapper.put("tacheCode", "TACHE_CODE");
		mapper.put("name", "NAME");
		mapper.put("participantId", "PARTICIPANTID");
		mapper.put("participantPositionId", "PARTICIPANTPOSITIONID");
		mapper.put("organizationId", "ORGANIZATIONID");
		mapper.put("assignedDate", "ASSIGNEDDATE");
		mapper.put("startedDate", "STARTEDDATE");
		mapper.put("completedDate", "COMPLETEDDATE");
		mapper.put("priority", "PRIORITY");
		mapper.put("dueDate", "DUEDATE");
		mapper.put("state", "STATE");
		mapper.put("memo", "MEMO");
		mapper.put("subFlowCount", "SUBFLOW_COUNT");
		mapper.put("finishSubFlowCount", "FINISH_SUBFLOW_COUNT");
		mapper.put("partyType", "PARTY_TYPE");
		mapper.put("partyId", "PARTY_ID");
		mapper.put("partyName", "PARTY_NAME");
		mapper.put("manualPartyType", "MANUAL_PARTY_TYPE");
		mapper.put("manualPartyId", "MANUAL_PARTY_ID");
		mapper.put("manualPartyName", "MANUAL_PARTY_NAME");
		mapper.put("operatePartyType", "OPERATE_PARTY_TYPE");
		mapper.put("operatePartyId", "OPERATE_PARTY_ID");
		mapper.put("operatePartyName", "OPERATE_PARTY_NAME");
		mapper.put("isAuto", "IS_AUTO");
		mapper.put("areaId", "AREA_ID");
		mapper.put("areaName", "AREA_NAME");
		return mapper;
	}
	
	@Override
	public WorkItemDto clone(){
		try {
			return (WorkItemDto)super.clone();
		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
		}
		return null;
	}

	public Date getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(Date limitDate) {
		this.limitDate = limitDate;
	}

	public Date getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}
}
