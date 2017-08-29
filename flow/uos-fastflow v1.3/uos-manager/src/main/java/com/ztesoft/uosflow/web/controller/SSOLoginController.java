package com.ztesoft.uosflow.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zterc.uos.fastflow.dto.specification.StaffDto;
import com.ztesoft.uosflow.web.service.staff.StaffServ;
//import com.ztesoft.uosflow.web.util.GsonUtil;

/**
 * 单点登录控制器
 */
@Controller
public class SSOLoginController {
	private static Logger logger = LoggerFactory.getLogger(SSOLoginController.class);
	private static final HttpHeaders HTTP_HEADERS;
	static{
		HTTP_HEADERS = new HttpHeaders();
		HTTP_HEADERS.set("Pragma", "no-cache");
		HTTP_HEADERS.set("Cache-Control", "no-cache");
		HTTP_HEADERS.setDate("Expires", 0);
		HTTP_HEADERS.setContentType(MediaType.TEXT_HTML);
	}
	
	@Autowired
	private StaffServ staffService;
	@RequestMapping(value = "/ssologin.do", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody ResponseEntity<String> ssoLogin(HttpServletRequest request, HttpServletResponse response){
		String account = request.getParameter("account");//登录用户名
		String gotoUrl = request.getParameter("goto");//需要集成的页面地址
		HttpSession session = request.getSession();
		logger.info("---account:"+account);
		logger.info("---gotoUrl:"+gotoUrl);
//		logger.info("---session:"+GsonUtil.toJson(session));//Jetty报错
		StaffDto staff = staffService.queryStaffByUsername(account);
		if(staff.getUserName()==null||gotoUrl==null){
			String returnUrl = request.getContextPath() + "/login.html";
			return new ResponseEntity<String>("<script>alert('ssoLogoin Fail');" +
					"window.location.href='" + returnUrl+
					"'</script>",HTTP_HEADERS,HttpStatus.OK);
		}else{
			//登陆
			String returnUrl = request.getContextPath() + gotoUrl;
			session.setAttribute("username", staff.getUserName());
			session.setAttribute("staffId", staff.getStaffId());
			session.setAttribute("staffName",staff.getStaffName());
			session.setAttribute("systemCode",staff.getSystemCode());
			session.setAttribute("areaId",staff.getAreaId());
			
			return new ResponseEntity<String>("<script>window.location.href='" + returnUrl+
					"'</script>",HTTP_HEADERS,HttpStatus.OK);
		}
	}
}
