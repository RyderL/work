package com.ztesoft.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.fastflow.dto.process.ExceptionDto;
import com.zterc.uos.fastflow.service.ExceptionService;
import com.ztesoft.uosflow.dubbo.inf.server.WorkFlowServerInf;

public class ErrorProcessor extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(ErrorProcessor.class);

	private ExceptionService exceptionService;
	private WorkFlowServerInf workFlowService;

	public void setWorkFlowService(WorkFlowServerInf workFlowService) {
		this.workFlowService = workFlowService;
	}

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
				e.printStackTrace();
				try {
					Thread.currentThread().sleep(1000l);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
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
			Thread.currentThread().sleep(1000l);
		} else {
			for (ExceptionDto error : errors) {
				dealError(error);
			}
		}
	}

	public void dealError(ExceptionDto error) {
		logger.debug("=========dealError:" + error.getId());
		workFlowService.dealException(error.getId());
	}
}
