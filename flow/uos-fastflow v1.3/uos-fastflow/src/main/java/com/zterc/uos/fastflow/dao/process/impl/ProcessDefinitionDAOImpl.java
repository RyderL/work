package com.zterc.uos.fastflow.dao.process.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.jdbc.Page;
import com.zterc.uos.base.jdbc.QueryFilter;
import com.zterc.uos.fastflow.dao.process.ProcessDefinitionDAO;
import com.zterc.uos.fastflow.dto.process.ProcessDefinitionDto;

public class ProcessDefinitionDAOImpl extends AbstractDAOImpl implements ProcessDefinitionDAO {
	private static final String QUERY_PROCESS_DEFINE="SELECT PACKAGEDEFINEID,PACKAGEID,PACKAGEDEFINECODE,NAME,DESCRIPTION,VERSION,AUTHOR," +
			" STATE,STATEDATE,VALIDFROMDATE,VALIDTODATE,EDITUSER,EDITDATE,XPDL," +
			JDBCHelper.getDialect().getSubstr("VERSION,3")+" AS VERSIONNUM" +
			" FROM UOS_PROCESSDEFINE WHERE ROUTE_ID=1";
	@Override
	public ProcessDefinitionDto[] qryProcessDefineByPackageIds(String packageIds) {
		StringBuffer sqlSb = new StringBuffer();
        sqlSb.append(QUERY_PROCESS_DEFINE);
        sqlSb.append(" AND STATE<>'10X'");
        
        if(packageIds!=null){
        	sqlSb.append(" AND PACKAGEID IN ("+packageIds+")" );
        }
        sqlSb.append(" ORDER BY ").append(JDBCHelper.getDialect().getToNumber("VERSIONNUM"));
        //zdaas不支持，所以直接以version排序
//        sqlSb.append(" ORDER BY ").append("VERSION");
        List<ProcessDefinitionDto> processDefinitionDtos = queryList(ProcessDefinitionDto.class, sqlSb.toString(), new Object[]{});
		return processDefinitionDtos.toArray(new ProcessDefinitionDto[]{});
	}

//	private static final String QUERY_XPDL="SELECT XPDL FROM UOS_PROCESSDEFINE WHERE ROUTE_ID=1 AND PACKAGEDEFINEID = ?";
//	@Override
//	public ProcessDefinitionDto getXPDL(Long processDefineId) {
//		return queryObject(ProcessDefinitionDto.class, QUERY_XPDL, processDefineId);
//	}

	private static final String BY_PROCESS_DEFINE_ID=" AND PACKAGEDEFINEID = ? ";
	private static final String BY_PROCESS_DEFINE_CODE=" AND PACKAGEDEFINECODE = ? AND STATE='10A'";
	@Override
	public ProcessDefinitionDto qryProcessDefinitionById(Long processDefineId) {
		return queryObject(ProcessDefinitionDto.class, QUERY_PROCESS_DEFINE+BY_PROCESS_DEFINE_ID, processDefineId);
	}

	@Override
	public ProcessDefinitionDto qryProcessDefinitionByCode(String processDefineCode) {
		return queryObject(ProcessDefinitionDto.class, QUERY_PROCESS_DEFINE+BY_PROCESS_DEFINE_CODE, processDefineCode);
	}

	

	private static final String INSERT_PROCESS_DEFINE ="INSERT INTO UOS_PROCESSDEFINE(PACKAGEDEFINEID,PACKAGEDEFINECODE,PACKAGEID,NAME,DESCRIPTION,VERSION,AUTHOR,STATE,STATEDATE,VALIDFROMDATE,VALIDTODATE,EDITUSER,EDITDATE)" +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	@Override
	public void addProcessDefinition(ProcessDefinitionDto dto) {
		String packageDefineCode = dto.getPackageDefineCode()==null?StringHelper.valueOf(dto.getPackageDefineId()):dto.getPackageDefineCode();
		Object[] args = new Object[] {dto.getPackageDefineId(),packageDefineCode,dto.getPackageId(),dto.getName(),dto.getDescription(),dto.getVersion()
				,dto.getAuthor(),dto.getState(),dto.getStateDate(),dto.getValidFromDate(),dto.getValidToDate(),dto.getEditUser(),dto.getEditDate()};
		saveOrUpdate(buildMap(INSERT_PROCESS_DEFINE, args));
	}

