package com.ztesoft.uosflow.web.service.error;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.service.ExceptionHisService;

@Transactional
@Service("ExceptionHisServ")
public class ExceptionHisServImpl implements ExceptionHisServ {
	private final Logger logger = LoggerFactory.getLogger(ExceptionHisServImpl.class);

	@Autowired
	private ExceptionHisService exceptionHisService;
	
	@Override
	public String qryUosFlowErrorsByCond(Map<String, Object> map)
			throws Exception {
		PageDto pageDto = exceptionHisService.queryExceptionsByCond(map);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

}
