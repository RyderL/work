package com.ztesoft.uosflow.util.mq.producer.inf;

import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public interface IMqProducer {

	public void send(final String queueName, final MessageDto obj);

	public void send(final String queueName, final MessageDto obj,
			final int priority, final String groupName);
}
