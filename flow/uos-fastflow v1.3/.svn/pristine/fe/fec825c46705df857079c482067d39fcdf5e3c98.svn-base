package com.zterc.uos.fastflow.service;

import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.StaticCacheHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.specification.ProcessPackageDAO;
import com.zterc.uos.fastflow.dto.specification.PackageCatalogDto;
import com.zterc.uos.fastflow.dto.specification.PackageDto;

/**
 * （定义）流程定义操作类
 * 
 * @author gong.yi
 *
 */
public class ProcessPackageService {
	public static final String PROCESS_PACKAGE_CACHE = "PROCESS_PACKAGE_CACHE";
	private ProcessPackageDAO processPackageDAO;
	
	public void setProcessPackageDAO(ProcessPackageDAO processPackageDAO) {
		this.processPackageDAO = processPackageDAO;
	}

	public PackageCatalogDto[] qryPackageCatalogByAreaIdAndSystemCode(String areaIds,String systemCode){
		return processPackageDAO.qryPackageCatalogByAreaIdAndSystemCode(areaIds, systemCode);
	}
	
	public PackageDto[] qryPackageByPackageCatalogIds(String packageCatalogIds){
		return processPackageDAO.qryPackageByPackageCatalogIds(packageCatalogIds);
	}
	
	public PackageDto qryPackageById(Long packageId){
		PackageDto packageDto = getCachePackage(packageId);
		if(packageDto == null
				&& !FastflowConfig.loadStaticCache){
			packageDto = processPackageDAO.qryPackageById(packageId);
			setCachePackage(packageDto);
		}
		return packageDto;
	}

	private void setCachePackage(PackageDto packageDto) {
		StaticCacheHelper.set(PROCESS_PACKAGE_CACHE,
				StringHelper.valueOf(packageDto.getPackageId()), packageDto);
	}

	private PackageDto getCachePackage(
			Long packageId) {
		return (PackageDto) StaticCacheHelper.get(
				PROCESS_PACKAGE_CACHE, StringHelper.valueOf(packageId));
	}
	
	public void addPackageCatalog(PackageCatalogDto dto){
		processPackageDAO.addPackageCatalog(dto);
	}
	
	public void updatePackageCatalog(PackageCatalogDto dto){
		processPackageDAO.updatePackageCatalog(dto);
	}
	
	public void deletePackageCatalog(PackageCatalogDto dto){
		processPackageDAO.deletePackageCatalog(dto);
	}
	
	public void addPackage(PackageDto dto){
		processPackageDAO.addPackage(dto);
	}
	
	public void updatePackage(PackageDto dto){
		processPackageDAO.updatePackage(dto);
	}
	
	public void deletePackage(PackageDto dto){
		processPackageDAO.deletePackage(dto);
	}

	public PackageDto[] qryPackageByName(Map<String, Object> map) {
		return processPackageDAO.qryPackageByName(map);
	}
	
	public void loadAllProcessPackage(){
		List<PackageDto> list = processPackageDAO.qryAllPackages();
		for(PackageDto packageDto:list){
			setCachePackage(packageDto);
		}
	}
}
