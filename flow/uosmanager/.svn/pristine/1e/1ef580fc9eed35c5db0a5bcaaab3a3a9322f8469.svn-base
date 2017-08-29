package com.ztesoft.uosflow.web.service.flow;

import java.util.Map;

/**
 * 
 * 流程定义处理接口
 * 
 * @author gong.yi 2015.05.05
 * 
 */
public interface FlowServ {
	/**
	 * 根据区域id和系统编码获取流程目录列表
	 * 
	 * @param params
	 * @return
	 */
	public String queryPackageCatalogByAreaIdAndSystemCode(
			Map<String, Object> map);

	/**
	 * 根据流程目录id获取流程定义列表
	 * 
	 * @param params
	 * @return
	 */
	public String qryPackageDefineByCatalogId(Map<String, Object> map);

	/**
	 * 根据流程模板id获取流程定义列表
	 * 
	 * @param params
	 * @return
	 */
	public String qryPackageDefineByPackageId(Map<String, Object> map);

	/**
	 * 根据流程定义id获取流程定义报文
	 * 
	 * @param params
	 * @return
	 */
	public String getXPDL(Map<String, Object> map);

	/**
	 * 根据流程定义id查找流程定义信息
	 * 
	 * @param params
	 * @return
	 */
	public String findProcessDefinitionById(Map<String, Object> map);

	/**
	 * 根据流程定义code查找流程定义信息
	 * 
	 * @param params
	 * @return
	 */
	public String findProcessDefinitionByCode(Map<String, Object> map);

	/**
	 * 根据流程模板id查找流程模板信息
	 * 
	 * @param params
	 * @return
	 */
	public String findPackageById(Map<String, Object> map);

	/**
	 * 添加流程目录
	 * 
	 * @param params
	 * @return
	 */
	public String addPackageCatalog(Map<String, Object> map);

	/**
	 * 修改目录
	 * 
	 * @param params
	 * @return
	 */
	public String updatePackageCatalog(Map<String, Object> map);

	/**
	 * 删除流程目录
	 * 
	 * @param params
	 * @return
	 */
	public String deletePackageCatalog(Map<String, Object> map);

	/**
	 * 增加流程模板
	 * 
	 * @param params
	 * @return
	 */
	public String addPackage(Map<String, Object> map);

	/**
	 * 修改流程模板
	 * 
	 * @param params
	 * @return
	 */
	public String updatePackage(Map<String, Object> map);

	/**
	 * 删除流程模板
	 * 
	 * @param params
	 * @return
	 */
	public String deletePackage(Map<String, Object> map);

	/**
	 * 添加流程定义
	 * 
	 * @param params
	 * @return
	 */
	public String addProcessDefine(Map<String, Object> map);

	/**
	 * 删除流程定义
	 * 
	 * @param params
	 * @return
	 */
	public String deleteProcessDefine(Map<String, Object> map);

	/**
	 * 保存流程定义
	 * 
	 * @param params
	 * @return
	 */
	public String saveXPDL(Map<String, Object> map);

	/**
	 * 流程版本生效，失效
	 * 
	 * @param params
	 * @return
	 */
	public String updateFlowState(Map<String, Object> map);

	/**
	 * 获取流程参数信息
	 */
	public String qryFlowParamDefs(Map<String, Object> map);

	/**
	 * 增加流程参数
	 */
	public String addFlowParamDef(Map<String, Object> map);

	/**
	 * 修改流程参数
	 */
	public String modFlowParamDef(Map<String, Object> map);

	/**
	 * 删除流程参数
	 */
	public String delFlowParamDef(Map<String, Object> map);
	
	/**
	 * 删除流程参数
	 */
	public String delAddBatchFlowParamDefRel(Map<String, Object> map) ;
	/**
	 * 断流程参数是否被使用到模版中
	 */
	public String isExistRela(Map<String, Object> map);
	/**
	 * 获取流程参数关系信息
	 */
	public String qryFlowParamDefRels(Map<String, Object> map);

	/**
	 * 批量处理流程参数关系：删除旧值，重插新值
	 */
	public String updateBatchFlowParamDefRel(Map<String, Object> map);

	/**
	 * 根据多个流程定义codes查找多个流程定义信息
	 * 
	 * @param params
	 * @return
	 */
	public String findProcessDefinitionsByCodes(Map<String, Object> map);

	/**
	 * 查询派发规则
	 * @param map
	 * @return
	 */
	public String qryDispatchRuleByCond(Map<String, Object> map);
	/**
	 * 增加派发规则
	 * @param map
	 * @return
	 */
	public String addDispatchRule(Map<String, Object> map);
	/**
	 * 修改派发规则
	 * @param map
	 * @return
	 */
	public String modDispatchRule(Map<String, Object> map);
	/**
	 * 删除派发规则
	 * @param map
	 * @return
	 */
	public String delDispatchRule(Map<String, Object> map);
	
	/**
	 * 根据目录id查询流程模板信息（不包含流程版本信息）
	 * @param map
	 * @return
	 */
	public String qryProcessDefineByCatalogId(Map<String,Object> map);
	
	/**
	 * 根据流程模板名称查询流程模板信息
	 * @param map
	 * @return
	 */
	public String qryProcessDefineByName(Map<String,Object> map) throws Exception;
	
	/**
	 * 将流程模板的配置数据迁移到新版本中
	 * @param map
	 * @return
	 */
	public String saveProcessDefsAsNew(Map<String,Object> map);
}
