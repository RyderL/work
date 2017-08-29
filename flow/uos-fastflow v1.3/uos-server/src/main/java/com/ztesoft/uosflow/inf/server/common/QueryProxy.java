package com.ztesoft.uosflow.inf.server.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zterc.uos.base.cache.UosCacheClient;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.core.FastflowRunner;
import com.zterc.uos.fastflow.core.FastflowTrace;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.ProcessDefinitionDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.dto.specification.PackageCatalogDto;
import com.zterc.uos.fastflow.dto.specification.PackageDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.ProcessParamDefRelDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonConfigDto;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.model.Transition;
import com.zterc.uos.fastflow.model.WorkflowProcess;
import com.zterc.uos.fastflow.service.ActivityInstanceService;
import com.zterc.uos.fastflow.service.ProcessDefinitionService;
import com.zterc.uos.fastflow.service.ProcessInstanceService;
import com.zterc.uos.fastflow.service.ProcessPackageService;
import com.zterc.uos.fastflow.service.ProcessParamDefService;
import com.zterc.uos.fastflow.service.ReturnReasonService;
import com.zterc.uos.fastflow.service.TacheService;
import com.zterc.uos.fastflow.service.WorkItemService;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.cons.InfConstant;

@Component("queryProxy")
public class QueryProxy {
	private Logger logger = LoggerFactory.getLogger(QueryProxy.class);

	@Autowired
	private ProcessPackageService processPackageService;

	@Autowired
	private ProcessDefinitionService processDefinitionService;

	@Autowired
	private ProcessParamDefService processParamDefService;

	@Autowired
	private TacheService tacheService;
	
	@Autowired
	private ReturnReasonService returnReasonService;

	@Autowired
	private WorkItemService workItemService;

	@Autowired
	private ActivityInstanceService activityInstanceService;
	
	@Autowired
	private ProcessInstanceService processInstanceService;

	@Autowired
	private FastflowTrace fastflowTrace;
	
	@Autowired
	private UosCacheClient uosCacheClient;
	@Autowired
	private FastflowRunner fastflowRunner;

	public QueryProxy() {
	}

	public static QueryProxy getInstance() {
		return (QueryProxy) ApplicationContextProxy.getBean("queryProxy");
	}

	public String dealForJson(String requestJson) {

		Map<String, Object> rMap = new HashMap<String, Object>();
		try {
			// 解析命令
			Map<String, Object> requestMap = GsonHelper.toMap(requestJson);
			String commandCode = StringHelper.valueOf(requestMap
					.get(InfConstant.INF_COMMAND_CODE));
			String processInstanceId = StringHelper.valueOf(requestMap
					.get(InfConstant.INF_PROCESSINSTANCEID));
			Method method = this.getClass().getMethod(commandCode, Map.class);
			
			if (processInstanceId!=null&& FastflowConfig.isCacheModel) {
				if (logger.isInfoEnabled()) {
					logger.info("query接口:"+commandCode
							+ "--redis--getObject:[key:"
							+ processInstanceId + "]");
				}
				ProcessModel processModel = getCache(Long.valueOf(processInstanceId));
				ProcessLocalHolder.set(processModel);
			}
			
			String result = StringHelper.valueOf(method.invoke(this, requestMap));
			
			rMap.put(InfConstant.INF_DEAL_FLAG, InfConstant.INF_SUCCESS);
			rMap.put(InfConstant.INF_QUERY_RESULT, result);
			rMap.put(InfConstant.INF_DEAL_MSG, "接口调用成功");
		} catch (Exception e) {
			logger.error("-------反射调用方法异常", e);
			rMap.put(InfConstant.INF_DEAL_FLAG, InfConstant.INF_FAILED);
			rMap.put(InfConstant.INF_DEAL_MSG, "接口调用异常，原因：" + e.getMessage());
		} finally{
			ProcessLocalHolder.clear();
		}
		return GsonHelper.toJson(rMap);
	}
	
	public ProcessModel getCache(Long processInstanceId) {
		return uosCacheClient.getObject(ProcessModel.PROCESS_INSTANCE_MODEL
				+ processInstanceId, ProcessModel.class,
				Long.valueOf(processInstanceId));
	}


