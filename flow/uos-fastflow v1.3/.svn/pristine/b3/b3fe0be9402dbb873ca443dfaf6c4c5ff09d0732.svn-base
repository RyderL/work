package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.specification.TacheLimitDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.TacheLimitDto;

public class TacheLimitDAOImpl extends AbstractDAOImpl implements TacheLimitDAO {
	private static Logger logger = Logger.getLogger(TacheLimitDAOImpl.class);

	private String INSERT_TACHELIMIT = "INSERT INTO UOS_TACHE_LIMIT(ID,TACHE_ID,LIMIT_VALUE," +
			"ALERT_VALUE,TIME_UNIT,IS_WORK_TIME,AREA_ID) VALUES (?,?,?,?,?,?,?)";
	@Override
	public void addTacheLimit(TacheLimitDto tacheLimitDto) {
		tacheLimitDto.setId(SequenceHelper.getId("UOS_TACHE_LIMIT"));
		Object[] args = new Object[]{tacheLimitDto.getId(),tacheLimitDto.getTacheId(),tacheLimitDto.getLimitValue(),
				tacheLimitDto.getAlertValue(),tacheLimitDto.getTimeUnit(),tacheLimitDto.getIsWorkTime(),tacheLimitDto.getAreaId()};
		saveOrUpdate(buildMap(INSERT_TACHELIMIT, args));
	}

	private String UPDATE_TACHE_LIMIT = "UPDATE UOS_TACHE_LIMIT SET TACHE_ID=?,LIMIT_VALUE=?,ALERT_VALUE=?," +
			"TIME_UNIT=?,IS_WORK_TIME=?,AREA_ID=? WHERE ID=?";
	@Override
	public void modTacheLimit(TacheLimitDto tacheLimitDto) {
		Object[] args = new Object[]{tacheLimitDto.getTacheId(),tacheLimitDto.getLimitValue(),
				tacheLimitDto.getAlertValue(),tacheLimitDto.getTimeUnit(),tacheLimitDto.getIsWorkTime(),
				tacheLimitDto.getAreaId(),tacheLimitDto.getId()};
		saveOrUpdate(buildMap(UPDATE_TACHE_LIMIT, args));
	}

	private String DEL_TACHE_LIMIT = "DELETE FROM UOS_TACHE_LIMIT WHERE ID=?";
	@Override
	public void delTacheLimit(TacheLimitDto tacheLimitDto) {
		Object[] args = new Object[]{tacheLimitDto.getId()};
		saveOrUpdate(buildMap(DEL_TACHE_LIMIT, args));
	}
	
	private String QRY_TACHE_LIMIT = "SELECT UTL.ID,UTL.ALERT_VALUE,UTL.AREA_ID,UTL.IS_WORK_TIME," +
			"UTL.LIMIT_VALUE,UTL.TACHE_ID,UTL.TIME_UNIT,UT.TACHE_NAME,UA.AREA_NAME,UTC.PATH_NAME" +
			" FROM UOS_TACHE_LIMIT UTL " +
			"LEFT JOIN "+FastflowConfig.tacheTableName+" UT ON UTL.TACHE_ID=UT.ID " +
			" LEFT  JOIN UOS_AREA UA ON UTL.AREA_ID=UA.AREA_ID" +
			" LEFT  JOIN UOS_TACHE_CATALOG UTC ON UT.TACHE_CATALOG_ID=UTC.ID" +
			" WHERE UTL.ROUTE_ID=1 ";
	@Override
	public PageDto qryTacheLimitByCond(Map<String, Object> params) {
		PageDto pageDto = new PageDto();
		List<Object> keys = new ArrayList<Object>();
		StringBuffer qrySql = new StringBuffer(QRY_TACHE_LIMIT);
		if(params!=null){
			if(params.get("tacheId") != null && !"".equals(params.get("tacheId"))){
				qrySql.append(" AND UTL.TACHE_ID=?");
				keys.add(params.get("tacheId"));
			}
			if(params.get("areaId") != null && !"".equals(params.get("areaId"))){
				qrySql.append(" AND UTL.AREA_ID=?");
				keys.add(params.get("areaId"));
			}
			if(params.get("tacheName") != null && !"".equals(params.get("tacheName"))){
				qrySql.append(" AND UT.TACHE_NAME like '%").append(params.get("tacheName")).append("%'");
			}
			QueryFilter queryFilter = new QueryFilter();
			
			queryFilter.setOrderBy("UTL.ID");
			queryFilter.setOrder(QueryFilter.ASC);
			
			if(params.get("page")!=null&&params.get("pageSize")!=null){
				Page<TacheLimitDto> page = new Page<TacheLimitDto>();
				page.setPageNo(IntegerHelper.valueOf(String.valueOf(params.get("page"))));
				page.setPageSize(IntegerHelper.valueOf(String.valueOf(params.get("pageSize"))));
				List<TacheLimitDto> list = queryList(page,queryFilter,TacheLimitDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
			}else{
				List<TacheLimitDto> list = queryList(TacheLimitDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(list.size());
			}
		}
		return pageDto;
	}
	@Override
	public TacheLimitDto qryTacheLimitByByTAP(Long tacheId, String areaId,
			String processDefineId) {
		String sql = "SELECT UTL.ID,UTL.ALERT_VALUE,UTL.AREA_ID,UTL.IS_WORK_TIME," +
			"UTL.LIMIT_VALUE,UTL.TACHE_ID,UTL.TIME_UNIT " +
			" FROM UOS_TACHE_LIMIT UTL " +
			" LEFT JOIN UOS_TACHE_LIMIT_RULE UTLR ON UTL.ID=UTLR.TACHE_LIMIT_ID " +
			" LEFT JOIN UOS_PROCESSDEFINE UP ON UTLR.PACKAGE_ID=UP.PACKAGEID " +
			" WHERE UTL.TACHE_ID=? AND UP.PACKAGEDEFINEID=? AND UTL.AREA_ID=? AND UTL.ROUTE_ID=1 AND UP.STATE='10A' ";
		logger.info("-----qryTacheLimitByByTAPµÄsql:"+sql);
		TacheLimitDto tacheLimitDto = queryObject(TacheLimitDto.class, sql, new Object[]{tacheId,processDefineId,areaId});
		return tacheLimitDto;
	}

}
