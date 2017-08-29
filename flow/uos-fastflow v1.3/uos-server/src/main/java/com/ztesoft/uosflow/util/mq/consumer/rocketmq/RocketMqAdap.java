package com.ztesoft.uosflow.util.mq.consumer.rocketmq;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.PullResult;
import com.alibaba.rocketmq.client.consumer.PullStatus;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.zterc.uos.base.helper.SerializerHelper;
import com.ztesoft.uosflow.core.processor.AbstactMqPullInterface;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public class RocketMqAdap  extends AbstactMqPullInterface  {
	private static Logger logger = LoggerFactory.getLogger(RocketMqAdap.class);

	private String queueName;
	private RocketMQConsumerFactory rocketMQConsumerFactory;

	private DefaultMQPullConsumer consumer;

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setRocketMQConsumerFactory(
			RocketMQConsumerFactory rocketMQConsumerFactory) {
		this.rocketMQConsumerFactory = rocketMQConsumerFactory;
	}

	public RocketMqAdap() {

	}

	public void init() throws Exception {
		try {
			consumer = rocketMQConsumerFactory.create();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public MessageDto getObject() throws Exception {
		MessageDto messageDto = null;
		
		try {
			Set<MessageQueue> messages = consumer
					.fetchSubscribeMessageQueues(this.queueName);

			for (MessageQueue queue : messages) {
				long offset = consumer.fetchConsumeOffset(queue, false);
				
				PullResult pullResult = consumer.pull(queue,
						null, offset == -1 ? 0 : offset, 1);
				
				consumer.updateConsumeOffset(queue,pullResult.getNextBeginOffset());
				
				consumer.getDefaultMQPullConsumerImpl().getOffsetStore().persist(queue);
				
				if (PullStatus.FOUND.equals(pullResult.getPullStatus())) {
					if (pullResult.getMsgFoundList().size() > 0) {
						messageDto = (MessageDto)SerializerHelper.deSerialize(pullResult
								.getMsgFoundList().get(0).getBody());
						logger.info("Receive From " + queue.getTopic() + "-"
								+ queue.getBrokerName() + "-"
								+ queue.getQueueId());
						logger.info("=======接收消息======" + messageDto.getCommandDto().getCommandCode());
						return messageDto;
					}
				}
			}

		} catch (Exception ex) {
			throw ex;
		}
		return messageDto;
	}
}
