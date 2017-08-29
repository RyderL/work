
package com.ztesoft.uosflow.core.dbpersist.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.zterc.uos.base.helper.GsonHelper;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.dbpersist.dto.AsyncSqlDto;
import com.ztesoft.uosflow.core.dbpersist.util.DbPersist4BatchUtils;
import com.ztesoft.uosflow.core.dbpersist.util.SqlExcuteQueueUtils;
import com.ztesoft.uosflow.core.dbpersist.util.SqlPersistQueueUtils;

public class SqlPersistQueueEvent extends Thread {
	private static Logger logger = Logger.getLogger(SqlPersistQueueEvent.class);
	private int maxBatchDealCount = 1000;
	private long freeSleepTime = 100L;
	Queue<AsyncSqlDto> sqlQueue;
	private SqlExcuteQueueUtils sqlExcuteQueueUtils;
	
	public SqlPersistQueueEvent(Queue<AsyncSqlDto> sqlQueue) {
		this.sqlQueue = sqlQueue;
	}
	public SqlPersistQueueEvent(Queue<AsyncSqlDto> sqlQueue,
			int maxBatchDealCount, long freeSleepTime) {
		this.sqlQueue = sqlQueue;
		sqlExcuteQueueUtils=(SqlExcuteQueueUtils) ApplicationContextProxy.getBean("sqlExcuteQueueUtils");
		this.maxBatchDealCount = maxBatchDealCount;
		this.freeSleepTime = freeSleepTime;
	}

	@Override
	public void run() {
		int queueSize = -1;
		long startTime=System.currentTimeMillis();
		while (true) {
			long now=System.currentTimeMillis();
			queueSize = sqlQueue.size();
			if (queueSize > 0) {
				int getCount = maxBatchDealCount < queueSize
						? maxBatchDealCount
						: queueSize;
				if (shouldExe(now-startTime, getCount)) {
					List<AsyncSqlDto> asyncSqlDtos = getAsyncSqlDtos(getCount);
					dealAsyncSqlDtos(asyncSqlDtos);
					startTime=System.currentTimeMillis();
				}else{
					try {
						sleep(freeSleepTime);
					} catch (InterruptedException e) {
						logger.error("休眠异常!!!", e);
					}
				}
			} else {
				try {
					sleep(freeSleepTime);
				} catch (InterruptedException e) {
					logger.error("休眠异常!!!", e);
				}
			}
			
		}
	}
	
	/**
	 * 是否应该执行
	 * @param countTime
	 * @param queueSize
	 * @return
	 * @author  zhong.kaijie  on 2017年3月10日 下午3:36:13
	 * @version 1.0.0
	 */
	public boolean shouldExe(long countTime, int queueSize) {
		boolean result = true;
//		// 如果持久化属于繁忙状态，则要么是达到一定的数量后提交，要么是达到一定时间后提交
		if (SqlPersistQueueUtils.isBusy()) {
			//
			if (countTime >= SqlPersistQueueUtils.busyCommitTime
					|| queueSize >= maxBatchDealCount) {
				result=true;
			}else{
				result=false;
			}
		}
		return result;
	}

	/**
	 * 从队列sqlQueue中获取指定个数的AsyncSqlDtos集合
	 * @param getCount
	 * @return
	 * @author  zhong.kaijie  on 2017年3月9日 下午7:02:46
	 * @version 1.0.0
	 */
	List<AsyncSqlDto> getAsyncSqlDtos(int getCount) {
		List<AsyncSqlDto> asyncSqlDtos = new ArrayList<>();
		for (int i = 0; i < getCount; i++) {
			AsyncSqlDto tempAsyncSqlDto = sqlQueue.poll();
			logger.info("--SqlPersistQueueEvent取出的对象--"+GsonHelper.toJson(tempAsyncSqlDto));
			if (tempAsyncSqlDto != null) {
				asyncSqlDtos.add(tempAsyncSqlDto);
			} else {
				break;
			}
		}
		return asyncSqlDtos;
	}

