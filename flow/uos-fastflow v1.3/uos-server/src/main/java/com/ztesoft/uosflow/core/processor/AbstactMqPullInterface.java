package com.ztesoft.uosflow.core.processor;

import com.ztesoft.uosflow.core.processor.push.MqPushThread;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public abstract class AbstactMqPullInterface implements MqInterface {

	@Override
	public abstract MessageDto getObject() throws Exception;

	@Override
	public void start(MqPushThread consumer) throws Exception {
		//null
	}
	
	@Override
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
