package com.ztesoft.uosflow.web.service.flow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.helper.StreamHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dto.process.ProcessDefinitionDto;
import com.zterc.uos.fastflow.dto.process.ProcessParamDefDto;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.dto.specification.DispatchRuleDto;
import com.zterc.uos.fastflow.dto.specification.ProcessParamDefRelDto;
import com.zterc.uos.fastflow.dto.specification.PackageCatalogDto;
import com.zterc.uos.fastflow.dto.specification.PackageDto;
import com.zterc.uos.fastflow.dto.specification.PageDto;
import com.zterc.uos.fastflow.service.AreaService;
import com.zterc.uos.fastflow.service.DispatchRuleService;
import com.zterc.uos.fastflow.service.ProcessDefinitionService;
import com.zterc.uos.fastflow.service.ProcessPackageService;
import com.zterc.uos.fastflow.service.ProcessParamDefService;
import com.ztesoft.uosflow.dubbo.inf.manager.FlowManagerInf;

@Service("FlowServ")
@Lazy(true)
public class FlowServImpl implements FlowServ {

	private static Logger logger = LoggerFactory.getLogger(FlowServImpl.class);

	@Autowired
	private ProcessPackageService processPackageService;

	@Autowired
	private AreaService areaService; 

	@Autowired
	private DispatchRuleService dispatchRuleService;

	@Autowired
	private ProcessDefinitionService processDefinitionService;
	
	@Autowired
	private ProcessParamDefService processParamDefService;
	
	@Autowired
	@Qualifier("flowManagerService")
	private FlowManagerInf flowManagerService;
	
	private PlatformTransactionManager txManager = null;
	private DefaultTransactionDefinition def = null;
	private TransactionStatus status = null;
	
	@Override
	public String queryPackageCatalogByAreaIdAndSystemCode(
			Map<String, Object> map) {
		Long areaId = LongHelper.valueOf(map.get("areaId"));
		String systemCode = String.valueOf(map.get("systemCode"));

		// 根据areaId和systemCode获取流程目录部分
		PackageCatalogDto[] packageCatalogDtos = null;
		if (areaId == null || areaId == -1) {
			packageCatalogDtos = processPackageService
					.qryPackageCatalogByAreaIdAndSystemCode(null, systemCode);
		} else {
			AreaDto area = areaService.findAreaByAreaId(areaId);
			AreaDto[] areaDtos = areaService
					.findAreasByPathCode(area.getPathCode());
			StringBuffer areaIds = new StringBuffer();
			for (int i = 0; i < areaDtos.length; i++) {
				if (i != areaDtos.length - 1) {
					areaIds.append(areaDtos[i].getAreaId() + ",");
				} else {
					areaIds.append(areaDtos[i].getAreaId());
				}
			}
			packageCatalogDtos = processPackageService
					.qryPackageCatalogByAreaIdAndSystemCode(
							areaIds.toString(), systemCode);
		}

		return this.catalogArrayToTreeJson(packageCatalogDtos);
	}

	private String catalogArrayToTreeJson(PackageCatalogDto[] packageCatalogDtos) {
		JsonArray list = new JsonArray();

		// 加载目录到树
		Map<String, JsonObject> catalogMap = new HashMap<String, JsonObject>();
		if (packageCatalogDtos != null && packageCatalogDtos.length > 0) {
			Map<String, JsonObject> parentMap = new HashMap<String, JsonObject>();
			for (int i = 0; i < packageCatalogDtos.length; i++) {
				PackageCatalogDto dto = packageCatalogDtos[i];
				String pathCode = dto.getPathCode();
				if (pathCode.lastIndexOf(".") >= 0) { // 不是最高层地区
					pathCode = pathCode.substring(0, pathCode.lastIndexOf("."));
				}
				// 给节点赋值
				JsonObject catalog = dto.getTreeJsonObject();
				// 节点的层次
				if (parentMap.containsKey(pathCode)) {
					JsonObject parent = (JsonObject) parentMap.get(pathCode);
					JsonArray children = parent.getAsJsonArray("children");
					parent.addProperty("state", "closed");
					if (children == null) {
						children = new JsonArray();
						parent.add("children", children);
					}
					children.add(catalog);
				} else {
					list.add(catalog);
				}
				parentMap.put(dto.getPathCode(), catalog);
				catalogMap.put(dto.getCatalogId().toString(), catalog);
			}
		}
		return GsonHelper.toJson(list);
	}