	/** 查询流程目录 */
	public String qryPackageCatalogs(Map<String, Object> params) {
		String systemCode = StringHelper.valueOf(params.get(InfConstant.INF_SYSTEM_CODE));

		// 根据areaId和systemCode获取流程目录部分
		PackageCatalogDto[] packageCatalogDtos = processPackageService
				.qryPackageCatalogByAreaIdAndSystemCode(null, systemCode);

		JsonArray list = new JsonArray();
		// 加载目录到树
		Map<String, JsonObject> catalogMap = new HashMap<String, JsonObject>();
		if (packageCatalogDtos != null && packageCatalogDtos.length > 0) {
			Map<String, JsonObject> parentMap = new HashMap<String, JsonObject>();
			for (int i = 0; i < packageCatalogDtos.length; i++) {
				PackageCatalogDto dto = packageCatalogDtos[i];
				String pathCode = dto.getPathCode();
				if (pathCode.lastIndexOf(".") >= 0) { // 不是最高层地区
					pathCode = pathCode.substring(0, pathCode.lastIndexOf("."));
				}
				// 给节点赋值
				JsonObject catalog = dto.getTreeJsonObject();
				// 节点的层次
				if (parentMap.containsKey(pathCode)) {
					JsonObject parent = (JsonObject) parentMap.get(pathCode);
					JsonArray children = parent.getAsJsonArray("children");
					parent.addProperty("state", "closed");
					if (children == null) {
						children = new JsonArray();
						parent.add("children", children);
					}
					children.add(catalog);
				} else {
					list.add(catalog);
				}
				parentMap.put(dto.getPathCode(), catalog);
				catalogMap.put(dto.getCatalogId().toString(), catalog);
			}
		}
		return GsonHelper.toJson(list);
	}

	/** 查询指定目录下的所有流程模版和模版的所有版本 */
	public String qryPackagesByCatalogId(Map<String, Object> params) {
		String catalogId = StringHelper.valueOf(params.get(InfConstant.INF_CATALOGID));

		// 流程目录获取流程模板部分
		PackageDto[] packageDtos = processPackageService.qryPackageByPackageCatalogIds(catalogId);

		// 根据流程模板获取流程版本部分
		ProcessDefinitionDto[] processDefinitionDtos = null;
		if (packageDtos != null && packageDtos.length > 0) {
			StringBuffer packageIds = new StringBuffer();
			for (int i = 0; i < packageDtos.length; i++) {
				if (i != packageDtos.length - 1) {
					packageIds.append(packageDtos[i].getPackageId() + ",");
				} else {
					packageIds.append(packageDtos[i].getPackageId());
				}
			}
			processDefinitionDtos = processDefinitionService
					.qryProcessDefineByPackageIds(packageIds.toString());
		}

		JsonArray list = new JsonArray();
		// 加载模板到树
		Map<String, JsonObject> packageMap = new HashMap<String, JsonObject>();
		if (packageDtos != null && packageDtos.length > 0) {
			for (int i = 0; i < packageDtos.length; i++) {
				PackageDto dto = packageDtos[i];
				// 给节点赋值
				JsonObject pack = dto.getTreeJsonObject();
				// 节点的层次
				packageMap.put(dto.getPackageId().toString(), pack);

				list.add(pack);
			}
		}
		// 加载版本到树
		if (processDefinitionDtos != null && processDefinitionDtos.length > 0) {
			for (int i = 0; i < processDefinitionDtos.length; i++) {
				ProcessDefinitionDto dto = processDefinitionDtos[i];
				// 给节点赋值
				JsonObject def = dto.getTreeJsonObject();
				// 节点的层次
				if (packageMap.containsKey(dto.getPackageId().toString())) {
					JsonObject parent = (JsonObject) packageMap.get(dto
							.getPackageId().toString());
					JsonArray children = parent.getAsJsonArray("children");
					parent.addProperty("state", "closed");
					if (children == null) {
						children = new JsonArray();
						parent.add("children", children);
					}
					children.add(def);
				}
			}
		}
		return GsonHelper.toJson(list);
	}

