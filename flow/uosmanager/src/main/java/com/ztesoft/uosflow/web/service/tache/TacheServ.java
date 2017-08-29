package com.ztesoft.uosflow.web.service.tache;

import java.util.Map;

/**
 * 
 * 环节service接口
 * @author zhou.yanfang
 *
 */
public interface TacheServ {
	public String qryTaches(Map<String,Object>  map)throws Exception;
	public String qryTacheCatalogTree(Map<String,Object>  map)throws Exception;
	public String addTache(Map<String,Object>  map) throws Exception;
	public String modTache(Map<String,Object>  map) throws Exception;
	public String delTache(Map<String,Object>  map) throws Exception;
	public String addTacheCatalog(Map<String,Object>  map) throws Exception;
	public String modTacheCatalog(Map<String,Object>  map) throws Exception;
	public String delTacheCatalog(Map<String,Object>  map) throws Exception;
	public String qryTachesByReturnReasonId(Map<String,Object>  map)throws Exception;
}
