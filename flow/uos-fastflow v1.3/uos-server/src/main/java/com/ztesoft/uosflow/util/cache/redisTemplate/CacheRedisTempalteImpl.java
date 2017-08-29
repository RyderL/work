package com.ztesoft.uosflow.util.cache.redisTemplate;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import com.zterc.uos.base.helper.SerializerHelper;
import com.ztesoft.uosflow.util.cache.AbstractRedisDao;
import com.ztesoft.uosflow.util.cache.CacheInterface;
import com.ztesoft.uosflow.util.cache.CacheUtils;
import com.ztesoft.uosflow.util.cache.SeedUtils;

public class CacheRedisTempalteImpl extends AbstractRedisDao implements
		CacheInterface {

	@Override
	public Object get(String cacheName, String key) throws Exception {
		Object value = null;
		byte[] ret = getValue(key);
		value = SerializerHelper.deSerialize(ret);
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
		expireTime = SeedUtils.getExpireTime();
		setValue(key, value);
	}

	/**
	 * @param cacheName
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean contain(String cacheName, String key) throws Exception {
		final byte[] keyBytes = key.getBytes();
		Boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				Boolean ret = connection.exists(keyBytes);
				return ret;
			}
		});
		return ret;
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
		final byte[] keyBytes = key.getBytes();
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long ret = connection.del(keyBytes);
				return ret;
			}
		});
	}

	public void setAddress(String address) {
		this.address = address;
		CacheUtils.addressMap.put(this.address.trim(), this);
	}

}
