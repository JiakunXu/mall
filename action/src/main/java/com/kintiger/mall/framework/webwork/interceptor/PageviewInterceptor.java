package com.kintiger.mall.framework.webwork.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import javax.servlet.http.HttpServletRequest;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.ip.IIPService;
import com.kintiger.mall.api.pageview.bo.Pageview;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.ClientUtil;
import com.kintiger.mall.framework.util.DateUtil;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * PageviewInterceptor.
 * 
 * @author xujiakun
 * 
 */
public class PageviewInterceptor implements Interceptor {

	private static final long serialVersionUID = -8251011587472392420L;

	private Logger4jExtend logger = Logger4jCollection.getLogger(PageviewInterceptor.class);

	private IMemcachedCacheService memcachedCacheService;

	private IIPService ipService;

	public void init() {
	}

	public void destroy() {
	}

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

		com.kintiger.mall.framework.annotation.Pageview pv =
			method.getAnnotation(com.kintiger.mall.framework.annotation.Pageview.class);
		if (pv != null) {
			@SuppressWarnings("rawtypes")
			Map session = invocation.getInvocationContext().getSession();
			Shop shop = (Shop) session.get("ACEGI_SECURITY_LAST_SHOP");
			if (shop == null) {
				return invocation.invoke();
			}

			Pageview pageview = new Pageview();
			pageview.setShopId(shop.getShopId());
			pageview.setActionName(getActionName());
			pageview.setParameter(getQueryString());

			final String ip = getIpAddr();
			pageview.setIp(ip);
			pageview.setCreateDate(DateUtil.getNowDatetimeStr());
			pageview.setModifyUser("system");

			try {
				@SuppressWarnings("unchecked")
				List<Pageview> pageviews =
					(List<Pageview>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_PAGEVIEW);

				if (pageviews == null || pageviews.size() == 0) {
					pageviews = new ArrayList<Pageview>();
				}

				pageviews.add(pageview);

				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_PAGEVIEW, pageviews,
					IMemcachedCacheService.CACHE_KEY_PAGEVIEW_DEFAULT_EXP);
			} catch (Exception e) {
				logger.error(e);
			}

			// 异步验证更新 IP 信息
			ExecutorService service = null;
			try {
				service = Executors.newFixedThreadPool(1);
			} catch (IllegalArgumentException e) {
				logger.error(e);
			}

			if (service != null) {
				try {
					service.execute(new Runnable() {
						public void run() {
							ipService.validate(ip);
						}
					});
				} catch (RejectedExecutionException e) {
					logger.error(e);
				} catch (Exception e) {
					logger.error(e);
				}

				try {
					service.shutdown();
				} catch (SecurityException e) {
					logger.error(e);
				} catch (Exception e) {
					logger.error(e);
				}
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

	private String getQueryString() {
		// 获取当前applicationContex
		ActionContext ctx = ActionContext.getContext();
		// Map map = ctx.getSession()
		// 设置当前请求的URL
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		return request.getQueryString();
	}

	private String getIpAddr() {
		// 获取当前applicationContex
		ActionContext ctx = ActionContext.getContext();
		// Map map = ctx.getSession()
		// 设置当前请求的URL
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		return ClientUtil.getIpAddr(request);
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IIPService getIpService() {
		return ipService;
	}

	public void setIpService(IIPService ipService) {
		this.ipService = ipService;
	}

}
