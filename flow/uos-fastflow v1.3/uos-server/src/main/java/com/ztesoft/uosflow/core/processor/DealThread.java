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

	private String dealThreadName;// �߳����� ��־���ʱʹ��
	private BlockingQueue<DealThread> dealThreadQueue;
	private EventInterface eventInterface;// �¼������߼�
	private MessageDto request;// ��ǰ����������е�ֵ

	private long beginTime = System.currentTimeMillis();
	private long costTime = 0l;// ������ʱ
	private long countDeal = 0l;

	public DealThread(String dealThreadName,
			BlockingQueue<DealThread> dealThreadQueue,
			EventInterface eventInterface) {
		this.dealThreadName = dealThreadName;
		this.dealThreadQueue = dealThreadQueue;
		this.eventInterface = eventInterface;
	}

	/**
	 * ����ִ�е���Ϣ ÿ�η������������Ҫ����
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
			// ͳ��һ���Ӹÿ�����������
			if ((endTime - beginTime) >= 60000) {
				countLogger.info("�����߳����ƣ�" + dealThreadName + "��ʱ�䣨���룩��"
						+ (endTime - beginTime) + "��ʵ�ʺ�ʱ:" + costTime + "����������"
						+ countDeal + " tps:"
						+ (countDeal * 1000 / (endTime - beginTime)));
				beginTime = endTime;
				countDeal = 0l;
				costTime = 0l;
			}
		} catch (Exception e) {
			String errMsg = "��������Ϣ���д���ʧ��," + dealThreadName + " request:"
					+ request;
			logger.error(errMsg, e);
		} finally {
			request = null;
			dealThreadQueue.add(this);
		}
	}

}
