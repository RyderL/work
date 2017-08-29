package com.zterc.uos.fastflow.dao.specification.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.zterc.uos.fastflow.dao.specification.ReturnReasonDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonCatalogDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonConfigDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonDto;
import com.zterc.uos.fastflow.dto.specification.TacheReturnReasonDto;

public class ReturnReasonDAOImpl extends AbstractDAOImpl implements ReturnReasonDAO {

	private static final String QUERY_RETURN_REASON = "SELECT URR.* FROM UOS_RETURN_REASON URR" +
			" JOIN UOS_TACHE_RETURN_REASON UTRR ON URR.ID = UTRR.RETURN_REASON_ID" +
			" WHERE URR.ROUTE_ID=1 AND UTRR.TACHE_ID = ? AND UTRR.STATE='10A' ";
	@Override
	public PageDto qryReturnReasonsByTacheId(String tacheId){
		PageDto pageDto = new PageDto();
		List<ReturnReasonDto> list = queryList(ReturnReasonDto.class, QUERY_RETURN_REASON, new Object[]{tacheId});
		pageDto.setTotal(list.size());
		pageDto.setRows(list);
		return pageDto;
	}

	private static final String QUERY_REASON_CATALOG = "SELECT B.* FROM UOS_REASON_CATALOG A,UOS_REASON_CATALOG B" +
			" WHERE A.ROUTE_ID=1 AND A.STATE='10A' AND B.STATE='10A'" +
			" AND A.SYSTEM_CODE=? AND A.ID='-1' AND B.PATH_CODE LIKE " +
			JDBCHelper.getDialect().concat("A.PATH_CODE", "'.%'") +
			" ORDER BY B.ID";
	@Override
	public String qryReturnReasonCatalogTree(String systemCode){
		List<ReturnReasonCatalogDto> list = queryList(ReturnReasonCatalogDto.class, QUERY_REASON_CATALOG, new Object[]{systemCode});
		if(list.isEmpty()){
			initCatalog(systemCode);
		}
		return this.treeDataFormat(list);
	}
	
	private static final String IS_HAVE_ROOT ="SELECT COUNT(*) NUM FROM UOS_REASON_CATALOG WHERE ROUTE_ID=1 AND ID ='-1'";
	private static final String INSERT_CATALOG_ROOT ="INSERT INTO UOS_REASON_CATALOG" +
			"(ID,REASON_CATALOG_NAME,PATH_CODE,CREATE_DATE,STATE,STATE_DATE,COMMENTS,SYSTEM_CODE)" +
			"VALUES('-1','root','-1',?,'10A',?,'根目录',?)";
	//modify by bobping 初始化目录传入参数systemCode
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

	private String treeDataFormat(List<ReturnReasonCatalogDto> tlist) {
		JsonArray list = new JsonArray();
		if (tlist != null && tlist.size() > 0) {
			Map<String, JsonObject> parentMap = new HashMap<String, JsonObject>();
			for (int i = 0; i < tlist.size(); i++) {
				ReturnReasonCatalogDto dto = tlist.get(i);
				String catalogPathCode = dto.getPathCode();
				String pathCode = catalogPathCode;
				if (catalogPathCode.lastIndexOf(".") >= 0) { // 不是最高层地区
					pathCode = catalogPathCode.substring(0,
							catalogPathCode.lastIndexOf("."));
				}
				// 给节点赋值
				JsonObject catalog = dto.getTreeJsonObject();
				// 节点的层次
				if (parentMap.containsKey(pathCode)) {
					JsonObject parent = (JsonObject) parentMap.get(pathCode);
					JsonArray children = parent.getAsJsonArray("children");
					parent.addProperty("state", "closed");
					if (children == null) {
						children = new JsonArray();
						parent.add("children", children);
					}
					children.add(catalog);
				} else {
					list.add(catalog);
				}
				parentMap.put(catalogPathCode, catalog);
			}
		}
		return GsonHelper.toJson(list);
	}
	
