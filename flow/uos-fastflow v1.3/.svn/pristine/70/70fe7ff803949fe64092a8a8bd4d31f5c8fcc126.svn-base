package com.zterc.uos.base.cache.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zterc.uos.base.helper.StringHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;
import redis.clients.util.Pool;

public class UosJedisClient extends AbstactUosJedisClient {

	private static Logger logger = LoggerFactory
			.getLogger(UosJedisClient.class);

	public void init() {
		logger.info("加载redis地址：" + jedisAddrs);
		if (StringHelper.isEmpty(jedisAddrs)) {
			return;
		}
		// 普通的redis模式，127.0.0.1:6379:password
		if (jedisAddrs.indexOf("=") == -1) {
			String[] addrs = jedisAddrs.split(",");
			mod = addrs.length;
			poolMap = new HashMap<String, Pool<Jedis>>();
			for (int i = 0; i < mod; i++) {
				String[] addr = addrs[i].split(":");
				Pool<Jedis> pool =null;
				if (addr.length == 3) {
					//带密码的配置
					pool = new JedisPool(poolConfig, addr[0],
							Integer.valueOf(addr[1]),Protocol.DEFAULT_TIMEOUT,addr[2]);
				} else {
					pool = new JedisPool(poolConfig, addr[0],
							Integer.valueOf(addr[1]));
				}
				poolMap.put("" + i, pool);
			}
		}
		/**
		 * 哨兵模式
		 * ：jedisAddrs:master1=192.168.192.1:9876,92.168.192.2:9876,password:ztesoft;master2=192.168.192.3:9876,92.168.192.4:9876
		 */
		else {
			String[] configs = jedisAddrs.split(";");
			mod = configs.length;
			poolMap = new HashMap<String, Pool<Jedis>>();
			for (int i = 0; i < mod; i++) {
				String configString = configs[i];
				if (StringUtils.isEmpty(configString)) {
					continue;
				}
				// 哨兵地址配置信息
				String password = null;
				String[] config = configString.split("=");
				String masterName = config[0];
				String[] sentinels = config[1].split(",");
				Set<String> sentinelsSet = new HashSet<String>();
				for (String sentinel : sentinels) {
					if (StringUtils.isEmpty(sentinel)) {
						continue;
					} else if (sentinel.startsWith("password")) {
						// 如果是password开头，则表示密码
						password = sentinel.trim().substring(9).trim();
					} else {
						// 哨兵地址处理
						sentinelsSet.add(sentinel);
					}
				}
				Pool<Jedis> pool = null;
				if (StringUtils.isEmpty(password)) {
					pool = new JedisSentinelPool(masterName, sentinelsSet,poolConfig);
				} else {
					pool = new JedisSentinelPool(masterName, sentinelsSet,poolConfig,
							password);
				}
				poolMap.put("" + i, pool);
			}
		}
	}
}
