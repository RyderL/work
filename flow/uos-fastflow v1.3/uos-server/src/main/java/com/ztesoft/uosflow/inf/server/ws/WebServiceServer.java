package com.ztesoft.uosflow.inf.server.ws;

import com.ztesoft.uosflow.inf.server.common.ServerProxy;


/**
 * 
 * 流程平台的WebService服务实现类模拟
 * @author gong.yi
 *
 */
public class WebServiceServer {
	
	/**
	 * 模拟Server端的WebService接收实现方法
	 * 需要详细的交互协议设计：暂时参考文档-----CRM数据域与流程接口协议V1.3.docx
	 * @param jsonParams 统一json格式的入参
	 * @return 统一返回json格式的字符串
	 * @throws java.rmi.RemoteException
	 */
	public java.lang.String methodInvoke(java.lang.String jsonParams) throws java.rmi.RemoteException {
		return ServerProxy.getInstance().dealForJson(jsonParams);
	}
}
