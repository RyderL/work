package com.zterc.uos.fastflow.service;

import java.util.Map;

import com.zterc.uos.fastflow.dao.specification.TacheLimitDAO;
import com.zterc.uos.fastflow.dao.specification.TacheLimitRuleDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.TacheLimitDto;
import com.zterc.uos.fastflow.dto.specification.TacheLimitRuleDto;

public class TacheLimitService {
	
	private TacheLimitDAO tacheLimitDAO;
	private TacheLimitRuleDAO tacheLimitRuleDAO;
	public void setTacheLimitDAO(TacheLimitDAO tacheLimitDAO) {
		this.tacheLimitDAO = tacheLimitDAO;
	}
	public void setTacheLimitRuleDAO(TacheLimitRuleDAO tacheLimitRuleDAO) {
		this.tacheLimitRuleDAO = tacheLimitRuleDAO;
	}
	
	public void addTacheLimit(TacheLimitDto tacheLimitDto){
		tacheLimitDAO.addTacheLimit(tacheLimitDto);
	}
	public void modTacheLimit(TacheLimitDto tacheLimitDto){
		tacheLimitDAO.modTacheLimit(tacheLimitDto);
	}
	public void delTacheLimit(TacheLimitDto tacheLimitDto){
		tacheLimitDAO.delTacheLimit(tacheLimitDto);
	}
	public PageDto qryTacheLimitByCond(Map<String,Object> params){
		return tacheLimitDAO.qryTacheLimitByCond(params);
	}
	public void addTacheLimitRule(TacheLimitRuleDto tacheLimitRuleDto){
		tacheLimitRuleDAO.addTacheLimitRule(tacheLimitRuleDto);
	}
	public void delTacheLimitRule(TacheLimitRuleDto tacheLimitRuleDto){
		tacheLimitRuleDAO.delTacheLimitRule(tacheLimitRuleDto);
	}
	public PageDto qryTacheLimitRuleByCond(Map<String,Object> params){
		return tacheLimitRuleDAO.qryTacheLimitRuleByCond(params);
	}
	public TacheLimitDto qryTacheLimitByByTAP(Long tacheId, String areaId,
			String processDefineId) {
		return tacheLimitDAO.qryTacheLimitByByTAP(tacheId,areaId,processDefineId);
	}
}
