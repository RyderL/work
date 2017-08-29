package com.ztesoft.uosflow.web.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.fastflow.dto.specification.StaffDto;
import com.zterc.uos.fastflow.service.StaffService;


/**
 * 
 * 用户service实现
 * @author gong.yi
 *
 */
@Service("StaffServ")
public class StaffServImpl implements StaffServ{

	@Autowired
	private StaffService staffService;
	
	@Override
    @Transactional 
	public int insertStaff(StaffDto staff) {
		return staffService.insertStaff(staff);
	}

	@Override
	public StaffDto queryStaffByUsername(String username) {
		return staffService.queryStaffByUsername(username);
	}

	@Override
	public boolean isIpOk(String ip) {
//		return staffDao.isIpOk(ip);
		return true;
	}


}
