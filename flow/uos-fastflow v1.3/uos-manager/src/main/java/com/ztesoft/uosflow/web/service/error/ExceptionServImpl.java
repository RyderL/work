package com.ztesoft.uosflow.web.service.error;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.service.ExceptionService;
import com.ztesoft.uosflow.dubbo.inf.server.WorkFlowServerInf;

@Transactional
@Service("ExceptionServ")
@Lazy
public class ExceptionServImpl implements ExceptionServ {
	private final Logger logger = LoggerFactory.getLogger(ExceptionServImpl.class);

	@Autowired
	private ExceptionService exceptionService;
	
	@Autowired
	private WorkFlowServerInf workFlowServerInf;
	
	@Override
	public String qryUosFlowErrorsByCond(Map<String, Object> map)
			throws Exception {
		PageDto pageDto = exceptionService.queryExceptionsByCond(map);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

	@Override
	public String dealException(Map<String, Object> paramMap) {
		logger.info("----进入异常处理方法，入参paramMap:" + GsonHelper.toJson(paramMap));
		String result = "{\"isSuccess\":false}";
		try {
			Long errorId = LongHelper.valueOf(paramMap.get("errorId"));
			boolean flag = workFlowServerInf.dealException(errorId);
			if(flag){
				result = "{\"isSuccess\":true}";
			}
		} catch (Exception e) {
			logger.error("----异常处理异常，异常原因：" + e.getMessage(), e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String dealExceptions(Map<String, Object> paramMap) {
		String result = "{\"isSuccess\":true}";
		try {
			List<String> errorIds = (List<String>) paramMap.get("errorIds");
			if (errorIds != null && errorIds.size() > 0) {
				for (int i = 0; i < errorIds.size(); i++) {
					Long errorId = LongHelper.valueOf(errorIds.get(i));
					workFlowServerInf.dealException(errorId);
				}
			}
		} catch (Exception e) {
			logger.error("----异常处理异常，异常原因：" + e.getMessage(), e);
			result = "{\"isSuccess\":false}";
		}
		return result;
	}

}
