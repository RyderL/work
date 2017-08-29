package com.ztesoft.uosflow.util.mq.consumer.rocketmq;

import java.util.Set;
import java.util.UUID;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.alibaba.rocketmq.client.ClientConfig;
import com.alibaba.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.MessageQueueListener;
import com.alibaba.rocketmq.client.consumer.store.OffsetStore;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

public class RocketMQConsumerFactory extends BasePooledObjectFactory<DefaultMQPullConsumer> {
	/**
	 * RocketMQ producer和consumer公共配置
	 */
	private ClientConfig clientConfig = null;
	/**
	 * 长轮询模式，Consumer连接在Broker挂起最长时间，不建议修改
	 */
	Long brokerSuspendMaxTimeMillis = null;
	/**
	 * 长轮询模式，Consumer超时时间（必须要大于brokerSuspendMaxTimeMillis），不建议修改
	 */
	Long consumerTimeoutMillisWhenSuspend = null;
	/**
	 * 非阻塞拉模式，Consumer超时时间，不建议修改
	 */
	Long consumerPullTimeoutMillis = null;
	/**
	 * 集群消费/广播消费，默认是集群消费模式
	 */
	MessageModel messageModel = null;
	/**
	 * Offset存储，系统会根据客户端配置自动创建相应的实现，如果应用配置了，则以应用配置的为主
	 */
	OffsetStore offsetStore = null;
	/**
	 * 队列变化监听器
	 */
	MessageQueueListener messageQueueListener = null;
	/**
	 * 需要监听哪些Topic的队列变化
	 */
	Set<String> registerTopics = null;
	/**
	 * 队列分配算法，应用可重写
	 */
	AllocateMessageQueueStrategy allocateMessageQueueStrategy = null;

	private String groupName;

	@Override
	public DefaultMQPullConsumer create() throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer();
		if (clientConfig != null) {
			consumer.resetClientConfig(clientConfig);
		}
		if (groupName != null) {
			consumer.setConsumerGroup(groupName);
		} else {
			consumer.setConsumerGroup("UOSFLOW-CONSUMER");
		}
		if (brokerSuspendMaxTimeMillis != null) {
			consumer.setBrokerSuspendMaxTimeMillis(brokerSuspendMaxTimeMillis);
		}
		if (consumerTimeoutMillisWhenSuspend != null) {
			consumer.setConsumerTimeoutMillisWhenSuspend(consumerTimeoutMillisWhenSuspend);
		}
		if (consumerPullTimeoutMillis != null) {
			consumer.setConsumerPullTimeoutMillis(consumerPullTimeoutMillis);
		}
		if (messageModel != null) {
			consumer.setMessageModel(messageModel);
		} else {
			consumer.setMessageModel(MessageModel.CLUSTERING);
		}
		if (offsetStore != null) {
			consumer.setOffsetStore(offsetStore);
		}
		if (messageQueueListener != null) {
			consumer.setMessageQueueListener(messageQueueListener);
		}
		if (registerTopics != null) {
			consumer.setRegisterTopics(registerTopics);
		}
		if (allocateMessageQueueStrategy != null) {
			consumer.setAllocateMessageQueueStrategy(allocateMessageQueueStrategy);
		}
		consumer.setInstanceName(groupName + "-" + UUID.randomUUID());
		consumer.start();

		/* Rocketmq存在bug，这里需要延迟10秒左右 */
		Thread.sleep(3000);

		return consumer;
	}

	@Override
	public PooledObject<DefaultMQPullConsumer> wrap(DefaultMQPullConsumer consumer) {
		return new DefaultPooledObject<DefaultMQPullConsumer>(consumer);
	}

	@Override
	public void destroyObject(PooledObject<DefaultMQPullConsumer> p) throws Exception {
		p.getObject().shutdown();
	}

	public void setClientConfig(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	public void setBrokerSuspendMaxTimeMillis(Long brokerSuspendMaxTimeMillis) {
		this.brokerSuspendMaxTimeMillis = brokerSuspendMaxTimeMillis;
	}

	public void setConsumerTimeoutMillisWhenSuspend(Long consumerTimeoutMillisWhenSuspend) {
		this.consumerTimeoutMillisWhenSuspend = consumerTimeoutMillisWhenSuspend;
	}

	public void setConsumerPullTimeoutMillis(Long consumerPullTimeoutMillis) {
		this.consumerPullTimeoutMillis = consumerPullTimeoutMillis;
	}

	public void setMessageModel(MessageModel messageModel) {
		this.messageModel = messageModel;
	}

	public void setOffsetStore(OffsetStore offsetStore) {
		this.offsetStore = offsetStore;
	}

	public void setMessageQueueListener(MessageQueueListener messageQueueListener) {
		this.messageQueueListener = messageQueueListener;
	}

	public void setRegisterTopics(Set<String> registerTopics) {
		this.registerTopics = registerTopics;
	}

	public void setAllocateMessageQueueStrategy(AllocateMessageQueueStrategy allocateMessageQueueStrategy) {
		this.allocateMessageQueueStrategy = allocateMessageQueueStrategy;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
