package com.zterc.uos.fastflow.dao.specification;

import java.util.List;
import java.util.Map;

import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonCatalogDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonConfigDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonDto;

/**
 * 异常原因接口
 * 
 * @author zhou.yanfang
 * 
 */
public interface ReturnReasonDAO {
	/**
	 * 根据环节ID获取异常原因信息
	 * 
	 * @param params
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	public PageDto qryReturnReasonsByTacheId(String tacheId);

	/**
	 * 获取异常原因的目录信息 --easyui tree data
	 * 
	 * @return
	 * @throws Exception
	 */
	public String qryReturnReasonCatalogTree(String systemCode);

	/**
	 * 新增异常原因目录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ReturnReasonCatalogDto addReturnReasonCatalog(
			ReturnReasonCatalogDto dto);

	/**
	 * 修改异常原因目录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void modReturnReasonCatalog(ReturnReasonCatalogDto dto);

	/**
	 * 删除异常原因目录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void delReturnReasonCatalog(ReturnReasonCatalogDto dto);

	/**
	 * 获取异常原因信息
	 * 
	 * @param params
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	public PageDto qryReturnReasons(Map<String, Object> params);

	/**
	 * 增加异常原因
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ReturnReasonDto addReturnReason(ReturnReasonDto dto);

	/**
	 * 修改异常原因
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void modReturnReason(ReturnReasonDto dto);

	/**
	 * 删除异常原因
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void delReturnReason(ReturnReasonDto dto);

	/**
	 * 增加环节和异常原因的关联关系
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean addTacheReturnReason(Map<String, Object> params);

	/**
	 * 删除环节和异常原因的关联关系
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean delTacheReturnReason(Map<String, Object> params);

	/**
	 * 查询环节和异常原因的关联关系
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public PageDto qryTacheReturnReasons(Map<String, Object> params);

	/**
	 * 查询异常原因配置（流程）
	 * 
	 * @param packageDefineId
	 * @return
	 * @throws Exception
	 */
	public PageDto qryReturnReasonConfigs(String packageDefineId);

	/**
	 * 保存异常原因配置（流程）
	 * 
	 * @param packageDefineId
	 * @param list
	 * @throws Exception
	 */
	public void saveReturnReasonConfigs(String packageDefineId,
			List<ReturnReasonConfigDto> list);

	/**
	 * 根据异常编码和流程模版查询异常配置
	 * modify by bobping 流程模板id改成流程模板编码获取异常原因配置
	 * @param reasonCode
	 * @param packageDefineId
	 * @param tacheId
	 * @return
	 */
	public ReturnReasonConfigDto getTargerActivityIdByReasonCode(
			String reasonCode, String packageDefineCode,String tacheId,String areaId);
	/**
	 * 修改环节和异常原因的关联关系
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean modTacheReturnReason(Map<String, Object> params);

	/**
	 * 根据环节id查询是否有有效异常原因
	 * @param params
	 * @return
	 */
	public boolean hasActiveReturnReasonsByTacheId(
			Map<String, Object> params);

	/**
	 * 查询所有有效异常原因
	 * @return
	 */
	public List<ReturnReasonConfigDto> qryAllReturnReasonConfig();

	/**
	 * 根据流程模板id查询配置的异常原因
	 * @param oldProcessDefId
	 * @return
	 */
	public List<ReturnReasonConfigDto> qryReturnReasonConfigsByDefId(
			String oldProcessDefId);

}
