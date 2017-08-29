package com.zterc.uos.fastflow.dto;

import java.util.Date;

public class MQExceptionDto {

	private Long id;
	private String queueName;
	private String commandCode;
	private String sendMsg;
	private String sourceName;// 生产者或消费者名称
	private String exceptionSource;
	private String exceptionMsg;
	private Date createDate;
	private Date stateDate;
	private String dealFlag;
	private String dealResult;
	private String serial;
	private String fromSys;
	private String toSys;
	private Long routeId;

	public static String DEAL_INITIAL = "10I";// MQ异常消息未处理
	public static String DEAL_EXCEPTION = "10E";// MQ异常消息重新处理（重发、重消费）时异常
	public static String DEAL_SUCCESS = "10F";// MQ异常成功处理完成
	public static String DEAL_DELETE = "10D";// MQ异常消息作废

	public static String PRODUCER_EXCEPTION = "Producer";// MQ发送异常
	public static String CUSUMER_EXCEPTION = "Cusumer";// MQ消费异常

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	public String getSendMsg() {
		return sendMsg;
	}

	public void setSendMsg(String sendMsg) {
		this.sendMsg = sendMsg;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getExceptionSource() {
		return exceptionSource;
	}

	public void setExceptionSource(String exceptionSource) {
		this.exceptionSource = exceptionSource;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	public Date getStateDate() {
		return stateDate;
	}

	public void setStateDate(java.util.Date stateDate) {
		this.stateDate = stateDate;
	}

	public String getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getDealResult() {
		return dealResult;
	}

	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getFromSys() {
		return fromSys;
	}

	public void setFromSys(String fromSys) {
		this.fromSys = fromSys;
	}

	public String getToSys() {
		return toSys;
	}

	public void setToSys(String toSys) {
		this.toSys = toSys;
	}

	public Long getRouteId() {
		return routeId;
	}

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}

}
