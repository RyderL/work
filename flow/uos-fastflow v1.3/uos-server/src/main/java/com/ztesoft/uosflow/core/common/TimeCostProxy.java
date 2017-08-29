package com.ztesoft.uosflow.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.fastflow.dao.specification.CostTimeDAO;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.server.CostTimeDto;
import com.ztesoft.uosflow.core.util.CommandResultDtoUtil;

public class TimeCostProxy {

	private Logger logger = LoggerFactory.getLogger(TimeCostProxy.class);

	private CostTimeDAO costTimeDAO;

	public CostTimeDAO getCostTimeDAO() {
		return costTimeDAO;
	}

	public void setCostTimeDAO(CostTimeDAO costTimeDAO) {
		this.costTimeDAO = costTimeDAO;
	}

	public TimeCostProxy() {
	}

	public static TimeCostProxy getInstance() {
		return (TimeCostProxy) ApplicationContextProxy.getBean("timeCostProxy");
	}

	public CommandResultDto costTime(CommandDto commandDto) {
		CommandResultDto retDto = null;
		CostTimeDto ctd = (CostTimeDto) commandDto;
		try {
			/** ��������ʵ�� */
			// System.out.println(" cost " + ctd.getProcessInstanceId());
			costTimeDAO.saveCostTime(ctd.getProcessInstanceId(),ctd.getFlowCommand(),ctd.getCostTime());
			logger.debug("processId is " + ctd.getProcessInstanceId() + " cmd is " + ctd.getFlowCommand() + " ctime is  " + ctd.getCostTime());
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					true, "�����������ĳɹ�", ctd.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("������������, �쳣��Ϣ��" + e, e);
			retDto = CommandResultDtoUtil.createCommandResultDto(commandDto,
					false, "�������������쳣��", "-1");
		}
		return retDto;
	}

}
