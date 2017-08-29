package com.zterc.uos.base.sequence.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.jdbc.JDBCHelper;
import com.zterc.uos.base.sequence.SequenceService;

public class SequenceServiceOracleNew  implements SequenceService {
	
	private static final Logger logger = LoggerFactory.getLogger(SequenceServiceOracleNew.class);

	@Override
	public Long getGidByName(String name) {
//		 String sql = "{call getSeqValue(?,?,?)}";
		String sql = "select "+name+"_SEQ.nextval from dual";
        Connection conn = null;
		CallableStatement cst = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        long sequenceValue = 0L;
        try {
            conn = JDBCHelper.getConnection();
            ps = conn.prepareCall(sql);
            rs = ps.executeQuery();
            if(rs.next()){
            	sequenceValue = rs.getLong(1);
            }
            return sequenceValue;
        }
        catch (Exception ex) {
        	logger.error("获取"+name+"的sequence值失败,异常:"+ex.getMessage()+",原因:"+ex.getCause());
        	if(ex.getMessage().contains("序列不存在") || ex.getMessage().contains("sequence does not exist")){
        		sql = "{call getSeqValue(?,?,?)}";
                try {
            		conn = JDBCHelper.getConnection();
                    cst = conn.prepareCall(sql);
                    cst.setString(1, name);
                    cst.setLong(2, 1);
					cst.registerOutParameter(3,java.sql.Types.INTEGER);
	                cst.execute();
	                sequenceValue = cst.getLong(3);
	                return sequenceValue;
				} catch (SQLException e) {
		        	logger.error("获取"+name+"的sequence值失败,异常:"+e.getMessage()+",原因:"+e.getCause());
		            throw new RuntimeException(e.getMessage());
				}
        	}
            throw new RuntimeException(ex.getMessage());
        }
        finally {
        	JDBCHelper.close(rs);
        	JDBCHelper.close(ps);
        	JDBCHelper.close(cst);
        	JDBCHelper.close(conn);
        }
	}
}
