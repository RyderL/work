package com.ztesoft.uosflow.web.service.flow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.dto.specification.TacheDto;
import com.zterc.uos.fastflow.service.TacheService;

@Service("FlowFishServ")
@Lazy
public class FlowFishServImpl implements FlowFishServ{
	private static Logger logger = Logger.getLogger(FlowFishServImpl.class);
	private static final String STENCIL_TRANSISTION_TYPE = "SequenceFlow";
	private static final String STENCIL_STARTNODE_TYPE = "StartNoneEvent";
	private static final String STENCIL_ENDNODE_TYPE = "EndNoneEvent";
	private static final String STENCIL_PARALLEL_TYPE = "ParallelGateway";
	
	@Autowired
	private TacheService tacheService;
	@Autowired
	private FlowServ flowServ;

	@Transactional
	@Override
	public String saveXpdl(Map<String, Object> paramMap) {
		String json = MapUtils.getString(paramMap, "json");
		logger.info("---json:"+json);
		Map<String,Object> jsonMap = GsonHelper.toMap(json);
		String xpdl = createXpdl(jsonMap);
		logger.info("---转换后的xpdl:"+xpdl);
		paramMap.put("xpdl", xpdl);
		String result = flowServ.saveXPDL(paramMap);
		return result;
	}

	private String createXpdl(Map<String, Object> paramMap) {
		String versionId = StringHelper.valueOf(paramMap.get("resourceId"));
		String flowName = StringHelper.valueOf(paramMap.get("name"));
		String xpdl = "<?xml version='1.0' encoding='UTF-8'?>";
        xpdl += "<Package xmlns='http://www.wfmc.org/2002/XPDL1.0' " +
        "xmlns:xpdl='http://www.wfmc.org/2002/XPDL1.0' " +
        "xmlns:xsd='http://www.w3.org/2000/10/XMLSchema' " +
        "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
        "xsi:noNamespaceSchemaLocation='TC-1025_schema_10_xpdl.xsd' " +
        "Id='" + versionId +
        "' " +
        "Name='" + flowName +
        "'>";
        xpdl += this.getPackageHeaderXml(paramMap);
        xpdl += "<WorkflowProcesses>";
        xpdl += "<WorkflowProcess Id='" + versionId +
        "' Name='" + flowName +
        "' AccessLevel='PUBLIC'>";
        xpdl += this.generateXPDL(paramMap);
        xpdl += "</WorkflowProcess>";
        xpdl += "</WorkflowProcesses>";
        xpdl += "</Package>";

		return xpdl;
	}

