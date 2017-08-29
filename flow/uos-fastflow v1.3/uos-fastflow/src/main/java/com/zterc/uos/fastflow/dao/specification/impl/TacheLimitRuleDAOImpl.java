package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.dao.specification.TacheLimitRuleDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.TacheLimitRuleDto;

public class TacheLimitRuleDAOImpl extends AbstractDAOImpl implements
		TacheLimitRuleDAO {

	private String INSERT_TACHE_LIMIT_RULE = "INSERT INTO UOS_TACHE_LIMIT_RULE(ID,TACHE_LIMIT_ID,PACKAGE_ID) values(?,?,?)";
	@Override
	public void addTacheLimitRule(TacheLimitRuleDto tacheLimitRuleDto) {
		tacheLimitRuleDto.setId(SequenceHelper.getId("UOS_TACHE_LIMIT_RULE"));
		Object[] args = new Object[]{tacheLimitRuleDto.getId(),tacheLimitRuleDto.getTacheLimitId(),tacheLimitRuleDto.getPackageId()};
		saveOrUpdate(buildMap(INSERT_TACHE_LIMIT_RULE, args));
	}

	private String DEL_TACHE_LIMIT_RULE = "DELETE FROM UOS_TACHE_LIMIT_RULE WHERE ID=?";
	@Override
	public void delTacheLimitRule(TacheLimitRuleDto tacheLimitRuleDto) {
		Object[] args = new Object[]{tacheLimitRuleDto.getId()};
		saveOrUpdate(buildMap(DEL_TACHE_LIMIT_RULE, args));
	}
	
	private String QRY_TACHE_LIMIT_RULE = "SELECT UTLR.ID,UTLR.TACHE_LIMIT_ID,UTLR.PACKAGE_ID,UP.NAME AS FLOW_NAME " +
			" FROM UOS_TACHE_LIMIT_RULE UTLR " +
			" LEFT JOIN UOS_PACKAGE UP ON UTLR.PACKAGE_ID=UP.PACKAGEID " +
			" WHERE UTLR.ROUTE_ID=1 ";
	@Override
	public PageDto qryTacheLimitRuleByCond(Map<String, Object> params) {
		PageDto pageDto = new PageDto();
		List<Object> keys = new ArrayList<Object>();
		StringBuffer qrySql = new StringBuffer(QRY_TACHE_LIMIT_RULE);
		if(params!=null){
			qrySql.append(" AND UTLR.TACHE_LIMIT_ID=?");
			keys.add(params.get("tacheLimitId"));
			QueryFilter queryFilter = new QueryFilter();
			
			queryFilter.setOrderBy("UTLR.ID");
			queryFilter.setOrder(QueryFilter.ASC);
			
			if(params.get("page")!=null&&params.get("pageSize")!=null){
				Page<TacheLimitRuleDto> page = new Page<TacheLimitRuleDto>();
				page.setPageNo(IntegerHelper.valueOf(String.valueOf(params.get("page"))));
				page.setPageSize(IntegerHelper.valueOf(String.valueOf(params.get("pageSize"))));
				List<TacheLimitRuleDto> list = queryList(page,queryFilter,TacheLimitRuleDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
			}else{
				List<TacheLimitRuleDto> list = queryList(TacheLimitRuleDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(list.size());
			}
		}
		return pageDto;
	}

}
