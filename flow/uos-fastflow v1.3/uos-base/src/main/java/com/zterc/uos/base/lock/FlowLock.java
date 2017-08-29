package com.zterc.uos.base.lock;

public interface FlowLock {
	public boolean acquire();

	public void release();
	
}
