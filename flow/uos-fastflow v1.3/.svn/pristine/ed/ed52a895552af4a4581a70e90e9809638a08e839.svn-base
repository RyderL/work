package com.zterc.uos.fastflow.dao.process.his.impl;

import org.springframework.stereotype.Repository;

import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.fastflow.dao.process.his.ProcessParamHisDAO;
import com.zterc.uos.fastflow.dto.process.ProInstParamDto;

@Repository("processParamHisDAO")
public class ProcessParamHisDAOImpl extends AbstractDAOImpl implements
		ProcessParamHisDAO {

	protected static final String INSERT_PROINSPARAM_HIS = "INSERT INTO UOS_PROINSPARAM_HIS (ID,PID,CODE,VAL,TACHE_CODE,STATE_DATE) VALUES (?,?,?,?,?,?) ";

	@Override
	public void addProcessParamHis(ProInstParamDto proInstParamDto) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(LongHelper.valueOf(proInstParamDto.getPid()));
			Object[] args = new Object[] { proInstParamDto.getId(),proInstParamDto.getPid(), proInstParamDto.getCode(), proInstParamDto.getVal(),
					proInstParamDto.getTacheCode(),proInstParamDto.getStateDate() };
			saveOrUpdate(buildMap(INSERT_PROINSPARAM_HIS, args));
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

}
