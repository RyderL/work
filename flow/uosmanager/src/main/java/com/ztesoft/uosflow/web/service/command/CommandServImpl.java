package com.ztesoft.uosflow.web.service.command;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.service.CommandQueueService;

@Transactional
@Service("CommandServ")
@Lazy
public class CommandServImpl implements CommandServ {
	private final Logger logger = LoggerFactory.getLogger(CommandServImpl.class);

	@Autowired
	private CommandQueueService commandQueueService;
	
	/*@Autowired
	private WorkFlowServerInf workFlowServerInf;*/
	
	@Override
	public String qryUosCommandErrorsByCond(Map<String, Object> map)
			throws Exception {
		PageDto pageDto = commandQueueService.queryCommandByCond(map);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

	/*@Override
	public String dealException(Map<String, Object> paramMap) {
		logger.info("----�����쳣�����������paramMap:" + GsonHelper.toJson(paramMap));
		String result = "{\"isSuccess\":false}";
		try {
			Long errorId = LongHelper.valueOf(paramMap.get("errorId"));
			boolean flag = workFlowServerInf.dealException(errorId);
			if(flag){
				result = "{\"isSuccess\":true}";
			}
		} catch (Exception e) {
			logger.error("----�쳣�����쳣���쳣ԭ��" + e.getMessage(), e);
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
			logger.error("----�쳣�����쳣���쳣ԭ��" + e.getMessage(), e);
			result = "{\"isSuccess\":false}";
		}
		return result;
	}
*/
}
