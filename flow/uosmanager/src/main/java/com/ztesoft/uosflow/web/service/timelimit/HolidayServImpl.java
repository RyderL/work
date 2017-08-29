package com.ztesoft.uosflow.web.service.timelimit;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import com.zterc.uos.fastflow.dto.specification.HolidayDto;
import com.zterc.uos.fastflow.dto.specification.HolidaySystemDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.service.AreaService;
import com.zterc.uos.fastflow.service.HolidayService;


@Service("holidayServ")
public class HolidayServImpl implements HolidayServ {
	private static Logger logger = Logger.getLogger(HolidayServImpl.class);
	@Autowired
	private AreaService areaService; 
	@Autowired
	private HolidayService holidayService;

	/**
	 * 根据areaId查询节假日模板
	 * @throws Exception 
	 */
	@Override
	public String qryHolidayModelByAreaId(Map<String, Object> map) throws Exception {
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
			PageDto pageDto = holidayService.qryHolidayByCond(map);
			String result = GsonHelper.toJson(pageDto);
			return result;
		} catch (Exception e) {
			logger.error("---HolidayServ qryHolidayModelByAreaId方法执行异常，异常信息:"+e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * 删除节假日模板
	 * @throws Exception 
	 */
	@Transactional
	@Override
	public String delHolidayModel(Map<String, Object> map) throws Exception {
		try {
			Long holidayId = MapUtils.getLong(map, "id");
			HolidayDto holidayDto = new HolidayDto();
			holidayDto.setId(holidayId);
			holidayDto.setStateDate(new Date());
			holidayService.delHoliday(holidayDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----删除节假日模板异常，异常信息：" + e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * 新增节假日模板
	 */
	@Transactional
	@Override
	public String addHolidayModel(Map<String, Object> map) throws Exception {
		try {
			String holidayModelName = MapUtils.getString(map, "holidayModelName");
			String holidayModelRule = MapUtils.getString(map, "holidayModelRule");
			String holidayModelTimeUnit = MapUtils.getString(map, "holidayModelTimeUnit");
			String holidayModelState = MapUtils.getString(map, "holidayModelState");
			//String allService = MapUtils.getString(map, "allService");
			//String routeId = MapUtils.getString(map, "routeId");
			Long areaId = MapUtils.getLong(map, "areaId");
			String effDate = MapUtils.getString(map, "holidayModelEffDate");
			String expDate = MapUtils.getString(map, "holidayModelExpDate");
			String comments = MapUtils.getString(map, "holidayModelComments");
			HolidayDto holidayDto = new HolidayDto();
			holidayDto.setHolidayName(holidayModelName);
			holidayDto.setHolidayRule(holidayModelRule);
			holidayDto.setTimeUnit(holidayModelTimeUnit);
			holidayDto.setState(holidayModelState);
			//holidayDto.setAllService(allService);
			//holidayDto.setRoutId(routeId);
			holidayDto.setAreaId(areaId);
			holidayDto.setEffDate(effDate);
			holidayDto.setExpDate(expDate);
			holidayDto.setComments(comments);
			//holidayDto.setState(HolidayDto.HOLIDAY_STATE_NORMAL);
			Timestamp currentDate = DateHelper.getTimeStamp();
			holidayDto.setStateDate(currentDate);
			holidayDto.setCreateDate(currentDate);
			holidayService.addHoliday(holidayDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----添加节假日模板异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}
	
	/**
	 * 修改节假日模板
	 */
	@Transactional
	@Override
	public String modHolidayModel(Map<String, Object> map) throws Exception {
		try {
			String holidayName = MapUtils.getString(map, "holidayModelName");
			String holidayRule = MapUtils.getString(map, "holidayModelRule");
			String timeUnit = MapUtils.getString(map, "holidayModelTimeUnit");
			Long areaId = MapUtils.getLong(map, "areaId");
			String state = MapUtils.getString(map, "holidayModelState");
			String effDate = MapUtils.getString(map, "holidayModelEffDate");
			String expDate = MapUtils.getString(map, "holidayModelExpDate");
			String comments = MapUtils.getString(map, "holidayModelComments");
			Long id = MapUtils.getLong(map, "id");
			HolidayDto holidayDto = new HolidayDto();
			holidayDto.setHolidayName(holidayName);
			holidayDto.setHolidayRule(holidayRule);
			holidayDto.setState(state);
			holidayDto.setEffDate(effDate);
			holidayDto.setExpDate(expDate);
			holidayDto.setComments(comments);
			holidayDto.setId(id);
			holidayDto.setTimeUnit(timeUnit);
			holidayDto.setAreaId(areaId);
			holidayDto.setStateDate(DateHelper.getTimeStamp());
			holidayService.modHoliday(holidayDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----修改节假日模板异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * 根据areaId查询节假日
	 * @throws Exception 
	 */
	@Override
	public String qryHolidaySystemsByArea(Map<String, Object> map) throws Exception {
		try {
			Long areaId = LongHelper.valueOf(map.get("areaId"));//MapUtils.getLong(map, "areaId");
			if (areaId != null && areaId != -1 && areaId != 1) {
				AreaDto area = areaService.findAreaByAreaId(areaId);
				AreaDto[] areaDtos = areaService.findAreasByPathCode(area.getPathCode());
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
			PageDto pageDto = holidayService.qryHolidaySystemByCond(map);
			String result = GsonHelper.toJson(pageDto);
			logger.info("qryHolidaySystemsByArea.result="+result);
			return result;
		} catch (Exception e) {
			logger.error("---HolidayServ.qryHolidaySystemsByArea方法执行异常，异常信息:"+e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * 删除节假日
	 * @throws Exception 
	 */
	@Transactional
	@Override
	public String delHolidaySystems(Map<String, Object> map) throws Exception {
		try {
			Long holidayId = MapUtils.getLong(map, "id");
			HolidaySystemDto holidaySystemDto = new HolidaySystemDto();
			holidaySystemDto.setId(holidayId);
			holidaySystemDto.setStateDate(new Date());
			holidayService.delHolidaySystem(holidaySystemDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----删除节假日异常，异常信息：" + e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * 新增节假日
	 */
	@Transactional
	@Override
	public String addHolidaySystems(Map<String, Object> map) throws Exception {
		try {
			String holidaySystemName = MapUtils.getString(map, "holidaySystemName");
			String operType = MapUtils.getString(map, "holidaySystemOperType");
			String holidaySystemState = MapUtils.getString(map, "holidaySystemState");
			Long areaId = MapUtils.getLong(map, "areaId");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
			Date holidaySystemBeginDate = format.parse(MapUtils.getString(map, "holidaySystemStartDate"));
			Date holidaySystemEndDate = format.parse(MapUtils.getString(map, "holidaySystemEndDate"));
			String holidaySystemComments = MapUtils.getString(map, "holidaySystemComments");
			HolidaySystemDto holidaySystemDto = new HolidaySystemDto();
			holidaySystemDto.setHolidaySystemName(holidaySystemName);
			holidaySystemDto.setOperType(operType);
			holidaySystemDto.setState(holidaySystemState);
			//holidaySystemDto.setState(HolidaySystemDto.HOLIDAY_SYSTEM_STATE_NORMAL);
			//holidaySystemDto.setRoutId(routeId);
			holidaySystemDto.setAreaId(areaId);
			holidaySystemDto.setBeginDate(holidaySystemBeginDate);
			holidaySystemDto.setEndDate(holidaySystemEndDate);
			holidaySystemDto.setComments(holidaySystemComments);
			Date currentDate = new Date();
			holidaySystemDto.setStateDate(currentDate);
			holidaySystemDto.setCreateDate(currentDate);
			holidayService.addHolidaySystem(holidaySystemDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----添加节假日异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * 修改节假日
	 */
	@Transactional
	@Override
	public String modHolidaySystems(Map<String, Object> map) throws Exception {
		try {
			Long holidaySystemId = MapUtils.getLong(map, "id");
			String holidaySystemName = MapUtils.getString(map, "holidaySystemName");
			String operType = MapUtils.getString(map, "holidaySystemOperType");
			String holidaySystemState = MapUtils.getString(map, "holidaySystemState");
			Long areaId = MapUtils.getLong(map, "areaId");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
			Date holidaySystemBeginDate = format.parse(MapUtils.getString(map, "holidaySystemStartDate"));
			Date holidaySystemEndDate = format.parse(MapUtils.getString(map, "holidaySystemEndDate"));
			String holidaySystemComments = MapUtils.getString(map, "holidaySystemComments");
			HolidaySystemDto holidaySystemDto = new HolidaySystemDto();
			holidaySystemDto.setId(holidaySystemId);
			holidaySystemDto.setHolidaySystemName(holidaySystemName);
			holidaySystemDto.setOperType(operType);
			holidaySystemDto.setState(holidaySystemState);
			//holidaySystemDto.setState(HolidaySystemDto.HOLIDAY_SYSTEM_STATE_NORMAL);
			//holidaySystemDto.setRoutId(routeId);
			holidaySystemDto.setAreaId(areaId);
			holidaySystemDto.setBeginDate(holidaySystemBeginDate);
			holidaySystemDto.setEndDate(holidaySystemEndDate);
			holidaySystemDto.setComments(holidaySystemComments);
			Date currentDate = new Date();
			holidaySystemDto.setStateDate(currentDate);
			//holidaySystemDto.setCreateDate(currentDate);
			holidayService.modHolidaySystem(holidaySystemDto);
			return "{\"isSuccess\":true}";
		} catch (Exception e) {
			logger.error("----修改节假日异常，异常信息："+e.getMessage(),e);
			throw e;
		}
	}
	

}
