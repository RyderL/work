package com.ztesoft.uosflow.core.counter;

import java.util.concurrent.ConcurrentHashMap;

public class CounterCenter {
	public static ConcurrentHashMap<String, Long> counterMap = new ConcurrentHashMap<String, Long>();

	public static void increment(String commandCode) {
		synchronized (commandCode) {
			if (!counterMap.containsKey(commandCode)) {
				counterMap.put(commandCode, 0l);
			}
			counterMap.put(commandCode, counterMap.get(commandCode)+1) ;
		}
	}

	public synchronized static void clear(String commandCode) {
		counterMap.put(commandCode, 0l);
	}

	public  static void clearAll() {
		for (String key : counterMap.keySet()) {
			clear(key);
		}
	}
}
