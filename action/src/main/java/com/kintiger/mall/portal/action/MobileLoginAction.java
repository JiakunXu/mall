package com.kintiger.mall.portal.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.member.IMemberService;
import com.kintiger.mall.api.portal.ICAService;
import com.kintiger.mall.api.portal.bo.ValidateResult;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;

/**
 * 登录模块.
 * 
 * @author xujiakun
 * 
 */
public class MobileLoginAction extends BaseAction {

	private static final long serialVersionUID = 7498561926934442624L;

	private static final String GOTO = "goto";

	private Logger4jExtend logger = Logger4jCollection.getLogger(MobileLoginAction.class);

	private IMemcachedCacheService memcachedCacheService;

	private ICAService caService;

	private IMemberService memberService;

	private String passport;

	private String password;

	private String url;

	private String uuid;

	/**
	 * 登录验证.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "移动设备系统登录")
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

		// 判断当前用户 是否 已成为会员
		BooleanResult res = memberService.validateMember(getShop().getShopId(), user.getUserId());
		if (!res.getResult()) {
			this.setFailMessage("会员信息获取失败！");

			return "incorrect";
		}

		// 保存 会员信息
		user.setMemId(res.getCode());

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

		HttpServletRequest request = getServletRequest();
		url = request.getParameter(GOTO);

		return "goto";
	}

	/**
	 * 退出系统.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "系统退出")
	public String logout() {
		// 退出 -> 店铺首页
		uuid = this.getShop().getUuid();

		HttpSession session = this.getSession();

		try {
			// login
			session.removeAttribute("ACEGI_SECURITY_LAST_PASSPORT");
			session.removeAttribute("ACEGI_SECURITY_LAST_LOGINUSER");
		} catch (Exception e) {
			logger.error(e);
		}

		return LOGOUT;
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

	public IMemberService getMemberService() {
		return memberService;
	}

	public void setMemberService(IMemberService memberService) {
		this.memberService = memberService;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
