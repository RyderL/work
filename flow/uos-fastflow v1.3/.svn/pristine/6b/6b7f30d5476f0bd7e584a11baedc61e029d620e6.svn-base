package com.ztesoft.uosflow.core.dbpersist.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



/**
 * 数据库异步持久化对象
 * @author  zhong.kaijie  on 2017年3月2日 下午9:46:57
 * @version 1.0.0
 */
public class AsyncSqlDto  implements Serializable,IDueMessage,IPriorityMessage,Cloneable,Comparable<AsyncSqlDto> {
    private static Logger logger = Logger.getLogger(AsyncSqlDto.class);
	
	public  AsyncSqlDto(){
		
	}
	/**
	 * sqlType   1：insert；2:update;3：delete
	 * @author  zhong.kaijie  on 2016年11月24日 下午12:01:16
	 * @version 1.0.0
	 */
	public AsyncSqlDto(int sqlType){
		this.sqlType=sqlType;
	}
	
	private static final long serialVersionUID = 1L;
    private String key;
    private String dataSource;
    private String tableName;
	private String sqlStr;
	private int sqlSeq;
	private List<Object[]> paramsList;
	private Object[] params;
	@Deprecated
	private String exeClass;
	@Deprecated
	private String exeMethod;
	//执行的次数
	private int executeTimes=0;
	//预约执行的时间
	private Date  dueDate;
	//异步持久化对象创建的时间,持久化时，将以本时间为准
	private long executeTime=System.currentTimeMillis();
	/**
	 * 1：insert；2:update;3：delete
	 */
	private int sqlType;

	
	/**
	 * add by bobping 
	 * 当数据库连接异常的时候是否阻塞队列
	 * 
	 */
	private boolean isDbExceptionKeeping = false;
	
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getDataSource() {
        return dataSource;
    }
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
    public String getSqlStr() {
		return sqlStr;
	}
	public void setSqlStr(String sqlStr) {
		if (sqlType==0&&StringUtils.isNotEmpty(sqlStr)) {
			sqlStr=sqlStr.trim().toUpperCase();
			if (sqlStr.startsWith("INSERT")) {
				setSqlType(1);
			}else if (sqlStr.startsWith("UPDATE")) {
				setSqlType(2);
			}else if (sqlStr.startsWith("DELETE")) {
				setSqlType(3);
			}
		}
		this.sqlStr = sqlStr;
	}
    public List<Object[]> getParamsList() {
        return paramsList;
    }
    public void setParamsList(List<Object[]> paramsList) {
        this.paramsList = paramsList;
    }
	public String getExeClass() {
		return exeClass;
	}
	public void setExeClass(String exeClass) {
		this.exeClass = exeClass;
	}
	public String getExeMethod() {
		return exeMethod;
	}
	public void setExeMethod(String exeMethod) {
		this.exeMethod = exeMethod;
	}

	public int getExecuteTimes() {
		return executeTimes;
	}
	public void setExecuteTimes(int executeTimes) {
		this.executeTimes = executeTimes;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	@Override
    public String toString() {
		int paramsLength=0;
		if (CollectionUtils.isNotEmpty(paramsList)) {
			paramsLength=paramsList.size();
		}else if(params!=null){
			paramsLength=1;
		}
    	return "表『"+this.getTableName()+"』key『"+getKey()
    			+ "』,sqlType:"+getSqlType()+",sqlSeq:"+getSqlSeq()+",sql:"+getSqlStr()+",执行条数:"+paramsLength;
    }
	@Override
	public Date dueDate() {
		// TODO Auto-generated method stub
		return dueDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public boolean isDbExceptionKeeping() {
		return isDbExceptionKeeping;
	}

	public void setDbExceptionKeeping(boolean isDbExceptionKeeping) {
		this.isDbExceptionKeeping = isDbExceptionKeeping;
	}

	public int getSqlSeq() {
		return sqlSeq;
	}

	public void setSqlSeq(int sqlSeq) {
		this.sqlSeq = sqlSeq;
	}
	
	public long getExecuteTime() {
		return executeTime;
	}
	
	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}
	
	@Override
	public AsyncSqlDto clone(){
		AsyncSqlDto result=null;
		try {
			result=(AsyncSqlDto) super.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("克隆对象失败!!!",e);
		}
		return result;
	}
	
	
	@Override
	public double getPriority() {
		double result=0;
		String key=getKey();
		result=key.hashCode()*100;
		result+=(-1*sqlSeq*10);
		result+=getSqlSeq();
		return result;
	}
	
	/**
	 * 先对比sqlType，保证sql语句遵循insert》update》delete的原则，则按照相同的sqlseq比较大小
	 */
	@Override
	public int compareTo(AsyncSqlDto o) {
		int result=0;
		if (o==null) {
			result=1;
		}else{
			result=-1*(this.sqlType-o.sqlType);
			if (result==0) {
				//以sqlSeq优先作为排序的依据,sqlSeq越小，优先级越高
				result=o.getSqlSeq()-sqlSeq;
				if (result==0) {
					//如果sqlSeq相等
					result=key.compareTo(o.getKey());
					if (result==0) {
						result=this.hashCode()-o.hashCode();
					}
				}
			}
		}
		return result;
	}
}
