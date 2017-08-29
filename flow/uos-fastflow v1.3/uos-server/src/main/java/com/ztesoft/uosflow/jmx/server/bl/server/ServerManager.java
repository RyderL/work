package com.ztesoft.uosflow.jmx.server.bl.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.StaticCacheHelper;
import com.ztesoft.uosflow.core.counter.CounterCenter;
import com.ztesoft.uosflow.jmx.server.bl.cachemanager.StaticCacheManager;

public class ServerManager implements ServerManagerMXBean {

	@Override
	public String clearStaticCache(String cacheName) {
		StaticCacheHelper.clearAll();
		return "true";
	}

	@Override
	public String counterInfo() {
		Map<String, Long> map = new HashMap<String, Long>(
				CounterCenter.counterMap);
		return GsonHelper.toJson(map);
	}

	@Override
	public String clearCounterInfo() {
		CounterCenter.clearAll();
		return "true";
	}

	@Override
	public String refreshCacheAll() {
		StaticCacheManager.loadAllStaticCache();
		return "true";
	}

	@Override
	public  String changeLoggerLevel(String level){
		level = level.toUpperCase();
		Logger log = LogManager.getLogger(level);
		Level le = null;
		if(log == null){
			return "未改变日志级别";
		}else if("ALL".equalsIgnoreCase(level)){
			le = Level.ALL;
		}else if("DEBUG".equalsIgnoreCase(level)){
			le = Level.DEBUG;
		}else if("INFO".equalsIgnoreCase(level)){
			le = Level.INFO;
		}else if("WARN".equalsIgnoreCase(level)){
			le = Level.WARN;
		}else if("ERROR".equalsIgnoreCase(level)){
			le = Level.ERROR;
		}else if("FATAL".equalsIgnoreCase(level)){
			le = Level.FATAL;
		}else if("OFF".equalsIgnoreCase(level)){
			le = Level.OFF;
		}else{
			le = Level.OFF;
		}
		log.setLevel(le);
		
		return "Changed the log level of [" + log.getLevel() + "] successfully.";
	}
}
