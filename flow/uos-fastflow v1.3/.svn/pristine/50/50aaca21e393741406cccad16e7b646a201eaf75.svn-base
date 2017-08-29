package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.specification.DispatchRuleDAO;
import com.zterc.uos.fastflow.dto.specification.DispatchRuleDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public class DispatchRuleDAOImpl extends AbstractDAOImpl implements DispatchRuleDAO {

	@Override
	public PageDto qryDispatchRuleByCond(Map<String, Object> paramMap){
		PageDto pageDto = new PageDto();
		StringBuffer qrySql = new StringBuffer();
		qrySql.append("SELECT *")
			.append(" FROM UOS_DISPATCH_RULE WHERE ROUTE_ID=1 AND STATE='10A'");
		List<Object> params = new ArrayList<Object>();
		if(paramMap!=null){
			if(paramMap.get("packageDefineId")!=null){
				qrySql.append(" AND ((PACKAGEDEFINEID = ? AND APPLY_ALL='0')OR APPLY_ALL='1')");
				params.add(paramMap.get("packageDefineId"));
			}else{
				qrySql.append(" AND APPLY_ALL='1'");
			}
			if(paramMap.get("tacheCode")!=null){
				qrySql.append(" AND TACHE_CODE = ?");
				params.add(paramMap.get("tacheCode"));
			}
		}
		List<DispatchRuleDto> list = queryList(DispatchRuleDto.class, qrySql.toString(), params.toArray(new Object[]{}));
		pageDto.setTotal(list.size());
		pageDto.setRows(list);
		return pageDto;
	}
	
	private static final String IS_HAVE_APPLY_ALL = "SELECT COUNT(*) NUM FROM UOS_DISPATCH_RULE WHERE ROUTE_ID=1 AND STATE='10A'" +
			" AND AREA_ID = ? AND TYPE= ? AND APPLY_ALL ='0' AND TACHE_CODE = ? AND PACKAGEDEFINEID= ?";
	
	private static final String IS_HAVE_SINGLE = "SELECT COUNT(*) NUM FROM UOS_DISPATCH_RULE WHERE ROUTE_ID=1 AND STATE='10A' AND APPLY_ALL ='1'" +
			" AND AREA_ID = ? AND TYPE= ?  AND TACHE_CODE = ?";

	private static final String INSERT_DISPATCH_RULE = "INSERT INTO UOS_DISPATCH_RULE(" +
			"ID, PACKAGEDEFINEID, TACHE_ID,TACHE_CODE, AREA_ID, AREA_NAME, TYPE, ROLLBACK_TYPE, APPLY_ALL, PARTY_TYPE, PARTY_ID, PARTY_NAME," +
			"MANUAL_PARTY_TYPE, MANUAL_PARTY_ID, MANUAL_PARTY_NAME, CALL_TYPE, BIZ_ID, BIZ_NAME, IS_AUTOMATIC_RETURN, IS_AUTO_MANUAL, " +
			"IS_REVERSE_AUTOMATIC_RETURN, IS_REVERSE_AUTOMATIC_MANUAL, STATE" +
			")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'10A')";
	@Override
	public DispatchRuleDto addDispatchRule(DispatchRuleDto dto){
		String str = null;
		if("0".equals(dto.getApplyAll())){
			str = queryForString(IS_HAVE_APPLY_ALL,new Object[]{dto.getAreaId(),dto.getType(),dto.getTacheCode(),dto.getPackageDefineId()});
		}else{//1
			str = queryForString(IS_HAVE_SINGLE,new Object[]{dto.getAreaId(),dto.getType(),dto.getTacheCode()});
		}
		int num = IntegerHelper.valueOf(str);
		if(num>0){
			return null;//已经存在该派发规则的数据
		}
		Object[] args = new Object[] {
				dto.getId(),dto.getPackageDefineId(),dto.getTacheId(),dto.getTacheCode(),dto.getAreaId(),dto.getAreaName(),
				dto.getType(),dto.getRollbackType(),dto.getApplyAll(),dto.getPartyType(),dto.getPartyId(),dto.getPartyName(),
				dto.getManualPartyType(),dto.getManualPartyId(),dto.getManualPartyName(),dto.getCallType(),dto.getBizId(),dto.getBizName(),
				dto.getIsAutomaticReturn(),dto.getIsAutoManual(),dto.getIsReverseAutomaticReturn(),dto.getIsReverseAutomaticManual()};
		saveOrUpdate(buildMap(INSERT_DISPATCH_RULE, args));
		return dto;
	}

	private static final String UPDATE_DISPATCH_RULE ="UPDATE UOS_DISPATCH_RULE SET AREA_ID= ?,AREA_NAME=?,TYPE = ?,ROLLBACK_TYPE=?,APPLY_ALL=?,PARTY_TYPE=?,PARTY_ID=?,PARTY_NAME=?," +
			"MANUAL_PARTY_TYPE=?,MANUAL_PARTY_ID=?, MANUAL_PARTY_NAME=?, CALL_TYPE=?, BIZ_ID=?, BIZ_NAME=?, IS_AUTOMATIC_RETURN=?, IS_AUTO_MANUAL=?," +
			"IS_REVERSE_AUTOMATIC_RETURN=?, IS_REVERSE_AUTOMATIC_MANUAL=?,STATE_DATE = ? WHERE ID=?";
	
	@Override
	public void modDispatchRule(DispatchRuleDto dto){
		Object[] args = new Object[] {
				dto.getAreaId(),dto.getAreaName(),
				dto.getType(),dto.getRollbackType(),dto.getApplyAll(),dto.getPartyType(),dto.getPartyId(),dto.getPartyName(),
				dto.getManualPartyType(),dto.getManualPartyId(),dto.getManualPartyName(),dto.getCallType(),dto.getBizId(),dto.getBizName(),
				dto.getIsAutomaticReturn(),dto.getIsAutoManual(),dto.getIsReverseAutomaticReturn(),dto.getIsReverseAutomaticManual(),
				dto.getStateDate(),dto.getId()};
		saveOrUpdate(buildMap(UPDATE_DISPATCH_RULE, args));
	}

	private static final String DELETE_DISPATCH_RULE ="UPDATE UOS_DISPATCH_RULE SET STATE='10P',STATE_DATE = ? WHERE ID=?";
	
	@Override
	public void delDispatchRule(Long id){
		saveOrUpdate(buildMap(DELETE_DISPATCH_RULE, new Object[] {DateHelper.getTimeStamp(),id}));
	}

	@Override
	public DispatchRuleDto[] qryDispatchRule(DispatchRuleDto dto) {
		StringBuffer qrySql = new StringBuffer();
		qrySql.append("SELECT *")
			.append(" FROM UOS_DISPATCH_RULE WHERE ROUTE_ID=1");// AND STATE='10A'
		List<Object> params = new ArrayList<Object>();
		if(dto.getPackageDefineId()!=null){
			qrySql.append(" AND ((PACKAGEDEFINEID = ? AND APPLY_ALL='0')OR APPLY_ALL='1')");
			params.add(dto.getPackageDefineId());
		}else{
			qrySql.append(" AND APPLY_ALL='1'");
		}
		if(dto.getTacheId()!=null){
			qrySql.append(" AND TACHE_ID = ?");
			params.add(dto.getTacheId());
		}
		if(dto.getAreaId()!=null){
			qrySql.append(" AND AREA_ID = ?");
			params.add(dto.getAreaId());
		}
		qrySql.append(" ORDER BY APPLY_ALL,ID");//排序——非全局的置顶
		List<DispatchRuleDto> list = queryList(DispatchRuleDto.class, qrySql.toString(), params.toArray(new Object[]{}));
		return list.toArray(new DispatchRuleDto[]{});
	}
}