	@SuppressWarnings("unchecked")
	private String generateXPDL(Map<String, Object> paramMap) {
		Map<String, Object> properMap = (Map<String, Object>) paramMap
				.get("properties");
		String headerXml = "<ProcessHeader DurationUnit='m'>" + "<Created>"
				+ DateHelper.getTimeStamp() + "</Created>" + "<Description>"
				+ StringHelper.valueOf(properMap.get("notes"))
				+ "</Description>" + "<Priority>" + "1" + "</Priority>"
				+ "<ValidFrom>"
				+ StringHelper.valueOf(properMap.get("effDate"))
				+ "</ValidFrom>" + "<ValidTo>"
				+ StringHelper.valueOf(properMap.get("expDate")) + "</ValidTo>"
				+ "</ProcessHeader>";

		String redefinableHeaderXml = "<RedefinableHeader PublicationStatus='UNDER_TEST'>"
				+ "<Author>gongyi</Author>"
				+ "<Version>"
				+ StringHelper.valueOf(properMap.get("version"))
				+ "</Version>"
				+ "<Countrykey>GB</Countrykey>" + "</RedefinableHeader>";

//		String packageHeaderXml = "<PackageHeader DurationUnit='m'>"
//				+ "<XPDLVersion>1.0</XPDLVersion>"
//				+ "<Vendor>ZTERC UOSFlow V5.0</Vendor>" + "<Created>"
//				+ DateHelper.getTimeStamp() + "</Created>" + "<Description>"
//				+ StringHelper.valueOf(properMap.get("notes"))
//				+ "</Description>" + "<Priority>" + "1" + "</Priority>"
//				+ "<ValidFrom>"
//				+ StringHelper.valueOf(properMap.get("effDate"))
//				+ "</ValidFrom>" + "<ValidTo>"
//				+ StringHelper.valueOf(properMap.get("expDate")) + "</ValidTo>"
//				+ "</PackageHeader>" + redefinableHeaderXml;

		// 流程变量的XPDL
		String variableXml = "<DataFields>";
		variableXml += "</DataFields>";
		// 流程参数的XPDL
		String parameterXml = "<xpdl:FormalParameters>";
		parameterXml += "</xpdl:FormalParameters>";
		// 参与者的XPDL
		String participantsXml = "<Participants>";
		participantsXml += "</Participants>";
		// 应用程序的XPDL
		String applicationsXml = "<Applications>";
		applicationsXml += "</Applications>";

		// 活动的XPDL
		String activitiesXml = "<Activities>";
		List<Map<String,Object>> childShapes = (List<Map<String, Object>>) paramMap.get("childShapes");
		Map<String,String> fromActivityList = new HashMap<String,String>();
		List<Map<String,Object>> transitions = new ArrayList<Map<String,Object>>();
		List<String> activities = new ArrayList<String>();
		if(childShapes != null && childShapes.size()>0){
			int parallerNum = 0;
			int branchNum = 0;
			int branchIndex = 0;
			boolean isParallel = false;
			for(Map<String,Object> childShape:childShapes){
				Map<String,Object> stencil = (Map<String, Object>) childShape.get("stencil");
				String type = StringHelper.valueOf(stencil.get("type"));
				String nodeType = "Tache";
				if(!STENCIL_TRANSISTION_TYPE.equals(type)){
					List<String> outGoingList = (List<String>) childShape.get("outgoing");
					if(outGoingList != null && outGoingList.size()>0){
						if(outGoingList.size()>=2){
							isParallel = true;
							branchNum = outGoingList.size();
						}
						for(String temp:outGoingList){
							fromActivityList.put(temp, StringHelper.valueOf(childShape.get("resourceId")));
						}
					}
					activities.add(StringHelper.valueOf(childShape.get("resourceId")));
					if(STENCIL_STARTNODE_TYPE.equals(type)){
						nodeType = "Start";
						activitiesXml += generateStartOrFinish(childShape,nodeType);
					}else if(STENCIL_ENDNODE_TYPE.equals(type)){
						nodeType = "Finish";
						activitiesXml += generateStartOrFinish(childShape,nodeType);
					}else if(STENCIL_PARALLEL_TYPE.equals(type)){
						nodeType = "Parallel";
						parallerNum += 1;
						int modVal = parallerNum%2;
						if(modVal == 0){
							nodeType = "Relation";
							activitiesXml += generateRelation(childShape,nodeType);
						}else{
							activitiesXml += generateParallel(childShape,nodeType);
						}
					}else {
						nodeType = "Tache";
						activitiesXml += generateTache(childShape,nodeType,branchIndex);
						if(isParallel){
							if(branchIndex<branchNum){
								branchIndex += 1;
							}
							if(branchIndex == branchNum){
								isParallel = false;
								branchIndex = 0;
							}
						}
					}
				}else{
					transitions.add(childShape);
				}
			}
		}
		String exceptionId = getGid();
		activitiesXml += generateException(exceptionId);
		activities.add(exceptionId);
		activitiesXml += "</Activities>";

		// 转移的XPDL
		String transitionsXml = "<Transitions>";
		for (int i = 0; i < transitions.size(); i++) {
			Map<String,Object> childShape = transitions.get(i);
			String id = StringHelper.valueOf(childShape.get("resourceId"));
			String fromActivityId = fromActivityList.get(id);
			transitionsXml += generateTransitionXPDL(childShape,fromActivityId);
		}
		transitionsXml += "</Transitions>";

		String xpdl = "";
		xpdl += headerXml + redefinableHeaderXml + parameterXml
				+ participantsXml + variableXml + applicationsXml
				+ activitiesXml + transitionsXml;

		// 扩展属性
		List<Map<String, Object>> extendedAttributes = new ArrayList<Map<String, Object>>();
		Map<String, Object> extendMap1 = new HashMap<String, Object>();
		extendMap1.put("name", "ExStartOfWF");
		extendMap1.put("value", activities.get(0));
		extendedAttributes.add(extendMap1);
		Map<String, Object> extendMap2 = new HashMap<String, Object>();
		extendMap2.put("name", "ExExceptionOfWF");
		extendMap2.put("value", activities.get(activities.size()-1));
		extendedAttributes.add(extendMap2);
		Map<String, Object> extendMap3 = new HashMap<String, Object>();
		extendMap3.put("name", "ExEndOfWFs");
		List<Map<String, Object>> exEndOfWFs = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "ExEndOfWF");
		map.put("value", activities.get(activities.size()-2));
		exEndOfWFs.add(map);
		extendMap3.put("value", exEndOfWFs);
		extendedAttributes.add(extendMap3);
		Map<String, Object> extendMap4 = new HashMap<String, Object>();
		extendMap4.put("name", "ExStateOfWF");
		extendMap4.put("value", "10A");
		extendedAttributes.add(extendMap4);
		Map<String, Object> extendMap5 = new HashMap<String, Object>();
		extendMap5.put("name", "ExTypeOfWF");
		extendMap5.put("value", "COMP");
		extendedAttributes.add(extendMap5);

