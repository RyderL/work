package com.zterc.uos.fastflow.service;

import java.util.Date;
import java.util.Map;

import com.zterc.uos.fastflow.dao.specification.WorkTimeDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.WorkTimeDto;

public class WorkTimeService {

	private WorkTimeDAO workTimeDAO;

	public void setWorkTimeDAO(WorkTimeDAO workTimeDAO) {
		this.workTimeDAO = workTimeDAO;
	}

	public void addWorkTime(WorkTimeDto workTimeDto){
		workTimeDAO.addWorkTime(workTimeDto);
	}
	
	public void modWorkTime(WorkTimeDto workTimeDto){
		workTimeDAO.modWorkTime(workTimeDto);
	}
	
	public void delWorkTime(WorkTimeDto workTimeDto){
		workTimeDAO.delWorkTime(workTimeDto);
	}
	
	public PageDto qryWorkTimeByCond(Map<String,Object> params){
		return workTimeDAO.qryWorkTimeByCond(params);
	}

	public WorkTimeDto[] findActiveWorkTimes(Date inputDate) {
		WorkTimeDto[] workTimes = workTimeDAO.findActiveWorkTimes(inputDate);
		return workTimes;
	}
	
}
