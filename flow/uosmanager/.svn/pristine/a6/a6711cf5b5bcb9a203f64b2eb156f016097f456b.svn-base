package com.ztesoft.uosflow.web.service.timelimit;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.WorkTimeDto;
import com.zterc.uos.fastflow.service.AreaService;
import com.zterc.uos.fastflow.service.WorkTimeService;


@Service("WorkTimeServ")
public class WorkTimeServImpl implements WorkTimeServ {
	private static Logger logger = Logger.getLogger(WorkTimeServImpl.class);
	@Autowired
	private AreaService areaService; 
	@Autowired
	private WorkTimeService workTimeService;

	@Override
	public String qryWorkTimeByAreaId(Map<String, Object> map) throws Exception {
		try {
			Long areaId = LongHelper.valueOf(map.get("areaId"));
			if (areaId != null && areaId != -1 && areaId != 1) {
				AreaDto area = areaService.findAreaByAreaId(areaId);
				AreaDto[] areaDtos = areaService
						.findAreasByPathCode(area.getPathCode());
				StringBuffer areaIds = new StringBuffer();
				for (int i = 0; i < areaDtos.length; i++) {
					if (i != areaDtos.length - 1) {
						areaIds.append(areaDtos[i].getAreaId() + ",");
					} else {
						areaIds.append(areaDtos[i].getAreaId());
					}
				}
				map.put("areaIds", areaIds);
			}
			PageDto pageDto = workTimeService.qryWorkTimeByCond(map);
			String result = GsonHelper.toJson(pageDto);
			return result;
		} catch (Exception e) {
			logger.error("---WorkTimeServ qryWorkTimeByAreaId方法执行异常，异常信息:"+e.getMessage(),e);
			throw e;
		}
	}

	@Transactional
	@Override
	public String delWorkTime(Map<String, Object> map) throws Exception {
		try {
			Long workTimeId = MapUtils.getLong(map, "id");
			WorkTimeDto workTimeDto = new WorkTimeDto();
			workTimeDto.setId(workTimeId);
			workTimeDto.setStateDate(new Date());
			workTimeService.delWorkTime(workTimeDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----删除工作时间异常，异常信息：" + e.getMessage(),e);
			throw e;
		}
	}

	@Transactional
	@Override
	public String addWorkTime(Map<String, Object> map) throws Exception {
		try {
			String workTimeName = MapUtils.getString(map, "workTimeName");
			String workTimeRule = MapUtils.getString(map, "workTimeRule");
			String effDate = MapUtils.getString(map, "effDate");
			String expDate = MapUtils.getString(map, "expDate");
			String comments = MapUtils.getString(map, "comments");
			Long areaId = MapUtils.getLong(map, "areaId");
			WorkTimeDto workTimeDto = new WorkTimeDto();
			workTimeDto.setWorkTimeName(workTimeName);
			workTimeDto.setWorkTimeRule(workTimeRule);
			workTimeDto.setEffDate(effDate);
			workTimeDto.setExpDate(expDate);
			workTimeDto.setComments(comments);
			workTimeDto.setAreaId(areaId);
			workTimeDto.setState(WorkTimeDto.WORKTIME_STATE_NORMAL);
			Timestamp currentDate = DateHelper.getTimeStamp();
			workTimeDto.setStateDate(currentDate);
			workTimeDto.setCreateDate(currentDate);
			workTimeService.addWorkTime(workTimeDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----添加工作时间异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}
	
	@Transactional
	@Override
	public String modWorkTime(Map<String, Object> map) throws Exception {
		try {
			String workTimeName = MapUtils.getString(map, "workTimeName");
			String workTimeRule = MapUtils.getString(map, "workTimeRule");
			String effDate = MapUtils.getString(map, "effDate");
			String expDate = MapUtils.getString(map, "expDate");
			String comments = MapUtils.getString(map, "comments");
			Long id = MapUtils.getLong(map, "id");
			WorkTimeDto workTimeDto = new WorkTimeDto();
			workTimeDto.setWorkTimeName(workTimeName);
			workTimeDto.setWorkTimeRule(workTimeRule);
			workTimeDto.setEffDate(effDate);
			workTimeDto.setExpDate(expDate);
			workTimeDto.setComments(comments);
			workTimeDto.setId(id);
			workTimeDto.setStateDate(DateHelper.getTimeStamp());
			workTimeService.modWorkTime(workTimeDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----修改工作时间异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}
	

}
