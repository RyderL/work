package com.zterc.uos.fastflow.dao.specification;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.*;

/**
 * 
 * @author zheng.fengting 
 *
 * 2017Äê8ÔÂ16ÈÕ
 */
public interface FormManagerDAO {

	public List<ComboboxDto> qryCombox(Map<String, Object> params);
	public PageDto qryPageTemplate(Map<String, Object> params);
	public void addTemplate(PageTemplateDto templateDto);
	public void modTemplate(PageTemplateDto templateDto);
	public void delTemplate(PageTemplateDto templateDto);

	public PageDto qryTemplateDetail(Map<String, Object> params);

	public List<PageTemplateRuleDto> qryTemplateRule(Map<String, Object> params);
	public void addTemplateRule(PageTemplateRuleDto templateRuleDto);
	public void modTemplateRule(PageTemplateRuleDto templateRuleDto);
	public void delTemplateRule(PageTemplateRuleDto templateRuleDto);
	public List<PageTemplateRuleDto> qryPageTemplateRule(Map<String, Object> params);

	public PageDto qryPageConstraint(Map<String, Object> params);
	public void addPageConstraint(PageConstraintDto pageConstraintDto);
	public void modPageConstraint(PageConstraintDto pageConstraintDto);
	public void delPageConstraint(PageConstraintDto pageConstraintDto);

	public List<PageElementDto> qryWidgetType(Map<String, Object> params);
}
