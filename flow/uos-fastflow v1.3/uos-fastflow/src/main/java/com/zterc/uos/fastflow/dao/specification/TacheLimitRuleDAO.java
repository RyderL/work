package com.zterc.uos.fastflow.dao.specification;

import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.TacheLimitRuleDto;

public interface TacheLimitRuleDAO {

	public void addTacheLimitRule(TacheLimitRuleDto tacheLimitRuleDto);
	public void delTacheLimitRule(TacheLimitRuleDto tacheLimitRuleDto);
	public PageDto qryTacheLimitRuleByCond(Map<String,Object> params);
}
