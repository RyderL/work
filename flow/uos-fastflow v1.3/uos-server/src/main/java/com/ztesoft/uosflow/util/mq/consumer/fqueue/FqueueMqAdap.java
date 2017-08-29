package com.ztesoft.uosflow.util.mq.consumer.fqueue;

import com.google.code.fqueue.FQueue;
import com.zterc.uos.base.helper.SerializerHelper;
import com.ztesoft.uosflow.core.processor.AbstactMqPullInterface;
import com.ztesoft.uosflow.jmx.server.FqueueServer;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public class FqueueMqAdap extends AbstactMqPullInterface{

	private FqueueServer fqueueServer;
	private String queueName;
	private int queuePrefetch;

	public FqueueServer getFqueueServer() {
		return fqueueServer;
	}

	public void setFqueueServer(FqueueServer fqueueServer) {
		this.fqueueServer = fqueueServer;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public int getQueuePrefetch() {
		return queuePrefetch;
	}

	public void setQueuePrefetch(int queuePrefetch) {
		this.queuePrefetch = queuePrefetch;
	}

	public void init() {

	}

	@Override
	public MessageDto getObject() throws Exception {
		FQueue fqueue = fqueueServer.getFqueue(queueName);
		byte[] bytes = fqueue.poll();
		MessageDto messageDto = (MessageDto) SerializerHelper
				.deSerialize(bytes);
		return messageDto;
	}
}
