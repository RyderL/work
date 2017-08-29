package com.ztesoft.uosflow.util.mq.producer.common;

import com.ztesoft.uosflow.util.mq.producer.inf.IMqProducer;

public class QueueRuleConfig {
	private String queueName;
	private int from;
	private int to;
	private IMqProducer queue;

	public IMqProducer getQueue() {
		return queue;
	}

	public void setQueue(IMqProducer queue) {
		this.queue = queue;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

}
