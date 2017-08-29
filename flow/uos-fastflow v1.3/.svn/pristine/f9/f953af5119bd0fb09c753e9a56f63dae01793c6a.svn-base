package com.ztesoft.uosflow.core.processor;

import com.ztesoft.uosflow.core.processor.push.MqPushThread;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public abstract class AbstactMqPushInterface implements MqInterface {
	public abstract void start(MqPushThread thread) throws Exception;

	public MessageDto getObject() throws Exception {
		return null;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
