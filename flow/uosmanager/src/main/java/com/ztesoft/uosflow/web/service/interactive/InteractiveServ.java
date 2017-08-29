package com.ztesoft.uosflow.web.service.interactive;

import java.util.Map;

/**
 * 与业务系统交互的接口
 * @author Administrator
 *
 */
public interface InteractiveServ {
	public String getAreaTree(Map<String,Object> map) throws Exception;
	public String getSystemTree(Map<String,Object> map) throws Exception;
	public String getOrgTree(Map<String,Object> map) throws Exception;
	public String getJobTree(Map<String,Object> map) throws Exception;
	public String getStaffTree(Map<String,Object> map) throws Exception;
	public String getBizObjTree(Map<String,Object> map) throws Exception;
	/*
	 * 
	 * 查询服务[qrySystemForDispatchRuleMap]
	 * SELECT OUT_SYSTEM,OUT_SYSTEM_NAME,COMMENTS FROM PB_SYSTEM
	 * 
	 * 查询服务[qryBisiObjByCtagCode]
	 * SELECT PBO.ID,BUSINESS_OBJ_NAME,PBO.DTO_NAME
		FROM PB_BUSINESS_OBJ PBO,PB_BUSINESS_OBJ_CATALOG PBOC 
		WHERE PBO.STATE='10A' AND PBOC.STATE='10A' AND PBOC.BUSINESS_CATALOG_CODE=:BUSINESS_CATALOG_CODE
		AND PBOC.ID=PBO.BUSINESS_OBJ_CATALOG_ID
		ORDER BY PBO.ID
	 * 
	 * 查询人员页面：只查本组织的人员（不包括下属组织）查询出所有数据，后续页面可以加个查询处理（后台实际查的还是全部的）
	 * */
}
