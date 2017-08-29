package com.ztesoft.uosflow.util.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zterc.uos.base.bean.BeanContextProxy;
import com.zterc.uos.base.helper.SerializerHelper;

public abstract class AbstractRedisDao {
	private static Logger logger = LoggerFactory.getLogger(AbstractRedisDao.class);
	protected RedisTemplate<String, byte[]> redisTemplate;
	protected String address;
	protected static int expireTime = 0;
	protected JdbcTemplate jdbcTemplate;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public RedisTemplate<String, byte[]> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, byte[]> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 设置redis缓存
	 * 
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void setValue(final String key, Object value) throws Exception {
		this.setValue(key, value, 0);
	}

	/**
	 * 从redis缓存获取数据
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public byte[] getValue(final String key) throws Exception {
		return this.getValue(key, 0);
	}

	/**
	 * 写入数据到redis队列
	 * 
	 * @param queueName
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public long rPush(final String queueName, Object value) throws Exception {
		return this.rPush(queueName, value, 0);
	}

	/**
	 * 从redis队列弹出数据
	 * 
	 * @param queueName
	 * @return
	 * @throws Exception
	 */
	public byte[] lPop(final String queueName) throws Exception {
		return this.lPop(queueName, 0);
	}

	/**
	 * 设置超时时间
	 * 
	 * @param key
	 * @param expireTime
	 * @return
	 */
	public Boolean setExpireValue(final String key, final long expireTime) {
		return this.setExpireValue(key, expireTime, 0);
	}

