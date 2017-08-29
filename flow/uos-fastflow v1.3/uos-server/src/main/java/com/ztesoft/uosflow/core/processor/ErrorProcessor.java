package com.ztesoft.uosflow.core.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.service.ExceptionService;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.inf.util.ServerJsonUtil;

public class ErrorProcessor extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(ErrorProcessor.class);

	private ExceptionService exceptionService;

	public void setExceptionService(ExceptionService exceptionService) {
		this.exceptionService = exceptionService;
	}

	public ErrorProcessor() {

	}

	public void init() {
		this.start();
	}

	@Override
	@SuppressWarnings("all")
	public void run() {
		while (true) {
			try {
				queryAndDeal();
			} catch (Exception e) {
				logger.error("异常处理失败!",e);
				try {
					Thread.currentThread().sleep(1000l);
				} catch (InterruptedException e1) {
					logger.error("此处休眠异常!",e);
				}
			}
		}
	}

	@Transactional
	@SuppressWarnings("all")
	public void queryAndDeal() throws InterruptedException {
		List<ExceptionDto> errors = exceptionService
				.queryExceptionsByState(ExceptionDto.ERROR_NOT_HANDLE);
		if (errors == null || errors.size() == 0) {
			Thread.currentThread().sleep(500l);
		} else {
			for (ExceptionDto error : errors) {
				dealError(error);
			}
		}
	}

	public void dealError(ExceptionDto error) {
		logger.debug("=========dealError:" + error.getId());

		// 解析命令
		try {
			CommandDto cmdDto = ServerJsonUtil.getCommandDtoFromJson(error
					.getMsg());

			CommandProxy.getInstance().updateProcessInstance(error);

			CommandResultDto resultDto = CommandProxy.getInstance()
					.dealCommand(cmdDto, error.getId(), true);

			if (resultDto.isDealFlag()) {
				CommandProxy.getInstance().updateException(error,
						ExceptionDto.ERROR_HANDLE_FINISHED);
			}
		} catch (Exception e) {
			CommandProxy.getInstance().updateException(error,
					ExceptionDto.ERROR_INIT);
		}
	}
}
