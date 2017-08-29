package com.ztesoft.uosflow.util.mq.producer.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zterc.uos.fastflow.dto.specification.CommandCfgDto;
import com.zterc.uos.fastflow.service.CommandCfgService;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.util.cache.SeedUtils;
import com.ztesoft.uosflow.util.mq.dto.MessageDto;
import com.ztesoft.uosflow.util.mq.producer.inf.IMqProducer;

public class QueueProducer implements IMqProducer{
	private static final Logger logger = Logger.getLogger(QueueProducer.class);
	private Map<String, Map<Integer, IMqProducer>> queueRuleMap;
	private Map<String, List<QueueRuleConfig>> rules = new HashMap<String, List<QueueRuleConfig>>();
	private CommandCfgService commandCfgService;
	private String lock = "lock";
	
	public void setCommandCfgService(CommandCfgService commandCfgService) {
		this.commandCfgService = commandCfgService;
	}

	@SuppressWarnings("unchecked")
	public void init(){
		List<CommandCfgDto> retList = commandCfgService.qryComandCfgs();
		for (int i = 0; i < retList.size(); i++) {
			CommandCfgDto dto = retList.get(i);
			for(int j = 0;j<dto.getModeCount();j++){
				String queueName = dto.getQueueName(j);
				logger.info("=============queueName:"+queueName+",getQueueRuleBean:"+dto.getQueueRuleBean());
				List<QueueRuleConfig> configList = (List<QueueRuleConfig>) ApplicationContextProxy.getBean(dto.getQueueRuleBean());
				rules.put(queueName, configList);
			}
		}
		queueRuleMap = new HashMap<String, Map<Integer, IMqProducer>>();
		for (String queueName : rules.keySet()) {
			List<QueueRuleConfig> configList = rules.get(queueName);
			Map<Integer, IMqProducer> queueMap = new HashMap<Integer, IMqProducer>();
			for (QueueRuleConfig config : configList) {
				for (int i = config.getFrom(); i <= config.getTo(); i++) {
					queueMap.put(Integer.valueOf(i), config.getQueue());
				}
			}
			queueRuleMap.put(queueName, queueMap);
		}
	}

//	private void spaceExchangeTime(Map<String, List<QueueRuleConfig>> ruleConfig) {
//		queueRuleMap = new HashMap<String, Map<Integer, IMqProducer>>();
//		for (String queueName : ruleConfig.keySet()) {
//			List<QueueRuleConfig> configList = ruleConfig.get(queueName);
//			Map<Integer, IMqProducer> queueMap = new HashMap<Integer, IMqProducer>();
//			for (QueueRuleConfig config : configList) {
//				for (int i = config.getFrom(); i <= config.getTo(); i++) {
//					queueMap.put(Integer.valueOf(i), config.getQueue());
//				}
//			}
//			queueRuleMap.put(queueName, queueMap);
//		}
//
//	}

	/**
	 * ¶ÓÁÐput²Ù×÷
	 * 
	 * @param queueName
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void send(String queueName, MessageDto messageDto) {
		if(queueRuleMap == null){
			synchronized (lock) {
				if(queueRuleMap == null){
					this.init();
				}
			}
		}
		Map<Integer, IMqProducer> map = queueRuleMap.get(queueName);
		IMqProducer queue = map.get(getNumberByKey(messageDto.getCommandDto()
				.getProcessInstanceId()));
		queue.send(queueName, messageDto);
	}

	public Integer getNumberByKey(String key) {
		return SeedUtils.getInstance().getNumberByKey(key);
	}

	public Integer getNumberByString(String key) {
		return SeedUtils.getInstance().getNumberByString(key);
	}

	public void send(String queueName, MessageDto messageDto, int priority,
			String groupName) {
		if(queueRuleMap == null){
			synchronized (lock) {
				if(queueRuleMap == null){
					this.init();
				}
			}
		}
		Map<Integer, IMqProducer> map = queueRuleMap.get(queueName);
		if(map == null){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			map = queueRuleMap.get(queueName);
		}
		String key = messageDto.getCommandDto()
				.getProcessInstanceId();
		logger.info("--------ProcessInstanceId:"+key);
		int region = getNumberByKey(key);
		logger.info("--------region:"+region);
		IMqProducer queue = map.get(region);
		queue.send(queueName, messageDto, priority, groupName);
	}
}
