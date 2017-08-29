package com.zterc.uos.fastflow.dao.process;

import java.sql.Timestamp;
import java.util.List;

import com.zterc.uos.fastflow.dto.process.ProInstAttrDto;

public interface ProcessAttrDAO {

	String queryProInsAttr(Long processInstanceId, String exActivityId,
			String attrName);

	Long countProInsAttr(Long processInstanceId, String exActivityId,
			String attrName);
	
	void updateProInsAttr(Long processInstanceId, String exActivityId,
			String attrName, String attrVal);

	void saveProInsAttr(Long id, Long processInstanceId, String exActivityId,
			String attrName, String attrVal,Timestamp stateDate);

	List<ProInstAttrDto> getAllProcessAttrs(Long processInstanceId);

	void deleteByPid(String processInstanceId);

}
