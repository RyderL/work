package com.ztesoft.uosflow.web.service.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.fastflow.appcfg.AppInfoDto;
import com.zterc.uos.fastflow.appcfg.UosAppCfgUtil;
import com.ztesoft.uosflow.web.service.jmx.client.JmxClient;
import com.ztesoft.uosflow.web.service.jmx.dto.JmxDto;

@Service("ServerServ")
public class ServerServImpl implements ServerServ {


	@Override
	public String getServerThreadInfo(Map<String, Object> map) throws Exception {
		List<AppInfoDto> appInfoDtos = UosAppCfgUtil.getAppInfoDtos();
		return GsonHelper.toJson(appInfoDtos);
	}

	@Override
	public String getCounterInfo(Map<String, Object> map) throws Exception {
		@SuppressWarnings("all")
		List<Map<String, String>> rows = (List<Map<String, String>>) map
				.get("rows");
		Map<String, Long> retMap = new HashMap<String, Long>();
		for (int i = 0; i < rows.size(); i++) {
			Map<String, String> row = rows.get(i);
			String ip = row.get("ip");
			String jmxPort = row.get("jmxPort");
			JmxClient jmxClient = new JmxClient();
			JmxDto jmxDto = new JmxDto();
			jmxDto.setHostName(ip);
			jmxDto.setPort(IntegerHelper.valueOf(jmxPort));
			jmxDto.setObjName("JMX.ServerManager:type=ServerManager");
			jmxDto.setActName("counterInfo");
			String retValue = jmxClient.getReturnValue(jmxDto);
			if (retValue == null) {
				continue;
			}
			Map<String, Object> rMap = GsonHelper.toMap(retValue);
			for (String key : rMap.keySet()) {
				if (!retMap.containsKey(key)) {
					retMap.put(key, 0l);
				}
				retMap.put(key,
						retMap.get(key) + LongHelper.valueOf(rMap.get(key)));
			}
		}
		return GsonHelper.toJson(retMap);
	}

	@Override
	public String clearCounterInfo(Map<String, Object> map) throws Exception {
		@SuppressWarnings("all")
		List<Map<String, String>> rows = (List<Map<String, String>>) map
				.get("rows");
		for (int i = 0; i < rows.size(); i++) {
			Map<String, String> row = rows.get(i);
			String ip = row.get("ip");
			String jmxPort = row.get("jmxPort");
			JmxClient jmxClient = new JmxClient();
			JmxDto jmxDto = new JmxDto();
			jmxDto.setHostName(ip);
			jmxDto.setPort(IntegerHelper.valueOf(jmxPort));
			jmxDto.setObjName("JMX.ServerManager:type=ServerManager");
			jmxDto.setActName("clearCounterInfo");
			jmxClient.getReturnValue(jmxDto);
		}
		return GsonHelper.toJson("SUCCESS");
	}

	@Override
	public String clearStatics(Map<String, Object> map) throws Exception {
		@SuppressWarnings("all")
		List<Map<String, String>> rows = (List<Map<String, String>>) map
				.get("rows");
		for (int i = 0; i < rows.size(); i++) {
			Map<String, String> row = rows.get(i);
			String ip = row.get("ip");
			String jmxPort = row.get("jmxPort");
			JmxClient jmxClient = new JmxClient();
			JmxDto jmxDto = new JmxDto();
			jmxDto.setHostName(ip);
			jmxDto.setPort(IntegerHelper.valueOf(jmxPort));
			jmxDto.setObjName("JMX.ServerManager:type=ServerManager");
			jmxDto.setActName("clearStaticCache");
			jmxClient.getReturnValue(jmxDto);
		}
		return GsonHelper.toJson("SUCCESS");
	}

}