	/** 查询流程定义中配置的流程参数 */
	public String qryFlowParamDefRels(Map<String, Object> params) {
		ProcessParamDefRelDto dto = new ProcessParamDefRelDto();
		String flowPackageCode = StringHelper
				.valueOf(params.get(InfConstant.INF_FLOWPACKAGECODE));
		String type = params.get(InfConstant.INF_TYPE) == null ? "FLOW" : StringHelper
				.valueOf(params.get(InfConstant.INF_TYPE));
		String tacheCode = params.get(InfConstant.INF_TACHE_CODE) == null ? "-1"
				: StringHelper.valueOf(params.get(InfConstant.INF_TACHE_CODE));
		ProcessDefinitionDto processDefinitionDto = processDefinitionService.qryProcessDefinitionByCode(flowPackageCode);
		dto.setPackageDefineId(processDefinitionDto.getPackageDefineId());
		dto.setType(type);
		dto.setTacheCode(tacheCode);
		return GsonHelper.toJson(processParamDefService
				.qryProcessParamDefRels(dto));
	}

	/** 查询环节目录 */
	public String qryTacheCatalog(Map<String, Object> params) {
		params.put("systemCode", params.get(InfConstant.INF_SYSTEM_CODE));
		return tacheService.qryTacheCatalogTree(params);
	}

	/** 查询环节 */
	public String qryTaches(Map<String, Object> params) {
		if (params.get(InfConstant.INF_TACHE_ID) != null) {
			params.put("id", params.get(InfConstant.INF_TACHE_ID));
		}
		if (params.get(InfConstant.INF_TACHE_CATALOG_ID) != null) {
			params.put("tacheCatalogId", params.get(InfConstant.INF_TACHE_CATALOG_ID));
		}
		if (params.get(InfConstant.INF_TACHE_NAME) != null) {
			params.put("tacheName", params.get(InfConstant.INF_TACHE_NAME));
		}
		if (params.get(InfConstant.INF_TACHE_CODE) != null) {
			params.put("tacheCode", params.get(InfConstant.INF_TACHE_CODE));
		}
		if (params.get(InfConstant.INF_STATE) != null) {
			params.put("state", params.get(InfConstant.INF_STATE));
		}
		if (params.get(InfConstant.INF_TACHE_TYPE) != null) {
			params.put("tacheType", params.get(InfConstant.INF_TACHE_TYPE));
		}
		if (params.get(InfConstant.INF_PAGEINDEX) != null) {
			params.put("page", params.get(InfConstant.INF_PAGEINDEX));
		}
		if (params.get(InfConstant.INF_PAGESIZE) != null) {
			params.put("pageSize", params.get(InfConstant.INF_PAGESIZE));
		}
		PageDto pageDto = tacheService.qryTaches(params);
		return GsonHelper.toJson(pageDto);
	}
	
	/**
	 * 流程定义xml查询
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String qryProcessDefineXml(Map<String, Object> params)
			throws Exception {
		String flowPackageCode = (String) params.get(InfConstant.INF_FLOWPACKAGECODE);
		try {
			String xpdlContent = processDefinitionService
					.queryProcessDefinitionByCode(flowPackageCode)
					.getXpdlContent();
			logger.debug("=====>>>queryProcessDefefine： "
					+ "查询流程定义命令已发送，flowPackageCode：" + flowPackageCode);
			return GsonHelper.toJson(xpdlContent);
		} catch (Exception e) {
			logger.error("----查询流程定义失败：" + e.getMessage(), e);
			throw e;
		}
	}

	/** 流程预回单——获取下一个可达环节 */
	public String qryReachableTaches(Map<String, Object> params)
			throws Exception {
		String processInstanceId = StringHelper.valueOf(params.get(InfConstant.INF_PROCESSINSTANCEID));
		String workItemId = StringHelper.valueOf(params.get(InfConstant.INF_WORKITEMID));
		/* 获得流程定义，工作项，活动，活动实例 */
		ProcessInstanceDto processInstanceDto = processInstanceService.queryProcessInstance(processInstanceId);
		if(processInstanceDto==null||processInstanceDto.getProcessDefineId()==null){
			throw new Exception("流程实例不存在：[processInstanceId="+processInstanceId+"]");
		}
		WorkflowProcess process = processDefinitionService.findWorkflowProcessById(processInstanceDto.getProcessDefineId());
		WorkItemDto workItem = workItemService.queryWorkItem(workItemId);
		Activity activity = process.getActivityById(workItem
				.getActivityDefinitionId());
		ActivityInstanceDto activityInstance = activityInstanceService
				.queryActivityInstance(StringHelper.valueOf(workItem
						.getActivityInstanceId()));

		/* 取得当前流程里面所有的成对节点（分支合并） */
		List<Map<String,String>> activityList = new ArrayList<Map<String,String>>();

		if (!activityInstance.getDirection().trim()
				.equalsIgnoreCase(ActivityInstanceDto.REVERSE_DIRECTION)) {
			/* 正向 */
			List<String> activityIdList = new ArrayList<String>();
			activityList = getNextTache(activity,activityList,activityIdList);
		} else {
			/* 反向，暂时无需求 */
		}
		return GsonHelper.toJson(activityList);
	}

