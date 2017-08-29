package com.zterc.uos.fastflow.service;

import java.util.List;

import com.zterc.uos.fastflow.dao.specification.CommandCfgDAO;
import com.zterc.uos.fastflow.dto.specification.CommandCfgDto;

/**
 * �����壩�������ò�����
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
	 * ��ȡ���е����������б�
	 * 
	 * @return
	 */
	public List<CommandCfgDto> qryComandCfgs() {
		return commandCfgDAO.qryComandCfgs();
	}
}
