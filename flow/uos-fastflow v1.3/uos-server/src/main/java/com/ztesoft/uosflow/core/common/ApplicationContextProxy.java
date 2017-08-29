package com.ztesoft.uosflow.core.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProxy implements ApplicationContextAware {

	private static ApplicationContext context;

	public static Object getBean(String name) {
		return context.getBean(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		setContext(applicationContext);
	}

	private static void setContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

}