	private List<Map<String, String>> getNextTache(Activity activity,
			List<Map<String, String>> activityList, List<String> activityIdList) {
		/* 取出源活动的所有出边 */
		Iterator<Transition> transitions = activity
				.getEfferentTransitions().iterator();
		while (transitions.hasNext()) {
			Transition currentTransition = transitions.next();
			/*
			 * 如果当前节点为分支节点，则分支节点的目标节点中，如果有一个不满足，
			 * 需要把整条线上的节点在关系矩阵中的有效性设置为False
			 */
			Activity toActivity = currentTransition.getToActivity();
			/* 计算条件 */
			if("Tache".equals(toActivity.getNodeType())){
				if(activityIdList.contains(toActivity.getId())){
					continue;
				}
				Map<String,String> activityMap = new HashMap<String,String>();
				activityMap.put("id", toActivity.getId());
				activityMap.put("name", toActivity.getName());
				activityMap.put("nodeType", toActivity.getNodeType());
				activityMap.put("tacheId", toActivity.getTacheId());
				activityMap.put("tacheCode", toActivity.getTacheCode());
				activityMap.put("tacheName", toActivity.getTacheName());
				activityMap.put("branchIndex", toActivity.getBranchIndex());
				activityMap.put("nodeIndex", toActivity.getNodeIndex());
				activityMap.put("numOfBranch", StringHelper.valueOf(toActivity.getNumOfBranch()));
				activityMap.put("joinType", toActivity.getJoinType().toString());
				activityMap.put("order", StringHelper.valueOf(toActivity.getOrder()));
				activityList.add(activityMap);
				activityIdList.add(toActivity.getId());
			}else{
				getNextTache(toActivity, activityList,activityIdList);
			}
		}
		return activityList;
	}

	/* 计算线条条件 */
//	private boolean calculateCondition(String processInstanceId,
//			Condition condition, WorkflowProcess process, String tacheCode)
//			throws Exception {
//		String con = null;
//		boolean t = true;
//		if (condition != null) {
//			con = ((Xpression) condition.getXpressions().get(0)).getValue()
//					.trim();
//			if (con == null || con.length() == 0) {
//				return true;
//			}
//			/* 计算条件表达式-解析组件 */
//			while (con.indexOf('{') != -1) {
//				int startIndex = con.indexOf('{') + 1;
//				int endIndex = con.indexOf('}', startIndex);
//				String conditionStr = con.substring(startIndex, endIndex);
//				try {
//					Boolean result = false;
//					String paramName = conditionStr.substring(
//							conditionStr.indexOf("$") + 1,
//							conditionStr.lastIndexOf("$"));
//					String conditionVal = conditionStr.substring(
//							conditionStr.indexOf("=") + 1,
//							conditionStr.length());
//					String paramVal = processParamService.queryProcessParam(
//							LongHelper.valueOf(processInstanceId), paramName);
//					if (paramVal != null
//							&& conditionVal.equalsIgnoreCase(paramVal)) {
//						result = true;
//					}
//
//					String value = result.toString().trim();
//					/** 字符串替换 */
//					con = StringUtils.replace(con, "{" + conditionStr + "}",
//							value);
//				} catch (Exception ex) {
//					logger.error("计算条件失败:" + ex.getMessage() + ",原因:"
//							+ ex.getCause());
//					throw ex;
//				}
//			}
//			con = con.replaceAll("与", " && ");
//			con = con.replaceAll("或", " || ");
//			/** 计算条件表达式-用java自带的JavaScript计算复杂逻辑运算 */
//			boolean result = true;
//			try {
//				ScriptEngine jse = new ScriptEngineManager()
//						.getEngineByName("JavaScript");
//				String mutilResult = String.valueOf(jse.eval(con));
//				result = mutilResult.equals("true");
//			} catch (ScriptException e) {
//				logger.error("JavaScript计算异常：" + e.getMessage());
//			}
//			t = condition == null || con.trim().length() == 0 || result;
//		}
//		return t;
//	}


