package com.ztesoft.uosflow.core.processor;
import java.util.concurrent.ThreadFactory;

/**
 * 
 * add by bobping
 */
public class ZThreadFactory implements ThreadFactory{


	private final String  threadPoolName;
	private int threadNum=0;
	
	public ZThreadFactory(String threadPoolName) {
		this.threadPoolName=threadPoolName;
	}
	@Override
	public Thread newThread(Runnable r) {
		Thread thread=new Thread(r);
		thread.setName(getThreadName());
		return thread;
	}
	
	public synchronized String  getThreadName(){
		return threadPoolName+"-"+(threadNum++);
	}
}






