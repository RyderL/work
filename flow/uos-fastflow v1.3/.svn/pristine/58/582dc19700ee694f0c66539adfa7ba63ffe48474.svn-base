package com.zterc.uos.fastflow.dto.process;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class CommandQueueDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 993169267938680300L;
	private Long id;
	private String commandCode;
	private String commandMsg;
	private Long processInstanceId;
	private Long state;
	private Timestamp createDate;
	private Long route;
	private String commandResultMsg;
	private Long workItemId;
	private String areaId;
	private String serverAddr;//·þÎñÆ÷µØÖ·
	
	public Long getWorkItemId() {
		return workItemId;
	}
	public void setWorkItemId(Long workItemId) {
		this.workItemId = workItemId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCommandCode() {
		return commandCode;
	}
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	public String getCommandMsg() {
		return commandMsg;
	}
	public void setCommandMsg(String commandMsg) {
		this.commandMsg = commandMsg;
	}
	public Long getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Long getRoute() {
		return route;
	}
	public void setRoute(Long route) {
		this.route = route;
	}

	public String getCommandResultMsg() {
		return commandResultMsg;
	}
	public void setCommandResultMsg(String commandResultMsg) {
		this.commandResultMsg = commandResultMsg;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getServerAddr() {
		return serverAddr;
	}
	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("commandCode", "COMMAND_CODE");
		mapper.put("commandMsg", "COMMAND_MSG");
		mapper.put("processInstanceId", "PROCESS_INSTANCE_ID");
		mapper.put("state", "STATE");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("route", "ROUTE");
		mapper.put("commandResultMsg", "COMMAND_RESULT_MSG");
		mapper.put("workItemId", "WORKITEM_ID");
		mapper.put("areaId", "AREA_ID");
		mapper.put("serverAddr", "SERVER_ADDR");
		return mapper;
	}
}
