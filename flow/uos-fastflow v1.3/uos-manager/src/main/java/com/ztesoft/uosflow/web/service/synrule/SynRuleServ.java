package com.ztesoft.uosflow.web.service.synrule;

import java.util.Map;

/**
 * ͬ�����ýӿ�
 */
public interface SynRuleServ {
	public String qrySynRuleByCond(Map<String, Object> map) throws Exception;
	public String addSynRule(Map<String, Object> map) throws Exception;
	public String modSynRule(Map<String, Object> map) throws Exception;
}
