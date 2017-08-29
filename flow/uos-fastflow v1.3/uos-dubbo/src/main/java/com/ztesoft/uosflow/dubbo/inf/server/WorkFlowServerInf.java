package com.ztesoft.uosflow.dubbo.inf.server;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandResultDto;
import com.ztesoft.uosflow.dubbo.dto.result.DubboCreateProcInsResultDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboAbortProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboAddReturnReasonDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboAddTacheDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboCompleteWorkItemDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboCreateProcessInstacneDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboDelReturnReasonDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboDelTacheDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboDisableWorkItemDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboModReturnReasonDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboModTacheDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboProcessInstanceJumpDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboResumeProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboRollbackProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboStartProcessInstacneDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboSuspendProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboSuspendWorkItemDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboTerminateProcessInstanceDto;
import com.ztesoft.uosflow.dubbo.dto.server.DubboUpdateProcessInstanceDto;

public interface WorkFlowServerInf {
	
	/**
	 * 创建流程实例
	 * @param createProcessInstacneDto
	 * @return
	 */
	public DubboCreateProcInsResultDto createProcessInstance(DubboCreateProcessInstacneDto createProcessInstacneDto);
	
	/**
	 * 启动流程实例
	 * @param startProcessInstacneDto
	 * @return
	 */
	public DubboCommandResultDto startProcessInstance(DubboStartProcessInstacneDto startProcessInstacneDto);
	
	/**
	 * 提交工作项
	 * @param completeWorkItemDto
	 * @return
	 */
	public DubboCommandResultDto completeWorkItem(DubboCompleteWorkItemDto completeWorkItemDto);
	
	/**
	 * 流程回退（撤单）
	 * @param rollbackProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto rollbackProcessInstance(DubboRollbackProcessInstanceDto rollbackProcessInstanceDto);
	
	/**
	 * 流程回退（退单）
	 * @param disableWorkItemDto
	 * @return
	 */
	public DubboCommandResultDto disableWorkItem(DubboDisableWorkItemDto disableWorkItemDto);
	
	/**
	 * 作废流程
	 * @param disableWorkItemDto
	 * @return
	 */
	public DubboCommandResultDto abortProcessInstance(DubboAbortProcessInstanceDto abortProcessInstanceDto);
	
	/**
	 * 
	 * dubbo服务用于处理异常重派
	 * 
	 * @param exceptionId
	 * @return
	 */
	public boolean dealException(Long exceptionId);
	
	/**
	 * 通用调用接口
	 * @param requestJson
	 * @return
	 */
	public String executeCommand(String requestJson);
	
	/**
	 * 新增环节
	 * @param dubboAddTacheDto
	 * @return
	 */
	public DubboCommandResultDto addTache(DubboAddTacheDto dubboAddTacheDto);

	/**
	 * 修改环节
	 * @param dubboModTacheDto
	 * @return
	 */
	public DubboCommandResultDto modTache(DubboModTacheDto dubboModTacheDto);
	/**
	 * 删除环节
	 * @param dubboDelTacheDto
	 * @return
	 */
	public DubboCommandResultDto delTache(DubboDelTacheDto dubboDelTacheDto);
	/**
	 * 新增异常原因
	 * @param dubboAddReturnReasonDto
	 * @return
	 */
	public DubboCommandResultDto addReturnReason(DubboAddReturnReasonDto dubboAddReturnReasonDto);
	/**
	 * 修改异常原因
	 * @param dubboModReturnReasonDto
	 * @return
	 */
	public DubboCommandResultDto modReturnReason(DubboModReturnReasonDto dubboModReturnReasonDto);
	/**
	 * 删除异常原因
	 * @param dubboDelReturnReasonDto
	 * @return
	 */
	public DubboCommandResultDto delReturnReason(DubboDelReturnReasonDto dubboDelReturnReasonDto);
	
	/**
	 * 修改流程实例状态
	 * @param dubboUpdateProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto updateProcessInstance(DubboUpdateProcessInstanceDto dubboUpdateProcessInstanceDto);

	/**
	 * 流程跳转
	 * @param dubboProcessInstanceJumpDto
	 * @return
	 */
	public DubboCommandResultDto processInstanceJump(DubboProcessInstanceJumpDto dubboProcessInstanceJumpDto);
	
	/**
	 * 终止流程
	 * @param dubboTerminateProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto terminateProcessInstance(DubboTerminateProcessInstanceDto dubboTerminateProcessInstanceDto);
	
	/**
	 * 挂起流程
	 * @param dubboSuspendProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto suspendProcessInstance(DubboSuspendProcessInstanceDto dubboSuspendProcessInstanceDto);

	/**
	 * 挂起工作项
	 * @param dubboSuspendWorkItemDto
	 * @return
	 */
	public DubboCommandResultDto suspendWorkItem(DubboSuspendWorkItemDto dubboSuspendWorkItemDto);
	
	/**
	 * 解挂流程
	 * @param dubboResumeProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto resumeProcessInstance(DubboResumeProcessInstanceDto dubboResumeProcessInstanceDto);
}
