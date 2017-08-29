package com.zterc.uos.fastflow.dao.process.impl;

import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.process.DbPersitExceptionDAO;
import com.zterc.uos.fastflow.dto.process.DbpersistExceptionDto;

public class DbPersitExceptionDAOImpl extends AbstractDAOImpl implements
		DbPersitExceptionDAO {

	@Override
	public void createDbPersitException(
			DbpersistExceptionDto dbpersistExceptionDto) {
		String sql = "INSERT INTO UOS_DB_PERSIST_EXCEPTION(ID,SQLKEY,SQLSTR,PARAM,STATE,CREATE_DATE,STATE_DATE,EXCEPTION_DESC)" +
				" VALUES(?,?,?,?,?,?,?,?)";
		Object[] args = new Object[]{dbpersistExceptionDto.getId(),
				dbpersistExceptionDto.getSqlKey(),
				dbpersistExceptionDto.getSqlStr(),
				dbpersistExceptionDto.getParam(),
				dbpersistExceptionDto.getState(),
				dbpersistExceptionDto.getCreateDate(),
				dbpersistExceptionDto.getStateDate(),
				dbpersistExceptionDto.getExceptionDesc()};
		saveOrUpdate(buildMap(sql,args));
	}

	
}
