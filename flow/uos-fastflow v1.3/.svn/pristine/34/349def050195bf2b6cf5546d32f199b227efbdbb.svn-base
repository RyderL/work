package com.zterc.uos.base.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final Logger logger = LoggerFactory
			.getLogger(DynamicDataSource.class);

	private Properties dbProperties;
	private List<String> dbConfigs;
	private DataSource archievedDataSource;
	private DataSource historyDataSource;

	public void setDbProperties(Properties dbProperties) {
		this.dbProperties = dbProperties;
	}

	public void setDbConfigs(List<String> dbConfigs) throws Exception {
		this.dbConfigs = dbConfigs;
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

		if (this.dbConfigs != null && dbConfigs.size() > 0) {
			DsContextHolder.setMod(dbConfigs.size());
			logger.info("========初始化实例分库数据源====开始=====");
			for (int i = 0; i < dbConfigs.size(); i++) {
				String[] config = dbConfigs.get(i).split(";");
				logger.info("==初始化数据源连接池==" + config);
				dbProperties.setProperty("driverClassName", config[0]);
				dbProperties.setProperty("url", config[1]);
				dbProperties.setProperty("username", config[2]);
				dbProperties.setProperty("password", config[3]);
				DataSource basicDataSource = BasicDataSourceFactory.createDataSource(dbProperties);
				targetDataSources.put(DsContextHolder.DS_INSTANCE + i,
						basicDataSource);
			}
			logger.info("========初始化实例分库数据源====结束=====");
		}
		targetDataSources.put(DsContextHolder.DS_ARCHIEVED, DsContextHolder.DS_ARCHIEVED);
		targetDataSources.put(DsContextHolder.DS_HISTORY, DsContextHolder.DS_HISTORY);
		setTargetDataSources(targetDataSources);
	}

	public void setArchievedDataSource(DataSource archievedDataSource) {
		this.archievedDataSource = archievedDataSource;
	}

	public void setHistoryDataSource(DataSource historyDataSource) {
		this.historyDataSource = historyDataSource;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return DsContextHolder.getHoldDs();
	}
	
	@Override
	protected DataSource resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {
		if(dataSource!=null&&dataSource instanceof String){
			if(dataSource.equals(DsContextHolder.DS_ARCHIEVED)){
				return archievedDataSource;
			}else if(dataSource.equals(DsContextHolder.DS_HISTORY)){
				return historyDataSource;
			}
		}
		return super.resolveSpecifiedDataSource(dataSource);
	}
}