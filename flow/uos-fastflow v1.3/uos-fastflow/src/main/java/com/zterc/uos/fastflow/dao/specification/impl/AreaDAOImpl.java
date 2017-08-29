package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.List;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.specification.AreaDAO;
import com.zterc.uos.fastflow.dto.specification.AreaDto;

public class AreaDAOImpl extends AbstractDAOImpl implements AreaDAO{
	
	private static final String FIND_AREAS =" SELECT A.AREA_ID,A.AREA_NAME,A.PARENT_ID,A.PATH_CODE" +
			" ,A.PATH_NAME,A.CODE,A.GRADE,A.COMMENTS" +
			" FROM UOS_AREA A" +
			" WHERE A.STATE='10A' AND A.ROUTE_ID=1";
	
	private static final String BY_PATH_CODE =" AND A.PATH_CODE LIKE ?";
	
	private static final String BY_AREA_ID =" AND A.AREA_ID = ?";
	
	private static final String ORDER_BY_PATH_CODE = " ORDER BY A.PATH_CODE";

	
	@Override
	public AreaDto[] findAreas() {
		List<AreaDto> list = queryList(AreaDto.class, FIND_AREAS+ORDER_BY_PATH_CODE, new Object[]{});
		return list.toArray(new AreaDto[]{});
	}

	@Override
	public AreaDto[] findAreasByPathCode(String pathCode) {
		List<AreaDto> list = queryList(AreaDto.class, FIND_AREAS+BY_PATH_CODE+ORDER_BY_PATH_CODE,new Object[]{pathCode+"%"});
		return list.toArray(new AreaDto[]{});
	}

	@Override
	public AreaDto findAreaByAreaId(Long areaId) {
		return queryObject(AreaDto.class, FIND_AREAS + BY_AREA_ID+ORDER_BY_PATH_CODE, new Object[]{areaId});
	}
	
	
}
