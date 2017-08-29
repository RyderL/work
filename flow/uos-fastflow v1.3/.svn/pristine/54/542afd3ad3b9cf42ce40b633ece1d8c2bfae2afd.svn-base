package com.zterc.uos.fastflow.dao.specification;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.PackageCatalogDto;
import com.zterc.uos.fastflow.dto.specification.PackageDto;

public interface ProcessPackageDAO {
	/**
	 * 根据areaIds和systemCode获取流程定义的目录列表
	 * @param areaIds
	 * @param systemCode
	 * @return
	 */
	public PackageCatalogDto[] qryPackageCatalogByAreaIdAndSystemCode(String areaIds,String systemCode);
	
	/**
	 * 根据流程定义目录id列表获取流程模板的列表
	 * @param packageCatalogIds
	 * @return
	 */
	public PackageDto[] qryPackageByPackageCatalogIds(String packageCatalogIds);
	
	/**
	 * 根据流程模板id查找流程模板信息
	 * @param params
	 * @return
	 */
	public PackageDto qryPackageById(Long packageId);
	
	/**
	 * 添加流程目录
	 * @param PackageCatalogDto
	 */
	public void addPackageCatalog(PackageCatalogDto dto);
	
	/**
	 * 更新流程目录
	 * @param dto
	 */
	public void updatePackageCatalog(PackageCatalogDto dto);
	
	/**
	 * 删除流程目录
	 * @param dto
	 */
	public void deletePackageCatalog(PackageCatalogDto dto);
	
	/**
	 * 添加流程模板
	 * @param PackageDto
	 */
	public void addPackage(PackageDto dto);
	
	/**
	 * 更新流程模板
	 * @param dto
	 */
	public void updatePackage(PackageDto dto);
	
	/**
	 * 删除流程模板
	 * @param dto
	 */
	public void deletePackage(PackageDto dto);

	/**
	 * 根据名称查询流程模板
	 * @param map
	 * @return
	 */
	public PackageDto[] qryPackageByName(Map<String, Object> map);

	/**
	 * 查询所有流程模板
	 * @return
	 */
	public List<PackageDto> qryAllPackages();
}
