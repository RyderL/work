package com.ztesoft.uosflow.web.service.formmanager;

import java.sql.SQLException;
import java.util.Map;
/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public interface FormManagerServ {
	
	public String qryCombox(Map<String, Object> map) throws SQLException;

	public String qryTemplate(Map<String, Object> map) throws SQLException;
	public String addTemplate(Map<String, Object> map) throws SQLException;
	public String modTemplate(Map<String, Object> map) throws SQLException;
	public String delTemplate(Map<String, Object> map) throws SQLException;

	public String qryTemplateDetail(Map<String, Object> map) throws SQLException;

	public String qryTemplateRule(Map<String, Object> map) throws SQLException;
	public String addTemplateRule(Map<String, Object> mpa) throws SQLException;
	public String modTemplateRule(Map<String, Object> map) throws SQLException;
	public String delTemplateRule(Map<String, Object> map) throws SQLException;

	public String qryPageConstraint(Map<String, Object> map) throws SQLException;
	public String addPageConstraint(Map<String, Object> map) throws SQLException;
	public String modPageConstraint(Map<String, Object> map) throws SQLException;
	public String delPageConstraint(Map<String, Object> map) throws SQLException;

	public String qryWidgetType(Map<String, Object> map)throws SQLException;

	public String getOptiondataByElementCode(Map<String, Object> map)throws SQLException;

	public String qryPageTemplateRule(Map<String, Object> map)throws SQLException;
}
