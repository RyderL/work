package com.zterc.uos.fastflow.appcfg;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;

public class UosPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void init(){
		UosAppCfgUtil.setJdbcTemplate(jdbcTemplate);
		UosAppCfgUtil.init();
	}

	protected String resolvePlaceholder(String placeholder, Properties props,
			int systemPropertiesMode) {
		String propVal = super.resolvePlaceholder(placeholder, props,
				systemPropertiesMode);

		if (propVal == null) {
			propVal = resolveDbProperty(placeholder);
		}

		return propVal;
	}

	protected String resolveDbProperty(String placeholder) {
		return UosAppCfgUtil.getValue(placeholder);
	}
}
