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
import com.zterc.uos.fastflow.dao.process.ProcessAttrDAO;
import com.zterc.uos.fastflow.dto.process.ProInstAttrDto;
import com.zterc.uos.fastflow.holder.OperType;
import com.zterc.uos.fastflow.holder.ProcessLocalHolder;
import com.zterc.uos.fastflow.holder.model.ProcessModel;

/**
 * ��ʵ��������ʵ�����Բ�����
 * 
 * @author gong.yi
 *
 */
public class ProcessAttrService {

	public static final String TARGETACTIVITYID = "TargetActivityId";
	public static final String STARTMODE = "StartMode";
	public static final String AUTOTOMANUAL = "AutoToManual";

	private ProcessAttrDAO processAttrDAO;

	public ProcessAttrDAO getProcessAttrDAO() {
		return processAttrDAO;
	}

	public void setProcessAttrDAO(ProcessAttrDAO processAttrDAO) {
		this.processAttrDAO = processAttrDAO;
	}

	/**
	 * ��Server����������ʵ��id���id�Լ��������ƻ�ȡ����ʵ������ֵ
	 * 
	 * @param processInstanceId
	 * @param activityId
	 * @param attrName
	 * @return
	 */
	public String getProcessAttr(Long processInstanceId, String activityId,String attrName) {
		// �ڴ�ģʽ����----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			return processModel.getAttrMap().get(activityId+"_"+attrName);
		}
		// �ڴ�ģʽ����----end-------------
		
		return processAttrDAO.queryProInsAttr(processInstanceId, activityId, attrName);
	}
	
	/**
	 * ��Server����������ʵ������ֵ
	 * 
	 * @param processInstanceId
	 * @param activityId
	 * @param attrName
	 * @param attrVal
	 * @param useDB
	 */
	public void setProcessAttr(Long processInstanceId, String activityId,String attrName,String attrVal,boolean useDB){
		Timestamp stateDate = DateHelper.getTimeStamp();
		Long id = SequenceHelper.getIdWithSeed("UOS_PROINSATTR", StringHelper.valueOf(processInstanceId));
		if (FastflowConfig.isCacheModel) {
			if (useDB) {
				synchronized (lock) {
//					Long count = processAttrDAO.countProInsAttr(processInstanceId, activityId, attrName);
//					if(count==0){
						processAttrDAO.saveProInsAttr(id,processInstanceId, activityId, attrName, attrVal,stateDate);
//					}else{
//						processAttrDAO.updateProInsAttr(processInstanceId, activityId, attrName, attrVal);
//					}
				}
			} else {
				// �ڴ�ģʽ����----begin----------
				ProcessModel processModel = ProcessLocalHolder.get();
				if (processModel != null) {
					String key = activityId+"_"+attrName;
					if(processModel.getAttrMap().containsKey(key)){
						processModel.getUpdateKeyMap().put(key,OperType.UPDATE+"");
					}else{
						processModel.getUpdateKeyMap().put(key,OperType.INSERT+"");
					}
					processModel.getAttrMap().put(activityId+"_"+attrName,attrVal);
					
				}
				ProcessLocalHolder.set(processModel);
				// �ڴ�ģʽ����----end-------------
				return;
			}
		} else {
			synchronized (lock) {
//				Long count = processAttrDAO.countProInsAttr(processInstanceId, activityId, attrName);
//				if(count==0){
					processAttrDAO.saveProInsAttr(id,processInstanceId, activityId, attrName, attrVal,stateDate);
//				}else{
//					processAttrDAO.updateProInsAttr(processInstanceId, activityId, attrName, attrVal);
//				}
			}
		}
	}

	private static final Object lock = new Object();
	
	/**
	 * ��Server����������ʵ��id��ȡ����ʵ������ֵ�б�
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public Map<String,String> getProcessAttrsByPId(Long processInstanceId) {
		// �ڴ�ģʽ����----begin----------
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			return processModel.getAttrMap();
		}
		// �ڴ�ģʽ����----end-------------
		List<ProInstAttrDto> dtos = processAttrDAO
				.getAllProcessAttrs(processInstanceId);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < dtos.size(); i++) {
			String activityId = dtos.get(i).getActivityId();
			String attrName = dtos.get(i).getAttr();
			String attrVal = dtos.get(i).getVal();
			map.put(activityId+"_"+attrName, attrVal);
		}
		return map;
	}

	public List<ProInstAttrDto> getAllProcessAttrs(Long processInstanceId) {
		ProcessModel processModel = ProcessLocalHolder.get();
		if (processModel != null) {
			List<ProInstAttrDto> list = new ArrayList<ProInstAttrDto>();
			Map<String,String> map = processModel.getAttrMap();
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				String activityId = key.split("_")[0];
				String attrName = key.split("_")[1];
				String attrVal = map.get(key);
				ProInstAttrDto dto = new ProInstAttrDto();
				dto.setPid(StringHelper.valueOf(processInstanceId));
				dto.setActivityId(activityId);
				dto.setAttr(attrName);
				dto.setVal(attrVal);
				list.add(dto);
			}
			return list;
		}
		return processAttrDAO.getAllProcessAttrs(processInstanceId);
	}

	public void deleteByPid(String processInstanceId) {
		processAttrDAO.deleteByPid(processInstanceId);
	}
}
