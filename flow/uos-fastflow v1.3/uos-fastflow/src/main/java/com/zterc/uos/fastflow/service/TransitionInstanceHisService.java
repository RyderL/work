package com.zterc.uos.fastflow.service;

import com.zterc.uos.fastflow.dao.process.his.TransitionInstanceHisDAO;
import com.zterc.uos.fastflow.dto.process.TracerInstanceDto;

/**
 * （实例）转移实例操作类
 * 
 * @author gong.yi
 *
 */
public class TransitionInstanceHisService {

	private TransitionInstanceHisDAO transitionInstanceHisDAO;
	
	public TransitionInstanceHisDAO getTransitionInstanceHisDAO() {
		return transitionInstanceHisDAO;
	}

	public void setTransitionInstanceHisDAO(
			TransitionInstanceHisDAO transitionInstanceHisDAO) {
		this.transitionInstanceHisDAO = transitionInstanceHisDAO;
	}

	/**
	 * （是否需要内存模式获取？？？？）流程实例xml查找和组装
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public TracerInstanceDto findTransitionInstancesByPid(
			String processInstanceId) {
		return transitionInstanceHisDAO
				.findTransitionInstancesByPid(processInstanceId);
	}

}
