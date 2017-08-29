package com.ztesoft.uosflow.inf.server.restful;

import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.fqueue.FQueue;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.inf.util.ServerJsonUtil;
import com.ztesoft.uosflow.jmx.server.FqueueServer;
import com.ztesoft.uosflow.util.mq.producer.common.MqProducerProxy;

@Controller
@RequestMapping("/queueOper")
@Configuration
public class QueueOperController {

	@RequestMapping(value = "/sendMessage",produces = "text/html;charset=UTF-8")
    public @ResponseBody String sendMessage(String param) {
		Map<String,Object> paramMap = GsonHelper.toMap(param);
		String commandCode = StringHelper.valueOf(paramMap.get(InfConstant.INF_COMMAND_CODE));
		CommandDto commandDto = ServerJsonUtil.getCommandDtoFromJson(commandCode,paramMap);
		MqProducerProxy.getInstance().dealCommandAsyn(commandDto);
		return "success";
    }

	@RequestMapping(value = "/getFqueueSize",produces = "text/html;charset=UTF-8")
    public @ResponseBody String getFqueueSize(String queueName) {
		FqueueServer fqueueServer = (FqueueServer) ApplicationContextProxy
				.getBean("fqueueServer");
		FQueue fQueue = fqueueServer.getFqueue(queueName);
		int count = fQueue.size();
		return GsonHelper.toJson(StringHelper.valueOf(count));
    }
}
