package com.ztesoft.uosflow.main;

import org.apache.log4j.PropertyConfigurator;

public class UOSStartup {

	static {
		PropertyConfigurator.configure(ClassLoader.getSystemClassLoader()
				.getResource("log4j.properties"));
	}

	@SuppressWarnings("all")
	public static void main(String[] args) {

		new UOSServer().start();

		while (true) {
			try {
				Thread.currentThread().sleep(10000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
