package com.zterc.uos.fastflow.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.zterc.uos.base.helper.StaticCacheHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.process.ProcessDefinitionDAO;
import com.zterc.uos.fastflow.dao.process.ProcessParamDefDAO;
import com.zterc.uos.fastflow.dto.process.ProcessDefinitionDto;
import com.zterc.uos.fastflow.dto.process.ProcessParamDefDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.ProcessParamDefRelDto;
import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.model.Transition;
import com.zterc.uos.fastflow.model.condition.Condition;
import com.zterc.uos.fastflow.model.condition.Xpression;

/**
 * （定义）流程变量定义操作类
 * 
 * @author gong.yi
 *
 */
public class ProcessParamDefService {

	public static final String PROCESS_PARAM_DEF_CACHE = "PROCESS_PARAM_DEF_CACHE";
	public static final String PROCESS_PARAM_TACHE_CACHE = "PROCESS_PARAM_TACHE_CACHE";

	private ProcessParamDefDAO processParamDefDAO;
	@Autowired
	private ProcessDefinitionDAO processDefinitionDAO;

	public void setProcessParamDefDAO(ProcessParamDefDAO processParamDefDAO) {
		this.processParamDefDAO = processParamDefDAO;
	}

	/**
	 * （缓存）根据流程定义id查找流程变量定义列表
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public List<ProcessParamDefDto> queryParamDefsByDefId(
			String processDefinitionId) {
		// 先从缓存里面取
		List<ProcessParamDefDto> processParamDefDtos = getCachedParamDefs(processDefinitionId);

		// 缓存里面存在，则返回
		if (processParamDefDtos == null && !FastflowConfig.loadStaticCache){
			// 缓存里面不存在，则从数据库里面查询
			processParamDefDtos = processParamDefDAO
					.queryParamDefsByDefId(processDefinitionId);

			// 建立缓存
			setCachedParamDefs(processDefinitionId, processParamDefDtos);
		}

		return processParamDefDtos;
	}

	/**
	 * （缓存）根据流程变量定义编码查找流程变量定义信息
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public ProcessParamDefDto queryParamDefByCode(String code) {
		// 先从缓存里面取
		ProcessParamDefDto processParamDefDto = getCachedParamDefCode(code);

		if (processParamDefDto == null
				&& !FastflowConfig.loadStaticCache) {
			// 缓存里面不存在，则从数据库里面查询
			processParamDefDto = processParamDefDAO.queryParamDefByCode(code);

			// 建立缓存
			setCachedParamDefCode(code, processParamDefDto);
		}

		// 缓存里面存在，则返回
		return processParamDefDto;
	}

	@SuppressWarnings("all")
	private ProcessParamDefDto getCachedParamDefCode(String code) {
		return (ProcessParamDefDto) StaticCacheHelper.get(
				PROCESS_PARAM_DEF_CACHE, "CODE_" + code);
	}

	private void setCachedParamDefCode(String code,
			ProcessParamDefDto processParamDefDto) {
		StaticCacheHelper.set(PROCESS_PARAM_DEF_CACHE, "CODE_" + code,
				processParamDefDto);
	}

	/**
	 * （缓存）根据流程定义id、类型和环节编码查找需要转发给业务系统的流程变量列表
	 * 
	 * @param processDefineId
	 * @param tacheCode
	 * @return
	 */
	public Map<String, String> qryProInsTacheParam(String processDefineId,
			String tacheCode) {
		List<Map<String, Object>> list = getCachedTacheParamDefs(
				processDefineId, tacheCode);
		Map<String, String> map = new HashMap<String, String>();
		//只查询缓存中的数据，改了静态数据必须刷到缓存中
		if (list == null) {
			if(FastflowConfig.loadStaticCache){
				// 缓存中没取到环节相关参数，取缓存中的公共可变参数返回 mod by che.zi 20170322
				list = getCachedTacheParamDefs(processDefineId, "FLOW");
				if(list == null){
					//既没有环节相关参数，也没有公共可变参数，则直接返回
					return map;
				}
				//end 20170322
			}
			String qrySql = "SELECT code,value FROM  UOS_PROINSPARAM_DEF_REL WHERE ROUTE_ID=1 AND PACKAGEDEFINEID = ? AND ((TYPE = ? AND TACHE_CODE=?) OR IS_VARIABLE = 1) ";
			list = JDBCHelper.getJdbcTemplate().queryForList(qrySql,
					new Object[] { processDefineId, "TACHE", tacheCode });

			setCachedTacheParamDefs(processDefineId, tacheCode, list);
		}
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				map.put(StringHelper.valueOf(list.get(i).get("code")),
						StringHelper.valueOf(list.get(i).get("value")));
			}
		}
		
		return map;
	}

	/**
	 * 根据activityid 获取对应下一线条的流程参数
	 * @param activity
	 * @return
	 */
	public Map<String, String> qryProInsTacheParam(Activity activity){
		Map<String,String> paramMap = getCachedTacheParamDefs(activity.getId());
		if(paramMap == null){
			synchronized (activity.getId()) {
				if(paramMap == null){
					paramMap = new HashMap<String,String>();
					this.qryParam(activity,paramMap);
					setCachedTacheParamDefs(activity.getId(),paramMap);
				}
			}
		}
		return paramMap;
	}
	
	private void qryParam(Activity activity,Map<String,String> paramMap){

		Iterator<Transition> transitions = activity
				.getEfferentTransitions().iterator();
		while (transitions.hasNext()) {
			Transition currentTransition = transitions.next();
			/** 当前活动的下一条边进行处理逻辑 */
			Condition condition = currentTransition.getCondition();
			String con = null;
			if (condition != null) {
				con = ((Xpression) condition.getXpressions().get(0)).getValue()
						.trim();
				/** 计算条件表达式-解析组件 */
				while (con.indexOf('{') != -1) {
					int startIndex = con.indexOf('{') + 1;
					int endIndex = con.indexOf('}', startIndex);
					String conditionStr = con.substring(startIndex, endIndex);
					String paramName = conditionStr.substring(
							conditionStr.indexOf("$") + 1, conditionStr.lastIndexOf("$"));
					String conditionVal = conditionStr.substring(
							conditionStr.indexOf("=") + 1, conditionStr.length());
					paramMap.put(paramName, conditionVal);
					/** 字符串替换 */
					con = StringUtils.replace(con, "{" + conditionStr + "}","");
				}
			}
			Activity toActivity = currentTransition.getToActivity();
			if(!"Tache".equals(toActivity.getNodeType())){
				qryParam(toActivity, paramMap);
			}
		}
	}
	
	
	private void setCachedTacheParamDefs(String activityId, Map<String, String> paramMap) {
		StaticCacheHelper.set(PROCESS_PARAM_DEF_CACHE, activityId, paramMap);
	}

	@SuppressWarnings("all")
	private Map<String, String> getCachedTacheParamDefs(String activityId) {
		return (Map<String, String>) StaticCacheHelper.get(
				PROCESS_PARAM_DEF_CACHE, activityId);
	}

	@SuppressWarnings("all")
	private List<ProcessParamDefDto> getCachedParamDefs(
			String processDefinitionId) {
		return (List<ProcessParamDefDto>) StaticCacheHelper.get(
				PROCESS_PARAM_DEF_CACHE, processDefinitionId);
	}

	private void setCachedParamDefs(String processDefinitionId,
			List<ProcessParamDefDto> processParamDefDtos) {
		StaticCacheHelper.set(PROCESS_PARAM_DEF_CACHE,
				processDefinitionId.toString(), processParamDefDtos);
	}

	@SuppressWarnings("all")
	private List<Map<String, Object>> getCachedTacheParamDefs(
			String processDefinitionId, String tacheCode) {
		return (List<Map<String, Object>>) StaticCacheHelper.get(
				PROCESS_PARAM_DEF_CACHE, processDefinitionId + "-" + tacheCode);
	}

	private void setCachedTacheParamDefs(String processDefinitionId,
			String tacheCode, List<Map<String, Object>> list) {
		StaticCacheHelper.set(PROCESS_PARAM_DEF_CACHE, processDefinitionId
				+ "-" + tacheCode, list);
	}

	public PageDto qryProcessParamDefs(String systemCode) {
		return processParamDefDAO.qryProcessParamDefs(systemCode);
	}

	public void addProcessParamDef(ProcessParamDefDto dto) {
		processParamDefDAO.addProcessParamDef(dto);
	}

	public void modProcessParamDef(ProcessParamDefDto dto) {
		processParamDefDAO.modProcessParamDef(dto);
	}

	public void delProcessParamDef(ProcessParamDefDto dto) {
		processParamDefDAO.delProcessParamDef(dto);
	}
	
	public void addBatchProcessParamDefRel(List<ProcessParamDefRelDto> dtos) {
		processParamDefDAO.addBatchProcessParamDefRel(dtos);
	}

	public void updateBatchProcessParamDefRel(List<ProcessParamDefRelDto> dtos) {
		processParamDefDAO.updateBatchProcessParamDefRel(dtos);
	}

	public void delProcessParamDefRel(ProcessParamDefRelDto dto) {
		processParamDefDAO.delProcessParamDefRel(dto);
	}
	
	public void delProcessParamDefRelNoCode(ProcessParamDefRelDto dto) {
		processParamDefDAO.delProcessParamDefRelNoCode(dto);
	}
	
	public PageDto qryProcessParamDefRels(ProcessParamDefRelDto dto) {
		return processParamDefDAO.qryProcessParamDefRels(dto);
	}

	public boolean isSendParam(String key) {
		ProcessParamDefDto dto = this.queryParamDefByCode(key);
		if (dto == null) {
			return false;
		} else {
			if (dto.getType() == ProcessParamDefDto.TYPE_SEND) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isExistRela(String code){
		return processParamDefDAO.isExistRela(code);
	}
	
	public void loadAllProcessParamDef(){
		List<ProcessParamDefDto> list = processParamDefDAO.qryProcessParamDefs();
		for(ProcessParamDefDto dto:list){
			setCachedParamDefCode(dto.getCode(), dto);
		}

		ProcessDefinitionDto[] processDefinitionDtos = processDefinitionDAO.qryProcessDefineByPackageIds(null);
		for(ProcessDefinitionDto processDefinitionDto:processDefinitionDtos){
			String packageDefineId = processDefinitionDto.getPackageDefineId().toString();
			List<ProcessParamDefDto> processParamDefDtos = processParamDefDAO.queryParamDefsByDefId(packageDefineId);
			setCachedParamDefs(packageDefineId, processParamDefDtos);
			List<ProcessParamDefRelDto> tacheCodes = processParamDefDAO.qryDistinctTacheCodeByDefId(packageDefineId);
			for(ProcessParamDefRelDto dto:tacheCodes){
				String tacheCode = dto.getTacheCode();
				String qrySql = "SELECT code,value FROM  UOS_PROINSPARAM_DEF_REL WHERE ROUTE_ID=1 AND PACKAGEDEFINEID = ? AND ((TYPE = ? AND TACHE_CODE=?) OR IS_VARIABLE = 1) ";
				List<Map<String, Object>> processParamDefList = JDBCHelper.getJdbcTemplate().queryForList(qrySql,
						new Object[] { packageDefineId, "TACHE", tacheCode });
				setCachedTacheParamDefs(packageDefineId, tacheCode, processParamDefList);
			}
			// 单独将公共可变参数也放一个缓存，防止有的环节没配参数 没有缓存到对应的可变参数 add by che.zi 20170322
			String qrySql = "SELECT code,value FROM  UOS_PROINSPARAM_DEF_REL WHERE ROUTE_ID=1 AND PACKAGEDEFINEID = ? AND TYPE = ? AND IS_VARIABLE = 1 ";
			List<Map<String, Object>> processParamDefList = JDBCHelper.getJdbcTemplate().queryForList(qrySql,
					new Object[] { packageDefineId, "FLOW"});
			setCachedTacheParamDefs(packageDefineId, "FLOW", processParamDefList);
			//end 20170322
		}
	}

	/**
	 * 根据流程模板查询所有流程参数
	 * @param string
	 * @return
	 */
	public List<ProcessParamDefRelDto> queryParamDefRelsByDefId(String processDefineId) {
		return processParamDefDAO.qryProcessParamDefRelsByDefId(processDefineId);
	}
}
