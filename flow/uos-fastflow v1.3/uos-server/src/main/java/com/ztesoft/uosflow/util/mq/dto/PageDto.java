package com.ztesoft.uosflow.util.mq.dto;

import java.util.List;

import com.zterc.uos.fastflow.dto.MQExceptionDto;

public class PageDto {

	// ��Nҳ����0��ʼ
	private Long pageIndex = 1L;
	// ÿҳ����
	private Long pageSize = 0L;
	// ����
	private Long totalCount = 0L;
	// ��ҳ��
	private Long totalPages = 0L;
	// �����б�
	private List<MQExceptionDto> datas;

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

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}

	public List<MQExceptionDto> getDatas() {
		return datas;
	}

	public void setDatas(List<MQExceptionDto> datas) {
		this.datas = datas;
	}
}
