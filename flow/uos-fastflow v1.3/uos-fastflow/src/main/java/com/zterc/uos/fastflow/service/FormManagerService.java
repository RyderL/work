package com.zterc.uos.fastflow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.fastflow.dao.specification.FormManagerDAO;
import com.zterc.uos.fastflow.dto.specification.*;

/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public class FormManagerService {
	
	private FormManagerDAO formManagerDAO;

	public void setFormManagerDAO(FormManagerDAO formManagerDAO) {
		this.formManagerDAO = formManagerDAO;
	}

	public List<ComboboxDto> qryCombox(Map<String, Object> params){
		return formManagerDAO.qryCombox(params);
	}

	public PageDto qryPageTemplate(Map<String, Object> params) {
		return formManagerDAO.qryPageTemplate(params);
	}

	public void addTemplate(PageTemplateDto templateDto){
		formManagerDAO.addTemplate(templateDto);
	}
	public void modTemplate(PageTemplateDto templateDto){
		formManagerDAO.modTemplate(templateDto);
	}
	public void delTemplate(PageTemplateDto templateDto){
		formManagerDAO.delTemplate((templateDto));
	}

	public PageDto qryTemplateDetail(Map<String, Object> params){
		return formManagerDAO.qryTemplateDetail(params);
	}
	public void addTemplateDetail(PageTemplateDetailDto templateDetailDto){
		formManagerDAO.addTemplateDetail(templateDetailDto);
	}

	public List<PageTemplateRuleDto> qryTemplateRule(Map<String, Object> params){
		return formManagerDAO.qryTemplateRule(params);
	}
	public void addTemplateRule(PageTemplateRuleDto templateRuleDto){
		formManagerDAO.addTemplateRule(templateRuleDto);
	}
	public void modTemplateRule(PageTemplateRuleDto templateRuleDto){
		formManagerDAO.modTemplateRule(templateRuleDto);
	}
	public void delTemplateRule(PageTemplateRuleDto templateRuleDto){
		formManagerDAO.delTemplateRule(templateRuleDto);
	}

	public PageDto qryPageConstraint(Map<String, Object> params){
		return formManagerDAO.qryPageConstraint(params);
	}
	public void addPageConstraint(PageConstraintDto pageConstraintDto){
		formManagerDAO.addPageConstraint(pageConstraintDto);
	}
	public void modPageConstraint(PageConstraintDto pageConstraintDto){
		formManagerDAO.modPageConstraint(pageConstraintDto);
	}
	public void delPageConstraint(PageConstraintDto pageConstraintDto){
		formManagerDAO.delPageConstraint(pageConstraintDto);
	}

	public List<PageElementDto> qryWidgetType(Map<String, Object> params){
		return formManagerDAO.qryWidgetType(params);
	}

	public List<PageTemplateRuleDto> qryPageTemplateRule(Map<String, Object> params){
		return formManagerDAO.qryPageTemplateRule(params);
	}
}
