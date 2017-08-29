package com.zterc.uos.base.lock.local;

import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import com.zterc.uos.base.lock.FlowLock;
import com.zterc.uos.base.lock.LockConfig;

public class LocalLock implements FlowLock {

	private Logger logger = Logger.getLogger(LocalLock.class);
	static final String LOCKPREFIX = "UOS_FLOW_LOCK_";
	private String key;
	private ReentrantLock lock = new ReentrantLock();
	@SuppressWarnings("unused")
	private LockConfig lockConfig;
	
	public LocalLock(String key,LockConfig lockConfig){
		this.key = key;
		this.lockConfig = lockConfig;
	}
	
	@Override
	public boolean acquire() {
		lock.lock();
		logger.info("-----��ȡ����key:"+key+"��----currentTimeMillis:"+System.currentTimeMillis());
		return true;
	}

	@Override
	public synchronized void release() {
		try {
			lock.unlock();
		} catch (Exception e) {
			logger.error("----�ͷ���key:"+key+"�쳣���쳣��Ϣ��"+e.getMessage(),e);
		}
    	logger.info("----��key:"+key+"�ͷ���----currentTimeMillis:"+System.currentTimeMillis());
	}

}
