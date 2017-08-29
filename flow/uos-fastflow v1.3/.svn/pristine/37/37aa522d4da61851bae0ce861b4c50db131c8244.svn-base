package com.zterc.uos.fastflow.service;

import java.util.List;

import com.zterc.uos.fastflow.dao.specification.AppCfgDAO;
import com.zterc.uos.fastflow.dto.specification.AppCfgDto;

/**
 * （定义）进程app配置操作类
 * 
 * @author gong.yi
 *
 */
public class AppCfgService {
	private AppCfgDAO appCfgDAO;

	public AppCfgDAO getAppCfgDAO() {
		return appCfgDAO;
	}

	public void setAppCfgDAO(AppCfgDAO appCfgDAO) {
		this.appCfgDAO = appCfgDAO;
	}
	
	/**
	 * 根据id查找app配置
	 * 
	 * @param id
	 * @return
	 */
	public AppCfgDto queryAppCfgDtoById(Long id){
		return appCfgDAO.queryAppCfgDtoById(id);
	}
	
	/**
	 * 更新app配置
	 * 
	 * @param appCfgDto
	 */
	public void updateAppCfgDto(AppCfgDto appCfgDto){
		appCfgDAO.updateAppCfgDto(appCfgDto);
	}
	
	/**
	 * 查询所有的app配置
	 * 
	 * @return
	 */
	public List<AppCfgDto> queryAllAppCfgDtos(){
		return appCfgDAO.queryAllAppCfgDtos();
	}

	/**
	 * 根据APPName和key值查询APP配置
	 * @param appName
	 * @param string
	 */
	public AppCfgDto queryAppCfgDtoByKey(String appName, String pkey) {
		return appCfgDAO.queryAppCfgDtoByKey(appName,pkey);
	}
}
