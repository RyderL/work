package com.zterc.uos.base.cache.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class UosJedisSentinelClient extends AbstactUosJedisClient {

	// private Logger logger = LoggerFactory.getLogger(UosJedisClient.class);

	@Override
	/**
	 * jedisAddrs:master1=192.168.192.1:9876,92.168.192.2:9876;master2=192.168.192.3:9876,92.168.192.4:9876
	 */
	public void init() {
		String[] configs = jedisAddrs.split(";");
		mod = configs.length;
		poolMap = new HashMap<String, Pool<Jedis>>();
		for (int i = 0; i < mod; i++) {
			String[] config = configs[i].split("=");
			String masterName = config[0];
			String[] sentinels = config[1].split(",");
			Set<String> sentinelsSet = new HashSet<String>();
			for (String sentinel : sentinels) {
				sentinelsSet.add(sentinel);
			}
			Pool<Jedis> pool = new JedisSentinelPool(masterName, sentinelsSet,poolConfig);
			poolMap.put("" + i, pool);
		}
	}
}