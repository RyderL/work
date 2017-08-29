package com.ztesoft.uosflow.dubbo.dto.result;

import java.util.Map;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandResultDto;

/**
 * �������̷��񷵻ؽ������
 * 
 * @author Administrator
 * 
 */
public class DubboCreateProcInsResultDto extends DubboCommandResultDto {

	private static final long serialVersionUID = 1L;

	private Map<String, String> flowPassList;// ����͸�������б�
	private Map<String, String> flowParamList;// ���̲����б�
	
	public Map<String, String> getFlowParamList() {
		return flowParamList;
	}
	public void setFlowParamList(Map<String, String> flowParamList) {
		this.flowParamList = flowParamList;
	}
	public Map<String, String> getFlowPassList() {
		return flowPassList;
	}
	public void setFlowPassList(Map<String, String> flowPassList) {
		this.flowPassList = flowPassList;
	}
}
