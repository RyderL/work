package com.ztesoft.uosflow.jmx.server.bl.server;

public interface ServerManagerMXBean {

	/**
	 * 清理静态缓存
	 */
	public String clearStaticCache(String cacheName);

	/**
	 * 获取统计信息
	 * 
	 * @return
	 */
	public String counterInfo();

	public String clearCounterInfo();
	
	/**
	 * 刷新所有静态缓存
	 * @return
	 */
	public String refreshCacheAll();

	/**
	 * 修改日志级别
	 * @param level
	 * @return
	 */
	String changeLoggerLevel(String level);
}
