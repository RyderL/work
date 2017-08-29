package com.zterc.uos.base.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import com.zterc.uos.base.helper.ClassHelper;
import com.zterc.uos.base.helper.StringHelper;

/**
 * 抽象DAO，存放DAOImpl共享的方法和属性
 * 
 * @author gongyi
 * 
 */
public abstract class AbstractDAOImpl {
	private final Logger logger = LoggerFactory
			.getLogger(AbstractDAOImpl.class);

	protected static final String KEY_SQL = "SQL";
	protected static final String KEY_ARGS = "ARGS";
	protected static final String KEY_TYPE = "TYPE";

	/**
	 * dbutils的QueryRunner对象
	 */
	protected QueryRunner runner = new QueryRunner(true);
	
	/**
	 *  需要构造map传递给实现类
	 * @param sql  需要执行的sql语句
	 * @param args sql语句中的参数列表
	 * @return 构造的map
	 */
	protected Map<String, Object> buildMap(String sql, Object[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_SQL, sql);
		map.put(KEY_ARGS, args);
		int len = args.length;
		int[] type=new int[len];
		for(int i=0;i<len;i++){
			Object arg = args[i];
			if(arg instanceof java.util.Date || arg instanceof java.sql.Date){
				type[i]=Types.DATE;
			}else if(arg instanceof Long){
				type[i]=Types.INTEGER;
			}else if(arg instanceof Integer){
				type[i]=Types.INTEGER;
			}else{//arg instanceof String
				type[i]=Types.VARCHAR;
			}
		}
		map.put(KEY_TYPE, type);
		return map;
	}

	/**
	 * 需要构造map传递给实现类
	 * 
	 * @param sql
	 *            需要执行的sql语句
	 * @param args
	 *            sql语句中的参数列表
	 * @param type
	 *            sql语句中的参数类型
	 * @return 构造的map
	 */
	protected Map<String, Object> buildMap(String sql, Object[] args, int[] type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_SQL, sql);
		map.put(KEY_ARGS, args);
		map.put(KEY_TYPE, type);
		return map;
	}

	protected void saveOrUpdate(Map<String, Object> map) {
		String sql = (String) map.get(KEY_SQL);
		Object[] args = (Object[]) map.get(KEY_ARGS);
		Connection conn = null;
		try {
			conn = getConnection();
			runner.update(conn, sql, args);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			JDBCHelper.close(conn);
		}
	}

	/**
	 * 保存blob
	 * 
	 * @param sql
	 * @param bytes
	 */
	protected void saveBlob(String sql,final Object[] args) {
		LobHandler lobHandler = new DefaultLobHandler();
		JDBCHelper.getJdbcTemplate().execute(sql,new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
			@Override
			protected void setValues(PreparedStatement ps, LobCreator lobCreator)
					throws SQLException, DataAccessException {
				for (int i = 0; i < args.length; i++) {
					if (args[i] instanceof byte[]) {
						lobCreator.setBlobAsBytes(ps, i+1, (byte[]) args[i]);
					}else{
						ps.setObject(i+1, args[i]);
					}
				}
			}
		});
	}

	/**
	 * 查询指定列
	 * 
	 * @param column
	 *            结果集的列索引号
	 * @param sql
	 *            sql语句
	 * @param params
	 *            查询参数
	 * @return 指定列的结果对象
	 */
	protected Object query(int column, String sql, Object... params) {
		Object result;
		Connection conn = null;
		try {
			conn = getConnection();
			if (logger.isDebugEnabled()) {
				logger.debug("查询单列数据=\n" + sql);
			}
			result = runner.query(conn, sql, new ScalarHandler(column), params);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			JDBCHelper.close(conn);
		}
		return result;
	}

	/**
	 * 根据SQL+Args查询的结果集，取第一个转为clazz对象返回
	 * 
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> T queryObject(Class<T> clazz, String sql, Object... args) {
		List<T> result = null;
		Connection conn = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("查询单条记录=\n" + sql);
			}
			conn = getConnection();
			result = runner.query(conn, sql, new BeanPropertyHandler<T>(clazz,
					JDBCHelper.getBeanMapperHandler()), args);
			return JDBCHelper.requiredSingleResult(result);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			JDBCHelper.close(conn);
		}
	}

	/**
	 * 根据SQL+Args查询的结果集，取第一个转为clazz对象返回
	 * 
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> List<T> queryList(Class<T> clazz, String sql, Object... args) {
		Connection conn = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("查询单条记录=\n" + sql);
			}
			conn = getConnection();
			return runner.query(conn, sql, new BeanPropertyHandler<T>(clazz,
					JDBCHelper.getBeanMapperHandler()), args);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			JDBCHelper.close(conn);
		}
	}


	/**
	 * 根据SQL+Args查询的结果集，取第一个转为clazz对象返回
	 * 
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> List<T> queryList(Class<T> clazz,QueryFilter filter, String sql, Object... args) {
		Connection conn = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("查询单条记录=\n" + sql);
			}
			String orderby = StringHelper.buildPageOrder(filter.getOrder(),
					filter.getOrderBy());
			String querySQL = sql + orderby;
			conn = getConnection();
			return runner.query(conn, querySQL, new BeanPropertyHandler<T>(clazz,
					JDBCHelper.getBeanMapperHandler()), args);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			JDBCHelper.close(conn);
		}
	}
	/**
	 * 根据类型clazz、Sql语句、参数分页查询列表对象
	 * 
	 * @param page
	 *            分页对象
	 * @param filter
	 *            查询过滤器
	 * @param clazz
	 *            类型
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数列表
	 * @return 结果对象列表
	 */
	public <T> List<T> queryList(Page<T> page, QueryFilter filter,
			Class<T> clazz, String sql, Object... args) {
		String orderby = StringHelper.buildPageOrder(filter.getOrder(),
				filter.getOrderBy());
		String querySQL = sql + orderby;
		if (page == null) {
			return queryList(clazz, querySQL, args);
		}
		String countSQL = "select count(1) from (" + sql + ") c ";
		querySQL = JDBCHelper.getDialect().getPageSql(querySQL, page);
		if (logger.isDebugEnabled()) {
			logger.debug("查询分页countSQL=\n" + countSQL);
			logger.debug("查询分页querySQL=\n" + querySQL);
		}
		try {
			Object count = queryCount(countSQL, args);
			List<T> list = queryList(clazz, querySQL, args);
			if (list == null)
				list = Collections.emptyList();
			page.setResult(list);
			page.setTotalCount(ClassHelper.castLong(count));
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	public Long queryCount(String sql, Object... args) {
		return ClassHelper.castLong(query(1, sql, args));
	}

	public String queryForString(String sql, Object... args) {
		Connection conn = null;
		try {
			conn = getConnection();
			return StringHelper.valueOf(runner.query(conn, sql, new ScalarHandler(), args));
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			JDBCHelper.close(conn);
		}
	}

	/**
	 * 获取连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		return JDBCHelper.getConnection();
	}

}
