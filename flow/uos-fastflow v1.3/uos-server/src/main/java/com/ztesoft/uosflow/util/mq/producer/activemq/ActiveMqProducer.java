package com.ztesoft.uosflow.util.mq.producer.activemq;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ztesoft.uosflow.util.mq.config.ActiveMqServer;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;
import com.ztesoft.uosflow.util.mq.producer.inf.IMqProducer;

public class ActiveMqProducer implements IMqProducer {
	private static final Logger logger = LoggerFactory.getLogger(ActiveMqProducer.class);
	private ActiveMqServer activeMqServer;// ������Ϣ
	private String producerName;// ����������
	private String queueName;// ��������
	private boolean isPersistent;// ��Ϣ�Ƿ�־û�

	private boolean useAsyncSend;// �Ƿ��첽������Ϣ
	private int threadNum;// �߳���
	private ExecutorService threadPool;// �̳߳ض���
	private PooledConnectionFactory pooledConnectionFactory;
	private int maxConnections;// ���������
	private int maximumActiveSessionPerConnection;// ÿ��������ʹ�õ�����Ự��

	/**
	 * �޲ι��캯��
	 * 
	 */
	public ActiveMqProducer() {

	}

	public ActiveMqServer getActiveMqServer() {
		return activeMqServer;
	}

	public void setActiveMqServer(ActiveMqServer activeMqServer) {
		this.activeMqServer = activeMqServer;
	}

	/**
	 * ����������
	 * 
	 */
	public void start() {
		ActiveMQConnectionFactory activeMQConnectionFactory = (ActiveMQConnectionFactory) activeMqServer
				.getConnectionFactory();
		activeMQConnectionFactory.setUseAsyncSend(this.useAsyncSend);
		// �������ӳع���
		this.pooledConnectionFactory = new PooledConnectionFactory(
				activeMQConnectionFactory);
		this.pooledConnectionFactory.setCreateConnectionOnStartup(true);
		this.pooledConnectionFactory.setMaxConnections(this.maxConnections);
		this.pooledConnectionFactory
				.setMaximumActiveSessionPerConnection(this.maximumActiveSessionPerConnection);
		// �����̳߳ض���
		this.threadPool = Executors.newFixedThreadPool(this.threadNum);
	}

	/**
	 * ʹ���̳߳ط��Ͷ���ָ����ActiveMQ����
	 * 
	 * @param obj
	 */
	@Override
	public void send(final String queueName, final MessageDto obj) {
		if (this.useAsyncSend) {
			// ֱ��ʹ���̳߳���ִ��
			this.threadPool.execute(new Runnable() {

				@Override
				public void run() {
					sendMessage(queueName, obj);
				}

			});
		} else {
			sendMessage(queueName, obj);
		}
	}

	/**
	 * ���Ͷ���ActiveMQ����
	 * 
	 * @param obj
	 */
	private void sendMessage(String queueName, MessageDto obj) {
		Connection connection = null;
		Session session = null;

		try {
			// �����ӳع����л�ȡ����
			connection = this.pooledConnectionFactory.createConnection();
			// �����Ự���������Զ�Ӧ��ģʽ
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);

			if (session != null) {
				// ��������Ŀ�ĵ�
				Destination destination = session.createQueue(queueName);
				// ����������
				MessageProducer producer = session.createProducer(destination);
				// ������Ϣ�Ƿ�־û�
				producer.setDeliveryMode(this.isPersistent ? DeliveryMode.PERSISTENT
						: DeliveryMode.NON_PERSISTENT);
				// ������Ϣ����
				Message message = this.getMessage(session, obj);
				// ������Ϣ
				producer.send(message);
			}
		} catch (JMSException e) {
			logger.error(
					this.producerName + "�����Ͷ���ActiveMQ�����쳣��"
							+ e.getLocalizedMessage(), e);
			// ����ʧ�ܱ�������
			// mqManager.saveMQException(producerName, queueName,
			// MQExceptionDto.PRODUCER_EXCEPTION, (MessageDto) obj, e);
		} finally {
			if (session != null) {
				try {
					if (session != null) {
						session.close();
					}
				} catch (JMSException e) {
					logger.error("�����߹رջỰ�쳣��" + e.getLocalizedMessage(), e);
				}
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (JMSException e) {
					logger.error("�����߹ر������쳣��" + e.getLocalizedMessage(), e);
				}
			}
		}
	}

	@Override
	public void send(final String queueName, final MessageDto obj,
			final int priority, final String groupName) {
		if (this.useAsyncSend) {
			// ֱ��ʹ���̳߳���ִ��
			this.threadPool.execute(new Runnable() {

				@Override
				public void run() {
					sendMessage(queueName, obj, priority, groupName);
				}

			});
		} else {
			sendMessage(queueName, obj, priority, groupName);
		}
	}

	protected void sendMessage(String queueName, MessageDto obj, int priority,
			String groupName) {
		Connection connection = null;
		Session session = null;

		try {
			// �����ӳع����л�ȡ����
			connection = this.pooledConnectionFactory.createConnection();
			// �����Ự���������Զ�Ӧ��ģʽ
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			if (session != null) {
				// ��������Ŀ�ĵ�
				Destination destination = session.createQueue(queueName);
				// ����������
				MessageProducer producer = session.createProducer(destination);
				// ������Ϣ�Ƿ�־û�
				producer.setDeliveryMode(this.isPersistent ? DeliveryMode.PERSISTENT
						: DeliveryMode.NON_PERSISTENT);
				// ������Ϣ����
				Message message = this.getMessage(session, obj);
				// ������Ϣ���� add by che.zi 2015-02-04 begin
				message.setStringProperty("JMSXGroupID", groupName);
				// end
				// ������Ϣ
				// ������Ϣ���ȼ� add by che.zi 2015-02-03 begin
				producer.setPriority(priority);
				// end
				producer.send(message);
			}
		} catch (JMSException e) {
			logger.error(
					this.producerName + "�����Ͷ���ActiveMQ�����쳣��"
							+ e.getLocalizedMessage(), e);
			// ����ʧ�ܱ�������
			// mqManager.saveMQException(producerName, queueName,
			// MQExceptionDto.PRODUCER_EXCEPTION, (MessageDto) obj, e);
		} finally {
			if (session != null) {
				try {
					if (session != null) {
						session.close();
					}
				} catch (JMSException e) {
					logger.error("�����߹رջỰ�쳣��" + e.getLocalizedMessage(), e);
				}
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (JMSException e) {
					logger.error("�����߹ر������쳣��" + e.getLocalizedMessage(), e);
				}
			}
		}
	}

	/**
	 * ����obj���ͻ�ö�Ӧ��Message����
	 * 
	 * @return
	 * @throws JMSException
	 */
	private Message getMessage(Session session, Object obj) throws JMSException {
		Message message = null;
		message = session.createObjectMessage((Serializable) obj);
		return message;
	}

	public String getProducerName() {
		return producerName;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	public boolean isPersistent() {
		return isPersistent;
	}

	public void setPersistent(boolean isPersistent) {
		this.isPersistent = isPersistent;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public boolean isUseAsyncSend() {
		return useAsyncSend;
	}

	public void setUseAsyncSend(boolean useAsyncSend) {
		this.useAsyncSend = useAsyncSend;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public int getMaximumActiveSessionPerConnection() {
		return maximumActiveSessionPerConnection;
	}

	public void setMaximumActiveSessionPerConnection(
			int maximumActiveSessionPerConnection) {
		this.maximumActiveSessionPerConnection = maximumActiveSessionPerConnection;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
}
