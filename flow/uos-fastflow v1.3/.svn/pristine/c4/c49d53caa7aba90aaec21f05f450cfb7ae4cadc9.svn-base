package com.ztesoft.uosflow.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zterc.uos.fastflow.appcfg.AppInfoDto;
import com.zterc.uos.fastflow.appcfg.UosAppCfgUtil;
import com.ztesoft.uosflow.jmx.server.JmxServer;

public class UOSServer {
	private static Logger logger = LoggerFactory.getLogger(UOSServer.class);

	public static void start() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"datasource.xml");

		AppInfoDto appInfoDto = UosAppCfgUtil.getAppInfoDto();

		// 启动模式设置
		String model = appInfoDto.getType();
		if (model.equalsIgnoreCase(AppInfoDto.TYPE_ALL)) {
			context.setConfigLocation("application-web.xml");
			context.refresh();
		} else if (model.equalsIgnoreCase(AppInfoDto.TYPE_CONSUMER)) {
			context.setConfigLocation("application-consumer.xml");
			context.refresh();
		} else if (model.equalsIgnoreCase(AppInfoDto.TYPE_PERSIST)) {
			context.setConfigLocation("application-persist.xml");
			context.refresh();
		}

		// jmx服务设置
		try {
			new JmxServer().initJmxHtmlServer(Integer.valueOf(appInfoDto
					.getPort()));
			logger.info("成功发布Jmx服务");
		} catch (Exception e) {
			logger.error("发布Jmx服务失败。" + e.getMessage(), e);
		}

		logger.info("成功启动进程，运行。。。。。。。。。。。");
	}
}
