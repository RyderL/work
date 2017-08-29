package com.zterc.uos.fastflow.service;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.zterc.uos.base.helper.StaticCacheHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.specification.ReturnReasonDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonCatalogDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonConfigDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonDto;

import net.sf.ehcache.CacheException;

/**
 * �����壩�˵�ԭ�������
 * 
 * @author gong.yi
 *
 */
public class ReturnReasonService {
	public static final String RETURN_REASON_CACHE = "RETURN_REASON_CACHE";
	private static Logger logger = Logger.getLogger(ReturnReasonService.class);

	private ReturnReasonDAO returnReasonDAO;

	public void setReturnReasonDAO(ReturnReasonDAO returnReasonDAO) {
		this.returnReasonDAO = returnReasonDAO;
	}

	public PageDto qryReturnReasonsByTacheId(String tacheId) {
		return returnReasonDAO.qryReturnReasonsByTacheId(tacheId);
	}

	public String qryReturnReasonCatalogTree(String systemCode) {
		return returnReasonDAO.qryReturnReasonCatalogTree(systemCode);
	}

	public ReturnReasonCatalogDto addReturnReasonCatalog(
			ReturnReasonCatalogDto dto) {
		return returnReasonDAO.addReturnReasonCatalog(dto);
	}

	public void modReturnReasonCatalog(ReturnReasonCatalogDto dto) {
		returnReasonDAO.modReturnReasonCatalog(dto);
	}

	public void delReturnReasonCatalog(ReturnReasonCatalogDto dto) {
		returnReasonDAO.delReturnReasonCatalog(dto);
	}

	public PageDto qryReturnReasons(Map<String, Object> params) {
		return returnReasonDAO.qryReturnReasons(params);
	}

	public ReturnReasonDto addReturnReason(ReturnReasonDto dto) {
		return returnReasonDAO.addReturnReason(dto);
	}

	public void modReturnReason(ReturnReasonDto dto) {
		returnReasonDAO.modReturnReason(dto);
	}

	public void delReturnReason(ReturnReasonDto dto) {
		returnReasonDAO.delReturnReason(dto);
	}

	public boolean addTacheReturnReason(Map<String, Object> params) {
		return returnReasonDAO.addTacheReturnReason(params);
	}

	public boolean delTacheReturnReason(Map<String, Object> params) {
		return returnReasonDAO.delTacheReturnReason(params);
	}

	public PageDto qryTacheReturnReasons(Map<String, Object> params) {
		return returnReasonDAO.qryTacheReturnReasons(params);
	}

	//modify by bobping
	public PageDto qryReturnReasonConfigs(String packageDefineCode) {
		return returnReasonDAO.qryReturnReasonConfigs(packageDefineCode);
	}
	//modify by bobping
	public void saveReturnReasonConfigs(String packageDefineCode,
			List<ReturnReasonConfigDto> list) {
		returnReasonDAO.saveReturnReasonConfigs(packageDefineCode, list);
	}

