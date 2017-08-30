package com.zterc.uos.fastflow.dao.specification.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.fastflow.dao.specification.FormManagerDAO;
import com.zterc.uos.fastflow.dto.specification.*;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author zheng.fengting 
 *
 * 2017年8月16日
 */
public class FormManagerDAOImpl extends AbstractDAOImpl implements FormManagerDAO {

    @Override
    public List<ComboboxDto> qryCombox(Map<String, Object> params) {
        List<Object> keys = new ArrayList<Object>();
        String codeColumn = params.get("codeColumn").toString();
        String tableName = params.get("tableName").toString();
        String nameColumn = params.get("nameColumn").toString();
        String whereColumnName = null;
        if(params.get("whereColumnName") != null){
            whereColumnName = params.get("whereColumnName").toString();
        }
        String whereColumnValue = null;
        if(params.get("whereColumnValue") != null){
            whereColumnValue = params.get("whereColumnValue").toString();
        }
        StringBuffer qrySql = new StringBuffer("SELECT ");
        qrySql.append(codeColumn).append(" AS CODE,")
                .append(nameColumn).append(" AS NAME")
                .append(" FROM ").append(tableName)
                .append(" WHERE ROUTE_ID = 1 ");

        if(StringUtils.isNotEmpty(whereColumnName) && StringUtils.isNotEmpty(whereColumnValue)){
            qrySql.append(" AND ").append(whereColumnName).append(" = ? ");
            keys.add(whereColumnValue);
        }

        return queryList(ComboboxDto.class,qrySql.toString(),keys.toArray(new Object[]{}));
    }

    /**
     * 查询页面模板
     * create by liang.zhenyi
     */
	@Override
	public PageDto qryPageTemplate(Map<String, Object> params) {
		PageDto pageDto = new PageDto();
		StringBuffer qrySql = new StringBuffer(
				"SELECT *" + " FROM UOS_PAGE_TEMPLATE WHERE ROUTE_ID=1");
		List<Object> keys = new ArrayList<>();
		List<PageTemplateDto> list = queryList(PageTemplateDto.class, qrySql.toString(), keys.toArray(new Object[]{}));
		pageDto.setRows(list);
		pageDto.setTotal(list.size());
		return pageDto;
	}

	private static final String COUNT_TEMPLATE = "SELECT COUNT(*) NUM FROM UOS_PAGE_TEMPLATE WHERE TEMPLATE_CODE=?";
	private static final String INSERT_TEMPLATE = "INSERT INTO UOS_PAGE_TEMPLATE(ID, TEMPLATE_CODE, TEMPLATE_NAME, " +
            "CREATE_DATE, COMMENTS, ROUTE_ID, TENANT_ID) VALUES(?, ?, ?, ?, ?, ?, ?)";

    /**
     * 添加页面模板
     * create by liang.zhenyi
     */
	@Override
	public void addTemplate(PageTemplateDto templateDto) {
	    Object count = queryCount(COUNT_TEMPLATE, templateDto.getTemplateCode());
	    if(IntegerHelper.valueOf(count)>0){
	        templateDto.setId(-1L);
        }else {
            templateDto.setId(SequenceHelper.getId("UOS_PAGE_TEMPLATE"));
            Object[] args = new Object[]{templateDto.getId(), templateDto.getTemplateCode(), templateDto.getTemplateName(),
                    templateDto.getCreateDate(), templateDto.getComments(), templateDto.getRouteId(), templateDto.getTenantId()};
            saveOrUpdate(buildMap(INSERT_TEMPLATE, args));
        }
    }

	private static final String UPDATE_TEMPLATE = "UPDATE UOS_PAGE_TEMPLATE SET TEMPLATE_CODE=?, TEMPLATE_NAME=?," +
            "COMMENTS=? WHERE ID=?";

    /**
     * 修改页面模板数据
     * create by liang.zhenyi
     */
	@Override
	public void modTemplate(PageTemplateDto templateDto) {
	    Object[] args= new Object[]{templateDto.getTemplateCode(), templateDto.getTemplateName(),
                                    templateDto.getComments(),templateDto.getId()};
	    saveOrUpdate(buildMap(UPDATE_TEMPLATE, args));
	}


	private static final String DEL_TEMPLATE = "DELETE FROM UOS_PAGE_TEMPLATE WHERE ID = ?";

