package com.ztesoft.uosflow.core.processor;

import org.apache.log4j.Logger;

import com.zterc.uos.base.cache.UosCacheClient;
import com.zterc.uos.base.lock.LockFactory;
import com.zterc.uos.fastflow.config.FastflowConfig;

public class RedisHeartProcessor extends Thread {
	private static final Logger logger = Logger.getLogger(RedisHeartProcessor.class);

	private int count = 0;
	boolean cacheModel = true;
	private UosCacheClient uosCacheClient;
	private int outTimes;//失联次数
	private long sleepTime;//进程休眠时间

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public void setOutTimes(int outTimes) {
		this.outTimes = outTimes;
	}

	public void setUosCacheClient(UosCacheClient uosCacheClient) {
		this.uosCacheClient = uosCacheClient;
	}

	public void init() {
		this.start();
	}

	@Override
	@SuppressWarnings("all")
	public void run() {
		while (true) {
			try {
				boolean result = uosCacheClient.ping();
				if (result) {
					if(!cacheModel){
						count = 0;
						cacheModel = true;
						refreshCacheModel(true);
						//恢复redis锁 modify by bobping
						LockFactory.setLockType("Redis");
					}
				}else{
					count += 1;
					logger.info("----与redis断开连接次数："+count);
					if(count >= outTimes){
						if(cacheModel){
							cacheModel = false;
							refreshCacheModel(false);
							//切成jvm内部锁 modify by bobping
							LockFactory.setLockType("Local");
						}
					}
				}
				Thread.currentThread().sleep(sleepTime);
			} catch (Exception e) {
				logger.error("",e);
				try {
					Thread.currentThread().sleep(sleepTime);
				} catch (InterruptedException e1) {
					logger.error("",e);
				}
			}
		}
	}

	private void refreshCacheModel(boolean cacheModel) {
		FastflowConfig.setIsCacheModel(cacheModel);
		logger.info("----设置缓存模式："+cacheModel);
	}
}
