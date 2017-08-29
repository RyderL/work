package com.zterc.uos.base.cache;


public interface UosCacheClient {
	public <T> T getObject(String key, Class<T> clazz,Long route) ;

	public boolean setObject(String key, Object value,Long route);
	
	public boolean delObject(String key,Long route);

	public boolean set(String key, String value,Long route) ;

	public String get(String key,Long route);

	public boolean del(String key,Long route);
	
	public boolean exist(String key,Long route);

	public boolean ping();

	public void clearAll(Long route);
	
}
