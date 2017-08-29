package com.ztesoft.uosflow.util.mq.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import com.zterc.uos.base.helper.SerializerHelper;
import com.ztesoft.uosflow.util.cache.AbstractRedisDao;

public class QueueRedisServer extends AbstractRedisDao {
	private static Logger logger = LoggerFactory.getLogger(QueueRedisServer.class);

	public void put(final String queueName, Object value) throws Exception {
		// ��keyҲһ���ŵ�������
		rPush(queueName, value);
	}

	public byte[] get(final String queueName) throws Exception {
		byte[] ret = lPop(queueName);
		return ret;
	}

	public void putFileMsg(String queueName, String key) throws Exception {
		rPush(queueName, key);
	}

	public byte[] popFileMsg(String queueName) throws Exception {
		byte[] ret = lPop(queueName);
		return ret;
	}

	public Object[] get(String queueName, String key) throws Exception {
		return null;
	}

	public Object getItem(final String key) throws Exception {
		Object value = null;
		byte[] ret = redisTemplate.execute(new RedisCallback<byte[]>() {
			public byte[] doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] ret = connection.get(key.getBytes());
				return ret;
			}
		});
		value = SerializerHelper.deSerialize(ret);
		return value;
	}

	public void putQueueKey(String queueName, String key, Object obj)
			throws Exception {
		setValue(key, obj);
		rPush(queueName, key);
	}

	public List<byte[]> qryList(final String queueName, final long start,
			final long end) throws Exception {
		List<byte[]> byteList = redisTemplate
				.execute(new RedisCallback<List<byte[]>>() {
					public List<byte[]> doInRedis(RedisConnection connection)
							throws DataAccessException {
						List<byte[]> ret = connection.lRange(
								queueName.getBytes(), start, end);
						return ret;
					}
				});
		logger.info("QueueRedisTempaletImpl.byteList.size()=" + byteList.size());
		return byteList;
	}

	/**
	 * ����ϣ�� key �е��� field ��ֵ��Ϊ value
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return �����µ�ֵ�򷵻�1���������е�ֵ�򷵻�0
	 * @throws Exception
	 */
	public Long hset(final String key, final String field, final String value)
			throws Exception {
		return hSet(key, field, value);
	}

	/**
	 * ��ȡ��ϣ�� key �е��� field ��ֵ( value )
	 * 
	 * @param key
	 * @param field
	 * @return Object
	 * @throws Exception
	 */
	public Object hget(final String key, final String field) throws Exception {
		return hGet(key, field);
	}

	/**
	 * ��ȡ��ϣ�� key �ĳ���
	 * 
	 * @param key
	 * @return Long
	 * @throws Exception
	 */
	public Long hlen(final String key) throws Exception {
		return hLen(key);
	}

	/**
	 * ɾ��һ�� key
	 * 
	 * @param key
	 * @return Long ��ɾ�� key ������
	 * @throws Exception
	 */
	public Long hdel(final String key) throws Exception {
		return hDel(key);
	}

	public void flushdb() throws Exception {
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.flushDb();
				return 1L;
			}
		});
	}

	public long count(final String queueName) throws Exception {
		long size = redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long ret = connection.lLen(queueName.getBytes());
				return ret;
			}
		});
		return size;
	}

	public Long lrem(final byte[] queueName, final int count,
			final byte[] byteArry) throws Exception {
		// TODO Auto-generated method stub
		long delCount = redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long ret = connection.lRem(queueName, count, byteArry);
				return ret;
			}
		});
		return delCount;
	}

}
