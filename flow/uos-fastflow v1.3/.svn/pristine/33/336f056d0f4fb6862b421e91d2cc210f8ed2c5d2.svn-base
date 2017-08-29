package com.ztesoft.uosflow.inf.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
 

public class HttpClientUtils {
	private static Logger logger = Logger.getLogger(HttpClientUtils.class);
	private final static String ENCODE="UTF-8";
	
	
	public static String sendHttpPost(String url, String json)  { 
		return sendHttpPost(url,json,ENCODE);
	}
	
	
	public static String sendHttpPost(String url, String json, String encode)  {
		encode=encode==null?ENCODE:encode;
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		// 创建httppost
		HttpPost httpPost = null;
		String result = null;
		int count = 5;
		while((count--)>0){ 
			httpPost = new HttpPost(url);
			httpPost.setHeader("token", "test_token");
			try { 
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                NameValuePair nameValuePair = new BasicNameValuePair("param",json);
                nvps.add(nameValuePair);
                httpPost.setEntity(new UrlEncodedFormEntity(nvps,encode));
				HttpResponse response = httpclient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					if (encode != null) {
						result = EntityUtils.toString(response.getEntity(), encode);
					} else {
						result = EntityUtils.toString(response.getEntity());
					}
				}
			} catch (ClientProtocolException e) {
				logger.error("ClientProtocolException异常 : " + e);
			} catch (IOException e) {
				logger.error("IOException异常 :  " + e);
				throw new RuntimeException(e);
			} catch (Exception e) {
				logger.error("异常 :  " + e);
				throw new RuntimeException(e);
			}
			
		}
		/** 请求失败处理 */
		return result;
		
	}
	
	public static String sendHttpPost(String url, Map<String, String> paramMap )  {
		return sendHttpPost(url, paramMap, ENCODE);
	}
	
	public static String sendHttpPost(String url, Map<String, String> paramMap, String encode)  {
		encode=encode==null?ENCODE:encode;
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		// 创建httppost
		HttpPost httpPost = null;
		String result = null;
		int count = 5;
		while((count--)>0){ 
			httpPost = new HttpPost(url);
			httpPost.setHeader("token", "test_token");
			try { 
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Entry<String, String> paramEntry : paramMap.entrySet()) {
					NameValuePair nameValuePair=new BasicNameValuePair(paramEntry.getKey(),paramEntry.getValue());
					nvps.add(nameValuePair);
				} 
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				HttpResponse response = httpclient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					if (encode != null) {
						result = EntityUtils.toString(response.getEntity(), encode);
					} else {
						result = EntityUtils.toString(response.getEntity());
					}
				}
			} catch (ClientProtocolException e) {
				logger.error("ClientProtocolException异常 : " + e);
			} catch (IOException e) {
				logger.error("IOException异常 :  " + e);
				throw new RuntimeException(e);
			} catch (Exception e) {
				logger.error("异常 :  " + e);
				throw new RuntimeException(e);
			}
			
		}
		/** 请求失败处理 */
		return result;
		
	}
	
	
	public static String sendHttpPost(String url, HttpEntity httpEntity, String encode) throws Exception {
		encode=encode==null?ENCODE:encode;
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		// 创建httppost
		HttpPost httpPost = null;
		String result = null;
		int count = 5;
		while((count--)>0){ 
			httpPost = new HttpPost(url);
			httpPost.setHeader("token", "test_token");
			try {  
				httpPost.setEntity(httpEntity);
				HttpResponse response = httpclient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					if (encode != null) {
						result = EntityUtils.toString(response.getEntity(), encode);
					} else {
						result = EntityUtils.toString(response.getEntity());
					}
				}
				return result; 
			} catch (ClientProtocolException e) {
				logger.error("ClientProtocolException异常 : " + e);
			} catch (IOException e) {
				logger.error("IOException异常 :  " + e);
				throw e;
			} catch (Exception e) {
				logger.error("异常 :  " + e);
				throw e;
			}
			
		}
		/** 请求失败处理 */
		return result;
		
	}
	
	public static String sendHttpPost(String url, HttpEntity httpEntity, String encode,String token) throws Exception {
		encode=encode==null?ENCODE:encode;
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		// 创建httppost
		HttpPost httpPost = null;
		String result = null;
		int count = 5;
		while((count--)>0){ 
			httpPost = new HttpPost(url);
			httpPost.setHeader("token", token);
			try {  
				httpPost.setEntity(httpEntity);
				HttpResponse response = httpclient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					if (encode != null) {
						result = EntityUtils.toString(response.getEntity(), encode);
					} else {
						result = EntityUtils.toString(response.getEntity());
					}
				}
				return result; 
			} catch (ClientProtocolException e) {
				logger.error("ClientProtocolException异常 : " + e);
			} catch (IOException e) {
				logger.error("IOException异常 :  " + e);
				throw e;
			} catch (Exception e) {
				logger.error("异常 :  " + e);
				throw e;
			}
			
		}
		/** 请求失败处理 */
		return result;
		
	}
}
