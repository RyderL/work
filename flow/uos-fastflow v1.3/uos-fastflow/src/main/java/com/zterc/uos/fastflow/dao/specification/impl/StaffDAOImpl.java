package com.zterc.uos.fastflow.dao.specification.impl;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.specification.StaffDAO;
import com.zterc.uos.fastflow.dto.specification.StaffDto;

public class StaffDAOImpl extends AbstractDAOImpl implements StaffDAO{
	@Override
	public int insertStaff(StaffDto staff) {
		return 1;
	}

	private static final String QUERY_STAFF ="SELECT * FROM "+FastflowConfig.useTableName+" WHERE ROUTE_ID=1 AND USERNAME= ?";
	@Override
	public StaffDto queryStaffByUsername(String username) {
		return queryObject(StaffDto.class,QUERY_STAFF,username);
	}

	private static final String QUERY_IP_COUNT="SELECT COUNT(1) FROM UOS_IP WHERE IP_ADDRESS = ? AND ROUTE_ID = 1";
	@Override
	public boolean isIpOk(String ip) {
		boolean result = true;
		String str = queryForString(QUERY_IP_COUNT,new Object[]{ip});
		int num = IntegerHelper.valueOf(str);
		if (num <1) {
			result = false;
		}
		return result;
	}
	
}
