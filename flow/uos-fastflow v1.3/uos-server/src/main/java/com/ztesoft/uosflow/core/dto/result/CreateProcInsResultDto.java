package com.ztesoft.uosflow.core.dto.result;

import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandResultDto;



/**
 * 创建流程服务返回结果对象
 * @author Administrator
 *
 */
public class CreateProcInsResultDto extends CommandResultDto {

	private static final long serialVersionUID = 1L;


	@SuppressWarnings("unchecked")
	@Override
	public void init (Map<String, Object> paramsMap){
		super.init(paramsMap);
		this.processInstanceId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_PROCESSINSTANCEID));
		this.flowPassList = (Map<String, String>)paramsMap.get(InfConstant.INF_FLOW_PASS_LIST);
		this.flowParamList = (Map<String, String>)paramsMap.get(InfConstant.INF_FLOW_PARAM_LIST);
	}
}