	/**
	 * 流程实例xml查询
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String qryProcessInstanceXml(Map<String, Object> params)
			throws Exception {
		Boolean isHistory = false;
		Long processInstanceId = LongHelper.valueOf(params.get(InfConstant.INF_PROCESSINSTANCEID));
		String isHistoryStr = StringHelper.valueOf(params.get(InfConstant.INF_ISHISTORY));
		try {
			if("true".equals(isHistoryStr)){
				isHistory = true;
			}
			String xpdlContent = fastflowTrace.qryProcessInstanceForTrace(
					processInstanceId, isHistory);
			logger.debug("=====>>>queryProcessInstance： "
					+ "查询流程实例命令已发送，processInstanceId：" + processInstanceId);
			return GsonHelper.toJson(xpdlContent);
		} catch (Exception e) {
			logger.error("----查询流程实例失败：" + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 查询流程异常原因(数据库表)
	 * modify by bobping 此查询命令改用流程模板编码查询
	 * 
	 * @param commandDto
	 * @return
	 * @throws Exception
	 */
	public String qryReturnReasonConfigs(Map<String,Object> params) throws Exception {
		String processInstanceId = StringHelper.valueOf(params.get(InfConstant.INF_PROCESSINSTANCEID));
		String workItemId = StringHelper.valueOf(params.get(InfConstant.INF_WORKITEMID));
		try {
			// 根据工作项查询所需要的信息
			ProcessInstanceDto processInstanceDto = processInstanceService.queryProcessInstance(processInstanceId);
			WorkItemDto workItemDto = workItemService.queryWorkItem(workItemId);
			Long tacheId = workItemDto==null?null:workItemDto.getTacheId();
			
			// 查询流程异常原因
			//modify by bobping
			PageDto pageDto = returnReasonService.qryReturnReasonConfigs(processInstanceDto.getProcessDefineCode());
			List<?> rows = pageDto.getRows();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (rows != null) {
				for (int i = 0, j = rows.size(); i < j; i++) {
					ReturnReasonConfigDto dto = (ReturnReasonConfigDto) rows.get(i);
					if(tacheId!=null){
						if(dto.getTacheId().longValue() == tacheId.longValue()){
							Long targetTacheId = dto.getTargetTacheId();
							Boolean isCanRollBack = fastflowRunner.isCanRollBack(targetTacheId,processInstanceId);
							if(isCanRollBack){
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("REASON_CONFIG_ID", dto.getId());
								map.put("REASON_ID", dto.getReasonId());
								map.put("TACHE_ID", dto.getTacheId());
								map.put("TARGET_TACHE_ID", dto.getTargetTacheId());
								map.put("PACKAGEDEFINEID", dto.getPackageDefineId());
								map.put("AREA_ID", dto.getAreaId());
								map.put("REASON_CODE", dto.getReasonCode());
								map.put("RETURN_REASON_NAME", dto.getReasonName());
								map.put("REASON_TYPE", dto.getReasonType());
								map.put("TACHE_NAME", dto.getTacheName());
								map.put("TARGET_TACHE_NAME", dto.getTargetTacheName());
								map.put("TACHE_CODE", dto.getTacheCode());
								map.put("TARGET_TACHE_CODE", dto.getTargetTacheCode());
								map.put("PACKAGEDEFINECODE", dto.getPackageDefineCode());
								list.add(map);
							}
						}
					}else{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("REASON_CONFIG_ID", dto.getId());
						map.put("REASON_ID", dto.getReasonId());
						map.put("TACHE_ID", dto.getTacheId());
						map.put("TARGET_TACHE_ID", dto.getTargetTacheId());
						map.put("PACKAGEDEFINEID", dto.getPackageDefineId());
						map.put("AREA_ID", dto.getAreaId());
						map.put("REASON_CODE", dto.getReasonCode());
						map.put("RETURN_REASON_NAME", dto.getReasonName());
						map.put("REASON_TYPE", dto.getReasonType());
						map.put("TACHE_NAME", dto.getTacheName());
						map.put("TARGET_TACHE_NAME", dto.getTargetTacheName());
						map.put("TACHE_CODE", dto.getTacheCode());
						map.put("TARGET_TACHE_CODE", dto.getTargetTacheCode());
						map.put("PACKAGEDEFINECODE", dto.getPackageDefineCode());
						list.add(map);
					}
				}
			}
			logger.debug("=====>>>qryReturnReasonConfigs： "
					+ "查询流程异常原因命令已发送，processInstanceId：" + processInstanceId
					+ ",packageDefineId:" + processInstanceDto.getProcessDefineId() + ",查到的异常原因有"
					+ list.size());
			return GsonHelper.toJson(list);
		} catch (Exception e) {
			logger.error("----查询流程异常原因失败：processInstanceId=" + processInstanceId, e);
			throw e;
		}
	}
	
