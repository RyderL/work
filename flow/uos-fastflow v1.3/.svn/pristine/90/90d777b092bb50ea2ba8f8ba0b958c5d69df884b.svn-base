package com.zterc.uos.fastflow.dao.specification;

import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.TacheCatalogDto;
import com.zterc.uos.fastflow.dto.specification.TacheDto;

/**
 * 环节接口
 * @author zhou.yanfang
 *
 */
public interface TacheDAO {
	/**
	 * 获取环节信息
	 * @param params 查询条件
	 * @return
	 * @throws Exception
	 */
	public PageDto qryTaches(Map<String,Object> params);
	/**
	 * 获取环节的目录信息 --easyui tree data
	 * @param params 查询条件
	 * @return
	 * @throws Exception
	 */
	public String qryTacheCatalogTree(Map<String,Object> params);

	/**
	 *  增加环节
	 * @param dto
	 * @return
	 */
	public TacheDto addTache(TacheDto dto);
	/**
	 * 修改环节
	 * @param  增加环节
	 */
	public void modTache(TacheDto dto);
	/**
	 * 删除环节
	 * @param  增加环节
	 */
	public void delTache(TacheDto dto);
	/**
	 * 新增环节目录
	 * @param dto
	 * @return
	 */
	public TacheCatalogDto addTacheCatalog(TacheCatalogDto dto);
	/**
	 * 修改环节目录
	 * @param dto
	 */
	public void modTacheCatalog(TacheCatalogDto dto);
	/**
	 * 删除环节目录
	 * @param dto
	 */
	public void delTacheCatalog(TacheCatalogDto dto);
	/**
	 * 根据异常原因ID获取环节信息
	 * @param returnReasonId
	 * @return
	 */
	public PageDto qryTachesByReturnReasonId(String returnReasonId);
	
	public TacheDto queryTacheById(Long tacheId);
	
	public String getTacheCodeByTacheId(Long tacheId);
	public TacheDto queryTacheByCode(String tacheCode);
}
