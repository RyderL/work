package com.ztesoft.uosflow.jmx.server.bl.fqueuemxbean;

import com.google.code.fqueue.FQueue;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.jmx.server.FqueueServer;

public class FQueueManager implements FQueueManagerMXBean {

	@Override
	public long count(String queueName) throws Exception {
		FqueueServer fqueueServer = (FqueueServer) ApplicationContextProxy
				.getBean("fqueueServer");
		FQueue fQueue = fqueueServer.getFqueue(queueName);
		long count = fQueue.size();
		return count;
	}
}
