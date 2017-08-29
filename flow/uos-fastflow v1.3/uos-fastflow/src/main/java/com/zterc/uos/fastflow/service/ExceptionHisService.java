package com.zterc.uos.fastflow.service;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dao.process.his.ExceptionHisDAO;
import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

/**
 * （实例）异常信息操作类型
 * 
 * @author Administrator
 *
 */
public class ExceptionHisService {
	private ExceptionHisDAO exceptionHisDAO;

	public ExceptionHisDAO getExceptionHisDAO() {
		return exceptionHisDAO;
	}

	public void setExceptionHisDAO(ExceptionHisDAO exceptionHisDAO) {
		this.exceptionHisDAO = exceptionHisDAO;
	}

	public List<ExceptionDto> queryExceptionsByState(String state) {
		return exceptionHisDAO.queryExceptionsHisByState(state);
	}

	public PageDto queryExceptionsByCond(Map<String, Object> map) {
		return exceptionHisDAO.queryExceptionsHisByCond(map);
	}
}
