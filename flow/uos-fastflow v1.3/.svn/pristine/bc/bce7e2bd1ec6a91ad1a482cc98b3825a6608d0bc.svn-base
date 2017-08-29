package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.Map;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.specification.PrivlegeDAO;

public class PrivlegeDAOImpl extends AbstractDAOImpl implements PrivlegeDAO {

	@Override
	public boolean isExistButtonPriv(Map<String, Object> param) {
		String sql = "SELECT COUNT(1) FROM UOS_PRIVLEGE UP,UOS_USER_PRIVLEGE UUP,"+FastflowConfig.useTableName+" UU " +
				" WHERE UP.PRIVLEGE_ID=UUP.PRIVLEGE_ID AND UU.STAFF_ID=UUP.STAFF_ID " +
				" AND UUP.STATE='10A' AND UP.PRIVLEGE_TYPE='BUTTON'" +
				" AND UU.STAFF_ID=? AND UP.PRIVLEGE_CODE=? AND UU.ROUTE_ID=1";
		boolean result = true;
		String str = queryForString(sql,new Object[]{StringHelper.valueOf(param.get("staffId")),
				StringHelper.valueOf(param.get("privlegeCode"))});
		int num = IntegerHelper.valueOf(str);
		if (num <1) {
			result = false;
		}
		return result;
	}

}
