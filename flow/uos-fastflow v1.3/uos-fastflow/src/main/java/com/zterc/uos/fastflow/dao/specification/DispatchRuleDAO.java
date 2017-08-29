package com.zterc.uos.fastflow.dao.specification;

import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.DispatchRuleDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public interface DispatchRuleDAO {
	public PageDto qryDispatchRuleByCond(Map<String, Object> params);
	public DispatchRuleDto addDispatchRule(DispatchRuleDto dto);
	public void modDispatchRule(DispatchRuleDto dto);
	public void delDispatchRule(Long id);
	public DispatchRuleDto[] qryDispatchRule(DispatchRuleDto dto);
}
