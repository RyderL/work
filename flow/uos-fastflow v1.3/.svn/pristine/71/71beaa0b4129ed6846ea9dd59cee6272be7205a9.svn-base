package com.ztesoft.uosflow.util.cache;

/**
 * 缓存操作接口
 * @author yuxiao
 *
 */
public interface CacheInterface {
	public String getAddress();
	/**
	 * 从缓存中获取数据
	 * 
	 * @param cacheName
	 *            缓存名称
	 * @param key
	 * @return
	 */
	public Object get(String cacheName, String key) throws Exception;

	/**
	 * 向缓存里设置数据
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public void set(String cacheName, String key, Object value) throws Exception;

	/**
	 * 判断缓存中是否存在
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public boolean contain(String cacheName, String key) throws Exception;

	/**
	 * 清空缓存
	 */
	public void clearAll() throws Exception;
	
	/**
	 * 根据key值删除缓存
	 * 
	 * @param cacheName
	 * @param key
	 * @throws Exception
	 */
	public void remove(String cacheName, String key) throws Exception;
	
}
