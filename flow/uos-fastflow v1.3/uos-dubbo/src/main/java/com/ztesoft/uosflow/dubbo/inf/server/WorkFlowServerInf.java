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
	 * ��������ʵ��
	 * @param createProcessInstacneDto
	 * @return
	 */
	public DubboCreateProcInsResultDto createProcessInstance(DubboCreateProcessInstacneDto createProcessInstacneDto);
	
	/**
	 * ��������ʵ��
	 * @param startProcessInstacneDto
	 * @return
	 */
	public DubboCommandResultDto startProcessInstance(DubboStartProcessInstacneDto startProcessInstacneDto);
	
	/**
	 * �ύ������
	 * @param completeWorkItemDto
	 * @return
	 */
	public DubboCommandResultDto completeWorkItem(DubboCompleteWorkItemDto completeWorkItemDto);
	
	/**
	 * ���̻��ˣ�������
	 * @param rollbackProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto rollbackProcessInstance(DubboRollbackProcessInstanceDto rollbackProcessInstanceDto);
	
	/**
	 * ���̻��ˣ��˵���
	 * @param disableWorkItemDto
	 * @return
	 */
	public DubboCommandResultDto disableWorkItem(DubboDisableWorkItemDto disableWorkItemDto);
	
	/**
	 * ��������
	 * @param disableWorkItemDto
	 * @return
	 */
	public DubboCommandResultDto abortProcessInstance(DubboAbortProcessInstanceDto abortProcessInstanceDto);
	
	/**
	 * 
	 * dubbo�������ڴ����쳣����
	 * 
	 * @param exceptionId
	 * @return
	 */
	public boolean dealException(Long exceptionId);
	
	/**
	 * ͨ�õ��ýӿ�
	 * @param requestJson
	 * @return
	 */
	public String executeCommand(String requestJson);
	
	/**
	 * ��������
	 * @param dubboAddTacheDto
	 * @return
	 */
	public DubboCommandResultDto addTache(DubboAddTacheDto dubboAddTacheDto);

	/**
	 * �޸Ļ���
	 * @param dubboModTacheDto
	 * @return
	 */
	public DubboCommandResultDto modTache(DubboModTacheDto dubboModTacheDto);
	/**
	 * ɾ������
	 * @param dubboDelTacheDto
	 * @return
	 */
	public DubboCommandResultDto delTache(DubboDelTacheDto dubboDelTacheDto);
	/**
	 * �����쳣ԭ��
	 * @param dubboAddReturnReasonDto
	 * @return
	 */
	public DubboCommandResultDto addReturnReason(DubboAddReturnReasonDto dubboAddReturnReasonDto);
	/**
	 * �޸��쳣ԭ��
	 * @param dubboModReturnReasonDto
	 * @return
	 */
	public DubboCommandResultDto modReturnReason(DubboModReturnReasonDto dubboModReturnReasonDto);
	/**
	 * ɾ���쳣ԭ��
	 * @param dubboDelReturnReasonDto
	 * @return
	 */
	public DubboCommandResultDto delReturnReason(DubboDelReturnReasonDto dubboDelReturnReasonDto);
	
	/**
	 * �޸�����ʵ��״̬
	 * @param dubboUpdateProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto updateProcessInstance(DubboUpdateProcessInstanceDto dubboUpdateProcessInstanceDto);

	/**
	 * ������ת
	 * @param dubboProcessInstanceJumpDto
	 * @return
	 */
	public DubboCommandResultDto processInstanceJump(DubboProcessInstanceJumpDto dubboProcessInstanceJumpDto);
	
	/**
	 * ��ֹ����
	 * @param dubboTerminateProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto terminateProcessInstance(DubboTerminateProcessInstanceDto dubboTerminateProcessInstanceDto);
	
	/**
	 * ��������
	 * @param dubboSuspendProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto suspendProcessInstance(DubboSuspendProcessInstanceDto dubboSuspendProcessInstanceDto);

	/**
	 * ��������
	 * @param dubboSuspendWorkItemDto
	 * @return
	 */
	public DubboCommandResultDto suspendWorkItem(DubboSuspendWorkItemDto dubboSuspendWorkItemDto);
	
	/**
	 * �������
	 * @param dubboResumeProcessInstanceDto
	 * @return
	 */
	public DubboCommandResultDto resumeProcessInstance(DubboResumeProcessInstanceDto dubboResumeProcessInstanceDto);
}
