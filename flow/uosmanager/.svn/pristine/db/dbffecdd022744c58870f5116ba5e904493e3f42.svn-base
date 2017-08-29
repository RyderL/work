package com.ztesoft.uosflow.web.service.timelimit;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.fastflow.dto.specification.FlowLimitDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.service.FlowLimitService;


@Service("FlowLimitServ")
public class FlowLimitServImpl implements FlowLimitServ {
	private static Logger logger = Logger.getLogger(FlowLimitServImpl.class);
	
	@Autowired
	private FlowLimitService flowLimitService;

	@Override
	public String qryFlowLimitByPackageDefine(Map<String, Object> map) {
		PageDto pageDto = flowLimitService.qryFlowLimitByCond(map);
		String result = GsonHelper.toJson(pageDto);
		return result;
	}

	@Transactional
	@Override
	public String addFlowLimit(Map<String, Object> map) throws Exception {
		try {
			Long packageId = MapUtils.getLong(map, "packageId");
			Long limitValue = MapUtils.getLong(map, "limitValue");
			Long alertValue = MapUtils.getLong(map, "alertValue");
			Long areaId = MapUtils.getLong(map, "areaId");
			String isWorkTime = MapUtils.getString(map, "isWorkTime");
			String timeUnit = MapUtils.getString(map, "timeUnit");
			FlowLimitDto flowLimitDto = new FlowLimitDto();
			flowLimitDto.setAlertValue(alertValue);
			flowLimitDto.setAreaId(areaId);
			flowLimitDto.setIsWorkTime(isWorkTime);
			flowLimitDto.setLimitValue(limitValue);
			flowLimitDto.setPackageId(packageId);
			flowLimitDto.setTimeUnit(timeUnit);
			flowLimitService.addFlowLimit(flowLimitDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----添加流程时限异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

	@Transactional
	@Override
	public String modFlowLimit(Map<String, Object> map) throws Exception {
		try {
			Long id = MapUtils.getLong(map, "id");
			Long packageId = MapUtils.getLong(map, "packageId");
			Long limitValue = MapUtils.getLong(map, "limitValue");
			Long alertValue = MapUtils.getLong(map, "alertValue");
			Long areaId = MapUtils.getLong(map, "areaId");
			String isWorkTime = MapUtils.getString(map, "isWorkTime");
			String timeUnit = MapUtils.getString(map, "timeUnit");
			FlowLimitDto flowLimitDto = new FlowLimitDto();
			flowLimitDto.setId(id);
			flowLimitDto.setAlertValue(alertValue);
			flowLimitDto.setAreaId(areaId);
			flowLimitDto.setIsWorkTime(isWorkTime);
			flowLimitDto.setLimitValue(limitValue);
			flowLimitDto.setPackageId(packageId);
			flowLimitDto.setTimeUnit(timeUnit);
			flowLimitService.modFlowLimit(flowLimitDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----修改流程时限异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

	@Transactional
	@Override
	public String delFlowLimit(Map<String, Object> map) throws Exception {
		try {
			Long id = MapUtils.getLong(map, "id");
			FlowLimitDto flowLimitDto = new FlowLimitDto();
			flowLimitDto.setId(id);
			flowLimitService.delFlowLimit(flowLimitDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----删除流程时限异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

}
