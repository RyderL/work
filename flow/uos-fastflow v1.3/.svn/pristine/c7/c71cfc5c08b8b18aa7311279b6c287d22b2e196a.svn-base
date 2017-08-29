package com.zterc.uos.base.lock;

public class LockConfig {
	private int expireMsecs = 60 * 1000; // 锁超时，防止线程在入锁以后，无限的执行等待
	private int timeoutMsecs = 10 * 1000; // 锁等待，防止线程饥饿
	private int sleepTime = 100;//等待休眠时间

	public int getExpireMsecs() {
		return expireMsecs;
	}

	public void setExpireMsecs(int expireMsecs) {
		this.expireMsecs = expireMsecs;
	}

	public int getTimeoutMsecs() {
		return timeoutMsecs;
	}

	public void setTimeoutMsecs(int timeoutMsecs) {
		this.timeoutMsecs = timeoutMsecs;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	
}
