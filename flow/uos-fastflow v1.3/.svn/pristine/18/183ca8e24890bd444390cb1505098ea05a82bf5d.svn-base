package com.ztesoft.uosflow.util.mq.consumer.common;

import org.springframework.stereotype.Component;

import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.common.TimeCostProxy;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;

@Component("mqConsumerProxy")
public class MqConsumerProxy{
//	private static Logger logger = LoggerFactory.getLogger(MqConsumerProxy.class);
	
	/**
	 * 私有化无参构造函数
	 */
	private MqConsumerProxy() {
	}

	public static MqConsumerProxy getInstance() {
		return (MqConsumerProxy)ApplicationContextProxy.getBean("mqConsumerProxy");
	}
	
	public void dealMessage(MessageDto msgDto)
	{
		String commandCode = msgDto.getCommandDto().getCommandCode();
		if("costTime".equals(commandCode)){
			//拦截器用来做计算时间消耗.
			TimeCostProxy.getInstance().costTime(msgDto.getCommandDto());
		}else{
			CommandProxy.getInstance().dealCommand(msgDto.getCommandDto(),true);
		}
		
	}
}
