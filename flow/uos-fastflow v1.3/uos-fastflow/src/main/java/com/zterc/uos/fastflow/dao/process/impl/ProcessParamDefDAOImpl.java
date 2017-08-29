package com.zterc.uos.fastflow.dao.process.impl;

import java.util.ArrayList;
import java.util.List;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.process.ProcessParamDefDAO;
import com.zterc.uos.fastflow.dto.process.ProcessParamDefDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.ProcessParamDefRelDto;

public class ProcessParamDefDAOImpl extends AbstractDAOImpl implements
		ProcessParamDefDAO {

	protected static final String QUERY_PROCESS_PARAM_DEF = "SELECT A.CODE,A.NAME,A.VALUE,A.SYSTEM_CODE,A.COMMENTS,A.TYPE,A.IS_VARIABLE FROM UOS_PROINSPARAM_DEF A WHERE A.ROUTE_ID=1 ";

	@Override
	public List<ProcessParamDefDto> queryParamDefsByDefId(
			String processDefinitionId) {
		String sql = "SELECT A.CODE,A.NAME,A.VALUE,A.SYSTEM_CODE,A.COMMENTS,A.TYPE,A.IS_VARIABLE "
				+ "FROM UOS_PROINSPARAM_DEF A "
				+ "JOIN UOS_PROINSPARAM_DEF_REL B ON A.CODE = B.CODE "
				+ "WHERE B.TYPE='FLOW' AND B.PACKAGEDEFINEID = ? AND B.ROUTE_ID=1 ";
		return queryList(ProcessParamDefDto.class, sql,
				new Object[] { processDefinitionId });
	}

	private static final String QUERY_PROINSPARAM_DEF = "SELECT * FROM UOS_PROINSPARAM_DEF WHERE ROUTE_ID=1 AND SYSTEM_CODE = ?";

	@Override
	public PageDto qryProcessParamDefs(String systemCode) {
		PageDto pageDto = new PageDto();
		List<ProcessParamDefDto> list = queryList(ProcessParamDefDto.class,
				QUERY_PROINSPARAM_DEF, new Object[] { systemCode });
		pageDto.setTotal(list.size());
		pageDto.setRows(list);
		return pageDto;
	}

	private static final String INSERT_PROINSPARAM_DEF = "INSERT INTO UOS_PROINSPARAM_DEF(CODE,NAME,VALUE,SYSTEM_CODE,COMMENTS,IS_VARIABLE)VALUES(?,?,?,?,?,?)";

	@Override
	public void addProcessParamDef(ProcessParamDefDto dto) {
		Object[] args = new Object[] { dto.getCode(), dto.getName(),
				dto.getValue(), dto.getSystemCode(), dto.getComments(),dto.getIsVariable() };
		saveOrUpdate(buildMap(INSERT_PROINSPARAM_DEF, args));
	}

	private static final String UPDATE_PROINSPARAM_DEF = "UPDATE UOS_PROINSPARAM_DEF SET NAME = ?,VALUE = ?,COMMENTS = ?,IS_VARIABLE=? WHERE CODE = ?";

	@Override
	public void modProcessParamDef(ProcessParamDefDto dto) {
		Object[] args = new Object[] { dto.getName(), dto.getValue(),
				dto.getComments(),dto.getIsVariable(), dto.getCode() };
		saveOrUpdate(buildMap(UPDATE_PROINSPARAM_DEF, args));
	}

	private static final String DELETE_PROINSPARAM_DEF = "DELETE FROM UOS_PROINSPARAM_DEF WHERE CODE = ?";

	@Override
	public void delProcessParamDef(ProcessParamDefDto dto) {
		saveOrUpdate(buildMap(DELETE_PROINSPARAM_DEF,
				new Object[] { dto.getCode() }));
	}

	@Override
	public PageDto qryProcessParamDefRels(ProcessParamDefRelDto dto) {
		PageDto pageDto = new PageDto();
		String qrySql = "SELECT * FROM UOS_PROINSPARAM_DEF_REL WHERE ROUTE_ID=1 AND PACKAGEDEFINEID = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(dto.getPackageDefineId());
		if ("TACHE".equalsIgnoreCase(dto.getType())) {
			qrySql += " AND ((TYPE = ? AND TACHE_CODE = ?) OR IS_VARIABLE=1)";
			params.add(dto.getType());
			params.add(dto.getTacheCode());
		}else{
			qrySql += " AND TYPE = ? ";
			params.add(dto.getType());
		}
		List<ProcessParamDefRelDto> list = queryList(
				ProcessParamDefRelDto.class, qrySql.toString(),
				params.toArray(new Object[] {}));
		pageDto.setTotal(list.size());
		pageDto.setRows(list);
		return pageDto;
	}

	private static final String DELETE_PROINSPARAM_DEF_REL = "DELETE FROM UOS_PROINSPARAM_DEF_REL WHERE PACKAGEDEFINEID = ? AND TYPE = ? AND CODE = ?";
	private static final String BY_TACHE_CODE = " AND TACHE_CODE = ?";

	@Override
	public void delProcessParamDefRel(ProcessParamDefRelDto dto) {
		if ("FLOW".equalsIgnoreCase(dto.getType())) {
			Object[] args = new Object[] { dto.getPackageDefineId(),
					dto.getType(),dto.getCode() };
			saveOrUpdate(buildMap(DELETE_PROINSPARAM_DEF_REL, args));
		} else {// TACHE
			Object[] args = new Object[] { dto.getPackageDefineId(),
					dto.getType(),dto.getCode(), dto.getTacheCode() };
			saveOrUpdate(buildMap(DELETE_PROINSPARAM_DEF_REL + BY_TACHE_CODE,
					args));
		}
	}
	
	private static final String DELETE_PROINSPARAM_DEF_REL_NO_CODE = "DELETE FROM UOS_PROINSPARAM_DEF_REL WHERE PACKAGEDEFINEID = ? AND TYPE = ? ";

	@Override
	public void delProcessParamDefRelNoCode(ProcessParamDefRelDto dto) {
		if ("FLOW".equalsIgnoreCase(dto.getType())) {
			Object[] args = new Object[] { dto.getPackageDefineId(),
					dto.getType() };
			saveOrUpdate(buildMap(DELETE_PROINSPARAM_DEF_REL_NO_CODE, args));
		} else {// TACHE
			Object[] args = new Object[] { dto.getPackageDefineId(),
					dto.getType(), dto.getTacheCode() };
			saveOrUpdate(buildMap(DELETE_PROINSPARAM_DEF_REL_NO_CODE + BY_TACHE_CODE,
					args));
		}
	}

	private static final String INSERT_PROINSPARAM_DEF_REL = "INSERT INTO UOS_PROINSPARAM_DEF_REL(PACKAGEDEFINEID,CODE,VALUE,TYPE,TACHE_CODE,IS_VARIABLE)VALUES(?,?,?,?,?,?)";

	private static final String UPDATE_PROINSPARAM_DEF_REL = "UPDATE UOS_PROINSPARAM_DEF_REL SET VALUE = ? WHERE PACKAGEDEFINEID=? AND CODE=?,IS_VARIABLE=? AND TACHE_CODE=?";
	
	@Override
	public void updateBatchProcessParamDefRel(List<ProcessParamDefRelDto> dtos) {// batch!!!
		for (int i = 0, len = dtos.size(); i < len; i++) {
			ProcessParamDefRelDto dto = dtos.get(i);
			Object[] args = new Object[] {dto.getValue(), dto.getPackageDefineId(),
					dto.getCode(),dto.getIsVariable(), dto.getTacheCode() };
			saveOrUpdate(buildMap(UPDATE_PROINSPARAM_DEF_REL, args));
		}
	}
	

	@Override
	public void addBatchProcessParamDefRel(List<ProcessParamDefRelDto> dtos) {
		for (int i = 0, len = dtos.size(); i < len; i++) {
			ProcessParamDefRelDto dto = dtos.get(i);
			Object[] args = new Object[] { dto.getPackageDefineId(),
					dto.getCode(), dto.getValue(),dto.getType(),dto.getTacheCode(),dto.getIsVariable() };
			saveOrUpdate(buildMap(INSERT_PROINSPARAM_DEF_REL, args));
		}
	}

	@Override
	public ProcessParamDefDto queryParamDefByCode(String code) {
		String where = " AND CODE=?";
		return queryObject(ProcessParamDefDto.class, QUERY_PROCESS_PARAM_DEF
				+ where, new Object[] { code });
	}
	
	private static final String IS_HAVE_PROINSPARAM_DEF_REL = "SELECT COUNT(*) NUM FROM UOS_PROINSPARAM_DEF_REL WHERE ROUTE_ID=1 AND CODE = ?";
	@Override
	public boolean isExistRela(String code) {
		boolean isExist = false;
		Object count = queryCount(IS_HAVE_PROINSPARAM_DEF_REL, new Object[]{code});
		if(IntegerHelper.valueOf(count)>0){
			isExist = true;
		}
		return isExist;
	}

	@Override
	public List<ProcessParamDefDto> qryProcessParamDefs() {
		return queryList(ProcessParamDefDto.class, QUERY_PROCESS_PARAM_DEF);
	}

	@Override
	public List<ProcessParamDefRelDto> qryDistinctTacheCodeByDefId(
			String processDefinitionId) {
		String sql = "SELECT DISTINCT TACHE_CODE FROM UOS_PROINSPARAM_DEF_REL WHERE PACKAGEDEFINEID=? AND TACHE_CODE <> '-1' ";
		return queryList(ProcessParamDefRelDto.class, sql,new Object[]{processDefinitionId});
	}

	@Override
	public List<ProcessParamDefRelDto> qryProcessParamDefRelsByDefId(
			String processDefineId) {
		String sql = "SELECT PACKAGEDEFINEID,CODE,VALUE,TYPE,TACHE_CODE,ROUTE_ID,IS_VARIABLE FROM UOS_PROINSPARAM_DEF_REL WHERE PACKAGEDEFINEID=? AND ROUTE_ID=1";
		return queryList(ProcessParamDefRelDto.class, sql, new Object[]{processDefineId});
	}
	
}
