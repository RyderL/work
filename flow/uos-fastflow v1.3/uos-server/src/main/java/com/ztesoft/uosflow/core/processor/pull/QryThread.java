package com.ztesoft.uosflow.core.processor.pull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ztesoft.uosflow.core.processor.DealThread;
import com.ztesoft.uosflow.core.processor.MqInterface;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public class QryThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(QryThread.class);
	private static final Log countLogger = LogFactory
			.getLog("controllerQryThread");

	private String qryThreadName;// 线程名称用于打印日志时识别
	private long nonDataSleepTime = 1000;// 没有数据时休眠时间
	private BlockingQueue<DealThread> dealThreadQueue;// 用于保存空闲的处理线程
	private ThreadPoolExecutor dealThreadPoolExecutor;// 线程执行

	private boolean activeFlag = true;
	
	private MqInterface mqInterface;

	public QryThread(String qryThreadName, long nonDataSleepTime,
			BlockingQueue<DealThread> dealThreadQueue,
			ThreadPoolExecutor dealThreadPoolExecutor,MqInterface mqInterface) {
		this.qryThreadName = qryThreadName;
		this.nonDataSleepTime = nonDataSleepTime;
		this.dealThreadQueue = dealThreadQueue;
		this.dealThreadPoolExecutor = dealThreadPoolExecutor;
		this.mqInterface = mqInterface;
	}

	@Override
	public void run() {
		// 统计计时开始时间
		long beginTime = System.currentTimeMillis();
		// 统计处理数
		long countDeal = 0l;
		long redosCostTime = 0l;// redis耗时
		while (activeFlag) {
			try {
				long t0 = System.currentTimeMillis();

				MessageDto request = mqInterface.getObject();
				if (request != null) {
					long t1 = System.currentTimeMillis();
					DealThread dealThread = dealThreadQueue.take();
					dealThread.setRequest(request);
					// dealThread.setQueueName(queueName);

					dealThreadPoolExecutor.execute(dealThread);
					redosCostTime += (t1 - t0);
					countDeal++;
					long endTime = System.currentTimeMillis();

					// 统计一分钟该控制器处理数
					if ((endTime - beginTime) >= 60000) {
						countLogger.warn("查询线程名称：" + qryThreadName + "，时间（毫秒）："
								+ (endTime - beginTime) + "，处理数：" + countDeal
								+ " tps:"
								+ (countDeal * 1000 / (endTime - beginTime))
								+ " reids:" + redosCostTime + " reidsAvg:"
								+ (redosCostTime / countDeal));
						beginTime = endTime;
						countDeal = 0l;
						redosCostTime = 0l;
					}
				} else {
					logger.debug(qryThreadName + "队列中没有数据，休眠:"
							+ nonDataSleepTime);
					sleep(nonDataSleepTime);
				}
				
			} catch (Throwable e) {
				sleep(100);// 异常的时候休息一下。
				logger.error(qryThreadName + " error ", e);
			}
		}
		logger.error(qryThreadName + " quit!");
	}

	/**
	 * 用于终止查询线程
	 * 
	 * @param activeFlag
	 */
	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	/**
	 * 休眠
	 * 
	 * @param sleepTime
	 */
	private void sleep(long sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (Throwable e) {
			logger.error(qryThreadName + " sleep error!", e);
		}
	}

}
