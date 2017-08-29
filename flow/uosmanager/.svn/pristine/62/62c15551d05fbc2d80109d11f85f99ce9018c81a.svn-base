package com.ztesoft.uosflow.web.inf.model.server;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class JobInfDto implements InfDto, Serializable{
	private static final long serialVersionUID = 9108537437977304707L;
	private String jobId;
	private String jobName;
	private String orgId;
	private String postName;
	private String comments;
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public JsonObject getTreeJsonObject() {
		JsonObject job = new JsonObject();
		job.addProperty("id", this.getJobId());
		job.addProperty("text", this.getJobName());
		job.addProperty("type", "JOB");
		job.addProperty("orgId", this.getOrgId());
		job.addProperty("postName", this.getPostName());
		job.addProperty("coments", this.getComments());
		return job;
	}
}
