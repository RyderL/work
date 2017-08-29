package com.zterc.uos.fastflow.dao.process;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public interface ExceptionDAO {

	public ExceptionDto queryException(Long id) ;
	
	public ExceptionDto createException(ExceptionDto exceptionDto);

	public void updateException(ExceptionDto exceptionDto);

	public List<ExceptionDto> queryExceptionsByState(String state);

	public PageDto queryExceptionsByCond(Map<String, Object> map);

	public List<ExceptionDto> queryExceptionsByPid(String processInstanceId);

	public void deleteByPid(String processInstanceId);
}
