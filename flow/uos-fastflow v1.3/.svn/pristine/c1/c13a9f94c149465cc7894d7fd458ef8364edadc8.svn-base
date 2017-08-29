package com.ztesoft.uosflow.util.cache;
 

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;

public abstract class AbstractRedisHandler    {
	private static Logger logger = LoggerFactory.getLogger(AbstractRedisHandler.class);

	protected int expireTime;
	protected Pool<Jedis> jedisPool;
	 
	
	public Pool<Jedis> getJedisPool() {
		return jedisPool;
	}

	
	public abstract void setJedisPool(Pool<Jedis> jedisPool);

	public Jedis getJedis() throws Exception {
		int tryCount = 1;
		Jedis jedis = null;
		while (tryCount++ < 4) {
			try {
				jedis = jedisPool.getResource();
				break;
			} catch (JedisConnectionException e) {
				if (tryCount==3) {
					logger.error("redis获取连接失败", e);
					throw e;
				} else {
					logger.warn("redis获取连接失败", e);
					logger.warn("重试:" + tryCount + "次", e);	
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new Exception("获取连接失败", e);
			}
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error("休眠失败", e);
			}
		}
		if (jedis==null) {
			throw new Exception("获取连接失败");
		}
		return jedis;
	}


	public int getExpireTime() {
		return expireTime;
	}


	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}
}
