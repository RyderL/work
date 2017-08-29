package com.zterc.uos.fastflow.dao.specification;

import com.zterc.uos.fastflow.dto.specification.StaffDto;


/**
 * 
 * 用户表Dao接口
 * @author gong.yi
 *
 */
public interface StaffDAO {
	/**
	 * 添加用户
	 * @param staff
	 * @return
	 */
	public int insertStaff(StaffDto staff);
	
	/**
	 * 根据用户名查找人员信息
	 * @param username
	 * @return
	 */
	public StaffDto queryStaffByUsername(String username);

	/**
	 * 判断当前ip是否存在在uos_ip表中
	 * @param ip
	 * @return
	 */
	public boolean isIpOk(String ip);
}
