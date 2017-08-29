package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.specification.AreaDAO;
import com.zterc.uos.fastflow.dao.specification.ProcessPackageDAO;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.dto.specification.PackageCatalogDto;
import com.zterc.uos.fastflow.dto.specification.PackageDto;

public class ProcessPackageDAOImpl extends AbstractDAOImpl implements ProcessPackageDAO {
	@Autowired
	private AreaDAO areaDAO;
	@Override
	public PackageCatalogDto[] qryPackageCatalogByAreaIdAndSystemCode(
			String areaIds, String systemCode) {
		StringBuffer sqlSb = new StringBuffer();
        sqlSb.append(" SELECT A.CATALOGID,A.AREAID,A.CATALOGNAME,A.PARENTID ");
        sqlSb.append(" ,A.PATHCODE,A.PATH_NAME,A.PACKAGECATALOGTYPE,A.STATE ");
        sqlSb.append(" ,A.STATE_DATE,A.SYSTME_CODE,A.ROUTE_ID ");
        sqlSb.append(" FROM UOS_PACKAGECATALOG A ");
        sqlSb.append(" WHERE A.STATE='10A' AND A.ROUTE_ID=1 ");
        if(!systemCode.equals("FLOWPLAT")){
        	 sqlSb.append(" AND A.SYSTME_CODE = '"+systemCode+"'");
        }
        if(areaIds!=null){
        	sqlSb.append(" AND A.AREAID IN ("+areaIds+")" );
        }
        sqlSb.append(" ORDER BY A.PATHCODE");
        List<PackageCatalogDto> packageCatalogDtos = queryList(PackageCatalogDto.class, sqlSb.toString(),  new Object[]{});
        return packageCatalogDtos.toArray(new PackageCatalogDto[]{});
	}

	private static final String QUERY_PACKAGE ="SELECT DISTINCT PACK.PACKAGEID,PACK.CATALOGID,PACK.NAME,PACK.STATE,PACK.STATEDATE,"
			+ " PACK.DESCRIPTION,PACK.OWNERAREAID,PACK.PACKAGE_TYPE,PACK.ROUTE_ID,PACK.EFF_DATE,PACK.EXP_DATE"
			+ " FROM UOS_PACKAGE PACK LEFT JOIN UOS_AREA UA3 ON UA3.AREA_ID = UA3.PARENT_ID "
			+ " LEFT JOIN UOS_AREA UA1 ON UA1.AREA_ID = PACK.OWNERAREAID WHERE PACK.ROUTE_ID = 1";
	@Override
	public PackageDto[] qryPackageByPackageCatalogIds(String packageCatalogIds) {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append(QUERY_PACKAGE);
        sqlSb.append(" AND PACK.STATE='10A'");
        
        if(packageCatalogIds!=null){
        	sqlSb.append(" AND PACK.CATALOGID IN ("+packageCatalogIds+")" );
        }
        List<PackageDto> packageCatalogDtos = queryList(PackageDto.class, sqlSb.toString(),  new Object[]{});
		return packageCatalogDtos.toArray(new PackageDto[]{});
	}

	private static final String BY_PACKAGE_ID=" AND PACK.PACKAGEID = ?";
	@Override
	public PackageDto qryPackageById(Long packageId) {
		return queryObject(PackageDto.class, QUERY_PACKAGE+BY_PACKAGE_ID, packageId);
	}

	private static final String INSERT_PACKAGE_CATALOG="INSERT INTO UOS_PACKAGECATALOG(CATALOGID,AREAID,CATALOGNAME,PARENTID,PATHCODE," +
			"PATH_NAME,PACKAGECATALOGTYPE,STATE,STATE_DATE,SYSTME_CODE)" +
			"VALUES(?,?,?,?,?,?,?,?,?,?)";
	@Override
	public void addPackageCatalog(PackageCatalogDto dto) {
		Object[] args = new Object[] {dto.getCatalogId(),dto.getAreaId(),dto.getCatalogName(),dto.getParentId(),dto.getPathCode()
				,dto.getPathName(),dto.getPackageCatalogType(),dto.getState(),dto.getStateDate(),dto.getSystemCode()};
		saveOrUpdate(buildMap(INSERT_PACKAGE_CATALOG, args));
	}

