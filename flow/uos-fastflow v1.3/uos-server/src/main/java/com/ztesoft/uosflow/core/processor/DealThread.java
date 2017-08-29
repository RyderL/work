package com.ztesoft.uosflow.core.processor;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public class DealThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(DealThread.class);
	private static final Log countLogger = LogFactory
			.getLog("controllerDealThread");

	private String dealThreadName;// 线程名称 日志输出时使用
	private BlockingQueue<DealThread> dealThreadQueue;
	private EventInterface eventInterface;// 事件处理逻辑
	private MessageDto request;// 当前处理项队列中的值

	private long beginTime = System.currentTimeMillis();
	private long costTime = 0l;// 方法耗时
	private long countDeal = 0l;

	public DealThread(String dealThreadName,
			BlockingQueue<DealThread> dealThreadQueue,
			EventInterface eventInterface) {
		this.dealThreadName = dealThreadName;
		this.dealThreadQueue = dealThreadQueue;
		this.eventInterface = eventInterface;
	}

	/**
	 * 设置执行的消息 每次分配完任务后需要调用
	 * 
	 * @param itemName
	 */
	public void setRequest(MessageDto request) {
		this.request = request;
	}

	public void run() {
		try {
			long t0 = System.currentTimeMillis();
			this.eventInterface.deal(request);
			long endTime = System.currentTimeMillis();
			countDeal++;
			costTime += endTime - t0;
			// 统计一分钟该控制器处理数
			if ((endTime - beginTime) >= 60000) {
				countLogger.info("处理线程名称：" + dealThreadName + "，时间（毫秒）："
						+ (endTime - beginTime) + "。实际耗时:" + costTime + "，处理数："
						+ countDeal + " tps:"
						+ (countDeal * 1000 / (endTime - beginTime)));
				beginTime = endTime;
				countDeal = 0l;
				costTime = 0l;
			}
		} catch (Exception e) {
			String errMsg = "控制器消息队列处理失败," + dealThreadName + " request:"
					+ request;
			logger.error(errMsg, e);
		} finally {
			request = null;
			dealThreadQueue.add(this);
		}
	}

}
