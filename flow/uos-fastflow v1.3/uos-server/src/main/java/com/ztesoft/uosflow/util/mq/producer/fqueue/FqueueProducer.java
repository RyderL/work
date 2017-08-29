package com.ztesoft.uosflow.util.mq.producer.fqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.fqueue.FQueue;
import com.zterc.uos.base.helper.SerializerHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.ztesoft.uosflow.jmx.server.FqueueServer;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;
import com.ztesoft.uosflow.util.mq.producer.inf.IMqProducer;

public class FqueueProducer implements IMqProducer {
	private static final Logger logger = LoggerFactory.getLogger(FqueueProducer.class);
	private FqueueServer fqueueServer;
	private String producerName;// ����������

	public FqueueServer getFqueueServer() {
		return fqueueServer;
	}

	public void setFqueueServer(FqueueServer fqueueServer) {
		this.fqueueServer = fqueueServer;
	}

	public String getProducerName() {
		return producerName;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	@Override
	public void send(String queueName, MessageDto obj) {
		this.sendMessage(queueName, obj);
	}

	@Override
	public void send(String queueName, MessageDto obj, int priority,
			String groupName) {
		this.sendMessage(queueName, obj);
	}

	private void sendMessage(String queueName, MessageDto obj) {
		try {
			FQueue fqueue = fqueueServer.getFqueue(queueName);
			if (fqueue == null) {
				logger.error("����" + queueName + "δ��"+FastflowConfig.commandCfgTableName+"�������ã�");
				throw new Exception("����" + queueName + "δ��"+FastflowConfig.commandCfgTableName+"�������ã�");
			} else {
				fqueue.add(SerializerHelper.serialize(obj));
			}
		} catch (Exception e) {
			logger.error(this.producerName + ",���Ͷ���fqueue����" + queueName
					+ "�쳣��" + e.getLocalizedMessage(), e);
		}
	}

}
