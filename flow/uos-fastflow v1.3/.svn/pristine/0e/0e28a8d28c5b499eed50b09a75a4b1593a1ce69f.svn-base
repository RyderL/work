package com.zterc.uos.gid.service;

import java.util.Map;

import org.apache.log4j.Logger;

public class GidConfig {
	private static final Logger logger = Logger.getLogger(GidConfig.class);
	private String gidServerUrl;// GID服务端URL
	private String sysCode;// 系统编码
	private int tryTimes;// 尝试次数
	private Map<String, String> seqConfigMap;// 系统的GID配置
	public void init(){
		logger.info("----gidServerUrl:"+gidServerUrl);
		logger.info("----sysCode:"+sysCode);
		logger.info("----tryTimes:"+tryTimes);
		logger.info("----seqConfigMap:"+seqConfigMap);
	}
	public String getGidServerUrl() {
		return gidServerUrl;
	}
	public void setGidServerUrl(String gidServerUrl) {
		this.gidServerUrl = gidServerUrl;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public int getTryTimes() {
		return tryTimes;
	}
	public void setTryTimes(int tryTimes) {
		this.tryTimes = tryTimes;
	}
	public Map<String, String> getSeqConfigMap() {
		return seqConfigMap;
	}
	public void setSeqConfigMap(Map<String, String> seqConfigMap) {
		this.seqConfigMap = seqConfigMap;
	}
	
}