	/**
	 * 设置redis中的hash值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public Long hSet(final String key, final String field, final String value)
			throws Exception {
		return this.hSet(key, field, value, 0);
	}

	/**
	 * 取出redis里的hash值
	 * 
	 * @param key
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public Object hGet(final String key, final String field) throws Exception {
		return this.hGet(key, field, 0);
	}

	/**
	 * 计算redis中hash的长度
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Long hLen(final String key) throws Exception {
		return hLen(key, 0);
	}

	/**
	 * 删除redis中的hash变量
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Long hDel(final String key) throws Exception {
		return this.hDel(key, 0);
	}

	private void setValue(final String key, final Object value, int tryCount)
			throws Exception {
		// 将key也一起存放到队列中
		if (tryCount < 3) {
			try {
				final byte[] bytes = SerializerHelper.serialize(value);
				redisTemplate.execute(new RedisCallback<Void>() {
					@Override
					public Void doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set(key.getBytes(), bytes);
						// add by che.zi 20150624 begin
						connection.expire(key.getBytes(), expireTime);
						// add by che.zi 20150624 end
						return null;
					}
				});
			} catch (Exception e) {
				tryCount++;
				try {
					Integer interval = 0;
					JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanContextProxy
							.getBean("jdbcTemplate");
					String sql = "SELECT * FROM UOS_CONFIG WHERE ROUTE_ID = 1 AND NAME='REDIS_RETRY_INTERVAL_"
							+ tryCount+"'";
					List<Map<String, Object>> list = jdbcTemplate
							.queryForList(sql);
					if (list != null && list.size() > 0) {
						Map<String, Object> map = list.get(0);
						interval = MapUtils.getIntValue(map, "VALUE");
					}
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					logger.error("休眠失败", e1);
				}
				setValue(key, value, tryCount);
			}
		} else {
			throw new Exception("redis执行 setValue 出现异常！");
		}
	}

	private byte[] getValue(final String key, int tryCount) throws Exception {
		byte[] result = null;
		if (tryCount < 3) {
			try {
				result = redisTemplate.execute(new RedisCallback<byte[]>() {
					@Override
					public byte[] doInRedis(RedisConnection connection)
							throws DataAccessException {
						byte[] ret = connection.get(key.getBytes());
						return ret;
					}
				});
				return result;
			} catch (Exception e) {
				tryCount++;
				try {
					Integer interval = 0;
					JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanContextProxy
							.getBean("jdbcTemplate");
					String sql = "SELECT * FROM UOS_CONFIG WHERE ROUTE_ID = 1 AND NAME='REDIS_RETRY_INTERVAL_"
							+ tryCount+"'";
					List<Map<String, Object>> list = jdbcTemplate
							.queryForList(sql);
					if (list != null && list.size() > 0) {
						Map<String, Object> map = list.get(0);
						interval = MapUtils.getIntValue(map, "VALUE");
					}
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					logger.error("休眠失败", e1);
				}
				return getValue(key, tryCount);
			}
		} else {
			throw new Exception("redis执行 getValue 出现异常！");
		}
	}

	private long rPush(final String queueName, Object value, int tryCount)
			throws Exception {
		// 将key也一起存放到队列中
		long result = 0L;
		if (tryCount < 3) {
			try {
				final byte[] setValue = SerializerHelper.serialize(value);
				result = redisTemplate.execute(new RedisCallback<Long>() {
					@Override
					public Long doInRedis(RedisConnection redisConnection)
							throws DataAccessException {
						long ret = redisConnection.rPush(queueName.getBytes(),
								setValue);
						return ret;
					}
				});
				return result;
			} catch (Exception e) {
				tryCount++;
				try {
					Integer interval = 0;
					JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanContextProxy
							.getBean("jdbcTemplate");
					String sql = "SELECT * FROM UOS_CONFIG WHERE ROUTE_ID = 1 AND NAME='REDIS_RETRY_INTERVAL_"
							+ tryCount+"'";
					List<Map<String, Object>> list = jdbcTemplate
							.queryForList(sql);
					if (list != null && list.size() > 0) {
						Map<String, Object> map = list.get(0);
						interval = MapUtils.getIntValue(map, "VALUE");
					}
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					logger.error("休眠失败", e1);
				}
				return rPush(queueName, value, tryCount);
			}
		} else {
			throw new Exception("redis执行 rPush 出现异常！");
		}
	}

	private byte[] lPop(final String queueName, int tryCount) throws Exception {
		// 将key也一起存放到队列中
		byte[] result = null;
		if (tryCount < 3) {
			try {
				result = redisTemplate.execute(new RedisCallback<byte[]>() {
					@Override
					public byte[] doInRedis(RedisConnection redisConnection)
							throws DataAccessException {
						byte[] ret = redisConnection.lPop(queueName.getBytes());
						return ret;
					}
				});
				return result;
			} catch (Exception e) {
				tryCount++;
				try {
					Integer interval = 0;
					JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanContextProxy
							.getBean("jdbcTemplate");
					String sql = "SELECT * FROM UOS_CONFIG WHERE ROUTE_ID = 1 AND NAME='REDIS_RETRY_INTERVAL_"
							+ tryCount+"'";
					List<Map<String, Object>> list = jdbcTemplate
							.queryForList(sql);
					if (list != null && list.size() > 0) {
						Map<String, Object> map = list.get(0);
						interval = MapUtils.getIntValue(map, "VALUE");
					}
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					logger.error("休眠失败", e1);
				}
				return lPop(queueName, tryCount);
			}
		} else {
			throw new Exception("redis执行 lPop 出现异常！");
		}
	}

	/**
	 * 设置redis失效时间
	 * 
	 * @param key
	 * @param expireTime
	 * @return add by che.zi 20150624
	 */
	private Boolean setExpireValue(final String key, final long expireTime,
			int tryCount) {
		// 将key也一起存放到队列中
		Boolean result = null;
		if (tryCount < 3) {
			try {
				result = redisTemplate.execute(new RedisCallback<Boolean>() {
					@Override
					public Boolean doInRedis(RedisConnection redisConnection)
							throws DataAccessException {
						Boolean ret = redisConnection.expire(key.getBytes(),
								expireTime);
						return ret;
					}
				});
				return result;
			} catch (Exception e) {
				tryCount++;
				try {
					Integer interval = 0;
					JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanContextProxy
							.getBean("jdbcTemplate");
					String sql = "SELECT * FROM UOS_CONFIG WHERE ROUTE_ID = 1 AND NAME='REDIS_RETRY_INTERVAL_"
							+ tryCount+"'";
					List<Map<String, Object>> list = jdbcTemplate
							.queryForList(sql);
					if (list != null && list.size() > 0) {
						Map<String, Object> map = list.get(0);
						interval = MapUtils.getIntValue(map, "VALUE");
					}
					Thread.sleep(interval);
				} catch (Exception e1) {
					logger.error("休眠失败", e1);
				}
				return setExpireValue(key, expireTime, tryCount);
			}
		} else {
			logger.error("redis执行 setExpireValue 出现异常！");
			return false;
		}
	}

	private Long hSet(final String key, final String field, final String value,
			int tryCount) throws Exception {
		Integer result = null;
		if (tryCount < 3) {
			try {
				final byte[] valueBytes = SerializerHelper.serialize(value);
				result = redisTemplate.execute(new RedisCallback<Integer>() {
					public Integer doInRedis(RedisConnection connection)
							throws DataAccessException {
						byte[] hash = redisTemplate.getStringSerializer()
								.serialize(key + "_COUNTER");
						byte[] key = redisTemplate.getStringSerializer()
								.serialize(field);
						Boolean ret = connection.hSet(hash, key, valueBytes);
						connection.expire(hash, expireTime);// 增加缓存失效时间
						if (ret) {
							return 1;
						} else {
							return 0;
						}
					}
				});
				return result.longValue();
			} catch (Exception e) {
				tryCount++;
				try {
					Integer interval = 0;
					JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanContextProxy
							.getBean("jdbcTemplate");
					String sql = "SELECT * FROM UOS_CONFIG WHERE ROUTE_ID = 1 AND NAME='REDIS_RETRY_INTERVAL_"
							+ tryCount+"'";
					List<Map<String, Object>> list = jdbcTemplate
							.queryForList(sql);
					if (list != null && list.size() > 0) {
						Map<String, Object> map = list.get(0);
						interval = MapUtils.getIntValue(map, "VALUE");
					}
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					logger.error("休眠失败", e1);
				}
				return hSet(key, field, value, tryCount);
			}
		} else {
			throw new Exception("redis执行 hSet 出现异常！");
		}
	}

