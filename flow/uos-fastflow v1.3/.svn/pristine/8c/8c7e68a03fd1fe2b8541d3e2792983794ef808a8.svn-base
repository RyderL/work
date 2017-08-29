package com.zterc.uos.fastflow.service;

import java.util.List;

import com.zterc.uos.fastflow.dao.specification.CommandCfgDAO;
import com.zterc.uos.fastflow.dto.specification.CommandCfgDto;

/**
 * （定义）命令配置操作类
 * 
 * @author gong.yi
 * 
 */
public class CommandCfgService {
	private CommandCfgDAO commandCfgDAO;

	public void setCommandCfgDAO(CommandCfgDAO commandCfgDAO) {
		this.commandCfgDAO = commandCfgDAO;
	}

	/**
	 * 获取所有的命令配置列表
	 * 
	 * @return
	 */
	public List<CommandCfgDto> qryComandCfgs() {
		return commandCfgDAO.qryComandCfgs();
	}
}
