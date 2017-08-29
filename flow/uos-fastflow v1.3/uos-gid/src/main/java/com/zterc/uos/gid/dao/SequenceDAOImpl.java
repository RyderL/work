package com.zterc.uos.gid.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.zterc.uos.gid.service.GidConfig;

public class SequenceDAOImpl implements SequenceDAO {
	private static Logger logger = Logger.getLogger(SequenceDAOImpl.class);

	private GidConfig gidConfig;

	public SequenceDAOImpl(GidConfig gidConfig) {
		this.gidConfig = gidConfig;
	}

	@Override
	public Long getSequence(String seqName, long increment, long minValue) {
		Long result = getGID(this.gidConfig.getGidServerUrl(),
				this.gidConfig.getSysCode(), seqName, increment, minValue);
		logger.debug(seqName + "--gid:" + result);
		return result;
	}

	private Long getGID(String gidServerUrl, String sysCode, String seqName,
			Long increment, long minValue) {
		String getURL = gidServerUrl + "?sysCode=" + sysCode + "&seqName="
				+ seqName + "&increment=" + increment + "&minValue=" + minValue;
		logger.debug("--getURL:" + getURL);
		String result = null;
		URL getUrl = null;
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			getUrl = new URL(getURL);
			connection = (HttpURLConnection) getUrl.openConnection();
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String lines;

			while ((lines = reader.readLine()) != null) {
				result = lines;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		if (StringUtils.isEmpty(result)) {
			return -1L;
		} else {
			return Long.valueOf(result);
		}
	}

	public GidConfig getGidConfig() {
		return gidConfig;
	}

	public void setGidConfig(GidConfig gidConfig) {
		this.gidConfig = gidConfig;
	}

}
