package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.List;

import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.specification.MultiClientDAO;
import com.zterc.uos.fastflow.dto.specification.MultiClientDto;

public class MultiClientDAOImpl extends AbstractDAOImpl implements
		MultiClientDAO {
	private static final String QUERY_MULTI_CLIENT_CFG = "SELECT SYSTEM,SYSTEM_BEAN,INF_TYPE,WSDL_URL " +
			" FROM UOS_MULTI_CLIENT_CFG WHERE ROUTE_ID=1";

	@Override
	public List<MultiClientDto> qryAllMultiClientDto() {
		return queryList(MultiClientDto.class, QUERY_MULTI_CLIENT_CFG, new Object[] {});
	}

}
