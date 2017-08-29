package com.zterc.uos.fastflow.dao.process.his;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public interface ExceptionHisDAO {

	public ExceptionDto queryExceptionHis(Long id) ;
	
	public ExceptionDto createExceptionHis(ExceptionDto exceptionDto);

	public List<ExceptionDto> queryExceptionsHisByState(String state);

	public PageDto queryExceptionsHisByCond(Map<String, Object> map);
}
