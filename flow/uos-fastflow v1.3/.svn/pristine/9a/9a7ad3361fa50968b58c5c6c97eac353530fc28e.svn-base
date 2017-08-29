package com.zterc.uos.base.lock;

import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.lock.blank.BlankLock;
import com.zterc.uos.base.lock.local.LocalLockUtil;
import com.zterc.uos.base.lock.redis.RedisLock;

public class LockFactory {

	private static final String BLANK = "Blank";
	private static final String REDIS = "Redis";
	private static final String LOCAL = "Local";

	// 锁类型
	private static String lockType = "";// Blank/Redis/Local

	// 锁配置
	private static LockConfig lockConfig;

	private static BlankLock blankLock = new BlankLock();

	public static void setLockType(String lockType) {
		LockFactory.lockType = lockType;
	}

	public static void setLockConfig(LockConfig lockConfig) {
		LockFactory.lockConfig = lockConfig;
	}

	// public static Lock getLock(String key) {
	// if (lockType.equalsIgnoreCase(BLANK)) {
	// return blankLock;
	// }
	// String lockKey = key.intern();
	// synchronized (lockKey) {
	// ValueWrapper vw = StaticCacheHelper.getCache(LOCK_CACHE).get(key);
	// if(vw!=null){
	// return (Lock)vw.get();
	// }else{
	// Lock lock = null;
	// if (lockType.equalsIgnoreCase(REDIS)) {
	// lock = new RedisLock(key, lockConfig);
	// } else if (lockType.equalsIgnoreCase(ZOOKEEPER)) {
	// lock = new ZookeeperLock(key, lockConfig);
	// } else if (lockType.equalsIgnoreCase(LOCAL)) {
	// lock = new ReentrantLock();
	// } else {
	// lock = new ReentrantLock();
	// }
	// StaticCacheHelper.getCache(LOCK_CACHE).put(key, lock);
	// return lock;
	// }
	// }
	// }

	/**
	 * 加锁的情况下面，根据key获取锁对象
	 * 
	 * @param key
	 * @return
	 */
	public static FlowLock getLock(String key) {
		if (StringHelper.isEmpty(lockType)
				|| lockType.equalsIgnoreCase(BLANK)) {
			return blankLock;
		}
		FlowLock lock = null;
		if (lockType.equalsIgnoreCase(REDIS)) {
			lock = new RedisLock(key, lockConfig);
		}else if(lockType.equalsIgnoreCase(LOCAL)){
			lock = LocalLockUtil.getLocalLock(key,lockConfig);
		}
		return lock;
	}

	/**
	 * BLANK表示不加锁（一般用于串行的性能测试）
	 * 
	 * @return
	 */
	public static boolean isNoLock() {
		return !StringHelper.isEmpty(lockType)
				&& lockType.equalsIgnoreCase(BLANK);
	}

}
