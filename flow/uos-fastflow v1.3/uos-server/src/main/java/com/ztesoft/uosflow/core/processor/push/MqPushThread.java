package com.ztesoft.uosflow.core.processor.push;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import com.ztesoft.uosflow.core.processor.DealThread;
import com.ztesoft.uosflow.core.processor.MqInterface;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public class MqPushThread {
	private MqInterface mqInterface;

	private BlockingQueue<DealThread> dealThreadQueue;// 用于保存空闲的处理线程
	private ThreadPoolExecutor dealThreadPoolExecutor;// 线程执行

	public MqPushThread(BlockingQueue<DealThread> dealThreadQueue,
			ThreadPoolExecutor dealThreadPoolExecutor, MqInterface mqInterface) {
		this.dealThreadQueue = dealThreadQueue;
		this.dealThreadPoolExecutor = dealThreadPoolExecutor;
		this.mqInterface = mqInterface;
	}

	public void start() {
		try {
			mqInterface.start(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dealMessageDto(MessageDto request) throws InterruptedException {
		DealThread dealThread = dealThreadQueue.take();
		dealThread.setRequest(request);
		this.dealThreadPoolExecutor.execute(dealThread);
	}

}
