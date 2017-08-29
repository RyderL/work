package com.zterc.uos.fastflow.constant;

public class CommonDomain {
	// �ɹ���־
	public static final String FLAG_SUCCESS = "Y"; // �ɹ�
	public static final String FLAG_FAIL = "N"; // ʧ��

	public static final String TIME_UNIT_DAY = "DAY"; // ����
	public static final String TIME_UNIT_HOR = "HOR"; // ����
	public static final String TIME_UNIT_MIN = "MIN"; // ����

	// ϵͳĬ�ϲ���
	public static final String SYSTEM_STAFF = "-1"; // ϵͳִ����
	public static final String SYSTEM_POSITION = "-1"; // ϵͳ��λ
	public static final String SYSTEM_CRM_STAFF = "-1"; // CRMϵͳִ����
	public static final String SYSTEM_ORG = "-1"; // ϵͳ��֯
	public static final String SYSTEM_PASSWORD = ""; // ϵͳ����

	// ִ��������
	public static final String PARTY_TYPE_ORG = "ORG"; // ��֯
	public static final String PARTY_TYPE_JOB = "JOB"; // ְλ
	public static final String PARTY_TYPE_STA = "STA"; // Ա��
	public static final String PARTY_TYPE_SYS = "SYS"; // ϵͳ

	// �쳣ԭ�����
	public static final String UOS_REASON_TYPE_RETURN = "10R"; // �˵�
	public static final String UOS_REASON_TYPE_WAIT = "10W"; // ��װ
	public static final String UOS_REASON_TYPE_PAUSE = "10P"; // ��װ
	public static final String UOS_REASON_TYPE_BACK = "10B"; // ��̨��װ
	public static final String UOS_REASON_TYPE_CANCEL = "10C"; // ����

	// ͬ������
	public static final String UOS_SYN_TYPE_SYN = "001"; // ͬ��
	public static final String UOS_SYN_TYPE_DEPEND = "002"; // ����

	// ״̬
	public static final String STATE_ACTIVE = "10A"; // ����״̬
	public static final String STATE_NORMAL = "10N"; // ����״̬

	// �������
	public static final String BUSINESS_CALSS_TYPE_100 = "100"; // ����SQL
	public static final String BUSINESS_CALSS_TYPE_110 = "110"; // EJB
	public static final String BUSINESS_CALSS_TYPE_120 = "120"; // SERVICE_BEAN
	public static final String BUSINESS_CALSS_TYPE_130 = "130"; // ���ô洢����
	public static final String BUSINESS_CALSS_TYPE_140 = "140"; // ����JAVABEAN

	// ������Ϣ����
	public static final String BUSINESS_EXECUTE_PARAM_ERROR = "����Ĳ��������ֵ�벻ƥ��";
	public static final String BUSINESS_EXECUTE_PARAM_NULL = "���δ���ò���";
	public static final String BUSINESS_EXECUTE_ERROR = "ִ������쳣";

	// ����״̬֪ͨ
	public static final int WM_START_REPORT = 1; // ��ʼ֪ͨ
	public static final int WM_END_REPORT = 2; // ����֪ͨ
	public static final int WM_ERROR_REPORT = 3; // �쳣֪ͨ
	public static final int WM_NORMAL_REPORT = 4; // ��������֪ͨ
	public static final int WM_ZERO_REPORT = 5; // ���˵����֪ͨ
	public static final int WM_ROLLBACK_REPORT = 6; // ��װ����װ������֪ͨ
	public static final int WM_RESUME_REPORT = 7; // ��װ�����װ����֪ͨ
	public static final int WM_CHANGE_REPORT = 8; // �����֪ͨ
	public static final int WM_SYNWAIT_REPORT = 9; // ͬ���ȴ�֪ͨ
	public static final int WM_WAITTONORMAL_REPORT = 10; // �ȴ��ָ�����֪ͨ
	public static final int WM_ROLLBACKTONOMAL_REPORT = 11;// �˵�����֪ͨ
	public static final int WM_WAITROLLBACK_REPORT = 12; // ��װ�˵�����֪ͨ
	
	 //����ʱ��ʱ���ǽڼ���
    public static final String IS_CALCULATE_WORKTIME_YES = "1";    
    //����ʱ��ʱ�䵥λ
    public static final String YEAR = "YEAR";
    public static final String DAY = "DAY";
    public static final String HOR = "HOR";
    public static final String MIN = "MIN";

}