		xpdl += "<xpdl:ExtendedAttributes>";
		for (int i = 0; i < extendedAttributes.size(); i++) {
			if (StringHelper.valueOf(extendedAttributes.get(i).get("name")) == "ExEndOfWFs") {
				xpdl += "<xpdl:ExtendedAttribute Name='"
						+ StringHelper.valueOf(extendedAttributes.get(i).get(
								"name")) + "'>";
				xpdl += "<xpdl:ExEndOfWFs>";
				// 结束节点
				List<Map<String, Object>> ends = (List<Map<String, Object>>) extendedAttributes
						.get(i).get("value");
				for (int j = 0; j < ends.size(); j++) {
					xpdl += "<xpdl:ExEndOfWF Name='"
							+ StringHelper.valueOf(ends.get(j).get("name"))
							+ "' Value='"
							+ StringHelper.valueOf(ends.get(j).get("value"))
							+ "' />";
				}
				xpdl += "</xpdl:ExEndOfWFs>";
				xpdl += "</xpdl:ExtendedAttribute>";
			} else {
				xpdl += "<xpdl:ExtendedAttribute Name='"
						+ StringHelper.valueOf(extendedAttributes.get(i).get(
								"name"))
						+ "' Value='"
						+ StringHelper.valueOf(extendedAttributes.get(i).get(
								"value")) + "' />";
			}
		}
		xpdl += "</xpdl:ExtendedAttributes>";
	    
