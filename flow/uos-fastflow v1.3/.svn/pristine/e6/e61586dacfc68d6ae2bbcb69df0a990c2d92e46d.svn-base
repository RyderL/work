package com.zterc.uos.fastflow.dao.specification.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.constant.CommonDomain;
import com.zterc.uos.fastflow.dao.specification.TacheDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.TacheCatalogDto;
import com.zterc.uos.fastflow.dto.specification.TacheDto;


public class TacheDAOImpl extends AbstractDAOImpl implements TacheDAO {

	@Override
	public PageDto qryTaches(Map<String, Object> params){
		PageDto pageDto = new PageDto();
		StringBuffer qrySql = new StringBuffer();
		qrySql.append("SELECT ID,TACHE_CATALOG_ID,TACHE_NAME,TACHE_CODE,CREATE_DATE,STATE,STATE_DATE,IS_AUTO,TACHE_TYPE,PACKAGEDEFINECODES,EFF_DATE,EXP_DATE")
				.append(" FROM "+FastflowConfig.tacheTableName+" WHERE ROUTE_ID=1");
		List<Object> keys = new ArrayList<Object>();
		if(params!=null){
			if (params.get("id") != null) {
				qrySql.append(" AND ID = ?");
				keys.add(params.get("id"));
			}
			if (params.get("tacheCatalogId") != null) {
				qrySql.append(" AND TACHE_CATALOG_ID = ?");
				keys.add(params.get("tacheCatalogId"));
			}
			if (params.get("tacheName") != null) {// 模糊查询
				qrySql.append(" AND TACHE_NAME LIKE ?");
				keys.add("%" + params.get("tacheName") + "%");
			}
			if (params.get("tacheCode") != null) {// 模糊查询
				qrySql.append(" AND TACHE_CODE LIKE ?");
				keys.add("%" + params.get("tacheCode") + "%");
			}
			if (params.get("state") != null) {
				qrySql.append(" AND STATE = ?");
				keys.add(params.get("state"));
				//add by che.zi 201606223 for zmp889943 begin
				if(CommonDomain.STATE_ACTIVE.equals(params.get("state"))){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
					params.put("currentDate", format.format(date));
				}
				//add by che.zi 201606223 for zmp889943 end
			}
			if (params.get("tacheType") != null) {
				qrySql.append(" AND TACHE_TYPE = ?");
				keys.add(params.get("tacheType"));
			}
			if(params.get("currentDate") != null){//筛选在有效时间内的
				//mod by che.zi 201606223 for zmp889943 begin
//				qrySql.append("AND ( ? BETWEEN EFF_DATE AND EXP_DATE) ");
//				keys.add(DateHelper.parseTime(params.get("currentDate")));
//				qrySql.append("AND EFF_DATE <= STR_TO_DATE('").append(StringHelper.valueOf(params.get("currentDate"))).append("','%Y-%m-%d %T') ");
//				qrySql.append("AND EXP_DATE > STR_TO_DATE('").append(StringHelper.valueOf(params.get("currentDate"))).append("','%Y-%m-%d %T') ");
				qrySql.append("AND EFF_DATE <= ").append(JDBCHelper.getDialect().getFormatDate());
				keys.add(DateHelper.parseTime(params.get("currentDate")));
				qrySql.append("AND EXP_DATE > ").append(JDBCHelper.getDialect().getFormatDate());
				keys.add(DateHelper.parseTime(params.get("currentDate")));
				//mod by che.zi 201606223 for zmp889943 end
			}
			if(params.get("invalid") !=null){//失效查询(包含超过生效时间的环节)
				qrySql.append(" AND (STATE ='10P' OR (STATE ='10A' AND ? > EXP_DATE) ) ");
				keys.add(DateHelper.getTimeStamp());
			}
			QueryFilter queryFilter = new QueryFilter();
			
			queryFilter.setOrderBy("TACHE_CATALOG_ID,STATE_DATE,ID");
			queryFilter.setOrder(QueryFilter.ASC+","+QueryFilter.DESC+","+QueryFilter.ASC);
			
			if(params.get("page")!=null&&params.get("pageSize")!=null){
				Page<TacheDto> page = new Page<TacheDto>();
				page.setPageNo(IntegerHelper.valueOf(String.valueOf(params.get("page"))));
				page.setPageSize(IntegerHelper.valueOf(String.valueOf(params.get("pageSize"))));
				List<TacheDto> list = queryList(page,queryFilter,TacheDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
			}else{
				List<TacheDto> list = queryList(TacheDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(list.size());
			}
		}
		return pageDto;
	}

	private static final String QUERY_TACHE_CATALOG="SELECT B.* FROM UOS_TACHE_CATALOG A,UOS_TACHE_CATALOG B WHERE A.ROUTE_ID=1 AND A.STATE='10A' AND B.STATE='10A'" +
			" AND A.SYSTEM_CODE=?  AND A.ID='-1' AND B.PATH_CODE LIKE CONCAT(A.PATH_CODE,'.%') ORDER BY B.ID";
	@Override
	public String qryTacheCatalogTree(Map<String, Object> params){
		List<TacheCatalogDto> list = queryList(TacheCatalogDto.class, QUERY_TACHE_CATALOG, new Object[]{params.get("systemCode")});
		if(list.isEmpty()){
			initCatalog((String)params.get("systemCode"));
		}
		return this.treeDataFormat(list);
	}
	private static final String IS_HAVE_ROOT ="SELECT COUNT(*) NUM FROM UOS_TACHE_CATALOG WHERE ROUTE_ID=1 AND ID ='-1'";
	private static final String INSERT_CATALOG_ROOT ="INSERT INTO UOS_TACHE_CATALOG" +
			"(ID,TACHE_CATALOG_NAME,PATH_CODE,CREATE_DATE,STATE,STATE_DATE,COMMENTS,SYSTEM_CODE)" +
			"VALUES('-1','root','-1',?,'10A',?,'根目录',?)";//systemCode默认初始化改为根据传参创建  FLOWPLAT
	//modiy by bobping  systemCode根据入参初始化
	private void initCatalog(String systemCode){
		if(StringHelper.isEmpty(systemCode)){
			systemCode = "FLOWPLAT";
		}
		String str = queryForString(IS_HAVE_ROOT);
		if(IntegerHelper.valueOf(str)==0){
			Timestamp curTime = DateHelper.getTimeStamp();
			saveOrUpdate(buildMap(INSERT_CATALOG_ROOT, new Object[]{curTime,curTime,systemCode}));
		}
	}

	private String treeDataFormat(List<TacheCatalogDto> tlist) {
		JsonArray list = new JsonArray();
		if (tlist != null && tlist.size() > 0) {
			Map<String, JsonObject> parentMap = new HashMap<String, JsonObject>();
			for (int i = 0; i < tlist.size(); i++) {
				TacheCatalogDto dto = tlist.get(i);
				String catalogPathCode = dto.getPathCode();
				String pathCode = catalogPathCode;
				if (catalogPathCode.lastIndexOf(".") >= 0) { // 不是最高层地区
					pathCode = catalogPathCode.substring(0,
							catalogPathCode.lastIndexOf("."));
				}
				// 给节点赋值
				JsonObject tache = dto.getTreeJsonObject();
				// 节点的层次
				if (parentMap.containsKey(pathCode)) {
					JsonObject parent = (JsonObject) parentMap.get(pathCode);
					JsonArray children = parent.getAsJsonArray("children");
					parent.addProperty("state", "closed");
					if (children == null) {
						children = new JsonArray();
						parent.add("children", children);
					}
					children.add(tache);
				} else {
					list.add(tache);
				}
				parentMap.put(catalogPathCode, tache);
			}
		}
		return GsonHelper.toJson(list);
	}
	
	private static final String COUNT_TACHE="SELECT COUNT(*) NUM FROM "+FastflowConfig.tacheTableName+" WHERE ROUTE_ID=1 AND STATE='10A' AND TACHE_CODE = ?";
	private static final String INSERT_TACHE="INSERT INTO "+FastflowConfig.tacheTableName+"(ID,TACHE_CATALOG_ID,TACHE_NAME,TACHE_CODE,CREATE_DATE,STATE,STATE_DATE,IS_AUTO,TACHE_TYPE,PACKAGEDEFINECODES,EFF_DATE,EXP_DATE)" +
				"VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	@Override
	public TacheDto addTache(TacheDto dto) {
		Object count = queryCount(COUNT_TACHE, new Object[]{dto.getTacheCode()});
		if(IntegerHelper.valueOf(count)>0){
			dto.setId(-1L);// 已经存在该环节编码的数据
		}else{
			dto.setId(SequenceHelper.getId(FastflowConfig.tacheTableName));
			Object[] args = new Object[] {dto.getId(),dto.getTacheCatalogId(),dto.getTacheName(),dto.getTacheCode(),dto.getCreateDate(),dto.getState(),
					dto.getStateDate(),dto.getIsAuto(),dto.getTacheType(),dto.getPackageDefineCodes(),dto.getEffDate(),dto.getExpDate()};
			saveOrUpdate(buildMap(INSERT_TACHE, args));
		}
		return dto;
	}

	private static final String UPDATE_TACHE="UPDATE "+FastflowConfig.tacheTableName+" SET TACHE_CATALOG_ID =?,TACHE_NAME =?,IS_AUTO = ?,PACKAGEDEFINECODES=?,STATE_DATE =?,EFF_DATE=?,EXP_DATE=? WHERE ID = ?";
	@Override
	public void modTache(TacheDto dto) {
		Object[] args = new Object[] {dto.getTacheCatalogId(), dto.getTacheName(),
				dto.getIsAuto(), dto.getPackageDefineCodes(),dto.getStateDate(),dto.getEffDate(),dto.getExpDate(), dto.getId() };
		saveOrUpdate(buildMap(UPDATE_TACHE, args));
	}

	private static final String DELETE_TACHE="UPDATE "+FastflowConfig.tacheTableName+" SET STATE ='10P',STATE_DATE =? WHERE ID = ?";
	@Override
	public void delTache(TacheDto dto) {
		Object[] args = new Object[] {dto.getStateDate(), dto.getId() };
		saveOrUpdate(buildMap(DELETE_TACHE, args));
	}

	private static final String INSERT_TACHE_CATALOG="INSERT INTO UOS_TACHE_CATALOG(ID,TACHE_CATALOG_NAME,PATH_CODE,PATH_NAME,PARENT_TACHE_CATALOG_ID,CREATE_DATE,STATE,STATE_DATE,COMMENTS,SYSTEM_CODE)"
			+ " SELECT ?,?," +
			JDBCHelper.getDialect().concat("UTC.PATH_CODE", "'.'","?")+
			"," +
			JDBCHelper.getDialect().concat("UTC.PATH_NAME", "'/'","?") +
			",UTC.ID,?,'10A',?,?,?"
			+ " FROM UOS_TACHE_CATALOG UTC WHERE UTC.ID = ?";
	@Override
	public TacheCatalogDto addTacheCatalog(TacheCatalogDto dto) {
		dto.setId(SequenceHelper.getId("UOS_TACHE_CATALOG"));
		Object[] args = new Object[] {dto.getId(),dto.getTacheCatalogName(),dto.getId(),dto.getTacheCatalogName(),dto.getCreateDate(),
				dto.getStateDate(),dto.getComments(),dto.getSystemCode(),dto.getParentTacheCatalogId()};
		saveOrUpdate(buildMap(INSERT_TACHE_CATALOG, args));
		return dto;
	}

	private static final String UPDATE_TACHE_CATALOG="UPDATE UOS_TACHE_CATALOG SET TACHE_CATALOG_NAME =? WHERE ID = ?";
	@Override
	public void modTacheCatalog(TacheCatalogDto dto) {
		Object[] args = new Object[] {dto.getTacheCatalogName(), dto.getId() };
		saveOrUpdate(buildMap(UPDATE_TACHE_CATALOG, args));
	}

	private static final String COUNT_CATALOG_TACHE="SELECT COUNT(*) NUM FROM "+FastflowConfig.tacheTableName+" WHERE ROUTE_ID=1 AND STATE='10A' AND TACHE_CATALOG_ID = ?";
	private static final String DELETE_TACHE_CATALOG="UPDATE UOS_TACHE_CATALOG SET STATE ='10P' WHERE ID = ?";
	@Override
	public void delTacheCatalog(TacheCatalogDto dto) {
		Object count = queryCount(COUNT_CATALOG_TACHE, new Object[]{dto.getId()});
		if(IntegerHelper.valueOf(count)==0){
			Object[] args = new Object[] {dto.getId()};
			saveOrUpdate(buildMap(DELETE_TACHE_CATALOG, args));
		}
	}

	private static final String QUERY_TACHE_BY_REASON="SELECT UT.* FROM UOS_TACHE UT JOIN UOS_TACHE_RETURN_REASON UTRR ON UT.ID = UTRR.TACHE_ID"
			+ " WHERE UT.ROUTE_ID=1 AND UTRR.RETURN_REASON_ID = ?";
	@Override
	public PageDto qryTachesByReturnReasonId(String returnReasonId) {
		PageDto pageDto = new PageDto();
		List<TacheDto> list = queryList(TacheDto.class, QUERY_TACHE_BY_REASON, new Object[]{returnReasonId});
		pageDto.setTotal(list.size());
		pageDto.setRows(list);
		return pageDto;
	}

	private static final String QUERY_TACHE = "SELECT ID,TACHE_NAME,TACHE_CODE,IS_AUTO,TACHE_TYPE,PACKAGEDEFINECODES,TACHE_ICON_NAME,SHADOW_NAME FROM "+FastflowConfig.tacheTableName+" WHERE ROUTE_ID = 1";
	private static final String QUERY_TACHE_CODE = "SELECT TACHE_NAME FROM "+FastflowConfig.tacheTableName+" WHERE ID = ? AND ROUTE_ID = 1";

	@Override
	public TacheDto queryTacheById(Long tacheId) {
		String where = " AND ID = ?";
		return queryObject(TacheDto.class, QUERY_TACHE + where, tacheId);
	}

	@Override
	public String getTacheCodeByTacheId(Long tacheId) {
		String tacheName = "";
		tacheName = queryForString(QUERY_TACHE_CODE, tacheId);
		return tacheName;
	}

	@Override
	public TacheDto queryTacheByCode(String tacheCode) {
		String where = " AND TACHE_CODE = ?";
		return queryObject(TacheDto.class, QUERY_TACHE + where, tacheCode);
	}
	
}
