package com.ztesoft.uosflow.web.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zterc.uos.fastflow.dto.specification.StaffDto;
import com.ztesoft.uosflow.web.service.staff.StaffServ;

/**
 * 
 * µÇÂ¼¿ØÖÆÆ÷
 * @author gong.yi
 *
 */
@Controller
public class LoginController {
	
	@Autowired
	private StaffServ staffService;
	
	@RequestMapping(value="/login.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String login(HttpServletRequest request)
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
//		String certcode = request.getParameter("certcode");
		
		HttpSession session = request.getSession();
		
//		if(!certcode.equalsIgnoreCase((String)session.getAttribute("code")))
//		{
//			return "certCodeIncorrect";
//		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
//		System.out.println("---µ±Ç°ip:"+ip);
		boolean isIpOk = staffService.isIpOk(ip);
		if(isIpOk){
			StaffDto staff = staffService.queryStaffByUsername(username);
			if(staff.getUserName()==null)
			{
				return "usernameorpasswordIncorrect";
			}
			if(staff.getPassword().equals(password))
			{
				session.setAttribute("username", staff.getUserName());
				session.setAttribute("staffId", staff.getStaffId());
				session.setAttribute("staffName",staff.getStaffName());
				session.setAttribute("systemCode",staff.getSystemCode());
				session.setAttribute("areaId",staff.getAreaId());
				return "Y";
			}else{
				return "usernameorpasswordIncorrect";
			}
		}else {
			return "ipIncorrect";
		}
	}
	
	@SuppressWarnings("all")
	@RequestMapping(value="/logout.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody void logout(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		Enumeration<String> enumeration = (Enumeration<String>)session.getAttributeNames();
		while(enumeration.hasMoreElements()){
			String keyString = enumeration.nextElement();
			session.removeAttribute(keyString);
		}
	}
}
