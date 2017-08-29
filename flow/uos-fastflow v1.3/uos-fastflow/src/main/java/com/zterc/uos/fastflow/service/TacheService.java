package com.zterc.uos.fastflow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.StaticCacheHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.specification.TacheDAO;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.TacheCatalogDto;
import com.zterc.uos.fastflow.dto.specification.TacheDto;


/**
 * （定义）环节定义操作类
 * 
 * @author gong.yi
 *
 */
public class TacheService {

	public static final String TACHE_DEF_CACHE = "TACHE_DEF_CACHE";

	private TacheDAO tacheDAO;

	public void setTacheDAO(TacheDAO tacheDAO) {
		this.tacheDAO = tacheDAO;
	}

	/**
	 * （缓存）根据环节id查找环节定义
	 * 
	 * @param tacheId
	 * @return
	 */
	public TacheDto queryTache(Long tacheId) {
		TacheDto tacheDto = getCachedTacheDef(tacheId);
		if (tacheDto == null
				&& !FastflowConfig.loadStaticCache) {
			tacheDto = tacheDAO.queryTacheById(tacheId);
			setCachedTacheDef(tacheId, tacheDto);
			setCachedTacheDefByCode(tacheDto.getTacheCode(), tacheDto);
		}
		return tacheDto;
	}

	private void setCachedTacheDefByCode(String tacheCode, TacheDto tacheDto) {
		StaticCacheHelper.set(TACHE_DEF_CACHE, tacheCode, tacheDto);
	}

	private TacheDto getCachedTacheDefByCode(String tacheCode) {
		return (TacheDto) StaticCacheHelper
				.get(TACHE_DEF_CACHE, tacheCode);
	}
	
	private TacheDto getCachedTacheDef(Long tacheId) {
		return (TacheDto) StaticCacheHelper
				.get(TACHE_DEF_CACHE, tacheId.toString());
	}

	private void setCachedTacheDef(Long tacheId, TacheDto tacheDto) {
		StaticCacheHelper.set(TACHE_DEF_CACHE, tacheId.toString(), tacheDto);
	}

	public PageDto qryTaches(Map<String, Object> params) {
		return tacheDAO.qryTaches(params);
	}

	public String qryTacheCatalogTree(Map<String, Object> params) {
		return tacheDAO.qryTacheCatalogTree(params);
	}

	public TacheDto addTache(TacheDto dto) {
		return tacheDAO.addTache(dto);
	}

	public void modTache(TacheDto dto) {
		tacheDAO.modTache(dto);
	}

	public void delTache(TacheDto dto) {
		tacheDAO.delTache(dto);
	}

	public TacheCatalogDto addTacheCatalog(TacheCatalogDto dto) {
		return tacheDAO.addTacheCatalog(dto);
	}

	public void modTacheCatalog(TacheCatalogDto dto) {
		tacheDAO.modTacheCatalog(dto);
	}

	public void delTacheCatalog(TacheCatalogDto dto) {
		tacheDAO.delTacheCatalog(dto);
	}

	public PageDto qryTachesByReturnReasonId(String returnReasonId) {
		return tacheDAO.qryTachesByReturnReasonId(returnReasonId);
	}
	
	@SuppressWarnings("unchecked")
	public void loadAllTache(){
		Map<String,Object> map = new HashMap<String, Object>();
		PageDto page = tacheDAO.qryTaches(map);
		List<TacheDto> taches = (List<TacheDto>) page.getRows();
		for(TacheDto tacheDto:taches){
			setCachedTacheDef(tacheDto.getId(), tacheDto);
			setCachedTacheDefByCode(tacheDto.getTacheCode(), tacheDto);
		}
	}

	/**
	 * 根据环节编码查询环节
	 * @param tacheCode
	 * @return
	 */
	public TacheDto queryTacheByCode(String tacheCode) {
		TacheDto tacheDto = getCachedTacheDefByCode(tacheCode);
		if (tacheDto == null
				&& !FastflowConfig.loadStaticCache) {
			tacheDto = tacheDAO.queryTacheByCode(tacheCode);
			setCachedTacheDefByCode(tacheCode, tacheDto);
		}
		return tacheDto;
	}
}