	private static final String INSERT_REASON_CATALOG ="INSERT INTO UOS_REASON_CATALOG(ID,PARENT_REASON_CATALOG,REASON_CATALOG_NAME,PATH_CODE,CREATE_DATE,STATE,STATE_DATE,COMMENTS,SYSTEM_CODE)"
			+" SELECT ?,UTC.ID,?,"
			+JDBCHelper.getDialect().concat("UTC.PATH_CODE", "'.'","?")
			+",?,'10A',?,?,?"
			+" FROM UOS_REASON_CATALOG UTC WHERE UTC.ID = ? AND UTC.ROUTE_ID = 1";
	@Override
	public ReturnReasonCatalogDto addReturnReasonCatalog(ReturnReasonCatalogDto dto){
		dto.setId(SequenceHelper.getId("UOS_REASON_CATALOG"));
		Object[] args = new Object[] {dto.getId(),dto.getReasonCatalogName(),dto.getId(),dto.getCreateDate(),dto.getStateDate(),
				dto.getComments(),dto.getSystemCode(),dto.getParentReasonCatalog()};
		saveOrUpdate(buildMap(INSERT_REASON_CATALOG, args));
		return dto;
	}

	private static final String UPDATE_REASON_CATALOG = "UPDATE UOS_REASON_CATALOG SET REASON_CATALOG_NAME =? WHERE ID = ?";
	@Override
	public void modReturnReasonCatalog(ReturnReasonCatalogDto dto) {
		Object[] args = new Object[] {dto.getReasonCatalogName(),dto.getId()};
		saveOrUpdate(buildMap(UPDATE_REASON_CATALOG, args));
	}

	private static final String IS_CATALOG_HAVE_REASON ="SELECT COUNT(*) NUM FROM UOS_RETURN_REASON WHERE ROUTE_ID=1 AND STATE='10A' AND REASON_CATALOG_ID = ?";
	private static final String DELETE_REASON_CATALOG ="UPDATE UOS_REASON_CATALOG SET STATE ='10P' WHERE ID = ?";
	@Override
	public void delReturnReasonCatalog(ReturnReasonCatalogDto dto) {
		String str = queryForString(IS_CATALOG_HAVE_REASON,new Object[]{dto.getId()});
		int num = IntegerHelper.valueOf(str);
		if(num == 0){//页面不好控制的话，还是改成返回值的。如果页面能控制，上面的IS_CATALOG_HAVE_REASON应该就不需要了。
			saveOrUpdate(buildMap(DELETE_REASON_CATALOG,  new Object[]{dto.getId()}));
		}
	}

	@Override
	public PageDto qryReturnReasons(Map<String, Object> paramsMap) {
		PageDto pageDto = new PageDto();
		StringBuffer qrySql = new StringBuffer();
		qrySql.append("SELECT * FROM UOS_RETURN_REASON WHERE ROUTE_ID=1");
		List<Object> params = new ArrayList<Object>();
		if (paramsMap != null) {
			if (paramsMap.get("id") != null) {
				qrySql.append(" AND ID = ?");
				params.add(paramsMap.get("id"));
			}
			if (paramsMap.get("reasonCatalogId") != null) {
				qrySql.append(" AND REASON_CATALOG_ID = ?");
				params.add(paramsMap.get("reasonCatalogId"));
			}
			if (paramsMap.get("reasonCode") != null) {// 模糊查询
				qrySql.append(" AND REASON_CODE LIKE ?");
				params.add("%" + paramsMap.get("reasonCode") + "%");
			}
			if (paramsMap.get("reasonType") != null) {
				qrySql.append(" AND REASON_TYPE = ?");
				params.add(paramsMap.get("reasonType"));
			}
			if (paramsMap.get("returnReasonName") != null) {// 模糊查询
				qrySql.append(" AND RETURN_REASON_NAME LIKE ?");
				params.add("%" + paramsMap.get("returnReasonName") + "%");
			}
			if (paramsMap.get("state") != null) {
				qrySql.append(" AND STATE = ?");
				params.add(paramsMap.get("state"));
			}
			
			if(paramsMap.get("reasonClass") != null){
				qrySql.append(" AND REASON_CLASS = ?");
				params.add(paramsMap.get("reasonClass"));
			}
			QueryFilter queryFilter = new QueryFilter();
			
			queryFilter.setOrderBy("REASON_CATALOG_ID,STATE_DATE,ID");
			queryFilter.setOrder(QueryFilter.ASC+","+QueryFilter.DESC+","+QueryFilter.ASC);
			if(paramsMap.get("page")!=null&&paramsMap.get("pageSize")!=null){
				Page<ReturnReasonDto> page = new Page<ReturnReasonDto>();
				page.setPageNo(IntegerHelper.valueOf(String.valueOf(paramsMap.get("page"))));
				page.setPageSize(IntegerHelper.valueOf(String.valueOf(paramsMap.get("pageSize"))));
				List<ReturnReasonDto> list = queryList(page,queryFilter,ReturnReasonDto.class,qrySql.toString(),params.toArray(new Object[]{}));
				pageDto.setRows(list);
				pageDto.setTotal(IntegerHelper.valueOf(page.getTotalCount()));
			}
		}else{
			List<ReturnReasonDto> list = queryList(ReturnReasonDto.class, qrySql.toString(), params.toArray(new Object[]{}));
			pageDto.setTotal(list.size());
			pageDto.setRows(list);
		}
		return pageDto;
	}

