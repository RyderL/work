package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.List;

import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.specification.CommandCfgDAO;
import com.zterc.uos.fastflow.dto.specification.CommandCfgDto;

public class CommandCfgDAOImpl extends AbstractDAOImpl implements CommandCfgDAO {

	private static final String QUERY_COMMAND_CFG="SELECT * FROM "+FastflowConfig.commandCfgTableName+" WHERE ROUTE_ID =1";
	@Override
	public List<CommandCfgDto> qryComandCfgs() {
		return queryList(CommandCfgDto.class, QUERY_COMMAND_CFG, new Object[]{});
	}

//	private static final String BY_QUEUE_NAME=" BY QUEUE_NAME = ?";
//	@Override
//	public CommandCfgDto qryComandCfgByQueueName(String queueName) {
//		return queryObject(CommandCfgDto.class, QUERY_COMMAND_CFG + BY_QUEUE_NAME, queueName);
//	}
}
