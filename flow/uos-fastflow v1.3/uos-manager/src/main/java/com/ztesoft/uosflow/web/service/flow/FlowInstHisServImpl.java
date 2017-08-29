package com.ztesoft.uosflow.web.service.flow;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.core.FastflowTraceHis;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.model.WorkflowProcess;
import com.zterc.uos.fastflow.service.ActivityInstanceHisService;
import com.zterc.uos.fastflow.service.CommandQueueHisService;
import com.zterc.uos.fastflow.service.ProcessDefinitionService;
import com.zterc.uos.fastflow.service.ProcessInstanceHisService;
import com.zterc.uos.fastflow.service.WorkItemHisService;

@Service("FlowInstHisServ")
public class FlowInstHisServImpl implements FlowInstHisServ {
	private static Logger logger = LoggerFactory.getLogger(FlowInstHisServImpl.class);
	
	@Autowired
	private FastflowTraceHis flowFastflowTraceHis;

	@Autowired
	private ActivityInstanceHisService activityInstanceHisService;

	@Autowired
	private ProcessDefinitionService processDefinitionService;

	@Autowired
	private ProcessInstanceHisService processInstanceHisService;

	@Autowired
	private WorkItemHisService workItemHisService;
	
	@Autowired
	private CommandQueueHisService commandQueueHisService;
	
	
	/**
	 * 查询流程实例的xml
	 */
	public String qryProcessInstanceForTrace(Map<String, Object> map) {
		Long processInstanceId = LongHelper.valueOf(map
				.get("processInstanceId"));
		Boolean isHistory = Boolean.valueOf(map.get("isHistory").toString());
		return GsonHelper.toJson(flowFastflowTraceHis.qryProcessInstanceForTrace(
				processInstanceId, isHistory));
	}

	/**
	 * 查询流程定义的xml
	 */
	public String qryProcessDefineForTrace(Map<String, Object> map)
			throws SQLException, DocumentException, IOException {
		Long processInstanceId = LongHelper.valueOf(map
				.get("processInstanceId"));
		Boolean isHistory = Boolean.valueOf(map.get("isHistory").toString());
		return GsonHelper.toJson(flowFastflowTraceHis.qryProcessDefineForTrace(
				processInstanceId, isHistory));
	}

	@Override
	public String queryProcessInstancesHisByCond(Map<String, Object> map)
			throws Exception {
		/** 通过流程实例Id找流程实例对象 */
		PageDto pageDto = processInstanceHisService.queryProcessInstancesHisByCond(map);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

	@Override
	public String qryWorkItemByCond(Map<String, Object> map) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		/* 根据条件查询工作项 */
		PageDto pageDto  = workItemHisService.findWorkItemHisByCond(paramMap);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

	@Override
	public String qryProcessTacheByCond(Map<String, Object> map)
			throws Exception {
		String processDefineId = StringHelper.valueOf(map
				.get("processDefineId"));
		WorkflowProcess process = processDefinitionService
				.findWorkflowProcessById(processDefineId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String result = null;
		if (process != null) {
			/** 在流程定义中找到环节 */
			Collection<Activity> activities = process.getActivities();
			Iterator<Activity> iter = activities.iterator();
			while (iter.hasNext()) {
				Activity activity = iter.next();
				if ("Tache".equalsIgnoreCase(activity.getNodeType())) {
					Map<String, Object> tMap = new HashMap<String, Object>();
					tMap.put("id", activity.getTacheId());
					tMap.put("text", activity.getTacheName());
					tMap.put("tacheCode", activity.getTacheCode());
					tMap.put("type", 2);
					list.add(tMap);
				}
			}
		}
		result = GsonHelper.toJson(list);
		logger.debug("-----流程环节信息" + result);
		return result;
	}

	@Override
	public String qryActivityInstance(Map<String, Object> map) throws Exception {
		String activityInstanceId = StringHelper.valueOf(map
				.get("activityInstanceId"));
		ActivityInstanceDto dto = activityInstanceHisService
				.queryActivityInstance(activityInstanceId);
		return GsonHelper.toJson(dto);
	}

	@Override
	public String qryCommandMsgInfoByPid(Map<String, Object> map)
			throws Exception {
		/** 通过流程实例Id找流程消息内容 */
		PageDto pageDto = commandQueueHisService.qryCommandMsgInfoByPid(map);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

	@Override
	public String qryProcInstShadowForTrace(Map<String, Object> map) {
		Long processInstanceId = LongHelper.valueOf(map
				.get("processInstanceId"));
		Boolean isHistory = Boolean.valueOf(map.get("isHistory").toString());
		if(processInstanceId != null){
			return GsonHelper.toJson(flowFastflowTraceHis.qryProcInstShadowForTrace(
					processInstanceId, isHistory));
		}
		return GsonHelper.toJson("");
	}
}
