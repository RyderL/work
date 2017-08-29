package com.zterc.uos.fastflow.dto.process;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class ExceptionDto implements Serializable {
	static final long serialVersionUID = 2937418527633017723L;
	public static final String ERROR_HANDLE_FINISHED = "2";
	public static final String ERROR_NOT_HANDLE = "1";
	public static final String ERROR_INIT = "0";

	private Long id;
	private Long processInstanceId;
	private String msg;
	private String state;
	private Integer dealTimes;
	private Timestamp createDate;
	private Timestamp dealDate;
	private String errorInfo;
	private String workItemId;//工作项id
	private String tacheId;//环节id
	private String exceptionType;//异常类型
	private String reasonClass;//异常大类
	private String commandCode;//接口编码
	private String areaId;//区域

	public ExceptionDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getDealTimes() {
		return dealTimes;
	}

	public void setDealTimes(Integer dealTimes) {
		this.dealTimes = dealTimes;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getDealDate() {
		return dealDate;
	}

	public void setDealDate(Timestamp dealDate) {
		this.dealDate = dealDate;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("processInstanceId", "PROCESS_INSTANCE_ID");
		mapper.put("msg", "MSG");
		mapper.put("state", "STATE");
		mapper.put("dealTimes", "DEAL_TIMES");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("dealDate", "DEAL_DATE");
		mapper.put("errorInfo", "ERROR_INFO");
		mapper.put("workItemId", "WORKITEM_ID");
		mapper.put("tacheId", "TACHE_ID");
		mapper.put("exceptionType", "EXCEPTION_TYPE");
		mapper.put("reasonClass", "REASON_CLASS");
		mapper.put("areaId", "AREA_ID");
		mapper.put("commandCode", "COMMAND_CODE");
		return mapper;
	}

	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public String getTacheId() {
		return tacheId;
	}

	public void setTacheId(String tacheId) {
		this.tacheId = tacheId;
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getReasonClass() {
		return reasonClass;
	}

	public void setReasonClass(String reasonClass) {
		this.reasonClass = reasonClass;
	}

	public String getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
}
