package com.zterc.uos.fastflow.service;

import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.dao.process.ExceptionDAO;
import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

/**
 * （实例）异常信息操作类型
 * 
 * @author Administrator
 *
 */
public class ExceptionService {
	private ExceptionDAO exceptionDAO;

	public void setExceptionDAO(ExceptionDAO exceptionDAO) {
		this.exceptionDAO = exceptionDAO;
	}

	public ExceptionDto queryException(Long id) {
		return exceptionDAO.queryException(id);
	}

	public ExceptionDto createException(ExceptionDto exceptionDto) {
		if(exceptionDto.getProcessInstanceId() != null){
			exceptionDto.setId(SequenceHelper.getIdWithSeed("UOS_EXCEPTION",StringHelper.valueOf(exceptionDto.getProcessInstanceId())));
		}else{
			exceptionDto.setId(SequenceHelper.getId("UOS_EXCEPTION"));
		}
		return exceptionDAO.createException(exceptionDto);
	}

	public void updateException(ExceptionDto exceptionDto) {
		exceptionDAO.updateException(exceptionDto);
	}

	public List<ExceptionDto> queryExceptionsByState(String state) {
		return exceptionDAO.queryExceptionsByState(state);
	}

	public PageDto queryExceptionsByCond(Map<String, Object> map) {
		return exceptionDAO.queryExceptionsByCond(map);
	}

	public List<ExceptionDto> queryExceptionsByPid(String processInstanceId) {
		return exceptionDAO.queryExceptionsByPid(processInstanceId);
	}

	public void deleteByPid(String processInstanceId) {
		exceptionDAO.deleteByPid(processInstanceId);
	}
}
