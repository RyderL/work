package com.ztesoft.uosflow.core.processor;

import com.ztesoft.uosflow.util.mq.dto.MessageDto;

public interface EventInterface {

	/**
	 * �¼������߼�
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean deal(MessageDto request) throws Exception;

}
