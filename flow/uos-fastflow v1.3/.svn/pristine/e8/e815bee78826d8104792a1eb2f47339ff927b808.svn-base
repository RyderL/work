package com.zterc.uos.fastflow.dao.process;

import java.util.List;

import com.zterc.uos.fastflow.dto.process.ProcessParamDefDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.ProcessParamDefRelDto;

public interface ProcessParamDefDAO {

	public List<ProcessParamDefDto> queryParamDefsByDefId(
			String processDefinitionId);

	/**
	 * 获取流程参数信息
	 * 
	 * @param systemCode
	 * @return
	 * @throws Exception
	 */
	public PageDto qryProcessParamDefs(String systemCode);

	/**
	 * 增加流程参数
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void addProcessParamDef(ProcessParamDefDto dto);

	/**
	 * 修改流程参数
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void modProcessParamDef(ProcessParamDefDto dto);

	/**
	 * 删除流程参数
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void delProcessParamDef(ProcessParamDefDto dto);

	/**
	 * 获取流程参数关系信息
	 * 
	 * @param packageDefineId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public PageDto qryProcessParamDefRels(ProcessParamDefRelDto dto);

	/**
	 * 删除流程参数关系
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void delProcessParamDefRel(ProcessParamDefRelDto dto);
	
	/**
	 * 删除流程参数关系
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void delProcessParamDefRelNoCode(ProcessParamDefRelDto dto);

	/**
	 * 更新流程参数关系
	 * 
	 * @param dtos
	 */
	public void updateBatchProcessParamDefRel(List<ProcessParamDefRelDto> dtos);
	
	/**
	 * 增加流程参数关系
	 * 
	 * @param dtos
	 */
	public void addBatchProcessParamDefRel(List<ProcessParamDefRelDto> dtos);

	/**
	 * 根据流程变量编码查询流程变量对象
	 * 
	 * @param code
	 * @return
	 */
	public ProcessParamDefDto queryParamDefByCode(String code);
	/**
	 * 判断流程参数是否被使用到模版中
	 * @param code
	 * @return
	 */
	public boolean isExistRela(String code);

	/**
	 * 查询所有定义的流程参数
	 * @return
	 */
	public List<ProcessParamDefDto> qryProcessParamDefs();

	/**
	 * 查询一个流程模板下面配置过流程参数的环节
	 * @param processDefinitionId
	 * @return
	 */
	public List<ProcessParamDefRelDto> qryDistinctTacheCodeByDefId(
			String processDefinitionId);

	/**
	 * 查询一个流程模板下面配置的所有流程参数
	 * @param processDefineId
	 * @return
	 */
	public List<ProcessParamDefRelDto> qryProcessParamDefRelsByDefId(
			String processDefineId);

	
}
