package com.zterc.uos.fastflow.dto.process;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程实例数据传输对象
 */
public class ProcessInstanceDto implements Serializable,Cloneable {
	static final long serialVersionUID = 5837467467386658185L;

	// 特殊情况标记取值，正常流程
	public static final String SIGN_NORMAL = null;
	// 特殊情况标记取值，撤单流程
	public static final String SIGN_CANCEL = "1";

	/** 流程实例ID */
	private Long processInstanceId;
	/** 流程实例名称 */
	private String name;
	/** 流程定义名称 */
	private String processDefinitionName;
	/** 父活动节点的实例ID */
	private Long parentActivityInstanceId;
	/** 包ID */
	private String processDefineId;
	/** 包Name */
	/** 优先级 */
	private int priority;
	/** 流程实例的状态 */
	private int state;
	/** 创建时间 */
	private Timestamp createdDate;
	/** 开始时间 */
	private Timestamp startedDate;
	/** 完成时间 */
	private Timestamp completedDate; //
	/** 持续时间 */
	private Timestamp dueDate;
	/** 流程实例的启动者 */
	private Long participantId;
	/** 流程实例的启动者的职位 */
	private Long participantPositionId;
	/** 异常流程对应的正常流程实例ID */
	private Long oldProcessInstanceId;
	/** 处于同步依赖关系中的活动实例个数 */
	private Long synCount;
	/** 是否处于同步依赖中(0 - 否,1 - 是) */
	private String isSyn;
	/** 标记位，用于标记特殊情况的流程，默认为空 1-撤单流程 */
	private String sign;
	/**
	 * added by ye.runhua 2014-03-26 结合DaaS平台水平分库策略，只有入库的时候接收一下应用传入的orderId属性
	 * 以方便流程相关实例数据和应用定单处于同一个数据库中
	 */
	private Long daasSeedNumber;
	// 区域 add by che.zi 2014-12-23
	private String areaId;
	/** 流程模板code */
	private String processDefineCode;
	
	
	/** 基于内存模型的操作类型 */
	private Integer operType;
	
	private Timestamp limitDate;//完成时限
	
	private Timestamp alertDate;//告警时间

	private Timestamp suspendDate;//挂起时间
	private Timestamp resumeDate;//解挂时间
	private Map<String,String> flowParamMap;//流程参数
	private String areaName;//区域名称
	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public ProcessInstanceDto() {
	}

	public ProcessInstanceDto(Long processInstanceId, String name) {
		this.processInstanceId = processInstanceId;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public Long getParentActivityInstanceId() {
		return parentActivityInstanceId;
	}

	public void setParentActivityInstanceId(Long parentActivityInstanceId) {
		this.parentActivityInstanceId = parentActivityInstanceId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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

	public int hashCode() {
		return processInstanceId.hashCode();
	}

	public String toString() {
		return "PROCESSINSTANCE ID=" + processInstanceId + ",NAME=" + name;
	}

	public Long getOldProcessInstanceId() {
		return oldProcessInstanceId;
	}

	public void setOldProcessInstanceId(Long oldProcessInstanceId) {
		this.oldProcessInstanceId = oldProcessInstanceId;
	}

	public Long getParticipantPositionId() {
		return participantPositionId;
	}

	public void setParticipantPositionId(Long participantPositionId) {
		this.participantPositionId = participantPositionId;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public String getProcessDefineId() {
		return processDefineId;
	}

	public String getIsSyn() {
		return isSyn;
	}

	public Long getSynCount() {
		return synCount;
	}

	public void setProcessDefineId(String processDefineId) {
		this.processDefineId = processDefineId;
	}

	public void setIsSyn(String isSyn) {
		this.isSyn = isSyn;
	}

	public void setSynCount(Long synCount) {
		this.synCount = synCount;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Long getDaasSeedNumber() {
		return daasSeedNumber;
	}

	public void setDaasSeedNumber(Long daasSeedNumber) {
		this.daasSeedNumber = daasSeedNumber;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getProcessDefineCode() {
		return processDefineCode;
	}

	public void setProcessDefineCode(String processDefineCode) {
		this.processDefineCode = processDefineCode;
	}

	
	public Timestamp getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(Timestamp limitDate) {
		this.limitDate = limitDate;
	}

	public Timestamp getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(Timestamp alertDate) {
		this.alertDate = alertDate;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("processDefineId", "PACKAGEDEFINEID");
		mapper.put("processDefinitionName", "PROCESSDEFINITIONNAME");
		mapper.put("parentActivityInstanceId", "PARENTACTIVITYINSTANCEID");
		mapper.put("processDefineCode", "PACKAGEDEFINECODE");
		mapper.put("name", "NAME");
		mapper.put("createdDate", "CREATEDDATE");
		mapper.put("startedDate", "STARTEDDATE");
		mapper.put("completedDate", "COMPLETEDDATE");
		mapper.put("dueDate", "DUEDATE");
		mapper.put("priority", "PRIORITY");
		mapper.put("state", "STATE");
		mapper.put("participantId", "PARTICIPANTID");
		mapper.put("participantPositionId", "PARTICIPANTPOSITIONID");
		mapper.put("oldProcessInstanceId", "OLD_PROCESSINSTANCEID");
		mapper.put("sign", "SIGN");
		mapper.put("areaId", "AREA_ID");
		mapper.put("areaName", "AREA_NAME");
		mapper.put("processInstanceId", "PROCESSINSTANCEID");
		mapper.put("direction","DIRECTION");
		mapper.put("synCount", "SYN_COUNT");
		mapper.put("isSyn", "ISSYN");
		mapper.put("flag", "FLAG");
		mapper.put("limitDate", "LIMIT_DATE");
		mapper.put("alertDate", "ALERT_DATE");
		return mapper;
	}
	@Override
	public ProcessInstanceDto clone(){
		try {
			return (ProcessInstanceDto)super.clone();
		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
		}
		return null;
	}

	public Timestamp getSuspendDate() {
		return suspendDate;
	}

	public void setSuspendDate(Timestamp suspendDate) {
		this.suspendDate = suspendDate;
	}

	public Timestamp getResumeDate() {
		return resumeDate;
	}

	public void setResumeDate(Timestamp resumeDate) {
		this.resumeDate = resumeDate;
	}

	public Map<String, String> getFlowParamMap() {
		return flowParamMap;
	}

	public void setFlowParamMap(Map<String, String> flowParamMap) {
		this.flowParamMap = flowParamMap;
	}
}
