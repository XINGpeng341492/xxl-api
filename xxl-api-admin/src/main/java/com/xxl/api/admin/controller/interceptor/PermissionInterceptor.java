package com.xxl.api.admin.controller.interceptor;

import com.xxl.api.admin.controller.annotation.PermessionLimit;
import com.xxl.api.admin.core.model.XxlApiUser;
import com.xxl.api.admin.service.IXxlApiUserService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.xxl.api.admin.service.impl.XxlApiUserServiceImpl.LOGIN_IDENTITY_KEY;

/**
 * 权限拦截
 * @author xuxueli 2015-12-12 18:09:04
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private IXxlApiUserService xxlApiUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}

		boolean ifLimit = true;
		HandlerMethod method = (HandlerMethod)handler;
		PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
		if (permission != null && !permission.limit()) {
			ifLimit = false;
		}

		XxlApiUser loginUser = xxlApiUserService.ifLogin(request);
		request.setAttribute(LOGIN_IDENTITY_KEY, loginUser);

		if (ifLimit && loginUser == null) {
			response.sendRedirect("/toLogin");
			//request.getRequestDispatcher("/toLogin").forward(request, response);
			return false;
		}
		
		return super.preHandle(request, response, handler);
	}
	
}
