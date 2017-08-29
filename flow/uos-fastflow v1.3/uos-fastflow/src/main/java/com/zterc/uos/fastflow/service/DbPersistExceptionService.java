package com.zterc.uos.fastflow.service;

import com.zterc.uos.fastflow.dao.process.DbPersitExceptionDAO;
import com.zterc.uos.fastflow.dto.process.DbpersistExceptionDto;

public class DbPersistExceptionService {
	private DbPersitExceptionDAO dbPersitExceptionDAO;
	
	public void setDbPersitExceptionDAO(DbPersitExceptionDAO dbPersitExceptionDAO) {
		this.dbPersitExceptionDAO = dbPersitExceptionDAO;
	}

	public void save(DbpersistExceptionDto dbpersistExceptionDto){
		dbPersitExceptionDAO.createDbPersitException(dbpersistExceptionDto);
	}

}
