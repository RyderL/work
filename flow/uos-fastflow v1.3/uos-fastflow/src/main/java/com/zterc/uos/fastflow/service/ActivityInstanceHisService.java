package com.zterc.uos.fastflow.service;

import com.zterc.uos.fastflow.dao.process.his.ActivityInstanceHisDAO;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;

/**
 * ��ʵ�����ʵ��������
 * 
 * @author gong.yi
 *
 */
public class ActivityInstanceHisService {
	
	private ActivityInstanceHisDAO activityInstanceHisDAO;


	public void setActivityInstanceHisDAO(
			ActivityInstanceHisDAO activityInstanceHisDAO) {
		this.activityInstanceHisDAO = activityInstanceHisDAO;
	}


	/**
	 * ��Server�����ݻʵ��id��ѯ�ʵ��
	 * 
	 * @param activityInstanceId
	 * @return ActivityInstanceDto
	 */
	public ActivityInstanceDto queryActivityInstance(String activityInstanceId) {

		return activityInstanceHisDAO.queryActivityInstanceHis(activityInstanceId);
	}

}
