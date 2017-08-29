package com.zterc.uos.fastflow.service;

import java.util.Map;

import com.zterc.uos.fastflow.dao.specification.DispatchRuleDAO;
import com.zterc.uos.fastflow.dto.specification.DispatchRuleDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

/**
 * （定义）派发规则操作类
 * 
 * @author gong.yi
 *
 */
public class DispatchRuleService {
	private DispatchRuleDAO dispatchRuleDAO;

	public void setDispatchRuleDAO(DispatchRuleDAO dispatchRuleDAO) {
		this.dispatchRuleDAO = dispatchRuleDAO;
	}
	
	public PageDto qryDispatchRuleByCond(Map<String, Object> params){
		return dispatchRuleDAO.qryDispatchRuleByCond(params);
	}
	public DispatchRuleDto addDispatchRule(DispatchRuleDto dto){
		return dispatchRuleDAO.addDispatchRule(dto);
	}
	public void modDispatchRule(DispatchRuleDto dto){
		dispatchRuleDAO.modDispatchRule(dto);
	}
	public void delDispatchRule(Long id){
		dispatchRuleDAO.delDispatchRule(id);
	}
	public DispatchRuleDto[] qryDispatchRule(DispatchRuleDto dto){
		return dispatchRuleDAO.qryDispatchRule(dto);
	}
}
