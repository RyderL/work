package com.ztesoft.uosflow.web.service.formmanager;

import java.sql.SQLException;
import java.util.*;

import com.zterc.uos.base.helper.*;
import com.zterc.uos.fastflow.dto.specification.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.zterc.uos.base.jdbc.Page;
import com.ztesoft.uosflow.web.service.error.ExceptionHisServ;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zterc.uos.fastflow.service.FormManagerService;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

@Service("FormManagerServ")
public class FormManagerServImpl implements FormManagerServ {
	@Autowired
	private FormManagerService formManagerService;
	Logger logger = LoggerFactory.getLogger(FormManagerServImpl.class);

	@Override
	public String qryCombox(Map<String, Object> map) throws SQLException {
		List<ComboboxDto> comboboxList = formManagerService.qryCombox(map);
		String result = GsonHelper.toJson(comboboxList);
		return result; 

	}

	@Override
	@Transactional
	public String qryTemplate(Map<String, Object> map) throws SQLException {
		try {
			PageDto pageDto = formManagerService.qryPageTemplate(map);
			String result = GsonHelper.toJson(pageDto);
			logger.debug("result: "+result);
			return result;
		}catch (Exception e){
			logger.error("formManagerService-qryTemplate error: ", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public String addTemplate(Map<String, Object> map) throws SQLException {
		try {
			PageTemplateDto templateDto = new PageTemplateDto();
			templateDto.setTemplateCode(StringHelper.valueOf(map.get("templateCode")));
			templateDto.setTemplateName(StringHelper.valueOf(map.get("templateName")));
			templateDto.setCreateDate(DateHelper.getTimeStamp());
			templateDto.setComments(StringHelper.valueOf(map.get("comments")));
			templateDto.setRouteId(Integer.parseInt(map.get("routeId").toString()));
			templateDto.setTenantId((StringHelper.valueOf(map.get("tenantId"))));
			formManagerService.addTemplate(templateDto);

			Long templateId = templateDto.getId();
			Map<String, Long> result = new HashMap<>();
			result.put("templateId", templateId);
			logger.debug("result: "+GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
		}catch (Exception e){
			logger.error("formManagerService-addTemplate error: ", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public String modTemplate(Map<String, Object> map) throws SQLException {
		try {
			PageTemplateDto templateDto = new PageTemplateDto();
            templateDto.setTemplateCode(StringHelper.valueOf(map.get("templateCode")));
            templateDto.setTemplateName(StringHelper.valueOf(map.get("templateName")));
            templateDto.setComments(StringHelper.valueOf(map.get("comments")));
            templateDto.setId(LongHelper.valueOf(map.get("id")));
            formManagerService.modTemplate(templateDto);
            return "{\"isSuccess\":true}";
		}catch (Exception e){
			logger.error("formManagerService-modTemplate error: ", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public String delTemplate(Map<String, Object> map) throws SQLException {
		try {
			PageTemplateDto templateDto = new PageTemplateDto();
	    	templateDto.setId(LongHelper.valueOf(map.get("id")));
	    	formManagerService.delTemplate(templateDto);
	    	return "{\"isSuccess\":true}";
		}catch (Exception ex){
			logger.error("formManagerService-delTemplate error: ", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String qryTemplateDetail(Map<String, Object> map) throws SQLException {
	    try {
			PageDto pageDto = formManagerService.qryTemplateDetail(map);
			String result = GsonHelper.toJson(pageDto);
			logger.debug("result: "+result);
			return result;
		}catch (Exception ex){
	    	logger.error("formManagerService-qryTemplateDetail error: ", ex);
	    	throw ex;
		}
	}

	@Override
	@Transactional
	public String addTemplateDetail(Map<String, Object> map) throws SQLException {
	    try {
            PageTemplateDetailDto templateDetailDto = new PageTemplateDetailDto();
            templateDetailDto.setAlign(StringHelper.valueOf(map.get("align")));
            templateDetailDto.setComments(StringHelper.valueOf(map.get("comments")));
            templateDetailDto.setPageCode(StringHelper.valueOf(map.get("pageCode")));
            templateDetailDto.setPageTitle(StringHelper.valueOf(map.get("pageTitle")));
            templateDetailDto.setTemplateCode(StringHelper.valueOf(map.get("templateCode")));
            templateDetailDto.setElementCode(StringHelper.valueOf(map.get("elementName")));
            templateDetailDto.setLocateColumn(IntegerHelper.valueOf(map.get("locateColumn")));
            templateDetailDto.setLocateRow(IntegerHelper.valueOf(map.get("locateRow")));
            templateDetailDto.setIsDisplay(StringHelper.valueOf(map.get("isDisplay")));
            templateDetailDto.setIsEnabled(StringHelper.valueOf(map.get("isEnabled")));
            templateDetailDto.setIsInit(StringHelper.valueOf(map.get("isInit")));
            templateDetailDto.setIsMust(StringHelper.valueOf(map.get("isMust")));
            templateDetailDto.setIsRet(StringHelper.valueOf(map.get("isRet")));
            templateDetailDto.setRouteId(IntegerHelper.valueOf(map.get("routeId")));

            formManagerService.addTemplateDetail(templateDetailDto);
            Map<String, Long> result=new HashMap<>();
            Long templateDetailId= templateDetailDto.getTemplateDetailId();
            result.put("templateDetailId", templateDetailId);
            logger.debug("result: "+GsonHelper.toJson(result));
            return GsonHelper.toJson(result);
        }catch (Exception ex){
            logger.error("formManagerService-addTemplateDetail error: ", ex);
            throw ex;
        }
	}


	@Override
	@Transactional
	public String qryTemplateRule(Map<String, Object> map) throws SQLException {
		try {
			List<PageTemplateRuleDto> templateRuleList= formManagerService.qryTemplateRule(map);
			String result = GsonHelper.toJson(templateRuleList);
			logger.debug("result: " + result);
			return result;
		}catch (Exception ex){
			logger.error("formManagerService-qryTemplateRule error: ", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String addTemplateRule(Map<String, Object> map) throws SQLException {
	    try{
			PageTemplateRuleDto templateRuleDto = new PageTemplateRuleDto();
			templateRuleDto.setFramePageUrl(StringHelper.valueOf(map.get("framePageUrl")));
			templateRuleDto.setFuncCode(StringHelper.valueOf(map.get("funcId")));
			templateRuleDto.setTacheCode(StringHelper.valueOf(map.get("tacheId")));
			templateRuleDto.setPackageCode(StringHelper.valueOf(map.get("packageId")));
			templateRuleDto.setCreateDate(DateHelper.getTimeStamp());
			templateRuleDto.setTemplateCode(StringHelper.valueOf(map.get("templateCode")));
			templateRuleDto.setModifyDate(DateHelper.getTimeStamp());
			templateRuleDto.setRouteId(Integer.parseInt(map.get("routeId").toString()));
			templateRuleDto.setTenantId(StringHelper.valueOf(map.get("tenantId")));
			formManagerService.addTemplateRule(templateRuleDto);
			Map<String, Long> result = new HashMap<>();
			Long ruleId = templateRuleDto.getRuleId();
			result.put("ruleId", ruleId);
			logger.debug("result: "+GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
	    }catch (Exception ex){
	    	logger.error("formManagerService-addTemplateRule error: ", ex);
	    	throw ex;
		}
	}

	@Override
	@Transactional
	public String modTemplateRule(Map<String, Object> map) throws SQLException {
	    try {
	    	PageTemplateRuleDto templateRuleDto = new PageTemplateRuleDto();
	    	templateRuleDto.setRuleId(LongHelper.valueOf(map.get("ruleId")));
	    	templateRuleDto.setPackageCode(StringHelper.valueOf(map.get("packageId")));
	    	templateRuleDto.setTacheCode(StringHelper.valueOf(map.get("tacheId")));
	    	templateRuleDto.setFuncCode(StringHelper.valueOf(map.get("funcId")));
	    	templateRuleDto.setFramePageUrl(StringHelper.valueOf(map.get("framePageUrl")));
	    	templateRuleDto.setCreateDate(DateHelper.parse(map.get("createDate").toString()));
	    	templateRuleDto.setModifyDate(DateHelper.getTimeStamp());

	    	formManagerService.modTemplateRule(templateRuleDto);
	    	return "{\"isSuccess\":true}";
		}catch (Exception ex){
	    	logger.error("formManagerService-modTemplateRule error: ",ex);
	    	throw ex;
		}
	}

	@Override
	@Transactional
	public String delTemplateRule(Map<String, Object> map) throws SQLException {
	    try {
	    	PageTemplateRuleDto templateRuleDto = new PageTemplateRuleDto();
	    	templateRuleDto.setRuleId(LongHelper.valueOf(map.get("ruleId")));
	    	formManagerService.delTemplateRule(templateRuleDto);
	    	return "{\"isSuccess\":true}";
		}catch (Exception ex){
	    	logger.error("formManagerService-delTemplateRule error: ", ex);
	    	throw ex;
		}
	}

	@Override
	@Transactional
	public String qryPageConstraint(Map<String, Object> map) throws SQLException {
		try {
			PageDto pageDto = formManagerService.qryPageConstraint(map);
			String result = GsonHelper.toJson(pageDto);
			return result;
		}catch (Exception ex){
			logger.error("formManagerService-qryPageConstraint error: ",ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String addPageConstraint(Map<String, Object> map) throws SQLException {
		try{
			PageConstraintDto pageConstraintDto = new PageConstraintDto();
			pageConstraintDto.setConsType(StringHelper.valueOf(map.get("consType")));
			pageConstraintDto.setTemplateCode(StringHelper.valueOf(map.get("templateCode")));
			pageConstraintDto.setPageCode(StringHelper.valueOf(map.get("pageCode")));
			pageConstraintDto.setConsCondition(StringHelper.valueOf(map.get("consCondition")));
			pageConstraintDto.setResultValue(StringHelper.valueOf(map.get("resultValue")));
			pageConstraintDto.setResultClass(StringHelper.valueOf(map.get("resultClass")));
			pageConstraintDto.setResultMethod(StringHelper.valueOf(map.get("resultMethod")));
			pageConstraintDto.setRouteId(Integer.parseInt(map.get("routeId").toString()));
			pageConstraintDto.setTenantId(StringHelper.valueOf(map.get("tenantId")));

			formManagerService.addPageConstraint(pageConstraintDto);
			Map<String, Long> result = new HashMap<>();
			Long consId = pageConstraintDto.getConsId();
			result.put("consId", consId);
			logger.debug("result: "+GsonHelper.toJson(result));
			return GsonHelper.toJson(result);
		}catch (Exception ex){
			logger.error("formManagerService-addPageConstraint error: ", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String modPageConstraint(Map<String, Object> map) throws SQLException {
		try {
			PageConstraintDto pageConstraintDto = new PageConstraintDto();
			pageConstraintDto.setConsId(LongHelper.valueOf(map.get("consId")));
			pageConstraintDto.setConsType(StringHelper.valueOf(map.get("consType")));
			pageConstraintDto.setPageCode(StringHelper.valueOf(map.get("pageCode")));
			pageConstraintDto.setConsCondition(StringHelper.valueOf(map.get("consCondition")));
			pageConstraintDto.setResultValue(StringHelper.valueOf(map.get("resultValue")));
			pageConstraintDto.setResultClass(StringHelper.valueOf(map.get("resultClass")));
			pageConstraintDto.setResultMethod(StringHelper.valueOf(map.get("resultMethod")));
			formManagerService.modPageConstraint(pageConstraintDto);
			return "{\"isSuccess\":true}";
		}catch (Exception ex){
			logger.error("formManagerService-modPageConstraint error:", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String delPageConstraint(Map<String, Object> map) throws SQLException {
	    try {
	    	PageConstraintDto pageConstraintDto = new PageConstraintDto();
	    	pageConstraintDto.setConsId(LongHelper.valueOf(map.get("consId")));
	    	formManagerService.delPageConstraint(pageConstraintDto);
	    	return "{\"isSuccess\":true}";
		}catch (Exception ex){
	    	logger.error("formManagerService-delPageConstraint error:", ex);
	    	throw ex;
		}
	}

	/**
	 * 查询控件的元素类型
	 * create by liang.zhenyi
	 */
	@Override
    @Transactional
	public String qryWidgetType(Map<String, Object> map) throws SQLException {
		List<PageElementDto> elementList = formManagerService.qryWidgetType(map);
		String result = GsonHelper.toJson(elementList);
		return result;
	}

	/**
	 * 查询页面适用规则
	 * create by liang.zhenyi
	 */
	@Override
	public String qryPageTemplateRule(Map<String, Object> map) throws SQLException{
		List<PageTemplateRuleDto> templateRuleList = formManagerService.qryPageTemplateRule(map);
		String result = GsonHelper.toJson(templateRuleList);
		return result;
	}

	@Override
	@Transactional
	public String getOptiondataByElementCode(Map<String, Object> map) throws SQLException {
		String elementCode = map.get("elementCode").toString();
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("codeColumn","OPTION_VALUE");
		params.put("tableName","UOS_PAGE_OPTION_DATA");
		params.put("nameColumn","OPTION_TEXT");
		params.put("whereColumnName", "ELEMENT_CODE");
		params.put("whereColumnValue", elementCode);
		List<ComboboxDto> comboboxList = formManagerService.qryCombox(params);
		String result = GsonHelper.toJson(comboboxList);
		return result;
	}
}