		return xpdl;
	}

	private String generateException(String exceptionId) {
		String xpdl = "<Activity Id='" + exceptionId + "' Name='异常节点'>";
		// 加上描述
		xpdl += "<Description />";
		// 限制
		xpdl += "<Limit />";
		// 执行者
		xpdl += "<Performer />";
		// 开始模式
		xpdl += "<StartMode>Automatic</StartMode>";
		// 结束模式
		xpdl += "<FinishMode>Automatic</FinishMode>";
		// 优先级
		xpdl += "<Priority />";
		xpdl += "<Implementation><No/></Implementation>";
		// 扩展属性
		List<Map<String, Object>> extendedAttributes = new ArrayList<Map<String, Object>>();
		Map<String, Object> extendMap1 = new HashMap<String, Object>();
		extendMap1.put("name", "branchIndex");
		extendMap1.put("value", "0");
		extendedAttributes.add(extendMap1);
		Map<String, Object> extendMap2 = new HashMap<String, Object>();
		extendMap2.put("name", "nodeIndex");
		extendMap2.put("value", "0");
		extendedAttributes.add(extendMap2);
		Map<String, Object> extendMap3 = new HashMap<String, Object>();
		extendMap3.put("name", "nodeType");
		extendMap3.put("value", "Exception");
		extendedAttributes.add(extendMap3);
		Map<String, Object> extendMap4 = new HashMap<String, Object>();
		extendMap4.put("name", "ExExceptionConfigs");
		extendMap4.put("value", "");
		extendedAttributes.add(extendMap4);
		xpdl += "<xpdl:ExtendedAttributes>";
		for (int i = 0; i < extendedAttributes.size(); i++) {
			// 异常配置
			if (StringHelper.valueOf(extendedAttributes.get(i).get("name")) == "ExExceptionConfigs") {
				xpdl += "<xpdl:ExtendedAttribute Name='"
						+ extendedAttributes.get(i).get("name") + "'>";
				xpdl += "<xpdl:ExExceptionConfigs>";
				xpdl += "</xpdl:ExExceptionConfigs>";
				xpdl += "</xpdl:ExtendedAttribute>";

			} else {
				xpdl += "<xpdl:ExtendedAttribute Name='"
						+ StringHelper.valueOf(extendedAttributes.get(i).get(
								"name"))
						+ "' Value='"
						+ StringHelper.valueOf(extendedAttributes.get(i).get(
								"value")) + "' />";
			}
		}
		xpdl += "</xpdl:ExtendedAttributes>";
		xpdl += "</Activity>";
		return xpdl;
	}

	@SuppressWarnings("unchecked")
	private String generateTransitionXPDL(Map<String, Object> childShape, String fromActivityId) {
		String id = StringHelper.valueOf(childShape.get("resourceId"));
		Map<String, Object> properMap = (Map<String, Object>) childShape
				.get("properties");
		String name = StringHelper.valueOf(properMap.get("name"));
		String description = StringHelper.valueOf(properMap.get("notes"));
		List<String> outgoing = (List<String>) childShape.get("outgoing");
		String toActivityId = outgoing.get(0);
		String condition = StringHelper.valueOf(properMap.get("conditionExpr"));
		String xpdl = "";
		// 如果开始节点是并行节点，那么线条开始节点的id应当是这个并行节点的合并条件
		xpdl += "<Transition Id='" + id + "' Name='" + name + "' From='"
				+ fromActivityId + "' To='" + toActivityId + "'>";

		// 加上描述
		if (description != null) {
			xpdl += "<Description>" + description + "</Description>";
		} else {
			xpdl += "<Description />";
		}
		if (condition != null && !"".equals(condition)) {
			xpdl += "<xpdl:Condition><xpdl:Xpression>" + "<![CDATA["
					+ condition + "]]>" + "</xpdl:Xpression></xpdl:Condition>";
		} else {
			xpdl += "<xpdl:Condition><xpdl:Xpression/></xpdl:Condition>";
		}
		// 扩展属性
		List<Map<String, Object>> extendedAttributes = new ArrayList<Map<String, Object>>();
		Map<String, Object> extendMap1 = new HashMap<String, Object>();
		extendMap1.put("name", "LineType");
		extendMap1.put("value", "Normal");
		extendedAttributes.add(extendMap1);
		Map<String, Object> extendMap2 = new HashMap<String, Object>();
		extendMap2.put("name", "parentId");
		extendMap2.put("value", fromActivityId);
		extendedAttributes.add(extendMap2);
		xpdl += "<xpdl:ExtendedAttributes>";
		for (int i = 0; i < extendedAttributes.size(); i++) {
			Map<String, Object> map = extendedAttributes.get(i);
			xpdl += "<xpdl:ExtendedAttribute Name='"
					+ StringHelper.valueOf(map.get("name")) + "' Value='"
					+ StringHelper.valueOf(map.get("value")) + "' />";
		}
		xpdl += "</xpdl:ExtendedAttributes>";

		xpdl += "</Transition>";
		return xpdl;
	}

	@SuppressWarnings("unchecked")
	private String generateTache(Map<String, Object> childShape, String nodeType, int branchIndex) {
		String id = StringHelper.valueOf(childShape.get("resourceId"));
		Map<String, Object> properMap = (Map<String, Object>) childShape
				.get("properties");
		String name = StringHelper.valueOf(properMap.get("name"));
		String description = StringHelper.valueOf(properMap.get("notes"));
		String xpdl = "<Activity Id='" + id + "' Name='" + name + "'>";
		// 加上描述
		if (description != null) {
			xpdl += "<Description>" + description + "</Description>";
		} else {
			xpdl += "<Description />";
		}
		// 限制
		xpdl += "<Limit />";
		// 执行者
		xpdl += "<Performer />";
		// 开始模式
		xpdl += "<StartMode>Automatic</StartMode>";
		// 结束模式
		xpdl += "<FinishMode>Automatic</FinishMode>";
		// 流入流出方式
		xpdl += "<TransitionRestrictions>";
		xpdl += "<TransitionRestriction>"
				+ "<Join Type='AND'/><Split Type='AND'/></TransitionRestriction>";
		xpdl += "</TransitionRestrictions>";
		// 优先级
		xpdl += "<Priority />";
		xpdl += "<Implementation>";
		xpdl += "<Tool Id='" + this.getGid() + "' Type='Application'>";
		xpdl += "</Tool></Implementation>";

		// 扩展属性
		String exTacheCode = StringHelper.valueOf(properMap.get("activityName"));
		TacheDto tacheDto = tacheService.queryTacheByCode(exTacheCode);
		String exTacheId = "";
		String exTacheName = "";
		if(tacheDto != null){
			exTacheId = tacheDto.getId().toString();
			exTacheName = tacheDto.getTacheName();
		}
		List<Map<String, Object>> extendedAttributes = new ArrayList<Map<String, Object>>();
		Map<String, Object> extendMap1 = new HashMap<String, Object>();
		extendMap1.put("name", "branchIndex");
		extendMap1.put("value", branchIndex);
		extendedAttributes.add(extendMap1);
		Map<String, Object> extendMap2 = new HashMap<String, Object>();
		extendMap2.put("name", "nodeIndex");
		extendMap2.put("value", "0");
		extendedAttributes.add(extendMap2);
		Map<String, Object> extendMap3 = new HashMap<String, Object>();
		extendMap3.put("name", "nodeType");
		extendMap3.put("value", nodeType);
		extendedAttributes.add(extendMap3);
		Map<String, Object> extendMap4 = new HashMap<String, Object>();
		extendMap4.put("name", "ExTacheId");
		extendMap4.put("value", exTacheId);
		extendedAttributes.add(extendMap4);
		Map<String, Object> extendMap5 = new HashMap<String, Object>();
		extendMap5.put("name", "ExTacheCode");
		extendMap5.put("value", exTacheCode);
		extendedAttributes.add(extendMap5);
		Map<String, Object> extendMap6 = new HashMap<String, Object>();
		extendMap6.put("name", "ExTacheName");
		extendMap6.put("value", exTacheName);
		extendedAttributes.add(extendMap6);
		Map<String, Object> extendMap7 = new HashMap<String, Object>();
		extendMap7.put("name", "ExOperType");
		extendMap7.put("value", "1");
		extendedAttributes.add(extendMap7);
		Map<String, Object> extendMap8 = new HashMap<String, Object>();
		extendMap8.put("name", "ExWithdraw");
		extendMap8.put("value", "true");
		extendedAttributes.add(extendMap8);
		Map<String, Object> extendMap9 = new HashMap<String, Object>();
		extendMap9.put("name", "ExChange");
		extendMap9.put("value", "true");
		extendedAttributes.add(extendMap9);
		xpdl += "<xpdl:ExtendedAttributes>";
		for (int i = 0; i < extendedAttributes.size(); i++) {
			Map<String, Object> map = extendedAttributes.get(i);
			xpdl += "<xpdl:ExtendedAttribute Name='"
					+ StringHelper.valueOf(map.get("name")) + "' Value='"
					+ StringHelper.valueOf(map.get("value")) + "' />";
		}
		xpdl += "</xpdl:ExtendedAttributes>";
		xpdl += "</Activity>";
		return xpdl;
	}

	@SuppressWarnings("deprecation")
	private String getGid() {
		Date now = new Date();
		String uuid = "A" + Double.valueOf(Math.random() * 1e5).intValue() + "-" + now.getSeconds() + "-" + Double.valueOf(Math.random() * 1e5).intValue() + "-" + System.currentTimeMillis() + "-" + Double.valueOf(Math.random() * 1e5).intValue();
        return uuid;
	}

	@SuppressWarnings("unchecked")
	private String generateRelation(Map<String, Object> childShape,
			String nodeType) {
		String id = StringHelper.valueOf(childShape.get("resourceId"));
		Map<String, Object> properMap = (Map<String, Object>) childShape
				.get("properties");
		String name = StringHelper.valueOf(properMap.get("name"));
		String description = StringHelper.valueOf(properMap.get("notes"));
		 //最后要生成并行节点最后的条件合并节点，这个也作为路由节点处理
        String xpdl = "<Activity Id='" + id + "' Name='" + name + "'>";
        xpdl += "<Route />";
        //流入流出方式
        xpdl += "<TransitionRestrictions>";
        xpdl += "<TransitionRestriction>" +
        "<Join Type='AND'/><Split Type='AND'/></TransitionRestriction>";
        xpdl += "</TransitionRestrictions>";
        //加上描述
        if (description != null) {
            xpdl += "<Description>" + description + "</Description>";
        } else {
            xpdl += "<Description />";
        }
        //其他扩展属性
        List<Map<String, Object>> extendedAttributes = new ArrayList<Map<String, Object>>();
		Map<String, Object> extendMap = new HashMap<String, Object>();
		extendMap.put("name", "nodeType");
		extendMap.put("value", nodeType);
		extendedAttributes.add(extendMap);

		xpdl += "<xpdl:ExtendedAttributes>";
		for (int i = 0; i < extendedAttributes.size(); i++) {
			Map<String, Object> map = extendedAttributes.get(i);
			xpdl += "<xpdl:ExtendedAttribute Name='"
					+ StringHelper.valueOf(map.get("name")) + "' Value='"
					+ StringHelper.valueOf(map.get("value")) + "' />";
		}
        xpdl += "</xpdl:ExtendedAttributes>";
        xpdl += "</Activity>";
        return xpdl;
	}

	@SuppressWarnings("unchecked")
	private String generateParallel(Map<String, Object> childShape,
			String nodeType) {
		String id = StringHelper.valueOf(childShape.get("resourceId"));
		Map<String, Object> properMap = (Map<String, Object>) childShape
				.get("properties");
		String name = StringHelper.valueOf(properMap.get("name"));
		String description = StringHelper.valueOf(properMap.get("notes"));
		//并行节点首先要生成一个路由节点
        String xpdl = "<Activity Id='" + id + "' Name='" + name + "'>";
        xpdl += "<Route />";
        //流入流出方式
        xpdl += "<TransitionRestrictions>";
        xpdl += "<TransitionRestriction>" +
        "<Join Type='AND'/><Split Type='AND'/></TransitionRestriction>";
        xpdl += "</TransitionRestrictions>";
        
        //加上描述
        if (description != null) {
            xpdl += "<Description>" + description + "</Description>";
        } else {
            xpdl += "<Description />";
        }
        //扩展属性
        //加上本节点所处的分支序号和节点序号
        List<Map<String, Object>> extendedAttributes = new ArrayList<Map<String, Object>>();
		Map<String, Object> extendMap1 = new HashMap<String, Object>();
		extendMap1.put("name", "branchIndex");
		extendMap1.put("value", "0");
		extendedAttributes.add(extendMap1);
		Map<String, Object> extendMap2 = new HashMap<String, Object>();
		extendMap2.put("name", "nodeIndex");
		extendMap2.put("value", "0");
		extendedAttributes.add(extendMap2);
		Map<String, Object> extendMap3 = new HashMap<String, Object>();
		extendMap3.put("name", "nodeType");
		extendMap3.put("value", nodeType);
		extendedAttributes.add(extendMap3);
		Map<String, Object> extendMap4 = new HashMap<String, Object>();
		extendMap4.put("name", "numOfBranch");
		extendMap4.put("value", "2");
		extendedAttributes.add(extendMap4);

		xpdl += "<xpdl:ExtendedAttributes>";
		for (int i = 0; i < extendedAttributes.size(); i++) {
			Map<String, Object> map = extendedAttributes.get(i);
			xpdl += "<xpdl:ExtendedAttribute Name='"
					+ StringHelper.valueOf(map.get("name")) + "' Value='"
					+ StringHelper.valueOf(map.get("value")) + "' />";
		}
        xpdl += "</xpdl:ExtendedAttributes>";
        xpdl += "</Activity>";
        return xpdl;
	}

	@SuppressWarnings("unchecked")
	private String generateStartOrFinish(Map<String, Object> childShape, String nodeType) {
		String id = StringHelper.valueOf(childShape.get("resourceId"));
		Map<String, Object> properMap = (Map<String, Object>) childShape
				.get("properties");
		String name = StringHelper.valueOf(properMap.get("name"));
		String description = StringHelper.valueOf(properMap.get("notes"));
		String xpdl = "<Activity Id='" + id + "' Name='" + name + "'>";
		// 加上描述
		if (description != null) {
			xpdl += "<Description>" + description + "</Description>";
		} else {
			xpdl += "<Description />";
		}
		// 限制
		xpdl += "<Limit />";
		// 执行者
		xpdl += "<Performer />";
		// 开始模式
		xpdl += "<StartMode>Automatic</StartMode>";
		// 结束模式
		xpdl += "<FinishMode>Automatic</FinishMode>";
		// 优先级
		xpdl += "<Priority />";
		xpdl += "<Implementation><No/></Implementation>";
		// 扩展属性
		List<Map<String, Object>> extendedAttributes = new ArrayList<Map<String, Object>>();
		Map<String, Object> extendMap1 = new HashMap<String, Object>();
		extendMap1.put("name", "branchIndex");
		extendMap1.put("value", "0");
		extendedAttributes.add(extendMap1);
		Map<String, Object> extendMap2 = new HashMap<String, Object>();
		extendMap2.put("name", "nodeIndex");
		extendMap2.put("value", "0");
		extendedAttributes.add(extendMap2);
		Map<String, Object> extendMap3 = new HashMap<String, Object>();
		extendMap3.put("name", "nodeType");
		extendMap3.put("value", nodeType);
		extendedAttributes.add(extendMap3);

		xpdl += "<xpdl:ExtendedAttributes>";
		for (int i = 0; i < extendedAttributes.size(); i++) {
			Map<String, Object> map = extendedAttributes.get(i);
			xpdl += "<xpdl:ExtendedAttribute Name='"
					+ StringHelper.valueOf(map.get("name")) + "' Value='"
					+ StringHelper.valueOf(map.get("value")) + "' />";
		}
		xpdl += "</xpdl:ExtendedAttributes>";
		xpdl += "</Activity>";
		return xpdl;
	}

	@SuppressWarnings("unchecked")
	private String getPackageHeaderXml(Map<String, Object> paramMap) {
		Map<String, Object> properMap = (Map<String, Object>) paramMap
				.get("properties");
		String redefinableHeaderXml = "<RedefinableHeader PublicationStatus='UNDER_TEST'>"
				+ "<Author>gongyi</Author>"
				+ "<Version>"
				+ StringHelper.valueOf(properMap.get("version"))
				+ "</Version>"
				+ "<Countrykey>GB</Countrykey>" + "</RedefinableHeader>";

		String packageHeaderXml = "<PackageHeader DurationUnit='m'>"
				+ "<XPDLVersion>1.0</XPDLVersion>"
				+ "<Vendor>ZTERC UOSFlow V5.0</Vendor>" + "<Created>"
				+ DateHelper.getTimeStamp() + "</Created>" + "<Description>"
				+ StringHelper.valueOf(properMap.get("notes"))
				+ "</Description>" + "<Priority>" + "1" + "</Priority>"
				+ "<ValidFrom>"
				+ StringHelper.valueOf(properMap.get("effDate"))
				+ "</ValidFrom>" + "<ValidTo>"
				+ StringHelper.valueOf(properMap.get("expDate")) + "</ValidTo>"
				+ "</PackageHeader>" + redefinableHeaderXml;
		return packageHeaderXml;
	}

	@Transactional
	@Override
	public String saveBpmn(Map<String, Object> paramMap) {
		String json = MapUtils.getString(paramMap, "json");
		logger.info("---json:"+json);
		Map<String,Object> jsonMap = GsonHelper.toMap(json);
		String bpmn = createBpmn(jsonMap);
		logger.info("---转换后的bpmn:"+bpmn);
		return GsonHelper.toJson("true");
	}

	private String createBpmn(Map<String, Object> paramMap) {
		String versionId = StringHelper.valueOf(paramMap.get("resourceId"));
		String flowName = StringHelper.valueOf(paramMap.get("name"));
		String bpmn = "<?xml version='1.0' encoding='UTF-8'?>";
		bpmn += "<definitions xmlns='http://www.omg.org/spec/BPMN/20100524/MODEL'" +
				" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
				"xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
				"xmlns:activiti='http://activiti.org/bpmn' " +
				"xmlns:bpmndi='http://www.omg.org/spec/BPMN/20100524/DI' " +
				"xmlns:omgdc='http://www.omg.org/spec/DD/20100524/DC' " +
				"xmlns:omgdi='http://www.omg.org/spec/DD/20100524/DI' " +
				"typeLanguage='http://www.w3.org/2001/XMLSchema' " +
				"expressionLanguage='http://www.w3.org/1999/XPath' " +
				"targetNamespace='http://www.activiti.org/processdef'>";
		bpmn += "<process id='"+versionId+"' name='"+flowName+"' isExecutable='true'>";
		bpmn += this.generateBPMN(paramMap);
		bpmn += "</process>";
		bpmn += "<bpmndi:BPMNDiagram id='BPMNDiagram_"+versionId+"'>";
		bpmn += "<bpmndi:BPMNPlane bpmnElement='"+versionId+"' id='BPMNPlane_"+versionId+"'>";
		bpmn += this.generateBPMNDiagram(paramMap);
		bpmn += "</bpmndi:BPMNPlane>";
		bpmn += "</bpmndi:BPMNDiagram>";
		bpmn += "</definitions>";
		return bpmn;
	}

	@SuppressWarnings("unchecked")
	private String generateBPMNDiagram(Map<String, Object> paramMap) {
		String activitiesDiagramXml = "";
		List<Map<String,Object>> childShapes = (List<Map<String, Object>>) paramMap.get("childShapes");
		List<Map<String,Object>> transitions = new ArrayList<Map<String,Object>>();
		if(childShapes != null && childShapes.size()>0){
			for(Map<String,Object> childShape:childShapes){
				Map<String,Object> stencil = (Map<String, Object>) childShape.get("stencil");
				String type = StringHelper.valueOf(stencil.get("type"));
				String height = "";
				String width = "";
				if(!STENCIL_TRANSISTION_TYPE.equals(type)){
					if(STENCIL_STARTNODE_TYPE.equals(type)){
						height = "30.0";
						width = "30.0";
					}else if(STENCIL_ENDNODE_TYPE.equals(type)){
						height = "30.0";
						width = "30.0";
					}else if(STENCIL_PARALLEL_TYPE.equals(type)){
						height = "40.0";
						width = "40.0";
					}else {
						height = "80";
						width = "100.0";
					}
					activitiesDiagramXml += generateBpmnNodeDiagram(childShape,height,width);
				}else{
					transitions.add(childShape);
				}
			}
		}
		// 转移的XPDL
		String transitionsDiagramXml = "";
		for (int i = 0; i < transitions.size(); i++) {
			Map<String,Object> childShape = transitions.get(i);
			transitionsDiagramXml += generateTransitionBPMNEdge(childShape);
		}
		String bpmn = activitiesDiagramXml + transitionsDiagramXml;
		return bpmn;
	}

	@SuppressWarnings("unchecked")
	private String generateTransitionBPMNEdge(Map<String, Object> childShape) {
		String id = MapUtils.getString(childShape, "resourceId");
		String bpmn = "<bpmndi:BPMNEdge bpmnElement='" + id + "' id='BPMNEdge_" + id +"'>";
		List<Map<String,Object>> dockers = (List<Map<String, Object>>) childShape.get("dockers");
		for(Map<String, Object> map:dockers){
			String x = MapUtils.getString(map, "x");
			String y = MapUtils.getString(map, "y");
			bpmn += "<omgdi:waypoint x='" + x + "' y='" + y + "'></omgdi:waypoint>";
		}
		bpmn += "</bpmndi:BPMNEdge>";
		return bpmn;
	}

	@SuppressWarnings("unchecked")
	private String generateBpmnNodeDiagram(Map<String, Object> childShape,
			String height, String width) {
		String id = MapUtils.getString(childShape, "resourceId");
		Map<String,Object> properMap = (Map<String, Object>) childShape.get("properties");
		Map<String,Object> stencil = (Map<String, Object>) childShape.get("stencil");
		String type = StringHelper.valueOf(stencil.get("type"));
		String bpmn = "<bpmndi:BPMNShape bpmnElement='";
		if("userTask".equals(type)){
			id = MapUtils.getString(properMap, "activityName");
		}
		bpmn += id + "'";
		bpmn += " id='BPMNShape_" + id + "'>";
		Map<String,Object> boundsMap = (Map<String, Object>) childShape.get("bounds");
		Map<String,Object> upperLeft = (Map<String, Object>) boundsMap.get("upperLeft");
		String x = MapUtils.getString(upperLeft, "x");
		String y = MapUtils.getString(upperLeft, "y");
		bpmn += "<omgdc:Bounds height='" + height + "' width='" + width + "' x='" + x + "' y='" + y + "'></omgdc:Bounds>";
		bpmn += "</bpmndi:BPMNShape>";
		return bpmn;
	}

	@SuppressWarnings("unchecked")
	private String generateBPMN(Map<String, Object> paramMap) {
		String bpmn = "";
		String activitiesXml = "";
		List<Map<String,Object>> childShapes = (List<Map<String, Object>>) paramMap.get("childShapes");
		Map<String,String> fromActivityList = new HashMap<String,String>();
		List<Map<String,Object>> transitions = new ArrayList<Map<String,Object>>();
		if(childShapes != null && childShapes.size()>0){
			for(Map<String,Object> childShape:childShapes){
				Map<String,Object> stencil = (Map<String, Object>) childShape.get("stencil");
				String type = StringHelper.valueOf(stencil.get("type"));
				String nodeType = "userTask";
				if(!STENCIL_TRANSISTION_TYPE.equals(type)){
					String activityId = StringHelper.valueOf(childShape.get("resourceId"));
					if(STENCIL_STARTNODE_TYPE.equals(type)){
						nodeType = "startEvent";
					}else if(STENCIL_ENDNODE_TYPE.equals(type)){
						nodeType = "endEvent";
					}else if(STENCIL_PARALLEL_TYPE.equals(type)){
						nodeType = "parallelGateway";
					}else {
						nodeType = "userTask";
						Map<String,Object> properMap = (Map<String, Object>) childShape.get("properties");
						activityId = MapUtils.getString(properMap, "activityName");
					}
					activitiesXml += generateBpmnNode(childShape,nodeType);
					List<String> outGoingList = (List<String>) childShape.get("outgoing");
					if(outGoingList != null && outGoingList.size()>0){
						for(String temp:outGoingList){
							fromActivityList.put(temp, activityId);
						}
					}
				}else{
					transitions.add(childShape);
				}
			}
		}
		// 转移的XPDL
		String transitionsXml = "";
		for (int i = 0; i < transitions.size(); i++) {
			Map<String,Object> childShape = transitions.get(i);
			String id = StringHelper.valueOf(childShape.get("resourceId"));
			String fromActivityId = fromActivityList.get(id);
			transitionsXml += generateTransitionBpmn(childShape,fromActivityId);
		}
		
		bpmn += activitiesXml + transitionsXml;
		return bpmn;
	}

	@SuppressWarnings("unchecked")
	private String generateTransitionBpmn(Map<String, Object> childShape,
			String fromActivityId) {
		String id = MapUtils.getString(childShape, "resourceId");
		List<String> outgoing = (List<String>) childShape.get("outgoing");
		String toActivityId = outgoing.get(0);
		String bpmn = "<sequenceFlow id='" + id + "' sourceRef='" + fromActivityId + "' targetRef='" + 
				toActivityId + "'></sequenceFlow>";
		return bpmn;
	}

	@SuppressWarnings("unchecked")
	private String generateBpmnNode(Map<String, Object> childShape,
			String nodeType) {
		String id = MapUtils.getString(childShape, "resourceId");
		Map<String,Object> properMap = (Map<String, Object>) childShape.get("properties");
		String name = MapUtils.getString(properMap, "name");
		String bpmn = "<" + nodeType + " id='";
		if("userTask".equals(nodeType)){
			id = MapUtils.getString(properMap, "activityName");
			bpmn += id + "'";
			bpmn += " name='" + name + "'";
		}else{
			bpmn += id + "'";
		}
		bpmn += "></" + nodeType + ">";
		return bpmn;
	}

	

}
