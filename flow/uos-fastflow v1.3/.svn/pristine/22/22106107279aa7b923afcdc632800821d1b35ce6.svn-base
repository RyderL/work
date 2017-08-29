package com.zterc.uos.fastflow.dao.process;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public interface WorkItemDAO {

	Long getWorkItemBatchNo();

	WorkItemDto queryWorkItem(String workItemId);

	WorkItemDto createWorkItem(WorkItemDto workItem);

	void updateWorkItem(WorkItemDto workItem);

	PageDto findWorkItemByCond(Map<String, Object> paramMap);

	WorkItemDto queryWorkItemByTacheId(Long processInstanceId,Long targetTacheId);

	List<WorkItemDto> queryWorkItemsByProcess(String processInstanceId,
			int state, String disableWorkItemId);

	List<Map<String, Object>> qryWorkItemStateCount(Map<String, Object> paramMap);

	WorkItemDto qryWorkItemByActInstId(Long activityInstanceId);

	List<WorkItemDto> queryWorkItemsByPid(String processInstanceId);

	void deleteByPid(String processInstanceId);

	List<WorkItemDto> queryUndoWorkItems(Map<String, Object> params);

}