	@Override
	public String qryPackageDefineByCatalogId(Map<String, Object> map) {
		Long catalogId = LongHelper.valueOf(map.get("catalogId"));

		// 流程目录获取流程模板部分
		PackageDto[] packageDtos = processPackageService
				.qryPackageByPackageCatalogIds(catalogId.toString().toString());

		// 根据流程模板获取流程版本部分
		ProcessDefinitionDto[] processDefinitionDtos = null;
		if (packageDtos != null && packageDtos.length > 0) {
			StringBuffer packageIds = new StringBuffer();
			for (int i = 0; i < packageDtos.length; i++) {
				if (i != packageDtos.length - 1) {
					packageIds.append(packageDtos[i].getPackageId() + ",");
				} else {
					packageIds.append(packageDtos[i].getPackageId());
				}
			}
			processDefinitionDtos = processDefinitionService
					.qryProcessDefineByPackageIds(packageIds.toString());
			return this.defineArrayToTreeJson(packageDtos, processDefinitionDtos);
		}
		return GsonHelper.toJson(new JsonArray());
	}

	@Override
	public String qryProcessDefineByCatalogId(Map<String,Object> map){
		Long catalogId = LongHelper.valueOf(map.get("catalogId"));
		if(catalogId != null){
			// 流程目录获取流程模板部分
			PackageDto[] packageDtos = processPackageService
					.qryPackageByPackageCatalogIds(catalogId.toString());
			return this.defineArrayToTreeJson(packageDtos, null);
		}
		return GsonHelper.toJson(new JsonArray());
	}
	
	private String defineArrayToTreeJson(PackageDto[] packageDtos,
			ProcessDefinitionDto[] processDefinitionDtos) {
		JsonArray list = new JsonArray();

		// 加载模板到树
		Map<String, JsonObject> packageMap = new HashMap<String, JsonObject>();
		if (packageDtos != null && packageDtos.length > 0) {
			for (int i = 0; i < packageDtos.length; i++) {
				PackageDto dto = packageDtos[i];
				// 给节点赋值
				JsonObject pack = dto.getTreeJsonObject();
				// 节点的层次
				packageMap.put(dto.getPackageId().toString(), pack);

				list.add(pack);
			}
		}
		// 加载版本到树
		if (processDefinitionDtos != null && processDefinitionDtos.length > 0) {
			for (int i = 0; i < processDefinitionDtos.length; i++) {
				ProcessDefinitionDto dto = processDefinitionDtos[i];
				// 给节点赋值
				JsonObject def = dto.getTreeJsonObject();
				// 节点的层次
				if (packageMap.containsKey(dto.getPackageId().toString())) {
					JsonObject parent = (JsonObject) packageMap.get(dto
							.getPackageId().toString());
					JsonArray children = parent.getAsJsonArray("children");
					parent.addProperty("state", "closed");
					if (children == null) {
						children = new JsonArray();
						parent.add("children", children);
					}
					children.add(def);
				}
			}
		}
		return GsonHelper.toJson(list);
	}

	@Override
	public String qryPackageDefineByPackageId(Map<String, Object> map) {
		String packageId = String.valueOf(map.get("packageId"));
		return GsonHelper.toJson(processDefinitionService
				.qryProcessDefineByPackageIds(packageId));
	}

	@Override
	public String getXPDL(Map<String, Object> map) {
		Long processDefineId = LongHelper.valueOf(map.get("processDefineId"));
		ProcessDefinitionDto dto = processDefinitionService.qryProcessDefinitionById(processDefineId);
		Map<String, String> resultMap = new HashMap<String, String>();
		if (dto != null) {
			try {
				if (dto.getXpdl() == null) {
					resultMap.put("xpdl", null);
				} else {
					String xpdlContent = new String(StreamHelper.readBytes(dto
							.getXpdl().getBinaryStream()));
					resultMap.put("xpdl", xpdlContent);
				}
			} catch (Exception ex) {
				logger.error("----获取XPDL异常 ", ex);
			}
		}
		return GsonHelper.toJson(resultMap);
	}

