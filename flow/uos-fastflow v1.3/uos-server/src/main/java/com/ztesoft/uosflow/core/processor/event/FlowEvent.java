package com.ztesoft.uosflow.core.processor.event;

import org.apache.log4j.Logger;

import com.zterc.uos.base.helper.GsonHelper;
import com.ztesoft.uosflow.core.processor.EventInterface;
import com.ztesoft.uosflow.util.mq.consumer.common.MqConsumerProxy;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public class FlowEvent implements EventInterface {
	private Logger logger = Logger.getLogger(FlowEvent.class);
	public FlowEvent() {
	}

	@Override
	public boolean deal(MessageDto messageDto) throws Exception {
		try {
			MqConsumerProxy.getInstance().dealMessage(messageDto);
		} catch (Exception ex) {
			logger.error("消息处理异常："+ GsonHelper.toJson(messageDto));
		}
		return true;
	}

}
