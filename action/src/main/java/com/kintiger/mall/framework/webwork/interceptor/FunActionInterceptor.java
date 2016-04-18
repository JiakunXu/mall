package com.kintiger.mall.framework.webwork.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.function.IFunctionService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.annotation.FunAction;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * FunActionInterceptor.
 * 
 * @author xujiakun
 * 
 */
public class FunActionInterceptor implements Interceptor {

	private static final long serialVersionUID = -57833731348869514L;

	private static final String UNAUTHORIZED = "401";

	private IMemcachedCacheService memcachedCacheService;

	private IFunctionService functionService;

	public void init() {
	}

	public void destroy() {
	}

	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation invocation) throws Exception {

		Object action = invocation.getAction();
		Method method = null;
		try {
			method = action.getClass().getDeclaredMethod(invocation.getProxy().getMethod(), new Class[0]);
		} catch (Exception e) {
			method =
				action.getClass().getDeclaredMethod(
					"do" + invocation.getProxy().getMethod().substring(0, 1).toUpperCase()
						+ invocation.getProxy().getMethod().substring(1), new Class[0]);
		}

		FunAction funAction = method.getAnnotation(FunAction.class);
		if (funAction != null) {
			@SuppressWarnings("rawtypes")
			Map session = invocation.getInvocationContext().getSession();
			User user = (User) session.get("ACEGI_SECURITY_LAST_LOGINUSER");
			if (user == null) {
				return invocation.invoke();
			}

			String alias = funAction.alias();

			// alias 为空时 获取当前页面中所有权限点
			if (StringUtils.isBlank(alias)) {
				// 添加 cache
				Map<String, Boolean> map = functionService.getFunAction(user.getUserId(), getActionName());
				session.put("PERMISSION", map);

				return invocation.invoke();
			}

			// alias 不为空时 验证当前用户是否拥有操作权限
			boolean b = functionService.validateFunAction(user.getUserId(), getActionName(), alias);
			if (!b) {
				return UNAUTHORIZED;
			}
		}

		return invocation.invoke();
	}

	/**
	 * actionName.
	 * 
	 * @return
	 */
	private String getActionName() {
		String actionName = null;
		// 获取当前applicationContex
		ActionContext ctx = ActionContext.getContext();
		// Map map = ctx.getSession()
		// 设置当前请求的URL
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		StringBuffer url = request.getRequestURL();
		int index = url.lastIndexOf(request.getContextPath()) + request.getContextPath().length();
		actionName = url.substring(index, url.length());
		return actionName;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IFunctionService getFunctionService() {
		return functionService;
	}

	public void setFunctionService(IFunctionService functionService) {
		this.functionService = functionService;
	}

}
