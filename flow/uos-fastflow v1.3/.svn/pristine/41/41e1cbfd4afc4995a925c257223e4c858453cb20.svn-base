package com.zterc.uos.fastflow.config;

import java.util.ArrayList;
import java.util.List;

public class FastflowConfig {
	public static boolean isCacheModel = true;
	public static String tacheTableName = "UOS_TACHE_DEF";
	public static String commandCfgTableName = "UOS_COMMAND_CFG";
	public static String useTableName = "UOS_USER";
	public static boolean useTimeLimit = true;
	public static boolean loadStaticCache = true;
	public static boolean useInvocation = true;
	public static boolean useHis = true;
	public static boolean usePersistSelf = true;
	public static int sqlPersistQueueCount = 5;
	public static int maxBatchDealCount = 1000;
	public static long freeSleepTime = 100L;
	public static boolean rollbackBySingle = false;
	public static boolean getTacheParamImm = false;
	public static String needLockCommand = null;
	public static List<String> lockCommands = new ArrayList<String>();
	
	/**
	 * 使用第三方异步持久化，默认当然是false啦
	 */
	public static boolean usePersistBy3th = false;

	public static void setUseHis(boolean useHis) {
		FastflowConfig.useHis = useHis;
	}

	public static void setUseInvocation(boolean useInvocation) {
		FastflowConfig.useInvocation = useInvocation;
	}

	public static void setIsCacheModel(boolean isCacheModel) {
		FastflowConfig.isCacheModel = isCacheModel;
	}

	public static void setTacheTableName(String tacheTableName) {
		FastflowConfig.tacheTableName = tacheTableName;
	}

	public static void setCommandCfgTableName(String commandCfgTableName) {
		FastflowConfig.commandCfgTableName = commandCfgTableName;
	}

	public static void setUseTableName(String useTableName) {
		FastflowConfig.useTableName = useTableName;
	}

	public static void setUseTimeLimit(boolean useTimeLimit) {
		FastflowConfig.useTimeLimit = useTimeLimit;
	}

	public static void setLoadStaticCache(boolean loadStaticCache) {
		FastflowConfig.loadStaticCache = loadStaticCache;
	}

	public static void setUsePersistSelf(boolean usePersistSelf) {
		FastflowConfig.usePersistSelf = usePersistSelf;
	}

	public static void setUsePersistBy3th(boolean usePersistBy3th) {
		FastflowConfig.usePersistBy3th = usePersistBy3th;
	}

	public static void setSqlPersistQueueCount(int sqlPersistQueueCount) {
		FastflowConfig.sqlPersistQueueCount = sqlPersistQueueCount;
	}

	public static void setMaxBatchDealCount(int maxBatchDealCount) {
		FastflowConfig.maxBatchDealCount = maxBatchDealCount;
	}

	public static void setFreeSleepTime(long freeSleepTime) {
		FastflowConfig.freeSleepTime = freeSleepTime;
	}

	public static void setRollbackBySingle(boolean rollbackBySingle) {
		FastflowConfig.rollbackBySingle = rollbackBySingle;
	}
	
	public static void setGetTacheParamImm(boolean getTacheParamImm) {
		FastflowConfig.getTacheParamImm = getTacheParamImm;
	}

	public static String getNeedLockCommand() {
		return needLockCommand;
	}

	public static void setNeedLockCommand(String needLockCommand) {
		FastflowConfig.needLockCommand = needLockCommand;
		if(needLockCommand != null && !"".equals(needLockCommand)){
			String[] commandArr = needLockCommand.split(",");
			if(commandArr != null){
				for(int i=0;i<commandArr.length;i++){
					lockCommands.add(commandArr[i]);
				}
			}
		}
	}

}
