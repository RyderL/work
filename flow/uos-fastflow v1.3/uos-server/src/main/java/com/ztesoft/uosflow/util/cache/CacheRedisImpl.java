/**
 * @author zhang.qiaoxian
 * @date 2015Äê4ÔÂ24ÈÕ
 * @project 1_Master
 *
 */
package com.ztesoft.uosflow.util.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.zterc.uos.base.helper.SerializerHelper;

public class CacheRedisImpl extends AbstractRedisHandler implements
		CacheRedisInterface {
	private static final Logger logger = LoggerFactory.getLogger(CacheRedisImpl.class);
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		CacheUtils.addressMap.put(this.address.trim(), this);
	}

	public Pool<Jedis> getJedisPool() {
		return jedisPool;
	}

	@Override
	public Object get(String cacheName, String key) throws Exception {
		Object value = null;
		Jedis jedis = getJedis();
		try {
			byte[] bytes = jedis.get(SerializerHelper.serialize(key));
			logger.info("CacheRedisImpl =======================================================================================bytes size = "
					+ bytes.length);
			// modify by che.zi 20150519 forzmp:679688 end
			if (bytes != null) {
				value = SerializerHelper.deSerialize(bytes);
			}
		} catch (Exception e) {
			throw new Exception("redis²Ù×÷Ê§°Ü!", e);
		} finally {
			jedisPool.returnResource(jedis);
		}

		return value;
	}

	/**
	 * @param cacheName
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	@Override
	public void set(String cacheName, String key, Object value)
			throws Exception {
		Jedis jedis = getJedis();
		try {
			byte[] bytes = SerializerHelper.serialize(value);
			byte[] keyBytes = SerializerHelper.serialize(key);
			jedis.set(keyBytes, bytes);
			jedis.expire(keyBytes, expireTime);
		} catch (Exception e) {
			throw new Exception("redis²Ù×÷Ê§°Ü!", e);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * @param cacheName
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean contain(String cacheName, String key) throws Exception {
		Boolean result = false;
		Jedis jedis = getJedis();
		// modify by che.zi 20150519 forzmp:679688 begin
		// result = jedis.exists(ObjectHelper.toByteArray(key));
		try {
			byte[] keyBytes = SerializerHelper.serialize(key);
			result = jedis.exists(keyBytes);
		} catch (Exception e) {
			throw new Exception("redis²Ù×÷Ê§°Ü!", e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		// modify by che.zi 20150519 forzmp:679688 end
		return result.booleanValue();
	}

	/**
	 * @throws Exception
	 */
	@Override
	public void clearAll() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @param cacheName
	 * @param key
	 * @throws Exception
	 */
	@Override
	public void remove(String cacheName, String key) throws Exception {
		Jedis jedis = getJedis();
		// modify by che.zi 20150519 forzmp:679688 begin
		// jedis.del(ObjectHelper.toByteArray(key));
		try {
			byte[] keyBytes = SerializerHelper.serialize(key);
			jedis.del(keyBytes);
		} catch (Exception e) {
			throw new Exception("redisÉ¾³ýkeyÊ§°Ü!", e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		// modify by che.zi 20150519 forzmp:679688 end
	}

	@Override
	public Pool<Jedis> getPool() {
		// TODO Auto-generated method stub
		return jedisPool;
	}

	@Override
	public void setJedisPool(Pool<Jedis> jedisPool) {
		// TODO Auto-generated method stub
		super.jedisPool = jedisPool;
	}
}
