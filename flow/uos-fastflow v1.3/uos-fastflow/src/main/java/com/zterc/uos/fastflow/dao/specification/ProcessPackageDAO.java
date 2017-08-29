package com.zterc.uos.fastflow.dao.specification;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.PackageCatalogDto;
import com.zterc.uos.fastflow.dto.specification.PackageDto;

public interface ProcessPackageDAO {
	/**
	 * ����areaIds��systemCode��ȡ���̶����Ŀ¼�б�
	 * @param areaIds
	 * @param systemCode
	 * @return
	 */
	public PackageCatalogDto[] qryPackageCatalogByAreaIdAndSystemCode(String areaIds,String systemCode);
	
	/**
	 * �������̶���Ŀ¼id�б��ȡ����ģ����б�
	 * @param packageCatalogIds
	 * @return
	 */
	public PackageDto[] qryPackageByPackageCatalogIds(String packageCatalogIds);
	
	/**
	 * ��������ģ��id��������ģ����Ϣ
	 * @param params
	 * @return
	 */
	public PackageDto qryPackageById(Long packageId);
	
	/**
	 * �������Ŀ¼
	 * @param PackageCatalogDto
	 */
	public void addPackageCatalog(PackageCatalogDto dto);
	
	/**
	 * ��������Ŀ¼
	 * @param dto
	 */
	public void updatePackageCatalog(PackageCatalogDto dto);
	
	/**
	 * ɾ������Ŀ¼
	 * @param dto
	 */
	public void deletePackageCatalog(PackageCatalogDto dto);
	
	/**
	 * �������ģ��
	 * @param PackageDto
	 */
	public void addPackage(PackageDto dto);
	
	/**
	 * ��������ģ��
	 * @param dto
	 */
	public void updatePackage(PackageDto dto);
	
	/**
	 * ɾ������ģ��
	 * @param dto
	 */
	public void deletePackage(PackageDto dto);

	/**
	 * �������Ʋ�ѯ����ģ��
	 * @param map
	 * @return
	 */
	public PackageDto[] qryPackageByName(Map<String, Object> map);

	/**
	 * ��ѯ��������ģ��
	 * @return
	 */
	public List<PackageDto> qryAllPackages();
}
