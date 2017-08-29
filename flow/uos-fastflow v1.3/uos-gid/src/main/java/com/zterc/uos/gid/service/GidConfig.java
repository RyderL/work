package com.zterc.uos.gid.service;

import java.util.Map;

import org.apache.log4j.Logger;

public class GidConfig {
	private static final Logger logger = Logger.getLogger(GidConfig.class);
	private String gidServerUrl;// GID�����URL
	private String sysCode;// ϵͳ����
	private int tryTimes;// ���Դ���
	private Map<String, String> seqConfigMap;// ϵͳ��GID����
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
