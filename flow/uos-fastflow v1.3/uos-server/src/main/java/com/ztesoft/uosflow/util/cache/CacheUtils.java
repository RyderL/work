/**
 * @author zhang.qiaoxian
 * @date 2015年4月24日
 * @project 0_JSKT_Base
 *
 */
package com.ztesoft.uosflow.util.cache;
 
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ztesoft.uosflow.util.cache.dto.CacheRuleConfig;

public class CacheUtils {
	private static final Logger logger = LoggerFactory.getLogger(CacheUtils.class);
	public static Map<String, CacheInterface> addressMap = new ConcurrentHashMap<String, CacheInterface>();
	private Map<String, Map<Integer, CacheInterface>> cacheRuleMap = new ConcurrentHashMap<String, Map<Integer, CacheInterface>>();
	private Map<String, List<CacheRuleConfig>> rules;

	public void setRules(Map<String, List<CacheRuleConfig>> rules) {
		this.rules = rules;
		this.spaceExchangeTime(this.rules);
	}

	private void spaceExchangeTime(Map<String, List<CacheRuleConfig>> ruleConfig) {
		for (String cacheName : ruleConfig.keySet()) {
			List<CacheRuleConfig> configList = ruleConfig.get(cacheName);
			Map<Integer, CacheInterface> cacheMap = new ConcurrentHashMap<Integer, CacheInterface>();
			for (CacheRuleConfig config : configList) {
				for (int i = config.getFrom(); i <= config.getTo(); i++) {
					cacheMap.put(Integer.valueOf(i), config.getCache());
				}
			}
			cacheRuleMap.put(cacheName, cacheMap);
		}
	}

