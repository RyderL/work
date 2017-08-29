package com.ztesoft.uosflow.core.dbpersist.dao;

import com.ztesoft.uosflow.core.dbpersist.dto.AsyncSqlDto;

/**
 * Created with IntelliJ IDEA.
 * User: Yolanda
 * Date: 15-5-29
 * Time: 下午4:28
 * To change this template use File | Settings | File Templates.
 */
public interface DbPersistDAO {

    /**
     * 指定数据源执行sql
     * @param asyncSqlDto
     * @return 
     */
    public boolean executeSql(AsyncSqlDto asyncSqlDto);

    /**
     * 
     * 附带异常处理的异步持久化对象 持久操作。
     * TODO  异常处理逻辑：若是数据库连接异常，则将AsyncSqlDto对象持久化到文件中；
     *        若是sql执行异常，则将sql持久化到表IOM_OM_DBPERSIST_EXCEPTION中，等待人工处理
     * @param asyncSqlDto
     * @return
     * @author  zhong.kaijie  on 2017年2月15日 下午3:44:08
     * @version 1.0.0
     */
	boolean executeSqlWithExceptionDeal(AsyncSqlDto asyncSqlDto);
}