	/**
	 * 批处理异步持久化
	 * 
	 * @param asyncSqlDtos
	 * @author zhong.kaijie on 2017年3月2日 下午9:33:37
	 * @version 1.0.0
	 */
	void dealAsyncSqlDtos(List<AsyncSqlDto> asyncSqlDtos) {
		long startTime = System.currentTimeMillis();
		Set<AsyncSqlDto> mergeAsyncSqlDto = mergeAsyncSqlDtos(asyncSqlDtos);
		for (AsyncSqlDto asyncSqlDto : mergeAsyncSqlDto) {
			executeBathcAsyncSqlDto(asyncSqlDto);
		}
		if (logger.isInfoEnabled()) {
			long endTime = System.currentTimeMillis();
			logger.info("批量处理条数:" + asyncSqlDtos.size() + " 总耗时为:"
					+ (endTime - startTime)+"ms 当前队列数量为:"+sqlQueue.size());
		}
	}

	/**
	 * 处理合并好批量的AsyncSqlDto； 若期间出现异常则需要分开提交，以保证sql能正常执行
	 * 
	 * @param asyncSqlDto
	 * @author zhong.kaijie on 2017年3月2日 下午9:46:15
	 * @version 1.0.0
	 */
	public void executeBathcAsyncSqlDto(AsyncSqlDto asyncSqlDto) {
		try {
			sqlExcuteQueueUtils.putToQueue(asyncSqlDto);
		} catch (Exception e) {
			// 批量执行失败处理操作
			logger.error("批量执行sql失败!!", e);
		}
	}

	/**
	 * 合并
	 * 
	 * @param asyncSqlDtos
	 * @return
	 * @author zhong.kaijie on 2017年3月2日 下午9:40:04
	 * @version 1.0.0
	 */
	Set<AsyncSqlDto> mergeAsyncSqlDtos(List<AsyncSqlDto> asyncSqlDtos) {
		Map<String, AsyncSqlDto> baseAsyncSqlMap = new HashMap<>();
		if (CollectionUtils.isEmpty(asyncSqlDtos)) {
			return null;
		}
		Set<AsyncSqlDto> result = new TreeSet<>(new Comparator<AsyncSqlDto>() {

			@Override
			public int compare(AsyncSqlDto o1, AsyncSqlDto o2) {
				if (o1 == null) {
					if (o2 == null) {
						return 0;
					} else {
						return 1;
					}
				} else {
					return -1 * (o1.compareTo(o2));
				}
			}
		});
		// 将list集合中的第一个元素作为合并的基础AsyncSqlDto
		for (int i = 0; i < asyncSqlDtos.size(); i++) {
			AsyncSqlDto newAsyncSqlDto = asyncSqlDtos.get(i);
			AsyncSqlDto baseAsyncSqlDto = selectBaseAsyncSqlDto(baseAsyncSqlMap,
					newAsyncSqlDto);
			if (baseAsyncSqlDto != newAsyncSqlDto) {
				DbPersist4BatchUtils.mergeAsyncSqlDtos(baseAsyncSqlDto,
						newAsyncSqlDto);
			}
			result.add(baseAsyncSqlDto);
		}
		return result;
	}
	/**
	 * 从baseAsyncSqlMap中根据sql语句获取作为合并基础的baseAsyncSqlDto
	 * 
	 * @param baseAsyncSqlMap
	 * @param asyncSqlDto
	 * @return
	 * @author zhong.kaijie on 2017年3月2日 下午9:51:23
	 * @version 1.0.0
	 */
	AsyncSqlDto selectBaseAsyncSqlDto(Map<String, AsyncSqlDto> baseAsyncSqlMap,
			AsyncSqlDto asyncSqlDto) {
		AsyncSqlDto baAsyncSqlDto = null;
		String sql = asyncSqlDto.getSqlStr();
		if (MapUtils.isEmpty(baseAsyncSqlMap)) {
			synchronized (baseAsyncSqlMap) {
				if (MapUtils.isEmpty(baseAsyncSqlMap)) {
					baAsyncSqlDto = asyncSqlDto;
					baseAsyncSqlMap.put(sql, baAsyncSqlDto);
				}
			}
		}
		baAsyncSqlDto = baseAsyncSqlMap.get(sql);
		if (baAsyncSqlDto == null) {
			synchronized (baseAsyncSqlMap) {
				baAsyncSqlDto = baseAsyncSqlMap.get(sql);
				if (baAsyncSqlDto == null) {
					baAsyncSqlDto = asyncSqlDto;
					baseAsyncSqlMap.put(sql, baAsyncSqlDto);
				}
			}
		}
		return baAsyncSqlDto;
	}
}