	private static final String UPDATE_PROCESS_DEFINE_NAME ="UPDATE UOS_PROCESSDEFINE SET NAME = ? WHERE PACKAGEID= ? ";
	@Override
	public void updateProcessDefinitionNameByPackageId(Long packageId,String packageName) {
		Object[] args = new Object[] {packageName,packageId};
		saveOrUpdate(buildMap(UPDATE_PROCESS_DEFINE_NAME, args));
	}

	private static final String DELETE_PROCESS_DEFINE = "UPDATE UOS_PROCESSDEFINE SET STATE = ? , STATEDATE = ? WHERE PACKAGEDEFINEID = ? ";
	@Override
	public void deleteProcessDefinition(ProcessDefinitionDto dto) {
		Object[] args = new Object[] {dto.getState(),dto.getStateDate(),dto.getPackageDefineId()};
		saveOrUpdate(buildMap(DELETE_PROCESS_DEFINE, args));
	}
	
	private static final String BY_PACKAGE_ID=" AND PACKAGEID = ?";
	@Override
	public ProcessDefinitionDto[] qryProcessDefinitionsByPackageId(Long packageId) {
		List<ProcessDefinitionDto> processDefinitionDtos = queryList(ProcessDefinitionDto.class, QUERY_PROCESS_DEFINE+BY_PACKAGE_ID, new Object[]{packageId});
		return processDefinitionDtos.toArray(new ProcessDefinitionDto[]{});
	}

	private static final String SAVE_PROCESS_DEFINE_XPDL = "UPDATE UOS_PROCESSDEFINE SET XPDL=? , EDITDATE=? WHERE PACKAGEDEFINEID=?";
	@Override
	public void saveXPDL(Long processDefineId, String xpdl) {
		saveBlob(SAVE_PROCESS_DEFINE_XPDL,new Object[] {xpdl.getBytes(),DateHelper.getTimeStamp(),processDefineId});
	}

	private static final String UPDATE_PROCESS_DEFINE_STATE ="UPDATE UOS_PROCESSDEFINE SET STATE=? WHERE PACKAGEDEFINEID=? ";
	@Override
	public void updateFlowState(Long processDefineId, String state) {
		Object[] args = new Object[] {state,processDefineId};
		saveOrUpdate(buildMap(UPDATE_PROCESS_DEFINE_STATE, args));
	}
	
	@Override
	public List<ProcessDefinitionDto> qryProcessDefinitionsByCodes(
			String processDefineCodes) {
		String[] codes = processDefineCodes.split(",");
		if(codes.length==0){
			return new ArrayList<ProcessDefinitionDto>();
		}
		StringBuffer qrySql = new StringBuffer();
		qrySql.append(QUERY_PROCESS_DEFINE);
        qrySql.append(" AND PACKAGEDEFINECODE IN (");
        for(int i=0,len=codes.length;i<len;i++){
        	if(i>0){
        		qrySql.append(",");
        	}
        	qrySql.append("?");
        }
        qrySql.append(")");
        List<ProcessDefinitionDto> processDefineList = queryList(ProcessDefinitionDto.class, qrySql.toString(),new Object[]{codes});
		return processDefineList;
	}

	@Override
	public Map<String, Object> qryAllProcessDefinitionByCond(
			Map<String, String> paramMap) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		StringBuffer sqlStr = new StringBuffer("SELECT PACKAGEDEFINEID,NAME,PACKAGEDEFINECODE,VERSION " +
				"FROM UOS_PROCESSDEFINE WHERE STATE = '10A' AND ROUTE_ID=1 ");
		StringBuffer sqlStrCount = new StringBuffer("SELECT COUNT(*) FROM UOS_PROCESSDEFINE WHERE STATE = '10A'  AND ROUTE_ID=1 ");
        String flowPackageCode = paramMap.get("FLOWPACKAGECODE");
        String flowPackageName = paramMap.get("FLOWPACKAGENAME");
        int pageSize = LongHelper.valueOf(paramMap.get("PAGESIZE")).intValue();
        int pageIndex = LongHelper.valueOf(paramMap.get("PAGEINDEX")).intValue();

