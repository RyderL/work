package com.ztesoft.uosflow.util.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.fastflow.appcfg.UosAppCfgUtil;
import com.zterc.uos.fastflow.dto.specification.AppCfgDto;
import com.zterc.uos.fastflow.service.AppCfgService;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;

/**
 * Created with IntelliJ IDEA. User: Yolanda Date: 15-6-17 Time: 下午4:18 To
 * change this template use File | Settings | File Templates.
 */
public class SeedUtils {
	private static final Logger logger = LoggerFactory.getLogger(SeedUtils.class);
	private static final int MOD_NUM = 1024;
	private volatile static SeedUtils seedUtils = null;
	private static Integer EXPIRE_TIME;

	public SeedUtils() {
		// 初始化
		this.init();
	}

	public static SeedUtils getInstance() {
		if (seedUtils == null) {
			synchronized (SeedUtils.class) {
				if (seedUtils == null) {
					seedUtils = new SeedUtils();
				}
			}
		}
		return seedUtils;
	}

	private void init() {
		EXPIRE_TIME = 604800;// 默认七天
		AppCfgService appCfgService = (AppCfgService) ApplicationContextProxy.getBean("appCfgService");
		AppCfgDto appCfgDto = appCfgService.queryAppCfgDtoByKey(UosAppCfgUtil.getAppName(),"redis-expireTime");
		EXPIRE_TIME = IntegerHelper.valueOf(appCfgDto.getpValue());
	}

	public static int getExpireTime() {
		return EXPIRE_TIME;
	}

	public Integer getNumberByKey(String key) {
		char lastChar = key.charAt(key.length() - 1);
		return (1000 + (int) lastChar) % MOD_NUM;
	}

	public Integer getNumberByString(String key) {
		Integer number;
		try {
			key = key.substring(key.length() - 4);
			number = IntegerHelper.valueOf(key) % MOD_NUM;
		} catch (Exception e) {
			char lastChar = key.charAt(key.length() - 1);
			number = (1000 + (int) lastChar) % MOD_NUM;
		}
		return number;
	}

}
