package com.ztesoft.uosflow.core.dbpersist.dao.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zterc.uos.base.helper.DateHelper;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.SequenceHelper;
import com.zterc.uos.base.jdbc.AbstractDAOImpl;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.fastflow.dto.process.DbpersistExceptionDto;
import com.zterc.uos.fastflow.exception.FastflowException;
import com.zterc.uos.fastflow.service.DbPersistExceptionService;
import com.ztesoft.uosflow.core.dbpersist.dao.DbPersistDAO;
import com.ztesoft.uosflow.core.dbpersist.dto.AsyncSqlDto;
import com.ztesoft.uosflow.core.dbpersist.util.DbPersist4BatchUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: Yolanda Date: 15-5-29 Time: 下午4:28 To
 * change this template use File | Settings | File Templates.
 */
@Repository("dbPersistDAO")
@Transactional
public class DbPersistDAOImpl extends AbstractDAOImpl implements DbPersistDAO {
	private static final Logger logger = Logger
			.getLogger(DbPersistDAOImpl.class);

	@Autowired
	private DbPersistExceptionService dbPersistExceptionService;

	@Override
	public boolean executeSql(AsyncSqlDto asyncSqlDto) {
		if (asyncSqlDto == null) {
			return false;
		}
		boolean isSuccess = false;
		long ret = -1;// -1表示不判断

		if (StringUtils.isEmpty(asyncSqlDto.getTableName())) {
			logger.info("表名为空!!sql:" + asyncSqlDto.getSqlStr());
		}
		try {
			List<Object[]> paramsList = asyncSqlDto.getParamsList();
			if (logger.isInfoEnabled()) {
				logger.info("执行sql对象:"+GsonHelper.toJson(asyncSqlDto));
			}
			if (CollectionUtils.isNotEmpty(paramsList)) {
				 executeSql4Batch(asyncSqlDto,JDBCHelper.getJdbcTemplate());
			} else {
				Object[] params = asyncSqlDto.getParams();
				if (params != null) {
					 ret = JDBCHelper.getJdbcTemplate()
					 .update(asyncSqlDto.getSqlStr(), params);
				}
			}
			if (ret == 0) {
				// 如果返回值是0则表示执行失败,记录日志
				saveDbPersistError(asyncSqlDto, new FastflowException("执行结果为:0"));
				isSuccess = false;
			} else {
				isSuccess = true;
			}
		} catch (Exception e) {
			// 其他异常
			throw new FastflowException("数据库持久化出现异常，则转入异常队列!",e);
		}

		return isSuccess;
	}

	/**
	 * 批量提交持久化，此处需要加上事务，若批量提交中出现一个异常，则全部回滚，并改成依次提交，依次提交不加事务。
	 * 若返回的结果集中有小于0的，表示有执行但没有影响影响记录的sql，即可能update比insert先执行，此时也需要记录成异常的sql
	 * 
	 * @param asyncSqlDto
	 * @return
	 * @author zhong.kaijie on 2017年3月8日 下午5:03:24
	 * @version 1.0.0
	 */
	boolean executeSql4Batch(AsyncSqlDto asyncSqlDto,
			JdbcTemplate targetJdbcTemplate) {
		boolean result = false;
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(); // 定义一个某个框架平台的TransactionManager，如JDBC、Hibernate
		dataSourceTransactionManager
				.setDataSource(targetJdbcTemplate.getDataSource()); // 设置数据源
		DefaultTransactionDefinition transDef = new DefaultTransactionDefinition(); // 定义事务属性
		transDef.setPropagationBehavior(
				DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW); // 设置传播行为属性
		TransactionStatus status = dataSourceTransactionManager
				.getTransaction(transDef); // 获得事务状态
		try {
			int[] rets = targetJdbcTemplate.batchUpdate(asyncSqlDto.getSqlStr(),
					asyncSqlDto.getParamsList());
			for (int i = 0; i < rets.length; i++) {
				if (rets[i] > 0) {
					continue;
				}
				AsyncSqlDto failedSql = DbPersist4BatchUtils
						.getFromAsyncSqlDtos(asyncSqlDto, i);
				String message = "执行的结果为0!";
				logger.info("---failedSql:"+failedSql);
				saveDbPersistError(failedSql, new FastflowException(message));
			}
			result = true;
			// 数据库操作
			dataSourceTransactionManager.commit(status);// 提交
		} catch (Exception e) {
			dataSourceTransactionManager.rollback(status);// 回滚
			throw e;
		} finally {

		}
		return result;
	}

