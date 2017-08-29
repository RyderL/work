package com.ztesoft.uosflow.inf.persist.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.zterc.uos.fastflow.jdbc.sqlHolder.AsynSqlExecBy3thParamDto;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.dto.CommandDto;
import com.ztesoft.uosflow.core.dto.CommandResultDto;
import com.ztesoft.uosflow.core.dto.client.ExecuteSQLAsynBy3thDto;
import com.ztesoft.uosflow.dubbo.dto.client.DubboAsynSqlExecBy3thDto;
import com.ztesoft.uosflow.dubbo.inf.client.FlowDubboSqlExecBy3thInf;

@Component("persistBy3thClientProxy")
public class PersistBy3thClientProxy {

	private PersistBy3thClientProxy() {
	}

	public static PersistBy3thClientProxy getInstance() {
		return (PersistBy3thClientProxy) ApplicationContextProxy.getBean("persistBy3thClientProxy");
	}

	/**
	 * 
	 * 第三方异步持久化dubbo调用
	 * @param commandDto
	 * @param service
	 * @return   
	 * @author bobping
	 * @date 2017年4月6日
	 */
	public CommandResultDto executeSQLAsynBy3th(CommandDto commandDto) {
		ExecuteSQLAsynBy3thDto executeSQLAsynBy3th = (ExecuteSQLAsynBy3thDto) commandDto;
		List<DubboAsynSqlExecBy3thDto> params = new ArrayList<>();
		FlowDubboSqlExecBy3thInf service = (FlowDubboSqlExecBy3thInf) ApplicationContextProxy.getBean("persistBy3thSevice");
		//转dubboDto
		for(AsynSqlExecBy3thParamDto param :executeSQLAsynBy3th.getParam()){
			DubboAsynSqlExecBy3thDto dubboParam = new DubboAsynSqlExecBy3thDto(param.getSqlStr(), param.getKey(),
					param.getParam(), param.getSqlType(), param.getTableName(), param.getSqlSeq());
			params.add(dubboParam);
		}
		service.executeSQLAsynBy3th(params);
		return null;
	}

}