	/**
	 * (����) �����쳣���������ģ���ѯ�쳣����
	 * modify by bobping ����ģ��id�ĳ�����ģ������ȡ�쳣ԭ������
	 * @param reasonCode
	 * @param packageDefineId
	 * @return
	 */
	public ReturnReasonConfigDto getTargerActivityIdByReasonCode(
			String reasonCode, String packageDefineCode,String tacheId,String areaId) {
		if(!FastflowConfig.isCacheModel){
			return returnReasonDAO.getTargerActivityIdByReasonCode(
					reasonCode, packageDefineCode,tacheId,areaId);
		}
		ReturnReasonConfigDto reasonConfigDto = getCachedReturnReasonConfig(
				reasonCode, packageDefineCode,tacheId,areaId);
		if(!(reasonConfigDto != null)){
			logger.info("����packageDefineCode:" + packageDefineCode + " tacheId��" + tacheId + " areaId:" + areaId
					+ " reasonCode:" + reasonCode + " ��ehCache�Ҳ����쳣����");
		}
		if (reasonConfigDto == null
				&& !FastflowConfig.loadStaticCache) {
			reasonConfigDto = returnReasonDAO.getTargerActivityIdByReasonCode(
					reasonCode, packageDefineCode,tacheId,areaId);
			setCachedReturnReasonConfig(reasonCode, packageDefineCode,tacheId,areaId,
					reasonConfigDto);
		}
		//modify by bobping
		if(reasonConfigDto == null && !"0".equals(packageDefineCode)){
			//û��ȡ����ǰģ��id����ѯ�Ƿ���Ĭ�ϵ��쳣ԭ�������ڴ��Ͽ�ͨŲ�����ľ����ݣ�
			reasonConfigDto = getTargerActivityIdByReasonCode(reasonCode, "0", tacheId, areaId);
		}
		return reasonConfigDto;
	}
	//modify by bobping ����ģ��id�ĳ�����ģ������ȡ�쳣ԭ������
	private void setCachedReturnReasonConfig(String reasonCode,
			String packageDefineCode,String tacheId,String areaId, ReturnReasonConfigDto reasonConfigDto) {
		StaticCacheHelper.set(RETURN_REASON_CACHE, packageDefineCode + "_"
				+ reasonCode+"_"+tacheId+"_"+areaId, reasonConfigDto);
	}

	private ReturnReasonConfigDto getCachedReturnReasonConfig(
			String reasonCode, String packageDefineCode,String tacheId,String areaId) {
		return (ReturnReasonConfigDto) StaticCacheHelper.get(
				RETURN_REASON_CACHE, packageDefineCode + "_" + reasonCode+"_"+tacheId+"_"+areaId);
	}

	public boolean modTacheReturnReason(Map<String, Object> params) {
		return returnReasonDAO.modTacheReturnReason(params);
	}

	public boolean hasActiveReturnReasonsByTacheId(
			Map<String, Object> params) {
		return returnReasonDAO.hasActiveReturnReasonsByTacheId(params);
	}

	public void loadAllReturnReasonConfig(){
		List<ReturnReasonConfigDto> configlist = returnReasonDAO.qryAllReturnReasonConfig();
		for(ReturnReasonConfigDto dto:configlist){
			//modify by bobping ����ģ��id�ĳ�����ģ������ȡ�쳣ԭ������
			setCachedReturnReasonConfig(dto.getReasonCode(), dto.getPackageDefineCode(),
					StringHelper.valueOf(dto.getTacheId()), StringHelper.valueOf(dto.getAreaId()), dto);
		}
	}

	/**
	 * ��������ģ��id��ѯ�������õ��쳣ԭ��
	 * @param oldProcessDefId
	 * @return
	 */
	public List<ReturnReasonConfigDto> qryReturnReasonConfigsByDefId(
			String oldProcessDefId) {
		return returnReasonDAO.qryReturnReasonConfigsByDefId(oldProcessDefId);
	}
	
	
	public static void main(String[] args) throws CacheException, MalformedURLException {
		
//		Configuration configuration = ConfigurationFactory.parseConfiguration(new File("D:\\dev2\\uosFlow2\\uos-fastflow\\src\\main\\resources\\ehcache\\ehcache.xml"));
//
//		CacheManager cacheManager = new CacheManager(configuration);
//		EhCacheCacheManager springEhCache = new EhCacheCacheManager(cacheManager);
//		StaticCacheHelper.setCacheManager(springEhCache);
//
//		for(int i = 0 ;i <200 ;i++){
//			StaticCacheHelper.set(RETURN_REASON_CACHE, i+"", "���ǵ�"+i);
//		}
//		System.out.println(StaticCacheHelper.get(RETURN_REASON_CACHE, "0"));
//		System.out.println(StaticCacheHelper.get(RETURN_REASON_CACHE, "1"));
	}
	
	
	
	
}