	@Override
	public boolean executeSqlWithExceptionDeal(AsyncSqlDto asyncSqlDto) {
		boolean result = false;
		try {
			executeSql(asyncSqlDto);
			result = true;
		} catch (CannotGetJdbcConnectionException e) {
			// 连接异常，则需要保存到文件
			logger.error("异步持久化失败!!" + GsonHelper.toJson(asyncSqlDto), e);
		} catch (Exception e) {
			boolean isBatchSql = DbPersist4BatchUtils
					.isBatchAsyncSql(asyncSqlDto);
			if (isBatchSql) {
				List<AsyncSqlDto> asyncSqlDtos = DbPersist4BatchUtils
						.splitAsyncSqlDtos(asyncSqlDto);
				for (AsyncSqlDto subAsyncSqlDto : asyncSqlDtos) {
					executeSqlWithExceptionDeal(subAsyncSqlDto);
				}
			} else {
				saveDbPersistError(asyncSqlDto, e);
			}
			// 除了连接异常，其他的异常保存到数据库中
			logger.error("异步持久化失败!!" + GsonHelper.toJson(asyncSqlDto), e);
		}
		return result;
	}

	/**
	 * 保存异步持久化信息
	 * 
	 * @param asyncSqlDto
	 * @return
	 * @author zhong.kaijie on 2017年3月8日 下午1:19:12
	 * @version 1.0.0
	 */
	public boolean saveDbPersistError(AsyncSqlDto asyncSqlDto, Throwable e) {
		logger.info("--------进入saveDbPersistError方法-------");
		boolean result = false;
		try {
			String sqlTableName = asyncSqlDto.getTableName();
			if (StringUtils.isNotEmpty(sqlTableName)) {
				result = saveDbPersistError2DB(asyncSqlDto, e);
			}
		} catch (Exception ex) {
			result = saveDbPersistError2File(asyncSqlDto, e);
		}
		return result;
	}

	public boolean saveDbPersistError2DB(AsyncSqlDto asyncSqlDto, Throwable e) {
		logger.info("--------进入saveDbPersistError2DB方法-------");
		DbpersistExceptionDto dbpersistException = new DbpersistExceptionDto();
		dbpersistException.setCreateDate(DateHelper.getTimeStamp());
		dbpersistException.setStateDate(DateHelper.getTimeStamp());
		dbpersistException.setState(0);
		dbpersistException.setParam(GsonHelper.toJson(asyncSqlDto.getParams()));
		dbpersistException.setSqlKey(asyncSqlDto.getKey());
		dbpersistException.setSqlStr(asyncSqlDto.getSqlStr());
		dbpersistException.setId(SequenceHelper.getIdWithSeed(
				"UOS_DB_PERSIST_EXCEPTION",asyncSqlDto.getKey()));
		String exceptionDesc = "持久化对象:" + GsonHelper.toJson(asyncSqlDto) + " /n "
				+ e.getMessage();
		dbpersistException.setExceptionDesc(exceptionDesc);
		boolean result = true;
		try {
			dbPersistExceptionService.save(dbpersistException);
		} catch (Exception ex) {
			result = false;
			logger.error("----sql持久化异常写入数据库异常，异常原因："+ex.getMessage(),ex);
			throw ex;
		}
		return result;
	}

	/**
	 * 将异步持久化对象保存到文件中 TODO 将异步持久化对象保存到文件中
	 * 
	 * @param asyncSqlDto
	 * @return
	 * @author zhong.kaijie on 2017年3月8日 下午1:14:48
	 * @version 1.0.0
	 */
	public boolean saveDbPersistError2File(AsyncSqlDto asyncSqlDto,
			Throwable e) {
		logger.info("保存到文件!");
		return false;
	}
}
