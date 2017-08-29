package com.ztesoft.uosflow.util.mq.producer.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.dto.specification.CommandCfgDto;
import com.zterc.uos.fastflow.exception.FastflowException;
import com.zterc.uos.fastflow.service.CommandCfgService;
import com.zterc.uos.fastflow.service.WorkItemService;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.server.CompleteWorkItemDto;
import com.ztesoft.uosflow.core.util.CommandPropUtil;
import com.ztesoft.uosflow.core.util.CommandResultDtoUtil;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;
import com.ztesoft.uosflow.util.mq.producer.inf.IMqProducer;

@Component("mqProducerProxy")
@Lazy
public class MqProducerProxy {
	private static Logger logger = LoggerFactory.getLogger(MqProducerProxy.class);

	@Autowired
	private CommandCfgService commandCfgService;
	
	@Autowired
	private WorkItemService workItemService;

	@Autowired
	private IMqProducer mqProducer;
	
	private Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

	/**
	 * ˽�л��޲ι��캯��
	 */
	private MqProducerProxy() {

	}

	public static MqProducerProxy getInstance() {
		return (MqProducerProxy) ApplicationContextProxy
				.getBean("mqProducerProxy");
	}

	@PostConstruct
	public void init() {
		List<CommandCfgDto> retList = commandCfgService.qryComandCfgs();
		for (int i = 0; i < retList.size(); i++) {
			CommandCfgDto dto = retList.get(i);
			Map<String, String> regionMap = map.get(dto.getCommandCode());
			if (regionMap == null) {
				regionMap = new HashMap<String, String>();
				map.put(dto.getCommandCode(),regionMap);
			}
			for(int j = 0;j<dto.getModeCount();j++){
				regionMap.put(StringHelper.valueOf(j),dto.getQueueName(j));
			}
		}
	}

	private Gson gson = new Gson();

	@SuppressWarnings("all")
	public CommandResultDto dealCommandAsyn(CommandDto commandDto) {
		CommandResultDto resultDto = null;
		String processInstanceId = "";
		if(commandDto.getProcessInstanceId() != null){
			processInstanceId =  commandDto.getProcessInstanceId().toString();
		}
		try {
			MessageDto messageDto = new MessageDto();
			messageDto.setCommandDto(commandDto);
			messageDto.setJson(gson.toJson(commandDto.toMap()));

			// ��������ֶ��У��ĳɰ�����ʵ��idȡmodֵ��� modify by che.zi 2015-01-16
			if (processInstanceId == null || "".equals(processInstanceId)) {
				if (commandDto instanceof CompleteWorkItemDto) {
					String workItemId = ((CompleteWorkItemDto) commandDto)
							.getWorkitemId();
					WorkItemDto workItemDto = workItemService.queryWorkItem(workItemId);
					processInstanceId = StringHelper.valueOf(workItemDto.getProcessInstanceId());
				}
			}
			messageDto.getCommandDto().setProcessInstanceId(processInstanceId);
			sendMessageDto(messageDto);

			resultDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "�ӿڵ��óɹ�", null);

		} catch (Exception e) {
			logger.error("�첽�ӿ��쳣��" + e.getMessage(), e);
			resultDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�ӿڵ���ʧ�ܣ�" + commandDto.getCommandCode(), null);
		}
		return resultDto;
	}

	// ���������Ҫ�Ż�����Ҫÿ�ζ�ȥ��һ�����ݿ����ݣ�Ҫ��uos_command_cfg�����ݼ��ص��ڴ��С�
	public void sendMessageDto(MessageDto messageDto) throws Exception {
		if (StringHelper.isEmpty(messageDto.getCommandDto().getProcessInstanceId())) {
			logger.error("���첽�ӿ��޷���ȡ����ʵ��id:" + messageDto.getJson());
			throw new FastflowException("���첽�ӿ��޷���ȡ����ʵ��id:"+ messageDto.getJson());
		}

		String mod = String.valueOf(CommandPropUtil.getInstance()
				.getQueueCount(messageDto.getCommandDto().getCommandCode()));
		String commandQueueName = null;
		logger.info("mod is " + mod + " commod  "
				+ messageDto.getCommandDto().getCommandCode());
		if (null != mod && !"".equals(mod)) {
			long region = Long.valueOf(
					messageDto.getCommandDto().getProcessInstanceId())
					.longValue()
					% Long.valueOf(mod).longValue();
			logger.info("region is "
					+ region
					+ ";"
					+ Long.valueOf(
							messageDto.getCommandDto().getProcessInstanceId())
							.longValue());
			commandQueueName = map.get(
					messageDto.getCommandDto().getCommandCode()).get(
					String.valueOf(region));
			if (commandQueueName == null) {
				commandQueueName = map.get(
						messageDto.getCommandDto().getCommandCode()).get(
						String.valueOf(1));
			}
		} else {
			logger.error(messageDto.getCommandDto().getCommandCode()
					+ ":mod����Ϊ��");
		}
		logger.info("commandQueueName=" + commandQueueName);
		if (commandQueueName == null || "".equals(commandQueueName)) {
			commandQueueName = "FLOW-defaultQueue";
		}
		if (messageDto.getCommandDto().getProcessInstanceId() != null
				&& !"".equals(messageDto.getCommandDto().getProcessInstanceId())) {
			@SuppressWarnings("all")
			int priority = Integer
					.valueOf(messageDto.getCommandDto().getPriority());
			mqProducer.send(commandQueueName, messageDto, priority, messageDto
					.getCommandDto().getProcessInstanceId().toString());
		} else {
			mqProducer.send(commandQueueName, messageDto);
		}
	}
}
