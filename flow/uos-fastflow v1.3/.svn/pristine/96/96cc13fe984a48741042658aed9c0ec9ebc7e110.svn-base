package com.ztesoft.uosflow.web.service.interactive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zterc.uos.base.helper.GsonHelper;
import com.ztesoft.uosflow.web.inf.InfConstants;
import com.ztesoft.uosflow.web.inf.client.IClient;
import com.ztesoft.uosflow.web.inf.model.RequestDto;
import com.ztesoft.uosflow.web.inf.model.ResponseDto;
import com.ztesoft.uosflow.web.inf.model.server.AreaInfDto;
import com.ztesoft.uosflow.web.inf.model.server.InfDto;
@Service("InteractiveServ")
public class InteractiveServImpl implements InteractiveServ {
	private static Logger logger = LoggerFactory.getLogger(InteractiveServImpl.class);
	
	@Autowired
	private IClient iClient;
	
	@Override
	public String getAreaTree(Map<String,Object> map) throws Exception {
		RequestDto cDto = new RequestDto();
		cDto.setCommandCode(InfConstants.COMMANDCODE_QRY_AREA);
		ResponseDto retDto = iClient.sendMessage(cDto);
		String ret = null;
		if(retDto.isDealFlag()){
			ret = this.transAreaTree(retDto.getDatas());
		}else{//应该要做成抛异常保存到交互接口表里
			logger.error("--------"+InfConstants.COMMANDCODE_QRY_AREA+"失败");
		}
		return ret;
	}
	
	private String transAreaTree(List<? extends InfDto> datas){
		JsonArray list = new JsonArray();
		if(datas!=null&&datas.size()>0){
			Map<String , JsonObject> parentMap = new HashMap<String, JsonObject>();
			for(int i=0,j=datas.size();i<j;i++){
				AreaInfDto dto = (AreaInfDto) datas.get(i);
				String catalogPathCode = dto.getPathCode();
				String pathCode = catalogPathCode;
				if (catalogPathCode.lastIndexOf(".") >= 0) { //不是最高层地区
					pathCode = catalogPathCode.substring(0, catalogPathCode.lastIndexOf("."));
				}
				//给节点赋值
				JsonObject area = dto.getTreeJsonObject();
				 //节点的层次
                if (parentMap.containsKey(pathCode)) {
                	JsonObject parent = (JsonObject) parentMap.get(pathCode);
                	JsonArray children = parent.getAsJsonArray("children");
                	parent.addProperty("state", "closed");
                	if(children==null){
                		children = new JsonArray();
                		parent.add("children", children);
                	}
					children.add(area);
                }else {
                	list.add(area);
                }
                parentMap.put(catalogPathCode, area);
			}
		}
		return GsonHelper.toJson(list);
	}

	@Override
	public String getSystemTree(Map<String, Object> map) throws Exception {
		RequestDto cDto = new RequestDto();
		cDto.setCommandCode(InfConstants.COMMANDCODE_QRY_SYS);
		ResponseDto retDto = iClient.sendMessage(cDto);
		String ret = null;
		if(retDto.isDealFlag()){
			ret=this.transArrsTree(retDto.getDatas());
		}else{//应该要做成抛异常保存到交互接口表里
			logger.error("--------"+InfConstants.COMMANDCODE_QRY_SYS+"失败");
		}
		logger.info("ret:"+ret);
		return ret;
	}
	
	private String transArrsTree(List<? extends InfDto> datas){
		JsonArray list = new JsonArray();
		if(datas!=null&&datas.size()>0){
			for(int i=0,j=datas.size();i<j;i++){
				InfDto dto = datas.get(i);
				JsonObject jsonObj = dto.getTreeJsonObject();
				list.add(jsonObj);
			}
		}
		return GsonHelper.toJson(list);
	}

	@Override
	public String getOrgTree(Map<String, Object> map) throws Exception {
		RequestDto cDto = new RequestDto();
		cDto.setCommandCode(InfConstants.COMMANDCODE_QRY_ORG);
		ResponseDto retDto = iClient.sendMessage(cDto);
		String ret = null;
		if(retDto.isDealFlag()){
			ret=this.transArrsTree(retDto.getDatas());
		}else{//应该要做成抛异常保存到交互接口表里
			logger.error("--------"+InfConstants.COMMANDCODE_QRY_ORG+"失败");
		}
		logger.info("ret:"+ret);
		return ret;
	}

	@Override
	public String getJobTree(Map<String, Object> map) throws Exception {
		RequestDto cDto = new RequestDto();
		cDto.setOrgId(MapUtils.getString(map, "orgId", null));
		cDto.setCommandCode(InfConstants.COMMANDCODE_QRY_JOB);
		ResponseDto retDto = iClient.sendMessage(cDto);
		String ret = null;
		if(retDto.isDealFlag()){
			ret=this.transArrsTree(retDto.getDatas());
		}else{//应该要做成抛异常保存到交互接口表里
			logger.error("--------"+InfConstants.COMMANDCODE_QRY_JOB+"失败");
		}
		logger.info("ret:"+ret);
		return ret;
	}

	@Override
	public String getStaffTree(Map<String, Object> map) throws Exception {
		RequestDto cDto = new RequestDto();
		cDto.setOrgId(MapUtils.getString(map, "orgId", null));
		cDto.setCommandCode(InfConstants.COMMANDCODE_QRY_STA);
		cDto.setPageIndex(MapUtils.getString(map, "pageIndex", "1"));
		cDto.setPageSize(MapUtils.getString(map, "pageSize", "1000"));
		ResponseDto retDto = iClient.sendMessage(cDto);
		String ret = null;
		if(retDto.isDealFlag()){
			ret=this.transArrsTree(retDto.getDatas());
		}else{//应该要做成抛异常保存到交互接口表里
			logger.error("--------"+InfConstants.COMMANDCODE_QRY_STA+"失败");
		}
		logger.info("ret:"+ret);
		return ret;
	}

	@Override
	public String getBizObjTree(Map<String, Object> map) throws Exception {
		RequestDto cDto = new RequestDto();
		cDto.setCommandCode(InfConstants.COMMANDCODE_QRY_BIZ);
		ResponseDto retDto = iClient.sendMessage(cDto);
		String ret = null;
		if(retDto.isDealFlag()){
			ret=this.transArrsTree(retDto.getDatas());
		}else{//应该要做成抛异常保存到交互接口表里
			logger.error("--------"+InfConstants.COMMANDCODE_QRY_BIZ+"失败");
		}
		logger.info("ret:"+ret);
		return ret;
	}

}
