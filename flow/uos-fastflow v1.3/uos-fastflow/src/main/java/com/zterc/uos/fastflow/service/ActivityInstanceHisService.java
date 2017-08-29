package com.zterc.uos.fastflow.service;

import com.zterc.uos.fastflow.dao.process.his.ActivityInstanceHisDAO;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;

/**
 * （实例）活动实例操作类
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
	 * （Server）根据活动实例id查询活动实例
	 * 
	 * @param activityInstanceId
	 * @return ActivityInstanceDto
	 */
	public ActivityInstanceDto queryActivityInstance(String activityInstanceId) {

		return activityInstanceHisDAO.queryActivityInstanceHis(activityInstanceId);
	}

}
