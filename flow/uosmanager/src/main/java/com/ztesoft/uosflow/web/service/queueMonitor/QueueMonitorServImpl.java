package com.ztesoft.uosflow.web.service.queueMonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zterc.uos.base.bean.BeanContextProxy;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.URLCoderHelper;
import com.zterc.uos.fastflow.appcfg.AppInfoDto;
import com.zterc.uos.fastflow.appcfg.UosAppCfgUtil;
import com.zterc.uos.fastflow.dto.specification.AppCfgDto;
import com.zterc.uos.fastflow.dto.specification.CommandCfgDto;
import com.zterc.uos.fastflow.service.AppCfgService;
import com.zterc.uos.fastflow.service.CommandCfgService;
import com.ztesoft.uosflow.web.service.jmx.client.JmxClient;
import com.ztesoft.uosflow.web.service.jmx.dto.JmxDto;

@Service("QueueMonitorServ")
public class QueueMonitorServImpl implements QueueMonitorServ {
	private static Logger logger = LoggerFactory.getLogger(QueueMonitorServImpl.class);
	@Autowired
	private AppCfgService appCfgService;

	@Override
	public String qryFQueueLength(Map<String, String> paramMap)
			throws Exception {
		String hostName, port, index;
		hostName = MapUtils.getString(paramMap, "hostName");
		port = MapUtils.getString(paramMap, "port");
		index = MapUtils.getString(paramMap, "index");
		Map<String, String> retMap = new HashMap<String, String>();
		JmxClient jmxClient = new JmxClient();
		JmxDto jmxDto = new JmxDto();

		retMap.put("index", index);

		jmxDto.setHostName(hostName);
		jmxDto.setPort(Integer.valueOf(port));
		jmxDto.setObjName("JMX.FQueueManager:type=FQueueManager");
		jmxDto.setActName("count");

		Map<String,String> queueMap = this.getFQueuesNames();
		if (queueMap != null) {
			Set<String> list = queueMap.keySet();
			for (Iterator<String> it=list.iterator();it.hasNext();) {
				String queueName = it.next();
				queueName = URLCoderHelper.encode(queueName);
				jmxDto.setParams(queueName);
				String html = "";
				try {
					html = jmxClient.getReturn(jmxDto);
				} catch (Exception e) {
					logger.error(hostName + " JMX无法连接", e);
					// writeToPage(out,retMap);
				}
				if (html.indexOf("Successful") != -1) {
					String str = "The operation returned with the value:<P>";
					int startIndex = html.indexOf(str) + str.length();
					int endIndex = html.indexOf("<P>", startIndex);
					String length = html.substring(startIndex, endIndex);
					String displayName = queueMap.get(queueName);
					retMap.put(displayName, length);
				}
			}
		}
		return GsonHelper.toJson(retMap);
	}

	private Map<String,String> getFQueuesNames() {
		Map<String,String> map = new HashMap<String,String>();
		CommandCfgService commandCfgService = (CommandCfgService) BeanContextProxy
				.getBean("commandCfgService");
		List<CommandCfgDto> retList = commandCfgService.qryComandCfgs();
		for (int i = 0; i < retList.size(); i++) {
			CommandCfgDto dto = retList.get(i);
			if("1".equals(dto.getDisplay())){//1展示在页面，0不展示
				for (int j = 0; j < dto.getModeCount(); j++) {
					String queueName = dto.getQueueName(j);
					String displayName = dto.getDisplayName()+j;
					map.put(queueName, displayName);
				}
			}
		}
		return map;
	}

	public String qryQueueHost(Map<String, String> paramMap) throws Exception {
		Map<String, String> retMap = new HashMap<String, String>();
		List<String> list = new ArrayList<String>();

		try {
			ResourceBundle properties = ResourceBundle.getBundle("redis.redis");
			for (Iterator<String> it = properties.keySet().iterator(); it
					.hasNext();) {
				String key = it.next();
				if (key.contains("address")) {
					String address = properties.getString(key);
					list.add(address);
				}
			}
		} catch (Exception e) {
			logger.error("数据库查询Redis地址失败", e);
			return GsonHelper.toJson(retMap);
		}

		if (list != null) {
			int count = list.size();
			for (int i = 0; i < count; i++) {
				retMap.put(i + "", list.get(i));
			}
			retMap.put("count", count + "");
		}
		return GsonHelper.toJson(retMap);
	}

