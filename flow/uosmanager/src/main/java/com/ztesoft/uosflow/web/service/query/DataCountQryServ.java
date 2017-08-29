package com.ztesoft.uosflow.web.service.query;

import java.util.Map;

public interface DataCountQryServ {
	
	/**
	 * 流程实例个状态数量统计
	 * @param paramMap
	 * @return
	 */
	public String qryProcInstState(Map<String,Object> paramMap); 
	
	/**
	 * 流程模板使用情况top10统计
	 * @param paramMap
	 * @return
	 */
	public String qryProcDefineUseCount(Map<String,Object> paramMap); 

	/**
	 * 工作项各状态数量统计
	 * @param paramMap
	 * @return
	 */
	public String qryWorkItemState(Map<String,Object> paramMap); 

	/**
	 * 异常流程列表
	 * @param paramMap
	 * @return
	 */
	public String qryExceptionFlow(Map<String,Object> paramMap); 
	
	/**
	 * 查询流程实例状态统计4grid展示
	 * @param paramMap
	 * @return
	 */
	public String qryProcInstState4Grid(Map<String,Object> paramMap);

	/**
	 * 查询工作项状态统计4grid展示
	 * @param paramMap
	 * @return
	 */
	public String qryWorkItemState4Grid(Map<String,Object> paramMap);
	
	/**
	 * 查询流程模板使用最多的十个
	 * @param paramMap
	 * @return
	 */
	public String proDefinePie4Grid(Map<String,Object> paramMap);
}