	private static final String IS_HAVE_REASON ="SELECT COUNT(*) NUM FROM UOS_RETURN_REASON WHERE ROUTE_ID=1 AND STATE='10A' AND REASON_CODE = ?";
	private static final String INSERT_RETURN_REASON="INSERT INTO UOS_RETURN_REASON(ID,REASON_CATALOG_ID,REASON_CODE,REASON_TYPE,RETURN_REASON_NAME,"
			+ "COMMENTS,RECOMMEND_MEANS,CREATE_DATE,STATE,STATE_DATE,SYN_FLAG,ACTION_CODE)"
			+ "VALUES(?,?,?,?,?,?,?,?,'10A',?,'N','ADD')";
	@Override
	public ReturnReasonDto addReturnReason(ReturnReasonDto dto) {
		Object count = queryCount(IS_HAVE_REASON, new Object[]{dto.getReasonCode()});
		if(IntegerHelper.valueOf(count)>0){
			dto.setId(-1L);// 已经存在该异常原因编码的数据
		}else{
			dto.setId(SequenceHelper.getId("UOS_RETURN_REASON"));
			Object[] args = new Object[] {dto.getId(),dto.getReasonCatalogId(),dto.getReasonCode(),dto.getReasonType(),dto.getReturnReasonName(),
					dto.getComments(),dto.getRecommendMeans(),dto.getCreateDate(),dto.getStateDate()};
			saveOrUpdate(buildMap(INSERT_RETURN_REASON, args));
		}
		return dto;
	}

	private static final String UPDATE_RETURN_REASON="UPDATE UOS_RETURN_REASON SET REASON_CATALOG_ID =?,RETURN_REASON_NAME =?,STATE_DATE =?,SYN_FLAG='N',ACTION_CODE='MOD' WHERE ID = ?";
	@Override
	public void modReturnReason(ReturnReasonDto dto) {
		Object[] args = new Object[] {dto.getReasonCatalogId(),dto.getReturnReasonName(),dto.getStateDate(),dto.getId()};
		saveOrUpdate(buildMap(UPDATE_RETURN_REASON, args));
	}

	private static final String DELETE_RETURN_REASON="UPDATE UOS_RETURN_REASON SET STATE ='10P',SYN_FLAG='N',ACTION_CODE='DEL' WHERE ID = ?";
	@Override
	public void delReturnReason(ReturnReasonDto dto) {
		Object[] args = new Object[] {dto.getId()};
		saveOrUpdate(buildMap(DELETE_RETURN_REASON, args));
	}

