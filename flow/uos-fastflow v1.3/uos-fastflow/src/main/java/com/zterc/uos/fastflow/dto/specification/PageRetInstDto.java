package com.zterc.uos.fastflow.dto.specification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public class PageRetInstDto {
	private Long retInstId;
	private String workitemId;
	private String processinstanceId;
	private String tacheCode;
	private String retCode;
	private String retName;
	private String retValue;
	private String retValueName;
	private Date createDate;
	
	public Long getRetInstId() {
		return retInstId;
	}
	public void setRetInstId(Long retInstId) {
		this.retInstId = retInstId;
	}
	public String getWorkitemId() {
		return workitemId;
	}
	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}
	public String getProcessinstanceId() {
		return processinstanceId;
	}
	public void setProcessinstanceId(String processinstanceId) {
		this.processinstanceId = processinstanceId;
	}
	public String getTacheCode() {
		return tacheCode;
	}
	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetName() {
		return retName;
	}
	public void setRetName(String retName) {
		this.retName = retName;
	}
	public String getRetValue() {
		return retValue;
	}
	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}
	public String getRetValueName() {
		return retValueName;
	}
	public void setRetValueName(String retValueName) {
		this.retValueName = retValueName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("retInstId", "RET_INST_ID");
		mapper.put("workitemId", "WORKITEM_ID");
		mapper.put("processinstanceId", "PROCESSINSTANCE_ID");
		mapper.put("tacheCode", "TACHE_CODE");
		mapper.put("retCode", "RET_CODE");
		mapper.put("retName", "RET_NAME");
		mapper.put("retValue", "RET_VALUE");
		mapper.put("retValueName", "RET_VALUE_NAME");
		mapper.put("createDate", "CREATE_DATE");
		return mapper;
	}
	
}