	/**
	 * 从缓存中取值
	 * 
	 * @param cacheName
	 *            缓存名称
	 * @param key
	 *            缓存key
	 * @return
	 * @throws Exception
	 */
	public Object get(String cacheName, String key) throws Exception {
		try{
			Map<Integer, CacheInterface> map=getCacheInterfaceMap(cacheName);
		    CacheInterface cache = map.get(getNumberByKey(key));
		    Object ret=cache.get(cacheName, key);
		    return ret;
		}catch(Exception e){
			logger.error("取出缓存失败！！！key=" + key, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Map<String, T> get(String cacheName, String key, Class<T> clazzT) throws Exception {
		try{
	     	Map<Integer, CacheInterface> map=getCacheInterfaceMap(cacheName);
			CacheInterface cache = map.get(getNumberByKey(key));
			return (Map<String, T>) cache.get(cacheName, key);
		}catch(Exception e){
			logger.error("取出缓存失败！！！key=" + key, e);
			return null;
		}
		
	}

	/**
	 * 放入缓存
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void put(String cacheName, String key, Object value) throws Exception {
        try{
        	Map<Integer, CacheInterface> map=getCacheInterfaceMap(cacheName);
    		CacheInterface cache = map.get(getNumberByKey(key));
    		cache.set(cacheName, key, value);
        }catch(Exception e){
        	logger.error("放入缓存失败！！key=" +key,e);
        }
		
	}

	/**
	 * 判断是否存在
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public boolean contain(String cacheName, String key) throws Exception {
		Map<Integer, CacheInterface> map=getCacheInterfaceMap(cacheName);
		CacheInterface cache = map.get(getNumberByKey(key));		
		return cache.contain(cacheName, key);
	}

	/**
	 * 清空所有缓存
	 * @throws Exception 
	 */
	@Deprecated
	public void clearAll() throws Exception {
		
	}

	public void remove(String cacheName, String key) throws Exception {
		try{
            Map<Integer, CacheInterface> map=getCacheInterfaceMap(cacheName);
            CacheInterface cache = map.get(getNumberByKey(key));
            cache.remove(cacheName, key);
		}catch(Exception e){
        	logger.error("去除缓存失败！！key=" + key, e);
        }
	}
	
	public String getAddressByKey(String queueName, String key) {
		Map<Integer, CacheInterface> map=getCacheInterfaceMap(queueName);
		CacheInterface cache = map.get(getNumberByKey(key));
		return cache.getAddress();
	}
 
	
	private Map<Integer, CacheInterface> getCacheInterfaceMap(String key){
		Map<Integer, CacheInterface> map = cacheRuleMap.get(key);
		if (map==null) {
			throw new NullPointerException("从cacheRuleMap中找不到key为:"+key+" 的值!");
		}else{
			return map;
		}
	}
	private Integer getNumberByKey(String key) {
		int hashCode = key.hashCode();
		if (hashCode < 0) {
			hashCode = hashCode * (-1);
		}
		return Integer.valueOf(hashCode % 1024);
	}
	//add by che.zi 20150525 for zmp683864
	/**
	 * 从缓存中取数据
	 * 根据partitionKey的后四位路由到指定的缓存服务器
	 * @param cacheName
	 * @param key
	 * @param partitionKey
	 * @return
	 * @throws Exception
	 */
	public Object get(String cacheName, String key,String partitionKey) throws Exception {
		try {
			Map<Integer, CacheInterface> map = cacheRuleMap.get(cacheName);
			CacheInterface cache = map.get(getNumberByString(partitionKey));
			return cache.get(cacheName, key);
		} catch (Exception e) {
			logger.error("取出缓存失败！！！");
			return null;
		}
	}
	/**
	 * 将缓存数据转换成一个指定对象的map
	 * 根据partitionKey的后四位路由到指定的缓存服务器
	 * @param cacheName
	 * @param key
	 * @param clazzT
	 * @param partitionKey
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> get(String cacheName, String key, Class<T> clazzT,String partitionKey) throws Exception {
		try {
			Map<Integer, CacheInterface> map = cacheRuleMap.get(cacheName);
			CacheInterface cache = map.get(getNumberByString(partitionKey));
			return (Map<String, T>) cache.get(cacheName, key);
		} catch (Exception e) {
			logger.error("取出缓存失败！！！");
			return null;
		}
	}

	/**
	 * 放入缓存
	 * 
	 * 根据partitionKey的后四位路由到指定的缓存服务器
	 * @param cacheName
	 * @param key
	 * @param value
	 * @param partitionKey
	 * @throws Exception
	 */
	public void put(String cacheName, String key, Object value,String partitionKey) throws Exception {
		try {
			Map<Integer, CacheInterface> map = cacheRuleMap.get(cacheName);
			CacheInterface cache = map.get(getNumberByString(partitionKey));
			cache.set(cacheName, key, value);
		} catch (Exception e) {
			logger.error("放入缓存失败！！！");
		}
	}

	/**
	 * 判断是否存在
	 * 
	 * 根据partitionKey的后四位路由到指定的缓存服务器
	 * @param cacheName
	 * @param key
	 * @param partitionKey
	 * @return
	 * @throws Exception 
	 */
	public boolean contain(String cacheName, String key,String partitionKey) throws Exception {
		Map<Integer, CacheInterface> map = cacheRuleMap.get(cacheName);
		CacheInterface cache = map.get(getNumberByString(partitionKey));
		return cache.contain(cacheName, key);
	}

	/**
	 * 删除缓存
	 * 根据partitionKey的后四位路由到指定的缓存服务器
	 * @param cacheName
	 * @param key
	 * @param partitionKey
	 * @throws Exception
	 */
	public void remove(String cacheName, String key,String partitionKey) throws Exception {
		try {
			Map<Integer, CacheInterface> map = cacheRuleMap.get(cacheName);
			CacheInterface cache = map.get(getNumberByString(partitionKey));
			cache.remove(cacheName, key);
		} catch (Exception e) {
			logger.info("删除缓存失败！！！");
		}
	}
	
	/**
	 * 根据key值获取地址信息
	 * @param queueName
	 * @param key
	 * @return
	 */
	public String getAddressByKey(String queueName, String key,String paritionKey) {
		Map<Integer, CacheInterface> map=getCacheInterfaceMap(queueName);
		CacheInterface cache = map.get(getNumberByString(paritionKey));
		return cache.getAddress();
	}
	
	/**
	 * 
	 * 获取Key的后四位值
	 * @param key
	 * @return
	 */
	private Integer getNumberByString(String key){
		key = key.substring(key.length()-4);
		Integer number = Integer.valueOf(key);
		return number;
	}
	//end
	
	public static void main(String[] args) {
//		String key = "CDMA1506180010675";
//		key = key.substring(key.length()-4);
//		Integer number = IntegerUtils.valueOf(key);
//		System.out.println(number);
//		Map cacheMap = new ConcurrentHashMap();
//		for (int i = 0; i <= 511; i++) {
//			cacheMap.put(Integer.valueOf(i), "1");
//		}
//		for (int i = 512; i <= 1023; i++) {
//			cacheMap.put(Integer.valueOf(i), "2");
//		}
//		System.out.println(cacheMap.get(number));	
	}
}
