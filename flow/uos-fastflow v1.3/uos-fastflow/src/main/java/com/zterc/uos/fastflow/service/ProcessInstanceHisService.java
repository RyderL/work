package com.zterc.uos.fastflow.service;

import java.util.Map;

import com.zterc.uos.fastflow.dao.process.his.ProcessInstanceHisDAO;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

/**
 * （实例）流程实例操作类
 * 
 * @author gong.yi
 *
 */
public class ProcessInstanceHisService {

	private ProcessInstanceHisDAO processInstanceHisDAO;

	public ProcessInstanceHisDAO getProcessInstanceHisDAO() {
		return processInstanceHisDAO;
	}


	public void setProcessInstanceHisDAO(ProcessInstanceHisDAO processInstanceHisDAO) {
		this.processInstanceHisDAO = processInstanceHisDAO;
	}


	/**
	 * （Manager）根据条件查找流程实例
	 * 
	 * @param paramMap
	 * @return
	 */
	public PageDto queryProcessInstancesHisByCond(
			Map<String, Object> paramMap) {
		return processInstanceHisDAO.queryProcessInstancesHisByCond(paramMap);
	}


	public ProcessInstanceDto queryProcessInstance(String processInstanceId) {
		return processInstanceHisDAO.queryProcessInstance(processInstanceId);
	}

}