    /**
     * 删除页面模板数据
     * create by liang.zhenyi
     */
	@Override
	public void delTemplate(PageTemplateDto templateDto) {
	    Object[] args=new Object[]{templateDto.getId()};
	    saveOrUpdate(buildMap(DEL_TEMPLATE, args));
	}

    /**
     * 查询模板明细
     * create by liang.zhenyi
     */
    @Override
    public PageDto qryTemplateDetail(Map<String, Object> params) {
        PageDto pageDto = new PageDto();
        StringBuffer qrySql = new StringBuffer(
                "SELECT *" + " FROM UOS_PAGE_TEMPLATE_DETAIL WHERE ROUTE_ID=1 AND TEMPLATE_CODE=?");
        List<Object> keys = new ArrayList<>();
        keys.add(params.get("templateCode"));
        List<PageTemplateDetailDto> list = queryList(PageTemplateDetailDto.class, qrySql.toString(), keys.toArray(new Object[]{}));
        pageDto.setRows(list);
        pageDto.setTotal(list.size());
        return pageDto;
    }

    private static final String INSERT_TEMPLATE_DETAIL="INSERT INTO UOS_PAGE_TEMPLATE_DETAIL(TEMPLATE_DETAIL_ID, TEMPLATE_CODE, ELEMENT_CODE, PAGE_TITLE, PAGE_CODE, " +
            "LOCATE_ROW, LOCATE_COLUMN, ALIGN, IS_RET, IS_MUST, IS_ENABLED, IS_DISPlAY, IS_INIT, COMMENTS, ROUTE_ID, TENANT_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    @Override
    public void addTemplateDetail(PageTemplateDetailDto templateDetailDto) {
            templateDetailDto.setTemplateDetailId(SequenceHelper.getId("UOS_PAGE_TEMPLATE_DETAIL"));
            Object[] args = new Object[]{templateDetailDto.getTemplateDetailId(), templateDetailDto.getTemplateCode(),templateDetailDto.getElementCode(),
                    templateDetailDto.getPageTitle(), templateDetailDto.getPageCode(), templateDetailDto.getLocateRow(),templateDetailDto.getLocateColumn(),
                    templateDetailDto.getAlign(), templateDetailDto.getIsRet(), templateDetailDto.getIsMust(), templateDetailDto.getIsEnabled(),
                    templateDetailDto.getIsDisplay(), templateDetailDto.getIsInit(), templateDetailDto.getComments(), templateDetailDto.getRouteId(), templateDetailDto.getTenantId()};

            saveOrUpdate(buildMap(INSERT_TEMPLATE_DETAIL, args));
        }

    /**
     * 查询模板适用规则
     * create by liang.zhenyi
     */
    @Override
    public List<PageTemplateRuleDto> qryTemplateRule(Map<String, Object> params) {
        StringBuffer qrySql = new StringBuffer(
                "SELECT *" + " FROM UOS_PAGE_TEMPLATE_RULE WHERE ROUTE_ID=1 AND RULE_ID=?");
        List<Object> keys = new ArrayList<>();
        keys.add(params.get("ruleId"));
        return queryList(PageTemplateRuleDto.class, qrySql.toString(), keys.toArray(new Object[]{}));
    }

