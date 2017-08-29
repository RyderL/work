package com.ztesoft.uosflow.util.cache;

/**
 * ��������ӿ�
 * @author yuxiao
 *
 */
public interface CacheInterface {
	public String getAddress();
	/**
	 * �ӻ����л�ȡ����
	 * 
	 * @param cacheName
	 *            ��������
	 * @param key
	 * @return
	 */
	public Object get(String cacheName, String key) throws Exception;

	/**
	 * �򻺴�����������
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public void set(String cacheName, String key, Object value) throws Exception;

	/**
	 * �жϻ������Ƿ����
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public boolean contain(String cacheName, String key) throws Exception;

	/**
	 * ��ջ���
	 */
	public void clearAll() throws Exception;
	
	/**
	 * ����keyֵɾ������
	 * 
	 * @param cacheName
	 * @param key
	 * @throws Exception
	 */
	public void remove(String cacheName, String key) throws Exception;
	
}
