package com.kintiger.mall.framework.webwork.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.shop.IShopService;
import com.kintiger.mall.api.shop.bo.Shop;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * 店铺信息.
 * 
 * @author xujiakun
 * 
 */
public class ShopInfoInterceptor implements Interceptor {

	private static final long serialVersionUID = -7498838714747075663L;

	private static final String NONE = "none";

	private IShopService shopService;

	public void init() {
	}

	public void destroy() {
	}

	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation invocation) throws Exception {

		@SuppressWarnings("rawtypes")
		Map session = invocation.getInvocationContext().getSession();
		Shop shop = (Shop) session.get("ACEGI_SECURITY_LAST_SHOP");

		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String alias = request.getParameter("alias");

		// if shop == null then 根据 alias 获取 shop info OR alias is not null and not equals
		if (shop == null || (StringUtils.isNotBlank(alias) && !shop.getUuid().equals(alias.trim()))) {
			shop = shopService.getShopByUUID(alias);
			// if shop is null then 返回店铺不存在
			if (shop == null) {
				return NONE;
			}

			// set shop to session
			session.put("ACEGI_SECURITY_LAST_SHOP", shop);
		}

		// request setAttribute
		setAttribute("uuid", shop.getUuid());

		return invocation.invoke();
	}

	private void setAttribute(String name, Object o) {
		// 获取当前applicationContex
		ActionContext ctx = ActionContext.getContext();
		// Map map = ctx.getSession()
		// 设置当前请求的URL
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		request.setAttribute(name, o);
	}

	public IShopService getShopService() {
		return shopService;
	}

	public void setShopService(IShopService shopService) {
		this.shopService = shopService;
	}

}
