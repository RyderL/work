package com.ztesoft.uosflow.core.dto.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.uosflow.core.cons.InfConstant;
import com.ztesoft.uosflow.core.dto.CommandDto;

public class AddTacheDto extends CommandDto {

	private static final long serialVersionUID = 1L;

	public AddTacheDto() {
		this.setCommandCode("addTache");
	}

	private String tacheCode;//环节编码
	private String tacheName;//环节名称
	private String tacheCatalogId;//环节目录id
	private String tacheType;//环节类型
	private String packageDefineCodes;//环节适用流程模板编码集合
	private Date effDate;
	private Date expDate;
	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}


	public String getTacheCode() {
		return tacheCode;
	}

	public void setTacheCode(String tacheCode) {
		this.tacheCode = tacheCode;
	}

	public String getTacheName() {
		return tacheName;
	}

	public void setTacheName(String tacheName) {
		this.tacheName = tacheName;
	}

	public String getTacheCatalogId() {
		return tacheCatalogId;
	}

	public void setTacheCatalogId(String tacheCatalogId) {
		this.tacheCatalogId = tacheCatalogId;
	}

	public String getTacheType() {
		return tacheType;
	}

	public void setTacheType(String tacheType) {
		this.tacheType = tacheType;
	}

	public String getPackageDefineCodes() {
		return packageDefineCodes;
	}

	public void setPackageDefineCodes(String packageDefineCodes) {
		this.packageDefineCodes = packageDefineCodes;
	}

	@Override
	public void init(Map<String, Object> paramsMap) {
		super.init(paramsMap);
		this.tacheCatalogId = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TACHE_CATALOG_ID));
		this.tacheCode = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TACHE_CODE));
		this.tacheName = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TACHE_NAME));
		this.tacheType = StringHelper.valueOf(paramsMap.get(InfConstant.INF_TACHE_TYPE));
		this.packageDefineCodes = StringHelper.valueOf(paramsMap.get(InfConstant.INF_PACAKAGE_DEFINE_CODES));
		this.effDate = DateHelper.parse(StringHelper.valueOf(paramsMap.get(InfConstant.INF_EFF_DATE)));
		this.expDate = DateHelper.parse(StringHelper.valueOf(paramsMap.get(InfConstant.INF_EXP_DATE)));
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(InfConstant.INF_SERIAL, getSerial());
		map.put(InfConstant.INF_TIME, getTime());
		map.put(InfConstant.INF_FROM, getFrom());
		map.put(InfConstant.INF_TO, getTo());
		map.put(InfConstant.INF_COMMAND_CODE, getCommandCode());
		map.put(InfConstant.INF_AREA_CODE, areaCode);
		map.put(InfConstant.INF_PROCESSINSTANCEID, processInstanceId);
		map.put(InfConstant.INF_TACHE_CATALOG_ID, tacheCatalogId);
		map.put(InfConstant.INF_TACHE_CODE, tacheCode);
		map.put(InfConstant.INF_TACHE_NAME, tacheName);
		map.put(InfConstant.INF_TACHE_TYPE, tacheType);
		map.put(InfConstant.INF_PACAKAGE_DEFINE_CODES, packageDefineCodes);
		map.put(InfConstant.INF_EFF_DATE, effDate);
		map.put(InfConstant.INF_EXP_DATE, expDate);
		return map;
	}
}
