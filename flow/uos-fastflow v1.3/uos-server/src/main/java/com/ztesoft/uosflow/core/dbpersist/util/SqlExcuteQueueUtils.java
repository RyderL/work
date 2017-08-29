package com.ztesoft.uosflow.core.dbpersist.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.ztesoft.uosflow.core.dbpersist.dto.AsyncSqlDto;
import com.ztesoft.uosflow.core.dbpersist.event.IThreadStopHook;
import com.ztesoft.uosflow.core.dbpersist.event.SqlExcuteQueueEvent;

@Component
public class SqlExcuteQueueUtils implements IThreadStopHook{
	private static Logger logger = Logger.getLogger(SqlPersistQueueUtils.class);
	private   int sqlPersistQueueCount=5;
	private  int maxBatchDealCount=1000;
	private  long freeSleepTime= 100L;
	public static long busyCommitTime=30*1000L;
	private  static Set<Integer> busyQueue=new ConcurrentHashSet<>();
	private Map<String, Map<String,Queue<AsyncSqlDto>>> dataSourceQueueMap=new ConcurrentHashMap<>();
	private  Map<String,Queue<AsyncSqlDto>> sqlQueueMap= new ConcurrentHashMap<String,Queue<AsyncSqlDto>>();
	
	/**
	 * 将异步持久化对象放入队列中
	 * @param asyncSqlDto
	 * @author  zhong.kaijie  on 2017年3月2日 下午9:04:13
	 * @version 1.0.0
	 */
	public  void  putToQueue(AsyncSqlDto asyncSqlDto){
		if (asyncSqlDto!=null) {	
			Queue<AsyncSqlDto> sqlQueue=getSqlQueue(asyncSqlDto);
			sqlQueue.add(asyncSqlDto);
			if (sqlQueue.size()>maxBatchDealCount) {
				busyQueue.add(sqlQueue.hashCode());
			}else{
				busyQueue.remove(sqlQueue.hashCode());
			}
		}
	}
	/**
	 * 将异步持久化对象集合放入队列中
	 * @param asyncSqlDtos
	 * @author  zhong.kaijie  on 2017年3月2日 下午9:04:13
	 * @version 1.0.0
	 */
	public  void  putToQueue(List<AsyncSqlDto> asyncSqlDtos){
		if (CollectionUtils.isEmpty(asyncSqlDtos)) {
			return;
		}else{
			for (AsyncSqlDto temp : asyncSqlDtos) {
				putToQueue(temp);
			}
		}
	}
	
	/**
	 * 根据传入的asyncSqlDto获取到对应的队列
	 * @param asyncSqlDto
	 * @return
	 * @author  zhong.kaijie  on 2017年3月2日 下午10:51:00
	 * @version 1.0.0
	 */
	Queue<AsyncSqlDto>  getSqlQueue(AsyncSqlDto asyncSqlDto){
		String tableName=asyncSqlDto.getTableName();
		if (StringUtils.isEmpty(tableName)) {
			tableName="notableName";
		}
		Queue<AsyncSqlDto> sqlQueue=getSqlQueue(sqlQueueMap,tableName,asyncSqlDto.getKey());
		return sqlQueue;
	}
	
	/**
	 * 根据传入的dataSourceBeanId获取到对应的队列Map。
	 * @param dataSourceBeanId
	 * @return
	 * @author  zhong.kaijie  on 2017年3月2日 下午10:51:23
	 * @version 1.0.0
	 */
	Map<String,Queue<AsyncSqlDto>>  getSqlQueueMap(String dataSourceBeanId){
		Map<String,Queue<AsyncSqlDto>> sqlQueueMap=  dataSourceQueueMap.get(dataSourceBeanId);
		if (sqlQueueMap==null) {
			synchronized (SqlPersistQueueUtils.class) {
				sqlQueueMap=  dataSourceQueueMap.get(dataSourceBeanId);
				if (sqlQueueMap==null) {
					sqlQueueMap=new ConcurrentHashMap<>();
					dataSourceQueueMap.put(dataSourceBeanId, sqlQueueMap);
				}
			}
		}
		return  sqlQueueMap;
	}
	
	/**
	 * 根据key进行hash获取队列。
	 * @param sqlQueueMap
	 * @param key
	 * @return
	 * @author  zhong.kaijie  on 2017年3月2日 下午10:33:33
	 * @version 1.0.0
	 */
	Queue<AsyncSqlDto>  getSqlQueue(Map<String,Queue<AsyncSqlDto>> sqlQueueMap,String tableName,String key){
		int queueIndex=RouteManager.getRouteResultIndex(key, sqlPersistQueueCount);
		String queueKey=tableName+"-"+queueIndex;
		Queue<AsyncSqlDto> sqlQueue=sqlQueueMap.get(queueKey);
		if (sqlQueue==null) {
			synchronized (SqlPersistQueueUtils.class) {
				sqlQueue=  sqlQueueMap.get(queueKey);
				if (sqlQueue==null) {
					sqlQueue=createNewQueue();
					sqlQueueMap.put(queueKey, sqlQueue);
					Thread thread= new SqlExcuteQueueEvent(sqlQueue,maxBatchDealCount,freeSleepTime);
					thread.setName("SqlPersistQueueEvent-"+queueKey);
					thread.start();
				}
			}
		}
		return  sqlQueue;
	}
	
	/**
	 * 创建承载消息的队列
	 * @return
	 * @author  zhong.kaijie  on 2017年3月14日 上午10:14:17
	 * @version 1.0.0
	 */
	Queue<AsyncSqlDto>  createNewQueue(){
		return new ConcurrentLinkedQueue<>();
	}
	
	
	/**
	 * 判断队列是否是繁忙的状态
	 * @param queue 队列
	 * @return
	 * @author  zhong.kaijie  on 2017年3月10日 下午3:08:35
	 * @version 1.0.0
	 */
	public static boolean isBusyQueue(Queue<AsyncSqlDto> queue){
		boolean result=false;
		if (busyQueue.size()>0&&busyQueue.contains(queue.hashCode())) {
			result=true;
		}
		return result;
	}
	
	/**
	 * 判断是否是繁忙的状态
	 * @param queue 队列
	 * @return
	 * @author  zhong.kaijie  on 2017年3月10日 下午3:08:35
	 * @version 1.0.0
	 */
	public static boolean isBusy(){
		boolean result=busyQueue.size()>0;
		return result;
	}
	
	@Override
	public void stopEvent() {
		Collection<Map<String,Queue<AsyncSqlDto>>> AsyncSqlDtoMaps= dataSourceQueueMap.values();
		for (Map<String,Queue<AsyncSqlDto>> asyncSqlDtoMap : AsyncSqlDtoMaps) {
			Collection<Queue<AsyncSqlDto>> asyncSqlQueues=asyncSqlDtoMap.values();
			for (Queue<AsyncSqlDto> queue : asyncSqlQueues) {
				while(queue.size()>0){
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						logger.error("休眠失败!!",e);
					}
				}
			}
		}
	}
}