	@Override
	public String findProcessDefinitionById(Map<String, Object> map) {
		Long processDefineId = LongHelper.valueOf(map.get("processDefineId"));
		ProcessDefinitionDto processDefinitionDto = processDefinitionService
				.queryProcessDefinitionById(processDefineId.toString());
		if(processDefinitionDto != null){
			processDefinitionDto.setWorkflowProcess(null);
		}
		
		return GsonHelper.toJson(processDefinitionDto);
	}

	@Override
	public String findPackageById(Map<String, Object> map) {
		Long packageId = LongHelper.valueOf(map.get("packageId"));

		return GsonHelper.toJson(processPackageService.qryPackageById(packageId));
	}

	@Override
	@Transactional
	public String addPackageCatalog(Map<String, Object> map) {
		Long parentId = LongHelper.valueOf(map.get("parentId"));
		Long areaId = LongHelper.valueOf(map.get("areaId"));
		String systemCode = String.valueOf(map.get("systemCode"));
		String catalogName = String.valueOf(map.get("catalogName"));
		String pathCode = String.valueOf(map.get("pathCode"));

		Long packageCatalogId = SequenceHelper.getId("UOS_PACKAGECATALOG");

		PackageCatalogDto dto = new PackageCatalogDto();
		dto.setCatalogId(packageCatalogId);
		dto.setAreaId(areaId);
		dto.setCatalogName(catalogName);
		dto.setParentId(parentId);
		dto.setPathCode(pathCode.equals("") ? packageCatalogId.toString()
				: pathCode + "." + packageCatalogId);
		dto.setPackageCatalogType("0");
		dto.setState("10A");
		dto.setStateDate(DateHelper.getTimeStamp());
		dto.setSystemCode(systemCode);

		processPackageService.addPackageCatalog(dto);
		return GsonHelper.toJson(dto.getTreeJsonObject());
	}

	@Override
	@Transactional
	public String updatePackageCatalog(Map<String, Object> map) {
		Long id = LongHelper.valueOf(map.get("id"));
		String catalogName = String.valueOf(map.get("catalogName"));

		PackageCatalogDto dto = new PackageCatalogDto();
		dto.setCatalogId(id);
		dto.setCatalogName(catalogName);

		processPackageService.updatePackageCatalog(dto);
		return GsonHelper.toJson(dto.getTreeJsonObject());
	}

	@Override
	@Transactional
	public String deletePackageCatalog(Map<String, Object> map) {
		Long catalogId = LongHelper.valueOf(map.get("catalogId"));

		PackageCatalogDto dto = new PackageCatalogDto();
		dto.setState("10X");
		dto.setStateDate(DateHelper.getTimeStamp());
		dto.setCatalogId(catalogId);

		processPackageService.deletePackageCatalog(dto);
		return GsonHelper.toJson("true");
	}

