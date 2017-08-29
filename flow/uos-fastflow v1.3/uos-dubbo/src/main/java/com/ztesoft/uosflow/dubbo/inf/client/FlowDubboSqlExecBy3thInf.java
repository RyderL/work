package com.ztesoft.uosflow.dubbo.inf.client;

import java.util.List;

import com.ztesoft.uosflow.dubbo.dto.client.DubboAsynSqlExecBy3thDto;

/**
 * 第三方执行sql的dubbo接口
 * @author bobping
 *
 */
public interface FlowDubboSqlExecBy3thInf {

		/**
	 * 
	 * 第三方数据同步
	 * @param param
	 * @return   
	 * @author bobping
	 * @date 2017年4月6日
	 */
	public abstract boolean executeSQLAsynBy3th(List<DubboAsynSqlExecBy3thDto> param);
	
}