    	if (null != flowPackageCode && !"".equals(flowPackageCode)) {
			sqlStr.append( " AND PACKAGEDEFINECODE LIKE '%").append(flowPackageCode).append( "%'");
			sqlStrCount.append( " AND PACKAGEDEFINECODE LIKE '%").append(flowPackageCode).append( "%'");
		}
    	if (null != flowPackageName && !"".equals(flowPackageName)) {
			sqlStr.append(" AND NAME LIKE '%" ).append(flowPackageName).append( "%'");
			sqlStrCount.append(" AND NAME LIKE '%" ).append(flowPackageName).append( "%'");
		}
    	int totalCount = queryCount(sqlStrCount.toString()).intValue();
    	int pageCount = 0;
    	if (pageSize < 0) {
    		pageSize = totalCount - pageIndex;
        }
        //总页数
        if (totalCount == 0) {
        	resultMap.put("TOTALCOUNT", totalCount);
        	resultMap.put("PAGECOUNT", 0);
            return resultMap;
        }else {
        	resultMap.put("TOTALCOUNT", totalCount);
        	pageCount = (int) Math.ceil( (double)totalCount /pageSize);
        	resultMap.put("PAGECOUNT", pageCount);
        }
        //最后一页页面记录数可能小于步长
        if (pageIndex >= pageCount) {
        	pageIndex = pageCount;
        }
        Page<ProcessDefinitionDto> page = new Page<>();
        page.setPageNo(pageIndex);
        page.setPageSize(pageSize);
        QueryFilter filter = new QueryFilter();
        filter.setOrder(QueryFilter.ASC);
        filter.setOrderBy("PACKAGEDEFINEID");
        List<ProcessDefinitionDto> pList = queryList(page, filter, ProcessDefinitionDto.class, sqlStr.toString(), new Object[]{});
        for(int i = 0;i<pList.size();i++){
        	Map<String,Object> map = new HashMap<String, Object>();
        	ProcessDefinitionDto pDto = pList.get(i);
        	map.put("processId", pDto.getPackageDefineId());
        	map.put("processName", (pDto.getName() == null || pDto.getName() == "") ? "":pDto.getName());
        	map.put("processVersion", pDto.getVersion());
        	map.put("processCode", pDto.getPackageDefineCode());
        	list.add(map);
        }
        resultMap.put("PAGEINDEX", pageIndex);
        resultMap.put("PAGESIZE", pageSize);
        resultMap.put("FLOWLIST", list);
		return resultMap;
	}

	//modify by bobping 改为查询所有流程模板，包括失效和锁定，状态的过滤在代码测处理
	@Override
	public List<ProcessDefinitionDto> qryAllProcessDefines() {
//		return queryList(ProcessDefinitionDto.class, QUERY_PROCESS_DEFINE+" AND STATE = '10A' ");
		return queryList(ProcessDefinitionDto.class, QUERY_PROCESS_DEFINE + " AND STATE<>'10X' ");
	}

	@Override
	public String qryPackageDefinePath(Long processInstanceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String completedPath = "";
		try {
			Map<String, String> result = new HashMap<String, String>();
			connection = this.getConnection();
			preparedStatement = connection.prepareStatement("SELECT d.PATHCODE,c.NAME as PACKAGE_NAME,b.VERSION AS VERSION "
					+ "FROM uos_processinstance a "
					+ "LEFT JOIN uos_processdefine b on a.PACKAGEDEFINEID=b.PACKAGEDEFINEID "
					+ "LEFT JOIN uos_package c ON b.PACKAGEID=c.PACKAGEID "
					+ "LEFT JOIN uos_packagecatalog d ON c.CATALOGID=d.CATALOGID "
					+ "WHERE a.PROCESSINSTANCEID=?");
			preparedStatement.setLong(1, processInstanceId);
			rs = preparedStatement.executeQuery();
			if(rs.next()) {
				result.put("pathCode", rs.getString("PATHCODE"));
				result.put("packageName", rs.getString("PACKAGE_NAME"));
				result.put("version", rs.getString("VERSION"));
			}
			preparedStatement.close();
			rs.close();
			String pathCode = result.get("pathCode").replace(".", ",");
			preparedStatement = connection.prepareStatement("SELECT CATALOGNAME FROM uos_packagecatalog WHERE CATALOGID IN (" + pathCode + ") ORDER BY PATHCODE");
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				completedPath = completedPath + "/" + rs.getString("CATALOGNAME");
			}
			completedPath = completedPath + "/" + result.get("packageName");
			completedPath = completedPath + "/" + result.get("version");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return completedPath;
	}
	
}
