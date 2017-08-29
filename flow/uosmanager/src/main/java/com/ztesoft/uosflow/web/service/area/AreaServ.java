package com.ztesoft.uosflow.web.service.area;

import java.sql.SQLException;
import java.util.Map;

/**
 * 
 * 区域服务接口
 * @author gong.yi 2015.05.04
 *
 */
public interface AreaServ {
	/**
	 * 根据参数查找  区域树（easyui）
	 * @param jsonParam  可选：带areaId或者不带areaId，不带查询所有，带则只查询该区域id下联的树
	 * @return
	 * @throws SQLException
	 */
	public String getAreaJsonTree(Map<String,Object>  map) throws SQLException;
}
