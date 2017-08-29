package com.ztesoft.uosflow.dubbo.inf.client;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandResultDto;
import com.ztesoft.uosflow.dubbo.dto.client.DubboCreateWorkOrderDto;
import com.ztesoft.uosflow.dubbo.dto.client.DubboReportCalCondResultDto;
import com.ztesoft.uosflow.dubbo.dto.client.DubboReportProcessStateDto;
import com.ztesoft.uosflow.dubbo.dto.client.DubboReportTimeLimitDto;
import com.ztesoft.uosflow.dubbo.dto.result.DubboCreateWorkOrderResultDto;

public interface FlowDubboServiceInf {
	public abstract DubboCreateWorkOrderResultDto createWorkOrder(
			DubboCreateWorkOrderDto paramDubboCreateWorkOrderDto);

	public abstract DubboCommandResultDto reportProcessState(
			DubboReportProcessStateDto paramDubboReportProcessStateDto);
	
	public abstract DubboCommandResultDto reportCalCondResult(DubboReportCalCondResultDto dubboReportCalCondResultDto);
	
	public abstract DubboCommandResultDto reportTimeLimit(DubboReportTimeLimitDto dubboReportTimeLimitDto);
	
}
