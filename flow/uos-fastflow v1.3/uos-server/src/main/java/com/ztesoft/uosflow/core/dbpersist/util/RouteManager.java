package com.ztesoft.uosflow.core.dbpersist.util;

import org.apache.commons.lang.StringUtils;

import com.zterc.uos.fastflow.exception.FastflowException;

/**
 * ·�ɹ��� zhong.kaijie 2016��4��8������12:47:46
 */
public class RouteManager {

	/**
	 * ȡģ���� zhong.kaijie 2016��4��8�� ����1:17:33
	 */
	public static int SEED_LENGTH = 2;
	
	/**
	 * ȡģֵ
	 */
	public static int MOD_VALUE=1024;
	/**
	 * ��ȡ·�ɽ������
	 * @param routeKey
	 * @param routeResultArrayLength
	 * @return
	 * @author  zhong.kaijie  on 2017��4��13�� ����3:46:12
	 * @version 1.0.0
	 * @throws Exception 
	 */
	public static int getRouteResultIndex(String routeKey,int routeResultArrayLength) {
		int seedKey = getSeedKey(routeKey);
		int result = Math.abs(Integer.valueOf(seedKey))%routeResultArrayLength;
		return result;
	}
	
	/**
	 * ��ȡ�ַ�������·��key��ȡint·��key��
	 * �㷨Ϊ������Ǵ����֣���ȡ�����λ������Ǵ����֣�����ȡhashCode����ѭ�����ֵ��㷨��
	 * 
	 * @param routeKey
	 * @return zhong.kaijie 2016��4��11�� ����10:22:29
	 * @throws Exception 
	 */
	public static int getSeedKey(String routeKey){
		int result=0;
		if (StringUtils.isEmpty(routeKey)) {
			throw new FastflowException("�����·�ɹؼ���routeKey����Ϊ��!");
		} else {
			if (!StringUtils.isNumericSpace(routeKey)) {
				// ����������֣��򷵻�hashCode;
				result= routeKey.hashCode();
			} else {
				// ȡģ����
				if (routeKey.length() < SEED_LENGTH) {
					// throw new OssException("00005",
					// "�����·�ɹؼ���routeKey��������" + SEED_LENGTH + "λ��!");
					result=Integer.valueOf(routeKey);
				} else {
					String seedKey = routeKey;
					if (routeKey.length() > SEED_LENGTH) {

						seedKey = routeKey
								.substring(routeKey.length() - SEED_LENGTH);
					}
					result= Integer.valueOf(seedKey);
				}
			}
		}
		result=Math.abs(result);
		return result;
	}

}
