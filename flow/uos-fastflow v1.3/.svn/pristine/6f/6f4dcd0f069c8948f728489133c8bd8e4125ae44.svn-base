package com.zterc.uos.fastflow.dao.process.his.impl;


import org.springframework.stereotype.Repository;

import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.fastflow.dao.process.his.ProcessAttrHisDAO;
import com.zterc.uos.fastflow.dto.process.ProInstAttrDto;

@Repository("processAttrHisDAO")
public class ProcessAttrHisDAOImpl extends AbstractDAOImpl implements ProcessAttrHisDAO {

	protected static final String INSERT_PROINSATTR_HIS = "INSERT INTO UOS_PROINSATTR_HIS(ID,PID,ACTIVITYID,ATTR,VAL,STATE_DATE) VALUES(?,?,?,?,?,?)";

	
	@Override
	public void saveProInsAttrHis(ProInstAttrDto proInstAttrDto) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(LongHelper.valueOf(proInstAttrDto.getPid()));
			Object[] args = new Object[] { proInstAttrDto.getId(),proInstAttrDto.getPid(), proInstAttrDto.getActivityId(), 
					proInstAttrDto.getAttr(),proInstAttrDto.getVal(),proInstAttrDto.getStateDate()};
			saveOrUpdate(buildMap(INSERT_PROINSATTR_HIS, args));
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

}
