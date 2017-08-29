package com.zterc.uos.fastflow.dao.process.impl;

import java.sql.Timestamp;
import java.util.List;

import com.zterc.uos.base.dialect.OracleDialect;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dao.process.ProcessAttrDAO;
import com.zterc.uos.fastflow.dto.process.ProInstAttrDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.AsynSqlExecBy3thParamDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.SqlLocalHolder;

public class ProcessAttrDAOImpl extends AbstractDAOImpl implements ProcessAttrDAO {

	protected static final String QUERY_PROINSATTR = "SELECT VAL FROM UOS_PROINSATTR  WHERE PID=? AND ACTIVITYID = ? AND ATTR=?";
	protected static final String COUNT_PROINSATTR = "SELECT COUNT(*) FROM UOS_PROINSATTR  WHERE PID=? AND ACTIVITYID = ? AND ATTR=?";
	protected static final String UPDATE_PROINSATTR = "UPDATE UOS_PROINSATTR SET VAL = ? WHERE PID=? AND ACTIVITYID = ? AND ATTR=?";
	protected static final String INSERT_PROINSATTR = "INSERT INTO UOS_PROINSATTR(ID,PID,ACTIVITYID,ATTR,VAL,STATE_DATE) VALUES(?,?,?,?,?,?)";

	@Override
	public String queryProInsAttr(Long processInstanceId, String exActivityId, String attrName) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			Object[] args = new Object[] { processInstanceId, exActivityId, attrName };
			String sql = "SELECT * FROM (" + QUERY_PROINSATTR;
			String where= " ORDER BY STATE_DATE DESC,ID DESC) A ";
			if(JDBCHelper.getDialect() instanceof OracleDialect){
				where += " WHERE ROWNUM = 1";
			}else{
				where += " LIMIT 1";
			}
			return queryForString(sql + where, args);
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}
	
	@Override
	public Long countProInsAttr(Long processInstanceId, String exActivityId, String attrName) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			Object[] args = new Object[] { processInstanceId, exActivityId, attrName };
			return queryCount(COUNT_PROINSATTR, args);
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}
	
	@Override
	public void updateProInsAttr(Long processInstanceId, String exActivityId, String attrName,String attrVal) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			Object[] args = new Object[] { attrVal,processInstanceId, exActivityId, attrName };
			saveOrUpdate(buildMap(UPDATE_PROINSATTR, args));
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}
	
	@Override
	public void saveProInsAttr(Long id,Long processInstanceId, String exActivityId, String attrName,String attrVal,Timestamp stateDate) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			Object[] args = new Object[] { id,processInstanceId, exActivityId, attrName,attrVal,stateDate };
			if(!SqlLocalHolder.isHoldSqlOn()){
				saveOrUpdate(buildMap(INSERT_PROINSATTR, args));
			}else{

				AsynSqlExecBy3thParamDto sqlParam = new AsynSqlExecBy3thParamDto(INSERT_PROINSATTR,
						StringHelper.valueOf(id), args,
						AsynSqlExecBy3thParamDto.INSERT, "UOS_PROINSATTR");
				SqlLocalHolder.addSqlParam(sqlParam);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public List<ProInstAttrDto> getAllProcessAttrs(Long processInstanceId) {
		String ds = DsContextHolder.getHoldDs();
		List<ProInstAttrDto> proInstAttrDtos = null;
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			Object[] args = new Object[] { processInstanceId};
			String sql = "SELECT ID,PID,ATTR,VAL,ACTIVITYID,STATE_DATE FROM UOS_PROINSATTR WHERE PID=? ";
			proInstAttrDtos = queryList(ProInstAttrDto.class, sql, args);
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
		return proInstAttrDtos;
	}

	@Override
	public void deleteByPid(String processInstanceId) {
		String sql = "DELETE FROM UOS_PROINSATTR WHERE PID=?";
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(Long.valueOf(processInstanceId));
			Object[] args = new Object[] {processInstanceId};
			saveOrUpdate(buildMap(sql, args));
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}
}