	public Object hGet(final String key, final String field, int tryCount)
			throws Exception {
		Object result = null;
		if (tryCount < 3) {
			try {
				byte[] value = redisTemplate
						.execute(new RedisCallback<byte[]>() {
							public byte[] doInRedis(RedisConnection connection)
									throws DataAccessException {
								byte[] hash = redisTemplate
										.getStringSerializer().serialize(
												key + "_COUNTER");
								byte[] key = redisTemplate
										.getStringSerializer().serialize(field);
								byte[] ret = connection.hGet(hash, key);
								return ret;
							}
						});
				result = SerializerHelper.deSerialize(value);
				return result;
			} catch (Exception e) {
				tryCount++;
				try {
					Integer interval = 0;
					JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanContextProxy
							.getBean("jdbcTemplate");
					String sql = "SELECT * FROM UOS_CONFIG WHERE ROUTE_ID = 1 AND NAME='REDIS_RETRY_INTERVAL_"
							+ tryCount+"'";
					List<Map<String, Object>> list = jdbcTemplate
							.queryForList(sql);
					if (list != null && list.size() > 0) {
						Map<String, Object> map = list.get(0);
						interval = MapUtils.getIntValue(map, "VALUE");
					}
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					logger.error("休眠失败", e1);
				}
				return hGet(key, field, tryCount);
			}
		} else {
			throw new Exception("redis执行 hGet 出现异常！");
		}
	}

	public Long hLen(final String key, int tryCount) throws Exception {
		Long result = null;
		if (tryCount < 3) {
			try {
				result = redisTemplate.execute(new RedisCallback<Long>() {
					public Long doInRedis(RedisConnection connection)
							throws DataAccessException {
						byte[] hash = redisTemplate.getStringSerializer()
								.serialize(key + "_COUNTER");
						Long ret = connection.hLen(hash);
						return ret;
					}
				});
				return result;

			} catch (Exception e) {
				tryCount++;
				try {
					Integer interval = 0;
					JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanContextProxy
							.getBean("jdbcTemplate");
					String sql = "SELECT * FROM UOS_CONFIG WHERE ROUTE_ID = 1 AND NAME='REDIS_RETRY_INTERVAL_"
							+ tryCount+"'";
					List<Map<String, Object>> list = jdbcTemplate
							.queryForList(sql);
					if (list != null && list.size() > 0) {
						Map<String, Object> map = list.get(0);
						interval = MapUtils.getIntValue(map, "VALUE");
					}
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					logger.error("休眠失败", e1);
				}
				return hLen(key, tryCount);
			}
		} else {
			throw new Exception("redis执行 hLen 出现异常！");
		}
	}

	public Long hDel(final String key, int tryCount) throws Exception {
		Long result = null;
		if (tryCount < 3) {
			try {
				result = redisTemplate.execute(new RedisCallback<Long>() {
					public Long doInRedis(RedisConnection connection)
							throws DataAccessException {
						byte[] hash = redisTemplate.getStringSerializer()
								.serialize(key + "_COUNTER");
						Long ret = connection.del(hash);
						return ret;
					}
				});
				return result;
			} catch (Exception e) {
				tryCount++;
				try {
					Integer interval = 0;
					JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanContextProxy
							.getBean("jdbcTemplate");
					String sql = "SELECT * FROM UOS_CONFIG WHERE ROUTE_ID = 1 AND NAME='REDIS_RETRY_INTERVAL_"
							+ tryCount+"'";
					List<Map<String, Object>> list = jdbcTemplate
							.queryForList(sql);
					if (list != null && list.size() > 0) {
						Map<String, Object> map = list.get(0);
						interval = MapUtils.getIntValue(map, "VALUE");
					}
					Thread.sleep(interval);
				} catch (InterruptedException e1) {
					logger.error("休眠失败", e1);
				}
				return hDel(key, tryCount);
			}
		} else {
			throw new Exception("redis执行 hDel 出现异常！");
		}
	}
}
