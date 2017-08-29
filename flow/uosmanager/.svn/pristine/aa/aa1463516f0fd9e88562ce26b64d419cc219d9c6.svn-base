package com.ztesoft.uosflow.web.service.timelimit;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.TacheLimitDto;
import com.zterc.uos.fastflow.dto.specification.TacheLimitRuleDto;
import com.zterc.uos.fastflow.service.TacheLimitService;


@Service("TacheLimitServ")
public class TacheLimitServImpl implements TacheLimitServ {
	private static Logger logger = Logger.getLogger(TacheLimitServImpl.class);
	
	@Autowired
	private TacheLimitService tacheLimitService;

	@Override
	public String qryTacheLimitByTache(Map<String, Object> map) {
		PageDto pageDto = tacheLimitService.qryTacheLimitByCond(map);
		String result = GsonHelper.toJson(pageDto);
		return result;
	}

	@Transactional
	@Override
	public String addTacheLimit(Map<String, Object> map) throws Exception {
		try {
			Long tacheId = MapUtils.getLong(map, "tacheId");
			Long limitValue = MapUtils.getLong(map, "limitValue");
			Long alertValue = MapUtils.getLong(map, "alertValue");
			Long areaId = MapUtils.getLong(map, "areaId");
			String isWorkTime = MapUtils.getString(map, "isWorkTime");
			String timeUnit = MapUtils.getString(map, "timeUnit");
			TacheLimitDto tacheLimitDto = new TacheLimitDto();
			tacheLimitDto.setAlertValue(alertValue);
			tacheLimitDto.setAreaId(areaId);
			tacheLimitDto.setIsWorkTime(isWorkTime);
			tacheLimitDto.setLimitValue(limitValue);
			tacheLimitDto.setTacheId(tacheId);
			tacheLimitDto.setTimeUnit(timeUnit);
			tacheLimitService.addTacheLimit(tacheLimitDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----添加环节时限异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

	@Transactional
	@Override
	public String modTacheLimit(Map<String, Object> map) throws Exception {
		try {
			Long tacheId = MapUtils.getLong(map, "tacheId");
			Long id = MapUtils.getLong(map, "id");
			Long limitValue = MapUtils.getLong(map, "limitValue");
			Long alertValue = MapUtils.getLong(map, "alertValue");
			Long areaId = MapUtils.getLong(map, "areaId");
			String isWorkTime = MapUtils.getString(map, "isWorkTime");
			String timeUnit = MapUtils.getString(map, "timeUnit");
			TacheLimitDto tacheLimitDto = new TacheLimitDto();
			tacheLimitDto.setId(id);
			tacheLimitDto.setAlertValue(alertValue);
			tacheLimitDto.setAreaId(areaId);
			tacheLimitDto.setIsWorkTime(isWorkTime);
			tacheLimitDto.setLimitValue(limitValue);
			tacheLimitDto.setTacheId(tacheId);
			tacheLimitDto.setTimeUnit(timeUnit);
			tacheLimitService.modTacheLimit(tacheLimitDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----修改环节时限异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

	@Transactional
	@Override
	public String delTacheLimit(Map<String, Object> map) throws Exception {
		try {
			Long id = MapUtils.getLong(map, "id");
			TacheLimitDto tacheLimitDto = new TacheLimitDto();
			tacheLimitDto.setId(id);
			tacheLimitService.delTacheLimit(tacheLimitDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----删除环节时限异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

	@Transactional
	@Override
	public String addTacheLimitRule(Map<String, Object> map) throws Exception {
		try {
			Long tacheLimitId = MapUtils.getLong(map, "tacheLimitId");
			Long packageId = MapUtils.getLong(map, "packageId");
			TacheLimitRuleDto tacheLimitRuleDto = new TacheLimitRuleDto();
			tacheLimitRuleDto.setTacheLimitId(tacheLimitId);
			tacheLimitRuleDto.setPackageId(packageId);
			tacheLimitService.addTacheLimitRule(tacheLimitRuleDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----添加环节时限适用规则异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

	@Transactional
	@Override
	public String delTacheLimitRule(Map<String, Object> map) throws Exception {
		try {
			Long id = MapUtils.getLong(map, "id");
			TacheLimitRuleDto tacheLimitRuleDto = new TacheLimitRuleDto();
			tacheLimitRuleDto.setId(id);
			tacheLimitService.delTacheLimitRule(tacheLimitRuleDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----删除环节时限适用规则异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

	@Override
	public String qryTacheLimitRule(Map<String, Object> map) {
		PageDto pageDto = tacheLimitService.qryTacheLimitRuleByCond(map);
		String result = GsonHelper.toJson(pageDto);
		return result;
	}

}
