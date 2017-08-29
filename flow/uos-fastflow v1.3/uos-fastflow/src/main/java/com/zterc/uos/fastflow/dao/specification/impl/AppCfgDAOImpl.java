package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.List;

import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.specification.AppCfgDAO;
import com.zterc.uos.fastflow.dto.specification.AppCfgDto;

public class AppCfgDAOImpl extends AbstractDAOImpl implements AppCfgDAO {

	public static final String QUERY_APP_CFG = "SELECT * FROM UOS_APP_CFG WHERE ROUTE_ID=1";

	public static final String UPDATE_APP_CFG = "UPDATE UOS_APP_CFG SET APP_NAME=?,P_KEY=?,P_VALUE=? WHERE ID = ?";

	@Override
	public List<AppCfgDto> queryAllAppCfgDtos() {
		return queryList(AppCfgDto.class, QUERY_APP_CFG, new Object[] {});
	}

	@Override
	public void updateAppCfgDto(AppCfgDto appCfgDto) {
		Object[] args = new Object[] { appCfgDto.getAppName(),
				appCfgDto.getpKey(), appCfgDto.getpValue(),
				appCfgDto.getComment(), appCfgDto.getId() };
		saveOrUpdate(buildMap(UPDATE_APP_CFG, args));
	}

	@Override
	public AppCfgDto queryAppCfgDtoById(Long id) {
		return queryObject(AppCfgDto.class, QUERY_APP_CFG + " AND ID = ?",
				new Object[] { id });
	}

	@Override
	public AppCfgDto queryAppCfgDtoByKey(String appName, String pkey) {
		return  queryObject(AppCfgDto.class, QUERY_APP_CFG + " AND APP_NAME = ? AND P_KEY=?",
				new Object[] { appName,pkey });
	}

}
