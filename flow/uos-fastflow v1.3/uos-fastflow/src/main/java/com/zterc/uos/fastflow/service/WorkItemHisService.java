package com.zterc.uos.fastflow.service;

import java.util.Map;

import com.zterc.uos.fastflow.dao.process.his.WorkItemHisDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;

/**
 * ��Server�������������
 * 
 * @author gong.yi
 *
 */
public class WorkItemHisService {

	private WorkItemHisDAO workItemHisDAO;

	public WorkItemHisDAO getWorkItemHisDAO() {
		return workItemHisDAO;
	}

	public void setWorkItemHisDAO(WorkItemHisDAO workItemHisDAO) {
		this.workItemHisDAO = workItemHisDAO;
	}

	/**
	 * ��Manager�������������ҹ������б�
	 * 
	 * @param paramMap
	 * @return
	 */
	public PageDto findWorkItemHisByCond(Map<String, Object> paramMap) {
		return workItemHisDAO.findWorkItemHisByCond(paramMap);
	}

}