	/**
	 * 查询流程定义列表（无目录）
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String qryProcessDefList(Map<String,Object> params) throws Exception {
		String flowPackageCode = StringHelper.valueOf(params.get(InfConstant.INF_FLOWPACKAGECODE));
		String flowPackageName = StringHelper.valueOf(params.get(InfConstant.INF_FLOWPACKAGENAME));
		String pageSize = StringHelper.valueOf(params.get(InfConstant.INF_PAGESIZE));
		String pageIndex = StringHelper.valueOf(params.get(InfConstant.INF_PAGEINDEX));
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put(InfConstant.INF_FLOWPACKAGECODE, flowPackageCode);
		paramMap.put(InfConstant.INF_FLOWPACKAGENAME, flowPackageName);
		paramMap.put(InfConstant.INF_PAGESIZE, pageSize);
		paramMap.put(InfConstant.INF_PAGEINDEX, pageIndex);
		try {
			Map<String,Object> retMap = processDefinitionService.qryAllProcessDefinitionByCond(paramMap);
			return GsonHelper.toJson(retMap);
		} catch (Exception e) {
			logger.error("----查询流程定义列表失败", e);
			throw e;
		}
	}

	/**
	 * 查询异常原因目录
	 * @param params
	 * @return
	 */
	public String qryReturnReasonCatalogTree(Map<String, Object> params) {
		String systemCode = StringHelper.valueOf(params.get(InfConstant.INF_SYSTEM_CODE));
		String result= returnReasonService.qryReturnReasonCatalogTree(systemCode);
		return result;
	}

	/** 查询异常原因列表 
	 * @param params
	 * @return
	 * */
	public String qryReturnReasons(Map<String, Object> params) {
		if (params.get(InfConstant.INF_REASON_ID) != null) {
			params.put("id", params.get(InfConstant.INF_REASON_ID));
		}
		if (params.get(InfConstant.INF_REASON_CATALOG_ID) != null) {
			params.put("reasonCatalogId", params.get(InfConstant.INF_REASON_CATALOG_ID));
		}
		if (params.get(InfConstant.INF_REASON_CODE) != null) {
			params.put("reasonCode", params.get(InfConstant.INF_REASON_CODE));
		}
		if (params.get(InfConstant.INF_REASON_TYPE) != null) {
			params.put("reasonType", params.get(InfConstant.INF_REASON_TYPE));
		}
		if (params.get(InfConstant.INF_STATE) != null) {
			params.put("state", params.get(InfConstant.INF_STATE));
		}
		if (params.get(InfConstant.INF_REASON_NAME) != null) {
			params.put("returnReasonName", params.get(InfConstant.INF_REASON_NAME));
		}
		if (params.get(InfConstant.INF_PAGEINDEX) != null) {
			params.put("page", params.get(InfConstant.INF_PAGEINDEX));
		}
		if (params.get(InfConstant.INF_PAGESIZE) != null) {
			params.put("pageSize", params.get(InfConstant.INF_PAGESIZE));
		}
		PageDto pageDto = returnReasonService.qryReturnReasons(params);
		String result= GsonHelper.toJson(pageDto);
		return result;
	}

