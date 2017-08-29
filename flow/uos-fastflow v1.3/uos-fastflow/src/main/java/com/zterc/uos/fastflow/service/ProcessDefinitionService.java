package com.zterc.uos.fastflow.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StaticCacheHelper;
import com.zterc.uos.base.helper.StreamHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.config.FastflowConfig;
import com.zterc.uos.fastflow.dao.process.ProcessDefinitionDAO;
import com.zterc.uos.fastflow.dto.process.ProcessDefinitionDto;
import com.zterc.uos.fastflow.model.WorkflowPackage;
import com.zterc.uos.fastflow.model.WorkflowProcess;
import com.zterc.uos.fastflow.parse.XPDLParser;

/**
 * �����壩���̶��������
 * 
 * @author gong.yi
 * 
 */
public class ProcessDefinitionService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessDefinitionService.class);

	public static final String PROCESS_DEFINITION_CACHE = "PROCESS_DEFINITION_CACHE";
	public static final String PROCESS_CODE_ID_CACHE = "PROCESS_CODE_ID_CACHE";

	private ProcessDefinitionDAO processDefinitionDAO;

	public void setProcessDefinitionDAO(
			ProcessDefinitionDAO processDefinitionDAO) {
		this.processDefinitionDAO = processDefinitionDAO;
	}

	/**
	 * �����棩�������̶�������ѯ���̶���
	 * 
	 * @param processDefinitionCode
	 * @return
	 * @throws DocumentException
	 * @throws SQLException
	 * @throws IOException
	 */
	public ProcessDefinitionDto queryProcessDefinitionByCode(
			String processDefinitionCode) throws DocumentException,
			SQLException, IOException {
		String processDefinitionId = getCachedIdByCode(processDefinitionCode);
		LOGGER.info("----processDefinitionCode:"+processDefinitionCode+",processDefinitionId:"+processDefinitionId);
		ProcessDefinitionDto processDefinitionDto = getCacheProcessDefinition(processDefinitionId);

		if (processDefinitionDto == null
				&& !FastflowConfig.loadStaticCache) {
			processDefinitionDto = processDefinitionDAO
					.qryProcessDefinitionByCode(processDefinitionCode);
			setCacheProcessDefinition(processDefinitionDto);
		}
		return processDefinitionDto;
	}

	/**
	 * �����棩�������̶���id��ѯ���̶���
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public ProcessDefinitionDto queryProcessDefinitionById(
			String processDefinitionId) {
		ProcessDefinitionDto processDefinitionDto = getCacheProcessDefinition(processDefinitionId);

//		if ((processDefinitionDto == null
//				|| processDefinitionDto.getWorkflowProcess() == null)
//				&& !FastflowConfig.loadStaticCache) {
//			processDefinitionDto = processDefinitionDAO
//					.qryProcessDefinitionById(LongHelper
//							.valueOf(processDefinitionId));
//			setCacheProcessDefinition(processDefinitionDto);
//		}
		if(!FastflowConfig.loadStaticCache){
			if(processDefinitionDto != null){
				if(processDefinitionDto.getWorkflowProcess() == null){
					processDefinitionDto = processDefinitionDAO
							.qryProcessDefinitionById(LongHelper
									.valueOf(processDefinitionId));
					setCacheProcessDefinition(processDefinitionDto);
				}
			}else{
				processDefinitionDto = processDefinitionDAO
						.qryProcessDefinitionById(LongHelper
								.valueOf(processDefinitionId));
				setCacheProcessDefinition(processDefinitionDto);
			}
		}
		return processDefinitionDto;
	}

	/**
	 * �����棩�������̶���id��ѯ����xpdl����
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public WorkflowProcess findWorkflowProcessById(String processDefinitionId) {
		ProcessDefinitionDto processDefinitionDto = queryProcessDefinitionById(processDefinitionId);
		if (processDefinitionDto == null) {
			throw new RuntimeException("���̶��岻���ڣ�[processDefinitionId="
					+ processDefinitionId + "]");
		}
		return processDefinitionDto.getWorkflowProcess();
	}

	private ProcessDefinitionDto getCacheProcessDefinition(
			String processDefinitionId) {
		return (ProcessDefinitionDto) StaticCacheHelper.get(
				PROCESS_DEFINITION_CACHE, processDefinitionId);
	}

	private void setCacheProcessDefinition(
			ProcessDefinitionDto processDefinitionDto) {
		if (processDefinitionDto != null) {
			try {
				WorkflowProcess process = processDefinitionDto
						.getWorkflowProcess();
				if (process == null && processDefinitionDto.getXpdl() != null) {
					String xml = new String(
							StreamHelper.readBytes(processDefinitionDto
									.getXpdl().getBinaryStream()));
					InputStream inputStream = StreamHelper
							.getInputeStreamFromString(xml);
					LOGGER.debug("�������̶���XML��..., Id:"
							+ processDefinitionDto.getPackageDefineId());
					WorkflowPackage pkg = XPDLParser.parse(inputStream);
					process = pkg.getWorkflowProcess();
					process.setState(processDefinitionDto.getState());
					process.setName(processDefinitionDto.getName());
					processDefinitionDto.setWorkflowProcess(process);
					processDefinitionDto.setXpdlContent(xml);
					processDefinitionDto.setXpdl(null);
				}
				StaticCacheHelper.set(PROCESS_DEFINITION_CACHE,
						processDefinitionDto.getPackageDefineId().toString(),
						processDefinitionDto);
				/**
				 * Ϊ�����޸�������ģ��֮��ԭʹ�þ�����ģ�����;�����ܰ������̼����ߣ��������»����޸�
				 * ����ģ�嶨����idΪkey��ȫ�����ص�ehcache���棬����codeΪkey���ҽ���һ����Ч��¼�ɱ�
				 * ��ȡ��
				 * modify by bobping
				 */
				if (ProcessDefinitionDto.STATE_ACTIVE.equals(processDefinitionDto.getState())) {
					StaticCacheHelper.set(PROCESS_CODE_ID_CACHE, processDefinitionDto.getPackageDefineCode().toString(),
							StringHelper.valueOf(processDefinitionDto.getPackageDefineId()));
				}

			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	private String getCachedIdByCode(String processDefinitionCode) {
		return (String) StaticCacheHelper.get(PROCESS_CODE_ID_CACHE,
				processDefinitionCode);
	}

	public ProcessDefinitionDto[] qryProcessDefineByPackageIds(String packageIds) {
		return processDefinitionDAO.qryProcessDefineByPackageIds(packageIds);
	}

	public ProcessDefinitionDto qryProcessDefinitionById(Long processDefineId) {
		return processDefinitionDAO.qryProcessDefinitionById(processDefineId);
	}

	public ProcessDefinitionDto qryProcessDefinitionByCode(
			String processDefineCode) {
		return processDefinitionDAO
				.qryProcessDefinitionByCode(processDefineCode);
	}

	public List<ProcessDefinitionDto> qryProcessDefinitionsByCodes(
			String processDefineCodes) {
		return processDefinitionDAO
				.qryProcessDefinitionsByCodes(processDefineCodes);
	}

	public ProcessDefinitionDto[] qryProcessDefinitionsByPackageId(
			Long packageId) {
		return processDefinitionDAO.qryProcessDefinitionsByPackageId(packageId);
	}

	public void addProcessDefine(ProcessDefinitionDto dto) {
		processDefinitionDAO.addProcessDefinition(dto);
	}

	public void updateProcessDefineNameByPackageId(Long packageId,
			String packageName) {
		processDefinitionDAO.updateProcessDefinitionNameByPackageId(packageId,
				packageName);
	}

	public void deleteProcessDefine(ProcessDefinitionDto dto) {
		processDefinitionDAO.deleteProcessDefinition(dto);
	}

	public void saveXPDL(Long processDefineId, String xpdl) {
		processDefinitionDAO.saveXPDL(processDefineId, xpdl);
	}

	public void updateFlowState(Long processDefineId, String state) {
		processDefinitionDAO.updateFlowState(processDefineId, state);
	}

	public Map<String, Object> qryAllProcessDefinitionByCond(
			Map<String, String> paramMap) {
		return processDefinitionDAO.qryAllProcessDefinitionByCond(paramMap);
	}
	
	public void loadAllProcessDefinition(){
		LOGGER.info("ˢ������ģ�嶨������.....");
		List<ProcessDefinitionDto> processDefinitionDtos = processDefinitionDAO.qryAllProcessDefines();
		for(ProcessDefinitionDto processDefinitionDto:processDefinitionDtos){
			setCacheProcessDefinition(processDefinitionDto);
		}
		LOGGER.info("ˢ������ģ�嶨�����ý���");
	}

	public String qryPackageDefinePath(Long processInstanceId) {
		return processDefinitionDAO.qryPackageDefinePath(processInstanceId);
	}
}
