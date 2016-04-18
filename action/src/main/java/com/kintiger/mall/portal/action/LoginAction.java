package com.kintiger.mall.portal.action;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.employee.IEmployeeService;
import com.kintiger.mall.api.employee.bo.Employee;
import com.kintiger.mall.api.portal.ICAService;
import com.kintiger.mall.api.portal.bo.ValidateResult;
import com.kintiger.mall.api.shop.IShopService;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;

/**
 * 登录模块.
 * 
 * @author xujiakun
 * 
 */
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 7498561926934442624L;

	private static final String GOTO = "goto";

	private Logger4jExtend logger = Logger4jCollection.getLogger(LoginAction.class);

	private IMemcachedCacheService memcachedCacheService;

	private ICAService caService;

	private IEmployeeService employeeService;

	private IShopService shopService;

	private String passport;

	private String password;

	private String url;

	/**
	 * 登录验证.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "系统登录")
	public String login() {

		ValidateResult result = caService.validateUser(passport, password);

		// 验证失败
		if (ICAService.RESULT_FAILED.equals(result.getResultCode())
			|| ICAService.RESULT_ERROR.equals(result.getResultCode())) {
			this.setFailMessage(result.getMessage());

			setAttribute();

			return "incorrect";
		}

		// 验证通过
		User user = result.getUser();

		HttpSession session = this.getSession();
		session.setAttribute("ACEGI_SECURITY_LAST_PASSPORT", user.getPassport());
		session.setAttribute("ACEGI_SECURITY_LAST_LOGINUSER", user);

		HttpServletResponse response = getServletResponse();
		if (response != null) {
			Cookie ps = new Cookie("PS", user.getPassport());
			// ps.setMaxAge(-1);
			ps.setPath("/");
			ps.setDomain(env.getProperty("domain"));
			response.addCookie(ps);
		}

		setAttribute(session);

		// 判断受雇公司－店铺
		List<Employee> employeeList = employeeService.getEmployeeList(user.getUserId());

		// if null then 注册公司
		if (employeeList == null || employeeList.size() == 0) {
			return "registerCompany";
		}

		String[] companyId = new String[employeeList.size()];
		int i = 0;
		for (Employee employee : employeeList) {
			companyId[i++] = employee.getCompanyId();
		}

		List<Shop> shopList = shopService.getShopList(companyId);

		// if null then 创建店铺
		if (shopList == null || shopList.size() == 0) {
			return "registerShop";
		}

		// 只存在一家店铺 直接进入店铺管理
		if (shopList.size() == 1) {
			Shop shop = shopList.get(0);
			user.setCompanyId(shop.getCompanyId());
			user.setShopId(shop.getShopId());
			session.setAttribute("ACEGI_SECURITY_LAST_LOGINUSER", user);

			// 存在 goto
			HttpServletRequest request = getServletRequest();
			url = request.getParameter(GOTO);

			if (StringUtils.isNotBlank(url)) {
				return "goto";
			}

			return "gotoShop";
		}

		return "selectShop";
	}

	/**
	 * 退出系统.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "系统退出")
	public String logout() {
		HttpSession session = this.getSession();

		// 清空cache中session信息
		try {
			memcachedCacheService.remove(session.getId());
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			// login
			session.removeAttribute("ACEGI_SECURITY_LAST_PASSPORT");
			session.removeAttribute("ACEGI_SECURITY_LAST_LOGINUSER");

			session.invalidate();
		} catch (Exception e) {
			logger.error(e);
		}

		HttpServletResponse response = getServletResponse();
		if (response != null) {
			Cookie j = new Cookie("JSESSIONID", null);
			j.setMaxAge(0);
			j.setPath("/");
			response.addCookie(j);
		}

		return LOGOUT;
	}

	private void setAttribute(HttpSession session) {
		// if 存在 goto then
		HttpServletRequest request = getServletRequest();
		String u = request.getParameter(GOTO);
		if (StringUtils.isNotBlank(u)) {
			session.setAttribute(GOTO, u.trim());
		}
	}

	private void setAttribute() {
		// if 存在 goto then
		HttpServletRequest request = getServletRequest();
		String u = request.getParameter(GOTO);
		if (StringUtils.isNotBlank(u)) {
			request.setAttribute(GOTO, u.trim());
		}
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public ICAService getCaService() {
		return caService;
	}

	public void setCaService(ICAService caService) {
		this.caService = caService;
	}

	public IEmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public IShopService getShopService() {
		return shopService;
	}

	public void setShopService(IShopService shopService) {
		this.shopService = shopService;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
