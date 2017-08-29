package com.ztesoft.uosflow.jmx.server.bl.cachemanager;

import com.zterc.uos.base.helper.StaticCacheHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.service.ProcessDefinitionService;
import com.zterc.uos.fastflow.service.ProcessPackageService;
import com.zterc.uos.fastflow.service.ProcessParamDefService;
import com.zterc.uos.fastflow.service.ReturnReasonService;
import com.zterc.uos.fastflow.service.TacheService;


public class StaticCacheManager {
	private static TacheService tacheService;
	private static ProcessParamDefService processParamDefService;
	private static ProcessDefinitionService processDefinitionService;
	private static ProcessPackageService processPackageService;
	private static ReturnReasonService returnReasonService;
	
	public static TacheService getTacheService() {
		return tacheService;
	}

	public static void setTacheService(TacheService tacheService) {
		StaticCacheManager.tacheService = tacheService;
	}

	public static ProcessParamDefService getProcessParamDefService() {
		return processParamDefService;
	}

	public static void setProcessParamDefService(
			ProcessParamDefService processParamDefService) {
		StaticCacheManager.processParamDefService = processParamDefService;
	}

	public static ProcessDefinitionService getProcessDefinitionService() {
		return processDefinitionService;
	}

	public static void setProcessDefinitionService(
			ProcessDefinitionService processDefinitionService) {
		StaticCacheManager.processDefinitionService = processDefinitionService;
	}

	public static ProcessPackageService getProcessPackageService() {
		return processPackageService;
	}

	public static void setProcessPackageService(
			ProcessPackageService processPackageService) {
		StaticCacheManager.processPackageService = processPackageService;
	}

	public static ReturnReasonService getReturnReasonService() {
		return returnReasonService;
	}

	public static void setReturnReasonService(
			ReturnReasonService returnReasonService) {
		StaticCacheManager.returnReasonService = returnReasonService;
	}

	public static void loadAllStaticCache(){
		//如果是初始化加载静态缓存，则刷新缓存时先 将一次性加载缓存设置为false（以便可以让在途进程查不到缓存可以查数据库），然后再重新加载缓存。最后将一次性加载缓存的开关再打开
		boolean loadStaticCache = false;
		if(FastflowConfig.loadStaticCache){
			FastflowConfig.setLoadStaticCache(false);
			loadStaticCache = true;
		}
		StaticCacheHelper.clearAll();
		tacheService.loadAllTache();
		processParamDefService.loadAllProcessParamDef();
		processDefinitionService.loadAllProcessDefinition();
		processPackageService.loadAllProcessPackage();
		returnReasonService.loadAllReturnReasonConfig();
		if(loadStaticCache){
			FastflowConfig.setLoadStaticCache(true);
		}
	}

}
