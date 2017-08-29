package com.zterc.uos.fastflow.dao.specification;

import com.zterc.uos.fastflow.dto.specification.StaffDto;


/**
 * 
 * �û���Dao�ӿ�
 * @author gong.yi
 *
 */
public interface StaffDAO {
	/**
	 * ����û�
	 * @param staff
	 * @return
	 */
	public int insertStaff(StaffDto staff);
	
	/**
	 * �����û���������Ա��Ϣ
	 * @param username
	 * @return
	 */
	public StaffDto queryStaffByUsername(String username);

	/**
	 * �жϵ�ǰip�Ƿ������uos_ip����
	 * @param ip
	 * @return
	 */
	public boolean isIpOk(String ip);
}