	/**
	 * 流程定义xml查询for流程图展示页面
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String qryProcessDefineXmlForPage(Map<String, Object> params)
			throws Exception {
		String xpdlContent = "";
		String processInstanceId = StringHelper.valueOf(params.get(InfConstant.INF_PROCESSINSTANCEID));
		String processDefineCode = StringHelper.valueOf(params.get(InfConstant.INF_FLOWPACKAGECODE));
		try {
			//如果传入的是流程模板编码，则根据流程模板编码查询，否则还是根据流程实例id查询
			if(processDefineCode != null  && !"".equals(processDefineCode)){
				xpdlContent = fastflowTrace.qryProcessDefineForPage(
						processDefineCode);
				logger.debug("=====>>>qryProcessDefineXmlForPage： "
						+ "查询流程定义4页面命令已发送，processDefineCode：" + processDefineCode);
			}else{
				xpdlContent = fastflowTrace.qryProcessDefineForTrace(
						Long.valueOf(processInstanceId), false);
				logger.debug("=====>>>qryProcessDefineXmlForPage： "
						+ "查询流程定义4页面命令已发送，processInstanceId：" + processInstanceId);
			}
			return GsonHelper.toJson(xpdlContent);
		} catch (Exception e) {
			logger.error("----查询流程定义4页面失败：" + e.getMessage(), e);
			throw e;
		}
	}
	
    /**
     * 查询流程状态(数据库表)
     * 
     * @param commandDto
     * @return
     * @throws Exception
     */
    public String qryProcessInstanceState(Map<String,Object> params) throws Exception {
        String processInstanceId = StringHelper.valueOf(params.get(InfConstant.INF_PROCESSINSTANCEID));
        try {
            // 根据流程实例ID查询所流程实例信息
            ProcessInstanceDto processInstanceDto = processInstanceService.queryProcessInstance(processInstanceId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("STATE", processInstanceDto.getState());
            logger.debug("=====>>>qryProcessInstanceState： "
                    + "查询流程状态命令已发送，processInstanceId：" + processInstanceId +
                    ",查询到的流程状态是"+map);
            return GsonHelper.toJson(map);
        } catch (Exception e) {
            logger.error("----查询流程状态失败：processInstanceId=" + processInstanceId, e);
            throw e;
        }
    }
    
    /**
     * 查询代办工单
     * @param params
     * @return
     */
    public String qryUndoWorkItem(Map<String,Object> params){
//    	String processInstanceId = StringHelper.valueOf(params.get(InfConstant.INF_PROCESSINSTANCEID));
    	if (params.get(InfConstant.INF_PROCESSINSTANCEID) != null) {
			params.put("processInstanceId", params.get(InfConstant.INF_PROCESSINSTANCEID));
		}
    	List<WorkItemDto> workItemDtos = workItemService.queryUndoWorkItems(params);
    	String result = GsonHelper.toJson(workItemDtos);
		return result;
    }
    
    /**
     * 根据流程模板编码，获取流程下面所有的环节
     * @param params
     * @return
     * @throws Exception 
     */
    public String qryAllTacheByProcess(Map<String,Object> params) throws Exception{
    	String flowPackageCode = StringHelper.valueOf(params.get(InfConstant.INF_FLOWPACKAGECODE));
		/* 获得流程定义*/
    	ProcessDefinitionDto processDefinitionDto = processDefinitionService.qryProcessDefinitionByCode(flowPackageCode);
    	if(processDefinitionDto == null){
    		throw new Exception("流程模板不存在：[flowPackageCode="+flowPackageCode+"]");
    	}
    	WorkflowProcess process = processDefinitionService.findWorkflowProcessById(StringHelper.valueOf(processDefinitionDto.getPackageDefineId()));

		List<Map<String,String>> activityList = new ArrayList<Map<String,String>>();
		Collection<Activity> activities = process.getActivities();
		for(Activity activity:activities){
			if("Tache".equals(activity.getNodeType())){
				Map<String,String> activityMap = new HashMap<String,String>();
				activityMap.put("id", activity.getId());
				activityMap.put("name", activity.getName());
				activityMap.put("nodeType", activity.getNodeType());
				activityMap.put("tacheId", activity.getTacheId());
				activityMap.put("tacheCode", activity.getTacheCode());
				activityMap.put("tacheName", activity.getTacheName());
				activityMap.put("branchIndex", activity.getBranchIndex());
				activityMap.put("nodeIndex", activity.getNodeIndex());
				activityMap.put("numOfBranch", StringHelper.valueOf(activity.getNumOfBranch()));
				activityMap.put("joinType", activity.getJoinType().toString());
				activityMap.put("order", StringHelper.valueOf(activity.getOrder()));
				activityList.add(activityMap);
			}
		}
		return GsonHelper.toJson(activityList);
    }
    
    /**
     * 查询能回退的工作项
     * @param params
     * @return
     */
    public String qryCanRollbackWorkItems(Map<String,Object> params){
    	List<WorkItemDto> workItems = new ArrayList<WorkItemDto>();
    	String processInstanceId = MapUtils.getString(params, InfConstant.INF_PROCESSINSTANCEID);
		String areaId =  MapUtils.getString(params, InfConstant.INF_AREA_CODE);
		String workItemId =  MapUtils.getString(params, InfConstant.INF_WORKITEMID);
		String reasonConfigId = MapUtils.getString(params, InfConstant.INF_REASON_CFG_ID);
		String targetWorkItemId = MapUtils.getString(params, InfConstant.INF_TARGET_WORKITEM_ID);
		String reasonType =  MapUtils.getString(params, InfConstant.INF_REASON_TYPE);
		String reasonCode = MapUtils.getString(params, InfConstant.INF_REASON_CODE);
		if(!StringHelper.isEmpty(targetWorkItemId)){
			workItems = fastflowRunner.findCanRollBackWorkItemsByTarget(processInstanceId, workItemId, targetWorkItemId, reasonType, false, reasonConfigId);
		}else{
			if(workItemId != null && !"".equals(workItemId)){
				workItems = fastflowRunner.findCanRollBackWorkItems(processInstanceId, workItemId, reasonCode, reasonConfigId, false, areaId);
			}else{
				workItems = fastflowRunner.findCanRollBackWorkItems(processInstanceId);
			}
		}
		String result = GsonHelper.toJson(workItems);
		return result;
    }
    
    /**
     * 判断能否退单
     * @param params
     * @return
     */
    public String qryIsCanDisableWorkItem(Map<String,Object> params){
    	String result = "false";
		String areaId =  MapUtils.getString(params, InfConstant.INF_AREA_CODE);
		String workItemId =  MapUtils.getString(params, InfConstant.INF_WORKITEMID);
		String reasonConfigId = MapUtils.getString(params, InfConstant.INF_REASON_CFG_ID);
		String targetWorkItemId = MapUtils.getString(params, InfConstant.INF_TARGET_WORKITEM_ID);
		String reasonCode = MapUtils.getString(params, InfConstant.INF_REASON_CODE);
		Boolean isCanDisableWorkItem = false;
		if(!StringHelper.isEmpty(targetWorkItemId)){
			isCanDisableWorkItem = fastflowRunner.isCanDisableWorkItem(workItemId,targetWorkItemId);
		}else{
			isCanDisableWorkItem = fastflowRunner.isCanDisableWorkItem(workItemId, reasonCode,reasonConfigId,false,areaId);
		}
		if(isCanDisableWorkItem){
			result = "true";
		}
    	return GsonHelper.toJson(result);
    }
}
