package com.zterc.uos.base.lock.local;

import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.lock.LockConfig;

public class LocalLockUtil {
	private static Map<String,LocalLock> localLockMap = new HashMap<String, LocalLock>();

	public synchronized static LocalLock getLocalLock(String key,LockConfig lockConfig){
		LocalLock localLock = null;
		if(localLockMap.containsKey(key)){
			localLock = localLockMap.get(key);
		}else{
			localLock = new LocalLock(key, lockConfig);
			localLockMap.put(key, localLock);
		}
		return localLock;
	}
}
