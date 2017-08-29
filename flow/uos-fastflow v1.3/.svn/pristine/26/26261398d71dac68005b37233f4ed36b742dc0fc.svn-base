package com.zterc.uos.base.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.lock.FlowLock;
import com.zterc.uos.base.lock.LockConfig;

import redis.clients.jedis.Jedis;

/**
 * redis分布式锁
 * 
 * @author gong.yi
 *
 */
public class RedisLock implements FlowLock{
	
	private Logger logger  = LoggerFactory.getLogger(RedisLock.class);

	static final String LOCKPREFIX = "UOS_FLOW_LOCK_";
	
    private String  key;
    
    private String routeKey;
    
    private LockConfig lockConfig;

    boolean locked = false;
   
    public RedisLock(String key,LockConfig lockConfig){
    	this.key = LOCKPREFIX + key;
    	this.routeKey = key;
    	this.lockConfig = lockConfig;
    }
    
    public synchronized boolean acquire(){
    	Jedis jedis = null;
    	try {
    		jedis = RedisUtil.getInstance().getJedis(routeKey);
			int timeout = lockConfig.getTimeoutMsecs();
			long startTime = System.currentTimeMillis();
			int count = 0;
			while (timeout >= 0) {
				count++;
			    long expires = System.currentTimeMillis() + lockConfig.getExpireMsecs() + 1;
			    String expiresStr = String.valueOf(expires); //锁到期时间

			    if (jedis.setnx(key, expiresStr) == 1) {
			    	jedis.expire(key,lockConfig.getExpireMsecs()/1000);
			        // lock acquired
			        locked = true;
			        logger.info("redis锁获取时间："+count+"次-"+(System.currentTimeMillis()-startTime)+"ms");
			        return true;
			    }

			    String currentValueStr = jedis.get(key); //redis里的时间
			    if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
			        //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
			        // lock is expired
			        String oldValueStr = jedis.getSet(key, expiresStr);
					jedis.expire(key,lockConfig.getExpireMsecs()/1000);

					//获取上一个锁到期时间，并设置现在的锁到期时间，
			        //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
			        if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
			            //如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
			            // lock acquired
			            locked = true;
			            logger.info("redis锁获取时间："+count+"次-"+(System.currentTimeMillis()-startTime)+"ms");
			            return true;
			        }
			    }
			    timeout -= lockConfig.getSleepTime();
			    try {
					Thread.sleep(lockConfig.getSleepTime());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		} catch (Exception e) {
			logger.error("锁获取异常，lockKey：" + key,e);
			RedisUtil.getInstance().returnBrokenResource(jedis, routeKey);
			throw new RuntimeException("锁获取异常，lockKey：" + key,e);
		} finally{
			RedisUtil.getInstance().returnResource(jedis, routeKey);
		}
        return false;
    }

    public synchronized void release() {
    	Jedis jedis = null;
    	try {
			jedis = RedisUtil.getInstance().getJedis(routeKey);
			if (locked) {
				jedis.del(key);
				locked = false;
			}
		} catch (Exception e) {
    		logger.error("释放锁失败!!",e);
			RedisUtil.getInstance().returnBrokenResource(jedis, routeKey);
		} finally{
			RedisUtil.getInstance().returnResource(jedis, routeKey);
		}
    }

    /**
     * Acqurired lock release.
     */
    public synchronized void release(Jedis jedis) {
      
    }

}
