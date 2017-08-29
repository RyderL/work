package com.zterc.uos.fastflow.service;

import java.util.Map;

import com.zterc.uos.fastflow.dao.specification.PrivlegeDAO;

public class PrivlegeService {
	private PrivlegeDAO privlegeDAO;

	public PrivlegeDAO getPrivlegeDAO() {
		return privlegeDAO;
	}

	public void setPrivlegeDAO(PrivlegeDAO privlegeDAO) {
		this.privlegeDAO = privlegeDAO;
	}

	public boolean isExistButtonPriv(Map<String, Object> param) {
		return privlegeDAO.isExistButtonPriv(param);
	}

}
