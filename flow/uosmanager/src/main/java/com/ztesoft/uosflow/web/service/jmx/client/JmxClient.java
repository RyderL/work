package com.ztesoft.uosflow.web.service.jmx.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ztesoft.uosflow.web.service.jmx.dto.JmxDto;

public class JmxClient {

	private static final Logger logger = LoggerFactory.getLogger(JmxClient.class);

	public String getReturn(JmxDto jmxDto) throws IOException {
		StringBuilder urlSb = new StringBuilder();
		urlSb.append("http://").append(jmxDto.getHostName()).append(":")
				.append(jmxDto.getPort());
		urlSb.append("/InvokeAction//").append(
				jmxDto.getObjName().replace(".", "%2E").replace(":", "%3A")
						.replace("=", "%3D"));
		urlSb.append("/action=").append(jmxDto.getActName()).append("?action=")
				.append(jmxDto.getActName());
		String paramStr = jmxDto.getParams();
		if (null != paramStr && !"".equals(paramStr.trim())) {
			String[] params = paramStr.split(";");
			for (int i = 0; i < params.length; i++) {
				urlSb.append("&p").append(i).append("%2Bjava.lang.String=")
						.append(params[i]);
			}
		}
		logger.info("url=" + urlSb.toString());

		URL url = new URL(urlSb.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String lines;
		StringBuilder retSb = new StringBuilder();
		while ((lines = reader.readLine()) != null) {
			retSb.append(lines);
		}
		reader.close();
		conn.disconnect();
		return retSb.toString();
	}

	public String getReturnValue(JmxDto jmxDto) throws IOException {
		String content = getReturn(jmxDto);
		if (content.indexOf("Successful") != -1) {
			String str = "The operation returned with the value:<P>";
			int startIndex = content.indexOf(str) + str.length();
			int endIndex = content.indexOf("<P>", startIndex);
			return content.substring(startIndex, endIndex);
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		JmxClient client = new JmxClient();
		JmxDto jmxDto = new JmxDto();
		jmxDto.setHostName("localhost");
		jmxDto.setPort(6901);

		jmxDto.setObjName("JMX.ServerManager:type=ServerManager");
		jmxDto.setActName("counterInfo");

		String retStr = client.getReturnValue(jmxDto);
		logger.info(retStr);
	}
}
