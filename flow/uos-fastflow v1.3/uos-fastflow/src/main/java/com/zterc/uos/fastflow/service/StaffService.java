package com.zterc.uos.fastflow.service;

import com.zterc.uos.fastflow.dao.specification.StaffDAO;
import com.zterc.uos.fastflow.dto.specification.StaffDto;

/**
 * （定义）人员操作类
 * 
 * @author gong.yi
 *
 */
public class StaffService {

	private StaffDAO staffDAO;
	
	public void setStaffDAO(StaffDAO staffDAO) {
		this.staffDAO = staffDAO;
	}

	public int insertStaff(StaffDto staff){
		return staffDAO.insertStaff(staff);
	}
	
	public StaffDto queryStaffByUsername(String username){
		return staffDAO.queryStaffByUsername(username);
	}
}