	private static final String UPDATE_PACKAGE_CATALOG = "UPDATE UOS_PACKAGECATALOG SET CATALOGNAME =  ? WHERE CATALOGID =? ";
	@Override
	public void updatePackageCatalog(PackageCatalogDto dto) {
		Object[] args = new Object[] {dto.getCatalogName(),dto.getCatalogId()};
		saveOrUpdate(buildMap(UPDATE_PACKAGE_CATALOG, args));
	}

	private static final String DELETE_PACKAGE_CATALOG = "UPDATE UOS_PACKAGECATALOG SET STATE =  ?, STATE_DATE = ? WHERE CATALOGID =?";
	@Override
	public void deletePackageCatalog(PackageCatalogDto dto) {
		Object[] args = new Object[] {dto.getState(),dto.getStateDate(),dto.getCatalogId()};
		saveOrUpdate(buildMap(DELETE_PACKAGE_CATALOG, args));
	}

	private static final String INSERT_PACKAGE = "INSERT INTO UOS_PACKAGE(PACKAGEID,CATALOGID,NAME,STATE,STATEDATE,DESCRIPTION,OWNERAREAID,PACKAGE_TYPE,EFF_DATE,EXP_DATE)" +
			"VALUES(?,?,?,?,?,?,?,?,?,?)";
	@Override
	public void addPackage(PackageDto dto) {
		Object[] args = new Object[] {dto.getPackageId(),dto.getCatalogId(),dto.getName(),dto.getState(),dto.getStateDate()
				,dto.getDescription(),dto.getOwnerAreaId(),dto.getPackageType(),dto.getEffDate(),dto.getExpDate()};
		saveOrUpdate(buildMap(INSERT_PACKAGE, args));
	}

	private static final String UPDATE_PACKAGE = "UPDATE UOS_PACKAGE SET NAME = ?,EFF_DATE =?,EXP_DATE =? WHERE PACKAGEID= ? ";
	@Override
	public void updatePackage(PackageDto dto) {
		Object[] args = new Object[] {dto.getName(),dto.getEffDate(),dto.getExpDate(),dto.getPackageId()};
		saveOrUpdate(buildMap(UPDATE_PACKAGE, args));
	}

	private static final String DELETE_PACKAGE = "UPDATE UOS_PACKAGE SET STATE = ?,STATEDATE = ? WHERE PACKAGEID= ? ";
	@Override
	public void deletePackage(PackageDto dto) {
		Object[] args = new Object[] {dto.getState(),dto.getStateDate(),dto.getPackageId()};
		saveOrUpdate(buildMap(DELETE_PACKAGE, args));
	}
	@Override
	public PackageDto[] qryPackageByName(Map<String, Object> map) {
		StringBuffer sqlSb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
        sqlSb.append(QUERY_PACKAGE);
        sqlSb.append(" AND PACK.STATE='10A'");
        if(map!=null){
        	sqlSb.append(" AND PACK.NAME LIKE '%"+StringHelper.valueOf(map.get("name"))+"%'" );
        	
        	if (null != map.get("userAreaId")
    				&& !"".equals(map.get("userAreaId"))) {
    			String userAreaId = StringHelper.valueOf(map.get("userAreaId")); 
    			if(userAreaId != null){
        			sqlSb.append(" AND  PACK.OWNERAREAID in (").append(userAreaId);
        			AreaDto areaDto = areaDAO.findAreaByAreaId(LongHelper.valueOf(userAreaId));
        			AreaDto[] areas = areaDAO.findAreasByPathCode(areaDto.getPathCode());
        			for(AreaDto area:areas){
        				if(!userAreaId.equals(StringHelper.valueOf(area.getAreaId()))){
        					sqlSb.append(",").append(area.getAreaId());
        				}
        			}
        			sqlSb.append(") ");
    			}
    		}
        }
        
        List<PackageDto> packageCatalogDtos = queryList(PackageDto.class, sqlSb.toString(),  params.toArray(new Object[]{}));
		return packageCatalogDtos.toArray(new PackageDto[]{});
	}
	@Override
	public List<PackageDto> qryAllPackages() {
		return queryList(PackageDto.class, QUERY_PACKAGE);
	}

}
