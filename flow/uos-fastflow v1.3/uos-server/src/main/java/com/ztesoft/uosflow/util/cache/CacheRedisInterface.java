package com.ztesoft.uosflow.util.cache;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public interface CacheRedisInterface  extends CacheInterface{
	
	/**
	 * »ñÈ¡jedisPool
	 * @return
	 * zhong.kaijie	  ÉÏÎç11:24:40
	 */
	public Pool<Jedis> getPool();
}
