package com.ztesoft.uosflow.core.processor;

import com.ztesoft.uosflow.core.processor.push.MqPushThread;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public interface MqInterface extends Cloneable {
	public MessageDto getObject() throws Exception;

	public void start(MqPushThread consumer) throws Exception;
	
	public Object clone();
	
	public void setQueueName(String queueName);
}
