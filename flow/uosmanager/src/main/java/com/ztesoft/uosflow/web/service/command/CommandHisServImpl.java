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
import com.zterc.uos.fastflow.service.CommandQueueHisService;

@Transactional
@Service("CommandHisServ")
@Lazy
public class CommandHisServImpl implements CommandServ {
	private final Logger logger = LoggerFactory.getLogger(CommandHisServImpl.class);

	@Autowired
	private CommandQueueHisService commandQueueHisService;
	
	@Override
	public String qryUosCommandErrorsByCond(Map<String, Object> map)
			throws Exception {
		PageDto pageDto = commandQueueHisService.queryCommandByCond(map);
		String result = GsonHelper.toJson(pageDto);
		logger.debug("result:" + result);
		return result;
	}

}
