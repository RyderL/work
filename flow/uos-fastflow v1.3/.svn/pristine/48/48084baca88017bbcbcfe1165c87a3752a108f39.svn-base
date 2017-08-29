package com.ztesoft.uosflow.util.mq.consumer.rocketmq;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.ClientConfig;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.zterc.uos.base.helper.SerializerHelper;
import com.ztesoft.uosflow.core.processor.AbstactMqPushInterface;
import com.ztesoft.uosflow.core.processor.push.MqPushThread;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public class RocketMqPushAdap extends AbstactMqPushInterface{
	private static Logger logger = LoggerFactory.getLogger(RocketMqAdap.class);

	private String queueName;

	private ClientConfig clientConfig;

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public ClientConfig getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	public RocketMqPushAdap() {

	}

	@Override
	public void start(final MqPushThread thread) throws Exception {
		try {
			DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
					queueName + "-Consumer");
			consumer.setNamesrvAddr(clientConfig.getNamesrvAddr());
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			consumer.subscribe(queueName, "*");
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(
						List<MessageExt> msgs,
						ConsumeConcurrentlyContext context) {
					MessageDto messageDto = null;
					try {
						Message message = msgs.get(0);
						messageDto = (MessageDto) SerializerHelper
								.deSerialize(message.getBody());
						if (logger.isDebugEnabled()) {
							logger.debug("KEY:" + message.getKeys() + ",body:"
									+ new String(message.getBody(), "GBK"));
						}
						thread.dealMessageDto(messageDto);
					} catch (Exception e) {
						if (messageDto != null) {
							logger.error(
									e.getMessage() + ":"
											+ messageDto.getCommandDto(), e);
						} else {
							logger.error(e.getMessage(), e);
						}
					}
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			consumer.start();
		} catch (MQClientException e) {
			throw new RuntimeException(e);
		}
	}
}
