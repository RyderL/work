package com.ztesoft.uosflow.core.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;

public class CommandDto implements Serializable {
	
	public static String SYSTEM_SELF = "FLOW";

	private static final long serialVersionUID = 1l;

	protected String commandCode;
	protected String serial = "1";// �ӿ�ģʽҪ�õ���Ƕ��ģʽ�����߼�������Ҫ�õ�
	protected String from = "IOM";// �ӿ�ģʽҪ�õ���Ƕ��ģʽ�����߼�������Ҫ�õ�
	protected String to = "FLOW";// �ӿ�ģʽҪ�õ���Ƕ��ģʽ�����߼�������Ҫ�õ�
	protected String areaCode;
	protected String time;// ����ʱ��
	protected String processInstanceId;// ������ʵ��Id�Ľӿڣ����Բ��ô�ֵ����ʱֻ�д�������ʵ���ӿں��ύ�������޴�ֵ��
	protected String priority = "1";// ���ȼ���

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public CommandDto() {
	}

	public String getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void init(Map<String, Object> paramsMap) {
		this.commandCode = StringHelper.valueOf(paramsMap
				.get(InfConstant.INF_COMMAND_CODE));
		this.serial = StringHelper
				.valueOf(paramsMap.get(InfConstant.INF_SERIAL));
		this.from = StringHelper.valueOf(paramsMap.get(InfConstant.INF_FROM));
		this.to = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TO));
		this.areaCode = StringHelper.valueOf(paramsMap
				.get(InfConstant.INF_AREA_CODE));
		if("null".equals(areaCode)){
			this.areaCode = null;
		}
		this.processInstanceId = StringHelper.valueOf(paramsMap
				.get(InfConstant.INF_PROCESSINSTANCEID));
	}

	public Map<String,Object> toMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(InfConstant.INF_SERIAL, getSerial());
		map.put(InfConstant.INF_TIME, getTime());
		map.put(InfConstant.INF_FROM, getFrom());
		map.put(InfConstant.INF_TO, getTo());
		map.put(InfConstant.INF_COMMAND_CODE, getCommandCode());
		map.put(InfConstant.INF_AREA_CODE, areaCode);
		map.put(InfConstant.INF_PROCESSINSTANCEID, processInstanceId);
		return map;
	}
}
