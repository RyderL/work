package com.ztesoft.uosflow.jmx.server.bl.manager;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.service.ProcessDefinitionService;
import com.zterc.uos.fastflow.service.ProcessPackageService;
import com.zterc.uos.fastflow.service.ProcessParamDefService;
import com.zterc.uos.fastflow.service.ReturnReasonService;
import com.zterc.uos.fastflow.service.TacheService;
import com.ztesoft.uosflow.core.common.CommandProxy;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.client.CreateWorkOrderDto;
import com.ztesoft.uosflow.dubbo.inf.manager.FlowManagerInf;
import com.ztesoft.uosflow.inf.util.ServerJsonUtil;

public class FlowManagerImpl implements FlowManagerInf{

	@Autowired
	private TacheService tacheService;
	@Autowired
	private ProcessParamDefService processParamDefService;
	@Autowired
	private ProcessDefinitionService processDefinitionService;
	@Autowired
	private ProcessPackageService processPackageService;
	@Autowired
	private ReturnReasonService returnReasonService;

	@Override
	public void refreshProcessDefineCache() {
		processDefinitionService.loadAllProcessDefinition();
	}

	@Override
	public void refreshTacheDefCache() {
		tacheService.loadAllTache();
	}

	@Override
	public void refreshReturnReasonConfigCache() {
		returnReasonService.loadAllReturnReasonConfig();
	}

	@Override
	public void refreshProcessParamDefCache() {
		processParamDefService.loadAllProcessParamDef();
	}

	@Override
	public void reCreateWorkOrder(String paramJson) {
		Map<String,Object> paramsMap = GsonHelper.toMap(paramJson);
		CreateWorkOrderDto commandDto = new CreateWorkOrderDto();
		commandDto.init(paramsMap);
		CommandProxy.getInstance().dealCommand(commandDto);
	}

	@Override
	public void refreshProcessPackageCache() {
		processPackageService.loadAllProcessPackage();
	}

	@Override
	public void reExcuteCommand(String paramJson) {
		Map<String,Object> paramsMap = GsonHelper.toMap(paramJson);
		String commandCode = StringHelper.valueOf(paramsMap.get(InfConstant.INF_COMMAND_CODE));
		CommandDto commandDto = ServerJsonUtil.getCommandDtoFromJson(commandCode,paramsMap);
		if(commandDto != null){
			commandDto.init(paramsMap);
			CommandProxy.getInstance().dealCommand(commandDto);
		}
	}

}
