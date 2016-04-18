package com.kintiger.mall.framework.webwork.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.portal.ICAService;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.user.bo.User;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * AuthencationInterceptor.
 * 
 * @author xujiakun
 * 
 */
public class AuthenticationInterceptor implements Interceptor {

	private static final long serialVersionUID = -7498838714747075663L;

	private static final String LOGIN_TIMEOUT = "440";

	private static final String UNAUTHORIZED = "401";

	private static final String M_PORTAL_INDEX = "m_portal_index";

	private ICAService caService;

	public void init() {
	}

	public void destroy() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {

		@SuppressWarnings("rawtypes")
		Map session = invocation.getInvocationContext().getSession();
		User user = (User) session.get("ACEGI_SECURITY_LAST_LOGINUSER");

		if (user == null) {
			String actionName = getActionName();
			// 登录首页 不需要 goto
			if (!"/home.htm".equals(actionName)) {
				setAttribute("goto", getUrl());
			}

			// 判断是否移动设备
			if (isMobile()) {
				Shop shop = (Shop) session.get("ACEGI_SECURITY_LAST_SHOP");
				if (shop != null) {
					setAttribute("uuid", shop.getUuid());
				}
				
				return M_PORTAL_INDEX;
			} else {
				return LOGIN_TIMEOUT;
			}
		}

		return invocation.invoke();

		// if (validateRequest(user.getUserId())) {
		// return invocation.invoke();
		// } else {
		// return UNAUTHORIZED;
		// }
	}

	/**
	 * 判断是否移动终端.
	 * 
	 * @return
	 */
	private boolean isMobile() {
		// 获取当前applicationContex
		ActionContext ctx = ActionContext.getContext();
		// Map map = ctx.getSession()
		// 设置当前请求的URL
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		if (request == null) {
			return false;
		}

		String userAgent = request.getHeader("USER-AGENT");
		if (StringUtils.isBlank(userAgent)) {
			return false;
		}

		if (userAgent.contains("iPhone") || userAgent.contains("Android")) {
			return true;
		}

		return false;
	}

	/**
	 * 验证当前Request是否有权限.
	 * 
	 * @return
	 */
	private boolean validateRequest(String userId) {
		// if getActionUrl 由于menu数据量的问题，验证上存在漏洞 -> like
		String url = getActionName();
		if (StringUtils.isNotBlank(url)) {
			// 1 判断actionName是否属于menu
			// 2 判断当前用户是否拥有该menu权限
			return caService.validateRequest(userId, url);
		}

		return false;
	}

	private String getUrl() {
		// 获取当前applicationContex
		ActionContext ctx = ActionContext.getContext();
		// Map map = ctx.getSession()
		// 设置当前请求的URL
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		StringBuffer url = request.getRequestURL();

		String queryString = request.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			return url.toString() + "?" + queryString;
		}

		return url.toString();
	}

	private void setAttribute(String name, Object o) {
		// 获取当前applicationContex
		ActionContext ctx = ActionContext.getContext();
		// Map map = ctx.getSession()
		// 设置当前请求的URL
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		request.setAttribute(name, o);
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

	/**
	 * actionUrl.
	 * 
	 * @return
	 */
	private String getActionUrl() {
		String actionUrl = null;
		// 获取当前applicationContex
		ActionContext ctx = ActionContext.getContext();
		// Map map = ctx.getSession()
		// 设置当前请求的URL
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		StringBuffer url = request.getRequestURL();
		int index = url.lastIndexOf(request.getContextPath()) + request.getContextPath().length();
		actionUrl = url.substring(index, url.length());

		String queryString = request.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			return actionUrl + "?" + queryString;
		}

		return actionUrl;
	}

	private String getRequetSessionId() {
		// 获取当前applicationContex
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		return request.getRequestedSessionId();
	}

	@SuppressWarnings("rawtypes")
	private Map getRequestParam() {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		return request.getParameterMap();
	}

	public ICAService getCaService() {
		return caService;
	}

	public void setCaService(ICAService caService) {
		this.caService = caService;
	}

}