	public Map<String, String> getQueueName(Map<String, String> paramMap) throws Exception {
//		String publicQueueNames = "";
//		String queueDisplayNames = "";
		Map<String,String> map = new HashMap<String,String>();
		CommandCfgService commandCfgService = (CommandCfgService) BeanContextProxy
				.getBean("commandCfgService");
		List<CommandCfgDto> retList = commandCfgService.qryComandCfgs();
		for (int i = 0; i < retList.size(); i++) {
			CommandCfgDto dto = retList.get(i);
			if("1".equals(dto.getDisplay())){
				for (int j = 0; j < dto.getModeCount(); j++) {
					String queueName = dto.getQueueName(j);
//					publicQueueNames += "," + queueName;
					String queueDisplayName = dto.getDisplayName()+j;
					map.put(queueName, queueDisplayName);
				}
			}
		}
//		if (publicQueueNames != null && publicQueueNames.length() > 0) {
//			publicQueueNames = publicQueueNames.substring(1);
////			queueDisplayNames = queueDisplayNames.substring(1);
//		}
//		Map<String, String> retMap = new HashMap<String, String>();
//		retMap.put("publicQueueNames", publicQueueNames);
//		retMap.put("queueDisplayNames", queueDisplayNames);
		return map;
	}

	public String qryQueueLength(Map<String, String> paramMap) throws Exception {
		Map<String, String> retMap = new HashMap<String, String>();
//		String queueNames = MapUtils.getString(paramMap, "queueNames");
		String address = MapUtils.getString(paramMap, "address");
		String hostName = MapUtils.getString(paramMap, "host");
		String port = MapUtils.getString(paramMap, "port");
//		if (queueNames != null) {
//			String[] queueNameArray = queueNames.split(",");
//			List<String> queueList = Arrays.asList(queueNameArray);
		Map<String,String> queueMap = this.getQueueName(null);
			JmxClient jmxClient = new JmxClient();
			JmxDto jmxDto = new JmxDto();

			jmxDto.setHostName(hostName);
			jmxDto.setPort(Integer.valueOf(port));
			jmxDto.setObjName("JMX.RedisQueueManager:type=RedisQueueManager");
			jmxDto.setActName("count");
			try {
				if (queueMap != null) {
					Set<String> list = queueMap.keySet();
					for (Iterator<String> it=list.iterator();it.hasNext();) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("queueName", it.next());
						map.put("address", address);
						String param = GsonHelper.toJson(map);
						logger.info("----param:" + param);
						param = URLCoderHelper.encode(param);
						jmxDto.setParams(param);
						String html = "";
						try {
							html = jmxClient.getReturn(jmxDto);
						} catch (Exception e) {
							logger.error(hostName + " JMX无法连接", e);
						}
						if (html.indexOf("Successful") != -1) {
							String str = "The operation returned with the value:<P>";
							int startIndex = html.indexOf(str) + str.length();
							int endIndex = html.indexOf("<P>", startIndex);
							String length = html.substring(startIndex, endIndex);
							retMap.put(queueMap.get(it.next()), length);
						}
					}
				}
			} catch (Exception e) {
				logger.error("获取redis队列长度出错", e);
			}
//		}
		return GsonHelper.toJson(retMap);
	}

	@Override
	public String qryHostName(Map<String,String> paramMap) {
		AppInfoDto appInfoDto = UosAppCfgUtil.getAppInfoDto();
		List<String> hostName = new ArrayList<String>();
		hostName.add("undefined");
		hostName.add(appInfoDto.getHost());
		hostName.add(MapUtils.getString(paramMap, "name"));
		hostName.add(appInfoDto.getPort());
		return GsonHelper.toJson(hostName);
	}

	@Override
	public String qryFqAddrs(Map<String, String> paramMap) {
		AppCfgDto appCfgDto = appCfgService.queryAppCfgDtoByKey(UosAppCfgUtil.getAppName(),"fqueue-addrs");
		String fqueueAddrs = appCfgDto.getpValue();
		String[] fqueueAddrArr = fqueueAddrs.split(";");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(int i=0;i<fqueueAddrArr.length;i++){
			Map<String,String> addrMap = new HashMap<String,String>();
			String addr = fqueueAddrArr[i];
			addrMap.put("label", addr);
			addrMap.put("value", addr);
			list.add(addrMap);
		}
		return GsonHelper.toJson(list);
	}

}
