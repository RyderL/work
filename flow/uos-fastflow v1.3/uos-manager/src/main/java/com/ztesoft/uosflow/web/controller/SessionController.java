package com.ztesoft.uosflow.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zterc.uos.base.helper.GsonHelper;

@Controller
public class SessionController {
	@Autowired 
	private HttpSession session;
	
	@SuppressWarnings("all")
	@RequestMapping(value = "/session/getSession.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")  
	public @ResponseBody String getSession(HttpServletRequest request)
	{
		Enumeration<String> enumeration = (Enumeration<String>)session.getAttributeNames();
		Map<String, Object> map = new HashMap<String, Object>();
		while(enumeration.hasMoreElements()){
			String keyString = enumeration.nextElement();
			map.put(keyString, session.getAttribute(keyString));
		}
		return GsonHelper.toJson(map);
	}
}
