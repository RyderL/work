package com.ztesoft.uosflow.web.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 
 * @ClassName: LoginFilter
 * @Description: 登录过滤器
 * @author shaojian.yu
 * @date 2014年11月3日 下午1:19:28
 * 
 */
public class LoginFilter extends OncePerRequestFilter  {
	/**
	 * 
	 * Title：doFilter Description: 所有请求都走此过滤器来判断用户是否登录 user: shaojian.yu date:
	 * 2014 2014年11月3日
	 * 
	 * @param servletRequest
	 * @param servletResponse
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, FilterChain filterChain)
			throws ServletException, IOException {
		// 获得在下面代码中要用的request,response,session对象
		HttpSession session = httpRequest.getSession(true);

		// 路径中包含这些字符串的,可以不用登录直接访问
		String[] strs = {"code.do","login.do","login.html","flowInst.html","ssologin.do"}; 
		String[] methods = {"qryProcessInstanceForTrace","qryProcessDefineForTrace","qryPackageDefinePath","qryUndoActivityByCond","processInstanceJump","processInstanceJumpForServer","qryAreaIdByProcessInstId"};
		StringBuffer url = httpRequest.getRequestURL();

		/**
		 * 过滤掉根目录
		 */
		String path = httpRequest.getContextPath();
		String protAndPath = httpRequest.getServerPort() == 80 ? "" : ":"
				+ httpRequest.getServerPort();
		String basePath = httpRequest.getScheme() + "://"
				+ httpRequest.getServerName() + protAndPath + path + "/";
		if (basePath.equalsIgnoreCase(url.toString())) {
			filterChain.doFilter(httpRequest, httpResponse);
			return;
		}

		// 特殊用途的路径可以直接访问
		if (strs != null && strs.length > 0) {
			for (String str : strs) {
				if (url.indexOf(str) >= 0) {
					filterChain.doFilter(httpRequest, httpResponse);
					return;
				}
			}
		}
		// 从session中获取用户信息
		String loginInfo = (String) session.getAttribute("username");
		if (null != loginInfo && !"".equals(loginInfo)
				&& !"null".equals(loginInfo)) {
			// 用户存在,可以访问此地址
			filterChain.doFilter(httpRequest, httpResponse);
		} else {
			if(url.indexOf("call.do")>-1){
				String methodName = httpRequest.getParameter("method");
				for (String str : methods) {
					if (methodName.equals(str)) {
						filterChain.doFilter(httpRequest, httpResponse);
						return;
					}
				}
			}
			// 用户不存在,踢回登录页面
			String returnUrl = httpRequest.getContextPath() + "/login.html";
			httpRequest.setCharacterEncoding("UTF-8");
			httpResponse.setContentType("text/html; charset=UTF-8"); // 转码
			httpResponse
					.getWriter()
					.println(
							"<script language=\"javascript\">alert(\"您还没有登录，请先登录!\");if(window.opener==null){window.top.location.href=\""
									+ returnUrl
									+ "\";}else{window.opener.top.location.href=\""
									+ returnUrl
									+ "\";window.close();}</script>");
			return;
		}
	}
}
