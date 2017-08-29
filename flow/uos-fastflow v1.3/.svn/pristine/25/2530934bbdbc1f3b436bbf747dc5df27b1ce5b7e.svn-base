package com.zterc.uos.base.sequence.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.sequence.SequenceService;

public class SequenceServiceDB implements SequenceService {

	@Override
	public Long getGidByName(String name) {
		String key = name.intern();
		synchronized (key) {
			PlatformTransactionManager txManager = null;
			DefaultTransactionDefinition def = null;
			TransactionStatus status = null;

			try {
				txManager = JDBCHelper.getTransactionManager();
				def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
				status = txManager.getTransaction(def);
				JdbcTemplate jdbcTemplate = JDBCHelper.getJdbcTemplate();
				List<Map<String, Object>> list = jdbcTemplate
						.queryForList(
								"SELECT A.SEQ_VALUE FROM UOS_UOSSEQUENCE A WHERE A.SEQ_NAME = ?  AND A.ROUTE_ID =1",
								new Object[] { name });
				Long id = -1L;
				if (list == null || list.size() == 0) {
					id = 1L;
					jdbcTemplate
							.update("INSERT INTO UOS_UOSSEQUENCE(SEQ_NAME,SEQ_VALUE) VALUES(?,?)",
									new Object[] { name, id });
				} else {
					id = LongHelper.valueOf(list.get(0).get("SEQ_VALUE"));
					id = id + 1;
					jdbcTemplate
							.update("UPDATE UOS_UOSSEQUENCE SET SEQ_VALUE=? WHERE SEQ_NAME = ?",
									new Object[] { id, name });
				}
				txManager.commit(status);
				return id;
			} catch (Exception e) {
				if (txManager != null) {
					txManager.rollback(status);
				}
				throw e;
			}
		}
	}
}
