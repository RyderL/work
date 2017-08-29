package com.ztesoft.uosflow.web.service.flow;

import java.util.Map;

/**
 * 
 * ���̶��崦��ӿ�
 * 
 * @author gong.yi 2015.05.05
 * 
 */
public interface FlowServ {
	/**
	 * ��������id��ϵͳ�����ȡ����Ŀ¼�б�
	 * 
	 * @param params
	 * @return
	 */
	public String queryPackageCatalogByAreaIdAndSystemCode(
			Map<String, Object> map);

	/**
	 * ��������Ŀ¼id��ȡ���̶����б�
	 * 
	 * @param params
	 * @return
	 */
	public String qryPackageDefineByCatalogId(Map<String, Object> map);

	/**
	 * ��������ģ��id��ȡ���̶����б�
	 * 
	 * @param params
	 * @return
	 */
	public String qryPackageDefineByPackageId(Map<String, Object> map);

	/**
	 * �������̶���id��ȡ���̶��屨��
	 * 
	 * @param params
	 * @return
	 */
	public String getXPDL(Map<String, Object> map);

	/**
	 * �������̶���id�������̶�����Ϣ
	 * 
	 * @param params
	 * @return
	 */
	public String findProcessDefinitionById(Map<String, Object> map);

	/**
	 * �������̶���code�������̶�����Ϣ
	 * 
	 * @param params
	 * @return
	 */
	public String findProcessDefinitionByCode(Map<String, Object> map);

	/**
	 * ��������ģ��id��������ģ����Ϣ
	 * 
	 * @param params
	 * @return
	 */
	public String findPackageById(Map<String, Object> map);

	/**
	 * �������Ŀ¼
	 * 
	 * @param params
	 * @return
	 */
	public String addPackageCatalog(Map<String, Object> map);

	/**
	 * �޸�Ŀ¼
	 * 
	 * @param params
	 * @return
	 */
	public String updatePackageCatalog(Map<String, Object> map);

	/**
	 * ɾ������Ŀ¼
	 * 
	 * @param params
	 * @return
	 */
	public String deletePackageCatalog(Map<String, Object> map);

	/**
	 * ��������ģ��
	 * 
	 * @param params
	 * @return
	 */
	public String addPackage(Map<String, Object> map);

	/**
	 * �޸�����ģ��
	 * 
	 * @param params
	 * @return
	 */
	public String updatePackage(Map<String, Object> map);

	/**
	 * ɾ������ģ��
	 * 
	 * @param params
	 * @return
	 */
	public String deletePackage(Map<String, Object> map);

	/**
	 * ������̶���
	 * 
	 * @param params
	 * @return
	 */
	public String addProcessDefine(Map<String, Object> map);

	/**
	 * ɾ�����̶���
	 * 
	 * @param params
	 * @return
	 */
	public String deleteProcessDefine(Map<String, Object> map);

	/**
	 * �������̶���
	 * 
	 * @param params
	 * @return
	 */
	public String saveXPDL(Map<String, Object> map);

	/**
	 * ���̰汾��Ч��ʧЧ
	 * 
	 * @param params
	 * @return
	 */
	public String updateFlowState(Map<String, Object> map);

	/**
	 * ��ȡ���̲�����Ϣ
	 */
	public String qryFlowParamDefs(Map<String, Object> map);

	/**
	 * �������̲���
	 */
	public String addFlowParamDef(Map<String, Object> map);

	/**
	 * �޸����̲���
	 */
	public String modFlowParamDef(Map<String, Object> map);

	/**
	 * ɾ�����̲���
	 */
	public String delFlowParamDef(Map<String, Object> map);
	
	/**
	 * ɾ�����̲���
	 */
	public String delAddBatchFlowParamDefRel(Map<String, Object> map) ;
	/**
	 * �����̲����Ƿ�ʹ�õ�ģ����
	 */
	public String isExistRela(Map<String, Object> map);
	/**
	 * ��ȡ���̲�����ϵ��Ϣ
	 */
	public String qryFlowParamDefRels(Map<String, Object> map);

	/**
	 * �����������̲�����ϵ��ɾ����ֵ���ز���ֵ
	 */
	public String updateBatchFlowParamDefRel(Map<String, Object> map);

	/**
	 * ���ݶ�����̶���codes���Ҷ�����̶�����Ϣ
	 * 
	 * @param params
	 * @return
	 */
	public String findProcessDefinitionsByCodes(Map<String, Object> map);

	/**
	 * ��ѯ�ɷ�����
	 * @param map
	 * @return
	 */
	public String qryDispatchRuleByCond(Map<String, Object> map);
	/**
	 * �����ɷ�����
	 * @param map
	 * @return
	 */
	public String addDispatchRule(Map<String, Object> map);
	/**
	 * �޸��ɷ�����
	 * @param map
	 * @return
	 */
	public String modDispatchRule(Map<String, Object> map);
	/**
	 * ɾ���ɷ�����
	 * @param map
	 * @return
	 */
	public String delDispatchRule(Map<String, Object> map);
	
	/**
	 * ����Ŀ¼id��ѯ����ģ����Ϣ�����������̰汾��Ϣ��
	 * @param map
	 * @return
	 */
	public String qryProcessDefineByCatalogId(Map<String,Object> map);
	
	/**
	 * ��������ģ�����Ʋ�ѯ����ģ����Ϣ
	 * @param map
	 * @return
	 */
	public String qryProcessDefineByName(Map<String,Object> map) throws Exception;
	
	/**
	 * ������ģ�����������Ǩ�Ƶ��°汾��
	 * @param map
	 * @return
	 */
	public String saveProcessDefsAsNew(Map<String,Object> map);
}
