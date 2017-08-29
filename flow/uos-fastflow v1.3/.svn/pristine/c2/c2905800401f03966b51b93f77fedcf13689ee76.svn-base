package com.ztesoft.uosflow.core.dbpersist.event;

import java.util.Queue;
import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zterc.uos.base.jdbc.JDBCHelper;
import com.ztesoft.uosflow.core.common.ApplicationContextProxy;
import com.ztesoft.uosflow.core.dbpersist.dao.DbPersistDAO;
import com.ztesoft.uosflow.core.dbpersist.dto.AsyncSqlDto;

public class SqlExcuteQueueEvent extends Thread {
	private static Logger logger = Logger.getLogger(SqlExcuteQueueEvent.class);
	private long freeSleepTime = 100L;
	Queue<AsyncSqlDto> sqlQueue;
	private DbPersistDAO dbPersistDAO;

	
	public SqlExcuteQueueEvent(Queue<AsyncSqlDto> sqlQueue) {
		this.sqlQueue = sqlQueue;
		dbPersistDAO = (DbPersistDAO) ApplicationContextProxy.getBean("dbPersistDAO");
	}
	
	public SqlExcuteQueueEvent(Queue<AsyncSqlDto> sqlQueue,
			long freeSleepTime) {
		this.sqlQueue = sqlQueue;
		dbPersistDAO = (DbPersistDAO) ApplicationContextProxy.getBean("dbPersistDAO");
		this.freeSleepTime = freeSleepTime;
	}
	
	public SqlExcuteQueueEvent(Queue<AsyncSqlDto> sqlQueue,
			int maxBatchDealCount, long freeSleepTime) {
		this.sqlQueue = sqlQueue;
		dbPersistDAO = (DbPersistDAO) ApplicationContextProxy.getBean("dbPersistDAO");
		this.freeSleepTime = freeSleepTime;
	}


	@Override
	public void run() {
		int queueSize = -1;
		while (true) {
			queueSize = sqlQueue.size();
			if (queueSize > 0) {
				AsyncSqlDto asyncSqlDto=sqlQueue.poll();
				executeBathcAsyncSqlDto(asyncSqlDto);
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
	 * 处理合并好批量的AsyncSqlDto； 若期间出现异常则需要分开提交，以保证sql能正常执行
	 *  
	 * @param asyncSqlDto
	 * @author zhong.kaijie on 2017年3月2日 下午9:46:15
	 * @version 1.0.0
	 */
	public void executeBathcAsyncSqlDto(AsyncSqlDto asyncSqlDto) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(); // 定义一个某个框架平台的TransactionManager，如JDBC、Hibernate
		dataSourceTransactionManager
				.setDataSource(JDBCHelper.getJdbcTemplate().getDataSource()); // 设置数据源
		DefaultTransactionDefinition transDef = new DefaultTransactionDefinition(); // 定义事务属性
		transDef.setPropagationBehavior(
				DefaultTransactionDefinition.PROPAGATION_REQUIRED); // 设置传播行为属性
		TransactionStatus status = dataSourceTransactionManager
				.getTransaction(transDef); // 获得事务状态
		try {
			dbPersistDAO.executeSqlWithExceptionDeal(asyncSqlDto);
			dataSourceTransactionManager.commit(status);
		} catch (Exception e) {
			// 批量执行失败处理操作
			dataSourceTransactionManager.rollback(status);
			logger.error("批量执行sql失败!!", e);
		}
	}
}
