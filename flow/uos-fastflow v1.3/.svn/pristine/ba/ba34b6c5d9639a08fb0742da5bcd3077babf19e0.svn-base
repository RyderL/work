package com.ztesoft.uosflow.util.mq.consumer.activemq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;

import com.ztesoft.uosflow.core.processor.AbstactMqPullInterface;
import com.ztesoft.uosflow.util.mq.config.ActiveMqServer;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public class ActiveMqAdap extends AbstactMqPullInterface implements ExceptionListener  {

	private ActiveMqServer activeMqServer;

	private Connection connection;// JMS 客户端到JMS Provider 的连接
	private Session session;// 一个发送或接收消息的线程
	private Destination destination;// 消息的目的地
	private int queuePrefetch;// 队列预取策略
	private String queueName;
	private MessageConsumer consumer;

	public ActiveMqServer getActiveMqServer() {
		return activeMqServer;
	}

	public void setActiveMqServer(ActiveMqServer activeMqServer) {
		this.activeMqServer = activeMqServer;
	}

	public void setQueuePrefetch(int queuePrefetch) {
		this.queuePrefetch = queuePrefetch;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public ActiveMqAdap() {

	}

	public void init() {
		try {
			// 创建链接对象
			connection = ((ActiveMQConnectionFactory) activeMqServer
					.getConnectionFactory()).createConnection();
			// activeMQ预取策略
			ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
			prefetchPolicy.setQueuePrefetch(queuePrefetch);
			((ActiveMQConnection) connection).setPrefetchPolicy(prefetchPolicy);
			// 设置链接异常监听器
			connection.setExceptionListener(this);
			// 链接对象启动
			connection.start();
			// 创建会话，无事务，自动应答模式
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			// 创建队列目的地 
			destination = session.createQueue(this.queueName);
			consumer = session.createConsumer(destination);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public MessageDto getObject() {
		MessageDto messageDto = null;
		try {
			Message msg = consumer.receive();
			ObjectMessage objMsg = (ObjectMessage) msg;
			messageDto = (MessageDto) objMsg.getObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return messageDto;
	}

	@Override
	public void onException(JMSException arg0) {

	}
}
