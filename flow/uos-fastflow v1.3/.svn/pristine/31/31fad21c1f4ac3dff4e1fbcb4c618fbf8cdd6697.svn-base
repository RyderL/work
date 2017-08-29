/**
 * WorkFlowServicePortBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ztesoft.uosflow.inf.server.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ztesoft.uosflow.inf.server.common.ServerProxy;

public class WorkFlowServicePortBindingImpl implements WorkFlowService{
	private Logger logger = LoggerFactory.getLogger(WorkFlowServicePortBindingImpl.class);
	
	public java.lang.String methodInvoke(java.lang.String jsonParams) throws java.rmi.RemoteException {
    	logger.debug("recieve message =============>>"+jsonParams);
		return ServerProxy.getInstance().dealForJson(jsonParams);
	}
}
