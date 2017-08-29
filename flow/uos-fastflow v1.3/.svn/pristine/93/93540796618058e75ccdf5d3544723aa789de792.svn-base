package com.zterc.uos.base.cache.redis;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

import com.zterc.uos.base.cache.UosCacheClient;
import com.zterc.uos.base.helper.SerializerHelper;

public abstract class AbstactUosJedisClient implements UosCacheClient {

	private Logger logger = LoggerFactory
			.getLogger(AbstactUosJedisClient.class);

	protected JedisPoolConfig poolConfig;
	protected String jedisAddrs;
	protected Map<String, Pool<Jedis>> poolMap;
	protected int mod;
	protected Long expireTime;


	public JedisPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}
	
	public String getJedisAddrs() {
		return jedisAddrs;
	}

	public void setJedisAddrs(String jedisAddrs) {
		this.jedisAddrs = jedisAddrs;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public abstract void init();

	@SuppressWarnings("all")
	public <T> T getObject(String key, Class<T> clazz, Long route) {
		Jedis jedis = null;
		try {
			jedis = getJedis(route);
			byte[] b = jedis.get(key.getBytes());
			if (b == null) {
				return null;
			} else {
				return (T) SerializerHelper.deSerialize(b);
			}
		} catch (Exception ex) {
			returnBrokenResource(jedis,route);
			logger.error("get error.", ex);
		} finally {
			returnResource(jedis,route);
		}
		return null;
	}

	public boolean setObject(String key, Object value, Long route){
		Jedis jedis = null;
		try {
			jedis = getJedis(route);
			jedis.set(key.getBytes(), SerializerHelper.serialize(value));
			jedis.expire(key.getBytes(), expireTime.intValue());
			return true;
		} catch (Exception ex) {
			returnBrokenResource(jedis,route);
			logger.error("set error.", ex);
		} finally {
			returnResource(jedis,route);
		}
		return false;
	}

	public boolean delObject(String key, Long route) {
		Jedis jedis = null;
		try {
			jedis = getJedis(route);
			jedis.del(key.getBytes());
			return true;
		} catch (Exception ex) {
			returnBrokenResource(jedis,route);
			logger.error("del error.", ex);
		} finally {
			returnResource(jedis,route);
		}
		return false;
	}

	public boolean set(String key, String value, Long route) {
		Jedis jedis = null;
		try {
			jedis = getJedis(route);
			jedis.set(key, value);
			jedis.expire(key.getBytes(), expireTime.intValue());
			return true;
		} catch (Exception ex) {
			returnBrokenResource(jedis,route);
			logger.error("set error.", ex);
		} finally {
			returnResource(jedis,route);
		}
		return false;
	}

	public String get(String key, Long route) {
		Jedis jedis = null;
		try {
			jedis = getJedis(route);
			return jedis.get(key) == null ? null : jedis.get(key);
		} catch (Exception ex) {
			returnBrokenResource(jedis,route);
			logger.error("get error.", ex);
		} finally {
			returnResource(jedis,route);
		}
		return null;
	}

	public boolean del(String key, Long route) {
		Jedis jedis = null;
		try {
			jedis = getJedis(route);
			jedis.del(key);
			return true;
		} catch (Exception ex) {
			returnBrokenResource(jedis,route);
			logger.error("del error.", ex);
		} finally {
			returnResource(jedis,route);
		}
		return false;
	}

	@Override
	public boolean exist(String key, Long route) {
		try {
			if (get(key, route) != null) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public void returnResource(Jedis jedis,Long route) {
		try {
			poolMap.get(route % mod + "").returnResource(jedis);
		} catch (Exception e) {
			logger.warn("returnResource error.");
		}
	}
	
	public void returnBrokenResource(Jedis jedis,Long route) {
		try {
			poolMap.get(route % mod + "").returnBrokenResource(jedis);
		} catch (Exception e) {
			logger.warn("returnResource error.");
		}
	}

	public Jedis getJedis(Long route) {
		if (route == null) {
			return poolMap.get("0").getResource();
		}
		return poolMap.get(route % mod + "").getResource();
	}

	//TODO: 目前ping操作只针对了redis节点1。
	@Override
	public boolean ping() {
		Jedis jedis = null;
		try {
			jedis = getJedis(null);
			String ret = jedis.ping();
			if("PONG".equals(ret)){
				return true;
			}
		} catch (Exception e) {
			//添加异常归还连接池操作 modify by bobping
			returnBrokenResource(jedis,0L);
			logger.error("----redis连接异常："+e.getMessage(),e);
		} finally{ //添加归还连接池操作 modify by bobping
			returnResource(jedis,0L);
		}
		return false;
	}
	
	@Override
	public void clearAll(Long route){
		Jedis jedis = null;
		try {
			jedis = getJedis(route);
			jedis.flushDB();
		} catch (Exception e) {
			returnBrokenResource(jedis,0L);
			logger.error("----redis连接异常："+e.getMessage(),e);
		}finally{
			returnResource(jedis,0L);
		}
	}
	
}
