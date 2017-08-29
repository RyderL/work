package com.zterc.uos.base.jdbc;

public class DsContextHolder {
	
	public static final String DS_INSTANCE = "DS_INSTANCE";
	public static final String DS_DEFAULT = "DS_DEFAULT";
	public static final String DS_ARCHIEVED = "DS_ARCHIEVED";
	public static final String DS_HISTORY = "DS_HISTORY";
	
	private static int mod;
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	public static int getMod() {
		return mod;
	}

	public static void setMod(int mod) {
		DsContextHolder.mod = mod;
	}

	public static void setDsForDefault() {
		contextHolder.set(DS_DEFAULT);
	}
	
	public static void setDsForInstance(long route) {
		if(mod==0){
			contextHolder.set(DS_DEFAULT);
			return ;
		}
		long r = caculate(route);
		contextHolder.set("DS_INSTANCE"+r);
	}
	
	private static long caculate(long route) {
		return route%mod;
	}

	public static void setDsForAchieved() {
		contextHolder.set(DS_ARCHIEVED);
	}
	
	public static void setDsForHistory() {
		contextHolder.set(DS_HISTORY);
	}
	
	public static void setHoldDs(String name) {
		contextHolder.set(name);
	}

	public static String getHoldDs() {
		return contextHolder.get();
	}

	public static void clearHoldDs() {
		contextHolder.remove();
	}
}
