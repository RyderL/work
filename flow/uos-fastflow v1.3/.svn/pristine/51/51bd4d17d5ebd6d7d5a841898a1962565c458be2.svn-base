package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class CommandCfgDto implements Serializable {

	public static final String TYPE_SERVER = "SERVER";
	public static final String TYPE_CLIENT = "CLIENT";

	public static final int SYN_FLAG_TRUE = 0;
	public static final int SYN_FLAG_FALSE = 1;

	private static final long serialVersionUID = 1L;
	private Long id;
	private String commandCode;
	private int isSyn;
	private String beanName;
	private String type;
	private int modeCount;
	private int sleepTime;
	private int dealCount;
	private int qryCount;
	private int dealLiveTime;
	private String display;
	private String displayName;
	private String queueCode;
	private String queueRuleBean;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	public int getIsSyn() {
		return isSyn;
	}

	public void setIsSyn(int isSyn) {
		this.isSyn = isSyn;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getModeCount() {
		return modeCount;
	}

	public void setModeCount(int modeCount) {
		this.modeCount = modeCount;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getDealCount() {
		return dealCount;
	}

	public void setDealCount(int dealCount) {
		this.dealCount = dealCount;
	}

	public int getQryCount() {
		return qryCount;
	}

	public void setQryCount(int qryCount) {
		this.qryCount = qryCount;
	}

	public int getDealLiveTime() {
		return dealLiveTime;
	}

	public void setDealLiveTime(int dealLiveTime) {
		this.dealLiveTime = dealLiveTime;
	}

	public String getQueueName(int region) {
		return this.queueCode + "Queue_" + region + "_" + "FLOW";
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getQueueCode() {
		return queueCode;
	}

	public void setQueueCode(String queueCode) {
		this.queueCode = queueCode;
	}

	public String getQueueRuleBean() {
		return queueRuleBean;
	}

	public void setQueueRuleBean(String queueRuleBean) {
		this.queueRuleBean = queueRuleBean;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("commandCode", "COMMAND_CODE");
		mapper.put("isSyn", "IS_SYN");
		mapper.put("beanName", "BEAN_NAME");
		mapper.put("type", "TYPE");
		mapper.put("modeCount", "MODE_COUNT");
		mapper.put("sleepTime", "SLEEP_TIME");
		mapper.put("dealCount", "DEAL_COUNT");
		mapper.put("qryCount", "QRY_COUNT");
		mapper.put("dealLiveTime", "DEAL_LIVE_TIME");
		mapper.put("display", "DISPLAY");
		mapper.put("displayName", "DISPLAY_NAME");
		mapper.put("queueCode", "QUEUE_CODE");
		mapper.put("queueRuleBean", "QUEUE_RULE_BEAN");
		return mapper;
	}
}
