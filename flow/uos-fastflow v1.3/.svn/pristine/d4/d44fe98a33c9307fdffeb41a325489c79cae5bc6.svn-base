package com.ztesoft.uosflow.util.mq.producer.rocketmq;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.zterc.uos.base.helper.SerializerHelper;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;
import com.ztesoft.uosflow.util.mq.producer.inf.IMqProducer;

public class RocketMqProducer implements IMqProducer {
	private Logger logger = LoggerFactory.getLogger(RocketMqProducer.class);

	private GenericObjectPool<DefaultMQProducer> genericObjectPool;

	public GenericObjectPool<DefaultMQProducer> getGenericObjectPool() {
		return genericObjectPool;
	}

	public void setGenericObjectPool(
			GenericObjectPool<DefaultMQProducer> genericObjectPool) {
		this.genericObjectPool = genericObjectPool;
	}

	public void start() {
	}

	@Override
	public void send(final String queueName, final MessageDto obj) {
		sendMessage(queueName, obj);
	}

	@Override
	public void send(final String queueName, final MessageDto obj,
			final int priority, final String groupName) {
		sendMessage(queueName, obj);
	}

	private void sendMessage(final String queueName, final MessageDto obj) {
		DefaultMQProducer producer = null;
		try {
			Message msg = new Message(queueName,
					SerializerHelper.serialize(obj));
			producer = genericObjectPool.borrowObject();
			if (logger.isInfoEnabled()) {
				logger.info("=====getNumActive====="
						+ genericObjectPool.getNumActive());
			}

			producer.send(msg, new SendCallback() {

				@Override
				public void onSuccess(SendResult sendResult) {
					if (logger.isInfoEnabled()) {
						logger.info("=====发送成功=====" + queueName + ";"
								+ sendResult);
					}
				}

				@Override
				public void onException(Throwable e) {
					if (logger.isInfoEnabled()) {
						logger.info("=====发送失败=====" + queueName + ";" + e);
					}
				}
			});
		} catch (Exception e) {
			throw new RuntimeException("发送消息异常", e);
		} finally {
			if (producer != null) {
				genericObjectPool.returnObject(producer);
			}
		}
	}

}
