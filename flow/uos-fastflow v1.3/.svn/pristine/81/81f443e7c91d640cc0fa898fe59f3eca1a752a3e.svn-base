/**
 * @author zhang.qiaoxian
 * @date 2015年4月24日
 * @project 1_Master
 *
 */
package com.ztesoft.uosflow.util.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEhImpl implements CacheInterface {
	private static final Logger logger = LoggerFactory.getLogger(CacheEhImpl.class);
	private static String fileString ;
	private CacheManager cacheManager = null;
	private String address;

	public CacheEhImpl(){
		initCacheManager();
	}

	private void initCacheManager() {
		if (fileString == null) {
            String currentClassAbsPath = CacheEhImpl.class.getResource("").getPath(); // 当前类的绝对路径
            logger.info("---currentClassAbsPath:"+currentClassAbsPath);
            String rootAbsPath = currentClassAbsPath.substring(0, currentClassAbsPath.indexOf("classes"));  // 根目录绝对路径
//            String rootAbsPath = "D:\\software\\jetty\\jetty-distribution-9.2.4.v20141103\\webapps\\BUSI2IOM\\WEB-INF\\";
            fileString = rootAbsPath + "classes/cfg/ehcacheDisk.xml";
			logger.error("[init]------------------------------------ehcacheDisk.xml的绝对路径为:"+fileString);
		}

        if(null == cacheManager) {
		    cacheManager = new CacheManager(fileString);
        }
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		CacheUtils.addressMap.put(this.address.trim(), this);
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public Object get(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		Element element = cache.get(key);
		if (element != null) {
			return element.getObjectValue();
		} else {
			return null;
		}

	}

	@Override
	public void set(String cacheName, String key, Object valueObj) {
		Cache cache = cacheManager.getCache(cacheName);
		Element element = new Element(key, valueObj);
		cache.put(element);
	}

	@Override
	public boolean contain(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		return cache.get(key) != null;
	}

	@Override
	public void clearAll() {
		cacheManager.clearAll();
	}

	@Override
	public void remove(String cacheName, String key) throws Exception {
		Cache cache = cacheManager.getCache(cacheName);
		cache.remove(key);
	}

}
