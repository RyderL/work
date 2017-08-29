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

	private String qryThreadName;// �߳��������ڴ�ӡ��־ʱʶ��
	private long nonDataSleepTime = 1000;// û������ʱ����ʱ��
	private BlockingQueue<DealThread> dealThreadQueue;// ���ڱ�����еĴ����߳�
	private ThreadPoolExecutor dealThreadPoolExecutor;// �߳�ִ��

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
		// ͳ�Ƽ�ʱ��ʼʱ��
		long beginTime = System.currentTimeMillis();
		// ͳ�ƴ�����
		long countDeal = 0l;
		long redosCostTime = 0l;// redis��ʱ
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

					// ͳ��һ���Ӹÿ�����������
					if ((endTime - beginTime) >= 60000) {
						countLogger.warn("��ѯ�߳����ƣ�" + qryThreadName + "��ʱ�䣨���룩��"
								+ (endTime - beginTime) + "����������" + countDeal
								+ " tps:"
								+ (countDeal * 1000 / (endTime - beginTime))
								+ " reids:" + redosCostTime + " reidsAvg:"
								+ (redosCostTime / countDeal));
						beginTime = endTime;
						countDeal = 0l;
						redosCostTime = 0l;
					}
				} else {
					logger.debug(qryThreadName + "������û�����ݣ�����:"
							+ nonDataSleepTime);
					sleep(nonDataSleepTime);
				}
				
			} catch (Throwable e) {
				sleep(100);// �쳣��ʱ����Ϣһ�¡�
				logger.error(qryThreadName + " error ", e);
			}
		}
		logger.error(qryThreadName + " quit!");
	}

	/**
	 * ������ֹ��ѯ�߳�
	 * 
	 * @param activeFlag
	 */
	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	/**
	 * ����
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
