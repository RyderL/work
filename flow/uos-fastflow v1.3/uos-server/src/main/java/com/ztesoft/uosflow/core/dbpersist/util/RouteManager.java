package com.ztesoft.uosflow.core.dbpersist.util;

import org.apache.commons.lang.StringUtils;

import com.zterc.uos.fastflow.exception.FastflowException;

/**
 * 路由管理 zhong.kaijie 2016年4月8日下午12:47:46
 */
public class RouteManager {

	/**
	 * 取模长度 zhong.kaijie 2016年4月8日 下午1:17:33
	 */
	public static int SEED_LENGTH = 2;
	
	/**
	 * 取模值
	 */
	public static int MOD_VALUE=1024;
	/**
	 * 获取路由结果索引
	 * @param routeKey
	 * @param routeResultArrayLength
	 * @return
	 * @author  zhong.kaijie  on 2017年4月13日 下午3:46:12
	 * @version 1.0.0
	 * @throws Exception 
	 */
	public static int getRouteResultIndex(String routeKey,int routeResultArrayLength) {
		int seedKey = getSeedKey(routeKey);
		int result = Math.abs(Integer.valueOf(seedKey))%routeResultArrayLength;
		return result;
	}
	
	/**
	 * 获取字符串类型路由key获取int路由key。
	 * 算法为：如果是纯数字，则取最后四位；如果非纯数字，则先取hashCode再遵循纯数字的算法。
	 * 
	 * @param routeKey
	 * @return zhong.kaijie 2016年4月11日 上午10:22:29
	 * @throws Exception 
	 */
	public static int getSeedKey(String routeKey){
		int result=0;
		if (StringUtils.isEmpty(routeKey)) {
			throw new FastflowException("传入的路由关键字routeKey不能为空!");
		} else {
			if (!StringUtils.isNumericSpace(routeKey)) {
				// 如果不是数字，则返回hashCode;
				result= routeKey.hashCode();
			} else {
				// 取模长度
				if (routeKey.length() < SEED_LENGTH) {
					// throw new OssException("00005",
					// "传入的路由关键字routeKey不能少于" + SEED_LENGTH + "位数!");
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
