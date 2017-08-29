package com.zterc.uos.base.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.zterc.uos.base.bean.BeanContextProxy;
import com.zterc.uos.base.cache.redis.UosJedisClient;
import com.zterc.uos.base.helper.LongHelper;

public class RedisUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(RedisUtil.class);

	private UosJedisClient uosJedisClient;

	public static RedisUtil getInstance() {
		return (RedisUtil) BeanContextProxy.getBean("redisUtil");
	}

	public UosJedisClient getUosJedisClient() {
		return uosJedisClient;
	}

	public void setUosJedisClient(UosJedisClient uosJedisClient) {
		this.uosJedisClient = uosJedisClient;
	}

	public RedisUtil() {

	}

	public Jedis getJedis(String key) {
		logger.info("RedisUtil-lock:getJedis==" + key);
		return uosJedisClient.getJedis(LongHelper.valueOf(key));
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public void returnResource(Jedis redis,String key) {
		if (redis != null) {
			logger.info("RedisUtil-lock:returnResource==" + redis);
			uosJedisClient.returnResource(redis,LongHelper.valueOf(key));
		}
	}
	
	/**
	 * 释放redis
	 * 
	 * @param pool
	 * @param redis
	 */
	public void returnBrokenResource(Jedis redis,String key) {
		if (redis != null) {
			logger.info("RedisUtil-returnBrokenResource==" + redis);
			uosJedisClient.returnBrokenResource(redis,LongHelper.valueOf(key));
		}
	}
}