	private static final String INSERT_TACHE_RETURN_REASON="INSERT INTO UOS_TACHE_RETURN_REASON(RETURN_REASON_ID,TACHE_ID,AUDIT_FLAG,AREA_ID,STATE) VALUES(?,?,?,?,?)";
	@Override
	public boolean addTacheReturnReason(Map<String, Object> params) {
		boolean isSuccess = false;
		if (params != null) {
			if (params.get("tacheIds") != null&& params.get("returnReasonId") != null) {// 一个异常原因配置多个环节
				List<?> tacheIds = (List<?>) params.get("tacheIds");
				for(int i=0,len = tacheIds.size();i<len;i++){
					saveOrUpdate(buildMap(INSERT_TACHE_RETURN_REASON,
							new Object[]{params.get("returnReasonId"),tacheIds.get(i),params.get("audiFlag"),params.get("areaId"),params.get("state")}));
				}
				isSuccess = true;
			}
			if (params.get("returnReasonIds") != null&& params.get("tacheId") != null) {// 一个环节配置多个异常原因
				List<?> returnReasonIds = (List<?>) params.get("returnReasonIds");
				for(int i=0,len = returnReasonIds.size();i<len;i++){
					saveOrUpdate(buildMap(INSERT_TACHE_RETURN_REASON,
							new Object[]{returnReasonIds.get(i),params.get("tacheId"),params.get("audiFlag"),params.get("areaId"),params.get("state")}));
				}
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	private static final String DELETE_TACHE_RETURN_REASON="UPDATE UOS_TACHE_RETURN_REASON SET STATE ='10X' WHERE RETURN_REASON_ID = ? AND TACHE_ID = ? AND AREA_ID = ?";
	@Override
	public boolean delTacheReturnReason(Map<String, Object> params) {
		Object[] args = new Object[] {params.get("returnReasonId"),
				params.get("tacheId"), params.get("areaId") };
		saveOrUpdate(buildMap(DELETE_TACHE_RETURN_REASON, args));
		return true;
	}

	private static final String QUERY_TACHE_RETURN_REASON = "SELECT UTRR.TACHE_ID,UTRR.RETURN_REASON_ID,UTC.TACHE_CATALOG_NAME,UTRR.AREA_ID,UA.AREA_NAME,UT.TACHE_NAME,"
			+ " UT.TACHE_CODE,UTRR.AUDIT_FLAG,URR.RETURN_REASON_NAME,URR.REASON_TYPE,UTRR.STATE "
			+ " FROM UOS_TACHE_RETURN_REASON UTRR"
			+ " JOIN "+FastflowConfig.tacheTableName+" UT ON UT.ID = UTRR.TACHE_ID"
			+ " JOIN UOS_TACHE_CATALOG UTC ON UTC.ID = UT.TACHE_CATALOG_ID"
			+ " JOIN UOS_RETURN_REASON URR ON URR.ID = UTRR.RETURN_REASON_ID"
			+ " JOIN UOS_AREA UA ON UA.AREA_ID = UTRR.AREA_ID"
			+ " WHERE UTRR.ROUTE_ID=1 ";
	@Override
	public PageDto qryTacheReturnReasons(Map<String, Object> paramsMap) {
		PageDto pageDto = new PageDto();
		StringBuffer qrySql = new StringBuffer(QUERY_TACHE_RETURN_REASON);
		List<Object> params = new ArrayList<Object>();
		if (paramsMap.get("tacheId") != null) {
			qrySql.append(" AND UTRR.TACHE_ID = ?");
			params.add(paramsMap.get("tacheId"));
		}
		if (paramsMap.get("returnReasonId") != null) {
			qrySql.append(" AND UTRR.RETURN_REASON_ID = ?");
			params.add(paramsMap.get("returnReasonId"));
		}
		if (paramsMap.get("state") !=null ){
			qrySql.append(" AND UTRR.STATE = ?");
			params.add(paramsMap.get("state"));
		}
		List<TacheReturnReasonDto> list = queryList(TacheReturnReasonDto.class, qrySql.toString(), params.toArray(new Object[]{}));
		pageDto.setTotal(list.size());
		pageDto.setRows(list);
		return pageDto;
	}

	private static final String QUERY_RETURN_REASON_CONFIG = "SELECT URC.ID,URC.REASON_ID,URC.TACHE_ID,URC.TARGET_TACHE_ID,URC.PACKAGEDEFINEID,URC.PACKAGEDEFINECODE,URC.AUTO_TO_MANUAL,URC.START_MODE,URC.AREA_ID,"
			+ "URR.REASON_CODE,URR.RETURN_REASON_NAME,URR.REASON_TYPE,UT1.TACHE_NAME,UT2.TACHE_NAME AS TARGET_TACHE_NAME"
			+ ",UT1.TACHE_CODE,UT2.TACHE_CODE AS TARGET_TACHE_CODE"
			+ " FROM UOS_REASON_CONFIG URC"
			+ " LEFT JOIN UOS_RETURN_REASON URR ON URC.REASON_ID = URR.ID"
			+ " LEFT JOIN "+FastflowConfig.tacheTableName+" UT1 ON URC.TACHE_ID = UT1.ID"
			+ " LEFT JOIN "+FastflowConfig.tacheTableName+" UT2 ON URC.TARGET_TACHE_ID = UT2.ID"
			+ " WHERE URC.ROUTE_ID=1 AND URC.PACKAGEDEFINECODE = ?";
	@Override
	public PageDto qryReturnReasonConfigs(String packageDefineCode) {
		PageDto pageDto = new PageDto();
		//modify by bobping
		List<ReturnReasonConfigDto> list = queryList(ReturnReasonConfigDto.class, QUERY_RETURN_REASON_CONFIG, new Object[]{packageDefineCode});
		pageDto.setTotal(list.size());
		pageDto.setRows(list);
		return pageDto;
	}

	private static final String DELETE_RETURN_REASON_CONFIG = "DELETE FROM UOS_REASON_CONFIG WHERE PACKAGEDEFINECODE = ?";
	private static final String INSERT_RETURN_REASON_CONFIG = "INSERT INTO UOS_REASON_CONFIG(ID,REASON_ID,TACHE_ID,TARGET_TACHE_ID,PACKAGEDEFINEID,PACKAGEDEFINECODE,AUTO_TO_MANUAL,START_MODE,AREA_ID,CREATE_DATE)"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	@Override
	public void saveReturnReasonConfigs(String packageDefineCode,List<ReturnReasonConfigDto> list) {
		// 清空旧数据
		//
		saveOrUpdate(buildMap(DELETE_RETURN_REASON_CONFIG,  new Object[]{packageDefineCode}));
		// 插入新数据
		for(int i=0,len=list.size();i<len;i++){
			ReturnReasonConfigDto dto = list.get(i);
			if(dto.getId()==null){
				dto.setId(SequenceHelper.getId("UOS_REASON_CONFIG"));
			}
			//modify by bobping 增加流程编码字段
			Object[] args = new Object[] {dto.getId(),dto.getReasonId(),dto.getTacheId(),dto.getTargetTacheId(),
					dto.getPackageDefineId(),dto.getPackageDefineCode(),dto.getAutoToManual(),dto.getStartMode(),dto.getAreaId(),dto.getCreateDate()};
			saveOrUpdate(buildMap(INSERT_RETURN_REASON_CONFIG, args));
		}
	}

	private static final String QUERY_REASON_CONFIG = "SELECT URC.TARGET_TACHE_ID,URC.START_MODE,URR.ID,URR.REASON_TYPE,URC.AUTO_TO_MANUAL "
			+ " FROM UOS_REASON_CONFIG URC"
			+ " LEFT JOIN UOS_RETURN_REASON URR"
			+ " ON URC.REASON_ID = URR.ID"
			+ " WHERE URR.REASON_CODE = ? AND URC.PACKAGEDEFINECODE = ? AND URC.TACHE_ID= ?  AND URR.ROUTE_ID = 1 AND URC.AREA_ID=? ";
	//modify by bobping 流程模板id改成流程模板编码获取异常原因配置
	@Override
	public ReturnReasonConfigDto getTargerActivityIdByReasonCode(
			String reason_Code, String packageDefineCode,String tacheId,String areaId) {
		return queryObject(ReturnReasonConfigDto.class,QUERY_REASON_CONFIG,new Object[]{reason_Code,packageDefineCode,tacheId,areaId});
	}

	private static final String MOD_TACHE_RETURN_REASON="UPDATE UOS_TACHE_RETURN_REASON SET STATE = ? WHERE RETURN_REASON_ID = ? AND TACHE_ID = ? AND AREA_ID = ?";
	@Override
	public boolean modTacheReturnReason(Map<String, Object> params) {
		Object[] args = new Object[] {params.get("state"),
				params.get("returnReasonId"),
				params.get("tacheId"), params.get("areaId") };
		saveOrUpdate(buildMap(MOD_TACHE_RETURN_REASON, args));
		return true;
	}

	@Override
	public boolean hasActiveReturnReasonsByTacheId(
			Map<String, Object> params) {
		boolean result = false;
		String sql = " AND UTRR.STATE='10A' ";
		List<ReturnReasonDto> list = queryList(ReturnReasonDto.class, QUERY_RETURN_REASON + sql, new Object[]{params.get("tacheId")});
		if(list != null && list.size()>0 ){
			result = true;
		}
		return result;
	}

	//modify by bobping 流程模板id改成流程模板编码获取异常原因配置
	@Override
	public List<ReturnReasonConfigDto> qryAllReturnReasonConfig() {
		String sql = "SELECT URC.TARGET_TACHE_ID,URC.START_MODE,URR.ID,URR.REASON_TYPE,URC.REASON_ID,URC.TACHE_ID," 
				+ "URR.REASON_CODE,URC.PACKAGEDEFINECODE,URC.AREA_ID "
				+ " FROM UOS_REASON_CONFIG URC"
				+ " LEFT JOIN UOS_RETURN_REASON URR"
				+ " ON URC.REASON_ID = URR.ID"
				+ " WHERE URR.ROUTE_ID = 1 ";
		List<ReturnReasonConfigDto> list = queryList(ReturnReasonConfigDto.class, sql);
		return list;
	}

	@Override
	public List<ReturnReasonConfigDto> qryReturnReasonConfigsByDefId(
			String oldProcessDefId) {
		String sql = "SELECT ID,REASON_ID,TACHE_ID,TARGET_TACHE_ID,PRODUCT_ID,SERVICE_ID,PACKAGEDEFINEID," +
				"AUTO_TO_MANUAL,START_MODE,IS_RETURN_CRM,AREA_ID,ACCESS_IDS,PORTTYPE_IDS,CREATE_DATE," +
				"SYN_FLAG,ACTION_CODE,ROUTE_ID" +
				" FROM UOS_REASON_CONFIG" +
				" WHERE PACKAGEDEFINEID=? AND ROUTE_ID=1";
		return queryList(ReturnReasonConfigDto.class, sql, new Object[]{oldProcessDefId});
	}

//	private static final String QUERY_WORKITEM_REASON_CONFIG= "SELECT DISTINCT URC.TARGET_TACHE_ID,URC.START_MODE,URR.ID AS REASON_ID,URR.REASON_TYPE AS REASON_CATALOG_ID,URC.ID AS REASON_CONFIG_ID"
//			+ " FROM UOS_REASON_CONFIG URC"
//			+ " LEFT JOIN UOS_RETURN_REASON URR ON URC.REASON_ID = URR.ID AND URR.STATE='10A'"
//			+ " LEFT JOIN UOS_WORKITEM UW ON UW.PACKAGEDEFINEID = URC.PACKAGEDEFINEID AND UW.TACHE_ID = URC.TACHE_ID"
//			+ " WHERE UW.WORKITEMID = ? AND URR.REASON_CODE = ? AND URR.ROUTE_ID = 1";
//	@Override
//	public ReturnReasonConfigDto qryReasonConfigs(String workItemId,String reasonCode) {
//		return queryObject(ReturnReasonConfigDto.class,QUERY_WORKITEM_REASON_CONFIG,new Object[]{workItemId,reasonCode});
//	}

}
