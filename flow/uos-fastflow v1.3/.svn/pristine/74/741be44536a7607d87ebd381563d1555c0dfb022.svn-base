package com.zterc.uos.fastflow.dto.process;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TransitionInstanceDto implements Serializable,Cloneable {
	static final long serialVersionUID = 6669764971567180472L;
	// 正向常量
	public static final String NORMAL_DIRECTION = "1";
	// 反向常量
	public static final String REVERSE_DIRECTION = "0";

	/** 变迁实例ID */
	private Long id; //
	/** 变迁定义ID */
	private String transitionDefinitionId; //
	/** 变迁定义Name */
	private String transitionDefinitionName; //
	/** 流程实例数据 */
	private Long processInstanceId; //
	/** From活动实例ID */
	private Long fromActivityInstanceId; //
	/** To活动实例ID */
	private Long toActivityInstanceId; //
	/** 状态最近一次修改时间 */
	private Timestamp lastDate; //
	/** 实例状态 */
	private int state; //
	/** 引起状态变迁的操作 */
	private int action; //
	/** 操作者 */
	private int participant; //
	/** 操作注释 */
	private String memo;
	/** 方向 **/
	private String direction;

	// 区域 add by che.zi 2014-12-23
	private String areaId;
	
	private Integer operType;

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public TransitionInstanceDto() {
	}

	public TransitionInstanceDto(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransitionDefinitionId() {
		return transitionDefinitionId;
	}

	public void setTransitionDefinitionId(String transitionDefinitionId) {
		this.transitionDefinitionId = transitionDefinitionId;
	}

	public String getTransitionDefinitionName() {
		return transitionDefinitionName;
	}

	public void setTransitionDefinitionName(String transitionDefinitionName) {
		this.transitionDefinitionName = transitionDefinitionName;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getFromActivityInstanceId() {
		return fromActivityInstanceId;
	}

	public void setFromActivityInstanceId(Long fromActivityInstanceId) {
		this.fromActivityInstanceId = fromActivityInstanceId;
	}

	public Long getToActivityInstanceId() {
		return toActivityInstanceId;
	}

	public void setToActivityInstanceId(Long toActivityInstanceId) {
		this.toActivityInstanceId = toActivityInstanceId;
	}

	public Timestamp getLastDate() {
		return lastDate;
	}

	public void setLastDate(Timestamp lastDate) {
		this.lastDate = lastDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getParticipant() {
		return participant;
	}

	public void setParticipant(int participant) {
		this.participant = participant;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String toString() {
		return id.toString();
	}

	public int hashCode() {
		return id.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof TransitionInstanceDto) {
			TransitionInstanceDto otherKey = (TransitionInstanceDto) o;
			return (id.equals(otherKey.id));
		} else {
			return false;
		}
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "TRANSITIONINSTANCEID");
		mapper.put("processInstanceId", "PROCESSINSTANCEID");
		mapper.put("transitionDefinitionId", "TRANSITIONDEFINITIONID");
		mapper.put("transitionDefinitionName", "TRANSITIONDEFINITIONNAME");
		mapper.put("fromActivityInstanceId", "FROMACTIVITYINSTANCEID");
		mapper.put("toActivityInstanceId", "TOACTIVITYINSTANCEID");
		mapper.put("lastDate", "LASTDATE");
		mapper.put("state", "STATE");
		mapper.put("action", "ACTION");
		mapper.put("participant", "PARTICIPANTID");
		mapper.put("memo", "MEMO");
		mapper.put("direction", "DIRECTION");
		return mapper;
	}
	
	@Override
	public TransitionInstanceDto clone(){
		try {
			return (TransitionInstanceDto)super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return null;
	}
}
