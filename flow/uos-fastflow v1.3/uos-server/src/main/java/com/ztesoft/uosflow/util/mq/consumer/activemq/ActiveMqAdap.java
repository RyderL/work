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

	private Connection connection;// JMS �ͻ��˵�JMS Provider ������
	private Session session;// һ�����ͻ������Ϣ���߳�
	private Destination destination;// ��Ϣ��Ŀ�ĵ�
	private int queuePrefetch;// ����Ԥȡ����
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
			// �������Ӷ���
			connection = ((ActiveMQConnectionFactory) activeMqServer
					.getConnectionFactory()).createConnection();
			// activeMQԤȡ����
			ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
			prefetchPolicy.setQueuePrefetch(queuePrefetch);
			((ActiveMQConnection) connection).setPrefetchPolicy(prefetchPolicy);
			// ���������쳣������
			connection.setExceptionListener(this);
			// ���Ӷ�������
			connection.start();
			// �����Ự���������Զ�Ӧ��ģʽ
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			// ��������Ŀ�ĵ� 
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
