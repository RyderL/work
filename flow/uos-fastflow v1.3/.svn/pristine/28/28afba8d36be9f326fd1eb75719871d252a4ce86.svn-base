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
	private ActiveMqServer activeMqServer;// 配置信息
	private String producerName;// 生产者名称
	private String queueName;// 队列名称
	private boolean isPersistent;// 消息是否持久化

	private boolean useAsyncSend;// 是否异步发送消息
	private int threadNum;// 线程数
	private ExecutorService threadPool;// 线程池对象
	private PooledConnectionFactory pooledConnectionFactory;
	private int maxConnections;// 最大连接数
	private int maximumActiveSessionPerConnection;// 每个连接中使用的最大活动会话数

	/**
	 * 无参构造函数
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
	 * 生产者启动
	 * 
	 */
	public void start() {
		ActiveMQConnectionFactory activeMQConnectionFactory = (ActiveMQConnectionFactory) activeMqServer
				.getConnectionFactory();
		activeMQConnectionFactory.setUseAsyncSend(this.useAsyncSend);
		// 创建连接池工厂
		this.pooledConnectionFactory = new PooledConnectionFactory(
				activeMQConnectionFactory);
		this.pooledConnectionFactory.setCreateConnectionOnStartup(true);
		this.pooledConnectionFactory.setMaxConnections(this.maxConnections);
		this.pooledConnectionFactory
				.setMaximumActiveSessionPerConnection(this.maximumActiveSessionPerConnection);
		// 创建线程池对象
		this.threadPool = Executors.newFixedThreadPool(this.threadNum);
	}

	/**
	 * 使用线程池发送对象到指定的ActiveMQ队列
	 * 
	 * @param obj
	 */
	@Override
	public void send(final String queueName, final MessageDto obj) {
		if (this.useAsyncSend) {
			// 直接使用线程池来执行
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
	 * 发送对象到ActiveMQ队列
	 * 
	 * @param obj
	 */
	private void sendMessage(String queueName, MessageDto obj) {
		Connection connection = null;
		Session session = null;

		try {
			// 从链接池工程中获取链接
			connection = this.pooledConnectionFactory.createConnection();
			// 创建会话，无事务，自动应答模式
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);

			if (session != null) {
				// 创建队列目的地
				Destination destination = session.createQueue(queueName);
				// 创建生产者
				MessageProducer producer = session.createProducer(destination);
				// 设置消息是否持久化
				producer.setDeliveryMode(this.isPersistent ? DeliveryMode.PERSISTENT
						: DeliveryMode.NON_PERSISTENT);
				// 创建消息对象
				Message message = this.getMessage(session, obj);
				// 发送消息
				producer.send(message);
			}
		} catch (JMSException e) {
			logger.error(
					this.producerName + "，发送对象到ActiveMQ队列异常："
							+ e.getLocalizedMessage(), e);
			// 发送失败保存起来
			// mqManager.saveMQException(producerName, queueName,
			// MQExceptionDto.PRODUCER_EXCEPTION, (MessageDto) obj, e);
		} finally {
			if (session != null) {
				try {
					if (session != null) {
						session.close();
					}
				} catch (JMSException e) {
					logger.error("生产者关闭会话异常：" + e.getLocalizedMessage(), e);
				}
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (JMSException e) {
					logger.error("生产者关闭链接异常：" + e.getLocalizedMessage(), e);
				}
			}
		}
	}

	@Override
	public void send(final String queueName, final MessageDto obj,
			final int priority, final String groupName) {
		if (this.useAsyncSend) {
			// 直接使用线程池来执行
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
			// 从链接池工程中获取链接
			connection = this.pooledConnectionFactory.createConnection();
			// 创建会话，无事务，自动应答模式
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			if (session != null) {
				// 创建队列目的地
				Destination destination = session.createQueue(queueName);
				// 创建生产者
				MessageProducer producer = session.createProducer(destination);
				// 设置消息是否持久化
				producer.setDeliveryMode(this.isPersistent ? DeliveryMode.PERSISTENT
						: DeliveryMode.NON_PERSISTENT);
				// 创建消息对象
				Message message = this.getMessage(session, obj);
				// 设置消息分组 add by che.zi 2015-02-04 begin
				message.setStringProperty("JMSXGroupID", groupName);
				// end
				// 发送消息
				// 设置消息优先级 add by che.zi 2015-02-03 begin
				producer.setPriority(priority);
				// end
				producer.send(message);
			}
		} catch (JMSException e) {
			logger.error(
					this.producerName + "，发送对象到ActiveMQ队列异常："
							+ e.getLocalizedMessage(), e);
			// 发送失败保存起来
			// mqManager.saveMQException(producerName, queueName,
			// MQExceptionDto.PRODUCER_EXCEPTION, (MessageDto) obj, e);
		} finally {
			if (session != null) {
				try {
					if (session != null) {
						session.close();
					}
				} catch (JMSException e) {
					logger.error("生产者关闭会话异常：" + e.getLocalizedMessage(), e);
				}
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (JMSException e) {
					logger.error("生产者关闭链接异常：" + e.getLocalizedMessage(), e);
				}
			}
		}
	}

	/**
	 * 根据obj类型获得对应的Message对象
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