    /**
     *查询模板使用规则(在页面显示的)
     * create by liang.zhenyi
     */
    @Override
    public List<PageTemplateRuleDto> qryPageTemplateRule(Map<String, Object> params){

        StringBuffer qrySql = new StringBuffer("SELECT UOS_PAGE_TEMPLATE_RULE.RULE_ID AS RULE_ID, " +
                "(CASE UOS_PACKAGE.NAME WHEN NULL THEN '全部' ELSE UOS_PACKAGE.NAME END) PACKAGE_CODE, " +
                "(CASE UOS_TACHE_DEF.TACHE_NAME WHEN NULL THEN '全部' ELSE UOS_TACHE_DEF.TACHE_NAME END) TACHE_CODE, " +
                "(CASE UOS_PAGE_FUNC.FUNC_NAME  WHEN NULL THEN '全部' ELSE UOS_PAGE_FUNC.FUNC_NAME END) FUNC_CODE, " +
                "UOS_PAGE_TEMPLATE_RULE.FRAME_PAGE_URL AS FRAME_PAGE_URL, UOS_PAGE_TEMPLATE_RULE.TEMPLATE_CODE AS TEMPLATE_CODE, " +
                "UOS_PAGE_TEMPLATE_RULE.CREATE_DATE AS CREATE_DATE, UOS_PAGE_TEMPLATE_RULE.MODIFY_DATE AS MODIFY_DATE, " +
                "UOS_PAGE_TEMPLATE_RULE.ROUTE_ID AS ROUTE_ID, UOS_PAGE_TEMPLATE_RULE.TENANT_ID AS TENANT_ID " +
                "FROM UOS_PAGE_TEMPLATE_RULE " +
                "LEFT JOIN UOS_PAGE_FUNC ON UOS_PAGE_TEMPLATE_RULE.FUNC_CODE=UOS_PAGE_FUNC.FUNC_CODE " +
                "LEFT JOIN UOS_PACKAGE ON UOS_PAGE_TEMPLATE_RULE.PACKAGE_CODE=UOS_PACKAGE.PACKAGEID " +
                "LEFT JOIN UOS_TACHE_DEF ON UOS_PAGE_TEMPLATE_RULE.TACHE_CODE=UOS_TACHE_DEF.ID " +
                "WHERE UOS_PAGE_TEMPLATE_RULE.ROUTE_ID=1 AND UOS_PAGE_TEMPLATE_RULE.TEMPLATE_CODE=?");

        List<Object> keys=new ArrayList<>();
        keys.add(params.get("templateCode"));
        return queryList(PageTemplateRuleDto.class, qrySql.toString(), keys.toArray(new Object[]{}));
    }


    private static final String INSERT_TEMPLATE_RULE = "INSERT INTO UOS_PAGE_TEMPLATE_RULE(RULE_ID, PACKAGE_CODE," +
            " TACHE_CODE, FUNC_CODE, FRAME_PAGE_URL, TEMPLATE_CODE, CREATE_DATE, MODIFY_DATE, " +
            "ROUTE_ID, TENANT_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * 添加模板适用规则
     * create by liang.zhenyi
     */
    @Override
    public void addTemplateRule(PageTemplateRuleDto templateRuleDto) {
        templateRuleDto.setRuleId(SequenceHelper.getId("UOS_PAGE_TEMPLATE_RULE"));
        Object[] args = new Object[] {templateRuleDto.getRuleId(), templateRuleDto.getPackageCode(), templateRuleDto.getTacheCode(),
                templateRuleDto.getFuncCode(), templateRuleDto.getFramePageUrl(), templateRuleDto.getTemplateCode(), templateRuleDto.getCreateDate(),
                templateRuleDto.getModifyDate(), templateRuleDto.getRouteId(), templateRuleDto.getTenantId()};
        saveOrUpdate(buildMap(INSERT_TEMPLATE_RULE, args));
    }

    private static final  String UPDATE_TEMPLATE_RULE = "UPDATE UOS_PAGE_TEMPLATE_RULE SET PACKAGE_CODE=?, TACHE_CODE=?, FUNC_CODE=?, " +
            "FRAME_PAGE_URL=?, MODIFY_DATE=? WHERE RULE_ID=?";

    /**
     * 修改模板适用规则
     * create by liang.zhenyi
     */
    @Override
    public void modTemplateRule(PageTemplateRuleDto templateRuleDto) {
	    Object[] args= new Object[]{templateRuleDto.getPackageCode(), templateRuleDto.getTacheCode(), templateRuleDto.getFuncCode(),
        templateRuleDto.getFramePageUrl(), templateRuleDto.getModifyDate(), templateRuleDto.getRuleId()};
	    saveOrUpdate(buildMap(UPDATE_TEMPLATE_RULE, args));
    }

    private static final String DEL_TEMPLATE_RULE = "DELETE FROM UOS_PAGE_TEMPLATE_RULE WHERE RULE_ID=?";

    /**
     * 删除模板适用规则
     * create by liang.zhenyi
     */
    @Override
    public void delTemplateRule(PageTemplateRuleDto templateRuleDto) {
        Object[] args=new Object[]{templateRuleDto.getRuleId()};
        saveOrUpdate(buildMap(DEL_TEMPLATE_RULE, args));
    }

