package com.ztesoft.uosflow.util.mq.producer.rocketmq;

import java.util.UUID;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.ClientConfig;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

public class RocketMQProducerFactory extends
		BasePooledObjectFactory<DefaultMQProducer> {
	private static final Logger logger = LoggerFactory.getLogger(RocketMQProducerFactory.class);
	
	/**
	 * RocketMQ producer和consumer公共配置
	 */
	private ClientConfig clientConfig = null;
	/**
	 * 发送消息超时，不建议修改
	 */
	private Integer sendMsgTimeout;
	/**
	 * Message Body大小超过阀值，则压缩
	 */
	private Integer compressMsgBodyOverHowmuch = 4 * 1024;
	/**
	 * 发送失败后，重试几次，默认为2次
	 */
	private Boolean retryAnotherBrokerWhenNotStoreOK;
	/**
	 * 最大消息大小，默认512K
	 */
	private Integer maxMessageSize;

	private String groupName;
	
	private String instanceName;

	@Override
	public DefaultMQProducer create() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer();
		if (clientConfig != null) {
			producer.resetClientConfig(clientConfig);
			logger.info("producer rocketMQ:"+clientConfig.getNamesrvAddr());
		}
		if (groupName != null) {
			producer.setProducerGroup(groupName);
		}else {
			producer.setProducerGroup("UOSFLOW-PRODUCER");
		}
		if (instanceName != null) {
			producer.setInstanceName(instanceName);
		}else{
			producer.setInstanceName("UOSFLOW-PRODUCER"+UUID.randomUUID());
		}
		if (sendMsgTimeout != null) {
			producer.setSendMsgTimeout(sendMsgTimeout);
		}
		if (compressMsgBodyOverHowmuch != null) {
			producer.setCompressMsgBodyOverHowmuch(compressMsgBodyOverHowmuch);
		}
		if (retryAnotherBrokerWhenNotStoreOK != null) {
			producer.setRetryAnotherBrokerWhenNotStoreOK(retryAnotherBrokerWhenNotStoreOK);
		}
		if (maxMessageSize != null) {
			producer.setMaxMessageSize(maxMessageSize);
		}
		producer.start();
		return producer;
	}

	@Override
	public PooledObject<DefaultMQProducer> wrap(DefaultMQProducer producer) {
		return new DefaultPooledObject<DefaultMQProducer>(producer);
	}

	@Override
	public void destroyObject(PooledObject<DefaultMQProducer> p)
			throws Exception {
		p.getObject().shutdown();
	}

	public ClientConfig getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	public Integer getSendMsgTimeout() {
		return sendMsgTimeout;
	}

	public void setSendMsgTimeout(Integer sendMsgTimeout) {
		this.sendMsgTimeout = sendMsgTimeout;
	}

	public Integer getCompressMsgBodyOverHowmuch() {
		return compressMsgBodyOverHowmuch;
	}

	public void setCompressMsgBodyOverHowmuch(Integer compressMsgBodyOverHowmuch) {
		this.compressMsgBodyOverHowmuch = compressMsgBodyOverHowmuch;
	}

	public Boolean getRetryAnotherBrokerWhenNotStoreOK() {
		return retryAnotherBrokerWhenNotStoreOK;
	}

	public void setRetryAnotherBrokerWhenNotStoreOK(
			Boolean retryAnotherBrokerWhenNotStoreOK) {
		this.retryAnotherBrokerWhenNotStoreOK = retryAnotherBrokerWhenNotStoreOK;
	}

	public Integer getMaxMessageSize() {
		return maxMessageSize;
	}

	public void setMaxMessageSize(Integer maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
}
