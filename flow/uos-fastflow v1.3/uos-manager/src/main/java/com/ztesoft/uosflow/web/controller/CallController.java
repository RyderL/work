package com.ztesoft.uosflow.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zterc.uos.base.bean.BeanContextProxy;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.StringHelper;


@Controller
public class CallController {
	private static Logger logger = LoggerFactory.getLogger(CallController.class);
	
	//缓存serviceBean对象
	private Map<String, Object> serviceMap = new HashMap<String, Object>();
	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/call/call.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String callAsyn(HttpServletRequest request){
		String param = request.getParameter("param");//json格式的参数
		String bean =  request.getParameter("bean");
		String methodName =  request.getParameter("method");
		Object object = serviceMap.get(bean);
		if(object==null){
			object = BeanContextProxy.getBean(bean);
			serviceMap.put(bean,object);
		}
		logger.info("bean:"+bean+";method:"+methodName+";params:"+param);
		Map<String, Object> map = GsonHelper.toMap(param);
		logger.info("map:"+map);
		String errorMsg = "";
		String userAreaId = StringHelper.valueOf(request.getSession().getAttribute("areaId"));
		try {
			map.put("userAreaId", userAreaId);
			Method method = object.getClass().getMethod(methodName, Map.class);
			try {
				String result = (String)method.invoke(object,map);
				
				logger.info("返回结果："+result);
				return result;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				errorMsg = e.getMessage();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				errorMsg = e.getMessage();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				errorMsg = e.getMessage();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			errorMsg = e.getMessage();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			errorMsg = e.getMessage();
		}
		return "error:"+errorMsg;
	}
}
