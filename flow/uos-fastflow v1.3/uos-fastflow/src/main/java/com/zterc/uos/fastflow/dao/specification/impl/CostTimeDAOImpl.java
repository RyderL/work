package com.zterc.uos.fastflow.dao.specification.impl;

import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.specification.CostTimeDAO;

public class CostTimeDAOImpl extends AbstractDAOImpl implements CostTimeDAO {
	
	private static final String SAVE_COSTTIME = "insert into uos_time_cost(PROCESSINSTANCEID,COMMAND_CODE,COSTTIME)"
			+ " VALUES(?,?,?)";

	@Override
	public void saveCostTime(String processInstanceId, String flowCommand,
			long costTime) throws Exception {
		Object[] args = new Object[] {
				processInstanceId,
				flowCommand,
				costTime };
		saveOrUpdate(buildMap(SAVE_COSTTIME, args));
	}

}
