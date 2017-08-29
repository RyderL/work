package com.ztesoft.uosflow.web.service.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zterc.uos.base.dialect.MySqlDialect;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dao.process.ProcessInstanceDAO;
import com.zterc.uos.fastflow.dao.process.WorkItemDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;

@Service("DataCountQryServ")
public class DataCountQryServImpl implements DataCountQryServ {
	private static Logger logger = Logger.getLogger(DataCountQryServImpl.class);
	@Autowired
	private ProcessInstanceDAO processInstanceDAO;
	@Autowired
	private WorkItemDAO workItemDAO;

	@SuppressWarnings("unchecked")
	@Override
	public String qryProcInstState(Map<String,Object> paramMap) {
		List<String> list = new ArrayList<String>();
		List<String> states = (List<String>) paramMap.get("states");
		String stateStr = "";
		for(int i=0;i<states.size();i++){
			stateStr = states.get(i);
			List<Map<String,Object>> ret = processInstanceDAO.qryProcInstStateCount(stateStr);
			if(ret != null && ret.size() > 0){
				Map<String,Object> map = ret.get(0);
				Long count = 0L;
				if(JDBCHelper.getDialect() instanceof MySqlDialect){
					count = (Long) map.get("value");
				}else{
					count = ((BigDecimal) map.get("value")).longValue();
				}
				list.add(count.toString());
			}else{
				list.add("0");
			}
		}
		return GsonHelper.toJson(list);
	}

	@Override
	public String qryProcDefineUseCount(Map<String,Object> paramMap) {
		List<Map<String,Object>> list = processInstanceDAO.qryProcDefineUseCount();
		List<String> nameList = new ArrayList<String>();
		List<String> retList = new ArrayList<String>();
		for(Map<String,Object> temp:list){
			nameList.add(MapUtils.getString(temp, "name"));
			Long count = 0L;
			if(JDBCHelper.getDialect() instanceof MySqlDialect){
				count = (Long) temp.get("value");
			}else{
				count = ((BigDecimal) temp.get("value")).longValue();
			}
			retList.add(count.toString());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("count", retList);
		map.put("name", nameList);
		return GsonHelper.toJson(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String qryWorkItemState(Map<String,Object> paramMap) {
		List<String> list = new ArrayList<String>();
		List<String> states = (List<String>) paramMap.get("states");
		String stateStr = "";
		for(int i=0;i<states.size();i++){
			stateStr = states.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("states",stateStr);
			List<Map<String,Object>> ret = workItemDAO.qryWorkItemStateCount(map);
			if(ret != null && ret.size() > 0){
				Map<String,Object> retmap = ret.get(0);
				Long count = 0L;
				if(JDBCHelper.getDialect() instanceof MySqlDialect){
					count = (Long) retmap.get("value");
				}else{
					count = ((BigDecimal) retmap.get("value")).longValue();
				}
				list.add(count.toString());
			}else{
				list.add("0");
			}
		}
		return GsonHelper.toJson(list);
	}

	@Override
	public String qryExceptionFlow(Map<String, Object> paramMap) {
		PageDto page = processInstanceDAO.qryExceptionFlow(paramMap);
		return GsonHelper.toJson(page);
	}

	@Override
	public String qryProcInstState4Grid(Map<String, Object> paramMap) {
		List<Map<String,Object>> ret = processInstanceDAO.qryProcInstStateCount(null);
		Map<String,String> retMap = new HashMap<String,String>();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(Map<String,Object> map:ret){
			String value = "0";
			if(!"".equals(StringHelper.valueOf(map.get("value")))){
				value = StringHelper.valueOf(map.get("value"));
			}
			retMap.put(StringHelper.valueOf(map.get("name")), value);
		}
		list.add(retMap);
		String result = GsonHelper.toJson(list);
		logger.info("----result:"+result);
		return result;
	}

	@Override
	public String qryWorkItemState4Grid(Map<String, Object> paramMap) {
		List<Map<String,Object>> ret = workItemDAO.qryWorkItemStateCount(null);
		Map<String,String> retMap = new HashMap<String,String>();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(Map<String,Object> map:ret){
			String value = "0";
			if(!"".equals(StringHelper.valueOf(map.get("value")))){
				value = StringHelper.valueOf(map.get("value"));
			}
			retMap.put(StringHelper.valueOf(map.get("name")), value);
		}
		list.add(retMap);
		String result = GsonHelper.toJson(list);
		logger.info("----result:"+result);
		return result;
	}

	@Override
	public String proDefinePie4Grid(Map<String, Object> paramMap) {
		List<Map<String,Object>> ret = processInstanceDAO.qryProcDefineUseCount();
		Map<String,String> retMap = new HashMap<String,String>();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(Map<String,Object> map:ret){
			String value = "0";
			if(!"".equals(StringHelper.valueOf(map.get("value")))){
				value = StringHelper.valueOf(map.get("value"));
			}
			retMap.put(StringHelper.valueOf(map.get("name")), value);
		}
		list.add(retMap);
		String result = GsonHelper.toJson(list);
		logger.info("----result:"+result);
		return result;
	}

}
