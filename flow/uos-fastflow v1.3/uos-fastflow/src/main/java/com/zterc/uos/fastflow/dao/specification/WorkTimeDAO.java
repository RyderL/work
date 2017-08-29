package com.zterc.uos.fastflow.dao.specification;

import java.util.Date;
import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.WorkTimeDto;

public interface WorkTimeDAO {
	
	public void addWorkTime(WorkTimeDto workTimeDto);
	
	public void modWorkTime(WorkTimeDto workTimeDto);
	
	public void delWorkTime(WorkTimeDto workTimeDto);
	
	public WorkTimeDto qryWorkTimeById(Long id);

	public PageDto qryWorkTimeByCond(Map<String,Object> params);

	public WorkTimeDto[] findActiveWorkTimes(Date inputDate);
}