    /**
     * 查询模板约束条件
     * create by liang.zhenyi
     */
    @Override
    public PageDto qryPageConstraint(Map<String, Object> params) {
	    PageDto pageDto = new PageDto();
        StringBuffer qrySql = new StringBuffer(
                "SELECT *" + " FROM UOS_PAGE_CONSTRAINT WHERE ROUTE_ID=1 AND TEMPLATE_CODE=?");
        List<Object> keys = new ArrayList<>();
        keys.add(params.get("templateCode"));
        List<PageConstraintDto> list = queryList(PageConstraintDto.class, qrySql.toString(), keys.toArray(new Object[]{}));
        pageDto.setRows(list);
        pageDto.setTotal(list.size());
        return pageDto;
    }

    private static final String INSERT_PAGE_CONSTRAINT = "INSERT INTO UOS_PAGE_CONSTRAINT (CONS_ID, CONS_TYPE, TEMPLATE_CODE," +
            "PAGE_CODE, CONS_CONDITION, RESULT_VALUE, RESULT_CLASS, RESULT_METHOD, ROUTE_ID, TENANT_ID)" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    /**
     * 添加模板约束条件
     * create by liang.zhenyi
     */
    @Override
    public void addPageConstraint(PageConstraintDto pageConstraintDto) {
        pageConstraintDto.setConsId(SequenceHelper.getId("UOS_PAGE_CONSTRAINT"));
        Object[] args=new Object[]{pageConstraintDto.getConsId(), pageConstraintDto.getConsType(), pageConstraintDto.getTemplateCode(),
        pageConstraintDto.getPageCode(), pageConstraintDto.getConsCondition(), pageConstraintDto.getResultValue(), pageConstraintDto.getResultClass(),
        pageConstraintDto.getResultMethod(), pageConstraintDto.getRouteId(), pageConstraintDto.getTenantId()};
        saveOrUpdate(buildMap(INSERT_PAGE_CONSTRAINT, args));
    }

    private static final String UPDATE_PAGE_CONSTRAINT = "UPDATE UOS_PAGE_CONSTRAINT SET CONS_TYPE=?, PAGE_CODE=?, CONS_CONDITION=?, RESULT_VALUE=?, RESULT_CLASS=?, RESULT_METHOD=? " +
            "WHERE CONS_ID=?";

    /**
     * 修改模板约束条件
     * create by liang.zhenyi
     */
    @Override
    public void modPageConstraint(PageConstraintDto pageConstraintDto) {
        Object[] args=new Object[]{pageConstraintDto.getConsType(), pageConstraintDto.getPageCode(), pageConstraintDto.getConsCondition(),
        pageConstraintDto.getResultValue(), pageConstraintDto.getResultClass(), pageConstraintDto.getResultMethod(), pageConstraintDto.getConsId()};
        saveOrUpdate(buildMap(UPDATE_PAGE_CONSTRAINT, args));
    }

    private static final String DEL_PAGE_CONSTRAINT = "DELETE FROM UOS_PAGE_CONSTRAINT WHERE CONS_ID=?";

    /**
     * 删除模板约束条件
     * create by liang.zhenyi
     */
    @Override
    public void delPageConstraint(PageConstraintDto pageConstraintDto) {
        Object[] args = new Object[]{pageConstraintDto.getConsId()};
        saveOrUpdate(buildMap(DEL_PAGE_CONSTRAINT, args));
    }

    /**
     * 查询依赖控件的元素类型,及下拉框类型加载数据所用的方法
     * create by liang.zhenyi
     */
    @Override
    public List<PageElementDto> qryWidgetType(Map<String, Object> params) {
        String pageCode = params.get("pageCode").toString();
        StringBuffer qrySql = new StringBuffer("SELECT UOS_PAGE_ELEMENT.ELEMENT_CODE, ELEMENT_TYPE, OPTION_DATA_CLASS, OPTION_DATA_METHOD " +
                "FROM UOS_PAGE_TEMPLATE_DETAIL, UOS_PAGE_ELEMENT " +
                "WHERE PAGE_CODE=? AND " +
                "UOS_PAGE_TEMPLATE_DETAIL.ELEMENT_CODE=UOS_PAGE_ELEMENT.ELEMENT_CODE");

        List<Object> keys = new ArrayList<Object>();
        keys.add(pageCode);
        return queryList(PageElementDto.class, qrySql.toString(), keys.toArray(new Object[]{}));
    }


}
