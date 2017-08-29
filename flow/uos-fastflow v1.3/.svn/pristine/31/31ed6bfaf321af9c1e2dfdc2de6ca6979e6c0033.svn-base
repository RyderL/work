package com.ztesoft.uosflow.main;

import java.lang.management.ManagementFactory;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.ContextLoaderListener;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.fastflow.appcfg.AppInfoDto;
import com.zterc.uos.fastflow.appcfg.UosAppCfgUtil;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.ztesoft.uosflow.jmx.server.JmxServer;
import com.ztesoft.uosflow.jmx.server.bl.cachemanager.StaticCacheManager;

public class UOSListener extends ContextLoaderListener {

	private Logger logger = LoggerFactory.getLogger(UOSListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		super.contextDestroyed(arg0);
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		super.contextInitialized(arg0);

	}

	@Override
	protected void configureAndRefreshWebApplicationContext(
			ConfigurableWebApplicationContext wac, ServletContext sc) {
		if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
			// The application context id is still set to its original default
			// value
			// -> assign a more useful id based on available information
			String idParam = sc.getInitParameter(CONTEXT_ID_PARAM);
			if (idParam != null) {
				wac.setId(idParam);
			} else {
				// Generate default id...
				if (sc.getMajorVersion() == 2 && sc.getMinorVersion() < 5) {
					// Servlet <= 2.4: resort to name specified in web.xml, if
					// any.
					wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX
							+ ObjectUtils.getDisplayString(sc
									.getServletContextName()));
				} else {
					wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX
							+ ObjectUtils.getDisplayString(sc.getContextPath()));
				}
			}
		}

		wac.setServletContext(sc);
		String configLocationParam = sc.getInitParameter(CONFIG_LOCATION_PARAM);
		if (configLocationParam != null) {
			wac.setConfigLocation(configLocationParam);
		}

		// =======add by gong.yi 2016.1.28==============
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("datasource.xml");

		AppInfoDto appInfoDto = UosAppCfgUtil.getAppInfoDto();

		// 启动模式设置
		String model = appInfoDto.getType();
		if (model.equalsIgnoreCase(AppInfoDto.TYPE_ALL)) {
			wac.setConfigLocation("classpath:application-web.xml");
		} else if (model.equalsIgnoreCase(AppInfoDto.TYPE_CONSUMER)) {
			wac.setConfigLocation("classpath:application-consumer.xml");
		} else if (model.equalsIgnoreCase(AppInfoDto.TYPE_PERSIST)) {
			wac.setConfigLocation("classpath:application-persist.xml");
		}

		// jmx服务设置
		try {
			int port = IntegerHelper.valueOf(appInfoDto.getPort());
			List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			for(String inputArgument:inputArguments){
				if(inputArgument.contains("jmx-port")){
					String[] array = inputArgument.split("=");
					port = IntegerHelper.valueOf(array[1]);
				}
			}
			new JmxServer().initJmxHtmlServer(port);
			logger.info("成功发布Jmx服务");
		} catch (Exception e) {
			logger.error("发布Jmx服务失败。" + e.getMessage(), e);
		}
		
		context.close();
		// =======add by gong.yi 2016.1.28==============

		// The wac environment's #initPropertySources will be called in any case
		// when the context
		// is refreshed; do it eagerly here to ensure servlet property sources
		// are in place for
		// use in any post-processing or initialization that occurs below prior
		// to #refresh
		ConfigurableEnvironment env = wac.getEnvironment();
		if (env instanceof ConfigurableWebEnvironment) {
			((ConfigurableWebEnvironment) env).initPropertySources(sc, null);
		}

		customizeContext(sc, wac);
		wac.refresh();
		
		//加载静态数据到缓存
		if(FastflowConfig.loadStaticCache){
			StaticCacheManager.loadAllStaticCache();
			logger.error("---静态配置载入ehcache缓存完毕-----");
		}
	}
}