	@Override
	@Transactional
	public String addPackage(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		PackageDto dto = new PackageDto();
		try {
			Long parentId = LongHelper.valueOf(map.get("parentId"));
			String packageName = String.valueOf(map.get("packageName"));
			String packageType = String.valueOf(map.get("packageType"));
			String areaId = String.valueOf(map.get("areaId"));

			Long packageId = SequenceHelper.getId("UOS_PACKAGE");

			dto.setPackageId(packageId);
			dto.setCatalogId(parentId);
			dto.setName(packageName);
			dto.setState("10A");
			dto.setStateDate(DateHelper.getTimeStamp());
			dto.setDescription("");
			dto.setOwnerAreaId(areaId);
			dto.setPackageType(packageType);
			dto.setEffDate(Timestamp.valueOf(map.get("effDate").toString()));
			dto.setExpDate(Timestamp.valueOf(map.get("expDate").toString()));
			processPackageService.addPackage(dto);
			txManager.commit(status);
			flowManagerService.refreshProcessPackageCache();
		} catch (Exception e) {
			logger.error("添加流程模板异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson(dto.getTreeJsonObject());
	}

	@Override
	@Transactional
	public String updatePackage(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		try {
			Long id = LongHelper.valueOf(map.get("id"));
			String packageName = String.valueOf(map.get("packageName"));

			PackageDto dto = new PackageDto();
			dto.setPackageId(id);
			dto.setName(packageName);
			dto.setEffDate(Timestamp.valueOf(map.get("effDate").toString()));
			dto.setExpDate(Timestamp.valueOf(map.get("expDate").toString()));
			processPackageService.updatePackage(dto);
			processDefinitionService.updateProcessDefineNameByPackageId(id, packageName);
			txManager.commit(status);
			flowManagerService.refreshProcessPackageCache();
		} catch (Exception e) {
			logger.error("修改流程模板异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson("true");
	}

	@Override
	@Transactional
	public String deletePackage(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		try {
			Long packageId = LongHelper.valueOf(map.get("packageId"));

			PackageDto dto = new PackageDto();
			dto.setPackageId(packageId);
			dto.setState("10X");
			dto.setStateDate(DateHelper.getTimeStamp());
			processPackageService.deletePackage(dto);
			txManager.commit(status);
			flowManagerService.refreshProcessPackageCache();
		} catch (Exception e) {
			logger.error("删除流程模板异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson("true");
	}

	public String findProcessDefinitionByCode(Map<String, Object> map) {
		String packageDefineCode = StringHelper.valueOf(map
				.get("packageDefineCode"));
		ProcessDefinitionDto processDefinitionDto = processDefinitionService
				.qryProcessDefinitionByCode(packageDefineCode);
		if(processDefinitionDto==null){
			return GsonHelper.toJson(null);
		}
		processDefinitionDto.setXpdl(null);//置空blob字段，否则转换gson异常。
		return GsonHelper.toJson(processDefinitionDto);
	}

	@Override
	@Transactional
	public String addProcessDefine(Map<String, Object> map) {

		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		ProcessDefinitionDto dto = new ProcessDefinitionDto();
		try {
			Long parentId = LongHelper.valueOf(map.get("parentId"));
			String editUser = String.valueOf(map.get("editUser"));
			String name = String.valueOf(map.get("name"));

			Long defineId = SequenceHelper.getId("UOS_PROCESSDEFINE");

			ProcessDefinitionDto[] processDefinitionDtos = processDefinitionService
					.qryProcessDefinitionsByPackageId(parentId);

			int maxVersion = 0;
			for (int i = 0; i < processDefinitionDtos.length; i++) {
				int version = Integer.parseInt(processDefinitionDtos[i].getVersion()
						.split("\\.")[1]);
				if (maxVersion < version) {
					maxVersion = version;
				}
			}

			dto.setPackageDefineId(defineId);
			dto.setPackageDefineCode(StringHelper.valueOf(map
					.get("packageDefineCode")));
			dto.setPackageId(parentId);
			dto.setName(name);
			dto.setDescription("");
			dto.setVersion("1." + (maxVersion + 1));// version怎么玩。
			dto.setAuthor(editUser);
			dto.setState(ProcessDefinitionDto.STATE_LOCK);
			dto.setStateDate(DateHelper.getTimeStamp());
			dto.setValidFromDate(DateHelper.getTimeStamp());
			dto.setValidToDate(DateHelper.getTimeStamp());
			dto.setEditUser(editUser);
			dto.setEditDate(DateHelper.getTimeStamp());

			processDefinitionService.addProcessDefine(dto);
			txManager.commit(status);
			flowManagerService.refreshProcessDefineCache();
		} catch (Exception e) {
			logger.error("添加流程模板版本异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson(dto.getTreeJsonObject());
	}

	@Override
	@Transactional
	public String deleteProcessDefine(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		try {
			Long packageDefineId = LongHelper.valueOf(map.get("packageDefineId"));

			ProcessDefinitionDto dto = new ProcessDefinitionDto();
			dto.setPackageDefineId(packageDefineId);
			dto.setState(ProcessDefinitionDto.STATE_DELETED);
			dto.setStateDate(DateHelper.getTimeStamp());
			processDefinitionService.deleteProcessDefine(dto);
			txManager.commit(status);
			flowManagerService.refreshProcessDefineCache();
		} catch (Exception e) {
			logger.error("删除流程模板版本异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson("true");
	}

	@Override
	@Transactional
	public String saveXPDL(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		try {
			Long processDefineId = LongHelper.valueOf(map.get("processDefineId"));
			String xpdl = String.valueOf(map.get("xpdl"));
			processDefinitionService.saveXPDL(processDefineId, xpdl);
			txManager.commit(status);
			flowManagerService.refreshProcessDefineCache();
		} catch (Exception e) {
			logger.error("保存流程模板版本异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson("true");
	}

	@Override
	@Transactional
	public String updateFlowState(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		Long processDefineId = LongHelper.valueOf(map.get("processDefineId"));
		try {
			
			Boolean enable = Boolean.valueOf(map.get("enable").toString());
			processDefinitionService.updateFlowState(processDefineId,
					enable ? ProcessDefinitionDto.STATE_ACTIVE
							: ProcessDefinitionDto.STATE_INACTIVE);
			txManager.commit(status);
			flowManagerService.refreshProcessDefineCache();
		} catch (Exception e) {
			logger.error("修改流程模板版本状态异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson(processDefinitionService.qryProcessDefinitionById(
				processDefineId).getTreeJsonObject());
	}

	@Override
	@Transactional
	public String qryFlowParamDefs(Map<String, Object> map) {
		String systemCode = StringHelper.valueOf(map.get("systemCode"));
		return GsonHelper.toJson(processParamDefService.qryProcessParamDefs(systemCode));
	}

	@Override
	@Transactional
	public String addFlowParamDef(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		ProcessParamDefDto dto = new ProcessParamDefDto();
		try {
			
			dto.setCode(StringHelper.valueOf(map.get("code")));
			dto.setName(StringHelper.valueOf(map.get("name")));
			dto.setValue(StringHelper.valueOf(map.get("value")));
			dto.setSystemCode(StringHelper.valueOf(map.get("systemCode")));
			dto.setComments(StringHelper.valueOf(map.get("comments")));
			dto.setIsVariable(IntegerHelper.valueOf(map.get("isVariable")));
			processParamDefService.addProcessParamDef(dto);
			txManager.commit(status);
			flowManagerService.refreshProcessParamDefCache();
		} catch (Exception e) {
			logger.error("添加流程变量异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson(dto);
	}

	@Override
	@Transactional
	public String modFlowParamDef(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		ProcessParamDefDto dto = new ProcessParamDefDto();
		try {
			dto.setCode(StringHelper.valueOf(map.get("code")));
			dto.setName(StringHelper.valueOf(map.get("name")));
			dto.setValue(StringHelper.valueOf(map.get("value")));
			dto.setComments(StringHelper.valueOf(map.get("comments")));
			dto.setIsVariable(IntegerHelper.valueOf(map.get("isVariable")));
			processParamDefService.modProcessParamDef(dto);
			txManager.commit(status);
			flowManagerService.refreshProcessParamDefCache();
		} catch (Exception e) {
			logger.error("修改流程变量异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson(dto);
	}

	@Override
	@Transactional
	public String delFlowParamDef(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		ProcessParamDefDto dto = new ProcessParamDefDto();
		try {
			dto.setCode(StringHelper.valueOf(map.get("code")));
			processParamDefService.delProcessParamDef(dto);
			txManager.commit(status);
			flowManagerService.refreshProcessParamDefCache();
		} catch (Exception e) {
			logger.error("删除流程变量异常，异常原因："+e.getMessage(),e);
			txManager.rollback(status);
		}
		return GsonHelper.toJson(dto);
	}
	
	@Override
	@Transactional
	public String isExistRela(Map<String, Object> map){
		String code = StringHelper.valueOf(map.get("code"));
		boolean isExist = processParamDefService.isExistRela(code);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("isExist", isExist);
		return GsonHelper.toJson(result);
	}

	@Override
	@Transactional
	public String qryFlowParamDefRels(Map<String, Object> map) {
		ProcessParamDefRelDto dto = new ProcessParamDefRelDto();
		Long packageDefineId = LongHelper.valueOf(map.get("packageDefineId"));
		String type = StringHelper.valueOf(map.get("type"));
		String tacheCode = StringHelper.valueOf(map.get("tacheCode"));
		dto.setPackageDefineId(packageDefineId);
		dto.setType(type);
		dto.setTacheCode(tacheCode);
		return GsonHelper.toJson(processParamDefService.qryProcessParamDefRels(dto));
	}

	@Override
	@Transactional
	public String updateBatchFlowParamDefRel(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		List<ProcessParamDefRelDto> dtos = new ArrayList<ProcessParamDefRelDto>();
		if (map != null) {
			try {
				// 删除旧数据
				Long packageDefineId = LongHelper.valueOf(map
						.get("packageDefineId"));// 都是同一个流程定义ID的数据
				String type = StringHelper.valueOf(map.get("type"));
				String tacheCode = StringHelper.valueOf(map.get("tacheCode"));
				
				// 插入新数据
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> rows = (List<Map<String, Object>>) map
						.get("rows");
				for (int i = 0, len = rows.size(); i < len; i++) {
					Map<String, Object> tMap = rows.get(i);
					String code = StringHelper.valueOf(tMap.get("code"));
					int isVariable = IntegerHelper.valueOf(tMap.get("isVariable")).intValue();
					ProcessParamDefRelDto oldDto = new ProcessParamDefRelDto();
					oldDto.setPackageDefineId(packageDefineId);
					oldDto.setCode(code);
					ProcessParamDefRelDto dto = new ProcessParamDefRelDto();
					dto.setPackageDefineId(packageDefineId);
					dto.setCode(StringHelper.valueOf(tMap.get("code")));
					dto.setValue(StringHelper.valueOf(tMap.get("value")));
					oldDto.setType(type);
					dto.setType(type);
					dto.setIsVariable(isVariable);
					if(type.equals("TACHE")){
						oldDto.setTacheCode(tacheCode);
						dto.setTacheCode(tacheCode);
					}else{
						dto.setTacheCode("-1");
					}
					processParamDefService.delProcessParamDefRel(oldDto);
					dtos.add(dto);
				}
				processParamDefService.addBatchProcessParamDefRel(dtos);
				txManager.commit(status);
				flowManagerService.refreshProcessParamDefCache();
			} catch (Exception e) {
				logger.error("批量删除流程变量异常，异常原因："+e.getMessage(),e);
				txManager.rollback(status);
			}
		}
		return GsonHelper.toJson(dtos);
	}
	
	@Override
	@Transactional
	public String delAddBatchFlowParamDefRel(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		List<ProcessParamDefRelDto> dtos = new ArrayList<ProcessParamDefRelDto>();
		if (map != null) {
			try {
				// 删除旧数据
				Long packageDefineId = LongHelper.valueOf(map
						.get("packageDefineId"));// 都是同一个流程定义ID的数据
				String type = StringHelper.valueOf(map.get("type"));
				String tacheCode = StringHelper.valueOf(map.get("tacheCode"));
				
				ProcessParamDefRelDto oldDto = new ProcessParamDefRelDto();
				oldDto.setPackageDefineId(packageDefineId);
				oldDto.setType(type);
				oldDto.setTacheCode(tacheCode);
				processParamDefService.delProcessParamDefRelNoCode(oldDto);
				
				// 插入新数据
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> rows = (List<Map<String, Object>>) map
						.get("rows");
				for (int i = 0, len = rows.size(); i < len; i++) {
					Map<String, Object> tMap = rows.get(i);
					ProcessParamDefRelDto dto = new ProcessParamDefRelDto();
					if("TACHE".equals(type) && "FLOW".equals(StringHelper.valueOf(tMap.get("type")))){
						continue;
					}else{
						dto.setPackageDefineId(packageDefineId);
						dto.setCode(StringHelper.valueOf(tMap.get("code")));
						dto.setValue(StringHelper.valueOf(tMap.get("value")));
						dto.setType(type);
						dto.setTacheCode(tacheCode);
						dto.setIsVariable(IntegerHelper.valueOf(tMap.get("isVariable")));
						dtos.add(dto);
					}
				}
				processParamDefService.addBatchProcessParamDefRel(dtos);
				txManager.commit(status);
				flowManagerService.refreshProcessParamDefCache();
			} catch (Exception e) {
				logger.error("批量删除添加流程变量异常，异常原因："+e.getMessage(),e);
				txManager.rollback(status);
			}
		}
		return GsonHelper.toJson(dtos);
	}

	@Override
	public String findProcessDefinitionsByCodes(Map<String, Object> map) {
		String packageDefineCodes = StringHelper.valueOf(map
				.get("packageDefineCodes"));
		List<ProcessDefinitionDto> processDefineList = processDefinitionService
				.qryProcessDefinitionsByCodes(packageDefineCodes);
		return GsonHelper.toJson(processDefineList);
	}

	public static void main(String[] args) {
		System.out.println("1.12".split("\\.")[1]);
		System.out.println(GsonHelper.toJson("true"));
	}

	@Override
	public String qryDispatchRuleByCond(Map<String, Object> map) {
		String result = null;
		try {// 不分页
			PageDto pageDto = dispatchRuleService.qryDispatchRuleByCond(map);
			result = GsonHelper.toJson(pageDto.getRows());
			logger.debug("result:" + result);
		} catch (Exception ex) {
			logger.error("flowService-qryDispatchRuleByCond error:", ex);
		}
		return result;
	}

	@Override
	@Transactional
	public String addDispatchRule(Map<String, Object> map) {
		String result = null;
		try {
			DispatchRuleDto dto = new DispatchRuleDto();
			Long id = SequenceHelper.getId("UOS_DISPATCH_RULE");
			dto.setId(id);
			dto.setPackageDefineId(MapUtils.getString(map, "packageDefineId"));
			dto.setTacheId(MapUtils.getLong(map, "tacheId"));
			dto.setTacheCode(MapUtils.getString(map, "tacheCode"));
			dto.setAreaId(MapUtils.getLong(map, "areaId"));
			dto.setAreaName(MapUtils.getString(map, "areaName"));
			dto.setType(MapUtils.getString(map, "type"));
			dto.setRollbackType(MapUtils.getString(map, "rollbackType"));
			dto.setApplyAll(MapUtils.getString(map, "applyAll", "0"));
			dto.setPartyType(MapUtils.getString(map, "partyType"));
			dto.setPartyId(MapUtils.getString(map, "partyId"));
			dto.setPartyName(MapUtils.getString(map, "partyName"));
			dto.setManualPartyType(MapUtils.getString(map, "manualPartyType"));
			dto.setManualPartyId(MapUtils.getString(map, "manualPartyId"));
			dto.setManualPartyName(MapUtils.getString(map, "manualPartyName"));
			dto.setCallType(MapUtils.getString(map, "callType"));
			dto.setBizId(MapUtils.getLong(map, "bizId"));
			dto.setBizName(MapUtils.getString(map, "bizName"));
			dto.setIsAutomaticReturn(MapUtils.getString(map,
					"isAutomaticReturn", "1"));
			dto.setIsAutoManual(MapUtils.getString(map, "isAutoManual", "1"));
			dto.setIsReverseAutomaticReturn(MapUtils.getString(map,
					"isReverseAutomaticReturn", "1"));
			dto.setIsReverseAutomaticManual(MapUtils.getString(map,
					"isReverseAutomaticManual", "1"));
			result = GsonHelper.toJson(dispatchRuleService.addDispatchRule(dto));
			logger.debug("result:" + result);
		} catch (Exception ex) {
			logger.error("flowService-addDispatchRule error:", ex);
		}
		return result;
	}

	@Override
	@Transactional
	public String modDispatchRule(Map<String, Object> map) {
		String result = null;
		try {
			DispatchRuleDto dto = new DispatchRuleDto();
			dto.setId(MapUtils.getLong(map, "id"));
			dto.setAreaId(MapUtils.getLong(map, "areaId"));
			dto.setAreaName(MapUtils.getString(map, "areaName"));
			dto.setType(MapUtils.getString(map, "type"));
			dto.setRollbackType(MapUtils.getString(map, "rollbackType"));
			dto.setApplyAll(MapUtils.getString(map, "applyAll", "0"));
			dto.setPartyType(MapUtils.getString(map, "partyType"));
			dto.setPartyId(MapUtils.getString(map, "partyId"));
			dto.setPartyName(MapUtils.getString(map, "partyName"));
			dto.setManualPartyType(MapUtils.getString(map, "manualPartyType"));
			dto.setManualPartyId(MapUtils.getString(map, "manualPartyId"));
			dto.setManualPartyName(MapUtils.getString(map, "manualPartyName"));
			dto.setCallType(MapUtils.getString(map, "callType"));
			dto.setBizId(MapUtils.getLong(map, "bizId"));
			dto.setBizName(MapUtils.getString(map, "bizName"));
			dto.setIsAutomaticReturn(MapUtils.getString(map,
					"isAutomaticReturn", "1"));
			dto.setIsAutoManual(MapUtils.getString(map, "isAutoManual", "1"));
			dto.setIsReverseAutomaticReturn(MapUtils.getString(map,
					"isReverseAutomaticReturn", "1"));
			dto.setIsReverseAutomaticManual(MapUtils.getString(map,
					"isReverseAutomaticManual", "1"));
			dto.setStateDate(DateHelper.getTimeStamp());
			dispatchRuleService.modDispatchRule(dto);
			// result =
			// isSuccess>0?"{\"isSuccess\":true}":"{\"isSuccess\":false}";
			result = "{\"isSuccess\":true}";
			logger.debug("result:" + result);
		} catch (Exception ex) {
			result = "{\"isSuccess\":false}";
			logger.error("flowService-modDispatchRule error:", ex);
		}
		return result;
	}

	@Override
	@Transactional
	public String delDispatchRule(Map<String, Object> map) {
		String result = null;
		try {

			Long id = MapUtils.getLong(map, "id");
			dispatchRuleService.delDispatchRule(id);
			result = "{\"isSuccess\":true}";
			logger.debug("result:" + result);
		} catch (Exception ex) {
			result = "{\"isSuccess\":false}";
			logger.error("flowService-delDispatchRule error:", ex);
		}
		return result;
	}

	@Override
	public String qryProcessDefineByName(Map<String, Object> map) throws Exception {
		try {
			String result = "";
			PackageDto[] packageDtos = processPackageService.qryPackageByName(map);
			// 根据流程模板获取流程版本部分
			ProcessDefinitionDto[] processDefinitionDtos = null;
			if (packageDtos != null && packageDtos.length > 0) {
				StringBuffer packageIds = new StringBuffer();
				for (int i = 0; i < packageDtos.length; i++) {
					if (i != packageDtos.length - 1) {
						packageIds.append(packageDtos[i].getPackageId() + ",");
					} else {
						packageIds.append(packageDtos[i].getPackageId());
					}
				}
				processDefinitionDtos = processDefinitionService
						.qryProcessDefineByPackageIds(packageIds.toString());
				result = this.defineArrayToTreeJson(packageDtos, processDefinitionDtos);
			}
			logger.debug("result:" + result);
			return result;
		} catch (Exception ex) {
			logger.error("tacheService-qryTaches error:", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public String saveProcessDefsAsNew(Map<String, Object> map) {
		txManager = JDBCHelper.getTransactionManager();
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		status = txManager.getTransaction(def);
		String result = null;
		try {
			String oldProcessDefId = StringHelper.valueOf(map.get("oldProcessDefId"));
			Long newProcessDefId = LongHelper.valueOf(map.get("newProcessDefId"));
			List<ProcessParamDefRelDto> defDtos = processParamDefService.queryParamDefRelsByDefId(oldProcessDefId.toString());
			if(defDtos != null && defDtos.size()>0){
				for(ProcessParamDefRelDto dto:defDtos){
					dto.setPackageDefineId(newProcessDefId);
				}
				processParamDefService.addBatchProcessParamDefRel(defDtos);
			}
			//异常原因由模板编码关联，新增版本，模板编码不变，不需要拷贝异常原因配置 modify by bobping
//			List<ReturnReasonConfigDto> configDtos = returnReasonService.qryReturnReasonConfigsByDefId(oldProcessDefId);
//			if(configDtos != null && configDtos.size()>0){
//				for(ReturnReasonConfigDto dto:configDtos){
//					dto.setId(null);
//					dto.setPackageDefineId(StringHelper.valueOf(newProcessDefId));
//				}
//				returnReasonService.saveReturnReasonConfigs(StringHelper.valueOf(newProcessDefId), configDtos);
//			}
			result = "{\"isSuccess\":true}";
			txManager.commit(status);
			flowManagerService.refreshProcessParamDefCache();
			flowManagerService.refreshReturnReasonConfigCache();
		} catch (Exception e) {
			result = "{\"isSuccess\":false}";
			txManager.rollback(status);
			logger.error("----拷贝流程模板配置到新版本异常，异常原因："+e.getMessage(),e);
		}
		return result;
	}
}
