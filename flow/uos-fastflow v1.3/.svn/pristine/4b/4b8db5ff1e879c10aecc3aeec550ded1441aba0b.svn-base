package com.zterc.uos.fastflow.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.process.WorkItemDAO;
import com.zterc.uos.fastflow.dao.specification.DispatchRuleDAO;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;
import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.state.WMWorkItemState;

/**
 * （Server）工作项操作类
 * 
 * @author gong.yi
 *
 */
public class WorkItemService {

	private WorkItemDAO workItemDAO;

	private DispatchRuleDAO dispatchRuleDAO;

	public void setWorkItemDAO(WorkItemDAO workItemDAO) {
		this.workItemDAO = workItemDAO;
	}

	public void setDispatchRuleDAO(DispatchRuleDAO dispatchRuleDAO) {
		this.dispatchRuleDAO = dispatchRuleDAO;
	}

	public DispatchRuleDAO getDispatchRuleDAO() {
		return dispatchRuleDAO;
	}

	public Long getWorkItemBatchNo() {
		return workItemDAO.getWorkItemBatchNo();
	}

	/**
	 * （Server）创建工作项
	 * 
	 * @param activity
	 * @param processInstance
	 * @param activityInstance
	 * @param useDB
	 * @return
	 */
	public WorkItemDto createWorkItem(Activity activity,
			ProcessInstanceDto processInstance,
			ActivityInstanceDto activityInstance, boolean useDB) {
		WorkItemDto workItem = new WorkItemDto();
		Timestamp now = DateHelper.getTimeStamp();
		workItem.setActivityDefinitionId(activityInstance
				.getActivityDefinitionId());
		workItem.setActivityInstanceId(activityInstance.getId());
		workItem.setAssignedDate(now);
		workItem.setStartedDate(now);
		workItem.setName(activity.getName());
		workItem.setParticipantId(-1);
		workItem.setParticipantPositionId(-1);
		workItem.setOrganizationId(-1);
		workItem.setPriority(activityInstance.getPriority());
		workItem.setState(WMWorkItemState.OPEN_RUNNING_INT);
		workItem.setDueDate(null);
		workItem.setProcessInstanceId(processInstance.getProcessInstanceId());
		workItem.setProcessInstanceName(processInstance.getName());
		workItem.setProcessDefineId(processInstance.getProcessDefineId());
		workItem.setTacheId(Long.valueOf(activity.getTacheId()));
		// add by che.zi for 增加区域作为队列的分区字段
		workItem.setAreaId(processInstance.getAreaId());
		// modify by ji.dong 2012-11-05 ur:86940
		if (activityInstance.getBatchid() != null) {
			workItem.setBatchid(activityInstance.getBatchid());
		}
		// end
		// add by ji.dong 2013-03-06 ur:26538
		if (activityInstance.getCreateSource() != null) {
			workItem.setCreateSource(activityInstance.getCreateSource());
		}
		// end

		// 派发规则 --uos-manager里的DubboClient等逻辑需要迁到dubbo模块，才能实现区域递归查询派发规则
		// DispatchRuleDto qryDto = new DispatchRuleDto();
		// qryDto.setPackageDefineId(workItem.getProcessDefineId());
		// qryDto.setTacheId(workItem.getTacheId());
		// qryDto.setAreaId(LongHelper.valueOf(workItem.getAreaId()));
		// DispatchRuleDto[] dispatchRules =
		// dispatchRuleDAO.qryDispatchRule(qryDto);
		// if(dispatchRules.length>0){
		// DispatchRuleDto dispatchRule = dispatchRules[0];
		// workItem.setPartyType(dispatchRule.getPartyType());
		// workItem.setPartyId(dispatchRule.getPartyId());
		// workItem.setPartyName(dispatchRule.getPartyName());
		// if("SYS".equals(dispatchRule.getPartyType())){
		// workItem.setManualPartyType(dispatchRule.getManualPartyType());
		// workItem.setManualPartyId(dispatchRule.getManualPartyId());
		// workItem.setManualPartyName(dispatchRule.getManualPartyName());
		// workItem.setIsAuto(1);
		// }else{
		// workItem.setIsAuto(0);
		// }
		// }

		createWorkItem(workItem, useDB);
		// 将产生的工作项写入数据库
		return workItem;
	}

	
	/**
	 * （Server）创建工作项
	 * 
	 * @param workItem
	 * @param useDB
	 * @return
	 */
	public WorkItemDto createWorkItem(WorkItemDto workItem, boolean useDB) {
		if (workItem.getWorkItemId() == null) {
			workItem.setWorkItemId(SequenceHelper.getIdWithSeed("UOS_WORKITEM",
					workItem.getProcessInstanceId()==null?"0":workItem.getProcessInstanceId().toString()));
		}
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				workItemDAO.createWorkItem(workItem);
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					workItem.setOperType(OperType.getOperType(
							workItem.getOperType(), OperType.INSERT));
					processModel.getWorkItemDtos().add(workItem);
				}
				// 内存模式操作----end-------------
			}
		} else {
			workItemDAO.createWorkItem(workItem);
		}
		// 将产生的工作项写入数据库
		return workItem;
	}

	/**
	 * （Server）查询工作项
	 * 
	 * @param workItemId
	 * @return
	 */
	public WorkItemDto queryWorkItem(String workItemId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		WorkItemDto workItem = null;
		if (processModel != null) {
			List<WorkItemDto> workItemDtos = processModel.getWorkItemDtos();
			for (int i = 0; i < workItemDtos.size(); i++) {
				WorkItemDto dto = workItemDtos.get(i);
				if (dto != null
						&& dto.getWorkItemId().longValue() == Long
								.valueOf(workItemId)) {
					return dto;
				}
			}
			//如果内存模型中没有找到对应的工作项，则从数据库中查找，如果找到了将工作项回填到内存模型中
			workItem = workItemDAO.queryWorkItem(workItemId);
			if(workItem != null){
				workItem.setOperType(OperType.DEFAULT);
				processModel.getWorkItemDtos().add(workItem);
				return workItem;
			}
		}
		// 内存模式操作----end-------------

		return workItemDAO.queryWorkItem(workItemId);
	}

	/**
	 * （Server）根据条件查询工作项列表
	 * 
	 * @param processInstanceId
	 * @param state
	 * @param disableWorkItemId
	 * @return
	 */
	public List<WorkItemDto> queryWorkItemsByProcess(String processInstanceId,
			int state, String disableWorkItemId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<WorkItemDto> workItemDtos = processModel.getWorkItemDtos();
			List<WorkItemDto> retWorkItemDtos = new ArrayList<>();
			for (int i = 0; i < workItemDtos.size(); i++) {
				WorkItemDto dto = workItemDtos.get(i);
				if (dto != null && dto.getState() == state) {
					if (disableWorkItemId == null
							|| disableWorkItemId.length() == 0) {
						retWorkItemDtos.add(dto);
					} else if (!disableWorkItemId.equals(dto.getWorkItemId()
							+ "")) {
						retWorkItemDtos.add(dto);
					}
				}
			}
			return retWorkItemDtos;
		}
		// 内存模式操作----end-------------

		return workItemDAO.queryWorkItemsByProcess(processInstanceId, state,
				disableWorkItemId);
	}

	/**
	 * （Server）根据环节定义id查找工作项
	 * 
	 * @param processInstanceId
	 * @param targetTacheId
	 * @return
	 */
	public WorkItemDto queryWorkItemByTacheId(Long processInstanceId,
			Long targetTacheId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<WorkItemDto> workItemDtos = processModel.getWorkItemDtos();
			int i = workItemDtos.size() - 1;
			for (; i >= 0; i--) {
				WorkItemDto dto = workItemDtos.get(i);
				if (dto != null
						&& dto.getTacheId().longValue() == targetTacheId
						&& processInstanceId.longValue() == dto
								.getProcessInstanceId()) {
					return dto;
				}
			}
		}
		// 内存模式操作----end-------------

		return workItemDAO.queryWorkItemByTacheId(processInstanceId,
				targetTacheId);
	}

	/**
	 * （Server）更新工作项
	 * 
	 * @param workItem
	 * @param useDB
	 */
	public void updateWorkItem(WorkItemDto workItem, boolean useDB) {
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				workItemDAO.updateWorkItem(workItem);
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					List<WorkItemDto> workItemDtos = processModel
							.getWorkItemDtos();
					for (int i = 0; i < workItemDtos.size(); i++) {
						WorkItemDto dto = workItemDtos.get(i);
						if (dto != null
								&& dto.getWorkItemId().longValue() == workItem
										.getWorkItemId()) {
							workItem.setOperType(OperType.getOperType(
									workItem.getOperType(), OperType.UPDATE));
							workItemDtos.set(i, workItem);
							break;
						}
					}
				}
				// 内存模式操作----end-------------
			}
		} else {
			workItemDAO.updateWorkItem(workItem);
		}
	}

	/**
	 * （Manager）根据条件查找工作项列表
	 * 
	 * @param paramMap
	 * @return
	 */
	public PageDto findWorkItemByCond(Map<String, Object> paramMap) {
		return workItemDAO.findWorkItemByCond(paramMap);
	}

	/**
	 * 根据活动实例id获取工作项
	 * @param activityInstanceId
	 * @return
	 */
	public WorkItemDto qryWorkItemByActInstId(Long activityInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<WorkItemDto> workItemDtos = processModel.getWorkItemDtos();
			int i = workItemDtos.size() - 1;
			for (; i >= 0; i--) {
				WorkItemDto dto = workItemDtos.get(i);
				if (dto != null
						&& dto.getActivityInstanceId().longValue() == activityInstanceId.longValue()) {
					return dto;
				}
			}
		}
		// 内存模式操作----end-------------
		return workItemDAO.qryWorkItemByActInstId(activityInstanceId);
	}

	public List<WorkItemDto> queryWorkItemsByPid(String processInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<WorkItemDto> workItemDtos = processModel.getWorkItemDtos();
			return workItemDtos;
		}
		// 内存模式操作----end-------------
		return workItemDAO.queryWorkItemsByPid(processInstanceId);
	}

	public void deleteByPid(String processInstanceId) {
		workItemDAO.deleteByPid(processInstanceId);
	}

	public List<WorkItemDto> queryUndoWorkItems(Map<String, Object> params) {
		return workItemDAO.queryUndoWorkItems(params);
	}

}
