package com.zterc.uos.fastflow.dto.process;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.zterc.uos.fastflow.model.WorkflowProcess;

public class ProcessDefinitionDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 状态:激活
	public static final String STATE_ACTIVE = "10A";
	// 状态：锁定
	public static final String STATE_LOCK = "10B";
	// 失效
	public static final String STATE_INACTIVE = "10C";
	// 删除
	public static final String STATE_DELETED = "10X";

	// 状态:激活
	public static final String STATE_ACTIVE_TAG = "激活";
	// 状态：锁定
	public static final String STATE_LOCK_TAG = "锁定";
	// 失效
	public static final String STATE_INACTIVE_TAG = "失效";
	// 删除
	public static final String STATE_DELETED_TAG = "删除";

	private Long packageDefineId;
	private String packageDefineCode;
	private Long packageId;
	private String name;
	private String description;
	private String version;
	private String author;
	private String state;
	private String stateName;
	private Timestamp stateDate;
	private Timestamp validFromDate;
	private Timestamp validToDate;
	private String editUser;
	private Timestamp editDate;
	private Blob xpdl;
	private String xpdlContent;
	private String catalogId; // 流程所在目录id
	private String catalogName; // 流程所在目录名称
	private String areaId; // 地域id
	private String pathCode; // 目录路径
	private WorkflowProcess workflowProcess;
	private Long routeId;

	public Long getPackageDefineId() {
		return packageDefineId;
	}

	public void setPackageDefineId(Long packageDefineId) {
		this.packageDefineId = packageDefineId;
	}

	public String getPackageDefineCode() {
		return packageDefineCode;
	}

	public void setPackageDefineCode(String packageDefineCode) {
		this.packageDefineCode = packageDefineCode;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		if (state != null) {
			if (state.equalsIgnoreCase(STATE_ACTIVE)) {
				stateName = STATE_ACTIVE_TAG;
			}
			if (state.equalsIgnoreCase(STATE_LOCK)) {
				stateName = STATE_LOCK_TAG;
			}
			if (state.equalsIgnoreCase(STATE_INACTIVE)) {
				stateName = STATE_INACTIVE_TAG;
			}
			if (state.equalsIgnoreCase(STATE_DELETED)) {
				stateName = STATE_DELETED_TAG;
			}
		}
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Timestamp getStateDate() {
		return stateDate;
	}

	public void setStateDate(Timestamp stateDate) {
		this.stateDate = stateDate;
	}

	public Timestamp getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Timestamp validFromDate) {
		this.validFromDate = validFromDate;
	}

	public Timestamp getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Timestamp validToDate) {
		this.validToDate = validToDate;
	}

	public String getEditUser() {
		return editUser;
	}

	public void setEditUser(String editUser) {
		this.editUser = editUser;
	}

	public Timestamp getEditDate() {
		return editDate;
	}

	public void setEditDate(Timestamp editDate) {
		this.editDate = editDate;
	}

	public Blob getXpdl() {
		return xpdl;
	}

	public void setXpdl(Blob xpdl) {
		this.xpdl = xpdl;
	}

	public String getXpdlContent() {
		return xpdlContent;
	}

	public void setXpdlContent(String xpdlContent) {
		this.xpdlContent = xpdlContent;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getPathCode() {
		return pathCode;
	}

	public void setPathCode(String pathCode) {
		this.pathCode = pathCode;
	}

	public WorkflowProcess getWorkflowProcess() {
		return workflowProcess;
	}

	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

	public Long getRouteId() {
		return routeId;
	}

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ProcessDefinitionDto)) {
			return false;
		}
		ProcessDefinitionDto that = (ProcessDefinitionDto) obj;
		if (!(that.packageDefineId == null ? this.packageDefineId == null : that.packageDefineId
				.equals(this.packageDefineId))) {
			return false;
		}
		if (!(that.packageId == null ? this.packageId == null : that.packageId.equals(this.packageId))) {
			return false;
		}
		if (!(that.name == null ? this.name == null : that.name.equals(this.name))) {
			return false;
		}
		if (!(that.description == null ? this.description == null : that.description.equals(this.description))) {
			return false;
		}
		if (!(that.version == null ? this.version == null : that.version.equals(this.version))) {
			return false;
		}
		if (!(that.author == null ? this.author == null : that.author.equals(this.author))) {
			return false;
		}
		if (!(that.state == null ? this.state == null : that.state.equals(this.state))) {
			return false;
		}
		if (!(that.stateDate == null ? this.stateDate == null : that.stateDate.equals(this.stateDate))) {
			return false;
		}
		if (!(that.validFromDate == null ? this.validFromDate == null : that.validFromDate.equals(this.validFromDate))) {
			return false;
		}
		if (!(that.validToDate == null ? this.validToDate == null : that.validToDate.equals(this.validToDate))) {
			return false;
		}
		if (!(that.editUser == null ? this.editUser == null : that.editUser.equals(this.editUser))) {
			return false;
		}
		if (!(that.editDate == null ? this.editDate == null : that.editDate.equals(this.editDate))) {
			return false;
		}
		if (!(that.xpdlContent == null ? this.xpdlContent == null : that.xpdlContent.equals(this.xpdlContent))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.packageDefineId.hashCode();
		result = 37 * result + this.packageId.hashCode();
		result = 37 * result + this.name.hashCode();
		result = 37 * result + this.description.hashCode();
		result = 37 * result + this.version.hashCode();
		result = 37 * result + this.author.hashCode();
		result = 37 * result + this.state.hashCode();
		result = 37 * result + this.stateDate.hashCode();
		result = 37 * result + this.validFromDate.hashCode();
		result = 37 * result + this.validToDate.hashCode();
		result = 37 * result + this.editUser.hashCode();
		result = 37 * result + this.editDate.hashCode();

		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += packageDefineId;
		returnString += ", " + packageId;
		returnString += ", " + name;
		returnString += ", " + description;
		returnString += ", " + version;
		returnString += ", " + author;
		returnString += ", " + state;
		returnString += ", " + stateDate;
		returnString += ", " + validFromDate;
		returnString += ", " + validToDate;
		returnString += ", " + editUser;
		returnString += ", " + editDate;
		returnString += ", " + packageDefineCode;
		return returnString;
	}
	
	public JsonObject getTreeJsonObject() {
		JsonObject proc = new JsonObject();
		proc.addProperty("id", this.getPackageDefineId());
		proc.addProperty("parentId", this.getPackageId());
		proc.addProperty("text", this.getVersion() + "(" + this.getStateName()
				+ ")");
		proc.addProperty("type", 3);
		proc.addProperty("code", this.getPackageDefineCode());
		return proc;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("packageDefineId", "PACKAGEDEFINEID");
		mapper.put("packageId", "PACKAGEID");
		mapper.put("name", "NAME");
		mapper.put("description", "DESCRIPTION");
		mapper.put("version", "VERSION");
		mapper.put("author", "AUTHOR");
		mapper.put("state", "STATE");
		// mapper.put("stateName", "STATE");
		mapper.put("stateDate", "STATEDATE");
		mapper.put("validFromDate", "VALIDFROMDATE");
		mapper.put("validToDate", "VALIDTODATE");
		mapper.put("editUser", "EDITUSER");
		mapper.put("editDate", "EDITDATE");
		mapper.put("routeId", "ROUTE_ID");
		mapper.put("xpdl", "XPDL");
		mapper.put("packageDefineCode", "PACKAGEDEFINECODE");
		return mapper;
	}
}