package com.zterc.uos.fastflow.service;

import com.zterc.uos.fastflow.dao.specification.AreaDAO;
import com.zterc.uos.fastflow.dto.specification.AreaDto;

/**
 * （定义）区域操作类
 * 
 * @author gong.yi
 *
 */
public class AreaService {
	private AreaDAO areaDAO;
	
	public void setAreaDAO(AreaDAO areaDAO) {
		this.areaDAO = areaDAO;
	}

	/**
	 * 根据areaId查找其路径下的所有区域列表
	 * 
	 * @param areaId
	 * @return
	 */
	public AreaDto[] getAreas(Long areaId){
		AreaDto[] areas = null;
		if(areaId!=null&&areaId!=-1){
			AreaDto areaDto = areaDAO.findAreaByAreaId(areaId);
			if(areaDto!=null){
				areas = areaDAO.findAreasByPathCode(areaDto.getPathCode());
			}
		}else{
			areas = areaDAO.findAreas();
		}
		return areas;
	}
	
	/**
	 * 根据pathCode查询其路径下的所有区域列表
	 * 
	 * @param pathCode
	 * @return
	 */
	public AreaDto[] findAreasByPathCode(String pathCode){
		return areaDAO.findAreasByPathCode(pathCode);
	}
	
	/**
	 * 根据areaId查询区域信息
	 * 
	 * @param areaId
	 * @return
	 */
	public AreaDto findAreaByAreaId(Long areaId){
		return areaDAO.findAreaByAreaId(areaId);
	}
}
