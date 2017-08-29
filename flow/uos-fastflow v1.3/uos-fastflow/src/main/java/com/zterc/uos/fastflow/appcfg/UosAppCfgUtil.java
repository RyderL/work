package com.zterc.uos.fastflow.appcfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.dao.specification.impl.AppCfgDAOImpl;

public class UosAppCfgUtil {
	public static final String DEFAULT_APP_NAME = "default";
	public static final String SERVER_NAME = "server.name";
	private static JdbcTemplate jdbcTemplate;
	private static boolean inited = false;
	private static String appName;
	private static Map<String, Map<String, String>> appCfgMap = new HashMap<String, Map<String, String>>();

	public static JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		UosAppCfgUtil.jdbcTemplate = jdbcTemplate;
	}

	public static String getAppName() {
		return appName;
	}

	public static void setAppName(String appName) {
		UosAppCfgUtil.appName = appName;
	}

	public static void init() {
		if (!inited) {
			appName = System.getProperty(SERVER_NAME);
			if (StringHelper.isEmpty(appName)) {
				appName = DEFAULT_APP_NAME;
			}

			refreshAppCfgMap();

			inited = true;
		}
	}

	public static void refreshAppCfgMap() {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(AppCfgDAOImpl.QUERY_APP_CFG);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String name = String.valueOf(map.get("app_name"));
			String pKey = String.valueOf(map.get("p_key"));
			String pValue = String.valueOf(map.get("p_value"));

			if (!appCfgMap.containsKey(name)) {
				appCfgMap.put(name, new HashMap<String, String>());

			}
			appCfgMap.get(name).put(pKey, pValue);
		}
	}

	public static String getValue(String key) {
		return getValue(appName, key);
	}

	public static String getValue(String aName, String key) {
		return appCfgMap.get(aName).get(key);
	}

	public static AppInfoDto getAppInfoDto() {
		return getAppInfoDto(appName);
	}

	public static AppInfoDto getAppInfoDto(String appName) {
		Map<String, String> map = appCfgMap.get(appName);
		Assert.notEmpty(map);

		String host = map.get("jmx-ip");
		String port = map.get("jmx-port");
		String type = map.get("thread-type");

		AppInfoDto appInfoDto = new AppInfoDto();
		appInfoDto.setHost(host);
		appInfoDto.setPort(port);
		appInfoDto.setType(type);
		appInfoDto.setAppName(appName);

		return appInfoDto;
	}

	/**
	 * 供manager使用查询
	 * 
	 * @return
	 */
	public static List<AppInfoDto> getAppInfoDtos() {
		refreshAppCfgMap();
		List<AppInfoDto> list = new ArrayList<AppInfoDto>();
		for (String key : appCfgMap.keySet()) {
			AppInfoDto appInfoDto = getAppInfoDto(key);
			list.add(appInfoDto);
		}
		return list;
	}
}
