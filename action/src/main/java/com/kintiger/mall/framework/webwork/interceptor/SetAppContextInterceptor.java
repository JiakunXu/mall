package com.kintiger.mall.framework.webwork.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.framework.context.SecurityContext;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * 设置当前应用上下文的拦截器.
 * 
 * VM中可以使用的变量： currentUser: 当前用户对象； currentUrl: 当前请求的URL，包含请求参数.
 * 
 * @author jacky.chenb
 * 
 */
public class SetAppContextInterceptor implements Interceptor {

	private static final long serialVersionUID = -7498838714747075663L;

	private static final String CURRENT_USER = "currentUser";

	private static final String CURRENT_URL = "currentUrl";

	public void init() {
	}

	public void destroy() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		// 设置当前用户
		ActionContext ctx = ActionContext.getContext();
		ctx.put(CURRENT_USER, SecurityContext.getUser());

		// 设置当前请求的URL
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);

		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			url.append('?');
			url.append(queryString);
		}

		ctx.put(CURRENT_URL, url);

		return invocation.invoke();
	}

}
