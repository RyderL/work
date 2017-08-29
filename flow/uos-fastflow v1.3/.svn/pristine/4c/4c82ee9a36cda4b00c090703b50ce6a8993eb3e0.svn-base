package com.zterc.uos.base.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.cache.UosCacheClient;
import com.zterc.uos.base.helper.SerializerHelper;

public class UosJedisCluster implements UosCacheClient {
	private Logger logger = LoggerFactory.getLogger(UosJedisCluster.class);

	private BinaryJedisCluster jedisCluster;

	public BinaryJedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public void setJedisCluster(BinaryJedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	@Override
	@SuppressWarnings("all")
	public <T> T getObject(String key, Class<T> clazz, Long route) {
		if (logger.isInfoEnabled()) {
			logger.info("redis--getObject:[key:" + key + ";clazz:" + clazz
					+ "]");
		}
		try {
			byte[] b = jedisCluster.getBinary(key);
			if (b == null) {
				return null;
			} else {
				return (T) SerializerHelper.deSerialize(b);
			}
		} catch (Exception ex) {
			logger.error("get error.", ex);
		}
		return null;
	}

	@Override
	public boolean setObject(String key, Object value, Long route) {
		if (logger.isInfoEnabled()) {
			logger.info("redis--setObject:[key:" + key + ";value:" + value
					+ "]");
		}
		try {
			jedisCluster.setBinary(key, SerializerHelper.serialize(value));
			return true;
		} catch (Exception ex) {
			logger.error("set error.", ex);
		}
		return false;
	}
	

	@Override
	public boolean delObject(String key, Long route) {
		return del(key, route);
	}


	@Override
	public boolean set(String key, String value, Long route) {
		try {
			jedisCluster.set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String get(String key, Long route) {
		try {
			return jedisCluster.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean del(String key, Long route) {
		try {
			jedisCluster.del(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean exist(String key, Long route) {
		try {
			return jedisCluster.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean ping() {
		String ret = jedisCluster.ping();
		if("PONG".equals(ret)){
			return true;
		}
		return false;
	}

	@Override
	public void clearAll(Long route) {
		jedisCluster.flushDB();
	}
}
