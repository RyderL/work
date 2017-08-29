package com.zterc.uos.base.helper;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;

import com.zterc.uos.base.cache.UosCacheClient;

public class StaticCacheHelper {

	private static CacheManager cacheManager;
	private static String staticCacheType;
	private static UosCacheClient uosCacheClient;

	public static CacheManager getCacheManager() {
		return cacheManager;
	}

	public static void setCacheManager(CacheManager cacheManager) {
		StaticCacheHelper.cacheManager = cacheManager;
	}

	public static String getStaticCacheType() {
		return staticCacheType;
	}

	public static void setStaticCacheType(String staticCacheType) {
		StaticCacheHelper.staticCacheType = staticCacheType;
	}

	public UosCacheClient getUosCacheClient() {
		return uosCacheClient;
	}

	public void setUosCacheClient(UosCacheClient uosCacheClient) {
		StaticCacheHelper.uosCacheClient = uosCacheClient;
	}

	public static Cache getCache(String cacheName) {
		if (cacheManager != null) {
			return cacheManager.getCache(cacheName);
		}
		return null;
	}

	public static void set(String cacheName, String key, Object obj) {
		if("ehcache".equals(staticCacheType)){
			Cache cache = getCache(cacheName);
			if (cache != null) {
				cache.put(key, obj);
			}
		}else{
			String cacheKey = getRedisKey(cacheName,key);
			Long routeKey = getRouteKey(key);
			uosCacheClient.setObject(cacheKey, obj, routeKey);
		}
	}

	public static Object get(String cacheName, String key) {
		if("ehcache".equals(staticCacheType)){
			Cache cache = getCache(cacheName);
			if (cache != null) {
				ValueWrapper vw = cache.get(key);
				if(vw!=null){
					return vw.get();
				}
			}
		}else{
			String cacheKey = getRedisKey(cacheName,key);
			Long routeKey = getRouteKey(key);
			Object obj = uosCacheClient.getObject(cacheKey, Object.class,routeKey);
			return obj;
		}
		return null;
	}
	
	public static void clearCache(String cacheName){
		if("ehcache".equals(staticCacheType)){
			Cache cache = getCache(cacheName);
			if (cache != null) {
				cache.clear();
			}
		}
	}
	
	public static void clearAll(){
		if("ehcache".equals(staticCacheType)){
			Collection<String> cacheNames = cacheManager.getCacheNames();
			for(String cacheName:cacheNames){
				clearCache(cacheName);
			}
		}else{
			uosCacheClient.clearAll(null);
		}
		
	}

	private static String getRedisKey(String cacheName, String key) {
		return cacheName+"_"+key;
	}
	
	private static Long getRouteKey(String key) {
		Long routeKey = null;
		try {
			routeKey = LongHelper.valueOf(key);
		} catch (Exception e) {
			char lastChar = key.charAt(key.length() - 1);
			routeKey = LongHelper.valueOf((int)lastChar);
		}
		return routeKey;
	}

}
