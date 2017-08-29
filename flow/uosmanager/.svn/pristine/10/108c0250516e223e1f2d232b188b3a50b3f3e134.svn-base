package com.ztesoft.uosflow.web.service.flow;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.dom4j.DocumentException;

public interface FlowInstServ {
	public String qryProcessInstanceForTrace(Map<String, Object> map);

	public String qryProcessDefineForTrace(Map<String, Object> map)
			throws SQLException, DocumentException, IOException;

	public String qryProcessInstanceByCond(Map<String, Object> map)
			throws Exception;

	public String qryWorkItemByCond(Map<String, Object> map) throws Exception;

	public String qryProcessTacheByCond(Map<String, Object> map)
			throws Exception;

	public String qryUndoActivityByCond(Map<String, Object> map)
			throws Exception;

	public String qryActivityInstance(Map<String, Object> map) throws Exception;

	public String qryCommandMsgInfoByPid(Map<String, Object> map) throws Exception;
	
	public String qryProcInstShadowForTrace(Map<String,Object> map);
	
	public String qryPackageDefinePath(Map<String, Object> map);
	
	public String qryAreaIdByProcessInstId(Map<String, Object> map);
}
