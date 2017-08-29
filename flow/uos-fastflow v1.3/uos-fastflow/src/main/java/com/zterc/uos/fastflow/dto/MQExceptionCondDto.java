package com.zterc.uos.fastflow.dto;

import java.util.Date;

public class MQExceptionCondDto extends MQExceptionDto {

	public Date createDateFrom;
	public Date createDateTo;
	public Date stateDateFrom;
	public Date stateDateTo;
	public String sendMsgLike;
	public Long pageIndex;
	public Long pageSize;

	public Date getCreateDateFrom() {
		return createDateFrom;
	}

	public void setCreateDateFrom(Date createDateFrom) {
		this.createDateFrom = createDateFrom;
	}

	public Date getCreateDateTo() {
		return createDateTo;
	}

	public void setCreateDateTo(Date createDateTo) {
		this.createDateTo = createDateTo;
	}

	public Date getStateDateFrom() {
		return stateDateFrom;
	}

	public void setStateDateFrom(Date stateDateFrom) {
		this.stateDateFrom = stateDateFrom;
	}

	public Date getStateDateTo() {
		return stateDateTo;
	}

	public void setStateDateTo(Date stateDateTo) {
		this.stateDateTo = stateDateTo;
	}

	public String getSendMsgLike() {
		return sendMsgLike;
	}

	public void setSendMsgLike(String sendMsgLike) {
		this.sendMsgLike = sendMsgLike;
	}

	public Long getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Long pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
}
