package com.zterc.uos.fastflow.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.process.ProcessParamDAO;
import com.zterc.uos.fastflow.dto.process.ProInstParamDto;
import com.zterc.uos.fastflow.dto.process.ProcessParamDefDto;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;

/**
 * （实例）流程变量操作类
 * 
 * @author gong.yi
 *
 */
public class ProcessParamService {
	private static String ParentWid = "ParentWid"; // 父工作项标识,子流程才会用到。

	private ProcessParamDAO processParamDAO;
	private ProcessParamDefService processParamDefService;

	public void setProcessParamDAO(ProcessParamDAO processParamDAO) {
		this.processParamDAO = processParamDAO;
	}

	public void setProcessParamDefService(
			ProcessParamDefService processParamDefService) {
		this.processParamDefService = processParamDefService;
	}

	/**
	 * （Server）初始化流程变量信息
	 * 
	 * @param processInstanceId
	 * @param processDefinitionId
	 * @param processParam
	 * @param useDB
	 */
	public Map<String,String> initProcessParam(Long processInstanceId,
			String processDefinitionId, Map<String, String> processParam,
			boolean useDB) {
		List<ProcessParamDefDto> paramDefDtos = processParamDefService
				.queryParamDefsByDefId(processDefinitionId);
		Map<String,String> map = new HashMap<String,String>();
		if(paramDefDtos != null){
			for (ProcessParamDefDto paramDefDto : paramDefDtos) {
				String value = paramDefDto.getValue();
				if (processParam != null) {
					if(processParam.containsKey(paramDefDto.getCode())){
						value = processParam.get(paramDefDto.getCode());
					}
				}
				if (value == null) {
					continue;
				}
				map.put(paramDefDto.getCode(), value);
				Timestamp stateDate = DateHelper.getTimeStamp();
				setProcessParam(processInstanceId, paramDefDto.getCode(), value,
						null, useDB,stateDate);
			}
		}
		return map;
	}

	/**
	 * （Server）设置流程变量
	 * 
	 * @param processInstanceId
	 * @param key
	 * @param value
	 * @param tacheCode
	 * @param useDB
	 */
	public void setProcessParam(Long processInstanceId, String key,
			String value, String tacheCode, boolean useDB,Timestamp stateDate) {
		Long id = SequenceHelper.getIdWithSeed("UOS_PROINSPARAM", StringHelper.valueOf(processInstanceId));
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
//				int count = processParamDAO.countProcessParam(processInstanceId,key);
//				if(count==0){
					processParamDAO.addProcessParam(id,processInstanceId, key, value,
							tacheCode,stateDate);
//				}else{
//					processParamDAO.updateProcessParam(processInstanceId,
//							key, value, tacheCode);
//				}
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					if(processModel.getUpdateKeyMap().containsKey(key)){
						processModel.getUpdateKeyMap().put(key,OperType.UPDATE+"");
					}else{
						processModel.getUpdateKeyMap().put(key,OperType.INSERT+"");
					}
					processModel.getParamMap().put(key, value);
					
				}
				// 内存模式操作----end-------------
			}
		} else {
//			int count = processParamDAO.countProcessParam(processInstanceId,key);
//			if(count==0){
//				processParamDAO.addProcessParam(processInstanceId, key, value,
//						tacheCode);
//			}else{
//				processParamDAO.updateProcessParam(processInstanceId,
//						key, value, tacheCode);
//			}
			processParamDAO.addProcessParam(id,processInstanceId, key, value,
					tacheCode,stateDate);
		}
	}

	/**
	 * （Server）更新流程变量
	 * 
	 * @param processInstanceId
	 * @param processParam
	 * @param useDB
	 */
	public void setProcessParam(Long processInstanceId,
			Map<String, String> processParam, boolean useDB) {
		for (String key : processParam.keySet()) {
			if (key == null || "".equals(key)) {
				continue;
			} else {
				String value = processParam.get(key);
				if (value == null) {
					continue;
				}
				Timestamp stateDate = DateHelper.getTimeStamp();
				setProcessParam(processInstanceId, key,value,null, useDB,stateDate);
			}
		}
	}
	
	/**
	 * （Server）更新流程变量
	 * 
	 * @param processInstanceId
	 * @param key
	 * @param value
	 * @param useDB
	 */
	public void updateProcessParam(Long processInstanceId,String key,String value,boolean useDB){
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				processParamDAO.updateProcessParam(processInstanceId,
						key, value, null);
			} else {
				// 内存模式操作----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					processModel.getParamMap().put(key, value);
					processModel.getUpdateKeyMap().put(key,OperType.UPDATE+"");
				}
				ProcessLocalHolder.set(processModel);
				// 内存模式操作----end-------------
			}
		} else {
			processParamDAO.updateProcessParam(processInstanceId, key,
					value, null);
		}
	}

	/**
	 * （Server）根据流程变量名称查询流程变量信息
	 * 
	 * @param processInstanceId
	 * @param paramName
	 * @return
	 */
	public String queryProcessParam(Long processInstanceId, String paramName) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			Map<String, String> paramMap = processModel.getParamMap();
			if (paramMap != null && paramMap.containsKey(paramName)) {
				return paramMap.get(paramName);
			}
			if("reportCalCondResult".equals(paramName)){
				return null;
			}
		}
		// 内存模式操作----end-------------

		ProInstParamDto proInstParamDto = processParamDAO.getProcessParam(
				processInstanceId, paramName);
		if (proInstParamDto == null) {
			return null;
		}
		return proInstParamDto.getVal();
	}
	
	/**
	 * （Server）设置父流程实例id
	 * 
	 * @param processInstanceId
	 * @param val
	 * @param tacheCode
	 * @param useDB
	 */
	public void setParentWid(Long processInstanceId, String val,
			String tacheCode, boolean useDB) {
		Timestamp stateDate = DateHelper.getTimeStamp();
		setProcessParam(processInstanceId,ParentWid,val,tacheCode,useDB,stateDate);
	}

	/**
	 * （Server）获取父流程实例id
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public String getParentWid(Long processInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			return processModel.getParamMap().get(ParentWid);
		}
		// 内存模式操作----end-------------

		ProInstParamDto dto = processParamDAO.getProcessParam(
				processInstanceId, ParentWid);
		if (dto != null) {
			return dto.getVal();
		}
		return null;
	}

	/**
	 * （Server）根据流程实例id查询流程变量列表
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public Map<String, String> getAllProcessParams(Long processInstanceId) {
		// 内存模式操作----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			return processModel.getParamMap();
		}
		// 内存模式操作----end-------------
		List<ProInstParamDto> dtos = processParamDAO
				.getAllProcessParams(processInstanceId);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < dtos.size(); i++) {
			map.put(dtos.get(i).getCode(), dtos.get(i).getVal());
		}
		return map;
	}

	public List<ProInstParamDto> getAllProcessParamsByPid(Long processInstanceId) {
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ProInstParamDto> list = new ArrayList<ProInstParamDto>();
			Map<String,String> map = processModel.getParamMap();
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				String code = key;
				String val = map.get(key);
				ProInstParamDto dto = new ProInstParamDto();
				dto.setPid(StringHelper.valueOf(processInstanceId));
				dto.setCode(code);
				dto.setVal(val);
				list.add(dto);
			}
			return list;
		}
		return processParamDAO.getAllProcessParams(processInstanceId);
	}

	public void deleteByPid(String processInstanceId) {
		processParamDAO.deleteByPid(processInstanceId);
	}
}
