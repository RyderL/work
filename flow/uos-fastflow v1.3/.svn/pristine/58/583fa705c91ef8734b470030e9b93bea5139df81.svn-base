package com.zterc.uos.fastflow.dao.process.impl;

import java.sql.Timestamp;
import java.util.List;

import com.zterc.uos.base.dialect.OracleDialect;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.DsContextHolder;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dao.process.ProcessParamDAO;
import com.zterc.uos.fastflow.dto.process.ProInstParamDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.AsynSqlExecBy3thParamDto;
import com.zterc.uos.fastflow.jdbc.sqlHolder.SqlLocalHolder;

public class ProcessParamDAOImpl extends AbstractDAOImpl implements
		ProcessParamDAO {

	protected static final String INSERT_PROINSPARAM = "INSERT INTO UOS_PROINSPARAM (ID,PID,CODE,VAL,TACHE_CODE,STATE_DATE) VALUES (?,?,?,?,?,?) ";

	protected static final String UPDATE_PROINSPARAM = "UPDATE  UOS_PROINSPARAM SET VAL=?,TACHE_CODE=? WHERE PID=? AND CODE=? ";

	protected static final String QUERY_PROINSPARAM = "SELECT ID,PID,CODE,VAL,TACHE_CODE FROM UOS_PROINSPARAM ";

	protected static final String COUNT_PROINSPARAM = "SELECT COUNT(*) FROM UOS_PROINSPARAM WHERE PID=? AND CODE=? ";

	@Override
	public void addProcessParam(Long id,Long processInstanceId, String key,
			String value, String tacheCode,Timestamp stateDate) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			Object[] args = new Object[] { id,processInstanceId, key, value,
					tacheCode,stateDate };
			if(!SqlLocalHolder.isHoldSqlOn()){
				saveOrUpdate(buildMap(INSERT_PROINSPARAM, args));
			}else{

				AsynSqlExecBy3thParamDto sqlParam = new AsynSqlExecBy3thParamDto(INSERT_PROINSPARAM,
						StringHelper.valueOf(id), args,
						AsynSqlExecBy3thParamDto.INSERT, "UOS_PROINSPARAM");
				SqlLocalHolder.addSqlParam(sqlParam);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public ProInstParamDto getProcessParam(Long processInstanceId,
			String paramName) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			String sql = "SELECT * FROM (" + QUERY_PROINSPARAM;
			String where= " WHERE PID=? AND CODE=? ORDER BY STATE_DATE DESC,ID DESC) A ";
			if(JDBCHelper.getDialect() instanceof OracleDialect){
				where += " WHERE ROWNUM = 1";
			}else{
				where += " LIMIT 1";
			}
			Object[] args = new Object[] { processInstanceId, paramName };
			return queryObject(ProInstParamDto.class,
					sql + where , args);
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public void updateProcessParam(Long processInstanceId, String key,
			String value, String tacheCode) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			Object[] args = new Object[] { value, tacheCode, processInstanceId,
					key };
			saveOrUpdate(buildMap(UPDATE_PROINSPARAM, args));
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public List<ProInstParamDto> getAllProcessParams(Long processInstanceId) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			Object[] args = new Object[] { processInstanceId };
//			String countSql = "SELECT CODE FROM UOS_PROINSPARAM WHERE PID=? GROUP BY CODE";
//			List<ProInstParamDto> countList = queryList(ProInstParamDto.class, countSql, args);
//			if(countList != null && countList.size()>0){
//				String sql = "SELECT * FROM (SELECT CODE,TACHE_CODE,PID,VAL,STATE_DATE " +
//						" FROM UOS_PROINSPARAM WHERE PID=? ORDER BY STATE_DATE DESC) A ";
//				if(JDBCHelper.getDialect() instanceof OracleDialect){
//					sql += " WHERE ROWNUM<="+ countList.size();
//				}else{
//					sql += " LIMIT 0," + countList.size();
//				}
//				return queryList(ProInstParamDto.class, sql, args);
//			}
//			return new ArrayList<ProInstParamDto>();
			String sql = "SELECT ID,CODE,TACHE_CODE,PID,VAL,STATE_DATE FROM UOS_PROINSPARAM WHERE PID=?";
			return queryList(ProInstParamDto.class, sql, args);
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public int countProcessParam(Long processInstanceId, String key) {
		String ds = DsContextHolder.getHoldDs();
		try {
			DsContextHolder.setDsForInstance(processInstanceId);
			return queryCount(COUNT_PROINSPARAM, processInstanceId, key)
					.intValue();
		} catch (Exception e) {
			throw e;
		} finally {
			DsContextHolder.setHoldDs(ds);
		}
	}

	@Override
	public void deleteByPid(String processInstanceId) {
		String sql = "DELETE FROM UOS_PROINSPARAM WHERE PID=?";
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
