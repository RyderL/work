package com.zterc.uos.base.sequence.impl;

import java.sql.CallableStatement;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.sequence.SequenceService;

public class SequenceServiceOracle  implements SequenceService {
	
	private static final Logger logger = LoggerFactory.getLogger(SequenceServiceOracle.class);

	@Override
	public Long getGidByName(String name) {
		 String sql = "{call getSeqValue(?,?,?)}";
        Connection conn = null;
        CallableStatement cst = null;
        try {
            conn = JDBCHelper.getConnection();
            cst = conn.prepareCall(sql);
            cst.setString(1, name);
            cst.setLong(2, 1);
            cst.registerOutParameter(3,java.sql.Types.INTEGER);
            cst.execute();
            long sequenceValue = cst.getLong(3);
            return sequenceValue;
        }
        catch (Exception ex) {
        	logger.error("获取"+name+"的sequence值失败,异常:"+ex.getMessage()+",原因:"+ex.getCause());
            throw new RuntimeException(ex.getMessage());
        }
        finally {
        	JDBCHelper.close(cst);
        	JDBCHelper.close(conn);
        }
	}
}
