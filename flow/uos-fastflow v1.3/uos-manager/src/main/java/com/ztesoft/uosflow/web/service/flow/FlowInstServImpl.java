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
import com.zterc.uos.fastflow.core.FastflowTrace;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.model.Transition;
import com.zterc.uos.fastflow.model.WorkflowProcess;
import com.zterc.uos.fastflow.service.ActivityInstanceService;
import com.zterc.uos.fastflow.service.CommandQueueService;
import com.zterc.uos.fastflow.service.ProcessDefinitionService;
import com.zterc.uos.fastflow.service.ProcessInstanceService;
import com.zterc.uos.fastflow.service.WorkItemService;

@Service("FlowInstServ")
public class FlowInstServImpl implements FlowInstServ {
	private static Logger logger = LoggerFactory.getLogger(FlowInstServImpl.class);
	
	@Autowired
	private FastflowTrace flowFastflowTrace;

	@Autowired
	private ActivityInstanceService activityInstanceService;

	@Autowired
	private ProcessDefinitionService processDefinitionService;

	@Autowired
	private ProcessInstanceService processInstanceService;

	@Autowired
	private WorkItemService workItemService;
	
	@Autowired
	private CommandQueueService commandQueueService;
	
	
	/**
	 * ��ѯ����ʵ����xml
	 */
	public String qryProcessInstanceForTrace(Map<String, Object> map) {
		Long processInstanceId = LongHelper.valueOf(map
				.get("processInstanceId"));
		Boolean isHistory = Boolean.valueOf(map.get("isHistory").toString());
		return GsonHelper.toJson(flowFastflowTrace.qryProcessInstanceForTrace(
				processInstanceId, isHistory));
	}

	/**
	 * ��ѯ���̶����xml
	 */
	public String qryProcessDefineForTrace(Map<String, Object> map)
			throws SQLException, DocumentException, IOException {
		Long processInstanceId = LongHelper.valueOf(map
				.get("processInstanceId"));
		Boolean isHistory = Boolean.valueOf(map.get("isHistory").toString());
		if(processInstanceId != null){
			return GsonHelper.toJson(flowFastflowTrace.qryProcessDefineForTrace(
					processInstanceId, isHistory));
		}
		return GsonHelper.toJson("");
	}

	@Override
	public String qryProcessInstanceByCond(Map<String, Object> map)
			throws Exception {
		/** ͨ������ʵ��Id������ʵ������ */
		PageDto pageDto = processInstanceService.queryProcessInstancesByCond(map);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

	@Override
	public String qryWorkItemByCond(Map<String, Object> map) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		/* ����������ѯ������ */
		PageDto pageDto  = workItemService.findWorkItemByCond(paramMap);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

	@Override
	public String qryProcessTacheByCond(Map<String, Object> map)
			throws Exception {
		String result = null;
		String processDefineId = StringHelper.valueOf(map
				.get("processDefineId"));
		if(processDefineId != null && !"".equals(processDefineId)){
			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processDefineId);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (process != null) {
				/** �����̶������ҵ����� */
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
		}
		logger.debug("-----���̻�����Ϣ" + result);
		return result;
	}

	/**
	 * ��ѯ��ǰ����δ�ߺ�������
	 */
	@Override
	public String qryUndoActivityByCond(Map<String, Object> map)
			throws Exception {
		String processInstanceId = StringHelper.valueOf(map
				.get("processInstanceId"));
		/** ��ѯ���̵�ǰ���� */
		ActivityInstanceDto curActInstDto = activityInstanceService.qryCurrentActivityByProcInstId(processInstanceId);
		String activityDefinitionId = curActInstDto.getActivityDefinitionId();

		ProcessInstanceDto processInstanceDto = processInstanceService
				.queryProcessInstance(processInstanceId);
		/** ͨ�����̶��嵽�ڴ��������̶��� */
		WorkflowProcess process = processDefinitionService
				.findWorkflowProcessById(processInstanceDto
						.getProcessDefineId());
		Activity activity = process.getActivityById(activityDefinitionId);
		List<Map<String, String>> undoActivityList = new ArrayList<Map<String, String>>();
		this.getUndoNextActivity(undoActivityList, new ArrayList<String>(),
				process, activity);
		String retJson = GsonHelper.toJson(undoActivityList);
		return retJson;
	}

	@SuppressWarnings("all")
	private void getUndoNextActivity(
			List<Map<String, String>> undoActivityList,
			List<String> containIds, WorkflowProcess process,
			Activity fromActivity) {
		/** ȡ��Դ������г��� */
		Iterator transitions = fromActivity.getEfferentTransitions().iterator();
		while (transitions.hasNext()) {
			Transition currentTransition = (Transition) transitions.next();
			Activity toActivity = currentTransition.getToActivity();
			if (toActivity.isNotRouteActivity()) {
				Map<String, String> temp = new HashMap<String, String>();
				if (!containIds.contains(toActivity.getId())) {
					temp.put("id", toActivity.getId());
					temp.put("text", toActivity.getName());
					temp.put("tacheCode", toActivity.getTacheCode());
					temp.put("tacheId", toActivity.getTacheId());
					undoActivityList.add(temp);
					containIds.add(toActivity.getId());
				}
			}
			this.getUndoNextActivity(undoActivityList, containIds, process,
					toActivity);
		}
	}

	@Override
	public String qryActivityInstance(Map<String, Object> map) throws Exception {
		String activityInstanceId = StringHelper.valueOf(map
				.get("activityInstanceId"));
		ActivityInstanceDto dto = new ActivityInstanceDto();
		if(activityInstanceId != null){
			dto = activityInstanceService
					.queryActivityInstance(activityInstanceId);
		}
		return GsonHelper.toJson(dto);
	}

	@Override
	public String qryCommandMsgInfoByPid(Map<String, Object> map)
			throws Exception {
		/** ͨ������ʵ��Id��������Ϣ���� */
		PageDto pageDto = commandQueueService.qryCommandMsgInfoByPid(map);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

	@Override
	public String qryProcInstShadowForTrace(Map<String, Object> map) {
		Long processInstanceId = LongHelper.valueOf(map
				.get("processInstanceId"));
		Boolean isHistory = Boolean.valueOf(map.get("isHistory").toString());
		return GsonHelper.toJson(flowFastflowTrace.qryProcInstShadowForTrace(
				processInstanceId, isHistory));
	}

	@Override
	public String qryPackageDefinePath(Map<String, Object> map) {
		Long processInstanceId = LongHelper.valueOf(map
				.get("processInstanceId"));
//		Boolean isHistory = Boolean.valueOf(map.get("isHistory").toString());
		Map<String, String> result = new HashMap<String, String>();
		result.put("path", this.processDefinitionService.qryPackageDefinePath(processInstanceId));
		logger.info("��ѯ·�����ؽ��Ϊ��" + result.get("path"));
		return GsonHelper.toJson(result);
	}

	@Override
	public String qryAreaIdByProcessInstId(Map<String, Object> map) {
		String processInstId = String.valueOf(map.get("processInstanceId"));
		ProcessInstanceDto processInstanceDto = processInstanceService
				.queryProcessInstance(processInstId);
		return processInstanceDto.getAreaId();
	}
	
}
