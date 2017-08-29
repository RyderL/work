package com.ztesoft.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.zterc.uos.fastflow.config.FastflowConfig;

public class MyContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		ProtocolConfig.destroyAll();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		FastflowConfig.setLoadStaticCache(false);
	}

}
