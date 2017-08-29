package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.dao.specification.FlowLimitDAO;
import com.zterc.uos.fastflow.dto.specification.FlowLimitDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;

public class FlowLimitDAOImpl extends AbstractDAOImpl implements FlowLimitDAO {

	private String INSERT_FLOWLIMIT = "INSERT INTO UOS_FLOW_LIMIT(ID,PACKAGE_ID,LIMIT_VALUE," +
			"ALERT_VALUE,TIME_UNIT,IS_WORK_TIME,AREA_ID) VALUES (?,?,?,?,?,?,?)";
	@Override
	public void addFlowLimit(FlowLimitDto flowLimitDto) {
		flowLimitDto.setId(SequenceHelper.getId("UOS_FLOW_LIMIT"));
		Object[] args = new Object[]{flowLimitDto.getId(),flowLimitDto.getPackageId(),
				flowLimitDto.getLimitValue(),flowLimitDto.getAlertValue(),flowLimitDto.getTimeUnit(),
				flowLimitDto.getIsWorkTime(),flowLimitDto.getAreaId()};
		saveOrUpdate(buildMap(INSERT_FLOWLIMIT, args));
	}

	private String UPDATE_FLOWLIMIT = "UPDATE UOS_FLOW_LIMIT SET PACKAGE_ID=?,LIMIT_VALUE=?,ALERT_VALUE=?," +
			"TIME_UNIT=?,IS_WORK_TIME=?,AREA_ID=? WHERE ID=?";
	@Override
	public void modFlowLimit(FlowLimitDto flowLimitDto) {
		Object[] args = new Object[]{flowLimitDto.getPackageId(),
				flowLimitDto.getLimitValue(),flowLimitDto.getAlertValue(),flowLimitDto.getTimeUnit(),
				flowLimitDto.getIsWorkTime(),flowLimitDto.getAreaId(),flowLimitDto.getId()};
		saveOrUpdate(buildMap(UPDATE_FLOWLIMIT, args));
	}

	private String DEL_FLOW_LIMIT = "DELETE FROM UOS_FLOW_LIMIT WHERE ID=?";
	@Override
	public void delFlowLimit(FlowLimitDto flowLimitDto) {
		Object[] args = new Object[]{flowLimitDto.getId()};
		saveOrUpdate(buildMap(DEL_FLOW_LIMIT, args));
	}
	@Override
	public PageDto qryFlowLimitByCond(Map<String, Object> params) {
		PageDto pageDto = new PageDto();
		StringBuffer qrySql = new StringBuffer("SELECT UFL.ID,UFL.ALERT_VALUE,UFL.AREA_ID,UFL.LIMIT_VALUE,UFL.IS_WORK_TIME," +
				"UFL.PACKAGE_ID,UFL.TIME_UNIT,UA.AREA_NAME,UP.NAME AS PROCESS_NAME" +
				" FROM UOS_FLOW_LIMIT UFL " +
				" LEFT JOIN UOS_AREA UA ON UA.AREA_ID=UFL.AREA_ID " +
				" LEFT JOIN UOS_PACKAGE UP ON UP.PACKAGEID = UFL.PACKAGE_ID " +
				" WHERE UFL.ROUTE_ID=1");
		List<Object> keys = new ArrayList<Object>();
		if(params != null){
			if(params.get("areaId") != null){
				qrySql.append(" AND UFL.AREA_ID = ?");
				keys.add(params.get("areaId"));
			}
			if(params.get("packageId") != null){
				qrySql.append(" AND UFL.PACKAGE_ID = ?");
				keys.add(params.get("packageId"));
			}
			if(params.get("packageDefineName") != null){
				qrySql.append(" AND UP.NAME like '%").append(params.get("packageDefineName")).append("%'");
			}
			QueryFilter queryFilter = new QueryFilter();
			queryFilter.setOrderBy("UFL.ID");
			queryFilter.setOrder(QueryFilter.ASC);
			if(params.get("page")!=null&&params.get("pageSize")!=null){
				Page<FlowLimitDto> page = new Page<FlowLimitDto>();
				page.setPageNo(IntegerHelper.valueOf(String.valueOf(params.get("page"))));
				page.setPageSize(IntegerHelper.valueOf(String.valueOf(params.get("pageSize"))));
				List<FlowLimitDto> list = queryList(page,queryFilter,FlowLimitDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
			}else{
				List<FlowLimitDto> list = queryList(FlowLimitDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(list.size());
			}
		}
		return pageDto;
	}
	
	@SuppressWarnings("unused")
	private String QRY_FLOW_LIMIT = "SELECT UFL.ID,UFL.ALERT_VALUE,UFL.AREA_ID,UFL.LIMIT_VALUE,UFL.IS_WORK_TIME," +
			"UFL.PACKAGE_ID,UFL.TIME_UNIT" +
			" FROM UOS_FLOW_LIMIT UFL WHERE UFL.ROUTE_ID = 1 ";
	@Override
	public FlowLimitDto qryFlowLimit(String packageDefineId,String areaId) {
		String sql = "SELECT UFL.ID,UFL.ALERT_VALUE,UFL.AREA_ID,UFL.LIMIT_VALUE,UFL.IS_WORK_TIME," +
				"UFL.PACKAGE_ID,UFL.TIME_UNIT" +
				" FROM UOS_FLOW_LIMIT UFL " +
				" LEFT JOIN UOS_PROCESSDEFINE UP ON UP.PACKAGEID = UFL.PACKAGE_ID " +
				" WHERE UFL.ROUTE_ID = 1 " +
				" AND UP.PACKAGEDEFINEID = ? AND AREA_ID=?";
		Object[] args = new Object[]{packageDefineId,areaId};
		FlowLimitDto flowLimitDto = queryObject(FlowLimitDto.class, sql, args);
		return flowLimitDto;
	}

}
