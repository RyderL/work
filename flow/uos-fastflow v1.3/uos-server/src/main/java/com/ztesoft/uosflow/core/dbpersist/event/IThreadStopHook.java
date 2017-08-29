package com.ztesoft.uosflow.core.dbpersist.event;

/**
 * 进程关闭钩子接口
 * zhong.kaijie	  2015年12月29日
 */
public interface IThreadStopHook {

	/**
	 * 进程关闭时触发
	 * zhong.kaijie	 2015年12月29日
	 */
	void stopEvent(); 
}
