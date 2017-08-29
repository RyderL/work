package com.zterc.uos.fastflow.service;

import com.zterc.uos.fastflow.dao.specification.AreaDAO;
import com.zterc.uos.fastflow.dto.specification.AreaDto;

/**
 * �����壩���������
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
	 * ����areaId������·���µ����������б�
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
	 * ����pathCode��ѯ��·���µ����������б�
	 * 
	 * @param pathCode
	 * @return
	 */
	public AreaDto[] findAreasByPathCode(String pathCode){
		return areaDAO.findAreasByPathCode(pathCode);
	}
	
	/**
	 * ����areaId��ѯ������Ϣ
	 * 
	 * @param areaId
	 * @return
	 */
	public AreaDto findAreaByAreaId(Long areaId){
		return areaDAO.findAreaByAreaId(areaId);
	}
}
