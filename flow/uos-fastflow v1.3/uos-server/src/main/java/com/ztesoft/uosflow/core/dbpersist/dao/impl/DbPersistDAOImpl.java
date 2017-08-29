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
 * Created with IntelliJ IDEA. User: Yolanda Date: 15-5-29 Time: ����4:28 To
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
		long ret = -1;// -1��ʾ���ж�

		if (StringUtils.isEmpty(asyncSqlDto.getTableName())) {
			logger.info("����Ϊ��!!sql:" + asyncSqlDto.getSqlStr());
		}
		try {
			List<Object[]> paramsList = asyncSqlDto.getParamsList();
			if (logger.isInfoEnabled()) {
				logger.info("ִ��sql����:"+GsonHelper.toJson(asyncSqlDto));
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
				// �������ֵ��0���ʾִ��ʧ��,��¼��־
				saveDbPersistError(asyncSqlDto, new FastflowException("ִ�н��Ϊ:0"));
				isSuccess = false;
			} else {
				isSuccess = true;
			}
		} catch (Exception e) {
			// �����쳣
			throw new FastflowException("���ݿ�־û������쳣����ת���쳣����!",e);
		}

		return isSuccess;
	}

	/**
	 * �����ύ�־û����˴���Ҫ���������������ύ�г���һ���쳣����ȫ���ع������ĳ������ύ�������ύ��������
	 * �����صĽ��������С��0�ģ���ʾ��ִ�е�û��Ӱ��Ӱ���¼��sql��������update��insert��ִ�У���ʱҲ��Ҫ��¼���쳣��sql
	 * 
	 * @param asyncSqlDto
	 * @return
	 * @author zhong.kaijie on 2017��3��8�� ����5:03:24
	 * @version 1.0.0
	 */
	boolean executeSql4Batch(AsyncSqlDto asyncSqlDto,
			JdbcTemplate targetJdbcTemplate) {
		boolean result = false;
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(); // ����һ��ĳ�����ƽ̨��TransactionManager����JDBC��Hibernate
		dataSourceTransactionManager
				.setDataSource(targetJdbcTemplate.getDataSource()); // ��������Դ
		DefaultTransactionDefinition transDef = new DefaultTransactionDefinition(); // ������������
		transDef.setPropagationBehavior(
				DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW); // ���ô�����Ϊ����
		TransactionStatus status = dataSourceTransactionManager
				.getTransaction(transDef); // �������״̬
		try {
			int[] rets = targetJdbcTemplate.batchUpdate(asyncSqlDto.getSqlStr(),
					asyncSqlDto.getParamsList());
			for (int i = 0; i < rets.length; i++) {
				if (rets[i] > 0) {
					continue;
				}
				AsyncSqlDto failedSql = DbPersist4BatchUtils
						.getFromAsyncSqlDtos(asyncSqlDto, i);
				String message = "ִ�еĽ��Ϊ0!";
				logger.info("---failedSql:"+failedSql);
				saveDbPersistError(failedSql, new FastflowException(message));
			}
			result = true;
			// ���ݿ����
			dataSourceTransactionManager.commit(status);// �ύ
		} catch (Exception e) {
			dataSourceTransactionManager.rollback(status);// �ع�
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
			// �����쳣������Ҫ���浽�ļ�
			logger.error("�첽�־û�ʧ��!!" + GsonHelper.toJson(asyncSqlDto), e);
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
			// ���������쳣���������쳣���浽���ݿ���
			logger.error("�첽�־û�ʧ��!!" + GsonHelper.toJson(asyncSqlDto), e);
		}
		return result;
	}

	/**
	 * �����첽�־û���Ϣ
	 * 
	 * @param asyncSqlDto
	 * @return
	 * @author zhong.kaijie on 2017��3��8�� ����1:19:12
	 * @version 1.0.0
	 */
	public boolean saveDbPersistError(AsyncSqlDto asyncSqlDto, Throwable e) {
		logger.info("--------����saveDbPersistError����-------");
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
		logger.info("--------����saveDbPersistError2DB����-------");
		DbpersistExceptionDto dbpersistException = new DbpersistExceptionDto();
		dbpersistException.setCreateDate(DateHelper.getTimeStamp());
		dbpersistException.setStateDate(DateHelper.getTimeStamp());
		dbpersistException.setState(0);
		dbpersistException.setParam(GsonHelper.toJson(asyncSqlDto.getParams()));
		dbpersistException.setSqlKey(asyncSqlDto.getKey());
		dbpersistException.setSqlStr(asyncSqlDto.getSqlStr());
		dbpersistException.setId(SequenceHelper.getIdWithSeed(
				"UOS_DB_PERSIST_EXCEPTION",asyncSqlDto.getKey()));
		String exceptionDesc = "�־û�����:" + GsonHelper.toJson(asyncSqlDto) + " /n "
				+ e.getMessage();
		dbpersistException.setExceptionDesc(exceptionDesc);
		boolean result = true;
		try {
			dbPersistExceptionService.save(dbpersistException);
		} catch (Exception ex) {
			result = false;
			logger.error("----sql�־û��쳣д�����ݿ��쳣���쳣ԭ��"+ex.getMessage(),ex);
			throw ex;
		}
		return result;
	}

	/**
	 * ���첽�־û����󱣴浽�ļ��� TODO ���첽�־û����󱣴浽�ļ���
	 * 
	 * @param asyncSqlDto
	 * @return
	 * @author zhong.kaijie on 2017��3��8�� ����1:14:48
	 * @version 1.0.0
	 */
	public boolean saveDbPersistError2File(AsyncSqlDto asyncSqlDto,
			Throwable e) {
		logger.info("���浽�ļ�!");
		return false;
	}
}
