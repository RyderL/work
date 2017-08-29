package com.ztesoft.uosflow.web.service.returnreason;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonCatalogDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonConfigDto;
import com.zterc.uos.fastflow.dto.specification.ReturnReasonDto;
import com.zterc.uos.fastflow.service.ReturnReasonService;
import com.ztesoft.uosflow.dubbo.inf.manager.FlowManagerInf;

/**
 * 
 * 异常原因service实现
 * @author zhou.yanfang
 *
 */
@Service("ReturnReasonServ")
@Lazy(true)
public class ReturnReasonServImpl implements ReturnReasonServ {
	private static Logger logger = LoggerFactory.getLogger(ReturnReasonServImpl.class);
	@Autowired
	private ReturnReasonService returnReasonService;
	
	@Autowired
	@Qualifier("flowManagerService")
	private FlowManagerInf flowManagerService;
	
	private PlatformTransactionManager txManager = null;
	private DefaultTransactionDefinition def = null;
	private TransactionStatus status = null;
	
	@Override
	public String qryReturnReasonsByTacheId(Map<String,Object>  map) throws Exception {
		try{
			PageDto pageDto = new PageDto();
			if(map!=null&&map.get("tacheId")!=null){
				String tacheId = (String) map.get("tacheId");
				pageDto = returnReasonService.qryReturnReasonsByTacheId(tacheId);
			}
			String  result = GsonHelper.toJson(pageDto);
			logger.debug("result:"+result);
			return result;
		}catch(Exception ex){
			logger.error("returnReasonService-qryReturnReasonsByTacheId error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String qryReturnReasonCatalogTree(Map<String,Object>  map) throws Exception {
		try{
			String systemCode = StringHelper.valueOf(map.get("systemCode"));
			String result= returnReasonService.qryReturnReasonCatalogTree(systemCode);
			logger.debug("result:"+result);
			return result;
		}catch(Exception ex){
			logger.error("returnReasonService-qryReturnReasonCatalogTree error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String addReturnReasonCatalog(Map<String,Object>  map) throws Exception {
		try{
			ReturnReasonCatalogDto dto = new ReturnReasonCatalogDto();
			dto.setReasonCatalogName(StringHelper.valueOf(map.get("reasonCatalogName")));
			Timestamp curTime = DateHelper.getTimeStamp();
			dto.setCreateDate(curTime);
			dto.setStateDate(curTime);
			dto.setComments("");
			dto.setSystemCode(StringHelper.valueOf(map.get("systemCode")));
			dto.setParentReasonCatalog(LongHelper.valueOf(map.get("parentReasonCatalogId")));
			returnReasonService.addReturnReasonCatalog(dto);
			Long catalogId = dto.getId();
			Map<String, Long> result = new HashMap<String, Long>();
			result.put("catalogId", catalogId);
			logger.debug("result:"+GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
		}catch(Exception ex){
			logger.error("returnReasonService-addReturnReasonCatalog error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String modReturnReasonCatalog(Map<String,Object>  map) throws Exception {
		try{
			ReturnReasonCatalogDto dto = new ReturnReasonCatalogDto();
			dto.setReasonCatalogName(StringHelper.valueOf(map.get("reasonCatalogName")));
			dto.setId(LongHelper.valueOf(map.get("id")));
			returnReasonService.modReturnReasonCatalog(dto);
			return  "{\"isSuccess\":true}";
		}catch(Exception ex){
			logger.error("returnReasonService-modReturnReasonCatalog error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String delReturnReasonCatalog(Map<String,Object>  map) throws Exception {
		try{
			ReturnReasonCatalogDto dto = new ReturnReasonCatalogDto();
			dto.setId(LongHelper.valueOf(map.get("id")));
			returnReasonService.delReturnReasonCatalog(dto);
			return  "{\"isSuccess\":true}";
		}catch(Exception ex){
			logger.error("returnReasonService-delReturnReasonCatalog error:",ex);
			throw ex;
		}
	}
	@Override
	public String qryReturnReasons(Map<String,Object>  map) throws Exception {
		try{
			PageDto pageDto = returnReasonService.qryReturnReasons(map);
			String result= GsonHelper.toJson(pageDto);
			logger.debug("result:"+result);
			return result;
		}catch(Exception ex){
			logger.error("returnReasonService-qryReturnReasons error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String addReturnReason(Map<String,Object>  map) throws Exception {
		try{
			ReturnReasonDto dto = new ReturnReasonDto();
			dto.setReasonCode(StringHelper.valueOf(map.get("reasonCode")));
			dto.setReasonCatalogId(LongHelper.valueOf(map.get("reasonCatalogId")));
			dto.setReasonType(StringHelper.valueOf(map.get("reasonType")));
			dto.setReturnReasonName(StringHelper.valueOf(map.get("returnReasonName")));
			dto.setComments(StringHelper.valueOf(map.get("comments")));
			dto.setRecommendMeans(StringHelper.valueOf(map.get("recommendMeans")));
			Timestamp curTime = DateHelper.getTimeStamp();
			dto.setCreateDate(curTime);
			dto.setStateDate(curTime);
			returnReasonService.addReturnReason(dto);
			Long returnReasonId = dto.getId();
			Map<String, Long> result = new HashMap<String, Long>();
			result.put("returnReasonId", returnReasonId);
			logger.debug("result:"+GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
		}catch(Exception ex){
			logger.error("returnReasonService-addReturnReason error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String modReturnReason(Map<String,Object>  map) throws Exception {
		try{
			ReturnReasonDto dto = new ReturnReasonDto();
			dto.setReasonCatalogId(LongHelper.valueOf(map.get("reasonCatalogId")));
			dto.setReturnReasonName(StringHelper.valueOf(map.get("returnReasonName")));
			dto.setStateDate(DateHelper.getTimeStamp());
			dto.setId(LongHelper.valueOf(map.get("id")));
			returnReasonService.modReturnReason(dto);
			return "{\"isSuccess\":true}";
		}catch(Exception ex){
			logger.error("returnReasonService-modReturnReason error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String delReturnReason(Map<String,Object>  map) throws Exception {
		try{
			ReturnReasonDto dto = new ReturnReasonDto();
			dto.setId(LongHelper.valueOf(map.get("id")));
			returnReasonService.delReturnReason(dto);
			return "{\"isSuccess\":true}";
		}catch(Exception ex){
			logger.error("returnReasonService-delReturnReason error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String addTacheReturnReason(Map<String,Object>  map) throws Exception {
		try{
			boolean isSuccess = returnReasonService.addTacheReturnReason(map);
			Map<String, Boolean> result = new HashMap<String, Boolean>();
			result.put("isSuccess", isSuccess);
			logger.debug("result:"+GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
		}catch(Exception ex){
			logger.error("returnReasonService-addTacheReturnReason error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String delTacheReturnReason(Map<String,Object>  map) throws Exception {
		try{
			boolean isSuccess = returnReasonService.delTacheReturnReason(map);
			Map<String, Boolean> result = new HashMap<String, Boolean>();
			result.put("isSuccess", isSuccess);
			logger.debug("result:"+GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
		}catch(Exception ex){
			logger.error("returnReasonService-delTacheReturnReason error:",ex);
			throw ex;
		}
	}
	@Override
	public String qryTacheReturnReasons(Map<String,Object>  map) throws Exception {
		try{
			PageDto pageDto = returnReasonService.qryTacheReturnReasons(map);
			String  result = GsonHelper.toJson(pageDto);
			logger.debug("result:"+result);
			return result;
		}catch(Exception ex){
			logger.error("returnReasonService-qryTacheReturnReasons error:",ex);
			throw ex;
		}
	}
	@Override
	public String qryReturnReasonConfigs(Map<String, Object> map)throws Exception {
		try{
			//modify by bobping
			String packageDefineCode = StringHelper.valueOf(map.get("packageDefineCode"));
			PageDto pageDto = returnReasonService.qryReturnReasonConfigs(packageDefineCode);
			String  result = GsonHelper.toJson(pageDto);
			logger.info("result:"+result);
			return result;
		}catch(Exception ex){
			logger.error("returnReasonService-qryReturnReasonConfigs error:",ex);
			throw ex;
		}
	}
	@Override
	@Transactional
	public String saveReturnReasonConfigs(Map<String, Object> map)throws Exception {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		try{
			String packageDefineId = StringHelper.valueOf(map.get("packageDefineId"));
			Long areaId = LongHelper.valueOf(map.get("areaId"));
			//获取流程模板定义code --add by bobping on 2017-3-9
			String packageDefineCode = StringHelper.valueOf(map.get("packageDefineCode"));
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> rows = (List<Map<String, Object>>) map.get("rows");
			List<ReturnReasonConfigDto> list = new ArrayList<ReturnReasonConfigDto>();
			for(int i=0,len=rows.size();i<len;i++){
				Map<String,Object> row = rows.get(i);
				ReturnReasonConfigDto dto = new ReturnReasonConfigDto();
				dto.setReasonId(LongHelper.valueOf(row.get("reasonId")));
				dto.setTacheId(LongHelper.valueOf(row.get("tacheId")));
				dto.setTargetTacheId(LongHelper.valueOf(row.get("targetTacheId")));
				dto.setPackageDefineId(packageDefineId);
				dto.setPackageDefineCode(packageDefineCode);
				dto.setAutoToManual(StringHelper.valueOf(row.get("autoToManual")));
				dto.setStartMode(StringHelper.valueOf(row.get("startMode")));
				//dto.setCreateDate(new Date());
				dto.setAreaId(areaId);
				list.add(dto);
			}
			//入参改成流程模板编码 --modify by bobping on 2017-3-9
//			returnReasonService.saveReturnReasonConfigs(packageDefineId, list);
			returnReasonService.saveReturnReasonConfigs(packageDefineCode, list);
			txManager.commit(status);
			flowManagerService.refreshReturnReasonConfigCache();
		}catch(Exception ex){
			txManager.rollback(status);
			logger.error("returnReasonService-saveReturnReasonConfigs error:",ex);
			throw ex;
		}
		return "{\"isSuccess\":true}";
	}
	@Override
	@Transactional
	public String modTacheReturnReason(Map<String, Object> map) throws Exception {
		try{
			boolean isSuccess = returnReasonService.modTacheReturnReason(map);
			Map<String, Boolean> result = new HashMap<String, Boolean>();
			result.put("isSuccess", isSuccess);
			logger.debug("result:"+GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
		}catch(Exception ex){
			logger.error("returnReasonService-modTacheReturnReason error:",ex);
			throw ex;
		}
	}
	@Override
	public String hasActiveReturnReasonsByTacheId(Map<String, Object> map) throws Exception {
		try{
			boolean isHas = returnReasonService.hasActiveReturnReasonsByTacheId(map);
			Map<String, Boolean> result = new HashMap<String, Boolean>();
			result.put("isHas", isHas);
			logger.debug("result:"+GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
		}catch(Exception ex){
			logger.error("returnReasonService-qryActiveReturnReasonsByTacheId error:",ex);
			throw ex;
		}
	}
}
