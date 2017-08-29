package com.zterc.uos.fastflow.appcfg;

public class AppInfoDto {
	public static final String TYPE_CONSUMER = "consumer";
	public static final String TYPE_PERSIST = "persist";
	public static final String TYPE_ALL = "all";

	private String host;
	private String port;
	private String type;
	private String appName;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
