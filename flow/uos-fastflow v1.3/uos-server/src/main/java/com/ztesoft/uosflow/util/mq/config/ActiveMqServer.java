package com.ztesoft.uosflow.util.mq.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ActiveMqServer {

	private Log logger = LogFactory.getLog(this.getClass());

	private String userName;// 用户名
	private String password;// 密码
	private String brokerURL;// 地址

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBrokerURL() {
		return brokerURL;
	}

	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}

	public ActiveMqServer() {
	}

	private ConnectionFactory connectionFactory;// 连接工厂，JMS 用它创建连接

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void start() {
		try {
			// 创建链接工厂
			connectionFactory = new ActiveMQConnectionFactory(
					this.getUserName(), this.getPassword(), this.getBrokerURL());
		} catch (Exception e) {
			logger.error("ActiveMQ的工厂类启动异常：" + e.getLocalizedMessage(), e);
		}
	}

}
