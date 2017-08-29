package com.zterc.uos.base.cache.zcache;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.cache.UosCacheClient;
import com.zterc.uos.base.helper.StringHelper;
import com.ztesoft.zcache.client.IZcacheClient;
import com.ztesoft.zcache.client.ZcacheFactory;
import com.ztesoft.zcache.client.etc.ZcacheConfig;
import com.ztesoft.zcache.client.result.DataEntry;
import com.ztesoft.zcache.client.result.Result;
import com.ztesoft.zcache.client.result.ResultCode;

public class UosZcacheClient  implements UosCacheClient {

	private Logger logger = LoggerFactory
			.getLogger(UosZcacheClient.class);

	protected ZcacheConfig zcacheConfig;
	private Long expireTime;

	public ZcacheConfig getZcacheConfig() {
		return zcacheConfig;
	}

	public void setZcacheConfig(ZcacheConfig zcacheConfig) {
		this.zcacheConfig = zcacheConfig;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public void init(){
		
	}

	public IZcacheClient getClient(){
		IZcacheClient zcacheClient = ZcacheFactory.createCache(zcacheConfig);
		return zcacheClient;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObject(String key, Class<T> clazz, Long route) {
		IZcacheClient zcacheClient = getClient();
		try {
			Result<DataEntry> rt = zcacheClient.get(clazz, key);
			if (ResultCode.SUCCESS.equals(rt.getRc())) {
			   Object obj = rt.getValue().getValue();
			   return (T) obj;
			} else if (ResultCode.DATANOTEXISTS.equals(rt.getRc())) {
			    logger.error("get KEY NIL: " + rt.getRc());
			} else {
			    logger.error("ERROR:" + rt.getRc());
			}
		} catch (Exception e) {
		    logger.error("根据键值key:"+key+"获取缓存对象异常:" + e.getMessage(),e);
		}finally{
			zcacheClient.close();
			zcacheClient = null;
		}
		return null;
	}


	@Override
	public boolean setObject(String key, Object value, Long route) {
		IZcacheClient zcacheClient = getClient();
		try {
			ResultCode rc = zcacheClient.set(key,(Serializable) value,expireTime);
			if (ResultCode.SUCCESS.equals(rc)) {
			   logger.debug("----放入缓存成功-----key:"+key);
			   return true;
			} else {
			    logger.error("ERROR:"+rc);
			}
		} catch (Exception e) {
		    logger.error("对象放入缓存异常:" + e.getMessage(),e);
		}finally{
			zcacheClient.close();
			zcacheClient = null;
		}
		return false;
	}

	@Override
	public boolean delObject(String key, Long route) {
		IZcacheClient zcacheClient = getClient();
		try {
			ResultCode rc = zcacheClient.delete(key);
			if (ResultCode.SUCCESS.equals(rc)) {
			   logger.debug("----删除缓存成功-----key:"+key);
			   return true;
			} else {
			    logger.error("ERROR:"+rc);
			}
		} catch (Exception e) {
		    logger.error("删除key:"+key+"缓存异常:" + e.getMessage(),e);
		}finally{
			zcacheClient.close();
			zcacheClient = null;
		}
		return false;
	}

	@Override
	public boolean set(String key, String value, Long route) {
		IZcacheClient zcacheClient = getClient();
		try {
			ResultCode rc = zcacheClient.set(key, value,expireTime);
			if (ResultCode.SUCCESS.equals(rc)) {
			   logger.debug("----放入缓存成功-----key:"+key);
			   return true;
			} else {
			    logger.error("ERROR:"+rc);
			}
		} catch (Exception e) {
		    logger.error("对象放入缓存异常:" + e.getMessage(),e);
		}finally{
			zcacheClient.close();
			zcacheClient = null;
		}
		return false;
	}

	@Override
	public String get(String key, Long route) {
		IZcacheClient zcacheClient = getClient();
		try {
			Result<DataEntry> rt = zcacheClient.get(key);
			if (ResultCode.SUCCESS.equals(rt.getRc())) {
			   Object obj = rt.getValue().getValue();
			   return StringHelper.valueOf(obj);
			} else if (ResultCode.DATANOTEXISTS.equals(rt.getRc())) {
			    logger.error("get KEY NIL: " + rt.getRc());
			} else {
			    logger.error("ERROR:" + rt.getRc());
			}
		} catch (Exception e) {
		    logger.error("根据键值key:"+key+"获取缓存对象异常:" + e.getMessage(),e);
		}finally{
			zcacheClient.close();
			zcacheClient = null;
		}
		return null;
	}

	@Override
	public boolean del(String key, Long route) {
		IZcacheClient zcacheClient = getClient();
		try {
			ResultCode rc = zcacheClient.delete(key);
			if (ResultCode.SUCCESS.equals(rc)) {
			   logger.debug("----删除缓存成功-----key:"+key);
			   return true;
			} else {
			    logger.error("ERROR:"+rc);
			}
		} catch (Exception e) {
		    logger.error("删除缓存key:"+key+"异常:" + e.getMessage(),e);
		}finally{
			zcacheClient.close();
			zcacheClient = null;
		}
		return false;
	}

	@Override
	public boolean exist(String key, Long route) {
		IZcacheClient zcacheClient = getClient();
		try {
			Result<Boolean> bresult = zcacheClient.exists(key);
			if (ResultCode.SUCCESS.equals(bresult.getRc())) {
			   return true;
			} else {
			    logger.error("指定键key:"+key+"不存在，ERROR:"+bresult.getRc());
			}
		} catch (Exception e) {
		    logger.error("查看指定键是否存在出现异常:" + e.getMessage(),e);
		}finally{
			zcacheClient.close();
			zcacheClient = null;
		}
		return false;
	}

	@Override
	public boolean ping() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clearAll(Long route) {
		// TODO Auto-generated method stub
		
	}

}
