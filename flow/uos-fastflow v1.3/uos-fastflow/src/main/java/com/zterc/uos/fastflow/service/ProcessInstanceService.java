package com.zterc.uos.fastflow.service;

import java.util.Map;

import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.process.ProcessInstanceDAO;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;

/**
 * （实例）流程实例操作类
 * 
 * @author gong.yi
 *
 */
public class ProcessInstanceService {

	private ProcessInstanceDAO processInstanceDAO;

	public void setProcessInstanceDAO(ProcessInstanceDAO processInstanceDAO) {
		this.processInstanceDAO = processInstanceDAO;
	}

	/**
	 * （Server）根据流程实例id查找流程实例
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public ProcessInstanceDto queryProcessInstance(String processInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			ProcessInstanceDto processInstanceDto = processModel
					.getProcessInstanceDto();
			if (processInstanceDto != null) {
				return processInstanceDto;
			}
		}
		// 内存模式操作----end-------------

		return processInstanceDAO.queryProcessInstance(processInstanceId);
	}

	/**
	 * （Server）创建流程实例 
	 * 
	 * @param processInstance
	 * @param useDB
	 * @return
	 */
	public ProcessInstanceDto createProcessInstance(
			ProcessInstanceDto processInstance, boolean useDB) {
		if(processInstance.getProcessInstanceId()==null){
			processInstance.setProcessInstanceId(SequenceHelper
					.getId("UOS_PROCESSINSTANCE"));
		}
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				processInstance = processInstanceDAO
						.createProcessInstance(processInstance);
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel == null) {
					processModel = new ProcessModel();
				}
				processInstance.setOperType(OperType.getOperType(
						processInstance.getOperType(), OperType.INSERT));
				processModel.setProcessInstanceId(processInstance.getProcessInstanceId());
				processModel.setProcessInstanceDto(processInstance);
				ProcessLocalHolder.set(processModel);
				// 内存模式操作----end-------------
			}
		} else {
			processInstance = processInstanceDAO
					.createProcessInstance(processInstance);
		}
		return processInstance;
	}

	/**
	 * （Server）更新流程实例
	 * 
	 * @param processInstance
	 * @param useDB
	 */
	public void updateProcessInstance(ProcessInstanceDto processInstance,
			boolean useDB) {
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				processInstanceDAO.updateProcessInstance(processInstance);
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					processInstance.setOperType(OperType.getOperType(
							processInstance.getOperType(), OperType.UPDATE));
					processModel.setProcessInstanceDto(processInstance);
				}
				// 内存模式操作----end-------------
			}
		} else {
			processInstanceDAO.updateProcessInstance(processInstance);
		}
	}

	/**
	 * （Manager）根据条件查找流程实例
	 * 
	 * @param paramMap
	 * @return
	 */
	public PageDto queryProcessInstancesByCond(
			Map<String, Object> paramMap) {
		return processInstanceDAO.queryProcessInstancesByCond(paramMap);
	}

	public void deleteProcessInstance(String processInstanceId) {
		processInstanceDAO.deleteProcessInstance(processInstanceId);
	}

}
