package com.ztesoft.uosflow.web.service.tache;

import java.sql.Timestamp;
import java.util.HashMap;
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
import com.zterc.uos.fastflow.dto.specification.TacheCatalogDto;
import com.zterc.uos.fastflow.dto.specification.TacheDto;
import com.zterc.uos.fastflow.service.TacheService;
import com.ztesoft.uosflow.dubbo.inf.manager.FlowManagerInf;

/**
 * 
 * 环节service实现
 * 
 * @author zhou.yanfang
 * 
 */
@Service("TacheServ")
@Lazy(true)
public class TacheServImpl implements TacheServ {
	private static Logger logger = LoggerFactory.getLogger(TacheServImpl.class);
	@Autowired
	private TacheService tacheService;
	
	@Autowired
	@Qualifier("flowManagerService")
	private FlowManagerInf flowManagerService;
	
	private PlatformTransactionManager txManager = null;
	private DefaultTransactionDefinition def = null;
	private TransactionStatus status = null;

	@Override
	public String qryTaches(Map<String, Object> map) throws Exception {
		try {
			PageDto pageDto = tacheService.qryTaches(map);
			String result = GsonHelper.toJson(pageDto);
			logger.debug("result:" + result);
			return result;
		} catch (Exception ex) {
			logger.error("tacheService-qryTaches error:", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String qryTacheCatalogTree(Map<String, Object> map) throws Exception {
		try {
			String result = tacheService.qryTacheCatalogTree(map);
			logger.debug("result:" + result);
			return result;
		} catch (Exception ex) {
			logger.error("tacheService-qryTacheCatalogTree error:", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String addTache(Map<String, Object> map) throws Exception {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		TacheDto dto = new TacheDto();
		try {
			dto.setTacheCode(StringHelper.valueOf(map.get("tacheCode")));
			dto.setTacheName(StringHelper.valueOf(map.get("tacheName")));
			dto.setTacheCatalogId(LongHelper.valueOf(map.get("tacheCatalogId")));
			dto.setState("10A");
			dto.setIsAuto(Integer.valueOf(StringHelper.valueOf(map
					.get("isAuto"))));
			dto.setTacheType(map.get("tacheType").toString());// 存在""的情况，用StringHelper.valueOf会变成null
			dto.setPackageDefineCodes(map.get("packageDefineCodes").toString());
			dto.setCreateDate(DateHelper.getTimeStamp());
			dto.setStateDate(DateHelper.getTimeStamp());
			dto.setEffDate(Timestamp.valueOf(map.get("effDate").toString()));
			dto.setExpDate(Timestamp.valueOf(map.get("expDate").toString()));
			tacheService.addTache(dto);
			txManager.commit(status);
			flowManagerService.refreshTacheDefCache();
		} catch (Exception ex) {
			logger.error("tacheService-addTache error:", ex);
			txManager.rollback(status);
			throw ex;
		}
		return GsonHelper.toJson(dto);
	}

	@Override
	@Transactional
	public String modTache(Map<String, Object> map) throws Exception {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		try {
			TacheDto dto = new TacheDto();
			dto.setId(LongHelper.valueOf(map.get("id")));
			dto.setTacheName(StringHelper.valueOf(map.get("tacheName")));
			dto.setTacheCatalogId(LongHelper.valueOf(map.get("tacheCatalogId")));
			dto.setIsAuto(Integer.valueOf(StringHelper.valueOf(map.get("isAuto"))));
			dto.setPackageDefineCodes(map.get("packageDefineCodes").toString());
			dto.setStateDate(DateHelper.getTimeStamp());
			dto.setEffDate(Timestamp.valueOf(map.get("effDate").toString()));
			dto.setExpDate(Timestamp.valueOf(map.get("expDate").toString()));
			tacheService.modTache(dto);
			txManager.commit(status);
			flowManagerService.refreshTacheDefCache();
		} catch (Exception ex) {
			logger.error("tacheService-modTache error:", ex);
			txManager.rollback(status);
			throw ex;
		}
		return "{\"isSuccess\":true}";
	}

	@Override
	@Transactional
	public String delTache(Map<String, Object> map) throws Exception {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		try {
			TacheDto dto = new TacheDto();
			dto.setId(LongHelper.valueOf(map.get("id")));
			dto.setStateDate(DateHelper.getTimeStamp());
			tacheService.delTache(dto);
			txManager.commit(status);
			flowManagerService.refreshTacheDefCache();
		} catch (Exception ex) {
			logger.error("tacheService-delTache error:", ex);
			txManager.rollback(status);
			throw ex;
		}
		return "{\"isSuccess\":true}";
	}

	@Override
	@Transactional
	public String addTacheCatalog(Map<String, Object> map) throws Exception {
		try {
			TacheCatalogDto dto = new TacheCatalogDto();
			dto.setTacheCatalogName(StringHelper.valueOf(map
					.get("tacheCatalogName")));
			Timestamp curTime = DateHelper.getTimeStamp();
			dto.setCreateDate(curTime);
			dto.setStateDate(curTime);
			dto.setComments("");
			dto.setSystemCode(StringHelper.valueOf(map.get("systemCode")));
			dto.setParentTacheCatalogId(LongHelper.valueOf(map
					.get("parentTacheCatalogId")));
			tacheService.addTacheCatalog(dto);
			Long catalogId = dto.getId();
			Map<String, Long> result = new HashMap<String, Long>();
			result.put("catalogId", catalogId);
			logger.debug("result:" + GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
		} catch (Exception ex) {
			logger.error("tacheService-addTacheCatalog error:", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String modTacheCatalog(Map<String, Object> map) throws Exception {
		try {
			TacheCatalogDto dto = new TacheCatalogDto();
			dto.setTacheCatalogName(StringHelper.valueOf(map
					.get("tacheCatalogName")));
			dto.setId(LongHelper.valueOf(map.get("id")));
			tacheService.modTacheCatalog(dto);
			return "{\"isSuccess\":true}";
		} catch (Exception ex) {
			logger.error("tacheService-modTacheCatalog error:", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String delTacheCatalog(Map<String, Object> map) throws Exception {
		try {
			TacheCatalogDto dto = new TacheCatalogDto();
			dto.setId(LongHelper.valueOf(map.get("id")));
			tacheService.delTacheCatalog(dto);
			return "{\"isSuccess\":true}";
		} catch (Exception ex) {
			logger.error("tacheService-delTacheCatalog error:", ex);
			throw ex;
		}
	}

	@Override
	public String qryTachesByReturnReasonId(Map<String, Object> map)
			throws Exception {
		try {
			PageDto pageDto = new PageDto();
			if (map != null && map.get("returnReasonId") != null) {
				String returnReasonId = (String) map.get("returnReasonId");
				pageDto = tacheService
						.qryTachesByReturnReasonId(returnReasonId);
			}
			String result = GsonHelper.toJson(pageDto);
			logger.debug("result:" + result);
			return result;
		} catch (Exception ex) {
			logger.error("tacheService-qryTaches error:", ex);
			throw ex;
		}
	}

}
