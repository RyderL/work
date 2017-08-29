package com.zterc.uos.fastflow.dao.process;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.process.ProcessDefinitionDto;

public interface ProcessDefinitionDAO {
	/**
	 * 根据流程模板的列表获取流程定义版本的列表
	 * @param packageIds
	 * @return
	 */
	public ProcessDefinitionDto[] qryProcessDefineByPackageIds(String packageIds);
	
	/**
	 * 根据流程定义的id获取xpdl
	 * @param processDefineId
	 * @return
	 */
//	public ProcessDefinitionDto getXPDL(Long processDefineId);
	
	/**
	 * 根据流程定义id查找流程定义信息
	 * @param processDefineId
	 * @return
	 */
	public ProcessDefinitionDto qryProcessDefinitionById(Long processDefineId);
	
	/**
	 * 根据流程定义code查找流程定义信息
	 * @param processDefineCode
	 * @return
	 */
	public ProcessDefinitionDto qryProcessDefinitionByCode(String processDefineCode);
	
	

	/**
	 * 添加流程定义
	 * @param ProcessDefinitionDto
	 */
	public void addProcessDefinition(ProcessDefinitionDto dto);
	
	/**
	 * 根據packageId更新流程定义的名称
	 * @param packageId
	 * @param packageName
	 */
	public void updateProcessDefinitionNameByPackageId(Long packageId,String packageName);
	
	/**
	 * 删除流程定义
	 * @param dto
	 */
	public void deleteProcessDefinition(ProcessDefinitionDto dto);
	
	/**
	 * 根据模板id查找流程定义列表
	 * @param packageId
	 */
	public ProcessDefinitionDto[] qryProcessDefinitionsByPackageId(Long packageId);
	
	/**
	 * 保存流程定义xpdl
	 * @param processDefineId
	 * @param xpdl
	 */
	public void saveXPDL(Long processDefineId,String xpdl);
	
	/**
	 * 更新流程定义状态
	 * @param processDefineId
	 * @param state
	 */
	public void updateFlowState(Long processDefineId, String state);
	/**
	 * 根据多个流程定义codes查找多个流程定义信息
	 * @param processDefineCodes ex. CODE1,CODE2...,CODEN
	 * @return
	 */
	public List<ProcessDefinitionDto> qryProcessDefinitionsByCodes(String processDefineCodes);

	/**
	 * 根据条件查询流程定义列表（）
	 * 
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> qryAllProcessDefinitionByCond(
			Map<String, String> paramMap);

	public List<ProcessDefinitionDto> qryAllProcessDefines();
	
	public String qryPackageDefinePath(Long processInstanceId);
}
